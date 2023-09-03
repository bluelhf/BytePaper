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
        name = "Player Drop Item",
        description = "Run when a player drops an item",
        examples = {
                """
                on player drop item:
                    trigger:
                        send "You dropped an item!" to event-player
                """
        }
)
@EventMapsTo(PlayerDropItemEvent.class)
public class EventPlayerDropItem extends EventHolder {

    public EventPlayerDropItem(Library provider) {
        super(provider, "on [player] drop item");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerDropItem.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerDropItemEvent event;

        public Data(PlayerDropItemEvent event) {
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
            return event.getItemDrop();
        }

    }

}
