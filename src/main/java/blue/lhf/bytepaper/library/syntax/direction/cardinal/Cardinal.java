package blue.lhf.bytepaper.library.syntax.direction.cardinal;

import org.bukkit.util.BlockVector;

public enum Cardinal {
    UP, DOWN, NORTH, EAST, SOUTH, WEST;

    public BlockVector getVector() {
        return switch (this) {
            case UP -> new BlockVector(0, 1, 0);
            case DOWN -> new BlockVector(0, -1, 0);
            case NORTH -> new BlockVector(0, 0, -1);
            case EAST -> new BlockVector(1, 0, 0);
            case SOUTH -> new BlockVector(0, 0, 1);
            case WEST -> new BlockVector(-1, 0, 0);
        };
    }
}
