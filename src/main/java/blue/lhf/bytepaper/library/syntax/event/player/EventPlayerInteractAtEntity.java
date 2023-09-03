package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Interact at Entity",
        description = "Run when a player interacts with an entity.",
        examples = {
                """
                on player interact at entity:
                    trigger:
                        send "You interacted with an entity!" to event-player
                """
        }
)
@EventMapsTo(PlayerInteractAtEntityEvent.class)
public class EventPlayerInteractAtEntity extends EventHolder {

    public EventPlayerInteractAtEntity(Library provider) {
        super(provider, "on [player] interact at entity");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerInteractAtEntity.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerInteractAtEntityEvent event;

        public Data(PlayerInteractAtEntityEvent event) {
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

        @EventValue("hand")
        public EquipmentSlot hand() {
            return event.getHand();
        }

        @EventValue("position") // may be return a Location instead of a Vector ?
        public Vector position() {
            return event.getClickedPosition();
        }

        @EventValue("entity")
        public Entity entity() {
            return event.getRightClicked();
        }

    }

}
