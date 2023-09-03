package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Interact",
        description = "Run when a player interacts",
        examples = {
                """
                on player interact:
                    trigger:
                        send "You've interacted with the world!" to event-player
                """
        }
)
@EventMapsTo(PlayerInteractEvent.class)
public class EventPlayerInteract extends EventHolder {
    public EventPlayerInteract(Library provider) {
        super(provider, "on player interact");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerInteractEvent event;

        public Data(PlayerInteractEvent event) {
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

        @EventValue("action")
        public Action action() {
            return event.getAction();
        }

        @EventValue("item")
        public ItemStack item() {
            return event.getItem();
        }

        @EventValue("block")
        public Block block() {
            return event.getClickedBlock();
        }

        @EventValue("face")
        public BlockFace face() {
            return event.getBlockFace();
        }

        @EventValue("slot")
        public EquipmentSlot slot() {
            return event.getHand();
        }

        @EventValue("location")
        public Location point() {
            return event.getInteractionPoint();
        }
    }
}
