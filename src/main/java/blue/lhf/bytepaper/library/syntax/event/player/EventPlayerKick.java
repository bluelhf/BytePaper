package blue.lhf.bytepaper.library.syntax.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Kick",
        description = "Run when a player join the server.",
        examples = {
                """
                on player kick:
                    trigger:
                        broadcast mini "<rainbow>Player %event-player% has been kicked from the server.</rainbow>"
                """
        }
)
public class EventPlayerKick extends EventHolder {

    public EventPlayerKick(Library provider) {
        super(provider, "on [player] kick");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerKick.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerKickEvent event;

        public Data(PlayerKickEvent event) {
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
        public PlayerKickEvent.Cause cause() {
            return event.getCause();
        }

        @EventValue("reason")
        public Component reason() {
            return event.reason();
        }

    }

}
