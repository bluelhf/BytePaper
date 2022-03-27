package blue.lhf.bytepaper.util;

import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.network.chat.Component;

import java.awt.*;

import static blue.lhf.bytepaper.util.UI.Colour.wrap;
import static net.kyori.adventure.text.Component.text;

public class UI {
    public static Palette PALETTE = new Palette(
        wrap(new Color(0xc5d0e8)), // use Color constructor for preview
        wrap(new Color(0x838af4)),
        wrap(new Color(0x3e8989)),
        wrap(new Color(0xb80c09))
    );

    public static final MiniMessage MSG = MiniMessage.builder()
        .editTags(builder -> {
            builder.resolver(TagResolver.resolver("info", PALETTE.info().styler()));
            builder.resolver(TagResolver.resolver("primary", PALETTE.primary().styler()));
            builder.resolver(TagResolver.resolver("secondary", PALETTE.secondary().styler()));
            builder.resolver(TagResolver.resolver("error", PALETTE.erroneous().styler()));
        }).build();

    private UI() {
    }

    public static MiniMessage miniMessage() {
        return MSG;
    }

    public static Component toMC(net.kyori.adventure.text.Component component) {
        return Component.Serializer.fromJson(GsonComponentSerializer.gson().serializeToTree(component));
    }

    public record Colour(Tag styler, Color color, TextColor textColor) {
        public Colour(String code) {
            this(Color.decode(code).getRGB());
        }

        public Colour(int colour) {
            this(Tag.styling(b -> b.color(TextColor.color(colour))), new Color(colour), TextColor.color(colour));
        }

        public Colour(Color color) {
            this(color.getRGB());
        }

        public static Colour wrap(Color color) {
            return new Colour(color);
        }

        public int value() {
            return color.getRGB();
        }
    }

    public record Palette(Colour info, Colour primary, Colour secondary, Colour erroneous) {
        public static TextColor adventure(Colour color) {
            return TextColor.color(color.value());
        }

        public net.kyori.adventure.text.Component info(String content) {
            return text(content, info.textColor());
        }

        public net.kyori.adventure.text.Component primary(String content) {
            return text(content, primary.textColor());
        }

        public net.kyori.adventure.text.Component secondary(String content) {
            return text(content, secondary.textColor());
        }

        public net.kyori.adventure.text.Component error(String content) {
            return text(content, erroneous.textColor());
        }
    }
}
