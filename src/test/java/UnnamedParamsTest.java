import org.junit.jupiter.api.Test;

class UnnamedParamsTest {
    @Test
    void firstPositiveOrZero() {
        System.out.println(UnnamedParams.firstPositiveOrZero(new UnnamedParams.Point(5, -3)));
        System.out.println(UnnamedParams.firstPositiveOrZero(new UnnamedParams.Point(-5, 3)));
        System.out.println(UnnamedParams.firstPositiveOrZero(new UnnamedParams.Point(-5, -3)));
    }

    @Test
    void div() {
        System.out.println(UnnamedParams.div(6, 3));
        System.out.println(UnnamedParams.div(5, 0));
    }
}