package blue.lhf.bsfp.util;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.chat.Component;

public class Components {
    public static Component toMC(net.kyori.adventure.text.Component component) {
        return Component.Serializer.fromJson(GsonComponentSerializer.gson().serializeToTree(component));
    }
}
