package blue.lhf.bsfp.library.syntax;

import blue.lhf.bsfp.library.PaperBridgeSpec;
import mx.kenzie.foundation.Type;
import mx.kenzie.foundation.WriteInstruction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.Context;
import org.byteskript.skript.compiler.Pattern;
import org.byteskript.skript.lang.element.StandardElements;

import static mx.kenzie.foundation.WriteInstruction.*;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class ComponentExpression extends SimpleExpression {
    public ComponentExpression() {
        super(PaperBridgeSpec.LIBRARY, StandardElements.EXPRESSION, "(raw|mini|legacy|json) [component] [from] %String%");
    }

    @Override
    public Type getReturnType() {
        return new Type(Component.class);
    }

    @Override
    public void compile(Context context, Pattern.Match match) {
        var builder = context.getMethod();
        WriteInstruction converter = (writer, visitor) -> {
            visitor.visitLdcInsn(org.objectweb.asm.Type.getType(String.class));
            visitor.visitMethodInsn(INVOKESTATIC,
                    "org/byteskript/skript/runtime/Skript",
                    "convert",
                    "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;",
                    false);
        };

        switch (match.matcher().group().split(" ")[0]) {
            case "raw" -> writeCall(builder, findMethod(Component.class, "text", String.class), context);
            case "mini" -> {
                writeCall(builder, findMethod(MiniMessage.class, "miniMessage"), context);
                builder.writeCode(swap());
                builder.writeCode(converter);
                builder.writeCode(invokeInterface(findMethod(MiniMessage.class, "deserialize", Object.class)));
            }
            case "legacy" -> {
                writeCall(builder, findMethod(LegacyComponentSerializer.class, "legacyAmpersand"), context);
                builder.writeCode(swap());
                builder.writeCode(converter);
                builder.writeCode(invokeInterface(findMethod(LegacyComponentSerializer.class, "deserialize", Object.class)));
            }
            case "json" -> {
                writeCall(builder, findMethod(GsonComponentSerializer.class, "gson"), context);
                builder.writeCode(swap());
                builder.writeCode(converter);
                builder.writeCode(invokeInterface(findMethod(GsonComponentSerializer.class, "deserialize", Object.class)));
            }
            default -> throw new AssertionError("invalid");
        }
    }
}
