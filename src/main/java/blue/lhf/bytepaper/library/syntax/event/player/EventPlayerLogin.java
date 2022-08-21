package blue.lhf.bytepaper.library.syntax.event.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

import java.net.InetAddress;

@Documentation(
        name = "Player Login",
        description = "Run when a player login to the server.",
        examples = {
                """
                on player login:
                    trigger:
                        send "The player is logging in the server!" to event-player"
                """
        }
)
public class EventPlayerLogin extends EventHolder {

    public EventPlayerLogin(Library provider) {
        super(provider, "on [player] login");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerLogin.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerLoginEvent event;

        public Data(PlayerLoginEvent event) {
            this.event = event;
        }

        @Override
        public boolean isAsync() {
            return true;
        }

        @EventValue("player")
        public Player uuid() {
            return event.getPlayer();
        }

        @EventValue("address")
        public InetAddress player() {
            return event.getAddress();
        }

        @EventValue("real-address")
        public InetAddress rawAddress() {
            return event.getRealAddress();
        }

        @EventValue("kick-message")
        public Component kickMessage() {
            return event.kickMessage();
        }

        @EventValue("result")
        public PlayerLoginEvent.Result profile() {
            return event.getResult();
        }

        @EventValue("hostname")
        public String hostname() {
            return event.getHostname();
        }

        @EventValue("hostname")
        public String name() {
            return event.getHostname();
        }

    }

}
