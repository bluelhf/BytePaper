package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Armor Stand Manipulate",
        description = "Run when a player manipulates an armor stand.",
        examples = {
                """
                on armor stand manipulate:
                    trigger:
                        send "You've manipulated an armor stand!" to event-player
                """
        }
)
@EventMapsTo(PlayerArmorStandManipulateEvent.class)
public class EventPlayerArmorStandManipulate extends EventHolder {

    public EventPlayerArmorStandManipulate(Library provider) {
        super(provider, "on armor[ ]stand manipulate");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerArmorStandManipulate.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerArmorStandManipulateEvent event;

        public Data(PlayerArmorStandManipulateEvent event) {
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
        public ArmorStand entity() {
            return event.getRightClicked();
        }

        @EventValue("armorstand-item")
        public ItemStack armorstandItem() {
            return event.getArmorStandItem();
        }

        @EventValue("item")
        public ItemStack item() {
            return event.getPlayerItem();
        }

    }

}
