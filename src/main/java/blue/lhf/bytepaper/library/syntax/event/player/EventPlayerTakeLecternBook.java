package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.block.Lectern;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.inventory.ItemStack;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Take Lectern Book",
        description = "Run when a player takes a book from a lectern.",
        examples = {
                """
                on player take lectern book:
                    trigger:
                        send "You took a book from a lectern!" to event-player"
                """
        }
)
public class EventPlayerTakeLecternBook extends EventHolder {

    public EventPlayerTakeLecternBook(Library provider) {
        super(provider, "on [player] take lectern book");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerTakeLecternBook.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerTakeLecternBookEvent event;

        public Data(PlayerTakeLecternBookEvent event) {
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

        @EventValue("book")
        public ItemStack book() {
            return event.getBook();
        }

        @EventValue("lectern")
        public Lectern lectern() {
            return event.getLectern();
        }

    }

}
