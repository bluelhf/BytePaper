package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Item Pickup",
        description = "Run when a player picks up an item.",
        examples = {
                """
                on player pickup item:
                    trigger:
                        send "You picked up an item!" to event-player
                """
        }
)
@EventMapsTo(PlayerAttemptPickupItemEvent.class)
public class EventPlayerItemPickup extends EventHolder {
    public EventPlayerItemPickup(Library provider) {
        super(provider, "on [player] item pickup");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerAttemptPickupItemEvent event;

        public Data(PlayerAttemptPickupItemEvent event) {
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

        @EventValue("item")
        public Item item() {
            return event.getItem();
        }
    }
}
