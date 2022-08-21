package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRecipeDiscoverEvent;
import org.bukkit.inventory.Recipe;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Recipe Discover",
        description = "Run when a player discovers a recipe.",
        examples = {
                """
                on player recipe discover:
                    trigger:
                        send "You discovered a recipe!" to event-player"
                """
        }
)
public class EventPlayerRecipeDiscover extends EventHolder {

    public EventPlayerRecipeDiscover(Library provider) {
        super(provider, "on [player] recipe discover");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerRecipeDiscover.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerRecipeDiscoverEvent event;

        public Data(PlayerRecipeDiscoverEvent event) {
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

        @EventValue("recipe-key")
        public NamespacedKey recipeKey() {
            return event.getRecipe();
        }

        @EventValue("recipe")
        public Recipe recipe() {
            return Bukkit.getRecipe(event.getRecipe());
        }

    }

}
