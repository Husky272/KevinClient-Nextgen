package kevin.module.modules.movement;

import kevin.event.*;
import kevin.main.KevinClient;
import kevin.module.*;
import kevin.utils.MovementUtils;
import kevin.utils.entity.rotation.RotationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;

// TODO: fix
public final class CoordinateStrafe extends ClientModule {
    private final FloatValue radius = new FloatValue("Radius", 2.0f, 0.1f, 4.0f);
    private final FloatValue strengthValue = new FloatValue("Strength", 0.5F, 0F, 1F);
    private final BooleanValue render = new BooleanValue("Render", true);
    private final BooleanValue space = new BooleanValue("HoldSpace", false);
    private final IntegerValue hurtTime = new IntegerValue("MinHurtTime", 0, 0, 9);
    private final BooleanValue safewalk = new BooleanValue("SafeWalk", true);
    private final BooleanValue onlySpeed = new BooleanValue("OnlySpeed", false);

    private final IntegerValue posX = new IntegerValue("PosX", 0, -1000, 1000);
    private final IntegerValue posY = new IntegerValue("PosY", 0, 0, 0xff);
    private final IntegerValue posZ = new IntegerValue("PosZ", 0, -1000, 1000);
    @NotNull
    private int direction = -1;

    public CoordinateStrafe() {
        super("CoordinateStrafe", "Strafe around a block.", ModuleCategory.MOVEMENT);
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (event.getEventState() == EventState.PRE) {
            if (mc.thePlayer.isCollidedHorizontally) {
                switchDirection();
            }
            if (mc.gameSettings.keyBindLeft.isKeyDown()) {
                direction = 1;
            }
            if (mc.gameSettings.keyBindRight.isKeyDown()) {
                direction = -1;
            }
        }
    }

    private void switchDirection() {
        direction = direction == 1 ? -1 : 1;
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (safewalk.get() && mc.thePlayer.onGround && canStrafe()) {
            event.setSafeWalk(true);
        }


        if (canStrafe())
            setSpeed(
                    event,
                    MovementUtils.getSpeed(),
                    // Get rotation to target block position(x, y, z)
                    RotationUtils.getRotationsBlock(posX.get(), posY.get(), posZ.get()).getYaw(),
                    direction,
                    mc.thePlayer.getDistanceSqToCenter(new BlockPos(posX.get(), posY.get(),posZ.get())) <= radius.get() ? 0.0 : 1.0
            );
    }

    private void setSpeed(MoveEvent moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;

        if (forward == 0 && strafe == 0) {
            moveEvent.setZ(0);
            moveEvent.setX(0);
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            double cos = Math.cos(Math.toRadians(yaw + 90.0));
            double sin = Math.sin(Math.toRadians(yaw + 90.0));

            double speed = moveSpeed * strengthValue.get();
            double motionX = (mc.thePlayer.motionX * (1 - strengthValue.get()));
            double motionZ = (mc.thePlayer.motionZ * (1 - strengthValue.get()));

            moveEvent.setX((forward * speed * cos + strafe * speed * sin) + motionX);
            moveEvent.setZ((forward * speed * sin - strafe * speed * cos) + motionZ);
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        EntityLivingBase target = KevinClient.combatManager.getTarget();
        if (render.get()) {
            if(target == null) return;
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glEnable(2881);
            GL11.glEnable(2832);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glHint(3154, 4354);
            GL11.glHint(3155, 4354);
            GL11.glHint(3153, 4354);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glLineWidth(1.0f);

            GL11.glBegin(3);
            double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * event.getPartialTicks() - mc.getRenderManager().viewerPosX;
            double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * event.getPartialTicks() - mc.getRenderManager().viewerPosY;
            double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * event.getPartialTicks() - mc.getRenderManager().viewerPosZ;

            for (int i = 0; i <= 360; i++) {
                Color rainbow = new Color(Color.HSBtoRGB((float) ((mc.thePlayer.ticksExisted / 70.0 + Math.sin(i / 50.0 * 1.75)) % 1.0f), 0.7f, 1.0f));
                GL11.glColor3f(rainbow.getRed() / 255.0f, rainbow.getGreen() / 255.0f, rainbow.getBlue() / 255.0f);
                GL11.glVertex3d(x + radius.get() * Math.cos(i * 6.283185307179586 / 45.0), y, z + radius.get() * Math.sin(i * 6.283185307179586 / 45.0));
            }
            GL11.glEnd();

            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(2881);
            GL11.glEnable(2832);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
        }
    }

    private boolean canStrafe() {
        return this.getState() && (!space.get() || mc.thePlayer.movementInput.jump) && (!onlySpeed.get() || KevinClient.moduleManager.getModule(Speed.class).getState()) && (hurtTime.get() <= mc.thePlayer.hurtTime);
    }
}