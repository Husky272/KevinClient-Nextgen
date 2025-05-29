package kevin.via;

import com.viaversion.viaversion.api.Via;
import de.gerrygames.viarewind.api.ViaRewindPlatform;

import java.io.File;
import java.util.logging.Logger;

public final class RewindLoader implements ViaRewindPlatform {

    private Logger logger;

    private File file;

    public RewindLoader(File file) {
        this.file = new File(file.toURI()).toPath().resolve("ViaRewind").resolve("config.yml").toFile();
        logger = Via.getPlatform().getLogger();
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
