package blue.lhf.bytepaper.library.syntax.chat;

import blue.lhf.bytepaper.library.syntax.SyntaxUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.syntax.Effect;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.lang.element.StandardElements;

import static mx.kenzie.foundation.WriteInstruction.invokeInterface;
import static mx.kenzie.foundation.WriteInstruction.swap;

@Documentation(
        name = "Send",
        description = "Sends the given message component to the given audience.\n",
        examples = {
                """
                on script load:
                    trigger:
                        send mini "<rainbow>Some rainbow text!</rainbow>" to console""",
                """
                on first join:
                    trigger:
                        send mini ("<gradient:red:aqua>" + event-player's name + "just joined the server for the first time!</gradient>") to all players
                """
        }
)
public class SendEffect extends Effect {
    public SendEffect(Library library) {
        super(library, StandardElements.EFFECT, "send [message] %Object% to %Object%");
    }

    @Override
    public void compile(Context context, Pattern.Match match) {
        context.getMethod().writeCode(SyntaxUtils.convert(Audience.class));
        context.getMethod().writeCode(swap(), SyntaxUtils.convert(Component.class),
            invokeInterface(findMethod(Audience.class, "sendMessage", Component.class)));
        context.setState(CompileState.CODE_BODY);
    }
}
