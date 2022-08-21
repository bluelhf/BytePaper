package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Pickup Arrow",
        description = "Run when a player picks up an arrow from the ground",
        examples = {
                """
                on player pickup arrow:
                    trigger:
                        send "You picked up an arrow!" to event-player"
                """
        }
)
public class EventPlayerPickupArrow extends EventHolder {
    public EventPlayerPickupArrow(Library provider) {
        super(provider, "on [player] pickup arrow");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerPickupArrowEvent event;

        public Data(PlayerPickupArrowEvent event) {
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

        @EventValue("arrow")
        public AbstractArrow arrow() {
            return event.getArrow();
        }

        @EventValue("remaining")
        public Number remaining() {
            return event.getRemaining();
        }

        @EventValue("flying") // bad name for an event value + doesn't really fit here
        public Boolean flying() {
            return event.getFlyAtPlayer();
        }

    }
}
