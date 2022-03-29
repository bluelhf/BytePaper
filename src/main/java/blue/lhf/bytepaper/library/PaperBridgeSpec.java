package blue.lhf.bytepaper.library;

import blue.lhf.bytepaper.library.syntax.chat.*;
import blue.lhf.bytepaper.library.syntax.command.*;
import blue.lhf.bytepaper.library.syntax.entity.*;
import blue.lhf.bytepaper.library.syntax.server.ConsoleExpression;
import blue.lhf.bytepaper.util.*;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.plugin.Plugin;
import org.byteskript.skript.api.*;
import org.byteskript.skript.compiler.CompileState;
import org.byteskript.skript.lang.handler.StandardHandlers;
import org.byteskript.skript.runtime.Skript;

import java.util.*;

public class PaperBridgeSpec extends ModifiableLibrary {
    private final Skript skript;
    private final Plugin host;

    public PaperBridgeSpec(Skript skript, Plugin host) {
        super("Paper");
        this.skript = skript;
        this.host = host;
    }

    public void registerAll() {
        registerTypes(OfflinePlayer.class, Player.class, Audience.class, UUID.class, Component.class);
        registerConverter(String.class, Component.class, Component::text);
        registerConverter(UUID.class, OfflinePlayer.class, Bukkit::getOfflinePlayer);
        registerConverter(OfflinePlayer.class, Player.class, OfflinePlayer::getPlayer);
        registerConverter(String.class, Player.class, Bukkit::getPlayerExact);
        registerConverter(Player.class, String.class, Player::getName);
        registerConverter(Entity[].class, Audience.class, Audience::audience);
        registerConverter(Audience[].class, Audience.class, Audience::audience);

        registerSyntax(CompileState.STATEMENT,
                new ComponentExpression(this), new ConsoleExpression(this),
                new EntityTypeLiteral(this), new EntitiesExpression(this));

        Exceptions.trying(Bukkit.getConsoleSender(), "registering properties",
                (MayThrow.Runnable) () -> registerProperty(new PropertyHandler(StandardHandlers.GET, Player.class.getMethod("getName"), "name")));

        registerEvents(new ChatEvent(this));
        registerSyntax(CompileState.CODE_BODY, new SendEffect(this));

        hookCommands();
        hookEvents();
    }

    public void workaroundSkriptStupiding(Skript skript) {
        for (var entry : converters.entrySet()) {
            skript.registerConverter((Class) entry.getKey().from(), (Class) entry.getKey().to(), entry.getValue());
        }
    }

    protected void hookCommands() {
        registerSyntax(CompileState.ROOT, new CommandMember(this, new CommandRegistrar(host.getLogger(), skript)));
    }

    protected void hookEvents() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onChat(AsyncChatEvent event) {
                skript.runEvent(new ChatEvent.Data(event));
            }
        }, host);
    }
}
