package blue.lhf.bytepaper.library.syntax.entity;

import blue.lhf.bytepaper.library.PaperBridgeSpec;
import mx.kenzie.foundation.Type;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.lang.element.StandardElements;
import org.byteskript.skript.lang.handler.StandardHandlers;

public class EntitiesExpression extends SimpleExpression {
    public EntitiesExpression(Library library) {
        // The pattern order is important, because types compile to <.+>, which breaks optionals that follow
        super(library, StandardElements.EXPRESSION, "all %EntityType%s", "all %EntityType%");
        setHandler(StandardHandlers.GET, findMethod(getClass(), "getEntities", EntityType.class));
    }

    @Override
    public Type getReturnType() {
        return new Type(Entity[].class);
    }

    @Override
    public boolean requiresMainThread() {
        return true;
    }

    public static Entity[] getEntities(EntityType type) {
        Class<? extends Entity> typeClass = type.getEntityClass();

        /*
        * As of 2022-03-15T12:19+2, this never happens, but it's
        * technically possible, so let's keep it in either way.
        * */
        if (typeClass == null) return new Entity[0];

        return Bukkit.getWorlds().stream().sequential()
                .flatMap(w -> w.getEntitiesByClass(typeClass).stream())
                .toArray(Entity[]::new);
    }
}
