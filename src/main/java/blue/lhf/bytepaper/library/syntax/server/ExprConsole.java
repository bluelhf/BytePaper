package blue.lhf.bytepaper.library.syntax.server;

import mx.kenzie.foundation.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.lang.element.StandardElements;

public class ExprConsole extends SimpleExpression {
    public ExprConsole(Library library) {
        super(library, StandardElements.EXPRESSION, "[the] [server] console");
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
