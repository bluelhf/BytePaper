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
        name = "Player Respawn",
        description = "Run when a player respawns",
        examples = {
                """
                on player respawn:
                    trigger:
                        send "Welcome back!" to event-player"
                """
        }
)
@EventMapsTo(PlayerRespawnEvent.class)
public class EventPlayerRespawn extends EventHolder {

    public EventPlayerRespawn(Library provider) {
        super(provider, "on [player] respawn");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerRespawn.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerRespawnEvent event;

        public Data(PlayerRespawnEvent event) {
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

        @EventValue("location")
        public Location location() {
            return event.getRespawnLocation();
        }

        @EventValue("flags")
        public PlayerRespawnEvent.RespawnFlag[] flags() {
            return event.getRespawnFlags().toArray(PlayerRespawnEvent.RespawnFlag[]::new);
        }


    }

}
