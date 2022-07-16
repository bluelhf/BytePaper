package blue.lhf.bytepaper.library.syntax.player;

import blue.lhf.bytepaper.util.property.*;
import org.bukkit.entity.Player;
import org.byteskript.skript.lang.handler.StandardHandlers;

@SuppressWarnings("unused")
@PropertyHolder(owner = Player.class, implicit = true)
public class PlayerProperties {
    private PlayerProperties() {
    }
    @Property(name = "last login", type = Long.class)
    public static class PropLastLogin {
        private PropLastLogin() {
        }
        @PropertyActor(StandardHandlers.GET)
        public static Long get(Player player) {
            return player.getLastLogin();
        }
    }
}
