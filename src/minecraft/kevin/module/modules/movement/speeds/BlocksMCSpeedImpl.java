package kevin.module.modules.movement.speeds;

import kevin.module.modules.movement.Speed;
import kevin.utils.MovementUtils;
import kevin.utils.entity.ci.EntityUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;

import static kevin.utils.MinecraftInstance.mc;

/**
 * Oh my god blocks mc speed skidded from liquid bounce wow so cool ohhhh woo yes!
 */
public class BlocksMCSpeedImpl {

    public BlocksMCSpeedImpl() {
        // Nothing LOL
    }

    public static void onTick() {
        EntityPlayerSP player = mc.thePlayer;

        if (mc.thePlayer.onGround) {
            Speed.INSTANCE.blocksmc_CanSpeed = true;
        } else {
            if (!Speed.INSTANCE.blocksmc_CanSpeed) return;

            if (Speed.INSTANCE.fullStrafe.get()) {
                if (MovementUtils.isPlayerMoving()) {
                    MovementUtils.strafe(MovementUtils.getSqrtSpeed(mc.thePlayer) - 0.004);
                }
            } else {
                if (EntityUtils.getAirTicks(player) >= 6 && MovementUtils.isPlayerMoving()) {
                    MovementUtils.strafe(MovementUtils.getSqrtSpeed(mc.thePlayer));
                }
            }

            if (mc.thePlayer.isPotionActive(Potion.moveSpeed)
                &&
                mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() > 0
                &&
                EntityUtils.getAirTicks(player) == 3
            ) {

                player.setVelocity(
                        mc.thePlayer.motionX * 1.12,
                        mc.thePlayer.motionY,
                        mc.thePlayer.motionZ * 1.12
                );
            }

            if (Speed.INSTANCE.lowHop.get() && EntityUtils.getAirTicks(player) == 4) {
//                if (safeY) {
                if (player.posY % 1.0 == 0.16610926093821377) {
                    player.setVelocity(mc.thePlayer.motionX, -0.09800000190734863, mc.thePlayer.motionZ);
                }
//                } else {
//                    player.setVelocity(mc.thePlayer.motionX, -0.09800000190734863, mc.thePlayer.motionZ);
//                }
            }

            if (Speed.INSTANCE.damageBoost.get() && MovementUtils.isPlayerMoving()) {
                switch (Speed.INSTANCE.lastVelocity) {
                    case 1:
                    case 2:
                        MovementUtils.strafe(1.1);
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        MovementUtils.strafe(1.0);
                        break;
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                        MovementUtils.strafe(0.75);
                        break;
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                        MovementUtils.strafe(0.5);
                        break;
                }
            }

            if (Speed.INSTANCE.damageLowHop.get() && player.hurtTime >= 1) {
                if (player.motionY > 0) {
                    player.setVelocity(player.motionX, player.motionY - 0.15, player.motionZ);
                }
            }
        }
        Speed.INSTANCE.lastVelocity++;
    }
}
