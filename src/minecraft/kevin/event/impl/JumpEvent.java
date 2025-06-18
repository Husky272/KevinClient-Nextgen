// JumpEvent.java
package kevin.event.impl;

import kevin.event.struct.CancellableEvent;

public final class JumpEvent extends CancellableEvent {
    private float motion;
    private float yaw;

    public JumpEvent(float motion, float yaw) {
        this.motion = motion;
        this.yaw = yaw;
    }

    public final float getMotion() {
        return this.motion;
    }

    public final void setMotion(float var1) {
        this.motion = var1;
    }

    public final float getYaw() {
        return this.yaw;
    }

    public final void setYaw(float var1) {
        this.yaw = var1;
    }
}