package kevin.plugin;

import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

public class PluginModule extends ClientModule {
    public PluginModule(String name, String description, int keyBind, ModuleCategory category) {
        super(name, description, keyBind, category);
    }

    public PluginModule(String name, String description, ModuleCategory category) {
        this(name, description, Keyboard.KEY_NONE, category);
    }

    public PluginModule(String name, String description) {
        this(name, description, Keyboard.KEY_NONE, ModuleCategory.MISC);
    }
}
