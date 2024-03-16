package blue.lhf.bytepaper.api;

import org.byteskript.skript.api.ModifiableLibrary;
import org.byteskript.skript.runtime.Skript;

/**
 * Represents a library that can be registered with the ByteSkript instance BytePaper uses.
 * */
public abstract class BytePaperLibrary extends ModifiableLibrary {
    protected BytePaperLibrary(String name) {
        super(name);
    }

    /**
     * Initialises the library. This should call the relevant methods of {@link ModifiableLibrary} in order to
     * register any syntaxes the library provides.
     * */
    public abstract void initialise(final Skript skript);
}
