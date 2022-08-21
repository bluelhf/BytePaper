package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Move",
        description = "Run when a player moves",
        examples = {
                """
                on player move:
                    trigger:
                        send "You moved!" to event-player"
                """
        }
)
@EventMapsTo(PlayerMoveEvent.class)
public class EventPlayerMove extends EventHolder {
    public EventPlayerMove(Library provider) {
        super(provider, "on [player] move");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerMoveEvent event;

        public Data(PlayerMoveEvent event) {
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

        @EventValue("from")
        public Location from() {
            return event.getFrom();
        }

        @EventValue("to")
        public Location to() {
            return event.getTo();
        }

    }
}
