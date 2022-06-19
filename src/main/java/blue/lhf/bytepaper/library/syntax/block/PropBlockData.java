package blue.lhf.bytepaper.library.syntax.block;

import blue.lhf.bytepaper.util.Threading;
import mx.kenzie.foundation.Type;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.byteskript.skript.api.ModifiableLibrary;
import org.byteskript.skript.api.PropertyHandler;
import org.byteskript.skript.lang.handler.StandardHandlers;

public class PropBlockData {
    private PropBlockData() {

    }

    public static void register(ModifiableLibrary library) {
        try {
            library.registerProperty(new PropertyHandler("data",
                    StandardHandlers.GET, new Type(Block.class), new Type(BlockData.class),
                    PropBlockData.class.getDeclaredMethod("get", Block.class)
            ));

            library.registerProperty(new PropertyHandler("data",
                    StandardHandlers.SET, new Type(Block.class), new Type(BlockData.class),
                    PropBlockData.class.getDeclaredMethod("set", Block.class, BlockData.class)));
        } catch (NoSuchMethodException e) {
            throw new AssertionError("Cannot happen", e);
        }
    }

    public static BlockData get(Block block) {
        return Threading.forceMain(block::getBlockData);
    }

    public static void set(Block block, BlockData data) {
        Threading.forceMain(() -> block.setBlockData(data));
    }
}
