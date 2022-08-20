package blue.lhf.bytepaper.library.syntax.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Bucket Entity",
        description = "Run when a player captures an entity in a bucket",
        examples = {
                """
                on player bucket entity:
                    trigger:
                        send "You've caught an entity in your bucket!" to event-player"
                """
        }
)
public class EventPlayerBucketEntity extends EventHolder {

    public EventPlayerBucketEntity(Library provider) {
        super(provider, "on [player] bucket entity");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerBucketEntity.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerBucketEntityEvent event;

        public Data(PlayerBucketEntityEvent event) {
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

        @EventValue("entity")
        public Entity block() {
            return event.getEntity();
        }

        @EventValue("original-bucket")
        public ItemStack clickedBlock() {
            return event.getOriginalBucket();
        }

        @EventValue("bucket")
        public ItemStack item() {
            return event.getEntityBucket();
        }

    }

}
