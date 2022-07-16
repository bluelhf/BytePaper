package blue.lhf.bytepaper.commands.arguments;

/**
 * Internally, ByteSkript expects scripts to be in the skript package (for some reason)
 * this converts between the two formats
 * */
@SuppressWarnings("unused")
public class ArgUtils {
    private static final String INTERNAL_PREFIX = "skript.";
    private ArgUtils() {
    }

    public static boolean isValidFileScript(String input) {
        return input.endsWith(".bsk");
    }

    public static String toFileScript(String input) {
        return input + (input.endsWith(".bsk") ? "" : ".bsk");
    }

    public static String toInternalScript(String input) {
        return toInternalScript(input, true);
    }

    public static String toInternalScript(String input, boolean file) {
        final String withoutExtension = input.endsWith(".bsk") ?
            input.substring(0, input.length() - ".bsk".length()) : input;
        return INTERNAL_PREFIX + (file ? toFileScript(input) : withoutExtension);
    }

    public static String toExternalScript(String input) {
        return toExternalScript(input, true);
    }

    public static String toExternalScript(String input, boolean file) {
        final String base = input.substring((input.indexOf(INTERNAL_PREFIX) + 1) * INTERNAL_PREFIX.length());
        return file ? toFileScript(base) : base;
    }
}
