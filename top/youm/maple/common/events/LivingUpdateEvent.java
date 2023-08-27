package top.youm.maple.common.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.EntityLivingBase;

public class LivingUpdateEvent extends Event {
    public LivingUpdateEvent(EntityLivingBase entity){
        this.entity = entity;
    }

    
    private EntityLivingBase entity;

	public EntityLivingBase getEntity() {
		return entity;
	}
}