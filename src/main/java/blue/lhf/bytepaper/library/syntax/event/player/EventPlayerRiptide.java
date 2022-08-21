package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.inventory.ItemStack;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Riptide",
        description = "Run when a player activates the riptide enchantment",
        examples = {
                """
                on player riptide:
                    trigger:
                        send "You are riptiding!" to event-player"
                """
        }
)
public class EventPlayerRiptide extends EventHolder {

    public EventPlayerRiptide(Library provider) {
        super(provider, "on player riptide");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerRiptide.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerRiptideEvent event;

        public Data(PlayerRiptideEvent event) {
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
        public ItemStack item() {
            return event.getItem();
        }

    }

}
