package kevin.event.impl;

import kevin.event.struct.CancellableEvent;
import net.minecraft.network.Packet;
import org.jetbrains.annotations.NotNull;

public final class PacketEvent extends CancellableEvent {
    @NotNull
    private final Packet<?> packet;

    public PacketEvent(@NotNull Packet<?> packet/**,PacketMode eventState*/) {
        super();
        this.packet = packet;
    }

    @NotNull
    public final Packet<?> getPacket() {
        return this.packet;
    }
}
