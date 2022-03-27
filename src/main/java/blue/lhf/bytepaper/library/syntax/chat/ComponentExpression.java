package blue.lhf.bytepaper.library.syntax.chat;

import blue.lhf.bytepaper.library.PaperBridgeSpec;
import blue.lhf.bytepaper.library.syntax.SyntaxUtils;
import blue.lhf.bytepaper.util.UI;
import mx.kenzie.foundation.Type;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.Context;
import org.byteskript.skript.compiler.Pattern;
import org.byteskript.skript.lang.element.StandardElements;

import static mx.kenzie.foundation.WriteInstruction.invokeInterface;
import static mx.kenzie.foundation.WriteInstruction.swap;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class ComponentExpression extends SimpleExpression {
    public ComponentExpression() {
        super(PaperBridgeSpec.LIBRARY, StandardElements.EXPRESSION, "(raw|mini|legacy|json) [component] [from] %Object%");
    }

    @Override
    public Type getReturnType() {
        return new Type(Component.class);
    }

    @Override
    public void compile(Context context, Pattern.Match match) {
        var builder = context.getMethod();
        builder.writeCode(SyntaxUtils.convert(String.class));
        switch (match.matcher().group().split(" ")[0]) {
            case "raw" -> builder.writeCode((writer, visitor) ->
                visitor.visitMethodInsn(
                    INVOKESTATIC,
                    "net/kyori/adventure/text/Component",
                    "text",
                    "(Ljava/lang/String;)Lnet/kyori/adventure/text/TextComponent;",
                    true
                ));
            case "mini" -> {
                writeCall(builder, findMethod(UI.class, "miniMessage"), context);
                builder.writeCode(swap());
                builder.writeCode(invokeInterface(findMethod(MiniMessage.class, "deserialize", Object.class)));
            }
            case "legacy" -> {
                writeCall(builder, findMethod(LegacyComponentSerializer.class, "legacyAmpersand"), context);
                builder.writeCode(swap());
                builder.writeCode(invokeInterface(findMethod(LegacyComponentSerializer.class, "deserialize", Object.class)));
            }
            case "json" -> {
                writeCall(builder, findMethod(GsonComponentSerializer.class, "gson"), context);
                builder.writeCode(swap());
                builder.writeCode(invokeInterface(findMethod(GsonComponentSerializer.class, "deserialize", Object.class)));
            }
            default -> throw new AssertionError("invalid");
        }
    }
}
