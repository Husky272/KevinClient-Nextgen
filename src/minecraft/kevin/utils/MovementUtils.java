package kevin.utils;

import kotlin.Metadata;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import org.jetbrains.annotations.NotNull;


public final class MovementUtils extends MinecraftInstance {


    public static boolean isMoving = true;

    public static double direction = 0.0d;

    public static double movingYaw = 0.0d;

    public static double speed = 0.0d;

    static{
        isMoving = isMoving();
        direction = getDirection();
        movingYaw = getMovingYaw();
        speed = getSpeed();
    }




    @NotNull
    public static final MovementUtils INSTANCE = new MovementUtils();

    private MovementUtils() {
    }

    public static float getSpeed() {
        EntityPlayerSP var10000 = MinecraftInstance.mc.thePlayer;
        Intrinsics.checkNotNull(var10000);
        double var0 = var10000.motionX;
        EntityPlayerSP var10001 = MinecraftInstance.mc.thePlayer;
        Intrinsics.checkNotNull(var10001);
        var0 *= var10001.motionX;
        var10001 = MinecraftInstance.mc.thePlayer;
        Intrinsics.checkNotNull(var10001);
        double var3 = var10001.motionZ;
        EntityPlayerSP var10002 = MinecraftInstance.mc.thePlayer;
        Intrinsics.checkNotNull(var10002);
        return (float)Math.sqrt(var0 + var3 * var10002.motionZ);
    }

    public static void setSpeed(float value) {
        setMotion(value);
    }

    /** @deprecated */
    // $FF: synthetic method

    public static void getSpeed$annotations() {
    }

    public static boolean isMoving() {
        boolean var1;
        label38: {
            if (MinecraftInstance.mc.thePlayer != null) {
                EntityPlayerSP var10000 = MinecraftInstance.mc.thePlayer;
                Intrinsics.checkNotNull(var10000);
                if (var10000.movementInput.moveForward != 0.0F) {
                    break label38;
                }

                var10000 = MinecraftInstance.mc.thePlayer;
                Intrinsics.checkNotNull(var10000);
                if (var10000.movementInput.moveStrafe != 0.0F) {
                    break label38;
                }
            }

            var1 = false;
            return var1;
        }

        var1 = true;
        return var1;
    }

    /** @deprecated */
    // $FF: synthetic method

    public static void isMoving$annotations() {
    }

    public static boolean hasMotion() {
        EntityPlayerSP var10000 = MinecraftInstance.mc.thePlayer;
        Intrinsics.checkNotNull(var10000);
        boolean var3;
        if (var10000.motionX != (double)0.0F) {
            EntityPlayerSP var1 = MinecraftInstance.mc.thePlayer;
            Intrinsics.checkNotNull(var1);
            if (var1.motionZ != (double)0.0F) {
                var1 = MinecraftInstance.mc.thePlayer;
                Intrinsics.checkNotNull(var1);
                if (var1.motionY != (double)0.0F) {
                    var3 = true;
                    return var3;
                }
            }
        }

        var3 = false;
        return var3;
    }


    public static void strafe(float speed) {
        MovementUtils var10000 = INSTANCE;
        if (isMoving()) {
            var10000 = INSTANCE;
            double yaw = getDirection();
            EntityPlayerSP var5 = MinecraftInstance.mc.thePlayer;
            Intrinsics.checkNotNull(var5);
            EntityPlayerSP thePlayer = var5;
            thePlayer.motionX = -Math.sin(yaw) * (double)speed;
            thePlayer.motionZ = Math.cos(yaw) * (double)speed;
        }
    }

    // $FF: synthetic method
    public static void strafe$default(float var0, int var1, Object var2) {
        if ((var1 & 1) != 0) {
            MovementUtils var10000 = INSTANCE;
            var0 = getSpeed();
        }

        strafe(var0);
    }


    public static void strafeDouble(double speed) {
        MovementUtils var10000 = INSTANCE;
        if (isMoving()) {
            var10000 = INSTANCE;
            double yaw = getDirection();
            EntityPlayerSP var6 = MinecraftInstance.mc.thePlayer;
            Intrinsics.checkNotNull(var6);
            EntityPlayerSP thePlayer = var6;
            thePlayer.motionX = -Math.sin(yaw) * speed;
            thePlayer.motionZ = Math.cos(yaw) * speed;
        }
    }


    public static void forward(double length) {
        EntityPlayerSP var10000 = MinecraftInstance.mc.thePlayer;
        Intrinsics.checkNotNull(var10000);
        EntityPlayerSP thePlayer = var10000;
        double yaw = Math.toRadians(thePlayer.rotationYaw);
        thePlayer.setPosition(thePlayer.posX + -Math.sin(yaw) * length, thePlayer.posY, thePlayer.posZ + Math.cos(yaw) * length);
    }

    public static double getDirection() {
        EntityPlayerSP var10000 = MinecraftInstance.mc.thePlayer;
        Intrinsics.checkNotNull(var10000);
        EntityPlayerSP thePlayer = var10000;
        float rotationYaw = thePlayer.rotationYaw;
        if (thePlayer.moveForward < 0.0F) {
            rotationYaw += 180.0F;
        }

        float forward = 1.0F;
        if (thePlayer.moveForward < 0.0F) {
            forward = -0.5F;
        } else if (thePlayer.moveForward > 0.0F) {
            forward = 0.5F;
        }

        if (thePlayer.moveStrafing > 0.0F) {
            rotationYaw -= 90.0F * forward;
        }

        if (thePlayer.moveStrafing < 0.0F) {
            rotationYaw += 90.0F * forward;
        }

        return Math.toRadians(rotationYaw);
    }

    /** @deprecated */
    // $FF: synthetic method

    public static void getDirection$annotations() {
    }

    public static float getMovingYaw() {
        return (float)(getDirection() * (double)180.0F / Math.PI);
    }

    public  static void move(float speed) {
        if (isMoving()) {
            double yaw = getDirection();
            EntityPlayerSP var4 = MinecraftInstance.mc.thePlayer;
            var4.motionX += -Math.sin(yaw) * (double)speed;
            var4 = MinecraftInstance.mc.thePlayer;
            var4.motionZ += Math.cos(yaw) * (double)speed;
        }
    }

    public static void setMotion(double speed) {
        double forward = MinecraftInstance.mc.thePlayer.movementInput.moveForward;
        double strafe = MinecraftInstance.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MinecraftInstance.mc.thePlayer.rotationYaw;
        if (forward == (double)0.0F && strafe == (double)0.0F) {
            MinecraftInstance.mc.thePlayer.motionX = 0.0F;
            MinecraftInstance.mc.thePlayer.motionZ = 0.0F;
        } else {
            if (forward != (double)0.0F) {
                if (strafe > (double)0.0F) {
                    yaw += (float)(forward > (double)0.0F ? -45 : 45);
                } else if (strafe < (double)0.0F) {
                    yaw += (float)(forward > (double)0.0F ? 45 : -45);
                }

                strafe = 0.0F;
                if (forward > (double)0.0F) {
                    forward = 1.0F;
                } else if (forward < (double)0.0F) {
                    forward = -1.0F;
                }
            }

            double cos = Math.cos(Math.toRadians(yaw + 90.0F));
            double sin = Math.sin(Math.toRadians(yaw + 90.0F));
            MinecraftInstance.mc.thePlayer.motionX = forward * speed * cos + strafe * speed * sin;
            MinecraftInstance.mc.thePlayer.motionZ = forward * speed * sin - strafe * speed * cos;
        }

    }


    public static double getJumpMotion(double base) {
        return MinecraftInstance.mc.thePlayer.isPotionActive(Potion.jump) ? base + (double)(MinecraftInstance.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1 : base;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (MinecraftInstance.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= (double)1.0F + 0.2 * (double)(MinecraftInstance.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }

        return baseSpeed;
    }

    public static double getMoveSpeed(double baseSpeed) {
        return MinecraftInstance.mc.thePlayer.isPotionActive(Potion.moveSpeed) ? baseSpeed * ((double)1.0F + 0.2 * (double)(MinecraftInstance.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1)) : baseSpeed;
    }

    public static void resetMotion(boolean y) {
        MinecraftInstance.mc.thePlayer.motionX = 0.0F;
        MinecraftInstance.mc.thePlayer.motionZ = 0.0F;
        if (y) {
            MinecraftInstance.mc.thePlayer.motionY = 0.0F;
        }

    }


    public static void strafe() {
        strafe$default(0.0F, 1, null);
    }
}
