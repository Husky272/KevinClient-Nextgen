package kevin.module.modules.movement;

import kevin.event.EventTarget;
import kevin.module.BooleanValue;
import kevin.module.ClientModule;
import kevin.module.ListValue;
import kevin.module.ModuleCategory;
import kevin.module.modules.movement.speeds.BlocksMCSpeedImpl;
import kevin.utils.MovementUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Accelerate the player's motion speed
 */
public final class Speed extends ClientModule {


    /**
     * Running {@code Speed} Instance, will be null if the class was never constructed
     */
    public static Speed INSTANCE;
    /**
     * Speed modes
     */
    public ListValue modes = new ListValue("mode", arrayOf(blocksMCMode), blocksMCMode);

    private static final String stopStrafeOnDisabledValue = "stop-strafe-on-disabled";

    // BlocksMC

    @NonNls
    @NotNull
    private static final String blocksMCMode = "BlocksMC";
    private final String fullStrafeValue = "full-strafe";
    private final String lowHopValue = "lowHop";
    private final String damageBoostValue = "damage-boost";
    private final String damageLowHopValue = "damage-low-hop";
    /**
     * BlocksMC Low hop
     */
    public final BooleanValue lowHop = new BooleanValue(blocksMCMode + ":" + lowHopValue, true, this::isSpeedModeBlocksMC);

    /**
     * BlocksMC damage Boost
     */
    public final BooleanValue damageBoost = new BooleanValue(blocksMCMode + ":" + damageBoostValue, true, this::isSpeedModeBlocksMC);

    /**
     * BlocksMC damage low hop
     */
    public final BooleanValue damageLowHop = new BooleanValue(blocksMCMode + ":" + damageLowHopValue, true, this::isSpeedModeBlocksMC);

    /**
     * BlocksMC full strafe
     */
    public final BooleanValue fullStrafe = new BooleanValue(blocksMCMode + ":" + fullStrafeValue, true, () -> {
        return modes.get().equals(blocksMCMode);
    });

    /**
     * Blocks MC speed condition value
     */
    public boolean blocksmc_CanSpeed = false;
    // END



    /**
     * Reset x and z motion on when this module was disabled
     */
    public BooleanValue stopStrafeOnDisabled = new BooleanValue(stopStrafeOnDisabledValue, true);

    /**
     * I don't know what's this
     */
    public int lastVelocity = 0;

    /**
     * Speed module constructor
     */
    public Speed() {
        super("Speed", "TODO: rewrite", ModuleCategory.MOVEMENT);
        //noinspection ThisEscapedInObjectConstruction
        INSTANCE = this;
    }

    /**
     *
     */
    @EventTarget
    public void onTick() {
        // TODO: more speeds
        //noinspection SwitchStatementWithTooFewBranches
        switch (modes.get()){
            case blocksMCMode:
                BlocksMCSpeedImpl.onTick();
            break;
        }
    }


    @EventTarget
    public void onEnable() {
        blocksmc_CanSpeed = false;
        lastVelocity = 9999;
    }

    @EventTarget
    public void onDisable() {
        if (stopStrafeOnDisabled.get()) {
            MovementUtils.resetMotion(false);
        }
    }

    private Boolean isSpeedModeBlocksMC() {
        return modes.get().equals(blocksMCMode);
    }
}