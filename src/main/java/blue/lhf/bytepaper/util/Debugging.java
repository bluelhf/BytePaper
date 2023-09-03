package blue.lhf.bytepaper.util;

import mx.kenzie.foundation.Type;
import mx.kenzie.jupiter.stream.OutputStreamController;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.compiler.DebugSkriptCompiler;
import org.byteskript.skript.compiler.FileContext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

public class Debugging {
    private Debugging() {
    }

    public static class Stream extends OutputStream {
        protected final ByteArrayOutputStream buffer = new ByteArrayOutputStream(32);
        protected final Logger logger;

        public Stream(Logger logger) {
            this.logger = logger;
        }

        @Override
        public void write(byte[] bytes, int off, int len) {
            for (byte b : bytes) write(b);
        }

        @Override
        public void write(int b) {
            if (b == 10) {
                flush();
                return;
            }

            buffer.write(b);
        }

        @Override
        public void flush() {
            logger.info(buffer.toString());
            buffer.reset();
        }
    }

    public static class Compiler extends DebugSkriptCompiler {
        public Compiler(OutputStreamController controller, Library... libraries) {
            super(controller, libraries);
        }

        @Override
        protected FileContext createContext(Type path) {
            return new FileContext(path);
        }
    }


}
