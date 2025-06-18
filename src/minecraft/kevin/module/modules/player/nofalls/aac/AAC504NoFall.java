package kevin.module.modules.player.nofalls.aac;

import kevin.event.UpdateEvent;
import kevin.event.impl.PacketEvent;
import kevin.module.modules.player.nofalls.NoFallMode;
import kevin.utils.MinecraftInstance;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import org.jetbrains.annotations.NotNull;

public final class AAC504NoFall extends NoFallMode {
    @NotNull
    public static final AAC504NoFall INSTANCE = new AAC504NoFall();
    private static boolean isDmgFalling;

    private AAC504NoFall() {
        super("AAC5.0.4");
    }

    public void onNoFall(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        isDmgFalling = MinecraftInstance.mc.thePlayer.fallDistance > 3.0F;
    }

    public void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        Packet<?> packet = event.getPacket();
        if (packet instanceof C03PacketPlayer && isDmgFalling) {
            C03PacketPlayer playerPacket = (C03PacketPlayer) packet;
            if (playerPacket.onGround && MinecraftInstance.mc.thePlayer.onGround) {
                isDmgFalling = false;
                playerPacket.onGround = true;
                MinecraftInstance.mc.thePlayer.onGround = false;
                ++playerPacket.y;

                NetHandlerPlayClient netHandler = MinecraftInstance.mc.getNetHandler();
                double x = playerPacket.x;
                double y = playerPacket.y - 1.0784;
                double z = playerPacket.z;

                netHandler.addToSendQueue(new C04PacketPlayerPosition(x, y, z, false));

                y = playerPacket.y - 0.5F;
                netHandler.addToSendQueue(new C04PacketPlayerPosition(x, y, z, true));
            }
        }
    }
}