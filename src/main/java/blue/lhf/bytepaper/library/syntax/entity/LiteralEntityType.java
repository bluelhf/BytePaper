package blue.lhf.bytepaper.library.syntax.entity;

import mx.kenzie.foundation.MethodBuilder;
import mx.kenzie.foundation.Type;
import org.bukkit.entity.EntityType;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.Literal;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.lang.element.StandardElements;

import java.util.*;
import java.util.stream.Collectors;

import static mx.kenzie.foundation.WriteInstruction.loadConstant;

public class LiteralEntityType extends Literal<EntityType> {

    private static final Map<String, EntityType> parseMap = new HashMap<>();

    static {
        for (EntityType type : EntityType.values()) {
            if (type == EntityType.UNKNOWN) continue;
            var key = type.getKey();
            parseMap.put(key.value(), type);
            parseMap.put(key.asString(), type);
        }
    }

    public LiteralEntityType(Library library) {
        super(library, StandardElements.EXPRESSION, "(" + parseMap.keySet()
                .stream()
                .map(java.util.regex.Pattern::quote)
                .map(s -> s.replace("\\", "\\\\"))
                .collect(Collectors.joining("|")) + ")");
    }

    @Override
    public Type getReturnType() {
        return new Type(EntityType.class);
    }

    public void compile(Context context, Pattern.Match match) throws Throwable {
        String string = match.matcher().group();

        assert string.length() > 1;

        MethodBuilder method = context.getMethod();

        method.writeCode(loadConstant(string));
        writeCall(method, findMethod(getClass(), "fromString", String.class), context);
    }

    public static EntityType fromString(String s) {
        return parseMap.get(s);
    }

    @Override
    public EntityType parse(String s) {
        return parseMap.get(s);
    }
}
