package kevin.via;


import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.libs.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

public final class Injector implements ViaInjector {
    public void inject() {
    }

    public void uninject() {
    }

    public int getServerProtocolVersion() {
        return ViaVersion.getCLIENT_VERSION();
    }

    @NotNull
    public String getEncoderName() {
        return "via-encoder";
    }

    @NotNull
    public String getDecoderName() {
        return "via-decoder";
    }

    @NotNull
    public JsonObject getDump() {
        return new JsonObject();
    }
}
