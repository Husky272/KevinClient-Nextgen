package kevin.script;

import kevin.main.KevinClient;
import kevin.plugin.PluginManager;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ScriptLoader {

    /**
     * Loads scripts by:
     * <p>Checking for script files in the designated directory</p>
     * <p>Ensuring the required PyScript.jar is available (copying it if needed)</p>
     * <p>Loading the plugin using PluginManager</p>
     */
    public static void load() {
        // Get the directory where scripts are stored from KevinClient's file manager
        File dir = KevinClient.fileManager.scripts;

        // If the scripts folder doesn't exist, there's nothing to load
        if (!dir.exists()) return;

        // List all files in the scripts directory
        File[] files = dir.listFiles();

        // If no files are found, log a message and exit
        if (files == null || files.length == 0) {
            Minecraft.logger.info("[ScriptManager] There is no script to load");
            return;
        }

        // Define the target location for the PyScript library (a JAR file)
        File libFile = new File(KevinClient.fileManager.libraries, "PyScript.jar");

        // If the PyScript.jar doesn't exist yet, we need to copy it from resources
        if (!libFile.exists()) {
            try (InputStream in = ScriptLoader.class.getResourceAsStream("/PyScript.jar")) {
                // Check if the resource exists
                if (in == null) {
                    throw new RuntimeException("Failed to find PyScript.jar in resources!");
                }

                // Copy the embedded resource to the target file location
                Files.copy(in, libFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                // Wrap any I/O error into a runtime exception and throw
                throw new RuntimeException("Failed to copy PyScript lib!", e);
            }
        }

        // Double-check that the file was successfully copied
        if (!libFile.exists()) {
            throw new RuntimeException("Failed to copy PyScript lib!");
        }

        // Load the plugin using the PluginManager with the PyScript.jar
        try {
            PluginManager.load(libFile);
        } catch (IOException e) {
            Minecraft.logger.error("[ScriptManager] Script failed to load: " + e.getStackTrace()[0] + " because " + e.getCause());
        }

        // Log that the script loading process was successful
        Minecraft.logger.info("[ScriptManager] Script loaded");
    }
}