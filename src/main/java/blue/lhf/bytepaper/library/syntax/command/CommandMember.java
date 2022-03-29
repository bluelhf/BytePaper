package blue.lhf.bytepaper.library.syntax.command;

import blue.lhf.bytepaper.library.BytePaperFlag;
import mx.kenzie.foundation.Type;
import org.bukkit.command.*;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.TriggerHolder;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.compiler.structure.SectionMeta;
import org.byteskript.skript.error.*;
import org.byteskript.skript.lang.element.StandardElements;
import org.objectweb.asm.Opcodes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static mx.kenzie.foundation.WriteInstruction.*;
import static org.byteskript.skript.compiler.Pattern.Match;

//TODO(ilari): Support for patterns, expressions for the command data
public class CommandMember extends TriggerHolder {

    private static final Pattern COMMAND_PATTERN = Pattern.compile("^command /?(?<name>" + SkriptLangSpec.IDENTIFIER + ")");
    private final CommandRegistrar registrar;
    //private static final Pattern ARGUMENT_PATTERN = Pattern.compile("\\s*(?:(?<typed><(?:(?<type>.+?)=)?(?<name>.+?)(?:: ?(?<def>.+?))?>)|(?<optional>\\[.+?])|(?<literal>[^<\\[]+))");

    public CommandMember(Library library, CommandRegistrar registrar) {
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
    public void compile(Context context, Match match) {
        super.compile(context, match);
        String name = match.matcher().group("name");
        String path = context.getType().internalName() + "/command_" + name + "_executor";
        context.addFlag(BytePaperFlag.IN_COMMAND);
        context.addSuppressedBuilder(Type.of(path))
            .setModifiers(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC)
            .addInterfaces(CommandExecutor.class)
            .addMethod("onCommand")
            .addAnnotation(Override.class).finish()
            .addAnnotation(CommandData.class).setVisible(true).finish()
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
            registrar.accept(name, Type.of(path));
        } catch (DuplicateCommandException e) {
            throw new ScriptCompileError(context.lineNumber(),
                    "The command /" + name + " could not be registered, because another command with that name already exists.", e);
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
