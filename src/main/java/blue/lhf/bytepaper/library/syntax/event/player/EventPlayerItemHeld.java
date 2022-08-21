package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Item Held",
        description = "Run when a player holds an item.",
        examples = {
                """
                on player item held:
                    trigger:
                        send "You held an item!" to event-player"
                """
        }
)
@EventMapsTo(PlayerItemHeldEvent.class)
public class EventPlayerItemHeld extends EventHolder {

    public EventPlayerItemHeld(Library provider) {
        super(provider, "on [player] [item] held");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerItemHeld.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerItemHeldEvent event;

        public Data(PlayerItemHeldEvent event) {
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

        @EventValue("previous-slot")
        public Number previousSlot() {
            return event.getPreviousSlot();
        }

        @EventValue("new-slot")
        public Number newSlot() {
            return event.getNewSlot();
        }

    }

}
