package kevin.module.modules.render;

import kevin.event.EventTarget;
import kevin.event.Render3DEvent;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import kevin.utils.RenderUtils;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;

public final class TNTESP extends ClientModule {

    public TNTESP() {
        super("TNTESP", "Allows you to see ignited TNT blocks through walls.", ModuleCategory.RENDER);
    }

    @EventTarget
    public void onRender3D(@NotNull Render3DEvent event) {
        WorldClient world = mc.theWorld;
        if (world == null) return;

        List<Entity> entities = world.loadedEntityList;
        for (Entity entity : entities) {
            if (entity instanceof EntityTNTPrimed) {
                RenderUtils.drawEntityBox(entity, Color.RED, false);
            }
        }
    }
}