package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Portal",
        description = "Run when a player enters a portal",
        examples = {
                """
                on player portal enter:
                    trigger:
                        send "You entered a portal!" to event-player"
                """
        }
)
public class EventPlayerPortal extends EventHolder {

    public EventPlayerPortal(Library provider) {
        super(provider, "on [player] portal [enter]");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerPortal.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerPortalEvent event;

        public Data(PlayerPortalEvent event) {
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

        @EventValue("creation-radius")
        public Number creationRadius() {
            return event.getCreationRadius();
        }

        @EventValue("search-radius")
        public Number searchRadius() {
            return event.getSearchRadius();
        }

        @EventValue("cause")
        public PlayerTeleportEvent.TeleportCause cause() {
            return event.getCause();
        }

    }

}
