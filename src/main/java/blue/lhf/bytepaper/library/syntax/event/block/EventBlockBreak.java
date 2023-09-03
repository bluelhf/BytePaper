package blue.lhf.bytepaper.library.syntax.event.block;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
    name = "Block Break",
    description = "Run when a player breaks a block.",
    examples = {
            """
            on block break:
                trigger:
                    send "You've break a block!" to event-player"
            """
    }
)
@EventMapsTo(BlockBreakEvent.class)
public class EventBlockBreak extends EventHolder {

    public EventBlockBreak(Library provider) {
        super(provider, "on [block] break");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventBlockBreak.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final BlockBreakEvent event;

        public Data(BlockBreakEvent event) {
            this.event = event;
        }

        @Override // Not used right now, but if it ever is, then our code will have it
        public boolean isAsync() {
            return false;
        }

        @EventValue("block")
        public Block block() {
            return event.getBlock();
        }

        @EventValue("exp")
        public int exp() {
            return event.getExpToDrop();
        }

        @EventValue("player")
        public Player player() {
            return event.getPlayer();
        }
    }
}
