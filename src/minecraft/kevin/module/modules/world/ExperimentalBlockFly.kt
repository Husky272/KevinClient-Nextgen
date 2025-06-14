//TODO: Make java great again
package kevin.module.modules.world

import kevin.event.*
import kevin.module.*
import kevin.utils.*
import kevin.utils.MovementUtils.movingYaw
import kevin.utils.entity.combatAndInventory.InventoryUtils
import kevin.utils.entity.rotation.RotationUtils
import kevin.utils.entity.getNearestPointBB
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.network.play.client.C09PacketHeldItemChange
import net.minecraft.network.play.client.C0APacketAnimation
import net.minecraft.network.play.server.S09PacketHeldItemChange
import net.minecraft.util.*
import org.lwjgl.input.Keyboard
import java.awt.Color
import java.util.*
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.min

class ExperimentalBlockFly :
    ClientModule("ExperimentalFeature", "Auto place blocks under your feet.", Keyboard.KEY_NONE, ModuleCategory.WORLD) {
    private var rot = Rotation(0f, 0f)
    private var currentRot: Rotation? = Rotation(0f, 0f)
    private val maxHorizontalRotationSpeed = FloatValue("MaxHorizontalRotationSpeed", 50f, 0.01f, 180f)
    private val maxVerticalRotationSpeed = FloatValue("MaxVerticalRotationSpeed", 50f, 0.01f, 180f)
    private val sneak = BooleanValue("Sneak", true)
    private val rotationSneak = BooleanValue("RotationSneak", true)
    private val swing = BooleanValue("VisualSwing", true)
    private val silentMode = ListValue("SilentMode", arrayOf("None", "Spoof", "Switch"), "Spoof")
    private val esp = BooleanValue("ESP", true)
    private var lastFeetVec = Vec3(0.0, 0.0, 0.0)
    private var slotID = 0
    private var lastSlotID = 0
    private var sneakWaitTick = 0
    private var blockPos: BlockPos? = null

    //    private boolean tickUpdated = false;
    override fun onDisable() {
        if (mc.thePlayer.inventory.currentItem != slotID) {
            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
        }
        RotationUtils.setTargetRotation(
            Rotation(
                mc.thePlayer.rotationYaw + currentRot!!.yaw / 2,
                mc.thePlayer.rotationPitch + currentRot!!.pitch / 2
            )
        )
        mc.thePlayer.setSprinting(false)
        slotID = mc.thePlayer.inventory.currentItem
        lastSlotID = mc.thePlayer.inventory.currentItem
    }

    override fun onEnable() {
        rot = Rotation(mc.thePlayer.rotationYaw + 179.3512f, 80.64f)
        slotID = mc.thePlayer.inventory.currentItem
        lastSlotID = mc.thePlayer.inventory.currentItem
        //        rot.setYaw(MovementUtils.INSTANCE.getMovingYaw() + 179.212f);
        blockPos = null
        currentRot = RotationUtils.bestServerRotation()
    }

    @EventTarget
    fun onClick(event: ClickUpdateEvent) {
        if (mc.currentScreen != null) {
//            updateRotation();
            return
        }
        val itemstack: ItemStack
        if (silentMode.get() != "None") {
            slotID = InventoryUtils.findAutoBlockBlock() - 36
            if (slotID == -1) {
                updateRotation()
                return
            }
            itemstack = mc.thePlayer.inventoryContainer.getSlot(slotID + 36).stack
            if (silentMode.get() == "Spoof" && lastSlotID != slotID) {
                mc.netHandler.addToSendQueue(C09PacketHeldItemChange(slotID))
            }
        } else {
            slotID = mc.thePlayer.inventory.currentItem
            lastSlotID = mc.thePlayer.inventory.currentItem
            itemstack = mc.thePlayer.heldItem
        }
        if (itemstack.item is ItemBlock) {
            willThisTickOutOfEdge()
            val b = isBlockSolid(BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.5, mc.thePlayer.posZ))
            //            Pair<Rotation, BlockData> pair = searchBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ).down(), !b, (ItemBlock) itemstack.getItem(), itemstack);
            val pair = search2(itemstack.item as ItemBlock, itemstack)
            if (pair != null && b) {
                rot = pair.first
                updateRotation()
                val data = pair.second
                blockPos = data.blockPos
                // do raytrace to make sure everything is ok
                val eyes = mc.thePlayer.getPositionEyes(1f)
                val target = currentRot!!.toDirection().multiply(4.5).add(eyes)
                val m4 = mc.theWorld.rayTraceBlocks(eyes, target, false, false, true)
                if (m4 != null && m4.sideHit == data.facing && m4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && m4.blockPos == data.blockPos) {
                    click(itemstack, data, m4.hitVec)
                }
            } else {
//                rot = new Rotation((float) MovementUtils.getDirection() + 179.98f, 80.53f);
                updateRotation()
                val eyes = mc.thePlayer.getPositionEyes(1f)
                val target = currentRot!!.toDirection().multiply(4.5).add(eyes)
                val m4 = mc.theWorld.rayTraceBlocks(eyes, target, false, false, true)
                blockPos =
                    if (m4 != null && m4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && m4.blockPos != null && m4.sideHit != null && m4.blockPos.getY() <= mc.thePlayer.posY) {
                        click(itemstack, BlockData(m4.blockPos, m4.sideHit), m4.hitVec)
                        m4.blockPos
                    } else {
                        //                    updateRotation();
                        null
                    }
            }
        } else {
            updateRotation()
            blockPos = null
        }
        lastSlotID = slotID
        event.cancelEvent()
        lastFeetVec = Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)
    }

    @EventTarget
    fun onUpdate(ignored: UpdateEvent?) {
        setRotation()
        if (sneakWaitTick > 0) {
            mc.gameSettings.keyBindSneak.pressed = true
            --sneakWaitTick
        } else {
            mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.keyCode)
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is C09PacketHeldItemChange) {
            val id = packet.slotId
            if (id == lastSlotID) event.cancelEvent()
            lastSlotID = id
        } else if (packet is S09PacketHeldItemChange) {
            lastSlotID = packet.heldItemHotbarIndex
        }
    }

    @EventTarget
    fun onEventRender3D(event: Render3DEvent?) {
        if (esp.get() && blockPos != null) {
            val red = 0.16470589f
            val green = 0.5686275f
            val blue = 0.96862745f
            //            GL11.glColor4f(red, green, blue, 0.39215687F);

//            final RenderManager renderManager = mc.getRenderManager();
            RenderUtils.drawBlockBox(blockPos, Color(red, green, blue, 0.39215687f), false)
            //            RenderUtils.drawFilledBox(new AxisAlignedBB(this.blockPos.getX() - renderManager.getRenderPosX(), this.blockPos.getY() - renderManager.getRenderPosY(), this.blockPos.getZ() - renderManager.getRenderPosZ(), this.blockPos.getX() + 1 - renderManager.getRenderPosX(), this.blockPos.getY() + 1 - renderManager.getRenderPosY(), this.blockPos.getZ() + 1 - renderManager.getRenderPosZ()));
        }
    }

    private fun click(stack: ItemStack?, data: BlockData, hitVec: Vec3) {
        if (mc.playerController.onPlayerRightClick(
                mc.thePlayer,
                mc.theWorld,
                stack,
                data.blockPos,
                data.facing,
                hitVec
            )
        ) {
            if (swing.get()) mc.thePlayer.swingItem() else mc.netHandler.addToSendQueue(C0APacketAnimation())
        }
        if (stack != null && stack.stackSize == 0) {
            mc.thePlayer.inventory.mainInventory[slotID] = null
        }
        mc.sendClickBlockToController(mc.currentScreen == null && mc.gameSettings.keyBindAttack.isKeyDown && mc.inGameHasFocus)
    }

    private fun updateRotation() {
        currentRot = RotationUtils.limitAngleChange(
            if (currentRot != null) currentRot else RotationUtils.bestServerRotation(),
            rot,
            maxHorizontalRotationSpeed.get(),
            maxVerticalRotationSpeed.get()
        )
        currentRot!!.fixedSensitivity()
        if ((RotationUtils.getRotationDifference(
                currentRot,
                rot
            ) > 0.3 || abs((currentRot!!.yaw - rot.yaw).toDouble()) > 0.01) && mc.thePlayer.onGround && shouldSneak(
                rotationSneak
            )
        ) {
            sneakWaitTick = 3
        }
        setRotation()
    }

    private fun setRotation() {
        RotationUtils.setTargetRotation(currentRot, 0)
    }

    private fun shouldSneak(value: BooleanValue): Boolean {
        return sneak.get() && value.get()
    }

    private fun willThisTickOutOfEdge(): Boolean {
        val player = mc.thePlayer
        val world = mc.theWorld
        if (player == null || world == null) return false
        if (!player.onGround) return player.motionY > 0
        val motionX = player.motionX
        val motionZ = player.motionZ
        val bb = player.collisionBoundingBox
        var offset = bb.offset(motionX * 0.91f * 0.6f, -0.08, motionZ * 0.91f * 0.6f)
        var abb = bb.offset(motionX * 0.91f * 0.6f, 0.0, motionZ * 0.91f * 0.6f)
        var realY = -0.08
        for (box in world.getCollidingBoundingBoxes(player, offset)) {
            realY = box.calculateYOffset(abb, realY)
        }
        if (realY <= -0.015625) return true
        realY = -0.08
        offset = bb.offset(motionX, -0.08, motionZ)
        abb = bb.offset(motionX, 0.0, motionZ)
        for (box in world.getCollidingBoundingBoxes(player, offset)) {
            realY = box.calculateYOffset(abb, realY)
        }
        if (realY <= -0.015625) return true
        offset = bb.offset(motionX * 1.09, -0.08, motionZ * 1.09)
        abb = bb.offset(motionX * 1.09, 0.0, motionZ * 1.09)
        for (box in world.getCollidingBoundingBoxes(player, offset)) {
            realY = box.calculateYOffset(abb, realY)
        }
        return realY <= -0.015625
    }

    fun searchBlock(
        bp: BlockPos,
        willFall: Boolean,
        itemBlock: ItemBlock,
        itemStack: ItemStack?,
    ): Pair<Rotation?, BlockData>? {
        val dataCollection = LinkedList<BlockData>()
        if (isBlockSolid(bp)) return null
        val vecEyes = mc.thePlayer.getPositionEyes(1f)
        val lookVec = currentRot!!.toDirection().multiply(4.5).add(vecEyes)
        val movingObjectPosition1 = mc.theWorld.rayTraceBlocks(vecEyes, lookVec, false, false, true)
        if (movingObjectPosition1 != null) {
            if (movingObjectPosition1.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                if (movingObjectPosition1.blockPos != null) {
                    if (movingObjectPosition1.blockPos.getY() <= mc.thePlayer.posY) {
                        // the player don't need to update rotation currently
                        if (itemBlock.canPlaceBlockOnSide(
                                mc.theWorld,
                                movingObjectPosition1.blockPos,
                                movingObjectPosition1.sideHit,
                                mc.thePlayer,
                                itemStack
                            ) && movingObjectPosition1.sideHit != EnumFacing.UP && movingObjectPosition1.sideHit != EnumFacing.DOWN || !willFall
                        ) {
                            return Pair(
                                currentRot,
                                BlockData(movingObjectPosition1.blockPos, movingObjectPosition1.sideHit)
                            )
                        } else {
                            // as a situation
                            dataCollection.add(BlockData(movingObjectPosition1.blockPos, movingObjectPosition1.sideHit))
                        }
                    }
                }
            }
        }
        for (posLayer in layerOffset) {
            val layer = bp.add(posLayer)
            for (offset in searchOffset) {
                val pos = layer.add(offset)
                for (data in searchBlockDataOffset) {
                    val added = pos.add(data.blockPos)
                    if (isBlockSolid(added)) {
                        if (itemBlock.canPlaceBlockOnSide(
                                mc.theWorld,
                                added,
                                data.facing,
                                mc.thePlayer,
                                itemStack
                            )
                        ) dataCollection.add(BlockData(added, data.facing))
                    }
                }
            }
            if (!dataCollection.isEmpty()) break
        }
        val rotationData = LinkedList<Pair<Rotation, BlockData>>()

        // let's validate what data can use directly
        for (data in dataCollection) {
            val rotation = getRotations(data.blockPos, data.facing)
            val look = rotation.toDirection().multiply(4.5).add(vecEyes)
            val mop = mc.theWorld.rayTraceBlocks(vecEyes, look, false, false, true) ?: continue
            // this rotation will not ray any target
            // this rotation will not ray on block
            if (mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) continue
            // this rotation's ray is not looking at the block we want
            if (mop.blockPos == null) continue
            if (mop.blockPos != data.blockPos) continue
            // to rotation's ray is looking at the block, but in wrong side
            // and please no downward
            if (mop.sideHit != data.facing || mop.sideHit == EnumFacing.DOWN) continue
            // we don't need to up temporary
            if (mop.sideHit == EnumFacing.UP && (mc.thePlayer.onGround || !mc.gameSettings.keyBindJump.isKeyDown || mop.blockPos.getY() > mc.thePlayer.posY)) continue
            // make sure again it is not invalid
            if (mc.theWorld.getBlockState(mop.blockPos).getBlock().material === Material.air) continue
            // rotation up, is this scaffold?
            if (rotation.pitch <= 0) continue
            // add to queue to wait for next calculation
            val pair = Pair(rotation, data)
            data.mop = mop
            rotationData.add(pair)
        }

        // no block was found, discontinued
        if (rotationData.isEmpty()) return null

        // compare the difference to current rotation, and choose the nearest one
        rotationData.sortWith(Comparator.comparingDouble { (first, second): Pair<Rotation?, BlockData> ->
            getDistanceToBlockPos(
                second.blockPos
            ) + RotationUtils.getRotationDifference(
                first,
                currentRot
            ) + min((second.blockPos.getY() - mc.thePlayer.posY + 0.5) * 2, 0.0)
        })
        return rotationData.getFirst()
    }

    private fun search2(itemBlock: ItemBlock, itemStack: ItemStack): Pair<Rotation, BlockData> {
        val rotation = Rotation(movingYaw, 76.0)
        val data = LinkedList<Pair<Rotation, BlockData>>()
        var p = 69.423f
        while (p <= 90.0f) {
            rotation.pitch = p
            rotation.fixedSensitivity()
            val vecEyes = mc.thePlayer.getPositionEyes(1f)
            val look = rotation.toDirection().multiply(4.5).add(vecEyes)
            val mop = mc.theWorld.rayTraceBlocks(vecEyes, look, false, false, true)
            if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                val blockPos1 = mop.blockPos
                val sideHit = mop.sideHit
                if (sideHit != null &&
                    isBlockSolid(blockPos1) && sideHit != EnumFacing.DOWN &&
                    (sideHit != EnumFacing.UP || mc.gameSettings.keyBindJump.isKeyDown)
                ) {
                    if (itemBlock.canPlaceBlockOnSide(mc.theWorld, blockPos1, sideHit, mc.thePlayer, itemStack)) {
                        data.add(Pair(rotation.cloneSelf(), BlockData(blockPos1, sideHit)))
                    }
                }
            }
            p += 0.02f
        }
        data.sortWith(Comparator.comparingDouble { (first): Pair<Rotation?, BlockData?> ->
            RotationUtils.getRotationDifference(
                first,
                currentRot
            )
        })
        return data.getFirst()
    }

    class BlockData(val blockPos: BlockPos, val facing: EnumFacing) {
        var mop: MovingObjectPosition? = null
    }

    companion object {
        private fun getDistanceToBlockPos(blockPos: BlockPos): Double {
//        double distance = 1337.0D;
//        for(float x = (float)blockPos.getX(); x <= (float)(blockPos.getX() + 1); x = (float)((double)x + 0.2D)) {
//            for(float y = (float)blockPos.getY(); y <= (float)(blockPos.getY() + 1); y = (float)((double)y + 0.2D)) {
//                for(float z = (float)blockPos.getZ(); z <= (float)(blockPos.getZ() + 1); z = (float)((double)z + 0.2D)) {
//                    double d0 = mc.thePlayer.getDistanceSq(x, y, z);
//                    if(d0 < distance) {
//                        distance = d0;
//                    }
//                }
//            }
//        }
//
//        return distance;
            val vec3 = Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)
            return getNearestPointBB(
                vec3,
                AxisAlignedBB(
                    blockPos.getX().toDouble(),
                    blockPos.getY().toDouble(),
                    blockPos.getZ().toDouble(),
                    (blockPos.getX() + 1).toDouble(),
                    (blockPos.getY() + 1).toDouble(),
                    (blockPos.getZ() + 1).toDouble()
                )
            ).distanceTo(vec3)
        }

        private fun isBlockSolid(pos: BlockPos): Boolean {
            val block = mc.theWorld.getBlockState(pos).getBlock()
            return ((block.material.isSolid || !block.isTranslucent || block.isBlockNormalCube || block is BlockLadder || block is BlockCarpet
                    || block is BlockSnow || block is BlockSkull)
                    && !(block.material.isLiquid || block is BlockContainer))
        }

        private fun getRotations(block: BlockPos, face: EnumFacing): Rotation {
            val x = block.getX() + 0.5 - mc.thePlayer.posX + face.frontOffsetX / 2.0
            val z = block.getZ() + 0.5 - mc.thePlayer.posZ + face.frontOffsetZ / 2.0
            val y = block.getY() + 0.5
            val d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y
            val d3 = MathHelper.sqrt_double(x * x + z * z).toDouble()
            var yaw = (atan2(z, x) * 180.0 / Math.PI).toFloat() - 90.0f
            val pitch = (atan2(d1, d3) * 180.0 / Math.PI).toFloat()
            if (yaw < 0.0f) {
                yaw += 360f
            }
            return Rotation(yaw, pitch)
        }

        private val searchBlockDataOffset = arrayOf(
            BlockData(BlockPos(0, -1, 0), EnumFacing.UP),
            BlockData(BlockPos(-1, 0, 0), EnumFacing.EAST),
            BlockData(BlockPos(1, 0, 0), EnumFacing.WEST),
            BlockData(BlockPos(0, 0, 1), EnumFacing.NORTH),
            BlockData(BlockPos(0, 0, -1), EnumFacing.SOUTH)
        )
        private val searchOffset = arrayOf(
            BlockPos(0, 0, 0),
            BlockPos(-1, 0, 0),
            BlockPos(1, 0, 0),
            BlockPos(0, 0, 1),
            BlockPos(0, 0, -1)
        )
        private val layerOffset = arrayOf(
            BlockPos(0, 0, 0),
            BlockPos(0, -1, 0)
        )
    }
}
