package kevin.module.modules.movement.speeds.matrix;

import kevin.event.UpdateEvent;
import kevin.module.modules.movement.Speed;
import kevin.module.modules.movement.speeds.SpeedMode;
import kevin.module.modules.world.Scaffold;
import kevin.utils.MovementUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import org.jetbrains.annotations.NotNull;

import static kevin.utils.MovementUtils.strafe;

/**
 * Working on Matrix: 7.11.8
 * Tested on: eu.loyisa.cn & anticheat.com
 * Credit: @EclipsesDev
 */

public class MatrixHop extends SpeedMode {
    public static final MatrixHop INSTANCE = new MatrixHop();

    private MatrixHop() {
        super("MatrixHop");
    }

    public void onUpdate(@NotNull UpdateEvent event) {

        @NotNull
        EntityPlayerSP player = mc.thePlayer;
        if(player == null){
            return;
        }
        if (player.isInWeb || player.isOnLadder() || player.isInWater() || player.isInLava()) {
            return;
        }

        if (Speed.mode.get().toLowerCase().equals("matrix")) {
            player.jumpMovementFactor = 0.026f;
        }
        if (MovementUtils.isMoving()) {
            if (player.onGround) {
                player.motionY = 0.42 -((Speed.mode.get().toLowerCase().equals("matrix")) ?3.48E-3 : 0.0);
            }

            if (player.fallDistance <= 0.4 && player.moveStrafing == 0f) {
                player.speedInAir = 0.02035f;
            } else {
                player.speedInAir = 0.02f;
            }
        }
    }
}
