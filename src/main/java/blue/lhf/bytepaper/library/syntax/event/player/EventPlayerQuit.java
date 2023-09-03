package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Quit",
        description = "Run when a player quits the server.",
        examples = {
                """
                on player quit:
                    trigger:
                        send "You left the server!" to event-player
                """
        }
)
@EventMapsTo(PlayerQuitEvent.class)
public class EventPlayerQuit extends EventHolder {

    public EventPlayerQuit(Library provider) {
        super(provider, "on [player] quit");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerQuit.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerQuitEvent event;

        public Data(PlayerQuitEvent event) {
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

        // TODO : add a way to set quit message
        @EventValue("message")
        public Component quitMessage() {
            return event.quitMessage();
        }

    }

}
