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
import kevin.utils.ChatUtils
import net.minecraft.item.*
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.network.play.client.C16PacketClientStatus
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing

@Suppress("unused_parameter")
class NoSlow : Module(name = "NoSlow", description = "Modify slowness caused by using items.", category = ModuleCategory.MOVEMENT) {

    private val sword = BooleanValue(name= "Sword", value = false)
    private val consume = BooleanValue(name = "Consume", value = false)
    private val bow = BooleanValue(name = "Bow", value = false)

    private val blockForwardMultiplier = FloatValue(name = "BlockForwardMultiplier", value = 1F, minimum = 0.2F, maximum = 1.0F) { sword.get() }
    private val blockStrafeMultiplier = FloatValue(name = "BlockStrafeMultiplier", value = 1F, minimum = 0.2F, maximum = 1.0F) { sword.get() }

    private val consumeForwardMultiplier = FloatValue(name = "ConsumeForwardMultiplier", value = 1.0F, minimum = 0.2F, maximum = 1.0F) { consume.get() }
    private val consumeStrafeMultiplier = FloatValue(name = "ConsumeStrafeMultiplier", value = 1.0F, minimum = 0.2F, maximum = 1.0F) { consume.get() }

    private val bowForwardMultiplier = FloatValue(name = "BowForwardMultiplier", value = 1.0F, minimum = 0.2F, maximum = 1.0F) { bow.get() }
    private val bowStrafeMultiplier = FloatValue(name = "BowStrafeMultiplier", value = 1.0F, minimum = 0.2F, maximum = 1.0F) { bow.get() }

    private val swordPlace = BooleanValue(name = "Sword-Place", value = true) { sword.get() }
    private val swordSwitch = BooleanValue(name = "Sword-Switch", value = false) { sword.get() }


    private val swordSwitchTiming = ListValue(name = "Sword-Switch-Timing", values = arrayOf("PRE", "POST", "NONE"), value = "NONE") { sword.get() && swordSwitch.get() }

    private val swordPlaceTiming = ListValue(name = "Sword-Place-Timing", values = arrayOf("PRE", "POST", "NONE"), value = "NONE") { sword.get() && swordPlace.get() }

    private val consumeSwitch = BooleanValue(name = "Consume-Switch", value = false) { consume.get() }
    private val consumeSwitchTiming = ListValue(name = "Consume-Switch-Timing", values = arrayOf("PRE", "POST", "NONE"), value = "NONE") { consume.get() && consumeSwitch.get() }
    private val consumeGay = BooleanValue(name = "Consume-Gay", value = true) { consume.get() }
    private val consumeBug = BooleanValue(name = "Consume-Bug", value = false) { consume.get() }
    private val consumeBugStopUsingItem = BooleanValue("Consume-Bug-StopUsingItem", true) { consume.get() && consumeBug.get()}
    private val consumeIntave = BooleanValue("Consume-Intave", false) { consume.get() }

    private val bowSwitch = BooleanValue(name = "Bow-Switch", value = false) { bow.get() }
    private val bowSwitchTiming = ListValue(name = "Bow-Switch-Timing", values = arrayOf("PRE", "POST", "NONE"), value = "NONE") { bow.get() && bowSwitch.get() }

    val soulsandValue = BooleanValue("Soulsand", true)
    val liquidPushValue = BooleanValue("LiquidPush", true)

/**
 * @author a114
*/
    override val tag: String
        get() = "主播为什么偷看我NoSlow的tag"
    @EventTarget
    fun onMotion(event: MotionEvent) {
        if (mc.thePlayer.isBlocking || KevinClient.moduleManager[KillAura::class.java].blockingStatus) {
            if(sword.get()){
                if (swordPlace.get()) {
                    when (swordPlaceTiming.get()) {
                        "PRE" ->{
                            if (event.eventState == EventState.PRE) {
                                mc.netHandler.addToSendQueue(
                                    C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f)
                                )
                            }
                        }
                        "POST" -> {
                            if (event.eventState == EventState.POST) {
                                mc.netHandler.addToSendQueue(
                                    C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f)
                                )
                            }
                        }
                        "NONE" -> {
                            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0f, 0f, 0f))
                        }
                    }
                }
                else if (swordSwitch.get()){
                    when (swordSwitchTiming.get()){
                        "PRE" -> {
                            if (event.eventState == EventState.PRE) {
                                mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9))
                                mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                            }
                        }
                        "POST" -> {
                            if (event.eventState == EventState.POST) {
                                mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9))
                                mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                            }
                        }
                        "NONE" -> {
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9))
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                        }
                    }
                }
            }
        }
        if (mc.thePlayer.isUsingItem && mc.thePlayer.currentEquippedItem.item is ItemFood){
            if (consumeSwitch.get()) {
                when(consumeSwitchTiming.get()){
                    "PRE" -> {
                        if (event.eventState == EventState.PRE) {
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9))
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                        }
                    }
                    "POST" -> {
                        if (event.eventState == EventState.POST) {
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9))
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                        }
                    }"NONE" -> {
                        mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9))
                        mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                    }
                }
            }
        }
        if (mc.thePlayer.isUsingItem){
            if (mc.thePlayer.currentEquippedItem.item is ItemBow) {
                if (bowSwitch.get()){
                    when(bowSwitchTiming.get()){
                        "PRE" -> {
                            if (event.eventState == EventState.PRE) {
                                mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9))
                                mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                            }
                        }
                        "POST" -> {
                            if (event.eventState == EventState.POST) {
                                mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9))
                                mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                            }
                        }
                        "NONE" -> {
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9))
                            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
                        }
                    }
                }
                
            }
            if (mc.thePlayer.currentEquippedItem.item is ItemFood
                ||
                mc.thePlayer.currentEquippedItem.item is ItemBucketMilk
                ||
                mc.thePlayer.currentEquippedItem.item is ItemPotion
                )
            {
                if(consumeIntave.get()){
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem +1 )%9))
                    mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos(-1, -1, -1), EnumFacing.DOWN))
                    mc.netHandler.addToSendQueue(C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem)))
                }
            }
        }
    }

//    @EventTarget
//    fun onUpdate(event: UpdateEvent){
//    }

    @EventTarget
    fun onClick(event: ClickUpdateEvent){
        try {
            val currentItem = mc.thePlayer.currentEquippedItem
            if (!(currentItem.item is ItemFood || currentItem.item is ItemBucketMilk || currentItem.item is ItemPotion) || (mc.thePlayer == null || mc.theWorld == null)) {
                return
            }
            //Try to fix crash client bug
            if ((consume.get() && consumeBug.get()) && mc.thePlayer.isUsingItem /*&& (currentItem.item is ItemFood || currentItem.item is ItemBucketMilk)*/) {
                event.cancelEvent()
                mc.netHandler.networkManager.sendPacketNoEvent(C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))
                if (consumeBugStopUsingItem.get()) {
                    mc.thePlayer.stopUsingItem()
                }
                mc.thePlayer.closeScreen()
            }
            if (event.isCancelled) {
                mc.sendClickBlockToController(mc.currentScreen == null && mc.gameSettings.keyBindAttack.isKeyDown && mc.inGameHasFocus)
            }
        }
        catch (_:Throwable) {
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
            (item is ItemFood || item is ItemPotion || item is ItemBucketMilk) && consume.get() -> {
                if (isForward) this.consumeForwardMultiplier.get() else this.consumeStrafeMultiplier.get()
            }
            (item is ItemSword && sword.get()) -> {
                if (isForward) this.blockForwardMultiplier.get()
                else this.blockStrafeMultiplier.get()
            }
            (item is ItemBow && bow.get()) -> {
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
        if (mc.thePlayer.isUsingItem && (item is ItemFood || item is ItemBucketMilk || item is ItemPotion)) {//Is consuming
            if(consumeGay.get()) {
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
}
