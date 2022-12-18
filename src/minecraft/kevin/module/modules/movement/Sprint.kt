package kevin.module.modules.movement

import kevin.event.EventTarget
import kevin.event.PacketEvent
import kevin.event.UpdateEvent
import kevin.main.KevinClient
import kevin.module.BooleanValue
import kevin.module.ListValue
import kevin.module.Module
import kevin.module.ModuleCategory
import kevin.module.modules.combat.SuperKnockback
import kevin.utils.MovementUtils
import kevin.utils.Rotation
import kevin.utils.RotationUtils
import net.minecraft.network.play.client.C0BPacketEntityAction
import net.minecraft.potion.Potion
import org.lwjgl.input.Keyboard

class Sprint : Module("Sprint","Automatically sprints all the time.", Keyboard.KEY_NONE,ModuleCategory.MOVEMENT) {

    val allDirectionsValue = BooleanValue("AllDirections", true)
    private val blindnessValue = BooleanValue("Blindness", true)
    val foodValue = BooleanValue("Food", true)

    val checkServerSide: BooleanValue = BooleanValue("CheckServerSide", false)
    val checkServerSideGround: BooleanValue = BooleanValue("CheckServerSideOnlyGround", false)

    val packetMode = ListValue("PacketMode", arrayOf("Normal", "NoPacket", "NoStart", "NoStop"), "Normal")

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        val keepSprint = KevinClient.moduleManager.getModule(SuperKnockback::class.java)
        if (keepSprint.stopSprint && keepSprint.stopTimer.hasTimePassed(keepSprint.delay/2+50)) {
            keepSprint.stopSprint = false
        }

        if (!MovementUtils.isMoving || mc.thePlayer.isSneaking ||
            blindnessValue.get() && mc.thePlayer
                .isPotionActive(Potion.blindness) ||
            foodValue.get() && !(mc.thePlayer.foodStats.foodLevel > 6.0f || mc.thePlayer.capabilities.allowFlying)
            || (checkServerSide.get() && (mc.thePlayer.onGround || !checkServerSideGround.get())
                    && !allDirectionsValue.get() && RotationUtils.targetRotation != null && RotationUtils.getRotationDifference(
                Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch)
            ) > 30) || keepSprint.stopSprint || KevinClient.moduleManager.getModule(InvMove::class.java).needStopSprint) {
            mc.thePlayer.isSprinting = false
            return
        }
        if (allDirectionsValue.get() || mc.thePlayer.movementInput.moveForward >= 0.8f) mc.thePlayer.isSprinting = true
    }

    @EventTarget fun onPacket(event: PacketEvent) {
        if (packetMode equal "Normal") return
        val packet = if (event.packet is C0BPacketEntityAction) event.packet else return
        when (packet.action) {
            C0BPacketEntityAction.Action.START_SPRINTING -> {
                if (!(packetMode equal "NoStop")) event.cancelEvent()
            }
            C0BPacketEntityAction.Action.STOP_SPRINTING -> {
                if (!(packetMode equal "NoStart")) event.cancelEvent()
            }
            else -> return
        }
    }
}