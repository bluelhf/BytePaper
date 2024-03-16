package blue.lhf.bytepaper.library.syntax.block;

import blue.lhf.bytepaper.util.property.Property;
import blue.lhf.bytepaper.util.property.PropertyActor;
import blue.lhf.bytepaper.util.property.PropertyHolder;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;
import org.byteskript.skript.lang.handler.StandardHandlers;

import java.util.concurrent.ExecutionException;

@SuppressWarnings("unused")
@PropertyHolder(owner = Block.class)
public class BlockProperties {
    private BlockProperties() {}

    @Property(name = "data", type = BlockData.class)
    public static class PropData {
        private PropData() {}
        @PropertyActor(StandardHandlers.GET)
        public static BlockData get(Block block) throws ExecutionException, InterruptedException {
            return Bukkit.getScheduler().callSyncMethod(
                    JavaPlugin.getProvidingPlugin(BlockProperties.class),
                    block::getBlockData
            ).get();
        }

        @PropertyActor(StandardHandlers.SET)
        public static void set(Block block, BlockData data) {
            Bukkit.getScheduler().runTask(
                    JavaPlugin.getProvidingPlugin(BlockProperties.class),
                    () -> block.setBlockData(data)
            );
        }
    }
}
