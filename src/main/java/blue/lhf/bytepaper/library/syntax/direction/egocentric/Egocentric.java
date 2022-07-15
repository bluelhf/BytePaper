package blue.lhf.bytepaper.library.syntax.direction.egocentric;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public enum Egocentric {
    ABOVE, BELOW, FRONT, BEHIND, LEFT, RIGHT;

    public Vector getVector(Location ego) {
        return switch (this) {
            case ABOVE -> new Vector(0, 1, 0);
            case BELOW -> new Vector(0, -1, 0);
            case FRONT -> ego.getDirection().clone();
            case BEHIND -> ego.getDirection().clone().multiply(-1);
            case LEFT -> ego.getDirection().clone().multiply(new Vector(1, 0, 1)).rotateAroundY(Math.PI/2);
            case RIGHT -> ego.getDirection().clone().multiply(new Vector(1, 0, 1)).rotateAroundY(-Math.PI/2);
        };
    }
}
