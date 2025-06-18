package kevin.event.impl;

import kevin.event.struct.Event;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;


public final class AttackEvent extends Event {
    @Nullable
    private final Entity targetEntity;

    public AttackEvent(@Nullable Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    @Nullable
    public final Entity getTargetEntity() {
        return this.targetEntity;
    }
}
