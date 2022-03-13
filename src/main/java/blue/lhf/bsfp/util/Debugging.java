package blue.lhf.bsfp.util;

import java.io.OutputStream;
import java.util.logging.Logger;

public enum Debugging {
    OFF,
    TRACE_ONLY,
    COMPILER_ONLY,
    BOTH;

    public boolean compiler() {
        return this == COMPILER_ONLY || this == BOTH;
    }


    public boolean trace() {
        return this == TRACE_ONLY || this == BOTH;
    }

    public static class Stream extends OutputStream {
        protected final StringBuilder buffer = new StringBuilder(32);
        protected final Logger logger;

        public Stream(Logger logger) {
            this.logger = logger;
        }

        public Logger getLogger() {
            return logger;
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

            // for some reason DebugSkriptCompiler
            // writes a null byte between every byte
            if (b > 0) {
                buffer.append((char) b);
            }
        }

        @Override
        public void flush() {
            logger.info(buffer::toString);
            buffer.setLength(0);
        }
    }


}
