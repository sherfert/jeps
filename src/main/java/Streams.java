import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

///  JEP 485: Stream Gatherers (JDK 24)
///
/// Define your own Gatherers:
///
/// A `Gatherer<INPUT, STATE, OUTPUT>` can be defined with 4 functions:
/// 1) Initializer: `() -> STATE`
///    Sets initial mutable state
/// 2) Integrator: `(STATE, INPUT,(RESULT) -> Boolean) -> Boolean`
///    invoked on each incoming element. May send elements downstream and may mutate state.
///    Returns whether more elements should be sent from upstream.
/// 3) Combiner: `(STATE, STATE) -> STATE`
///    Only needed if Gatherer should be able to run in parallel.
///    Combines two states.
/// 4) Finisher: `(STATE,(RESULT) -> Boolean) -> ()`
///    Runs after all input elements are consumed. May sent more elements downstream.
///
/// (^^^ JEP 467: Markdown Documentation Comments (JDK 23))
public class Streams {

    // Built-in Gatherers:
    public static Stream<List<Integer>> windowFixed() {
        return Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                .gather(Gatherers.windowFixed(3));
    }

    public static Stream<List<Integer>> windowSliding() {
        return Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                .gather(Gatherers.windowSliding(3));
    }

    public static Stream<String> fold() {
        return Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                .gather(Gatherers.fold(() -> "", (string, number) -> string + number));
    }

    public static Stream<String> scan() {
        return Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
                .gather(Gatherers.scan(() -> "", (string, number) -> string + number));
    }


    // Ad hoc:
    public record Pair<A, B>(A a, B b) {
    }

    public static <T> Gatherer<T, ?, Pair<Integer, T>> zipWithIndex() {
        return Gatherer.ofSequential(
                /*initializer:*/ () -> new AtomicInteger(0),
                /*integrator:*/ Gatherer.Integrator.ofGreedy(
                        // Greedy is a marker interface signalling that this method never initiates short-circuiting
                        // of its own.
                        (state, elem, downstream) -> {
                            final var index = state.getAndIncrement();
                            return downstream.push(new Pair<>(index, elem));
                        })
        );
    }

    // Implementing the interface:
    public record Reverse<T>() implements Gatherer<T, ArrayDeque<T>, T> {

        @Override
        public Supplier<ArrayDeque<T>> initializer() {
            return ArrayDeque::new;
        }

        @Override
        public Integrator<ArrayDeque<T>, T, T> integrator() {
            return Gatherer.Integrator.ofGreedy(
                    (state, elem, downstream) -> {
                        state.push(elem);
                        return true;
                    }
            );
        }

        @Override
        public BiConsumer<ArrayDeque<T>, Downstream<? super T>> finisher() {
            return (state, downstream) -> {
                state.forEach(downstream::push);
            };
        }
    }

    // Oh no, mutable state!
    private static class StateHolder<A> {
        A state;

        StateHolder(A state) {
            this.state = state;
        }
    }

    static <T, A, R> Gatherer<T, StateHolder<A>, R> ofFunctional(
            Supplier<A> initializer,
            BiFunction<A, T, A> stateMapper,
            Gatherer.Integrator<A, T, R> integrator,
            BinaryOperator<A> combiner,
            BiConsumer<A, Gatherer.Downstream<? super R>> finisher
    ) {
        return Gatherer.of(
                () -> new StateHolder<>(initializer.get()),
                (sH, elem, downstream) -> {
                    sH.state = stateMapper.apply(sH.state, elem);
                    return integrator.integrate(sH.state, elem, downstream);
                },
                combiner == Gatherer.defaultCombiner() ? Gatherer.defaultCombiner() :
                        (sHA, sHB) -> {
                            sHA.state = combiner.apply(sHA.state, sHB.state);
                            return sHA;
                        },
                (sH, downStream) -> finisher.accept(sH.state, downStream)
        );
    }

    // Re-implementing zipWithIndex purely functional:
    public static <T> Gatherer<T, ?, Pair<Integer, T>> zipWithIndex0() {
        return ofFunctional(
                () -> 0,
                (state, _) -> state + 1,
                (state, elem, downstream) -> downstream.push(new Pair<>(state, elem)),
                Gatherer.defaultCombiner(),
                Gatherer.defaultFinisher()
        );
    }


}
