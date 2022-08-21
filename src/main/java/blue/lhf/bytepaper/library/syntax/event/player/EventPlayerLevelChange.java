package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Level Change",
        description = "Run when a player's level changes.",
        examples = {
                """
                on player level change:
                    trigger:
                        send "You leveled up!" to event-player"
                """
        }
)
public class EventPlayerLevelChange extends EventHolder {

    public EventPlayerLevelChange(Library provider) {
        super(provider, "on [player] level change");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerLevelChange.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerLevelChangeEvent event;

        public Data(PlayerLevelChangeEvent event) {
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

        @EventValue("old-level")
        public Number oldLevel() {
            return event.getOldLevel();
        }

        @EventValue("new-level")
        public Number newLevel() {
            return event.getNewLevel();
        }

    }

}
