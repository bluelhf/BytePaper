package blue.lhf.bytepaper.library.syntax.chat;

import blue.lhf.bytepaper.library.syntax.SyntaxUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.syntax.Effect;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.lang.element.StandardElements;

import java.util.*;

import static mx.kenzie.foundation.WriteInstruction.*;

@Documentation(
    name = "Broadcast",
    description = "Sends the given message component to the entire server, that is to say, all players and console.",
    examples = {
        """
        on skript load:
            trigger:
                broadcast mini "<rainbow>BytePaper is up and running!</rainbow>"
        """
    }
)
public class EffectBroadcast extends Effect {
    public EffectBroadcast(Library library) {
        super(library, StandardElements.EFFECT, "broadcast [message] %Object%");
    }

    @Override
    public void compile(Context context, Pattern.Match match) {
        context.getMethod().writeCode(
                SyntaxUtils.convert(Component.class),
                invokeStatic(findMethod(EffectBroadcast.class, "everyone")),
                swap(),
                invokeInterface(findMethod(Audience.class, "sendMessage", Component.class))
        );
        context.setState(CompileState.CODE_BODY);
    }

    public static Audience everyone() {
        List<Audience> audiences = new ArrayList<>();
        audiences.add(Bukkit.getConsoleSender());
        audiences.addAll(Bukkit.getWorlds());
        return Audience.audience(audiences);
    }
}
