package blue.lhf.bytepaper.library.syntax.entity;

import blue.lhf.bytepaper.util.Threading;
import io.papermc.paper.util.MCUtil;
import mx.kenzie.foundation.Type;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.lang.element.StandardElements;
import org.byteskript.skript.lang.handler.StandardHandlers;
import org.byteskript.skript.runtime.Skript;

public class ExprEntities extends SimpleExpression {
    public ExprEntities(Library library) {
        // The pattern order is important, because types compile to <.+>, which breaks optionals that follow
        super(library, StandardElements.EXPRESSION, "all %Object%s", "all %Object%");
        setHandler(StandardHandlers.GET, findMethod(getClass(), "getEntities", Object.class));
    }

    @Override
    public Type getReturnType() {
        return new Type(Entity[].class);
    }

    @SuppressWarnings("unused")
    public static Entity[] getEntities(Object type0) {
        if (type0 == null) return new Entity[0];
        final EntityType type = Skript.convert(type0, org.bukkit.entity.EntityType.class);
        Class<? extends Entity> typeClass = type.getEntityClass();

        /*
        * As of 2022-03-15T12:19+2, this never happens, but it's
        * technically possible, so let's keep it in either way.
        * */
        if (typeClass == null) return new Entity[0];

        // ByteSkript makes no guarantees regarding what thread handlers are called from. We must block here.
        if (!MCUtil.isMainThread())
            return Threading.forceMain(() -> getEntities(type));

        return Bukkit.getWorlds().stream()
                .flatMap(w -> w.getEntitiesByClass(typeClass).stream())
                .toArray(Entity[]::new);
    }
}