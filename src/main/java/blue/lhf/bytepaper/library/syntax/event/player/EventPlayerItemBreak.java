package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Item Break",
        description = "Run when a player breaks an item.",
        examples = {
                """
                on player item break:
                    trigger:
                        send "You broke an item!" to event-player"
                """
        }
)
public class EventPlayerItemBreak extends EventHolder {

    public EventPlayerItemBreak(Library provider) {
        super(provider, "on [player] item break");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerItemBreak.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerItemBreakEvent event;

        public Data(PlayerItemBreakEvent event) {
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
        public ItemStack item() {
            return event.getBrokenItem();
        }

    }

}
