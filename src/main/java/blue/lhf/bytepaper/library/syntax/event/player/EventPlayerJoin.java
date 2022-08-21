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
        name = "Player Join",
        description = "Run when a player join the server.",
        examples = {
                """
                on player join:
                    trigger:
                        send "You joined the server!" to event-player"
                """
        }
)
@EventMapsTo(PlayerJoinEvent.class)
public class EventPlayerJoin extends EventHolder {

    public EventPlayerJoin(Library provider) {
        super(provider, "on [player] join");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerJoin.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerJoinEvent event;

        public Data(PlayerJoinEvent event) {
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

        // TODO : add a way to set join message
        @EventValue("message")
        public Component joinMessage() {
            return event.joinMessage();
        }

    }

}
