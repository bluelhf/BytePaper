package blue.lhf.bytepaper.library.syntax.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

public class EventBlockPlace extends EventHolder {

    public EventBlockPlace(Library provider) {
        super(provider, "[on] block place");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventBlockPlace.Data.class;
    }

    public static class Data extends Event {
        protected final BlockPlaceEvent event;

        public Data(BlockPlaceEvent event) {
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

        @EventValue("item")
        public ItemStack item() {
            return event.getItemInHand();
        }

        @EventValue("against")
        public Block against() {
            return event.getBlockAgainst();
        }

        @EventValue("player")
        public Player player() {
            return event.getPlayer();
        }
    }
}
