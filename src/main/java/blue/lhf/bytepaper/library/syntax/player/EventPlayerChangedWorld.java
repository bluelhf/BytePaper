package blue.lhf.bytepaper.library.syntax.player;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Changed World",
        description = "Run when a player changes worlds.",
        examples = {
                """
                on player changed world:
                    trigger:
                        send "You've changed your world" to event-player"
                """
        }
)
public class EventPlayerChangedWorld extends EventHolder {

    public EventPlayerChangedWorld(Library provider) {
        super(provider, "on [player] world changed");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerChangedWorld.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerChangedWorldEvent event;

        public Data(PlayerChangedWorldEvent event) {
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
        public World from() {
            return event.getFrom();
        }

    }

}
