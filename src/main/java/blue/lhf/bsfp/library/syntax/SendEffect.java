package blue.lhf.bsfp.library.syntax;

import blue.lhf.bsfp.library.PaperBridgeSpec;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.byteskript.skript.api.syntax.Effect;
import org.byteskript.skript.compiler.Context;
import org.byteskript.skript.compiler.Pattern;
import org.byteskript.skript.lang.element.StandardElements;

public class SendEffect extends Effect {
    public SendEffect() {
        super(PaperBridgeSpec.LIBRARY, StandardElements.EFFECT, "send [message] %Component% to %Audience%");
    }

    @Override
    public void compile(Context context, Pattern.Match match) {
        writeCall(context.getMethod(), findMethod(Audience.class, "sendMessage", Component.class), context);
    }
}
