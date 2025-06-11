package kevin.module.modules.render;

import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import org.jetbrains.annotations.NotNull;


public final class TrueSight extends ClientModule {
    @NotNull
    private final BooleanValue barriersValue = new BooleanValue("Barriers", true);
    @NotNull
    private final BooleanValue entitiesValue = new BooleanValue("Entities", true);

    public TrueSight() {
        super("TrueSight", "Allows you to see invisible entities and barriers.", ModuleCategory.RENDER);
    }

    @NotNull
    public BooleanValue getBarriersValue() {
        return this.barriersValue;
    }

    @NotNull
    public BooleanValue getEntitiesValue() {
        return this.entitiesValue;
    }
}
