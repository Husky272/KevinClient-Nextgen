package kevin.module.modules.movement.speeds;

import java.util.List;
import kevin.event.BlockBBEvent;
import kevin.event.MoveEvent;
import kevin.event.PacketEvent;
import kevin.event.UpdateEvent;
import kevin.main.KevinClient;
import kevin.module.Value;
import kevin.module.modules.movement.Speed;
import kevin.utils.MinecraftInstance;
import kevin.utils.reflection.ClassUtils;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;


public abstract class SpeedMode extends MinecraftInstance {
    @NotNull
    private final String modeName;
    @NotNull
    private final String valuePrefix;
    @NotNull
    private final Lazy<Speed> speed$delegate;

    public SpeedMode(@NotNull String modeName) {
        super();
        this.modeName = modeName;
        this.valuePrefix = this.modeName + '-';
        this.speed$delegate = LazyKt.lazy(SpeedMode::speed_delegate$lambda$0);
    }

    @NotNull
    public final String getModeName() {
        return this.modeName;
    }

    @NotNull
    protected final String getValuePrefix() {
        return this.valuePrefix;
    }

    @NotNull
    protected final Speed getSpeed() {
        return (Speed) this.speed$delegate.getValue();
    }

    @NotNull
    public final List<Value<?>> getValues() {

        return ClassUtils.getValues(this.getClass(), this);
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onMove(@NotNull MoveEvent event) {
    }

    public void onUpdate(@NotNull UpdateEvent event) {
    }

    public void onPacket(@NotNull PacketEvent event) {
    }

    public void onPreMotion() {
    }

    public void onBlockBB(@NotNull BlockBBEvent event) {
    }

    private static final Speed speed_delegate$lambda$0() {
        return (Speed)KevinClient.INSTANCE.getModuleManager().getModule(Speed.class);
    }
}
