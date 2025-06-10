package kevin.module.modules.player;

import kevin.event.EventTarget;
import kevin.event.UpdateEvent;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import kevin.utils.system.timer.MSTimer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class AutoFish extends ClientModule {
    @NotNull
    private final MSTimer rodOutTimer = new MSTimer();

    public AutoFish() {
        super("AutoFish", "Automatically catches fish when using a rod.", ModuleCategory.PLAYER);
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        EntityPlayerSP thePlayer = ClientModule.mc.thePlayer;
        if ((thePlayer != null ? thePlayer.getHeldItem() : null) != null) {
            ItemStack heldItem = thePlayer.getHeldItem();
            if (heldItem.getItem() instanceof ItemFishingRod) {
                EntityFishHook fishEntity = thePlayer.fishEntity;
                if (this.rodOutTimer.hasTimePassed(500L) && fishEntity == null || fishEntity != null && fishEntity.motionX == (double)0.0F && fishEntity.motionZ == (double)0.0F && fishEntity.motionY != (double)0.0F) {
                    ClientModule.mc.rightClickMouse();
                    this.rodOutTimer.reset();
                }
                return;
            }
        }

    }
}
