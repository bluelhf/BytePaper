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
        name = "Player Teleport",
        description = "Run when a player teleports",
        examples = {
                """
                on player teleport:
                    trigger:
                        send "You teleported!" to event-player
                """
        }
)
@EventMapsTo(PlayerTeleportEvent.class)
public class EventPlayerTeleport extends EventHolder {

    public EventPlayerTeleport(Library provider) {
        super(provider, "on [player] (tp|teleport)");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerTeleport.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerTeleportEvent event;

        public Data(PlayerTeleportEvent event) {
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

        @EventValue("cause")
        public PlayerTeleportEvent.TeleportCause cause() {
            return event.getCause();
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
