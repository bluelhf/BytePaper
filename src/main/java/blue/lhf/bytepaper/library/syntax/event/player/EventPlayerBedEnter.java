package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Bed Enter",
        description = "Run when a player enters a bed.",
        examples = {
                """
                on player bed enter:
                    trigger:
                        send "You've entered a bed!" to event-player
                """
        }
)
@EventMapsTo(PlayerBedEnterEvent.class)
public class EventPlayerBedEnter extends EventHolder {

    public EventPlayerBedEnter(Library provider) {
        super(provider, "on [player] bed enter");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerBedEnter.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerBedEnterEvent event;

        public Data(PlayerBedEnterEvent event) {
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

        @EventValue("bed-result")
        public PlayerBedEnterEvent.BedEnterResult bedResult() {
            return event.getBedEnterResult();
        }

    }

}
