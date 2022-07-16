package blue.lhf.bytepaper.library.syntax.direction.cardinal;

import mx.kenzie.foundation.*;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.Literal;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.lang.element.StandardElements;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static mx.kenzie.foundation.WriteInstruction.getStaticField;

public class LiteralCardinal extends Literal<Cardinal> {
    private static final Map<String, Cardinal> parseMap = new HashMap<>();

    static {
        for (Cardinal dir : Cardinal.values()) {
            parseMap.put(dir.name(), dir);
        }
    }

    public LiteralCardinal(Library library) {
        super(library, StandardElements.EXPRESSION, "(" + parseMap.keySet()
                .stream()
                .map(java.util.regex.Pattern::quote)
                .map(s -> s.replace("\\", "\\\\"))
                .collect(Collectors.joining("|")) + ")");
    }

    @Override
    public Type getReturnType() {
        return new Type(Cardinal.class);
    }

    public void compile(Context context, Pattern.Match match) {
        String string = match.matcher().group();

        assert string.length() > 1;

        MethodBuilder method = context.getMethod();
        Cardinal type = parse(string);
        method.writeCode(getStaticField(new Type(type.getDeclaringClass()), new FieldErasure(type.getDeclaringClass(), type.name())));
    }


    @Override
    public Cardinal parse(String s) {
        return parseMap.get(s);
    }
}
