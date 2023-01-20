package dhondt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SeatCalculatorTest {

    @ParameterizedTest
    @NullAndEmptySource
    void parties_must_not_be_empty(Set<String> parties) {
        assertThatThrownBy(() -> new SeatCalculator(parties));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void seats_must_be_positive(int seats) {
        var seatCalculator = new SeatCalculator(Set.of("party"));

        assertThatThrownBy(() -> seatCalculator.calculate(Map.of("party", 1L), seats));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void votes_must_not_be_empty(Map<String, Long> votes) {
        var seatCalculator = new SeatCalculator(Set.of("party"));

        assertThatThrownBy(() -> seatCalculator.calculate(votes, 1));
    }

    @Test
    void at_least_one_valid_vote_required() {
        var seatCalculator = new SeatCalculator(Set.of("party"));

        assertThatThrownBy(() -> seatCalculator.calculate(Map.of("not registered party", 1L), 1));
    }

    @Test
    void only_one_party() {
        var seatCalculator = new SeatCalculator(Set.of("party"));
        var expected = Map.of("party", 2);

        assertThat(seatCalculator.calculate(Map.of(
                "party", 1L,
                "invalid", 1L),
                2))
                .containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    void less_parties_than_seats() {
        var seatCalculator = new SeatCalculator(Set.of("CDU", "SPD", "FDP", "Grüne", "SSW"));
        var expected = Map.of(
                "CDU", 30,
                "SPD", 29,
                "FDP", 4,
                "Grüne", 4,
                "SSW", 2);

        assertThat(seatCalculator.calculate(Map.of(
                "CDU", 576_100L,
                "FDP", 94_920L,
                "SPD", 554_844L,
                "Grüne", 89_330L,
                "SSW", 51_901L),
                69))
                .containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    void less_parties_than_seats2() {
        var seatCalculator = new SeatCalculator(Set.of("A", "B"));
        var expected = Map.of(
                "A", 3,
                "B", 2);

        assertThat(seatCalculator.calculate(Map.of(
                "A", 4L,
                "B", 3L),
                5))
                .containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    void morePartiesThanSeats() {
        var seatCalculator = new SeatCalculator(Set.of("A", "B", "C"));
        var expected = Map.of(
                "A", 1,
                "B", 1);

        assertThat(seatCalculator.calculate(Map.of(
                "A", 4L,
                "B", 3L,
                "C", 2L),
                2))
                .containsExactlyInAnyOrderEntriesOf(expected);
    }

    @Test
    void seats_correctly_assigned_considers_fractions() {
        var seatCalculator = new SeatCalculator(Set.of("A", "B"));
        var expected = Map.of(
                "A", 3,
                "B", 2);

        assertThat(seatCalculator.calculate(Map.of(
                "A", 4L,
                "B", 3L),
                5))
                .containsExactlyInAnyOrderEntriesOf(expected);
    }
}
