package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Advancement Done",
        description = "Run when a player completes an advancement.",
        examples = {
                """
                on player advancement done:
                    trigger:
                        send "You've just completed an advancement!" to event-player"
                """
        }
)
@EventMapsTo(PlayerAdvancementDoneEvent.class)
public class EventPlayerAdvancementDone extends EventHolder {

    public EventPlayerAdvancementDone(Library provider) {
        super(provider, "on [player] advancement [done]");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerAdvancementDone.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerAdvancementDoneEvent event;

        public Data(PlayerAdvancementDoneEvent event) {
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

        @EventValue("advancement")
        public Advancement advancement() {
            return event.getAdvancement();
        }

    }

}
