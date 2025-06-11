package kevin.module.modules.render;

import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import kotlin.Metadata;

public final class NoHurtCam extends ClientModule {
    public NoHurtCam() {
        super("NoHurtCam", "Disables hurt cam effect when getting hurt.", ModuleCategory.RENDER);
    }
}
