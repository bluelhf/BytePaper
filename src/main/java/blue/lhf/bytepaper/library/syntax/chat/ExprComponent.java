package blue.lhf.bytepaper.library.syntax.chat;

import blue.lhf.bytepaper.library.syntax.SyntaxUtils;
import blue.lhf.bytepaper.util.UI;
import mx.kenzie.foundation.Type;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.lang.element.StandardElements;

import static mx.kenzie.foundation.WriteInstruction.*;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

@SuppressWarnings("UnnecessaryStringEscape")
@Documentation(
    name = "Component",
    description = "Deserialises a formatted message from a string",
    examples = {
        """
        broadcast raw "<rainbow>This text will not be rainbow; the MiniMessage tags aren't parsed</rainbow>"
        """,
        """
        broadcast mini "<rainbow>This text will be rainbow; the MiniMessage tags are parsed</rainbow>"
        """,
        """
        broadcast legacy "&aThis text will be bright green, because it is parsed using the ampersand legacy serializer!"
        """,
        """
        broadcast json "{\"text\":\"This is a complicated, internal JSON format for messages\",\"color\":\"bright_magenta\"}"
        """
    }
)
public class ExprComponent extends SimpleExpression {
    public ExprComponent(Library library) {
        super(library, StandardElements.EXPRESSION, "(raw|mini|legacy|json) [component] [from] %Object%");
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
                builder.writeCode(push(false));
                writeCall(builder, findMethod(UI.class, "miniMessage", boolean.class), context);
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
