///  JEP 492: Flexible Constructor Bodies (Third Preview) (JDK 24)
public class Cons {

    // Example 1: Constructor argument checks
    public static class IntSuper {
        public IntSuper(int x) {
        }
    }

    public static class PositiveInt0 extends IntSuper {
        // Constructor with unnecessary super call
        public PositiveInt0(int value) {
            super(value); // Potentially unnecessary work
            if (value <= 0) throw new IllegalArgumentException();
        }
    }

    public static class PositiveInt1 extends IntSuper {
        // Move computation into own method
        private static int verifyPositive(int value) {
            if (value <= 0) throw new IllegalArgumentException();
            return value;
        }

        public PositiveInt1(int value) {
            super(verifyPositive(value));
        }
    }

    public static class PositiveInt2 extends IntSuper {
        public PositiveInt2(int value) {
            // Check before super call. Hurray!
            if (value <= 0) throw new IllegalArgumentException();
            super(value);
        }
    }

    // Example 2: assign field before super()
    public abstract static class PrintSuper {
        public PrintSuper() {
            doStuff();
        }

        abstract void doStuff();
    }

    public static class Overrider0 extends PrintSuper {
        final int x;

        public Overrider0() {
            // Implicit super():  Prints 0
            x = 42;
        }

        @Override
        void doStuff() {
            System.out.println(x);
        }
    }

    public static class Overrider1 extends PrintSuper {
        final int x;

        public Overrider1() {
            x = 42;
            super(); // Prints 42
        }

        @Override
        void doStuff() {
            System.out.println(x);
        }
    }
}
