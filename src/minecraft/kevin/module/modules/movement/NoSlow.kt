/*
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kevin.module.modules.movement

import kevin.event.*
import kevin.main.KevinClient
import kevin.module.*
import kevin.module.modules.combat.KillAura
import net.minecraft.item.*
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.network.play.client.C16PacketClientStatus
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing

@Suppress("unused_parameter")
class NoSlow : ClientModule(
    "NoSlow", "Modify slowness caused by using items.", ModuleCategory.MOVEMENT
) {

    private val sword = BooleanValue("Sword", false)
    private val consume = BooleanValue("Consume", false)
    private val bow = BooleanValue("Bow", false)

    private val blockForwardMultiplier =
        FloatValue("BlockForwardMultiplier", 1F, 0.2F, 1.0F) { sword.get() }
    private val blockStrafeMultiplier =
        FloatValue("BlockStrafeMultiplier", 1F, 0.2F, 1.0F) { sword.get() }

    private val consumeForwardMultiplier =
        FloatValue("ConsumeForwardMultiplier", 1.0F, 0.2F, 1.0F) { consume.get() }
    private val consumeStrafeMultiplier =
        FloatValue("ConsumeStrafeMultiplier", 1.0F, 0.2F, 1.0F) { consume.get() }

    private val bowForwardMultiplier =
        FloatValue("BowForwardMultiplier", 1.0F, 0.2F, 1.0F) { bow.get() }
    private val bowStrafeMultiplier =
        FloatValue("BowStrafeMultiplier", 1.0F, 0.2F, 1.0F) { bow.get() }

    private val swordPlace = BooleanValue("Sword-Place", true) { sword.get() }
    private val swordSwitch = BooleanValue("Sword-Switch", false) { sword.get() }


    private val swordSwitchTiming = ListValue(
        "Sword-Switch-Timing", arrayOf("PRE", "POST", "NONE"), "NONE"
    ) { sword.get() && swordSwitch.get() }

    private val swordPlaceTiming = ListValue(
        "Sword-Place-Timing", arrayOf("PRE", "POST", "NONE"), "NONE"
    ) { sword.get() && swordPlace.get() }

    private val consumeSwitch =
        BooleanValue("Consume-Switch", false) { consume.get() }
    private val consumeSwitchTiming = ListValue(
        "Consume-Switch-Timing", arrayOf("PRE", "POST", "NONE"), "NONE"
    ) { consume.get() && consumeSwitch.get() }
    private val consumeGay = BooleanValue("Consume-Gay", true) { consume.get() }
    private val consumeBug = BooleanValue("Consume-Bug", false) { consume.get() }
    private val consumeBugStopUsingItem = BooleanValue(
        "Consume-Bug-StopUsingItem", true
    ) { consume.get() && consumeBug.get() }
    private val consumeIntave =
        BooleanValue("Consume-Intave", false) { consume.get() }

    private val bowSwitch = BooleanValue("Bow-Switch", false) { bow.get() }
    private val bowSwitchTiming = ListValue(
        "Bow-Switch-Timing", arrayOf("PRE", "POST", "NONE"), "NONE"
    ) { bow.get() && bowSwitch.get() }

    val soulsand = BooleanValue("Soulsand", true)
    val liquidPush = BooleanValue("LiquidPush", true)

    /**
     * @author a114
     */
    override fun getTag() =
        "Sword: ${sword.get()}, Consume: ${consume.get()}, Bow: ${bow.get()}"

    private fun amIBlocking(): Boolean {

        try {
            if (mc.thePlayer == null || mc.thePlayer.itemInUse == null || mc.thePlayer.itemInUse.item == null) return false
        } catch (e: Throwable) { return false }

        
        var whatAnAwsomeVarName = false
        try {
            // Sword is not stackable
            whatAnAwsomeVarName = !mc.thePlayer.itemInUse.isStackable
                    && mc.thePlayer.itemInUse.item is ItemSword
        } catch (e: ClassCastException) {
            return false
        }

        return mc.thePlayer.isBlocking || KevinClient.moduleManager[KillAura::class.java].blockingStatus || whatAnAwsomeVarName
    }

    private fun amIConsuming(): Boolean {
        try {
            if (mc.thePlayer.itemInUse == null) return false
            if (mc.thePlayer.itemInUse.item == null) return false
        } catch (e: Throwable) {
            return false
        }
        return isHoldingConsumable(mc.thePlayer.currentEquippedItem.item) && isHoldingConsumable(
            mc.thePlayer.itemInUse.item
        )
    }


    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (amIBlocking()) {
            if (sword.get()) {
                if (swordPlace.get()) {
                    when (swordPlaceTiming.get()) {
                        "PRE" -> {
                            if (event.eventState == EventState.PRE) {
                                place()
                            }
                        }

                        "POST" -> {
                            if (event.eventState == EventState.POST) {
                                place()

                            }
                        }

                        "NONE" -> {
                            place()
                        }
                    }
                }
                if (swordSwitch.get()) {
                    when (swordSwitchTiming.get()) {
                        "PRE" -> {
                            if (event.eventState == EventState.PRE) {
                                switchAndBack()

                            }
                        }

                        "POST" -> {
                            if (event.eventState == EventState.POST) {
                                switchAndBack()

                            }
                        }

                        "NONE" -> {
                            switchAndBack()

                        }
                    }
                }
            }
        }
        if (amIConsuming()) {
            if (consumeSwitch.get()) {
                when (consumeSwitchTiming.get()) {
                    "PRE" -> {
                        if (event.eventState == EventState.PRE) {
                            switchAndBack()
                        }
                    }

                    "POST" -> {
                        if (event.eventState == EventState.POST) {
                            switchAndBack()
                        }
                    }

                    "NONE" -> {
                        switchAndBack()
                    }
                }
            }
        }
        if (mc.thePlayer.isUsingItem) {
            if (isHoldingBow(mc.thePlayer.currentEquippedItem.item)) {
                if (bowSwitch.get()) {
                    when (bowSwitchTiming.get()) {
                        "PRE" -> {
                            if (event.eventState == EventState.PRE) {
                                switchAndBack()
                            }
                        }

                        "POST" -> {
                            if (event.eventState == EventState.POST) {
                                switchAndBack()
                            }
                        }

                        "NONE" -> {
                            switchAndBack()
                        }
                    }
                }

            }
            if (isHoldingConsumable(mc.thePlayer.currentEquippedItem.item)) {
                if (consumeIntave.get()) {
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9))
                    mc.netHandler.addToSendQueue(
                        C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.DROP_ITEM,
                            BlockPos(-1, -1, -1),
                            EnumFacing.DOWN
                        )
                    )
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem)))
                }
            }
        }
    }

//    @EventTarget
//    fun onUpdate(event: UpdateEvent){
//    }

    @EventTarget
    fun onClick(event: ClickUpdateEvent) {
        try {
            val currentEquippedItemStack = mc.thePlayer.currentEquippedItem
            val currentEquippedItemStackItem = currentEquippedItemStack.item
            // If the player is holding something wrong, or something other went wrong, it will return
            if (!(isHoldingConsumable(currentEquippedItemStackItem)) || (mc.thePlayer == null || mc.theWorld == null)) {
                return
            }
            //Try to fix crash client bug
            if ((consume.get() && consumeBug.get()) && mc.thePlayer.isUsingItem && isHoldingConsumable(
                    currentEquippedItemStackItem
                )
            ) {
                event.cancelEvent()
                mc.netHandler.networkManager.sendPacketNoEvent(
                    C16PacketClientStatus(
                        C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT
                    )
                )
                if (consumeBugStopUsingItem.get()) {
                    mc.thePlayer.stopUsingItem()
                }
                mc.thePlayer.closeScreen()
            }
            if (event.isCancelled) {
                mc.sendClickBlockToController(mc.currentScreen == null && mc.gameSettings.keyBindAttack.isKeyDown && mc.inGameHasFocus)
            }
        } catch (_: Throwable) {
        }
    }

    @EventTarget
    fun onSlowDown(event: SlowDownEvent) {
        val heldItem = mc.thePlayer!!.heldItem?.item
        event.forward = getMultiplier(heldItem, true)
        event.strafe = getMultiplier(heldItem, false)
    }

    private fun getMultiplier(item: Item?, isForward: Boolean): Float {

        return when {
            (isHoldingConsumable(item)) && consume.get() -> {
                if (isForward) this.consumeForwardMultiplier.get() else this.consumeStrafeMultiplier.get()
            }

            (isHoldingSword(item) && sword.get()) -> {
                if (isForward) this.blockForwardMultiplier.get()
                else this.blockStrafeMultiplier.get()
            }

            (isHoldingBow(item) && bow.get()) -> {
                if (isForward) this.bowForwardMultiplier.get() else this.bowStrafeMultiplier.get()
            }

            else -> {
                0.2F
            }
        }

    }

    @EventTarget
    fun onTick(event: TickEvent, item: Item?) {
        if (mc.thePlayer == null || mc.theWorld == null) return
        if (mc.thePlayer.isUsingItem && (isHoldingConsumable(item))) { //Is consuming
            if (consumeGay.get()) {
                if (mc.thePlayer.ticksExisted % 5 == 0) {
                    mc.netHandler.addToSendQueue(
                        C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                            BlockPos.ORIGIN,
                            EnumFacing.DOWN
                        )
                    )
                }
                if (mc.thePlayer.ticksExisted % 5 == 1) {
                    mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.currentEquippedItem))
                }
            }
        }
    }

    private fun isHoldingConsumable(item: Item?): Boolean {
        if (item == null) return false
        return item is ItemFood || item is ItemBucketMilk || item is ItemPotion
    }

    private fun isHoldingBow(item: Item?): Boolean {
        if (item == null) return false
        return item is ItemBow
    }

    private fun isHoldingSword(item: Item?): Boolean {
        if (item == null) return false
        return item is ItemSword
    }

    private fun switchAndBack() {
        mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9))
        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
    }

    private fun place() {
        mc.netHandler.addToSendQueue(
            C08PacketPlayerBlockPlacement(
                BlockPos(
                    -1, -1, -1
                ), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f
            )
        )
    }
}
