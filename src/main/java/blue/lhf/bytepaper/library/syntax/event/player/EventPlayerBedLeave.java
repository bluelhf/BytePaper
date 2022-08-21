package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Bed Leave",
        description = "Run when a player leaves a bed.",
        examples = {
                """
                on player bed leave:
                    trigger:
                        send "You've left a bed!" to event-player"
                """
        }
)
public class EventPlayerBedLeave extends EventHolder {

    public EventPlayerBedLeave(Library provider) {
        super(provider, "on [player] bed leave");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerBedLeave.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerBedLeaveEvent event;

        public Data(PlayerBedLeaveEvent event) {
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

        @EventValue("block")
        public Block block() {
            return event.getBed();
        }

    }

}
