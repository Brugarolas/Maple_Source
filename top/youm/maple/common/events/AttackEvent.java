package top.youm.maple.common.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author YouM
 * Created on 2023/8/20
 */
public class AttackEvent extends Event {
    private final EntityLivingBase targetEntity;

    public AttackEvent(EntityLivingBase targetEntity) {
        this.targetEntity = targetEntity;
    }

    public EntityLivingBase getTargetEntity() {
        return targetEntity;
    }
}
