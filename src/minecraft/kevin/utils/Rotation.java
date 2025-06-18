// Rotation.java
package kevin.utils;

import kevin.event.impl.StrafeEvent;
import kevin.utils.entity.rotation.RotationUtils;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


//
//
//data class Rotation(var yaw: Float, var pitch: Float) : MinecraftInstance() {
//
//
//
//    /**
//     * Set rotations to [player]
//     */
//    fun toPlayer(player: EntityPlayer) {
//        if (yaw.isNaN() || pitch.isNaN())
//            return
//
//                    fixedSensitivity(mc.gameSettings.mouseSensitivity)
//
//        player.rotationYaw = yaw
//        player.rotationPitch = pitch
//    }
//
//    /**
//     * Patch gcd exploit in aim
//     *
//     * @see net.minecraft.client.renderer.EntityRenderer.updateCameraAndRender
//     */
//    @JvmOverloads
//    fun fixedSensitivity(sensitivity: Float = mc.gameSettings.mouseSensitivity): Rotation {
//        val f = sensitivity * 0.6F + 0.2F
//        val gcd = f * f * f * 1.2F
//
//        // get previous rotation
//        val rotation = RotationUtils.serverRotation
//
//        // fix yaw
//        var deltaYaw = yaw - rotation.yaw
//        deltaYaw -= deltaYaw % gcd
//        yaw = rotation.yaw + deltaYaw
//
//        // fix pitch
//        var deltaPitch = pitch - rotation.pitch
//        deltaPitch -= deltaPitch % gcd
//        pitch = rotation.pitch + deltaPitch
//        return this
//    }
//
//    /**
//     * Apply strafe to player
//     *
//     * @author bestnub
//     */
//    fun applyStrafeToPlayer(event: StrafeEvent) {
//        val player = mc.thePlayer!!
//
//                val dif = ((MathHelper.wrapAngleTo180_float(player.rotationYaw - this.yaw
//                - 23.5f - 135)
//                + 180) / 45).toInt()
//
//        val yaw = this.yaw
//
//        val strafe = event.strafe
//        val forward = event.forward
//        val friction = event.friction
//
//        var calcForward = 0f
//        var calcStrafe = 0f
//
//        when (dif) {
//            0 -> {
//                calcForward = forward
//                calcStrafe = strafe
//            }
//            1 -> {
//                calcForward += forward
//                calcStrafe -= forward
//                calcForward += strafe
//                calcStrafe += strafe
//            }
//            2 -> {
//                calcForward = strafe
//                calcStrafe = -forward
//            }
//            3 -> {
//                calcForward -= forward
//                calcStrafe -= forward
//                calcForward += strafe
//                calcStrafe -= strafe
//            }
//            4 -> {
//                calcForward = -forward
//                calcStrafe = -strafe
//            }
//            5 -> {
//                calcForward -= forward
//                calcStrafe += forward
//                calcForward -= strafe
//                calcStrafe -= strafe
//            }
//            6 -> {
//                calcForward = -strafe
//                calcStrafe = forward
//            }
//            7 -> {
//                calcForward += forward
//                calcStrafe += forward
//                calcForward -= strafe
//                calcStrafe += strafe
//            }
//        }
//
//        if (calcForward > 1f || calcForward < 0.9f && calcForward > 0.3f || calcForward < -1f || calcForward > -0.9f && calcForward < -0.3f) {
//            calcForward *= 0.5f
//        }
//
//        if (calcStrafe > 1f || calcStrafe < 0.9f && calcStrafe > 0.3f || calcStrafe < -1f || calcStrafe > -0.9f && calcStrafe < -0.3f) {
//            calcStrafe *= 0.5f
//        }
//
//        var d = calcStrafe * calcStrafe + calcForward * calcForward
//
//        if (d >= 1.0E-4f) {
//            d = sqrt(d)
//            if (d < 1.0f) d = 1.0f
//            d = friction / d
//            calcStrafe *= d
//            calcForward *= d
//            val yawSin = sin((yaw * Math.PI / 180f).toFloat())
//            val yawCos = cos((yaw * Math.PI / 180f).toFloat())
//            player.motionX += calcStrafe * yawCos - calcForward * yawSin.toDouble()
//            player.motionZ += calcForward * yawCos + calcStrafe * yawSin.toDouble()
//        }
//    }
//
//    fun applyVanillaStrafeToPlayer(event: StrafeEvent) {
//        val player = mc.thePlayer!!
//
//                val dif = ((MathHelper.wrapAngleTo180_float(player.rotationYaw - this.yaw
//                - 23.5f - 135)
//                + 180) / 45).toInt()
//
//        val yaw = this.yaw
//
//        val strafe = event.strafe
//        val forward = event.forward
//        val friction = event.friction
//
//        var calcForward = 0f
//        var calcStrafe = 0f
//
//        when (dif) {
//            0 -> {
//                calcForward = forward
//                calcStrafe = strafe
//            }
//            1 -> {
//                calcForward += forward
//                calcStrafe -= forward
//                calcForward += strafe
//                calcStrafe += strafe
//            }
//            2 -> {
//                calcForward = strafe
//                calcStrafe = -forward
//            }
//            3 -> {
//                calcForward -= forward
//                calcStrafe -= forward
//                calcForward += strafe
//                calcStrafe -= strafe
//            }
//            4 -> {
//                calcForward = -forward
//                calcStrafe = -strafe
//            }
//            5 -> {
//                calcForward -= forward
//                calcStrafe += forward
//                calcForward -= strafe
//                calcStrafe -= strafe
//            }
//            6 -> {
//                calcForward = -strafe
//                calcStrafe = forward
//            }
//            7 -> {
//                calcForward += forward
//                calcStrafe += forward
//                calcForward -= strafe
//                calcStrafe += strafe
//            }
//        }
//
//        if (calcForward > 1f || calcForward < 0.9f && calcForward > 0.3f || calcForward < -1f || calcForward > -0.9f && calcForward < -0.3f) {
//            calcForward *= 0.5f
//        }
//
//        if (calcStrafe > 1f || calcStrafe < 0.9f && calcStrafe > 0.3f || calcStrafe < -1f || calcStrafe > -0.9f && calcStrafe < -0.3f) {
//            calcStrafe *= 0.5f
//        }
//
//        var d = calcStrafe * calcStrafe + calcForward * calcForward
//
//        if (d >= 1.0E-4f) {
//            d = MathHelper.sqrt_float(d)
//            if (d < 1.0f) d = 1.0f
//            d = friction / d
//            calcStrafe *= d
//            calcForward *= d
//            val yawSin = MathHelper.sin(yaw * Math.PI.toFloat() / 180f)
//            val yawCos = MathHelper.cos(yaw * Math.PI.toFloat() / 180f)
//            player.motionX += calcStrafe * yawCos - calcForward * yawSin
//            player.motionZ += calcForward * yawCos + calcStrafe * yawSin
//        }
//    }
//
//    fun applyStrictSilentStrafeForPlayer(event: StrafeEvent) {}
//
//    fun toDirection(): Vec3 {
//        val f: Float = MathHelper.cos(-yaw * 0.017453292f - Math.PI.toFloat())
//        val f1: Float = MathHelper.sin(-yaw * 0.017453292f - Math.PI.toFloat())
//        val f2: Float = -MathHelper.cos(-pitch * 0.017453292f)
//        val f3: Float = MathHelper.sin(-pitch * 0.017453292f)
//        return Vec3((f1 * f2).toDouble(), f3.toDouble(), (f * f2).toDouble())
//    }
//
//    fun cloneSelf(): Rotation {
//        return Rotation(yaw, pitch)
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (other == null) return false
//        if (other is Array<*>) {
//            if (other.size != 2) return false
//            if (other[0] !is Number || other[1] !is Number) return false
//            return other[0] == yaw && other[1] == pitch
//        }
//        if (other !is Rotation) return false
//        return other.yaw == this.yaw && other.pitch == this.pitch
//    }
//
//    override fun hashCode(): Int {
//        return (yaw * 25566).toInt().shl(10) + (pitch * 25566).toInt()
//    }
//}
//
public final class Rotation extends MinecraftInstance {
    private float yaw;
    private float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public Rotation(double yaw, double pitch) {
        this.yaw = (float) yaw;
        this.pitch = (float) pitch;
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final void setYaw(float var1) {
        this.yaw = var1;
    }

    public final float getPitch() {
        return this.pitch;
    }

    public final void setPitch(float var1) {
        this.pitch = var1;
    }

    public final void toPlayer(@NotNull EntityPlayer player) {
        Intrinsics.checkNotNullParameter(player, "player");
        if (!Float.isNaN(this.yaw) && !Float.isNaN(this.pitch)) {
            this.fixedSensitivity(MinecraftInstance.mc.gameSettings.mouseSensitivity);
            player.rotationYaw = this.yaw;
            player.rotationPitch = this.pitch;
        }
    }

    @JvmOverloads
    @NotNull
    public final Rotation fixedSensitivity(float sensitivity) {
        float f = sensitivity * 0.6F + 0.2F;
        float gcd = f * f * f * 1.2F;
        Rotation rotation = RotationUtils.serverRotation;
        float deltaYaw = this.yaw - rotation.yaw;
        deltaYaw -= deltaYaw % gcd;
        this.yaw = rotation.yaw + deltaYaw;
        float deltaPitch = this.pitch - rotation.pitch;
        deltaPitch -= deltaPitch % gcd;
        this.pitch = rotation.pitch + deltaPitch;
        return this;
    }

    // $FF: synthetic method
    public static Rotation fixedSensitivity$default(Rotation var0, float var1, int var2, Object var3) {
        if ((var2 & 1) != 0) {
            var1 = MinecraftInstance.mc.gameSettings.mouseSensitivity;
        }

        return var0.fixedSensitivity(var1);
    }

    public final void applyStrafeToPlayer(@NotNull StrafeEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        EntityPlayerSP var10000 = MinecraftInstance.mc.thePlayer;
        Intrinsics.checkNotNull(var10000);
        EntityPlayerSP player = var10000;
        int dif = (int)((MathHelper.wrapAngleTo180_float(player.rotationYaw - this.yaw - 23.5F - (float)135) + (float)180) / (float)45);
        float yaw = this.yaw;
        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();
        float calcForward = 0.0F;
        float calcStrafe = 0.0F;
        switch (dif) {
            case 0:
                calcForward = forward;
                calcStrafe = strafe;
                break;
            case 1:
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            case 2:
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            case 3:
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            case 4:
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            case 5:
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            case 6:
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            case 7:
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
        }

        if (calcForward > 1.0F || calcForward < 0.9F && calcForward > 0.3F || calcForward < -1.0F || calcForward > -0.9F && calcForward < -0.3F) {
            calcForward *= 0.5F;
        }

        if (calcStrafe > 1.0F || calcStrafe < 0.9F && calcStrafe > 0.3F || calcStrafe < -1.0F || calcStrafe > -0.9F && calcStrafe < -0.3F) {
            calcStrafe *= 0.5F;
        }

        float d = calcStrafe * calcStrafe + calcForward * calcForward;
        if (d >= 1.0E-4F) {
            d = (float)Math.sqrt((double)d);
            if (d < 1.0F) {
                d = 1.0F;
            }

            d = friction / d;
            calcStrafe *= d;
            calcForward *= d;
            float yawSin = (float)Math.sin((double)((float)((double)yaw * Math.PI / (double)180.0F)));
            float yawCos = (float)Math.cos((double)((float)((double)yaw * Math.PI / (double)180.0F)));
            player.motionX += (double)(calcStrafe * yawCos) - (double)calcForward * (double)yawSin;
            player.motionZ += (double)(calcForward * yawCos) + (double)calcStrafe * (double)yawSin;
        }

    }

    public final void applyVanillaStrafeToPlayer(@NotNull StrafeEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        EntityPlayerSP var10000 = MinecraftInstance.mc.thePlayer;
        Intrinsics.checkNotNull(var10000);
        EntityPlayerSP player = var10000;
        int dif = (int)((MathHelper.wrapAngleTo180_float(player.rotationYaw - this.yaw - 23.5F - (float)135) + (float)180) / (float)45);
        float yaw = this.yaw;
        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();
        float calcForward = 0.0F;
        float calcStrafe = 0.0F;
        switch (dif) {
            case 0:
                calcForward = forward;
                calcStrafe = strafe;
                break;
            case 1:
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            case 2:
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            case 3:
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            case 4:
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            case 5:
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            case 6:
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            case 7:
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
        }

        if (calcForward > 1.0F || calcForward < 0.9F && calcForward > 0.3F || calcForward < -1.0F || calcForward > -0.9F && calcForward < -0.3F) {
            calcForward *= 0.5F;
        }

        if (calcStrafe > 1.0F || calcStrafe < 0.9F && calcStrafe > 0.3F || calcStrafe < -1.0F || calcStrafe > -0.9F && calcStrafe < -0.3F) {
            calcStrafe *= 0.5F;
        }

        float d = calcStrafe * calcStrafe + calcForward * calcForward;
        if (d >= 1.0E-4F) {
            d = MathHelper.sqrt_float(d);
            if (d < 1.0F) {
                d = 1.0F;
            }

            d = friction / d;
            calcStrafe *= d;
            calcForward *= d;
            float yawSin = MathHelper.sin(yaw * (float)Math.PI / 180.0F);
            float yawCos = MathHelper.cos(yaw * (float)Math.PI / 180.0F);
            player.motionX += (double)(calcStrafe * yawCos - calcForward * yawSin);
            player.motionZ += (double)(calcForward * yawCos + calcStrafe * yawSin);
        }

    }

    public final void applyStrictSilentStrafeForPlayer(@NotNull StrafeEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
    }

    @NotNull
    public final Vec3 toDirection() {
        float f = MathHelper.cos(-this.yaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float f1 = MathHelper.sin(-this.yaw * ((float)Math.PI / 180F) - (float)Math.PI);
        float f2 = -MathHelper.cos(-this.pitch * ((float)Math.PI / 180F));
        float f3 = MathHelper.sin(-this.pitch * ((float)Math.PI / 180F));
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }

    @NotNull
    public final Rotation cloneSelf() {
        return new Rotation(this.yaw, this.pitch);
    }

    public boolean equals(@Nullable Object other) {
        if (other == null) {
            return false;
        } else if (other instanceof Object[]) {
            if (((Object[])other).length != 2) {
                return false;
            } else if (((Object[])other)[0] instanceof Number && ((Object[])other)[1] instanceof Number) {
                return Intrinsics.areEqual(((Object[])other)[0], this.yaw) && Intrinsics.areEqual(((Object[])other)[1], this.pitch);
            } else {
                return false;
            }
        } else if (!(other instanceof Rotation)) {
            return false;
        } else {
            return ((Rotation)other).yaw == this.yaw && ((Rotation)other).pitch == this.pitch;
        }
    }

    public int hashCode() {
        return ((int)(this.yaw * (float)25566) << 10) + (int)(this.pitch * (float)25566);
    }

    public final float component1() {
        return this.yaw;
    }

    public final float component2() {
        return this.pitch;
    }

    @NotNull
    public final Rotation copy(float yaw, float pitch) {
        return new Rotation(yaw, pitch);
    }

    // $FF: synthetic method
    public static Rotation copy$default(Rotation var0, float var1, float var2, int var3, Object var4) {
        if ((var3 & 1) != 0) {
            var1 = var0.yaw;
        }

        if ((var3 & 2) != 0) {
            var2 = var0.pitch;
        }

        return var0.copy(var1, var2);
    }

    @NotNull
    public String toString() {
        return "Rotation(yaw=" + this.yaw + ", pitch=" + this.pitch + ')';
    }

    @JvmOverloads
    @NotNull
    public final Rotation fixedSensitivity() {
        return fixedSensitivity$default(this, 0.0F, 1, (Object)null);
    }
}