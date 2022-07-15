package blue.lhf.bytepaper.library.syntax.world;

import mx.kenzie.foundation.Type;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.Context;
import org.byteskript.skript.compiler.Pattern;
import org.byteskript.skript.lang.element.StandardElements;

import java.util.UUID;

import static mx.kenzie.foundation.WriteInstruction.invoke;

public class ExprWorld extends SimpleExpression {
    public ExprWorld(Library provider) {
        super(provider, StandardElements.EXPRESSION, "world %Object%");
    }

    @Override
    public Type getReturnType() {
        return new Type(World.class);
    }

    @Override
    public void compile(Context context, Pattern.Match match) throws Throwable {
        context.getMethod().writeCode(
                invoke(ExprWorld.class.getDeclaredMethod("getWorld", Object.class))
        );
    }

    public static World getWorld(Object object) {
        if (object instanceof String string) {
            return Bukkit.getWorld(string);
        } else if (object instanceof NamespacedKey key) {
            return Bukkit.getWorld(key);
        } else if (object instanceof UUID uuid) {
            return Bukkit.getWorld(uuid);
        } else {
            return Bukkit.getWorld(String.valueOf(object));
        }
    }
}
