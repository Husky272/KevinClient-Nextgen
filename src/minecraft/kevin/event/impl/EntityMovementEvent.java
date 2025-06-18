// EntityMovementEvent.java
package kevin.event.impl;

import kevin.event.struct.Event;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class EntityMovementEvent extends Event {
    @NotNull
    private final Entity movedEntity;

    public EntityMovementEvent(@NotNull Entity movedEntity) {
        super();
        this.movedEntity = movedEntity;
    }

    @NotNull
    public final Entity getMovedEntity() {
        return this.movedEntity;
    }

    @NotNull
    public final Entity component1() {
        return this.movedEntity;
    }

    @NotNull
    public final EntityMovementEvent copy(@NotNull Entity movedEntity) {
        Intrinsics.checkNotNullParameter(movedEntity, "movedEntity");
        return new EntityMovementEvent(movedEntity);
    }

    // $FF: synthetic method
    public static EntityMovementEvent copy$default(EntityMovementEvent var0, Entity var1, int var2, Object var3) {
        if ((var2 & 1) != 0) {
            var1 = var0.movedEntity;
        }

        return var0.copy(var1);
    }

    @NotNull
    public String toString() {
        return "EntityMovementEvent(movedEntity=" + this.movedEntity + ')';
    }

    public int hashCode() {
        return this.movedEntity.hashCode();
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof EntityMovementEvent)) {
            return false;
        } else {
            EntityMovementEvent var2 = (EntityMovementEvent)other;
            return Intrinsics.areEqual(this.movedEntity, var2.movedEntity);
        }
    }
}