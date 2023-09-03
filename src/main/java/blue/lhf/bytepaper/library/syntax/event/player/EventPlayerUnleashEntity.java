package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Unleash Entity",
        description = "Run when a player unleashes an entity.",
        examples = {
                """
                on player unleash entity:
                    trigger:
                        send "You unleashed an entity!" to event-player
                """
        }
)
@EventMapsTo(PlayerUnleashEntityEvent.class)
public class EventPlayerUnleashEntity extends EventHolder {
    public EventPlayerUnleashEntity(Library provider) {
        super(provider, "on player unleash entity");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerUnleashEntityEvent event;

        public Data(PlayerUnleashEntityEvent event) {
            this.event = event;
        }

        @Override
        public boolean isAsync() {
            return false;
        }

        @EventValue("player")
        public Player player() {
            return event.getPlayer();
        }

        @EventValue("entity")
        public Entity entity() {
            return event.getEntity();
        }

        @EventValue("entity-type")
        public EntityType entityType() {
            return event.getEntityType();
        }

        @EventValue("reason")
        public EntityUnleashEvent.UnleashReason reason() {
            return event.getReason();
        }

    }
}
