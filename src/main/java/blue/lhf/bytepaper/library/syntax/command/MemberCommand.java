package blue.lhf.bytepaper.library.syntax.command;

import blue.lhf.bytepaper.library.BytePaperFlag;
import mx.kenzie.foundation.Type;
import org.bukkit.command.*;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.TriggerHolder;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.compiler.structure.*;
import org.byteskript.skript.lang.element.StandardElements;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.*;

import static mx.kenzie.foundation.WriteInstruction.*;
import static org.byteskript.skript.compiler.Pattern.Match;

//TODO(ilari): Support for patterns, expressions for the command data
public class MemberCommand extends TriggerHolder {

    private static final Pattern COMMAND_PATTERN = Pattern.compile("^command /?(?<name>" + SkriptLangSpec.IDENTIFIER + ")");
    //private static final Pattern ARGUMENT_PATTERN = Pattern.compile("\\s*(?:(?<typed><(?:(?<type>.+?)=)?(?<name>.+?)(?:: ?(?<def>.+?))?>)|(?<optional>\\[.+?])|(?<literal>[^<\\[]+))");

    public MemberCommand(Library library) {
        super(library, StandardElements.MEMBER, "command");
    }

    @Override
    public Match match(String thing, Context context) {
        final Matcher matcher = COMMAND_PATTERN.matcher(thing);
        return matcher.find() ? new Match(matcher) : null;
    }

    @Override
    public void onSectionExit(Context context, SectionMeta meta) {
        context.removeFlag(BytePaperFlag.IN_COMMAND);
        super.onSectionExit(context, meta);
    }

    @Override
    public void compile(Context context, Match match) {
        super.compile(context, match);

        PreVariable executor = new PreVariable(null);
        executor.internal = true;
        context.forceUnspecVariable(executor);

        String name = match.matcher().group("name");
        String path = context.getType().internalName() + "$Command" + name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase() + "Executor";
        Type type = Type.of(path);
        context.addFlag(BytePaperFlag.IN_COMMAND);

        context.getBuilder().addInnerClass(type, Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC);

        context.addSuppressedBuilder(type)
            .addModifiers(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
            .addInterfaces(CommandExecutor.class)
            .addMethod("onCommand")
            .addAnnotation(CommandData.class)
            .addValue("label", name)
            .setVisible(true)
            .finish()
            .addAnnotation(Override.class).finish()
            .setModifiers(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
            .setReturnType(boolean.class)
            .addParameter(CommandSender.class, Command.class, String.class, String[].class)
            .writeCode(
                load(new Type(CommandSender.class), 0),
                load(new Type(Command.class), 1),
                load(new Type(String.class), 2),
                load(new Type(String[].class), 3),
                invokeStatic(false, context.getType(), returnType(context, match), name, parameters(context, match.matcher())),
                push(true),
                returnSmall()
            ).finish();
    }

    @Override
    public String callSiteName(Context context, Match match) {
        return match.matcher().group("name");
    }

    @Override
    public Type returnType(Context context, Match match) {
        return CommonTypes.VOID;
    }

    @Override
    public Type[] parameters(Context context, Matcher match) {
        Arrays.asList("__sender__", "__command__", "__label__", "__args__")
                .forEach(v -> context.getVariable(v).parameter = true);
        return Type.of(CommandSender.class, Command.class, String.class, String[].class);
    }

    @Override
    public boolean allowAsInputFor(Type type) {
        return false;
    }
}
