package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Velocity",
        description = "Run when the player's velocity changes",
        examples = {
                """
                on player velocity change:
                    trigger:
                        send "Your velocity has changed!" to event-player"
                """
        }
)
public class EventPlayerVelocity extends EventHolder {
    public EventPlayerVelocity(Library provider) {
        super(provider, "on [player] velocity [change]");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerVelocityEvent event;

        public Data(PlayerVelocityEvent event) {
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

        @EventValue("velocity")
        public Vector velocity() {
            return event.getVelocity();
        }


    }
}
