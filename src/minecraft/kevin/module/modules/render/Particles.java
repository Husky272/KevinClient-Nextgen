package kevin.module.modules.render;

import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.FloatValue;
import kevin.module.ModuleCategory;
import org.jetbrains.annotations.NotNull;

public final class Particles extends ClientModule {
    @NotNull
    public static final Particles INSTANCE = new Particles();
    @NotNull
    private static final BooleanValue noCriticalParticlesValue = new BooleanValue("NoCriticalParticles", true);
    @NotNull
    private static final BooleanValue noCriticalParticlesFromServerValue = new BooleanValue("NoCriticalParticlesFromServer", true);
    @NotNull
    private static final BooleanValue noSharpParticlesValue = new BooleanValue("NoSharpParticles", true);
    @NotNull
    private static final BooleanValue noSharpParticlesFromServerValue = new BooleanValue("NoSharpParticlesFromServer", true);
    @NotNull
    private static final FloatValue blockParticleSpeedValue = new FloatValue("BlockParticleSpeed", 0.4F, 0.4F, 1.5F);
    @NotNull
    private static final FloatValue otherParticleSpeedValue = new FloatValue("OtherParticleSpeed", 0.4F, 0.4F, 1.5F);

    private Particles() {
        super("Particles", "Particles control.", ModuleCategory.RENDER);
    }

    public boolean getNoCriticalParticles() {
        return this.getState() && noCriticalParticlesValue.get();
    }

    public boolean getNoCriticalParticlesFromServer() {
        return this.getState() && noCriticalParticlesFromServerValue.get();
    }

    public boolean getNoSharpParticles() {
        return this.getState() && noSharpParticlesValue.get();
    }

    public boolean getNoSharpParticlesFromServer() {
        return this.getState() && noSharpParticlesFromServerValue.get();
    }

    public float getBlockParticleSpeed() {
        return blockParticleSpeedValue.get().floatValue();
    }

    public float getOtherParticleSpeed() {
        return otherParticleSpeedValue.get().floatValue();
    }
}
