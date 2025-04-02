import org.junit.jupiter.api.Test;

class SwitchTest {

    @Test
    void testSwitchPrimitive() {
        System.out.println(Switch.switchPrimitive(100));
    }

    @Test
    void testDoubleUp() {
        System.out.println(Switch.switchAndCast(new Switch.DoubleUp(5.4)));
        System.out.println(Switch.switchAndCast(new Switch.DoubleUp(5.0)));
    }

    @Test
    void testIntHolder() {
        System.out.println(Switch.switchAndCast(new Switch.IntHolder(1000)));
        System.out.println(Switch.switchAndCast(new Switch.IntHolder(42)));
    }
}