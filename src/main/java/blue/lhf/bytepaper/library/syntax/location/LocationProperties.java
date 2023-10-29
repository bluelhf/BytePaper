package blue.lhf.bytepaper.library.syntax.location;

import blue.lhf.bytepaper.util.property.*;
import org.bukkit.Location;
import org.byteskript.skript.lang.handler.StandardHandlers;

@PropertyHolder(owner = Location.class)
public class LocationProperties {
    private LocationProperties() {
    }
    @Property(name = "x", type = Double.class)
    public static class PropX {
        private PropX() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Double get(final Location location) {
            return location.getX();
        }
    }

    @Property(name = "y", type = Double.class)
    public static class PropY {
        private PropY() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Double get(final Location location) {
            return location.getY();
        }
    }

    @Property(name = "z", type = Double.class)
    public static class PropZ {
        private PropZ() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Double get(final Location location) {
            return location.getZ();
        }
    }

    @Property(name = "yaw", type = Float.class)
    public static class PropYaw {
        private PropYaw() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Float get(final Location location) {
            return location.getYaw();
        }
    }

    @Property(name = "pitch", type = Float.class)
    public static class PropPitch {
        private PropPitch() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Float get(final Location location) {
            return location.getPitch();
        }
    }
}
