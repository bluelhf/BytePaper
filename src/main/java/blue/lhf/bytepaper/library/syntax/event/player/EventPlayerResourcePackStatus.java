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
        name = "Player Resource Pack Status",
        description = "Run when a player takes action on a resource pack request",
        examples = {
                """
                on player resource pack status:
                    trigger:
                        send "You've just received a resource pack request!" to event-player
                """
        }
)
@EventMapsTo(PlayerResourcePackStatusEvent.class)
public class EventPlayerResourcePackStatus extends EventHolder {

    public EventPlayerResourcePackStatus(Library provider) {
        super(provider, "on [player] resource pack status");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerResourcePackStatus.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerResourcePackStatusEvent event;

        public Data(PlayerResourcePackStatusEvent event) {
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

        @EventValue("status")
        public PlayerResourcePackStatusEvent.Status status() {
            return event.getStatus();
        }


    }

}
