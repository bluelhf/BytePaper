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
        name = "Player Toggle Sprint",
        description = "Run when a player toggles sprint.",
        examples = {
                """
                on player toggle sprint:
                    trigger:
                        send "You toggled sneak!" to event-player"
                """
        }
)
@EventMapsTo(PlayerToggleSprintEvent.class)
public class EventPlayerToggleSprint extends EventHolder {
    public EventPlayerToggleSprint(Library provider) {
        super(provider, "on [player] sprint [toggle]");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerToggleSprintEvent event;

        public Data(PlayerToggleSprintEvent event) {
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

    }
}
