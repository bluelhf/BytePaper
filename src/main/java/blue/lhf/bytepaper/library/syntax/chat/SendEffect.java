package blue.lhf.bytepaper.library.syntax.chat;

import blue.lhf.bytepaper.library.PaperBridgeSpec;
import blue.lhf.bytepaper.library.syntax.SyntaxUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.byteskript.skript.api.syntax.Effect;
import org.byteskript.skript.compiler.CompileState;
import org.byteskript.skript.compiler.Context;
import org.byteskript.skript.compiler.Pattern;
import org.byteskript.skript.lang.element.StandardElements;

import static mx.kenzie.foundation.WriteInstruction.invokeInterface;
import static mx.kenzie.foundation.WriteInstruction.swap;

public class SendEffect extends Effect {
    public SendEffect() {
        super(PaperBridgeSpec.LIBRARY, StandardElements.EFFECT, "send [message] %Object% to %Object%");
    }

    @Override
    public void compile(Context context, Pattern.Match match) {
        context.getMethod().writeCode(SyntaxUtils.convert(Audience.class));
        context.getMethod().writeCode(swap(), SyntaxUtils.convert(Component.class),
            invokeInterface(findMethod(Audience.class, "sendMessage", Component.class)));
        context.setState(CompileState.CODE_BODY);
    }
}
