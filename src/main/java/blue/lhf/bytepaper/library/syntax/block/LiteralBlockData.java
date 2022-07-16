package blue.lhf.bytepaper.library.syntax.block;

import blue.lhf.bytepaper.library.syntax.SyntaxUtils;
import mx.kenzie.foundation.MethodBuilder;
import mx.kenzie.foundation.Type;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.syntax.Literal;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.lang.element.StandardElements;

import static mx.kenzie.foundation.WriteInstruction.*;

public class LiteralBlockData extends Literal<BlockData> {
    // private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("^(?<key>[a-zA-Z0-9_]+)?:(?<name>[a-zA-Z0-9_]+)(?<states>\\[([a-zA-Z0-9_]+=[a-zA-Z0-9_]+)?(?:,([a-zA-Z0-9_]+=[a-zA-Z0-9_]+))*\\])?$");
    private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("^(?:[a-zA-Z0-9_]+)?:[a-zA-Z0-9_]+(?:\\[([a-zA-Z0-9_]+=[a-zA-Z0-9_]+)?(?:,([a-zA-Z0-9_]+=[a-zA-Z0-9_]+))*])?$");


    public LiteralBlockData(Library provider) {
        super(provider, StandardElements.EXPRESSION, "block data string");
    }

    @Override
    public Pattern.Match match(String thing, Context context) {
        var matcher = PATTERN.matcher(thing);
        return matcher.find() ? new Pattern.Match(matcher) : null;
    }

    @Override
    public void compile(Context context, Pattern.Match match) {
        String string = match.matcher().group();

        assert string.length() > 1;

        MethodBuilder method = context.getMethod();
        method.writeCode(SyntaxUtils.convert(String.class));
        method.writeCode(invokeStatic(findMethod(Bukkit.class, "createBlockData", String.class)));
    }

    @Override
    public BlockData parse(String s) {
        return Bukkit.createBlockData(s);
    }

    @Override
    public Type getReturnType() {
        return new Type(BlockData.class);
    }
}
