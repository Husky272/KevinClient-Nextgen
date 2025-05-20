package kevin.module.modules.misc;

import kevin.event.EventTarget;
import kevin.event.TickEvent;
import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import kevin.utils.ChatUtils;


public class DebugModule extends ClientModule {
    public BooleanValue itemInUse = new BooleanValue("ItemInUse",true);

    public DebugModule() {
        super("DebugModule", "Debugging", ModuleCategory.MISC);
    }

    @EventTarget
    public void onTick(TickEvent event){
        if(itemInUse.get()){
            ChatUtils.messageWithStart(String.valueOf(mc.thePlayer.isUsingItem()));
        }
    }
}
