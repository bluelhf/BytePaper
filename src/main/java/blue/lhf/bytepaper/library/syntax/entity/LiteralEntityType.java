package blue.lhf.bytepaper.library.syntax.entity;

import mx.kenzie.foundation.*;
import org.bukkit.entity.EntityType;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.syntax.Literal;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.lang.element.StandardElements;

import java.util.*;

import static mx.kenzie.foundation.WriteInstruction.getStaticField;

@Documentation(
    name = "Entity Type",
    description = "Any minecraft entity type, such as 'zombie' or 'minecraft:strider'.",
    examples = {
        """
        command /list:
          trigger:
            
        """
    }
)
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
        super(library, StandardElements.EXPRESSION, "any minecraft entity id");
    }

    @Override
    public Type getReturnType() {
        return new Type(EntityType.class);
    }

    public void compile(Context context, Pattern.Match match) {
        String string = match.matcher().group();

        assert string.length() > 1;

        MethodBuilder method = context.getMethod();
        EntityType type = parse(string);
        method.writeCode(getStaticField(new Type(type.getDeclaringClass()), new FieldErasure(type.getDeclaringClass(), type.name())));
    }


    @Override
    public EntityType parse(String s) {
        return parseMap.get(s);
    }

    public static EntityType fromString(final String s) {
        return parseMap.get(s);
    }
}
