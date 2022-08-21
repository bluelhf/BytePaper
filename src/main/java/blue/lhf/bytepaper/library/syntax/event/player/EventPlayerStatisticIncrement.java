package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Statistic Increment",
        description = "Run when a player increments a statistic.",
        examples = {
                """
                on player statistic increment:
                    trigger:
                        send "You incremented a statistic!" to event-player"
                """
        }
)
@EventMapsTo(PlayerStatisticIncrementEvent.class)
public class EventPlayerStatisticIncrement extends EventHolder {

    public EventPlayerStatisticIncrement(Library provider) {
        super(provider, "on player stat[istic] increment");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerStatisticIncrement.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerStatisticIncrementEvent event;

        public Data(PlayerStatisticIncrementEvent event) {
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

        @EventValue("entity-type")
        public EntityType entityType() {
            return event.getEntityType();
        }

        @EventValue("statistic")
        public Statistic statistic() {
            return event.getStatistic();
        }

        @EventValue("material")
        public Material material() {
            return event.getMaterial();
        }

        @EventValue("previous-value")
        public Number previousValue() {
            return event.getPreviousValue();
        }

        @EventValue("new-value")
        public Number newValue() {
            return event.getNewValue();
        }

    }

}
