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
        name = "Player Item Consume",
        description = "Run when a player consumes an item.",
        examples = {
                """
                on player item consume:
                    trigger:
                        send "You consumed an item!" to event-player
                """
        }
)
@EventMapsTo(PlayerItemConsumeEvent.class)
public class EventPlayerItemConsume extends EventHolder {
    public EventPlayerItemConsume(Library provider) {
        super(provider, "on [player] [item] consume");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerItemConsume.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerItemConsumeEvent event;

        public Data(PlayerItemConsumeEvent event) {
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

        @EventValue("replacement")
        public ItemStack replacement() {
            return event.getReplacement();
        }

    }

}
