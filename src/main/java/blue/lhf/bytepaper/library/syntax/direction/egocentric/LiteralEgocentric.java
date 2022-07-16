package blue.lhf.bytepaper.library.syntax.direction.egocentric;

import mx.kenzie.foundation.*;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.Literal;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.lang.element.StandardElements;

import java.util.*;
import java.util.stream.Collectors;

import static mx.kenzie.foundation.WriteInstruction.getStaticField;

public class LiteralEgocentric extends Literal<Egocentric> {
    public LiteralEgocentric(Library library) {
        super(library, StandardElements.EXPRESSION, "(" +
            Arrays.stream(Egocentric.values())
                .map(Egocentric::name).map(String::toLowerCase)
                .map(java.util.regex.Pattern::quote)
                .map(s -> s.replace("\\", "\\\\"))
                .collect(Collectors.joining("|")) + ")");
    }

    @Override
    public Pattern.Match match(String thing, Context context) {
        return super.match(thing, context);
    }

    @Override
    public Type getReturnType() {
        return new Type(Egocentric.class);
    }

    public void compile(Context context, Pattern.Match match) {
        String string = match.matcher().group();

        assert string.length() > 1;

        MethodBuilder method = context.getMethod();
        Egocentric type = parse(string);
        method.writeCode(getStaticField(new Type(type.getDeclaringClass()), new FieldErasure(type.getDeclaringClass(), type.name())));
    }

    @Override
    public Egocentric parse(String s) {
        return Egocentric.valueOf(s.toUpperCase(Locale.ROOT));
    }
}
