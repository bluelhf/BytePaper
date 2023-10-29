package blue.lhf.bytepaper.library.syntax.entity;

import blue.lhf.bytepaper.util.property.*;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Entity;
import org.byteskript.skript.lang.handler.StandardHandlers;


@PropertyHolder(owner = Entity.class)
public class EntityProperties {
    private EntityProperties() {
    }
    @Property(name = "custom name", type = Component.class)
    public static class PropCustomName {
        private PropCustomName() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Component get(final Entity entity) {
            return entity.customName();
        }

        @PropertyActor(StandardHandlers.SET)
        public static void set(final Entity entity, final Component component) {
            entity.customName(component);
        }
    }

    @Property(name = "x", type = Double.class)
    public static class PropX {
        private PropX() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Double get(final Entity entity) {
            return entity.getX();
        }
    }

    @Property(name = "y", type = Double.class)
    public static class PropY {
        private PropY() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Double get(final Entity entity) {
            return entity.getY();
        }
    }

    @Property(name = "z", type = Double.class)
    public static class PropZ {
        private PropZ() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Double get(final Entity entity) {
            return entity.getZ();
        }
    }

    @Property(name = "yaw", type = Float.class)
    public static class PropYaw {
        private PropYaw() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Float get(final Entity entity) {
            return entity.getYaw();
        }
    }

    @Property(name = "pitch", type = Float.class)
    public static class PropPitch {
        private PropPitch() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Float get(final Entity entity) {
            return entity.getPitch();
        }
    }
}
