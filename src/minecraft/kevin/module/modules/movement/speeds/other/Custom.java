package kevin.module.modules.movement.speeds.other;

import kevin.event.UpdateEvent;
import kevin.module.modules.movement.Speed;
import kevin.module.modules.movement.speeds.SpeedMode;
import kevin.utils.MinecraftInstance;
import kevin.utils.MovementUtils;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public final class Custom extends SpeedMode {
    @NotNull
    public static final Custom INSTANCE = new Custom();
    private static int groundTick;

    private Custom() {
        super("Custom");
    }

    public void onPreMotion() {
        if (Speed.INSTANCE.getUsePreMotion().get()) {
            handleMovement();
        }
    }

    public void onUpdate(@NotNull UpdateEvent event) {
        if (!Speed.INSTANCE.getUsePreMotion().get()) {
            handleMovement();
        }
    }

    private void handleMovement() {

        if (MovementUtils.isMoving()) {
            EntityPlayerSP player = MinecraftInstance.mc.thePlayer;
            boolean isOnGround = player.onGround;

            // Set timer speed based on vertical direction
            MinecraftInstance.mc.getTimer().timerSpeed = (player.motionY > 0.0)
                    ? Speed.INSTANCE.getUpTimerValue().get().floatValue()
                    : Speed.INSTANCE.getDownTimerValue().get().floatValue();

            if (isOnGround) {
                if (groundTick >= Speed.INSTANCE.getGroundStay().get().intValue()) {
                    if (Speed.INSTANCE.getGroundSpaceKeyPressed().get()) {
                        MinecraftInstance.mc.gameSettings.keyBindJump.pressed =
                                GameSettings.isKeyDown(MinecraftInstance.mc.gameSettings.keyBindJump);
                    }

                    MinecraftInstance.mc.getTimer().timerSpeed = Speed.INSTANCE.getJumpTimerValue().get();
                    player.jump();

                    if (Speed.INSTANCE.getDoLaunchSpeedValue().get()) {
                        MovementUtils.strafe(Speed.INSTANCE.getLaunchSpeedValue().get());
                    }

                    if (Speed.INSTANCE.getDoCustomYValue().get() && Speed.INSTANCE.getYValue().get() != 0f) {
                        player.motionY = Speed.INSTANCE.getYValue().get();
                    }
                } else if (Speed.INSTANCE.getGroundResetXZValue().get()) {
                    player.motionX = 0.0F;
                    player.motionZ = 0.0F;
                }

                groundTick++;
            } else {
                groundTick = 0;

                if (Speed.INSTANCE.getAirSpaceKepPressed().get()) {
                    MinecraftInstance.mc.gameSettings.keyBindJump.pressed =
                            GameSettings.isKeyDown(MinecraftInstance.mc.gameSettings.keyBindJump);
                }

                if (Speed.INSTANCE.getDoMinimumSpeedValue().get() &&
                        MovementUtils.getSpeed() < Speed.INSTANCE.getMinimumSpeedValue().get()) {
                    MovementUtils.strafe(Speed.INSTANCE.getMinimumSpeedValue().get());
                }

                String strafeMode = Speed.INSTANCE.getStrafeValue().get().toLowerCase(Locale.ROOT);
                switch (strafeMode) {
                    case "strafe":
                        MovementUtils.strafe(Speed.INSTANCE.getSpeedValue().get());
                        break;
                    case "plusonlydown":
                        if (player.motionY < 0.0) applyPlusMode(player);
                        else MovementUtils.strafe(0.0F);
                        break;
                    case "non-strafe":
                    case "boost":
                        MovementUtils.strafe(0.0F);
                        break;
                    case "plus":
                        applyPlusMode(player);
                        break;
                    case "plusonlyup":
                        if (player.motionY > 0.0) applyPlusMode(player);
                        else MovementUtils.strafe(0.0F);
                        break;
                }

                player.motionY += Speed.INSTANCE.getAddYMotionValue().get() * 0.03;
            }
        } else if (Speed.INSTANCE.getResetXZValue().get()) {
            MinecraftInstance.mc.thePlayer.motionX = 0.0F;
            MinecraftInstance.mc.thePlayer.motionZ = 0.0F;
        }
    }

    private void applyPlusMode(EntityPlayerSP player) {
        String plusMode = Speed.INSTANCE.getPlusMode().get().toLowerCase(Locale.ROOT);

        if (Intrinsics.areEqual(plusMode, "plus")) {
            MovementUtils.INSTANCE.move(Speed.INSTANCE.getSpeedValue().get() * 0.1F);
        } else if (Intrinsics.areEqual(plusMode, "multiply")) {
            double multiplyFactor = Speed.INSTANCE.getPlusMultiply().get().doubleValue();
            player.motionX *= multiplyFactor;
            player.motionZ *= multiplyFactor;
        }
    }

    public void onEnable() {
        EntityPlayerSP player = MinecraftInstance.mc.thePlayer;
        if (Speed.INSTANCE.getResetXZValue().get()) {
            player.motionX = 0.0F;
            player.motionZ = 0.0F;
        }
        if (Speed.INSTANCE.getResetYValue().get()) {
            player.motionY = 0.0F;
        }
    }

    public void onDisable() {
        MinecraftInstance.mc.getTimer().timerSpeed = 1.0F;
    }
}