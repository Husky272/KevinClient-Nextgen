package kevin.module.modules.combat;

import kevin.event.EventTarget;
import kevin.event.UpdateEvent;
import kevin.main.KevinClient;
import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

public final class AutoBow extends ClientModule {
    @NotNull
    private final BooleanValue waitForBowAimbot = new BooleanValue("WaitForBowAimbot", true);

    public AutoBow() {
        super("AutoBow", "Automatically shoots an arrow whenever your bow is fully loaded.", ModuleCategory.COMBAT);
    }

    @EventTarget
    public void onUpdate(@NotNull UpdateEvent event) {
        BowAimbot bowAimbot = KevinClient.INSTANCE.getModuleManager().getModule(BowAimbot.class);

        EntityPlayerSP thePlayer = ClientModule.mc.thePlayer;
        if (thePlayer.isUsingItem()) {
            ItemStack selfHoldingItem = thePlayer.getHeldItem();
            if (
                    (selfHoldingItem != null ? selfHoldingItem.getItem() : null) instanceof ItemBow
                            && thePlayer.getItemInUseDuration() > 20
                            && (
                                    !this.waitForBowAimbot.get() || !bowAimbot.getState() || bowAimbot.hasTarget()
                    )
            ) {
                thePlayer.stopUsingItem();
                ClientModule.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }

    }
}
