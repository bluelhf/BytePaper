package blue.lhf.bytepaper.library;

import blue.lhf.bytepaper.library.syntax.chat.ChatEvent;
import blue.lhf.bytepaper.library.syntax.chat.ComponentExpression;
import blue.lhf.bytepaper.library.syntax.ConsoleExpression;
import blue.lhf.bytepaper.library.syntax.chat.SendEffect;
import blue.lhf.bytepaper.util.Exceptions;
import blue.lhf.bytepaper.util.MayThrow;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.ModifiableLibrary;
import org.byteskript.skript.api.PropertyHandler;
import org.byteskript.skript.compiler.CompileState;
import org.byteskript.skript.lang.handler.StandardHandlers;
import org.byteskript.skript.runtime.Skript;

import java.util.UUID;

public class PaperBridgeSpec extends ModifiableLibrary {
    public static final PaperBridgeSpec INSTANCE = new PaperBridgeSpec();
    public static final Library LIBRARY = INSTANCE;

    public PaperBridgeSpec() {
        super("Paper");
        registerTypes(OfflinePlayer.class, Player.class, Audience.class, UUID.class, Component.class);
        registerConverter(String.class, Component.class, Component::text);
        registerConverter(UUID.class, OfflinePlayer.class, Bukkit::getOfflinePlayer);
        registerConverter(OfflinePlayer.class, Player.class, OfflinePlayer::getPlayer);
        registerConverter(String.class, Player.class, Bukkit::getPlayerExact);
        registerConverter(Player.class, String.class, Player::getName);

        registerSyntax(CompileState.STATEMENT, new ComponentExpression(), new ConsoleExpression());

        Exceptions.trying(Bukkit.getConsoleSender(), "registering properties",
                (MayThrow.Runnable) () -> registerProperty(new PropertyHandler(StandardHandlers.GET, Player.class.getMethod("getName"), "name")));

        registerEvents(new ChatEvent());
        registerSyntax(CompileState.CODE_BODY, new SendEffect());
    }

    public void registerEvents(Skript skript, Plugin host) {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onChat(AsyncChatEvent event) {
                skript.runEvent(new ChatEvent.Data(event));
            }
        }, host);
    }
}
