package blue.lhf.bytepaper.commands.arguments;

import blue.lhf.bytepaper.IScriptLoader;
import com.moderocky.mask.command.Argument;
import org.byteskript.skript.runtime.Script;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;

import static blue.lhf.bytepaper.commands.arguments.ArgUtils.toFileScript;
import static blue.lhf.bytepaper.commands.arguments.ArgUtils.toInternalScript;

public class ArgLoadedScript implements Argument<Optional<Script>> {
    private final Supplier<IScriptLoader> loader;
    private final Predicate<Script> filter;
    private boolean required = true;
    private String label = null;

    public ArgLoadedScript(Supplier<IScriptLoader> loaderSupplier, Predicate<Script> filter) {
        this.loader = loaderSupplier;
        this.filter = filter;
    }

    @Override
    public @NotNull Optional<Script> serialise(String s) {
        Script target = null;
        for (Script script : loader.get().getSkript().getScripts()) {
            if (script.getPath().equals(toInternalScript(s, false)))
                target = script;
        }

        return Optional.ofNullable(target);
    }

    @Override
    public boolean matches(String s) {
        return getCompletions().contains(toFileScript(s));
    }

    @Override
    public @NotNull String getName() {
        return label != null ? label : "script";
    }

    @Override
    public @NotNull List<String> getCompletions() {
        return Arrays.stream(loader.get().getSkript().getScripts()).filter(filter).map(Script::getPath)
            .map(ArgUtils::toExternalScript).map(ArgUtils::toFileScript).toList();
    }

    @Override
    public boolean isPlural() {
        return false;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public Argument<Optional<Script>> setRequired(boolean b) {
        required = b;
        return this;
    }

    @Override
    public Argument<Optional<Script>> setLabel(@NotNull String s) {
        label = s;
        return this;
    }
}
