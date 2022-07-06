package blue.lhf.bytepaper.library.syntax.command;

import blue.lhf.bytepaper.library.BytePaperFlag;
import mx.kenzie.foundation.Type;
import org.bukkit.command.*;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.TriggerHolder;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.compiler.structure.*;
import org.byteskript.skript.error.*;
import org.byteskript.skript.lang.element.StandardElements;
import org.byteskript.skript.runtime.data.SourceData;
import org.objectweb.asm.Opcodes;

import java.time.Instant;
import java.util.regex.Pattern;
import java.util.regex.*;

import static mx.kenzie.foundation.WriteInstruction.*;
import static org.byteskript.skript.compiler.Pattern.Match;

//TODO(ilari): Support for patterns, expressions for the command data
public class MemberCommand extends TriggerHolder {

    private static final Pattern COMMAND_PATTERN = Pattern.compile("^command /?(?<name>" + SkriptLangSpec.IDENTIFIER + ")");
    private final CommandRegistrar registrar;
    //private static final Pattern ARGUMENT_PATTERN = Pattern.compile("\\s*(?:(?<typed><(?:(?<type>.+?)=)?(?<name>.+?)(?:: ?(?<def>.+?))?>)|(?<optional>\\[.+?])|(?<literal>[^<\\[]+))");

    public MemberCommand(Library library, CommandRegistrar registrar) {
        super(library, StandardElements.MEMBER, "command");
        this.registrar = registrar;
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
    public void preCompile(Context context, Match match) throws Throwable {
        String name = match.matcher().group("name");
        if (registrar.exists(name)) {
            throw new ScriptParseError(context.lineNumber(),
                context.getError(), "The command /" + name + " could not be registered, because another command with " +
                "that name already exists", null);
        }

        super.preCompile(context, match);
    }

    @Override
    public void compile(Context context, Match match) {
        super.compile(context, match);

        PreVariable executor = new PreVariable(null);
        executor.internal = true;
        context.forceUnspecVariable(executor);

        String name = match.matcher().group("name");
        String path = context.getType().internalName() + "/command_" + name + "_executor";
        context.addFlag(BytePaperFlag.IN_COMMAND);
        context.addSuppressedBuilder(Type.of(path))
            .addAnnotation(SourceData.class).setVisible(true)
            .addValue("name", this.name())
            .addValue("type", "command")
            .addValue("line", context.lineNumber())
            .addValue("compiled", Instant.now().getEpochSecond())
            .finish()
            .addAnnotation(CommandData.class)
            .addValue("label", name)
            .setVisible(true)
            .finish()
            .setModifiers(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
            .addInterfaces(CommandExecutor.class)
            .addMethod("onCommand")
            .addAnnotation(Override.class).finish()
            .setModifiers(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
            .setReturnType(boolean.class)
            .addParameter(CommandSender.class, Command.class, String.class, String[].class)
            .writeCode(
                load(new Type(CommandSender.class), 0),
                load(new Type(Command.class), 1),
                load(new Type(String.class), 2),
                load(new Type(String[].class), 3),
                invokeStatic(false, context.getType(), returnType(context, match), name, parameters(context, match)),
                push(true),
                returnSmall()
            ).finish();

        try {
            registrar.register(name, Type.of(path));
        } catch (DuplicateCommandException e) {
            throw new ScriptCompileError(context.lineNumber(), e);
        }
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
    public Type[] parameters(Context context, Match match) {
        return Type.of(CommandSender.class, Command.class, String.class, String[].class);
    }

    @Override
    public boolean allowAsInputFor(Type type) {
        return false;
    }
}
