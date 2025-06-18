package kevin.module.modules.movement;

import kevin.module.*;

/**
 * Accelerate the player's motion speed
 */
public final class Speed extends ClientModule {

    /**
     * Running {@code Speed} Instance, will be null if the class was never constructed
     */
    public static Speed INSTANCE;

    /**
     * Speed module constructor
     */
    public Speed() {
        super("Speed", "TODO: rewrite", ModuleCategory.MOVEMENT);
        INSTANCE = this;
    }
}