package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.*;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
    name = "Player Chat",
    description = "Runs when a player says something in the in-game chat.",
    examples = {
        """
        on player message:
            trigger:
                send raw ("You said: " + event-message) to event-player
        """
    }
)
@EventMapsTo(AsyncChatEvent.class)
public class EventPlayerAsyncChat extends EventHolder {
    public EventPlayerAsyncChat(Library library) {
        super(library, "on [player] (chat|message)");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerAsyncChat.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final AsyncChatEvent event;

        public Data(AsyncChatEvent event) {
            this.event = event;
        }

        @Override // Not used right now, but if it ever is, then our code will have it
        public boolean isAsync() {
            return true;
        }

        @EventValue("message")
        public Component message() {
            return event.message();
        }

        @EventValue("original-message")
        public Component originalMessage() {
            return event.originalMessage();
        }

        @EventValue("sender")
        public Player sender() {
            return event.getPlayer();
        }

        @EventValue("receivers")
        public Audience[] receivers() {
            return event.viewers().toArray(Audience[]::new);
        }
    }
}
