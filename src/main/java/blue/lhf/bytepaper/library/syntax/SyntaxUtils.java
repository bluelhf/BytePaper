package blue.lhf.bytepaper.library.syntax;

import mx.kenzie.foundation.*;
import org.objectweb.asm.Opcodes;

import static mx.kenzie.foundation.WriteInstruction.duplicate;
import static mx.kenzie.foundation.WriteInstruction.invoke;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class SyntaxUtils {
    private SyntaxUtils() {
    }

    public static WriteInstruction convert(Type type) {
        return (writer, visitor) -> {
            visitor.visitLdcInsn(org.objectweb.asm.Type.getType(type.descriptor()));
            visitor.visitMethodInsn(INVOKESTATIC,
                    "org/byteskript/skript/runtime/Skript",
                    "convert",
                    "(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;",
                    false);
            visitor.visitTypeInsn(Opcodes.CHECKCAST, type.internalName());
        };
    }

    public static WriteInstruction box(Class<?> clazz) throws NoSuchMethodException {
        if (!clazz.isPrimitive()) return (writer, visitor) -> {};
        return switch (clazz.getSimpleName()) {
            case "boolean" -> invoke(Boolean.class.getDeclaredMethod("valueOf", boolean.class));
            case "byte" -> invoke(Byte.class.getDeclaredMethod("valueOf", byte.class));
            case "char" -> invoke(Character.class.getDeclaredMethod("valueOf", char.class));
            case "short" -> invoke(Short.class.getDeclaredMethod("valueOf", short.class));
            case "int" -> invoke(Integer.class.getDeclaredMethod("valueOf", int.class));
            case "float" -> invoke(Float.class.getDeclaredMethod("valueOf", float.class));
            case "long" -> invoke(Long.class.getDeclaredMethod("valueOf", long.class));
            case "double" -> invoke(Double.class.getDeclaredMethod("valueOf", double.class));
            default -> (writer, visitor) -> {};
        };
    }

    public static <T> T forceBox(T t) { // compiler will add the conversion at the call site
        return t;
    }

    public static WriteInstruction convert(Class<?> classy) {
        return convert(new Type(classy));
    }
}
