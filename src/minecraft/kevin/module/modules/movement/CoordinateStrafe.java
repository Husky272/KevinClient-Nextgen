package kevin.module.modules.movement;

import kevin.event.*;
import kevin.event.impl.MotionEvent;
import kevin.event.impl.MoveEvent;
import kevin.event.struct.EventState;
import kevin.main.KevinClient;
import kevin.module.*;
import kevin.utils.MovementUtils;
import kevin.utils.entity.rotation.RotationUtils;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

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
    private final IntegerValue posY = new IntegerValue("PosY", 64, 0, 0xff);
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
                    // Radius check
                    mc.thePlayer.getDistanceSqToCenter(
                            new BlockPos(posX.get(), posY.get(),posZ.get())
                    ) < radius.get() ? -1.0 : 1.0
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
        // Render a circle around the target block position
        if (render.get()) {
            GL11.glPushMatrix();
            GL11.glDisable(GL_TEXTURE_2D);// Disable texture rendering
            GL11.glEnable(GL_LINE_SMOOTH);// Enable line smoothing
            GL11.glEnable(GL_POLYGON_SMOOTH);// Enable depth testing
            GL11.glEnable(GL11.GL_POINT_SMOOTH);// Enable point smoothing
            GL11.glEnable(GL_BLEND);// Enable blending
            GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);// Set blending function
            GL11.glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);// Set hint for line smoothing
            GL11.glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);// Set hint for point smoothing
            GL11.glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);// Set hint for polygon smoothing
            GL11.glDisable(GL_DEPTH_TEST);// Disable depth buffer
            GL11.glDepthMask(false);
            GL11.glLineWidth(1.0f);

            GL11.glBegin(3);
            double x = posX.get() + (0) * event.getPartialTicks() - mc.getRenderManager().viewerPosX;
            double y = posY.get() + (0) * event.getPartialTicks() - mc.getRenderManager().viewerPosY;
            double z = posZ.get() + (0) * event.getPartialTicks() - mc.getRenderManager().viewerPosZ;

            for (int i = 0; i <= 360; i++) {
                Color rainbow = new Color(Color.HSBtoRGB((float) ((mc.thePlayer.ticksExisted / 70.0 + Math.sin(i / 50.0 * 1.75)) % 1.0f), 0.7f, 1.0f));
                GL11.glColor3f(rainbow.getRed() / 255.0f, rainbow.getGreen() / 255.0f, rainbow.getBlue() / 255.0f);
                GL11.glVertex3d(x + radius.get() * Math.cos(i * 6.283185307179586 / 45.0), y, z + radius.get() * Math.sin(i * 6.283185307179586 / 45.0));
            }
            GL11.glEnd();

            GL11.glDepthMask(true);
            GL11.glEnable(GL_DEPTH_TEST);
            GL11.glDisable(GL_LINE_SMOOTH);
            GL11.glDisable(GL_POLYGON_SMOOTH);
            GL11.glEnable(GL_POINT_SMOOTH);
            GL11.glEnable(GL_TEXTURE_2D);
            GL11.glPopMatrix();
            // Reset OpenGL state
        }
    }

    private boolean canStrafe() {
        return this.getState() && (!space.get() || mc.thePlayer.movementInput.jump) && (!onlySpeed.get() || KevinClient.moduleManager.getModule(Speed.class).getState()) && (hurtTime.get() <= mc.thePlayer.hurtTime);
    }
}