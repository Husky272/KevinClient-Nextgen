package kevin.event.impl;

import kevin.event.struct.Event;
import net.minecraft.entity.EntityLivingBase;

public final class EntityKilledEvent extends Event {
    public final EntityLivingBase targetEntity;

    public EntityKilledEvent(EntityLivingBase targetEntity){
        this.targetEntity = targetEntity;
    }
}
