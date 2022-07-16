package blue.lhf.bytepaper.util;

import java.util.*;

import static java.util.Collections.singleton;

@SuppressWarnings("unused")
public class Classes {
    private Classes() {

    }

    public static Set<Class<?>> flattenClassTree(Class<?> clazz) {
        return flattenClassTrees(singleton(clazz));
    }

    public static Set<Class<?>> flattenClassTrees(Class<?>[] classes) {
        return flattenClassTrees(List.of(classes));
    }

    public static Set<Class<?>> flattenClassTrees(Collection<Class<?>> clazz) {
        final Set<Class<?>> set = new HashSet<>(clazz);
        final Deque<Class<?>> processDeque = new ArrayDeque<>(clazz);
        while (!processDeque.isEmpty()) {
            Class<?> target = processDeque.pop();
            processDeque.addAll(List.of(target.getDeclaredClasses()));
            set.add(target);
        }

        return set;
    }
}
