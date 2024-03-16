package blue.lhf.bytepaper.library.syntax.block;

import blue.lhf.bytepaper.library.syntax.entity.ExprEntities;
import mx.kenzie.foundation.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.lang.element.StandardElements;
import org.byteskript.skript.lang.handler.StandardHandlers;
import org.byteskript.skript.runtime.Skript;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExprBlocksWithin extends SimpleExpression {
    public ExprBlocksWithin(Library library) {
        super(library, StandardElements.EXPRESSION, "blocks within %Object% and %Object%");
        setHandler(StandardHandlers.GET, findMethod(getClass(), "getBlocks", Object.class, Object.class));
    }

    @Override
    public Type getReturnType() {
        return new Type(Block[].class);
    }

    @SuppressWarnings("unused")
    public static Block[] getBlocks(final Object oneObject, final Object twoObject) throws InterruptedException, ExecutionException {
        final Location one = Skript.convert(oneObject, Location.class).toBlockLocation();
        final Location two = Skript.convert(twoObject, Location.class).toBlockLocation();

        return Bukkit.getScheduler().callSyncMethod(JavaPlugin.getProvidingPlugin(ExprEntities.class), () -> {
            final int minX = Math.min(one.getBlockX(), two.getBlockX());
            final int minY = Math.min(one.getBlockY(), two.getBlockY());
            final int minZ = Math.min(one.getBlockZ(), two.getBlockZ());

            final int maxX = Math.max(one.getBlockX(), two.getBlockX());
            final int maxY = Math.max(one.getBlockY(), two.getBlockY());
            final int maxZ = Math.max(one.getBlockZ(), two.getBlockZ());

            List<Block> blocks = new ArrayList<>();

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        blocks.add(one.getWorld().getBlockAt(x, y, z));
                    }
                }
            }

            return blocks.toArray(new Block[0]);
        }).get();
    }
}