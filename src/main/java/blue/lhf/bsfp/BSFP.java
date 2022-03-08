package blue.lhf.bsfp;

import blue.lhf.bsfp.library.PaperBridgeSpec;
import org.bukkit.plugin.java.JavaPlugin;
import org.byteskript.skript.runtime.Skript;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public final class BSFP extends JavaPlugin {

    private Skript skript;

    @Override
    public void onEnable() {
        { // Skript registration
            this.skript = new Skript();
            skript.registerLibrary(PaperBridgeSpec.LIBRARY);
        }

        { // Script loading
            try {
                skript.compileLoadScripts(new File(getDataFolder(), "scripts"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Skript getSkript() {
        return skript;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Arrays.stream(skript.getScripts()).forEachOrdered((s) -> {
            try {
                skript.unloadScript(s);
            } catch (UnsupportedOperationException ex) {
                // fail due to kenzie's bad record graveyarding
            }
        });
        skript.unregisterLibrary(PaperBridgeSpec.LIBRARY);
    }
}
