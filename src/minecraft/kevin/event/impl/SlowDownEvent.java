// SlowDownEvent.java
package kevin.event.impl;

import kevin.event.struct.Event;

public final class SlowDownEvent extends Event {
    private float strafe;
    private float forward;

    public SlowDownEvent(float strafe, float forward) {
        this.strafe = strafe;
        this.forward = forward;
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
}