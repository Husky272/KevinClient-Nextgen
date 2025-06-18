// ClickUpdateEvent.java
package kevin.event.impl;

import kevin.event.struct.CancellableEvent;
import org.jetbrains.annotations.NotNull;


public final class ClickUpdateEvent extends CancellableEvent {
    @NotNull
    public static final ClickUpdateEvent INSTANCE = new ClickUpdateEvent();

    private ClickUpdateEvent() {
    }

    public final void reInit() {
        this.setCancelled(false);
    }
}