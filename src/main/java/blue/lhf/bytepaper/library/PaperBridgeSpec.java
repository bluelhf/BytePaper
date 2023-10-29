package blue.lhf.bytepaper.library;

import blue.lhf.bytepaper.library.syntax.block.*;
import blue.lhf.bytepaper.library.syntax.chat.*;
import blue.lhf.bytepaper.library.syntax.command.*;
import blue.lhf.bytepaper.library.syntax.direction.cardinal.LiteralCardinal;
import blue.lhf.bytepaper.library.syntax.direction.egocentric.LiteralEgocentric;
import blue.lhf.bytepaper.library.syntax.entity.*;
import blue.lhf.bytepaper.library.syntax.location.*;
import blue.lhf.bytepaper.library.syntax.player.PlayerProperties;
import blue.lhf.bytepaper.library.syntax.server.ExprConsole;
import blue.lhf.bytepaper.library.syntax.world.ExprWorld;
import blue.lhf.bytepaper.util.*;
import blue.lhf.bytepaper.util.property.PropertyRegistrar;
import mx.kenzie.mirror.Mirror;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.DenyAllFilter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.plugin.Plugin;
import org.byteskript.skript.api.*;
import org.byteskript.skript.api.syntax.EventHolder;
import org.byteskript.skript.compiler.CompileState;
import org.byteskript.skript.runtime.Skript;
import org.reflections.Reflections;

import java.lang.reflect.*;
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
        registerConverter(String.class, EntityType.class, LiteralEntityType::fromString);
        registerConverter(Player.class, String.class, Player::getName);
        registerConverter(Entity[].class, Audience.class, Audience::audience);
        registerConverter(Audience[].class, Audience.class, Audience::audience);
        registerConverter(Object[].class, String.class, Arrays::deepToString);
        registerConverter(Block.class, Location.class, Block::getLocation);
        registerConverter(Entity.class, Location.class, Entity::getLocation);
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
                PlayerProperties.class, BlockProperties.class, LocationProperties.class, EntityProperties.class));

        hookBukkitEvents();

        registerSyntax(CompileState.CODE_BODY, new EffectSend(this), new EffectBroadcast(this));


        hookCommands();
    }

    private void hookBukkitEvents() {
        removeReflectionsLogger();
        final Reflections reflections = new Reflections(getClass().getPackage().getName() + ".syntax.event");
        final Set<Class<? extends EventHolder>> eventClasses = reflections.getSubTypesOf(EventHolder.class);
        for (final Class<? extends EventHolder> holderClass : eventClasses) {
            final EventMapsTo map = holderClass.getAnnotation(EventMapsTo.class);
            if (map == null) {
                host.getLogger().warning("Event %s does not have a mapping!".formatted(holderClass));
                continue;
            }

            final EventHolder holder = (EventHolder) Mirror.of(holderClass).constructor(Library.class).newInstance(this);
            final Class<? extends org.byteskript.skript.api.Event> byteskriptClass = holder.eventClass();
            final Class<? extends org.bukkit.event.Event> bukkitClass = map.value();
            final Constructor<? extends org.byteskript.skript.api.Event> constructor;
            try {
                constructor = byteskriptClass.getDeclaredConstructor(bukkitClass);
            } catch (NoSuchMethodException e) {
                host.getLogger().warning("Event class of " + holderClass + " does not have a valid constructor!");
                continue;
            }

            registerEvents(holder);
            host.getServer().getPluginManager().registerEvent(bukkitClass, new Listener() {
                }, EventPriority.MONITOR,
                ((listener, event) -> {
                    try {
                        if (bukkitClass.isAssignableFrom(event.getClass()))
                            skript.runEvent(constructor.newInstance(bukkitClass.cast(event)));
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }), host);
        }
    }

    protected void hookCommands() {
        registerSyntax(CompileState.ROOT, new MemberCommand(this));
    }

    private void removeReflectionsLogger() {
        final Logger logger = ((org.apache.logging.log4j.core.Logger) LogManager.getLogger(Reflections.class));
        logger.addFilter(new DenyAllFilter.Builder().build());
    }

}
