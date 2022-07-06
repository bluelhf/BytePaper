package blue.lhf.bytepaper.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.awt.*;
import java.util.function.*;

import static blue.lhf.bytepaper.util.UI.Colour.wrap;
import static net.kyori.adventure.text.Component.*;

public class UI {
    public static final Palette PALETTE = new Palette(
        wrap(new Color(0xF8F0FB)), // use Color constructor for preview
        wrap(new Color(0xD2883E)),
        wrap(new Color(0xBABAC7)),
        wrap(new Color(0xFF674D))
    );

    public static final MiniMessage MSG;
    public static final MiniMessage RAW;
    private static final Component PREFIX;
    private static final String STRING_PREFIX =
        "<secondary>[</secondary><primary><bold>BytePaper</bold></primary><secondary>]</secondary> <reset>";

    static {
        Consumer<TagResolver.Builder> adder = builder -> {
            builder.resolver(TagResolver.resolver("info", PALETTE.info().styler()));
            builder.resolver(TagResolver.resolver("primary", PALETTE.primary().styler()));
            builder.resolver(TagResolver.resolver("secondary", PALETTE.secondary().styler()));
            builder.resolver(TagResolver.resolver("error", PALETTE.erroneous().styler()));
        };


        RAW = MiniMessage.builder().editTags(adder).build();
        PREFIX = RAW.deserialize(STRING_PREFIX);


        UnaryOperator<Component> prefix = data ->
            empty()
                .append(PREFIX)
                .append(data.contains(newline())
                    ? newline()
                    : empty())
                .append(data);

        MSG = MiniMessage.builder().editTags(adder)
            .postProcessor(prefix)
            .build();
    }

    private UI() {
    }

    public static MiniMessage miniMessage(boolean prefix) {
        if (prefix) return MSG;
        return RAW;
    }

    public static MiniMessage miniMessage() {
        return MSG;
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

        public Component info(String content) {
            return text(content, info.textColor());
        }

        public Component primary(String content) {
            return text(content, primary.textColor());
        }

        public Component secondary(String content) {
            return text(content, secondary.textColor());
        }

        public Component error(String content) {
            return text(content, erroneous.textColor());
        }
    }
}
