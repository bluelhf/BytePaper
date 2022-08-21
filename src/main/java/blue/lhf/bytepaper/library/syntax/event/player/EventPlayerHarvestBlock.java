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
        name = "Player Harvest Block",
        description = "Run when a player harvest a block",
        examples = {
                """
                on player harvest block:
                    trigger:
                        send "You harvested a block!" to event-player"
                """
        }
)
@EventMapsTo(PlayerHarvestBlockEvent.class)
public class EventPlayerHarvestBlock extends EventHolder {

    public EventPlayerHarvestBlock(Library provider) {
        super(provider, "on [player] harvest [block]");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerHarvestBlock.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerHarvestBlockEvent event;

        public Data(PlayerHarvestBlockEvent event) {
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
            return event.getHarvestedBlock();
        }

    }

}
