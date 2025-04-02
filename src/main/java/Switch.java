/// JEP 488: Primitive Types in Patterns, instanceof, and switch (Second Preview) (JDK 24)
public class Switch {

    public static String switchPrimitive(int x) {
        return switch (x) {
            case 0 -> "zero";
            case 1 -> "one";
            case 2 -> "two";
            case int i when i >= 100 -> "big";
            default -> "default";
        };
    }

    public record DoubleUp(double d) {}
    public record IntHolder(int i) {}

    public static String switchAndCast(DoubleUp du) {
        return switch (du) {
            case DoubleUp(int i) -> "int:" + i;
            case DoubleUp(double d) -> "not int:" + d;
        };
    }

    public static String switchAndCast(IntHolder ih) {
        return switch (ih) {
            case IntHolder(byte b) -> "byte:" + b;
            //case IntHolder(42) -> "magic number:" + i; // <-- Future work
            case IntHolder(int i) -> "not byte:" + i;
        };
    }

    // Same things work with `instanceof`

}
