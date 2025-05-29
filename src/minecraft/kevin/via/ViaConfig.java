package kevin.via;

import com.viaversion.viaversion.configuration.AbstractViaConfig;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ViaConfig extends AbstractViaConfig {


    public static final String[] UNSUPPORTED_OPTIONS = {
            "anti-xray-patch",
            "bungee-ping-interval",
            "bungee-ping-save",
            "bungee-servers",
            "quick-move-action-fix",
            "nms-player-ticking",
            "velocity-ping-interval",
            "velocity-ping-save",
            "velocity-servers",
            "blockconnection-method",
            "change-1_9-hitbox",
            "change-1_14-hitbox"
    };

    public ViaConfig(File configFile) {
        super(configFile);
    }

    @Override
    public java.net.URL getDefaultConfigURL() {
        return this.getClass().getClassLoader().getResource("assets/viaversion/config.yml");
    }

    @Override
    public void handleConfig(Map<String, Object> config) {
    }

    @Override
    public List<String> getUnsupportedOptions() {
        return Arrays.asList(UNSUPPORTED_OPTIONS);
    }

    @Override
    // 我操你妈？
    public boolean isAntiXRay() {
        return false;
    }

    @Override
    public boolean isNMSPlayerTicking() {
        return false;
    }

    @Override
    public boolean is1_12QuickMoveActionFix() {
        return false;
    }

    @Override
    public String getBlockConnectionMethod() {
        return "packet";
    }

    @Override
    public boolean is1_9HitboxFix() {
        return false;
    }

    @Override
    public boolean is1_14HitboxFix() {
        return false;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
    }
}