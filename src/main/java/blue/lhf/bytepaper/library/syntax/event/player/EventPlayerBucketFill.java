package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Bucket Fill",
        description = "Run when a player fills a bucket",
        examples = {
                """
                on player bucket fill:
                    trigger:
                        send "You've filled your bucket!" to event-player
                """
        }
)
@EventMapsTo(PlayerBucketFillEvent.class)
public class EventPlayerBucketFill extends EventHolder {

    public EventPlayerBucketFill(Library provider) {
        super(provider, "on [player] bucket fill");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerBucketFill.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerBucketFillEvent event;

        public Data(PlayerBucketFillEvent event) {
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

        @EventValue("hand")
        public EquipmentSlot hand() {
            return event.getHand();
        }

        @EventValue("block")
        public Block block() {
            return event.getBlock();
        }

        @EventValue("blockface")
        public BlockFace blockface() {
            return event.getBlockFace();
        }

        @EventValue("clicked-block")
        public Block clickedBlock() {
            return event.getBlockClicked();
        }

        @EventValue("bucket")
        public Material bucket() {
            return event.getBucket();
        }

        @EventValue("item")
        public ItemStack item() {
            return event.getItemStack();
        }

    }

}
