package blue.lhf.bytepaper.library.syntax.command;

import blue.lhf.bytepaper.library.BytePaperFlag;
import mx.kenzie.foundation.Type;
import org.bukkit.command.CommandSender;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.error.ScriptParseError;
import org.byteskript.skript.lang.element.StandardElements;

import static mx.kenzie.foundation.WriteInstruction.load;

public class ExprExecutor extends SimpleExpression {
    public ExprExecutor(Library library) {
        super(library, StandardElements.EXPRESSION, "[the] [command] (executor|sender)");
    }

    @Override
    public Type getReturnType() {
        return new Type(CommandSender.class);
    }

    @Override
    public void preCompile(Context context, Pattern.Match match) throws Throwable {
        super.preCompile(context, match);
        if (!context.hasFlag(BytePaperFlag.IN_COMMAND)) {
            throw new ScriptParseError(context.lineNumber(), "The 'command executor' expression may only be used in " +
                "commands.");
        }
    }

    @Override
    public void compile(Context context, Pattern.Match match) throws Throwable {
        context.getMethod().writeCode(
            load(new Type(CommandSender.class), 0)
        );
    }
}
