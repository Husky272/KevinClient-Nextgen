package kevin.module.modules.combat;

import kevin.event.*;
import kevin.event.impl.AttackEvent;
import kevin.event.impl.BlockBBEvent;
import kevin.event.impl.JumpEvent;
import kevin.event.impl.PacketEvent;
import kevin.main.KevinClient;
import kevin.module.*;
import kevin.utils.MovementUtils;
import kevin.utils.OtherExtensionsKt;
import kevin.utils.PacketUtils;
import kevin.utils.entity.rotation.RaycastUtils;
import kevin.utils.system.timer.MSTimer;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.Locale;

public final class AntiKnockback extends ClientModule {

    private final Minecraft mc = Minecraft.getMinecraft();

    // Values
    private final FloatValue horizontalValue = new FloatValue("Horizontal", 0F, -1F, 1F);
    private final FloatValue verticalValue = new FloatValue("Vertical", 0F, -1F, 1F);
    private final ListValue modeValue = new ListValue("Mode", new String[]{
            "AAC", "AACPush", "AACZero", "AACv4", "AAC5Packet", "AllowFirst", "BlockCollection", "Click", "Cancel",
            "Down", "Glitch", "GrimDig", "HypixelReverse", "IntaveJump", "IntaveTest", "Jump",
            "LegitSmart", "MatrixReduce", "MatrixSimple", "MatrixReverse", "MMC", "Reverse", "SmoothReverse", "Simple", "TestBuzzReverse"
    }, "Simple");

    // Simple
    private final BooleanValue simpleCancelTransaction = new BooleanValue("SimpleCancelTransactions", false);
    private final IntegerValue simpleCancelTransactionCount = new IntegerValue("SimpleCancelTransactionsCount", 6, 0, 20);

    // Reverse
    private final FloatValue reverseStrengthValue = new FloatValue("ReverseStrength", 1F, 0.1F, 1F);
    private final FloatValue reverse2StrengthValue = new FloatValue("SmoothReverseStrength", 0.05F, 0.02F, 0.1F);

    // AAC Push
    private final FloatValue aacPushXZReducerValue = new FloatValue("AACPushXZReducer", 2F, 1F, 3F);
    private final FloatValue AllowFirstStopTick = new FloatValue("AllowFirstStopTick", 2F, 1F, 10F);
    private final BooleanValue aacPushYReducerValue = new BooleanValue("AACPushYReducer", true);

    // AAC v4
    private final FloatValue aacv4MotionReducerValue = new FloatValue("AACv4MotionReducer", 0.62F, 0F, 1F);

    // Click
    private final IntegerValue clickCount = new IntegerValue("ClickCount", 2, 1, 10);
    private final IntegerValue clickTime = new IntegerValue("ClickMinHurtTime", 8, 1, 10);
    private final FloatValue clickRange = new FloatValue("ClickRange", 3.0F, 2.5F, 7F);
    private final BooleanValue clickOnPacket = new BooleanValue("ClickOnPacket", true);
    private final BooleanValue clickSwing = new BooleanValue("ClickSwing", false);
    private final BooleanValue clickFakeSwing = new BooleanValue("ClickFakeSwing", true);
    private final BooleanValue clickOnlyNoBlocking = new BooleanValue("ClickOnlyNoBlocking", false);

    // Explosion
    private final BooleanValue cancelExplosionPacket = new BooleanValue("CancelExplosionPacket", false);
    private final BooleanValue explosionCheck = new BooleanValue("ExplosionCheck", true);
    private final BooleanValue fireCheck = new BooleanValue("FireCheck", true);

    // GrimAC
    private final IntegerValue grimACTicks = new IntegerValue("OldGrimTicks", 0, 0, 10);
    private final MSTimer velocityTimer = new MSTimer();
    private final float intavex = 0f;
    private final float intavey = 0f;
    private final float intavez = 0f;
    private boolean velocityInput = false;
    private int grimTicks = 0;
    private int grimDisable = 0;
    private boolean explosion = false;
    // SmoothReverse
    private boolean reverseHurt = false;
    // Legit Smart
    private int jumped = 0;
    // AACPush
    private boolean jump = false;
    private int transactionCancelCount = 0;
    // MMC
    private int mmcTicks = 0;
    private boolean mmcLastCancel = false;
    private boolean mmcCanCancel = false;

    public AntiKnockback() {
        super("AntiKnockback", "Allows you to modify the amount of knockback you take.", ModuleCategory.COMBAT);
    }

    @Override
    public String getTag() {
        if ("Simple".equalsIgnoreCase(modeValue.get())) {
            return "H:" + (int) (horizontalValue.get() * 100) + "% V:" + (int) (verticalValue.get() * 100) + "%";
        } else {
            return modeValue.get();
        }
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.speedInAir = 0.02F;
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        EntityPlayer thePlayer = mc.thePlayer;
        if (thePlayer == null || thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.isInWeb) {
            return;
        }

        String mode = modeValue.get().toLowerCase(Locale.ENGLISH);

        switch (mode) {
            case "jump":
                if (thePlayer.hurtTime > 7 && thePlayer.onGround) {
                    thePlayer.jump();
                }
                break;
            case "glitch":
                thePlayer.noClip = velocityInput;
                if (thePlayer.hurtTime == 7) {
                    thePlayer.motionY = 0.4;
                }
                velocityInput = false;
                break;
            case "reverse":
                if (!velocityInput) return;
                if (!thePlayer.onGround) {
                    MovementUtils.strafe(MovementUtils.getSpeed() * reverseStrengthValue.get());
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false;
                }
                break;
            case "hypixelreverse":
                if (!velocityInput) return;
                if (!thePlayer.onGround) {
                    MovementUtils.strafe(MovementUtils.getSpeed() * reverseStrengthValue.get());
                } else if (velocityTimer.hasTimePassed(120L)) {
                    velocityInput = false;
                }
                break;
            case "smoothreverse":
                if (!velocityInput) {
                    thePlayer.speedInAir = 0.02F;
                    return;
                }
                if (thePlayer.hurtTime > 0) {
                    reverseHurt = true;
                }
                if (!thePlayer.onGround) {
                    if (reverseHurt) {
                        thePlayer.speedInAir = reverse2StrengthValue.get();
                    }
                } else if (velocityTimer.hasTimePassed(80L)) {
                    velocityInput = false;
                    reverseHurt = false;
                }
                break;
            case "aac":
                if (velocityInput && velocityTimer.hasTimePassed(80L)) {
                    thePlayer.motionX *= horizontalValue.get();
                    thePlayer.motionZ *= horizontalValue.get();
                    velocityInput = false;
                }
                break;
            case "aacv4":
                if (thePlayer.hurtTime > 0 && !thePlayer.onGround) {
                    float reduce = aacv4MotionReducerValue.get();
                    thePlayer.motionX *= reduce;
                    thePlayer.motionZ *= reduce;
                }
                break;
            case "aacpush":
                if (jump) {
                    if (thePlayer.onGround) {
                        jump = false;
                    }
                } else {
                    if (thePlayer.hurtTime > 0 && thePlayer.motionX != 0 && thePlayer.motionZ != 0) {
                        thePlayer.onGround = true;
                    }
                    if (thePlayer.hurtResistantTime > 0 && aacPushYReducerValue.get()) {
                        thePlayer.motionY -= 0.014999993;
                    }
                }
                if (thePlayer.hurtResistantTime >= 19) {
                    float reduce = aacPushXZReducerValue.get();
                    thePlayer.motionX /= reduce;
                    thePlayer.motionZ /= reduce;
                }
                break;
            case "aaczero":
                if (thePlayer.hurtTime > 0) {
                    if (!velocityInput || thePlayer.onGround || thePlayer.fallDistance > 2F)
                        return;
                    thePlayer.motionY -= 1.0;
                    thePlayer.isAirBorne = true;
                    thePlayer.onGround = true;
                } else {
                    velocityInput = false;
                }
                break;
            case "matrixreduce":
                if (thePlayer.hurtTime > 0) {
                    if (thePlayer.onGround) {
                        if (thePlayer.hurtTime <= 6) {
                            thePlayer.motionX *= 0.70;
                            thePlayer.motionZ *= 0.70;
                        }
                        if (thePlayer.hurtTime <= 5) {
                            thePlayer.motionX *= 0.80;
                            thePlayer.motionZ *= 0.80;
                        }
                    } else if (thePlayer.hurtTime <= 10) {
                        thePlayer.motionX *= 0.60;
                        thePlayer.motionZ *= 0.60;
                    }
                }
                break;
            case "allowfirst":
                if (velocityInput && thePlayer.hurtTime == AllowFirstStopTick.get()) {
                    velocityInput = false;
                    thePlayer.setVelocity(0.0, 0.0, 0.0);
                }
                break;
            case "click":
                if (velocityInput && thePlayer.hurtTime >= clickTime.get()) {
                    if (!attackRayTrace(clickCount.get(), clickRange.get(), thePlayer.isSprinting())) {
                        if (clickFakeSwing.get()) {
                            mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                        }
                        velocityInput = false;
                    }
                } else {
                    velocityInput = false;
                }
                break;
            case "legitsmart":
                if (velocityInput) {
                    if (thePlayer.onGround && thePlayer.hurtTime == 9 && thePlayer.isSprinting() && mc.currentScreen == null) {
                        if (jumped > 2) {
                            jumped = 0;
                        } else {
                            jumped++;
                            if (thePlayer.ticksExisted % 5 != 0) {
                                mc.gameSettings.keyBindJump.pressed = true;
                            }
                        }
                    } else if (thePlayer.hurtTime == 8) {
                        mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
                        velocityInput = false;
                    }
                }
                break;
            case "mmc":
                mmcTicks++;
                if (mmcTicks > 23) {
                    mmcCanCancel = true;
                }
                if (mmcTicks >= 2 && mmcTicks <= 4 && !mmcLastCancel) {
                    thePlayer.motionX *= 0.99;
                    thePlayer.motionZ *= 0.99;
                } else if (mmcTicks == 5 && !mmcLastCancel) {
                    MovementUtils.strafe();
                }
                break;
            case "down":
                if (velocityInput && velocityTimer.hasTimePassed(80)) {
                    if (!thePlayer.onGround) {
                        double reducer = (Math.random() - 0.5) / 50.0 + 0.2;
                        thePlayer.motionY *= reducer;
                    }
                    velocityInput = false;
                }
                break;
            case "blockcollection":
                if (velocityInput) {
                    if (thePlayer.hurtTime < 2) velocityInput = false;
                }
                break;
            case "grimdig":
                --grimDisable;
                if (grimTicks > 0) {
                    if (velocityInput) {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                        velocityInput = false;
                    }
                    --grimTicks;
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                            new BlockPos(thePlayer.posX, thePlayer.posY, thePlayer.posZ), EnumFacing.UP));
                }
                break;
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        EntityPlayer thePlayer = mc.thePlayer;
        if (thePlayer == null) return;

        Object packet = event.getPacket();

        if (packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity velPacket = (S12PacketEntityVelocity) packet;
            Entity entity = mc.theWorld.getEntityByID(velPacket.getEntityID());
            if (entity != thePlayer) return;

            velocityTimer.reset();

            String mode = modeValue.get().toLowerCase(Locale.ENGLISH);

            switch (mode) {
                case "simple":
                    if (explosion && explosionCheck.get()) {
                        explosion = false;
                        return;
                    }
                    float h = horizontalValue.get();
                    float v = verticalValue.get();
                    if (h == 0 && v == 0) event.cancelEvent();
                    velPacket.motionX = (int) (velPacket.motionX * h);
                    velPacket.motionY = (int) (velPacket.motionY * v);
                    velPacket.motionZ = (int) (velPacket.motionZ * h);
                    if (simpleCancelTransaction.get()) {
                        transactionCancelCount = simpleCancelTransactionCount.get();
                    }
                    break;
                case "cancel":
                    event.cancelEvent();
                    break;
                case "aac":
                case "reverse":
                case "smoothreverse":
                case "aaczero":
                case "allowfirst":
                case "down":
                case "intavejump":
                case "blockcollection":
                    velocityInput = true;
                    break;
                case "hypixelreverse":
                    if (MovementUtils.isMoving()) {
                        velocityInput = true;
                    } else {
                        velPacket.motionX = 0;
                        velPacket.motionZ = 0;
                    }
                    break;
                case "legitsmart":
                    if (velPacket.motionX * velPacket.motionX + velPacket.motionZ * velPacket.motionZ + velPacket.motionY * velPacket.motionY > 640000) {
                        velocityInput = true;
                    }
                    break;
                case "aac5packet":
                    // What the actual fuck
                    if (mc.isIntegratedServerRunning()) return;
                    if (thePlayer.isBurning() && fireCheck.get()) return;
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(
                            thePlayer.posX, Double.MAX_VALUE, thePlayer.posZ, thePlayer.onGround));
                    event.cancelEvent();
                    break;
                case "glitch":
                    if (!thePlayer.onGround) return;
                    velocityInput = true;
                    event.cancelEvent();
                    break;
                case "matrixsimple":
                    velPacket.motionX = (int) (velPacket.motionX * 0.36);
                    velPacket.motionZ = (int) (velPacket.motionZ * 0.36);
                    if (thePlayer.onGround) {
                        velPacket.motionX = (int) (velPacket.motionX * 0.9);
                        velPacket.motionZ = (int) (velPacket.motionZ * 0.9);
                    }
                    break;
                case "matrixreverse":
                    velPacket.motionX = (int) (-velPacket.motionX * 0.3);
                    velPacket.motionZ = (int) (-velPacket.motionZ * 0.3);
                    break;
                case "testbuzzreverse":
                    velPacket.motionX = -velPacket.motionX;
                    velPacket.motionZ = -velPacket.motionZ;
                    break;
                case "mmc":
                    mmcTicks = 0;
                    if (mmcCanCancel) {
                        event.cancelEvent();
                        mmcLastCancel = true;
                        mmcCanCancel = false;
                    } else {
                        thePlayer.jump();
                        mmcLastCancel = false;
                    }
                    break;
                case "click":
                    if (velPacket.motionX == 0 && velPacket.motionZ == 0) return;
                    if (attackRayTrace(clickCount.get(), clickRange.get(), clickOnPacket.get() && thePlayer.isSprinting()))
                        velocityInput = true;
                    break;
                case "grimdig":
                    if (grimDisable > 0) return;
                    event.cancelEvent();
                    velocityInput = true;
                    grimTicks = grimACTicks.get();
                    break;
            }

        } else if (packet instanceof S27PacketExplosion) {
            S27PacketExplosion expPacket = (S27PacketExplosion) packet;
            if (expPacket.func_149149_c() != 0F || expPacket.func_149144_d() != 0F || expPacket.func_149147_e() != 0F) {
                explosion = true;
            }
            if (cancelExplosionPacket.get()) event.cancelEvent();
        } else if (packet instanceof C0FPacketConfirmTransaction) {
            if (transactionCancelCount > 0) {
                transactionCancelCount--;
                event.cancelEvent();
            }
        }
    }

    @EventTarget
    public void onJump(JumpEvent event) {
        EntityPlayer thePlayer = mc.thePlayer;
        if (thePlayer == null || thePlayer.isInWater() || thePlayer.isInLava() || thePlayer.isInWeb)
            return;

        String mode = modeValue.get().toLowerCase(Locale.ENGLISH);

        if ("aacpush".equals(mode)) {
            jump = true;
            if (!thePlayer.isCollidedVertically) event.cancelEvent();
        } else if ("aaczero".equals(mode) && thePlayer.hurtTime > 0) {
            event.cancelEvent();
        }
    }

    @EventTarget
    public void onBB(BlockBBEvent event) {
        if ("blockcollection".equals(modeValue.get().toLowerCase(Locale.ENGLISH))) {
            if (velocityInput && event.getBlock() instanceof BlockAir) {
                double x = event.getX();
                double y = event.getY();
                double z = event.getZ();
                if (y == Math.floor(mc.thePlayer.posY) + 1) {
                    event.setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 0, 1).offset(x, y, z));
                }
            }
        }
    }

    public final boolean attackRayTrace(int attack, double range, boolean doAttack) {
        if (ClientModule.mc.thePlayer == null) {
            return false;
        } else if (!this.clickOnlyNoBlocking.get() || !ClientModule.mc.thePlayer.isBlocking() && !ClientModule.mc.thePlayer.isUsingItem() && !KevinClient.INSTANCE.getModuleManager().get(KillAura.class).getBlockingStatus()) {
            Entity raycastedEntity = RaycastUtils.raycastEntity(range + (double) 1, new RaycastUtils.EntityFilter() {
                public boolean canRaycast(Entity entity) {
                    return entity != null && entity instanceof EntityLivingBase;
                }
            });
            if (raycastedEntity == null) {
                return false;
            } else {
                Entity it = raycastedEntity;
                if (!(raycastedEntity instanceof EntityPlayer)) {
                    return true;
                } else {
                    AxisAlignedBB var9 = raycastedEntity.getEntityBoundingBox();

                    AxisAlignedBB var10000 = OtherExtensionsKt.expands(var9, raycastedEntity.getCollisionBorderSize(), false, false);
                    EntityPlayerSP var12 = ClientModule.mc.thePlayer;

                    if (OtherExtensionsKt.getLookingTargetRange(var10000, var12, null, 0.0F) > range) {
                        return false;
                    } else {
                        if (doAttack) {
                            KevinClient.INSTANCE.getEventManager().callEvent(new AttackEvent(raycastedEntity));

                            for (int var13 = 0; var13 < attack; ++var13) {
                                if (this.clickSwing.get()) {
                                    ClientModule.mc.thePlayer.swingItem();
                                } else {
                                    ClientModule.mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                                }

                                ClientModule.mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(it, C02PacketUseEntity.Action.ATTACK));
                            }

                            ClientModule.mc.thePlayer.attackTargetEntityWithCurrentItem(it);
                        }

                        return true;
                    }
                }
            }
        } else {
            return true;
        }
    }
}