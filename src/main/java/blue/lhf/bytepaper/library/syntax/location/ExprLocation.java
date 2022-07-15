package blue.lhf.bytepaper.library.syntax.location;

import mx.kenzie.foundation.Type;
import org.bukkit.Location;
import org.bukkit.World;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.Context;
import org.byteskript.skript.compiler.Pattern;
import org.byteskript.skript.lang.element.StandardElements;

import static blue.lhf.bytepaper.library.syntax.SyntaxUtils.convert;
import static mx.kenzie.foundation.WriteInstruction.*;

public class ExprLocation extends SimpleExpression {
    public ExprLocation(Library provider) {
        super(provider, StandardElements.EXPRESSION, "[location] [at] %Number%, %Number%, %Number% in %World%");
    }

    @Override
    public Type getReturnType() {
        return new Type(Location.class);
    }

    @Override
    public Pattern.Match match(String thing, Context context) {
        return super.match(thing, context);
    }

    @Override
    public void compile(Context context, Pattern.Match match) throws Throwable {
        context.getMethod().writeCode(
                // xyzw
                convert(World.class),
                // xyzw
                swap(),
                // xywz
                convert(Number.class),
                // xywz
                duplicate2Drop3(),
                // wzxywz
                pop2(),
                // wzxy
                convert(Number.class),
                // wzxy
                swap(),
                // wzyx
                convert(Number.class),
                // fuck it i'm not swapping the stack back together
                invoke(ExprLocation.class.getDeclaredMethod("get", World.class, Number.class, Number.class, Number.class))
        );
    }



    public static Location get(World world, Number z, Number y, Number x) {
        return new Location(world, x.doubleValue(), y.doubleValue(), z.doubleValue());
    }
}
