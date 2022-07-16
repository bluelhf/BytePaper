package blue.lhf.bytepaper.library.syntax.block;

import blue.lhf.bytepaper.library.syntax.SyntaxUtils;
import blue.lhf.bytepaper.library.syntax.direction.cardinal.Cardinal;
import blue.lhf.bytepaper.library.syntax.direction.egocentric.Egocentric;
import mx.kenzie.foundation.Type;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.RelationalExpression;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.lang.element.StandardElements;

import static mx.kenzie.foundation.WriteInstruction.*;

public class ExprBlockAt extends RelationalExpression {
    public ExprBlockAt(Library provider) {
        super(provider, StandardElements.EXPRESSION,
                "block at %Location%",
                "block %Integer% (blocks|met(er|re)s) [to the] %Cardinal% (from|of) %Location%",
                "block %Integer% (blocks|met(er|re)s) [in] %Egocentric% [of] %Location%");
    }

    @Override
    public Pattern.Match match(String thing, Context context) {
        return super.match(thing, context);
    }

    @Override
    public Type getReturnType() {
        return new Type(Block.class);
    }

    @Override
    public void compile(Context context, Pattern.Match match) throws Throwable {
        switch (match.matchedPattern) {
            case 0 -> compile0(context);
            case 1 -> compile1(context);
            case 2 -> compile2(context);
        }
    }

    protected void shuffleStack(Context context, Class<?> dirClass) {
        context.getMethod().writeCode(
                // object object object
                SyntaxUtils.convert(Location.class),
                // object object location
                swap(),
                // object location object
                SyntaxUtils.convert(dirClass),
                // object location cardinal
                duplicateDrop3(),
                // cardinal object location cardinal
                pop(),
                // cardinal object location
                swap(),
                // cardinal location object
                SyntaxUtils.convert(Integer.class),
                // cardinal location integer
                swap()
                // cardinal integer location
        );
    }

    public void compile0(Context context) throws NoSuchMethodException {
        context.getMethod().writeCode(
                SyntaxUtils.convert(Location.class),
                invoke(Location.class.getMethod("getBlock"))
        );
    }

    public void compile1(Context context) throws NoSuchMethodException {
        shuffleStack(context, Cardinal.class);
        context.getMethod().writeCode(invoke(ExprBlockAt.class.getDeclaredMethod("relative", Cardinal.class, Integer.class, Location.class)));
    }

    public void compile2(Context context) throws NoSuchMethodException {
        shuffleStack(context, Egocentric.class);
        context.getMethod().writeCode(invoke(ExprBlockAt.class.getDeclaredMethod("relative", Egocentric.class, Integer.class, Location.class)));
    }

    public static Block relative(Egocentric direction, Integer multiplier, Location base) {
        return base.clone().add(direction.getVector(base).multiply(multiplier)).getBlock();
    }

    public static Block relative(Cardinal direction, Integer multiplier, Location base) {
        return base.clone().add(direction.getVector().multiply(multiplier)).getBlock();
    }
}