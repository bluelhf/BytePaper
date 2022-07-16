package blue.lhf.bytepaper.library.syntax.command;

import blue.lhf.bytepaper.library.BytePaperFlag;
import mx.kenzie.foundation.Type;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.error.ScriptParseError;
import org.byteskript.skript.lang.element.StandardElements;

import java.util.Arrays;

import static mx.kenzie.foundation.WriteInstruction.*;

@SuppressWarnings("UnnecessaryStringEscape")
@Documentation(
    name = "Arguments",
    description = "Represents the arguments given as input to a command",
    examples = {
        """
        command /hello:
            trigger:
                if index 0 in the command arguments is "world":
                    send "Good mornin', starshine. The earth says, \"Hello!\"" to the executor
                else if index 0 in args is "there":
                    send "General Kenobi!" to the executor
                else:
                    send "Hi?" to the executor
        """
    }
)
public class ExprArguments extends SimpleExpression {
    public ExprArguments(Library library) {
        super(library, StandardElements.EXPRESSION, "[the] [command] arg[ument]s");
    }

    @Override
    public void preCompile(Context context, Pattern.Match match) throws Throwable {
        super.preCompile(context, match);
        if (!context.hasFlag(BytePaperFlag.IN_COMMAND)) {
            throw new ScriptParseError(context.lineNumber(), context.getError(), "The 'command arguments' expression " +
                "may only be used in commands.", null);
        }
    }

    @Override
    public Type getReturnType() {
        return CommonTypes.LIST;
    }

    @Override
    public void compile(Context context, Pattern.Match match) throws Throwable {
        context.getMethod().writeCode(
            load(new Type(String[].class), 3),
            cast(new Type(Object[].class)),
            invokeStatic(Arrays.class.getDeclaredMethod("asList", Object[].class))
        );
    }
}
