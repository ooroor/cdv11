package net.barakiroth.cdv11;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractDateBasedCdv11StringUnitTest {

    private static Stream<Arguments> provideYearsAndCounters() {
        return
                Stream.of(
                        Arguments.of(null, -1, -1),
                        Arguments.of(null, -1, 0),
                        Arguments.of(null, -1, 500),
                        Arguments.of(null, -1, 999),
                        Arguments.of(null, -1, 1000),
                        Arguments.of(null, 100, -1),
                        Arguments.of(null, 100, 0),
                        Arguments.of(null, 100, 500),
                        Arguments.of(null, 100, 999),
                        Arguments.of(null, 100, 1000),
                        Arguments.of(1900, 0, 0),
                        Arguments.of(1900, 0, 250),
                        Arguments.of(1900, 0, 499),
                        Arguments.of(2000, 0, 500),
                        Arguments.of(2000, 0, 750),
                        Arguments.of(2000, 0, 999),
                        Arguments.of(1900, 20, 0),
                        Arguments.of(1900, 20, 250),
                        Arguments.of(1900, 20, 499),
                        Arguments.of(2000, 20, 500),
                        Arguments.of(2000, 20, 750),
                        Arguments.of(2000, 20, 999),
                        Arguments.of(1900, 39, 0),
                        Arguments.of(1900, 39, 250),
                        Arguments.of(1900, 39, 499),
                        Arguments.of(2000, 39, 500),
                        Arguments.of(2000, 39, 750),
                        Arguments.of(2000, 39, 999),
                        Arguments.of(1900, 40, 0),
                        Arguments.of(1900, 40, 250),
                        Arguments.of(1900, 40, 499),
                        Arguments.of(null, 40, 500),
                        Arguments.of(null, 40, 700),
                        Arguments.of(null, 40, 899),
                        Arguments.of(1900, 40, 900),
                        Arguments.of(1900, 40, 950),
                        Arguments.of(1900, 40, 999),
                        Arguments.of(1900, 46, 0),
                        Arguments.of(1900, 46, 250),
                        Arguments.of(1900, 46, 499),
                        Arguments.of(null, 46, 500),
                        Arguments.of(null, 46, 700),
                        Arguments.of(null, 46, 899),
                        Arguments.of(1900, 46, 900),
                        Arguments.of(1900, 46, 950),
                        Arguments.of(1900, 46, 999),
                        Arguments.of(1900, 53, 0),
                        Arguments.of(1900, 53, 250),
                        Arguments.of(1900, 53, 499),
                        Arguments.of(null, 53, 500),
                        Arguments.of(null, 53, 700),
                        Arguments.of(null, 53, 899),
                        Arguments.of(1900, 53, 900),
                        Arguments.of(1900, 53, 950),
                        Arguments.of(1900, 53, 999),
                        Arguments.of(1900, 54, 0),
                        Arguments.of(1900, 54, 250),
                        Arguments.of(1900, 54, 499),
                        Arguments.of(1800, 54, 500),
                        Arguments.of(1800, 54, 625),
                        Arguments.of(1800, 54, 749),
                        Arguments.of(null, 54, 750),
                        Arguments.of(null, 54, 825),
                        Arguments.of(null, 54, 899),
                        Arguments.of(1900, 54, 900),
                        Arguments.of(1900, 54, 950),
                        Arguments.of(1900, 54, 999),
                        Arguments.of(1900, 77, 0),
                        Arguments.of(1900, 77, 250),
                        Arguments.of(1900, 77, 499),
                        Arguments.of(1800, 77, 500),
                        Arguments.of(1800, 77, 625),
                        Arguments.of(1800, 77, 749),
                        Arguments.of(null, 77, 750),
                        Arguments.of(null, 77, 825),
                        Arguments.of(null, 77, 899),
                        Arguments.of(1900, 77, 900),
                        Arguments.of(1900, 77, 950),
                        Arguments.of(1900, 77, 999),
                        Arguments.of(1900, 99, 0),
                        Arguments.of(1900, 99, 250),
                        Arguments.of(1900, 99, 499),
                        Arguments.of(1800, 99, 500),
                        Arguments.of(1800, 99, 625),
                        Arguments.of(1800, 99, 749),
                        Arguments.of(null, 99, 750),
                        Arguments.of(null, 99, 825),
                        Arguments.of(null, 99, 899),
                        Arguments.of(1900, 99, 900),
                        Arguments.of(1900, 99, 950),
                        Arguments.of(1900, 99, 999)
                );
    }

    @ParameterizedTest
    @MethodSource("provideYearsAndCounters")
    void when_calculating_the_year_from_the_last_two_digits_of_the_year_and_the_counter_then_the_result_should_be_as_expected(
            final Integer expectedYear,
            final int twoLastDigitsOfYear,
            final int counter) {
        assertThat(AbstractDateBasedCdv11String.calculateCenturyBasedOn(twoLastDigitsOfYear, counter)).as("twoLastDigitsOfYear: " + twoLastDigitsOfYear + ", counter: " + counter).isEqualTo(expectedYear);
    }
    private static Stream<Arguments> provideYears() {
        return
                Stream.of(
                        Arguments.of(1700, new HashSet<IntSpan>()),
                        Arguments.of(1799, new HashSet<IntSpan>()),
                        Arguments.of(1800, new HashSet<IntSpan>()),
                        Arguments.of(1853, new HashSet<IntSpan>()),
                        Arguments.of(1854, new HashSet<IntSpan>() {{add(IntSpan.of(500, 749));}}),
                        Arguments.of(1877, new HashSet<IntSpan>() {{add(IntSpan.of(500, 749));}}),
                        Arguments.of(1899, new HashSet<IntSpan>() {{add(IntSpan.of(500, 749));}}),
                        Arguments.of(1900, new HashSet<IntSpan>() {{add(IntSpan.of(  0, 499));}}),
                        Arguments.of(1939, new HashSet<IntSpan>() {{add(IntSpan.of(  0, 499));}}),
                        Arguments.of(1940, new HashSet<IntSpan>() {{add(IntSpan.of(  0, 499)); add(IntSpan.of(  900, 999));}}),
                        Arguments.of(1971, new HashSet<IntSpan>() {{add(IntSpan.of(  0, 499)); add(IntSpan.of(  900, 999));}}),
                        Arguments.of(1999, new HashSet<IntSpan>() {{add(IntSpan.of(  0, 499)); add(IntSpan.of(  900, 999));}}),
                        Arguments.of(2000, new HashSet<IntSpan>() {{add(IntSpan.of(  500, 999));}}),
                        Arguments.of(2020, new HashSet<IntSpan>() {{add(IntSpan.of(  500, 999));}}),
                        Arguments.of(2039, new HashSet<IntSpan>() {{add(IntSpan.of(  500, 999));}}),
                        Arguments.of(2040, new HashSet<IntSpan>()),
                        Arguments.of(2070, new HashSet<IntSpan>()),
                        Arguments.of(2099, new HashSet<IntSpan>()),
                        Arguments.of(2100, new HashSet<IntSpan>())
                );
    }
    @ParameterizedTest
    @MethodSource("provideYears")
    void when_calculating_the_counter_spans_from_a_year_then_the_result_should_be_as_expected(final int year, final Set<IntSpan> expectedCounterSpans) {

        final Set<IntSpan> actualYearSpans = AbstractDateBasedCdv11String.calculateCounterSpansFromYear(year);

        assertThat(actualYearSpans).as("Expected number of counter spans: " + expectedCounterSpans.size()).size().isEqualTo(expectedCounterSpans.size());
        assertThat(actualYearSpans).as("Expected counter spans differ from actual ones.").containsAll(expectedCounterSpans);
    }
}