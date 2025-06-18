package kevin.event.impl;

import kevin.event.struct.CancellableEvent;


public final class MoveEvent extends CancellableEvent {
    private double x;
    private double y;
    private double z;
    private boolean isSafeWalk;

    public MoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final double getX() {
        return this.x;
    }

    public final void setX(double var1) {
        this.x = var1;
    }

    public final double getY() {
        return this.y;
    }

    public final void setY(double var1) {
        this.y = var1;
    }

    public final double getZ() {
        return this.z;
    }

    public final void setZ(double var1) {
        this.z = var1;
    }

    public final boolean isSafeWalk() {
        return this.isSafeWalk;
    }

    public final void setSafeWalk(boolean var1) {
        this.isSafeWalk = var1;
    }

    public final void zero() {
        this.x = (double)0.0F;
        this.y = (double)0.0F;
        this.z = (double)0.0F;
    }

    public final void zeroXZ() {
        this.x = (double)0.0F;
        this.z = (double)0.0F;
    }
}
