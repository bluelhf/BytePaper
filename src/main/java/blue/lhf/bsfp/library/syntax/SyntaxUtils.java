package blue.lhf.bsfp.library.syntax;

import mx.kenzie.foundation.Type;
import mx.kenzie.foundation.WriteInstruction;
import org.objectweb.asm.Opcodes;

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

    public static WriteInstruction convert(Class<?> classy) {
        return convert(Type.of(classy)[0]);
    }
}
