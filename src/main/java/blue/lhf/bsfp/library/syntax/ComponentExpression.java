package blue.lhf.bsfp.library.syntax;

import blue.lhf.bsfp.library.PaperBridgeSpec;
import mx.kenzie.foundation.Type;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.Context;
import org.byteskript.skript.compiler.Pattern;
import org.byteskript.skript.lang.element.StandardElements;

public class ComponentExpression extends SimpleExpression {
    public ComponentExpression() {
        super(PaperBridgeSpec.LIBRARY, StandardElements.EXPRESSION, "(raw|mini|legacy|json) component from %String%");
    }

    @Override
    public Type getReturnType() {
        return new Type(Component.class);
    }

    @Override
    public void compile(Context context, Pattern.Match match) {
        var builder = context.getMethod();
        System.out.println(match.matcher());
        switch (match.matcher().group().split(" ")[0]) {
            case "raw" -> writeCall(builder, findMethod(Component.class, "text", String.class), context);
            case "mini" -> {
                writeCall(builder, findMethod(MiniMessage.class, "miniMessage"), context);
                writeCall(builder, findMethod(MiniMessage.class, "deserialize", Object.class), context);
            }
            case "legacy" -> {
                writeCall(builder, findMethod(LegacyComponentSerializer.class, "legacyAmpersand"), context);
                writeCall(builder, findMethod(LegacyComponentSerializer.class, "deserialize", Object.class), context);
            }
            case "json" -> {
                writeCall(builder, findMethod(GsonComponentSerializer.class, "gson"), context);
                writeCall(builder, findMethod(GsonComponentSerializer.class, "deserialize", Object.class), context);
            }
            default -> throw new AssertionError("invalid");
        }
    }
}
