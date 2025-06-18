// StepEvent.java
package kevin.event.impl;

import kevin.event.struct.Event;

public final class StepEvent extends Event {
    private float stepHeight;

    public StepEvent(float stepHeight) {
        this.stepHeight = stepHeight;
    }

    public final float getStepHeight() {
        return this.stepHeight;
    }

    public final void setStepHeight(float var1) {
        this.stepHeight = var1;
    }
}