package blue.lhf.bytepaper;

import mx.kenzie.foundation.Type;
import mx.kenzie.foundation.language.PostCompileClass;
import mx.kenzie.jupiter.stream.OutputStreamController;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.compiler.*;

import java.io.*;

public class TracingDebugSkriptCompiler extends DebugSkriptCompiler {
    private final OutputStreamController controller;

    public TracingDebugSkriptCompiler(OutputStreamController controller, Library... libraries) {
        super(controller, libraries);
        this.controller = controller;
    }

    @Override
    protected FileContext createContext(Type path) {
        return new FileContext(path);
    }

    @Override
    public PostCompileClass[] compile(InputStream stream, Type path) {
        try {
            this.controller.write("\n\n");
            this.controller.write("--" + path.internalName());
            this.controller.write("\n");
        } catch (IOException ignored) {
        }

        return super.compile(stream, path);
    }

    @Override
    public PostCompileClass[] compile(String source, Type path) {
        try {
            this.controller.write("\n\n");
            this.controller.write("--" + path.internalName());
            this.controller.write("\n");
        } catch (IOException ignored) {
        }

        return super.compile(source, path);
    }


    @Override
    protected void debug(ElementTree tree, FileContext context) {
        super.debug(tree, context);
        try {
            controller.flush();
        } catch (IOException ignored) {
        }
    }
}
