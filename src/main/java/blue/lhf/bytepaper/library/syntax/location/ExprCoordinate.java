package blue.lhf.bytepaper.library.syntax.location;

import blue.lhf.bytepaper.library.syntax.SyntaxUtils;
import org.bukkit.Location;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.RelationalExpression;
import org.byteskript.skript.compiler.Context;
import org.byteskript.skript.compiler.Pattern;
import org.byteskript.skript.lang.element.StandardElements;

import static mx.kenzie.foundation.WriteInstruction.invoke;
import static mx.kenzie.foundation.WriteInstruction.swap;

public class ExprCoordinate extends RelationalExpression {
    public ExprCoordinate(Library provider) {
        super(provider, StandardElements.EXPRESSION,
                "x( |-)(pos|coord[inate]) of %Location%",
                "y( |-)(pos|coord[inate]) of %Location%",
                "z( |-)(pos|coord[inate]) of %Location%");
    }

    @Override
    public void compile(Context context, Pattern.Match match) throws Throwable {
        boolean input = context.getHandlerMode().expectInputs();
        boolean output = context.getHandlerMode().expectReturn();
        String methodName = input ? "set" : "get";
        methodName += switch (match.matchedPattern) {
            case 0 -> "X"; case 1 -> "Y"; case 2 -> "Z";
            default -> throw new IllegalStateException(
                    "Unexpected value: " + match.matchedPattern);
        };

        if (input) {
            context.getMethod().writeCode(
                    swap(),
                    SyntaxUtils.convert(Location.class),
                    swap(),
                    invoke(Location.class.getDeclaredMethod(methodName, double.class))
            );
        } else {
            context.getMethod().writeCode(
                    SyntaxUtils.convert(Location.class),
                    invoke(Location.class.getDeclaredMethod(methodName))
            );
        }

        if (output) {
            context.getMethod().writeCode(
                    invoke(Double.class.getDeclaredMethod("valueOf", double.class))
            );
        }
    }
}
