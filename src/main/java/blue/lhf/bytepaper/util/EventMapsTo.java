package blue.lhf.bytepaper.util;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventMapsTo {
    Class<? extends org.bukkit.event.Event> value();
}
