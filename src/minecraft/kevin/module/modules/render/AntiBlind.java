package kevin.module.modules.render;

import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

public final class AntiBlind extends ClientModule {
    @NotNull
    private final BooleanValue confusionEffect = new BooleanValue("Confusion", true);
    @NotNull
    private final BooleanValue pumpkinEffect = new BooleanValue("Pumpkin", true);
    @NotNull
    private final BooleanValue fireEffect = new BooleanValue("Fire", false);

    public AntiBlind() {
        super("AntiBlind", "Cancels blindness effects.", ModuleCategory.RENDER);
    }

    @NotNull
    public final BooleanValue getConfusionEffect() {
        return this.confusionEffect;
    }

    @NotNull
    public final BooleanValue getPumpkinEffect() {
        return this.pumpkinEffect;
    }

    @NotNull
    public final BooleanValue getFireEffect() {
        return this.fireEffect;
    }
}
