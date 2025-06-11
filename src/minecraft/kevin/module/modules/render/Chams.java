package kevin.module.modules.render;

import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

public final class Chams extends ClientModule {
    @NotNull
    public static final Chams INSTANCE = new Chams();
    @NotNull
    private static final BooleanValue targetsValue = new BooleanValue("Targets", true);
    @NotNull
    private static final BooleanValue chestsValue = new BooleanValue("Chests", true);
    @NotNull
    private static final BooleanValue itemsValue = new BooleanValue("Items", true);

    private Chams() {
        super("Chams", "Allows you to see targets through blocks.", ModuleCategory.RENDER);
    }

    @NotNull
    public final BooleanValue getTargetsValue() {
        return targetsValue;
    }

    @NotNull
    public final BooleanValue getChestsValue() {
        return chestsValue;
    }

    @NotNull
    public final BooleanValue getItemsValue() {
        return itemsValue;
    }
}
