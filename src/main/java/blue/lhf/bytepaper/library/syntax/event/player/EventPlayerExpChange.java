package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Experience Change",
        description = "Run when a player's experience changes",
        examples = {
                """
                on player experience change:
                    trigger:
                        send "Your experience has changed!" to event-player"
                """
        }
)
public class EventPlayerExpChange extends EventHolder {
    public EventPlayerExpChange(Library provider) {
        super(provider, "on [player] ([e]xp|experience) change");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerExpChange.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerExpChangeEvent event;

        public Data(PlayerExpChangeEvent event) {
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

        @EventValue("amount")
        public Number amount() {
            return event.getAmount();
        }

        @EventValue("source")
        public Entity source() {
            return event.getSource();
        }

    }

}
