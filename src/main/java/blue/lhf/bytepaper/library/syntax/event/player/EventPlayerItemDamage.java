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
        name = "Player Item Damage",
        description = "Run when a player damages an item.",
        examples = {
                """
                on player item damage:
                    trigger:
                        send "You damaged an item!" to event-player
                """
        }
)
@EventMapsTo(PlayerItemDamageEvent.class)
public class EventPlayerItemDamage extends EventHolder {

    public EventPlayerItemDamage(Library provider) {
        super(provider, "on [player] item damage");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerItemDamage.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerItemDamageEvent event;

        public Data(PlayerItemDamageEvent event) {
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

        @EventValue("item")
        public ItemStack hand() {
            return event.getItem();
        }

        @EventValue("damage")
        public Number damage() {
            return event.getDamage();
        }

        @EventValue("original-damage")
        public Number originalDamage() {
            return event.getOriginalDamage();
        }

    }

}
