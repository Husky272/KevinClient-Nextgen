package kevin.depends;

import kevin.main.KevinClient;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * LibraryManager is a utility class that manages the loading of external libraries
 * </br>
 * It downloads and load libraries from Maven repositories when they are needed.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class LibraryManager {
    private static final HashMap<URLClassLoader, URLClassLoaderAccess> URL_INJECTORS = new HashMap<>();
    public static void loadDependency(MavenDependency dependency, URLClassLoader classLoader) {
        String name = dependency.getArtifactId() + "-" + dependency.getVersion();

        URLClassLoaderAccess URL_INJECTOR = URL_INJECTORS.get(classLoader);
        if (URL_INJECTOR == null) {
            URL_INJECTORS.put(classLoader, URL_INJECTOR = URLClassLoaderAccess.create(classLoader));
        }

        Logger logger = Minecraft.logger;
        File saveLocation = new File(getLibFolder(), name + ".jar");
        if (!saveLocation.exists()) {
            File tempLocation = new File(getLibFolder(), name + ".temp");
            try {
                mavenMirror(dependency);
                logger.info("Dependency '" + name + "' is not already in the libraries folder. Attempting to download...");
                String title = Display.getTitle();
                URL url = dependency.getUrl();
                URLConnection urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                String base = String.format("Downloading dependency: %s:%s from maven ", dependency.getArtifactId(), dependency.getVersion());
                Display.setTitle(base + "(" + dependency.getRepoUrl() + " | no connection) ");
                final AtomicLong count = new AtomicLong(0);
                AtomicBoolean done = new AtomicBoolean(false);
                new Thread(() -> {
                    try (InputStream is = urlConnection.getInputStream();
                         FileOutputStream stream = new FileOutputStream(tempLocation)) {
                        logger.info("connected to " + url);
//                    double total = urlConnection.getContentLengthLong() / 1024.0 / 1024.0;

                        int i;
                        while ((i = is.read()) != -1) {
                            stream.write(i);
                            count.incrementAndGet();
                        }
                        stream.flush();
                        if (!tempLocation.renameTo(saveLocation)) {
                            Files.copy(tempLocation.toPath(), saveLocation.toPath());
                            tempLocation.delete();
                        }
                    } catch (IOException e) {
                        Minecraft.logger.warn(e.getMessage());
                    }
                    done.set(true);
                });

                int c = 0;
                double last = 0;
                while (!done.get()){
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception ignored) {}
                    String e = "-";
                    switch (++c % 4) {
                        case 0: e = "\\"; break;
                        case 1: e = "|"; break;
                        case 2: e = "/"; break;
                        case 3: e = "-"; break;
                    }
                    double downloaded = count.get() / 1024.0 / 1024.0;
                    Display.setTitle(String.format("%s %s (%.3f MB) (%.3f MB/s)", base, e, downloaded, downloaded - last));
                    last = downloaded;
                }
                Display.setTitle(title);
            } catch (Exception e) {
                Minecraft.logger.warn(e.getMessage());
            }

            logger.info("Dependency '" + name + "' successfully downloaded.");
        }

        if (!saveLocation.exists()) {
            throw new RuntimeException("Unable to download dependency: " + dependency);
        }

        try {
            URL_INJECTOR.addURL(saveLocation.toURI().toURL());
        } catch (Exception e) {
            throw new RuntimeException("Unable to load dependency: " + saveLocation, e);
        }

        logger.info("Loaded dependency '" + name + "' successfully.");
    }

    private static void mavenMirror(MavenDependency dependency) {
        if ("CN".equalsIgnoreCase(System.getProperty("user.country")))
//            dependency.setRepoUrl("https://maven.aliyun.com/repository/central");
            dependency.setRepoUrl("https://repo.huaweicloud.com/repository/maven");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File getLibFolder() {
        File libraries = KevinClient.fileManager.libraries;
        if (!libraries.exists()) {
            libraries.mkdir();
        }
        return libraries;
    }
}
