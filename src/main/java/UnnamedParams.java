/// JEP 456: Unnamed Variables & Patterns (JDK 22)
public class UnnamedParams {

    public record Point(int x, int y) {
    }

    public static int firstPositiveOrZero(Point p) {
        return switch (p) {
            case Point(int x, _) when x > 0 -> x;
            case Point(_, int y) when y > 0 -> y;
            default -> 0;
        };
    }

    public static int div(int x, int y) {
        try {
            return x / y;
        } catch (ArithmeticException _) {
            return 0;
        }
    }

}