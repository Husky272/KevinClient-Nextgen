package kevin.utils.system.timer;

import kevin.module.IntegerValue;

import static kevin.utils.system.timer.TimeUtils.*;

public class TickDelayTimer {
    private final IntegerValue minDelayValue;
    private final IntegerValue maxDelayValue;
    private final TickTimer baseTimer;
    
    private int ticks = 0;

    public TickDelayTimer(IntegerValue minDelayValue, IntegerValue maxDelayValue, TickTimer baseTimer) {
        this.minDelayValue = minDelayValue;
        this.maxDelayValue = maxDelayValue;
        this.baseTimer = baseTimer;
    }

    public TickDelayTimer(IntegerValue minDelayValue, IntegerValue maxDelayValue) {
        this.minDelayValue = minDelayValue;
        this.maxDelayValue = maxDelayValue;
        this.baseTimer = new TickTimer();
    }

    public boolean hasTimePassed() {
        return baseTimer.hasTimePassed(ticks);
    }

    public void resetTicks() {
        ticks = Math.toIntExact(randomDelay(minDelayValue.get(), maxDelayValue.get()));
    }

    public void resetTimer() {
        baseTimer.reset();
    }

    public void update() {
        baseTimer.update();
    }

    public void reset() {
        resetTimer();
        resetTicks();
    }
}
