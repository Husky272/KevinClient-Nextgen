package kevin.event.impl;


import kevin.event.struct.Event;


/**
 * This event is triggered when the player's movement input is updated.
 */
public final class MovementInputUpdateEvent extends Event {
    private float strafe;
    private float forward;
    private boolean jump;
    private boolean sneak;

    /**
     * Constructs a new MovementInputUpdateEvent with the specified input values.
     * @param strafe The strafe input value.
     * @param forward The forward input value.
     * @param jump Whether the jump input is active.
     * @param sneak Whether the sneak input is active.
     */
    public MovementInputUpdateEvent(float strafe, float forward, boolean jump, boolean sneak) {
        this.strafe = strafe;
        this.forward = forward;
        this.jump = jump;
        this.sneak = sneak;
    }

    /**
     * Returns the strafe input value.
     * @return
     */
    public float getStrafe() {
        return strafe;
    }

    /**
     * Sets the strafe input value.
     * @param var1
     */
    public void setStrafe(float var1) {
        strafe = var1;
    }

    /**
     * Returns the forward input value.
     * @return
     */
    public float getForward() {
        return forward;
    }

    /**
     * Sets the forward input value.
     * @param var1
     */
    public void setForward(float var1) {
        forward = var1;
    }

    /**
     * Returns whether the jump input is active.
     * @return
     */
    public boolean getJump() {
        return jump;
    }

    /**
     * Sets the jump input state.
     * @param var1
     */
    public void setJump(boolean var1) {
        jump = var1;
    }

    /**
     * Returns whether the sneak input is active.
     * @return
     */
    public boolean getSneak() {
        return sneak;
    }

    /**
     * Sets the sneak input state.
     * @param var1
     */
    public void setSneak(boolean var1) {
        sneak = var1;
    }
}