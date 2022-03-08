package blue.lhf.bsfp.library.syntax;

import blue.lhf.bsfp.library.PaperBridgeSpec;
import mx.kenzie.foundation.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.Context;
import org.byteskript.skript.compiler.Pattern;
import org.byteskript.skript.lang.element.StandardElements;

public class ConsoleExpression extends SimpleExpression {
    public ConsoleExpression() {
        super(PaperBridgeSpec.LIBRARY, StandardElements.EXPRESSION, "[the] [server] console");
    }

    @Override
    public Type getReturnType() {
        return new Type(ConsoleCommandSender.class);
    }

    @Override
    public void compile(Context context, Pattern.Match match) {
        writeCall(context.getMethod(), findMethod(Bukkit.class, "getConsoleSender"), context);
    }
}
