package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Shear Entity",
        description = "Run when a player shears an entity.",
        examples = {
                """
                on player shear entity:
                    trigger:
                        send "You sheared an entity!" to event-player"
                """
        }
)
public class EventPlayerShearEntity extends EventHolder {
    public EventPlayerShearEntity(Library provider) {
        super(provider, "on [player] shear[ing] [entity]");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerShearEntity.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerShearEntityEvent event;

        public Data(PlayerShearEntityEvent event) {
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

        @EventValue("slot")
        public EquipmentSlot slot() {
            return event.getHand();
        }

        @EventValue("entity")
        public Entity entity() {
            return event.getEntity();
        }

        @EventValue("item")
        public ItemStack item() {
            return event.getItem();
        }

    }

}
