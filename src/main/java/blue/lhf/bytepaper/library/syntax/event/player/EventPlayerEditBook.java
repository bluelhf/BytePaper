package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.BookMeta;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Edit Book",
        description = "Run when a player drops an item",
        examples = {
                """
                on player edit book:
                    trigger:
                        send "You edited a book!" to event-player
                """
        }
)
@EventMapsTo(PlayerEditBookEvent.class)
public class EventPlayerEditBook extends EventHolder {

    public EventPlayerEditBook(Library provider) {
        super(provider, "on [player] edit book");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerEditBook.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerEditBookEvent event;

        public Data(PlayerEditBookEvent event) {
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

        @EventValue("previous-meta")
        public BookMeta previousMeta() {
            return event.getPreviousBookMeta();
        }

        @EventValue("new-meta")
        public BookMeta newMeta() {
            return event.getNewBookMeta();
        }

    }

}
