package kevin.module.modules.movement.flys.vulcan

import kevin.event.PacketEvent
import kevin.event.UpdateEvent
import kevin.module.BooleanValue
import kevin.module.modules.movement.flys.FlyMode
import kevin.utils.MovementUtils
import kevin.utils.PacketUtils
import net.minecraft.client.settings.GameSettings
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.util.MathHelper
import kotlin.math.sqrt

object VulcanDamage : FlyMode("VulcanDamage") {
    private val waitDamage = BooleanValue("${valuePrefix}WaitDamage", true)
    private val fastFlag = BooleanValue("${valuePrefix}FastFlag", false)
    private var flag = false

    private var lastSentX = 0.0
    private var lastSentY = 0.0
    private var lastSentZ = 0.0
    private var started = false

    override fun onEnable() {
        flag = false
        lastSentX = mc.thePlayer.posX
        lastSentY = mc.thePlayer.posY
        lastSentZ = mc.thePlayer.posZ
        started = false
    }

    override fun onUpdate(event: UpdateEvent) {
        if(mc.thePlayer.onGround && (!waitDamage.get() || mc.thePlayer.hurtTime > 0) && !started) {
            started = true
            mc.timer.timerSpeed = 0.2f
            PacketUtils.sendPacketNoEvent(
                C04PacketPlayerPosition(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY - 2 + Math.random() / 2,
                    mc.thePlayer.posZ,
                    false
                )
            )
        }
        if(started) {
            mc.timer.timerSpeed = if(!flag) 0.2f else 1.0f
            mc.gameSettings.keyBindJump.pressed = false
            mc.gameSettings.keyBindSneak.pressed = false
            MovementUtils.strafe((0.96 + Math.random() / 50).toFloat())
            if(GameSettings.isKeyDown(mc.gameSettings.keyBindJump)) {
                mc.thePlayer.motionY = 0.42
            } else if(GameSettings.isKeyDown(mc.gameSettings.keyBindSneak)) {
                mc.thePlayer.motionY = -0.42
            } else {
                mc.thePlayer.motionY = 0.0
            }
            if(!MovementUtils.isMoving) {
                MovementUtils.resetMotion(false)
            }
        }
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1.0f
        MovementUtils.resetMotion(true)
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if(packet is C03PacketPlayer && (packet is C04PacketPlayerPosition || packet is C06PacketPlayerPosLook)) {
            val deltaX = packet.x - lastSentX
            val deltaY = packet.y - lastSentY
            val deltaZ = packet.z - lastSentZ

            if (sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) > 9.5) {
                lastSentX = packet.x
                lastSentY = packet.y
                lastSentZ = packet.z
                return
            }
            event.cancelEvent()
        }else if(packet is C03PacketPlayer) {
            event.cancelEvent()
        }
        if(packet is S08PacketPlayerPosLook) {
            if (!flag) {
                flag = true
                val deltaX = packet.x - mc.thePlayer.posX
                val deltaY = packet.y - mc.thePlayer.posY
                val deltaZ = packet.z - mc.thePlayer.posZ

                if (sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) < 10) {
                    event.cancelEvent()
                    PacketUtils.sendPacketNoEvent(
                        C06PacketPlayerPosLook(
                            packet.x,
                            packet.y,
                            packet.z,
                            packet.getYaw(),
                            packet.getPitch(),
                            false
                        )
                    )
                }
                if (fastFlag.get()) {
                    val yaw = Math.toRadians(mc.thePlayer.rotationYaw.toDouble()).toFloat()
                    val dist = 9.6 + Math.random() / 5
                    val x = -MathHelper.sin(yaw) * dist
                    val z = MathHelper.cos(yaw) * dist
                    PacketUtils.sendPacketNoEvent(C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false))
                }
            }
        }
    }
}