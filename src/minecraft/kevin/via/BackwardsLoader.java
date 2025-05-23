// Decompiled with: CFR 0.152
// Class Version: 8
package kevin.via;

import com.viaversion.viabackwards.api.ViaBackwardsPlatform;
import java.io.File;
import java.util.logging.Logger;
import kevin.via.ViaVersion;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BackwardsLoader
        implements ViaBackwardsPlatform {
    @NotNull
    private final File file;

    public BackwardsLoader(@Nullable File fileIn) {
        this.file = new File(fileIn, "ViaBackwards");
        this.init(this.file);
    }

    @NotNull
    public Logger getLogger() {
        return ViaVersion.INSTANCE.getJLogger();
    }

    public void disable() {
    }

    public boolean isOutdated() {
        return false;
    }

    @NotNull
    public File getDataFolder() {
        return new File(this.file, "config.yml");
    }
}
