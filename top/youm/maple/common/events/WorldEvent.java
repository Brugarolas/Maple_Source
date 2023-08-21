package top.youm.maple.common.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.world.World;

public class WorldEvent extends Event {
    public final World world;

    public WorldEvent(World world) {
        this.world = world;
    }
    public static class Load extends WorldEvent {
        public Load(World world) {
            super(world);
        }
    }
    public static class Unload extends WorldEvent {
        public Unload(World world) {
            super(world);
        }
    }

}
