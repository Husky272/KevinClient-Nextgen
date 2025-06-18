// StrafeEvent.java
package kevin.event.impl;

import kevin.event.struct.CancellableEvent;

public final class StrafeEvent extends CancellableEvent {
    private float strafe;
    private float forward;
    private float friction;
    private float yaw;

    public StrafeEvent(float strafe, float forward, float friction, float yaw) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
    }

    public final float getStrafe() {
        return this.strafe;
    }

    public final void setStrafe(float var1) {
        this.strafe = var1;
    }

    public final float getForward() {
        return this.forward;
    }

    public final void setForward(float var1) {
        this.forward = var1;
    }

    public final float getFriction() {
        return this.friction;
    }

    public final void setFriction(float var1) {
        this.friction = var1;
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final void setYaw(float var1) {
        this.yaw = var1;
    }
}