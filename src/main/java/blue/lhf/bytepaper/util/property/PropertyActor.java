package blue.lhf.bytepaper.util.property;

import org.byteskript.skript.lang.handler.StandardHandlers;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface PropertyActor {
    StandardHandlers value();
}
