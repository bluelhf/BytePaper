package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Game Mode Change",
        description = "Run when a player changes game mode.",
        examples = {
                """
                on player gamemode change:
                    trigger:
                        send "You've changed your gamemode!" to event-player"
                """
        }
)
@EventMapsTo(PlayerGameModeChangeEvent.class)
public class EventPlayerGameModeChange extends EventHolder {

    public EventPlayerGameModeChange(Library provider) {
        super(provider, "on [player] gamemode change");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerGameModeChange.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerGameModeChangeEvent event;

        public Data(PlayerGameModeChangeEvent event) {
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

        @EventValue("cause")
        public PlayerGameModeChangeEvent.Cause cause() {
            return event.getCause();
        }

        @EventValue("gamemode")
        public GameMode gameMode() {
            return event.getNewGameMode();
        }

    }

}
