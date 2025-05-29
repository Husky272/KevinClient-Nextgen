package kevin.utils.system.timer;

import kevin.module.IntegerValue;
import static kevin.utils.entity.combatAndInventory.InventoryUtils.*;
import static kevin.utils.system.timer.TimeUtils.*;

public abstract class DelayTimer {
    private IntegerValue minDelayValue = null;
    private IntegerValue maxDelayValue = minDelayValue;
    /*package-private*/ static MSTimer baseTimer = CLICK_TIMER;

    public DelayTimer(IntegerValue minDelayValue, IntegerValue maxDelayValue, MSTimer baseTimer) {
        this.minDelayValue = minDelayValue;
        if (maxDelayValue != null) this.maxDelayValue = maxDelayValue;
        this.baseTimer = baseTimer;
    }

    protected long delay = 0L;

    public boolean hasTimePassed() {
        return baseTimer.hasTimePassed(delay);
    }

    public void resetDelay() {
        delay = randomDelay(minDelayValue.get(), maxDelayValue.get());
    }

    public void resetTimer() {
        baseTimer.reset();
    }

    public void reset() {
        resetTimer();
        resetDelay();
    }
}
