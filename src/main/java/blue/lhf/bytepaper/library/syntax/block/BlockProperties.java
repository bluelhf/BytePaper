package blue.lhf.bytepaper.library.syntax.block;

import blue.lhf.bytepaper.util.Threading;
import blue.lhf.bytepaper.util.property.Property;
import blue.lhf.bytepaper.util.property.PropertyActor;
import blue.lhf.bytepaper.util.property.PropertyHolder;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.byteskript.skript.lang.handler.StandardHandlers;

@SuppressWarnings("unused")
@PropertyHolder(owner = Block.class)
public class BlockProperties {
    private BlockProperties() {}

    @Property(name = "data", type = BlockData.class)
    public static class PropData {
        private PropData() {}
        @PropertyActor(StandardHandlers.GET)
        public static BlockData get(Block block) {
            return Threading.forceMain(block::getBlockData);
        }

        @PropertyActor(StandardHandlers.SET)
        public static void set(Block block, BlockData data) {
            Threading.forceMain(() -> block.setBlockData(data));
        }
    }
}
