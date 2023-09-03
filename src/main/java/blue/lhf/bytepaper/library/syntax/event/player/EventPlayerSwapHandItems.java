package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Swap Hand Items",
        description = "Run when a player swaps hand items.",
        examples = {
                """
                on player swap hand items:
                    trigger:
                        send "You swapped hand items!" to event-player
                """
        }
)
@EventMapsTo(PlayerSwapHandItemsEvent.class)
public class EventPlayerSwapHandItems extends EventHolder {

    public EventPlayerSwapHandItems(Library provider) {
        super(provider, "on player swap hand [item[s]]");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerSwapHandItems.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerSwapHandItemsEvent event;

        public Data(PlayerSwapHandItemsEvent event) {
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

        @EventValue("mainhand-item")
        public ItemStack mainHandItem() {
            return event.getMainHandItem();
        }

        @EventValue("offhand-item")
        public ItemStack offHandItem() {
            return event.getOffHandItem();
        }

    }

}
