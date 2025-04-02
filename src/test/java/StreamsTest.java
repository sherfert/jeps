import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

class StreamsTest {

    @Test
    void windowFixed() {
        System.out.println(Streams.windowFixed().toList());
    }

    @Test
    void windowSliding() {
        System.out.println(Streams.windowSliding().toList());
    }

    @Test
    void fold() {
        System.out.println(Streams.fold().toList());
    }

    @Test
    void scan() {
        System.out.println(Streams.scan().toList());
    }

    @Test
    void zipWithIndex() {
        System.out.println(
                Stream.of("a", "b", "c")
                        .gather(Streams.zipWithIndex())
                        .toList()
        );
    }

    @Test
    void zipWithIndex0() {
        System.out.println(
                Stream.of("a", "b", "c")
                        .gather(Streams.zipWithIndex0())
                        .toList()
        );
    }

    @Test
    void reverse() {
        System.out.println(
                Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                        .gather(new Streams.Reverse<>())
                        .toList()
        );
    }

}