package kevin.module.modules.player.nofalls.packet

import kevin.event.PacketEvent
import kevin.event.UpdateEvent
import kevin.module.modules.player.nofalls.NoFallMode
import net.minecraft.network.play.client.C03PacketPlayer

object PacketNoFall3 : NoFallMode("Packet3") {
    private var packet1Count = 0
    private var packetModify = false
    override fun onEnable() {
        packet1Count = 0
        packetModify = false
    }
    override fun onNoFall(event: UpdateEvent) {
        if (mc.thePlayer.fallDistance.toInt() / 3 > packet1Count) {
            packet1Count = mc.thePlayer.fallDistance.toInt() / 3
            packetModify = true
        }
        if (mc.thePlayer.onGround) {
            packet1Count = 0
        }
    }
    override fun onPacket(event: PacketEvent) {
        if(event.packet is C03PacketPlayer) {
            if(packetModify) {
                event.packet.onGround = true
                packetModify = false
            }
        }
    }
}