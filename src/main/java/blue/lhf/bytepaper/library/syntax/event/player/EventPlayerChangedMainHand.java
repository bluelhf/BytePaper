package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.inventory.MainHand;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Changed Main Hand",
        description = "Run when a player changes their main hand in the client settings.",
        examples = {
                """
                on player changed main hand:
                    trigger:
                        send "You've changed your main hand in the client settings!" to event-player"
                """
        }
)
@EventMapsTo(PlayerChangedMainHandEvent.class)
public class EventPlayerChangedMainHand extends EventHolder {

    public EventPlayerChangedMainHand(Library provider) {
        super(provider, "on [player] changed main hand");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerChangedMainHand.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerChangedMainHandEvent event;

        public Data(PlayerChangedMainHandEvent event) {
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

        @EventValue("hand")
        public MainHand hand() {
            return event.getMainHand();
        }

    }

}
