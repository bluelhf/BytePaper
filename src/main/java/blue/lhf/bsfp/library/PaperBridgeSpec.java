package blue.lhf.bsfp.library;

import blue.lhf.bsfp.library.syntax.ComponentExpression;
import blue.lhf.bsfp.library.syntax.ConsoleExpression;
import blue.lhf.bsfp.library.syntax.SendEffect;
import mx.kenzie.foundation.Type;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.ModifiableLibrary;
import org.byteskript.skript.compiler.CompileState;

import java.util.UUID;

public class PaperBridgeSpec extends ModifiableLibrary {
    private static final PaperBridgeSpec INSTANCE = new PaperBridgeSpec();
    public static final Library LIBRARY = INSTANCE;

    public PaperBridgeSpec() {
        super("Paper");
        registerTypes(OfflinePlayer.class, Player.class, Audience.class, UUID.class, Component.class);
        registerConverter(String.class, Component.class, Component::text);
        registerConverter(UUID.class, OfflinePlayer.class, Bukkit::getOfflinePlayer);
        registerConverter(OfflinePlayer.class, Player.class, OfflinePlayer::getPlayer);
        registerConverter(String.class, Player.class, Bukkit::getPlayerExact);

        registerSyntax(CompileState.STATEMENT, new ComponentExpression(), new ConsoleExpression());
        registerSyntax(CompileState.CODE_BODY, new SendEffect());
    }
}
