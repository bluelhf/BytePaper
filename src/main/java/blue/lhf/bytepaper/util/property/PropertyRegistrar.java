package blue.lhf.bytepaper.util.property;

import mx.kenzie.foundation.Type;
import org.byteskript.skript.api.ModifiableLibrary;
import org.byteskript.skript.api.PropertyHandler;
import org.byteskript.skript.lang.handler.StandardHandlers;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;

public class PropertyRegistrar {
    private PropertyRegistrar() {
    }

    public static void register(ModifiableLibrary provider, Class<?>... classes) {
        for (Class<?> target : classes) {
            if (!target.isAnnotationPresent(PropertyHolder.class)) {
                throw new IllegalArgumentException("Cannot register properties for non-PropertyHolder " + target.getCanonicalName());
            }

            PropertyHolder holder = target.getAnnotation(PropertyHolder.class);
            Class<?> owner = holder.owner();

            for (Class<?> subclass : target.getDeclaredClasses()) {
                if (!subclass.isAnnotationPresent(Property.class)) {
                    continue;
                }

                Property property = subclass.getAnnotation(Property.class);
                String name = property.name();
                Class<?> type = property.type();

                for (Method method : subclass.getMethods()) {
                    if (!method.isAnnotationPresent(PropertyActor.class)) {
                        continue;
                    }

                    if (!Modifier.isStatic(method.getModifiers())) {
                        throw new IllegalArgumentException(
                                "Cannot register dynamic property handler " + method.toGenericString());
                    }

                    PropertyActor handler = method.getAnnotation(PropertyActor.class);
                    register(provider, name, owner, type, handler.value(), method);
                }
            }

            for (Method method : owner.getMethods()) {
                StandardHandlers mode = null;
                for (StandardHandlers maybe : StandardHandlers.values()) {
                    if (!method.getName().startsWith(maybe.name().toLowerCase(Locale.ROOT)))
                        continue;
                    mode = maybe;
                    break;
                }

                if (mode == null)
                    continue;

                if (method.getParameterCount() == 0 && mode.expectInputs())
                    continue;

                Class<?> type = mode.expectReturn() ? method.getReturnType() : method.getParameterTypes()[0];

                String name = friendlyName(method.getName().substring(mode.name().length()));

                try {
                    register(provider, name, owner, type, mode, method);
                } catch (IllegalArgumentException e) {
                    if (Boolean.getBoolean("debug_mode"))
                        e.printStackTrace();
                }
            }
        }
    }

    protected static String friendlyName(String sub) {
        StringBuilder nameBuilder = new StringBuilder();
        boolean first = true;
        for (char c : sub.toCharArray()) {
            if (Character.isUpperCase(c) && !first) {
                nameBuilder.append(" ");
            }

            first = false;
            nameBuilder.append(Character.toLowerCase(c));
        }
        return nameBuilder.toString();
    }

    protected static void register(ModifiableLibrary provider, String name, Class<?> owner, Class<?> type, StandardHandlers mode, Method method) {
        boolean isStatic = Modifier.isStatic(method.getModifiers());
        Class<?>[] expected = new Class<?>[(isStatic ? 1 : 0) + (mode.expectInputs() ? 1 : 0)];
        if (isStatic) expected[0] = owner;
        if (mode.expectInputs()) expected[(isStatic ? 1 : 0)] = type;

        if (!Arrays.equals(method.getParameterTypes(), expected)) {
            throw new IllegalArgumentException(
                    "Expected parameter types %s for %s handler %s, but got %s".formatted(
                            Arrays.toString(expected), mode, method.toGenericString(),
                            Arrays.toString(method.getParameterTypes())
                    ));
        }

        Class<?> expectedReturn = mode.expectReturn() ? type : void.class;
        if (method.getReturnType() != expectedReturn) {
            throw new IllegalArgumentException(
                    "Expected return type %s for %s handler %s, but got %s".formatted(
                            expectedReturn.getCanonicalName(),
                            mode, method.toGenericString(),
                            method.getReturnType().getCanonicalName()
                    ));
        }

        provider.registerProperty(new PropertyHandler(name, mode, new Type(owner), new Type(method.getReturnType()), method));
    }
}
