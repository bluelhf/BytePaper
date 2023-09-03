package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Egg Throw",
        description = "Run when a player throws an egg",
        examples = {
                """
                on player egg throw:
                    trigger:
                        send "You threw an egg!" to event-player
                """
        }
)
@EventMapsTo(PlayerEggThrowEvent.class)
public class EventPlayerEggThrow extends EventHolder {

    public EventPlayerEggThrow(Library provider) {
        super(provider, "on [player] egg throw");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerEggThrow.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerEggThrowEvent event;

        public Data(PlayerEggThrowEvent event) {
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

        @EventValue("egg")
        public Egg egg() {
            return event.getEgg();
        }

        @EventValue("type")
        public EntityType type() {
            return event.getHatchingType();
        }

    }

}
