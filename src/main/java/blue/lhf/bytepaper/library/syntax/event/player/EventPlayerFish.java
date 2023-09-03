package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Fish",
        description = "Run when a player fishes",
        examples = {
                """
                on player fishing:
                    trigger:
                        send "You've just fished!" to event-player
                """
        }
)
@EventMapsTo(PlayerFishEvent.class)
public class EventPlayerFish extends EventHolder {
    public EventPlayerFish(Library provider) {
        super(provider, "on [player] fish[ing]");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerFish.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerFishEvent event;

        public Data(PlayerFishEvent event) {
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

        @EventValue("exp")
        public Number exp() {
            return event.getExpToDrop();
        }

        @EventValue("caught")
        public Entity caught() {
            return event.getCaught();
        }

        @EventValue("hook")
        public FishHook hook() {
            return event.getHook();
        }

        @EventValue("state")
        public PlayerFishEvent.State state() {
            return event.getState();
        }

    }

}
