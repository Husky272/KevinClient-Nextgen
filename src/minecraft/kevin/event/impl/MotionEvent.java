// MotionEvent.java
package kevin.event.impl;

import kevin.event.struct.CancellableEvent;
import kevin.event.struct.EventState;
import org.jetbrains.annotations.NotNull;

public final class MotionEvent extends CancellableEvent {
    private double posX;
    private double posY;
    private double posZ;
    private boolean onGround;
    @NotNull
    private final EventState eventState;

    public MotionEvent(double posX, double posY, double posZ, boolean onGround, @NotNull EventState eventState) {
        super();
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.onGround = onGround;
        this.eventState = eventState;
    }

    public final double getPosX() {
        return this.posX;
    }

    public final void setPosX(double var1) {
        this.posX = var1;
    }

    public final double getPosY() {
        return this.posY;
    }

    public final void setPosY(double var1) {
        this.posY = var1;
    }

    public final double getPosZ() {
        return this.posZ;
    }

    public final void setPosZ(double var1) {
        this.posZ = var1;
    }

    public final boolean getOnGround() {
        return this.onGround;
    }

    public final void setOnGround(boolean var1) {
        this.onGround = var1;
    }

    @NotNull
    public final EventState getEventState() {
        return this.eventState;
    }
}