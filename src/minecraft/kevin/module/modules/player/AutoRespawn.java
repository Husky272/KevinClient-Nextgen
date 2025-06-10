package kevin.module.modules.player;

import kevin.event.EventTarget;
import kevin.event.UpdateEvent;
import kevin.main.KevinClient;
import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.ModuleCategory;
import kevin.module.modules.exploit.Ghost;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

public final class AutoRespawn extends ClientModule {
    @NotNull
    private final BooleanValue instantValue = new BooleanValue("Instant", true);

    public AutoRespawn() {
        super("AutoRespawn", "Automatically respawns you after dying.", ModuleCategory.PLAYER);
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        EntityPlayerSP thePlayer = ClientModule.mc.thePlayer;

        // 如果你不是null并且不是鬼就继续，如果是就不理你
        if (thePlayer != null && !((Ghost)KevinClient.INSTANCE.getModuleManager().getModule(Ghost.class)).getState()) {
            boolean zeroHealthOrDead;
            if (this.instantValue.get()) {
                zeroHealthOrDead = thePlayer.getHealth() == 0.0F || thePlayer.isDead;
            } else {
                label28: {

                    if (ClientModule.mc.currentScreen instanceof GuiGameOver) {
                        GuiScreen var3 = ClientModule.mc.currentScreen;
                        Intrinsics.checkNotNull(var3);
                        if (((GuiGameOver)var3).enableButtonsTimer >= 20) {
                            zeroHealthOrDead = true;
                            break label28;
                        }
                    }

                    zeroHealthOrDead = false;
                }
            }

            if (zeroHealthOrDead) {
                thePlayer.respawnPlayer();
                ClientModule.mc.displayGuiScreen((GuiScreen)null);
            }

        }
    }
}
