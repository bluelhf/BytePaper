package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Item Mend",
        description = "Run when a player mends an item using mending.",
        examples = {
                """
                on player item mend:
                    trigger:
                        send "You mended an item!" to event-player
                """
        }
)
@EventMapsTo(PlayerItemMendEvent.class)
public class EventPlayerItemMend extends EventHolder {

    public EventPlayerItemMend(Library provider) {
        super(provider, "on [player] [item] mend");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerItemMend.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerItemMendEvent event;

        public Data(PlayerItemMendEvent event) {
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

        @EventValue("orb")
        public ExperienceOrb orb() {
            return event.getExperienceOrb();
        }

    }

}
