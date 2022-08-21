package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

import java.util.Locale;

@Documentation(
        name = "Player Locale Change",
        description = "Run when a player changes locale.",
        examples = {
                """
                on player locale change:
                    trigger:
                        send "You've changed your locale" to event-player"
                """
        }
)
@EventMapsTo(PlayerLocaleChangeEvent.class)
public class EventPlayerLocaleChange extends EventHolder {

    public EventPlayerLocaleChange(Library provider) {
        super(provider, "on [player] locale change");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerLocaleChange.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerLocaleChangeEvent event;

        public Data(PlayerLocaleChangeEvent event) {
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

        @EventValue("locale")
        public Locale locale() {
            return event.getPlayer().locale();
        }


    }

}
