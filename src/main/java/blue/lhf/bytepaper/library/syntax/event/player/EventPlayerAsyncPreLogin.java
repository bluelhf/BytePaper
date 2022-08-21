package blue.lhf.bytepaper.library.syntax.event.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

import java.net.InetAddress;
import java.util.UUID;

@Documentation(
        name = "Player Pre-Login",
        description = "Run when a player initiates a connection",
        examples = {
                """
                on player pre login:
                    trigger:
                        send "The player has initiated a connection with the server!" to event-player"
                """
        }
)
public class EventPlayerAsyncPreLogin extends EventHolder {

    public EventPlayerAsyncPreLogin(Library provider) {
        super(provider, "on [player] pre login");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerAsyncPreLogin.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final AsyncPlayerPreLoginEvent event;

        public Data(AsyncPlayerPreLoginEvent event) {
            this.event = event;
        }

        @Override
        public boolean isAsync() {
            return true;
        }

        @EventValue("address")
        public InetAddress player() {
            return event.getAddress();
        }

        @EventValue("result")
        public AsyncPlayerPreLoginEvent.Result joinMessage() {
            return event.getLoginResult();
        }

        @EventValue("raw-address")
        public InetAddress rawAddress() {
            return event.getRawAddress();
        }

        @EventValue("profile")
        public PlayerProfile profile() {
            return event.getPlayerProfile();
        }

        @EventValue("hostname")
        public String hostname() {
            return event.getHostname();
        }

        @EventValue("name")
        public String name() {
            return event.getName();
        }

        @EventValue("uuid")
        public UUID uuid() {
            return event.getUniqueId();
        }

    }

}
