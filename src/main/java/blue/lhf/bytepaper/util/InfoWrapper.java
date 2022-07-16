package blue.lhf.bytepaper.util;

import org.jetbrains.annotations.NotNull;

import java.util.logging.*;

/**
 * An evil wrapper that logs at INFO and then goes back and replaces the level in the message
 * */
public class InfoWrapper extends Logger {
    private final Logger wrap;

    public InfoWrapper(Logger wrap) {
        super(wrap.getName(), wrap.getResourceBundleName());
        this.wrap = wrap;
    }

    @Override
    public void log(@NotNull LogRecord logRecord) {
        var old = logRecord.getLevel();
        logRecord.setLevel(Level.INFO);
        // >:)
        logRecord.setLoggerName("\b\b\b\b\b\b\b\b" + old + "]: [" + getName());
        wrap.log(logRecord);
        logRecord.setLevel(old);
    }
}
