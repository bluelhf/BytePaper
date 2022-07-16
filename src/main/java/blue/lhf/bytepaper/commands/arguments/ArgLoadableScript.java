package blue.lhf.bytepaper.commands.arguments;

import com.moderocky.mask.command.Argument;
import org.jetbrains.annotations.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.function.Supplier;

import static blue.lhf.bytepaper.commands.arguments.ArgUtils.toFileScript;

public class ArgLoadableScript implements Argument<Path> {
    private final Supplier<Path> root;
    private boolean required = true;
    private String label = null;

    public ArgLoadableScript(Supplier<Path> rootSupplier) {
        this.root = rootSupplier;
    }

    @Override
    public @NotNull Path serialise(String s) {
        return root.get().resolve(toFileScript(s));
    }

    @Override
    public boolean matches(String s) {
        try {
            return Files.exists(serialise(s));
        } catch (InvalidPathException e) {
            return false;
        }
    }

    @Override
    public @NotNull String getName() {
        return label != null ? label : "path";
    }

    @Override
    public @Nullable List<String> getCompletions() {
        Path r = root.get();
        try (var stream = Files.walk(r)) {
            return stream
                .map(r::relativize)
                .map(Path::toString)
                .filter(ArgUtils::isValidFileScript)
                .toList();
        } catch (IOException e) {
            return null;
        }
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
    public Argument<Path> setRequired(boolean b) {
        required = b;
        return this;
    }

    @Override
    public Argument<Path> setLabel(@NotNull String s) {
        label = s;
        return this;
    }
}
