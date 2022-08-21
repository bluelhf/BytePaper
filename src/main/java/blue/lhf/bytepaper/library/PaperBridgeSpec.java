package blue.lhf.bytepaper.library;

import blue.lhf.bytepaper.library.syntax.block.BlockProperties;
import blue.lhf.bytepaper.library.syntax.block.ExprBlockAt;
import blue.lhf.bytepaper.library.syntax.block.LiteralBlockData;
import blue.lhf.bytepaper.library.syntax.chat.EffectBroadcast;
import blue.lhf.bytepaper.library.syntax.chat.EffectSend;
import blue.lhf.bytepaper.library.syntax.chat.ExprComponent;
import blue.lhf.bytepaper.library.syntax.command.ExprArguments;
import blue.lhf.bytepaper.library.syntax.command.ExprExecutor;
import blue.lhf.bytepaper.library.syntax.command.MemberCommand;
import blue.lhf.bytepaper.library.syntax.direction.cardinal.LiteralCardinal;
import blue.lhf.bytepaper.library.syntax.direction.egocentric.LiteralEgocentric;
import blue.lhf.bytepaper.library.syntax.entity.ExprEntities;
import blue.lhf.bytepaper.library.syntax.entity.LiteralEntityType;
import blue.lhf.bytepaper.library.syntax.event.block.EventBlockBreak;
import blue.lhf.bytepaper.library.syntax.event.block.EventBlockPlace;
import blue.lhf.bytepaper.library.syntax.event.player.*;
import blue.lhf.bytepaper.library.syntax.location.ExprLocation;
import blue.lhf.bytepaper.library.syntax.location.LocationProperties;
import blue.lhf.bytepaper.library.syntax.player.PlayerProperties;
import blue.lhf.bytepaper.library.syntax.server.ExprConsole;
import blue.lhf.bytepaper.library.syntax.world.ExprWorld;
import blue.lhf.bytepaper.util.Exceptions;
import blue.lhf.bytepaper.util.MayThrow;
import blue.lhf.bytepaper.util.property.PropertyRegistrar;
import mx.kenzie.mirror.Mirror;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.byteskript.skript.api.ModifiableLibrary;
import org.byteskript.skript.api.syntax.EventHolder;
import org.byteskript.skript.compiler.CompileState;
import org.byteskript.skript.runtime.Skript;
import org.reflections.Reflections;
import org.slf4j.Logger;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PaperBridgeSpec extends ModifiableLibrary {

    private static final HashMap<Class<? extends Event>, Class<? extends EventHolder>> EVENTS = new HashMap<>();

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

        // Player events
        registerEvents(
                new EventPlayerAdvancementDone(this),
                new EventPlayerArmorStandManipulate(this),
                new EventPlayerAsyncChat(this),
                new EventPlayerAsyncPreLogin(this),
                new EventPlayerBedEnter(this),
                new EventPlayerBedLeave(this),
                new EventPlayerBucketEmpty(this),
                new EventPlayerBucketEntity(this),
                new EventPlayerBucketFill(this),
                new EventPlayerChangedMainHand(this),
                new EventPlayerChangedWorld(this),
                new EventPlayerCommandPreprocess(this),
                new EventPlayerCommandSend(this),
                new EventPlayerDropItem(this),
                new EventPlayerEditBook(this),
                new EventPlayerEggThrow(this),
                new EventPlayerExpChange(this),
                new EventPlayerFish(this),
                new EventPlayerGameModeChange(this),
                new EventPlayerHarvestBlock(this),
                new EventPlayerInteract(this),
                new EventPlayerInteractAtEntity(this),
                new EventPlayerInteractEntity(this),
                new EventPlayerItemBreak(this),
                new EventPlayerItemConsume(this),
                new EventPlayerItemDamage(this),
                new EventPlayerItemHeld(this),
                new EventPlayerItemMend(this),
                new EventPlayerItemPickup(this),
                new EventPlayerJoin(this),
                new EventPlayerKick(this),
                new EventPlayerLevelChange(this),
                new EventPlayerLocaleChange(this),
                new EventPlayerLogin(this),
                new EventPlayerMove(this),
                new EventPlayerPickupArrow(this),
                new EventPlayerPortal(this),
                new EventPlayerAsyncPreLogin(this),
                new EventPlayerQuit(this),
                new EventPlayerRecipeDiscover(this),
                new EventPlayerResourcePackStatus(this),
                new EventPlayerRespawn(this),
                new EventPlayerRiptide(this),
                new EventPlayerShearEntity(this),
                new EventPlayerStatisticIncrement(this),
                new EventPlayerSwapHandItems(this),
                new EventPlayerTakeLecternBook(this),
                new EventPlayerTeleport(this),
                new EventPlayerToggleFlight(this),
                new EventPlayerToggleSneak(this),
                new EventPlayerToggleSprint(this),
                new EventPlayerUnleashEntity(this),
                new EventPlayerVelocity(this)
                // TODO: add paper's events
        );

        // Block events
        registerEvents(
                new EventBlockBreak(this),
                new EventBlockPlace(this)
        );

        registerSyntax(CompileState.CODE_BODY, new EffectSend(this), new EffectBroadcast(this));

        removeReflectionsLogger();

        hookCommands();
        hookEvents();
    }

    protected void hookCommands() {
        registerSyntax(CompileState.ROOT, new MemberCommand(this));
    }

    // TODO : Find a way to do this more elegantly
    protected void hookEvents() {

            final Consumer<Event> onEvent = (event) -> {
                try {
                    final Object skriptEvent = Mirror.of(EVENTS.get(event.getClass()).getClasses()[0]).constructor(event.getClass()).invoke(event);
                    skript.runEvent((org.byteskript.skript.api.Event) skriptEvent);
                } catch (Exception ignored) {
                    /**
                     * TODO: investigate why this happens and fix it
                     * Note: This problem appears only for the PlayerLocaleChangeEvent and not all the time!
                     */
                    Bukkit.getLogger().warning("Failed to run event " + event.getClass().getName());
                }
            };

        Bukkit.getScheduler().runTaskAsynchronously(host, () -> {
            Reflections bskEvents = new Reflections(getClass().getPackage().getName() + ".syntax.event");
            Set<Class<? extends EventHolder>> bskEventClasses = bskEvents.getSubTypesOf(EventHolder.class);
            final Set<Class<? extends Event>> eventClasses = getEventClasses();
            registerListenerForEvents(bskEventClasses, eventClasses, onEvent);
        });

    }

    private void registerListenerForEvents(Set<Class<? extends EventHolder>> bskEventClasses, Set<Class<? extends Event>> eventClasses, Consumer<Event> onEvent) {
        eventClasses.forEach(eventClass -> bskEventClasses
                .stream()
                .filter(clazz -> eventClass.getSimpleName().contains(clazz.getSimpleName().substring(5)))
                .findFirst()
                .ifPresent(clazz -> {
                    EVENTS.put(eventClass, clazz);
                    host.getServer().getPluginManager().registerEvent(eventClass, new Listener() {
                    }, EventPriority.MONITOR, ((listener, event) -> onEvent.accept(event)), host);
                })
        );
    }

    private Set<Class<? extends Event>> getEventClasses() {
        Reflections[] eventPackages = { new Reflections("org.bukkit"), new Reflections("com.destroystokyo.paper") };
        final Set<Class<? extends Event>> eventClasses = new HashSet<>();
        for (Reflections reflect : eventPackages) {
            eventClasses.addAll(
                reflect.getSubTypesOf(Event.class)
                    .stream()
                    .filter(clazz -> Arrays.stream(clazz.getDeclaredFields())
                            .anyMatch(field -> field.getType().getName().endsWith("HandlerList")))
                    .collect(Collectors.toSet())
            );
        }
        return eventClasses;
    }

    private void removeReflectionsLogger() {
        try {
            final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            final Unsafe unsafe = (Unsafe) unsafeField.get(null);
            final Logger unsued = Reflections.log; // For some reason, if Reflections.log is not called, the field is not modified
            final Field logField = Reflections.class.getDeclaredField("log");
            final Object staticFieldBase = unsafe.staticFieldBase(logField);
            final long staticFieldOffset = unsafe.staticFieldOffset(logField);
            unsafe.putObject(staticFieldBase, staticFieldOffset, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
