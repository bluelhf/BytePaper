package blue.lhf.bytepaper.library;

import blue.lhf.bytepaper.library.syntax.block.*;
import blue.lhf.bytepaper.library.syntax.chat.*;
import blue.lhf.bytepaper.library.syntax.command.*;
import blue.lhf.bytepaper.library.syntax.direction.cardinal.LiteralCardinal;
import blue.lhf.bytepaper.library.syntax.direction.egocentric.LiteralEgocentric;
import blue.lhf.bytepaper.library.syntax.entity.ExprEntities;
import blue.lhf.bytepaper.library.syntax.entity.LiteralEntityType;
import blue.lhf.bytepaper.library.syntax.location.ExprLocation;
import blue.lhf.bytepaper.library.syntax.location.LocationProperties;
import blue.lhf.bytepaper.library.syntax.player.EventPlayerInteract;
import blue.lhf.bytepaper.library.syntax.player.PlayerProperties;
import blue.lhf.bytepaper.library.syntax.server.ExprConsole;
import blue.lhf.bytepaper.library.syntax.world.ExprWorld;
import blue.lhf.bytepaper.util.Exceptions;
import blue.lhf.bytepaper.util.MayThrow;
import blue.lhf.bytepaper.util.property.PropertyRegistrar;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.byteskript.skript.api.ModifiableLibrary;
import org.byteskript.skript.compiler.CompileState;
import org.byteskript.skript.runtime.Skript;

import java.util.Arrays;
import java.util.UUID;

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
        registerConverter(Object[].class, String.class, Arrays::deepToString);
        registerConverter(Block.class, Location.class, Block::getLocation);
        registerConverter(Object.class, Component.class, o -> Component.text(o.toString()));

        registerSyntax(CompileState.STATEMENT,
                new ExprComponent(this), new ExprConsole(this),
                new LiteralEntityType(this), new ExprEntities(this),
                new LiteralBlockData(this), new LiteralCardinal(this),
                new LiteralEgocentric(this), new ExprBlockAt(this),
                new ExprWorld(this), new ExprLocation(this),
                new ExprArguments(this), new ExprExecutor(this));

        Exceptions.trying(Bukkit.getConsoleSender(), "registering properties",
                (MayThrow.Runnable) () -> PropertyRegistrar.register(this,
                        PlayerProperties.class, BlockProperties.class, LocationProperties.class));

        registerEvents(new EventChat(this), new EventPlayerInteract(this), new EventBlockPlace(this));
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

            @EventHandler
            public void onPlace(BlockPlaceEvent event) {
                skript.runEvent(new EventBlockPlace.Data(event));
            }
        }, host);
    }
}
