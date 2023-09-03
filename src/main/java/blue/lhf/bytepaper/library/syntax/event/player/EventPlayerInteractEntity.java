package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Interact Entity",
        description = "Run when a player interacts with an entity.",
        examples = {
                """
                on player interact entity:
                    trigger:
                        send "You interacted with an entity!" to event-player
                """
        }
)
@EventMapsTo(PlayerInteractEntityEvent.class)
public class EventPlayerInteractEntity extends EventHolder {

    public EventPlayerInteractEntity(Library provider) {
        super(provider, "on [player] interact entity");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerInteractEntity.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerInteractEntityEvent event;

        public Data(PlayerInteractEntityEvent event) {
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

        @EventValue("entity")
        public Entity entity() {
            return event.getRightClicked();
        }

    }

}
