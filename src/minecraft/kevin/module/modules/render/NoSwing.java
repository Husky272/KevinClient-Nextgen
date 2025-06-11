package kevin.module.modules.render;

import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

public final class NoSwing extends ClientModule {
    @NotNull
    private final BooleanValue serverSideValue = new BooleanValue("ServerSide", true);

    public NoSwing() {
        super("NoSwing", "Disabled swing effect when hitting an entity/mining a block.", ModuleCategory.RENDER);
    }

    @NotNull
    public final BooleanValue getServerSideValue() {
        return this.serverSideValue;
    }
}
