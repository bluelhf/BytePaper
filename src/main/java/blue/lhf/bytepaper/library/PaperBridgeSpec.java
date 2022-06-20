package blue.lhf.bytepaper.library;

import blue.lhf.bytepaper.library.syntax.block.LiteralBlockData;
import blue.lhf.bytepaper.library.syntax.block.PropBlockData;
import blue.lhf.bytepaper.library.syntax.chat.*;
import blue.lhf.bytepaper.library.syntax.command.*;
import blue.lhf.bytepaper.library.syntax.entity.*;
import blue.lhf.bytepaper.library.syntax.location.ExprCoordinate;
import blue.lhf.bytepaper.library.syntax.player.EventPlayerInteract;
import blue.lhf.bytepaper.library.syntax.server.ExprConsole;
import blue.lhf.bytepaper.util.*;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.byteskript.skript.api.*;
import org.byteskript.skript.compiler.CompileState;
import org.byteskript.skript.lang.handler.StandardHandlers;
import org.byteskript.skript.runtime.Skript;

import java.util.*;

public class PaperBridgeSpec extends ModifiableLibrary {
    private final Skript skript;
    private final Plugin host;
    private final CommandRegistrar registrar;

    public PaperBridgeSpec(Skript skript, Plugin host, CommandRegistrar registrar) {
        super("Paper");
        this.skript = skript;
        this.registrar = registrar;
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
                new ExprComponent(this), new ExprConsole(this),
                new LiteralEntityType(this), new ExprEntities(this),
                new LiteralBlockData(this), new ExprCoordinate(this));

        Exceptions.trying(Bukkit.getConsoleSender(), "registering properties",
                (MayThrow.Runnable) () -> {
            registerProperty("name", StandardHandlers.GET, Player.class.getMethod("getName"));
            PropBlockData.register(this);
        });

        registerEvents(new EventChat(this), new EventPlayerInteract(this));
        registerSyntax(CompileState.CODE_BODY, new EffectSend(this), new EffectBroadcast(this));

        hookCommands();
        hookEvents();
    }

    protected void hookCommands() {
        registerSyntax(CompileState.ROOT, new MemberCommand(this, registrar));
    }

    protected void hookEvents() {
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onChat(AsyncChatEvent event) {
                skript.runEvent(new EventChat.Data(event));
            }
            @EventHandler
            public void onInteract(PlayerInteractEvent event) {
                skript.runEvent(new EventPlayerInteract.Data(event));
            }
        }, host);
    }
}
