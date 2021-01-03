package net.barakiroth.cdv11;

import net.barakiroth.cdv11.exceptions.Cdv11StringFormatException;
import net.barakiroth.cdv11.exceptions.DateBasedCdv11StringFormatException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BnrUnitTest {

    private static Stream<Arguments> providePotentialBnrStrings() {
        return Stream.of(
                Arguments.of("40010112345", Cdv11StringFormatException.class, null, "The provided string does not validate as to control digits"),
                Arguments.of("01200112345", Cdv11StringFormatException.class, null, "The provided string does not validate as to control digits"),
                Arguments.of("19325612986", null, null, null)
        );
    }

    private static Stream<Arguments> provideLocalDatesAndCounters() {
        return Stream.of(
                Arguments.of(null, LocalDate.of(1, 2, 3), 0, DateBasedCdv11StringFormatException.class, null, ""),
                Arguments.of(null, LocalDate.of(1853, 1, 1), 0, DateBasedCdv11StringFormatException.class, null, "No date based cdv11 string can be calculated for the year in the given date: 1853-01-01"),
                Arguments.of(null, LocalDate.of(1853, 12, 1), 0, DateBasedCdv11StringFormatException.class, null, "No date based cdv11 string can be calculated for the year in the given date: 1853-12-01"),
                Arguments.of("19325600074", LocalDate.of(1956, 12, 19), 0, null, null, null),
                Arguments.of("19325632820", LocalDate.of(1956, 12, 19), 327, null, null, null),
                Arguments.of("01213950092", LocalDate.of(2039, 1, 1), 0, null, null, null),
                Arguments.of(null, LocalDate.of(2040, 1, 1), 0, DateBasedCdv11StringFormatException.class, null, "No date based cdv11 string can be calculated for the year in the given date: 2040-01-01")
        );
    }

    @ParameterizedTest
    @MethodSource("providePotentialBnrStrings")
    void when_instantiating_with_an_invalid_string_then_an_exception_should_be_thrown(
            final String potentialCdv11String,
            final Class<Throwable> expectedThrowableClass,
            final Class<Throwable> expectedThrowableCauseClass,
            final String expectedMsg) throws Cdv11StringFormatException, DateBasedCdv11StringFormatException {

        if (expectedThrowableClass == null) {

            assertThatCode(() -> new Bnr(potentialCdv11String))
                    .doesNotThrowAnyException();

            final Bnr bnr = new Bnr(potentialCdv11String);
            assertThat(bnr.getValue()).isEqualTo(potentialCdv11String);
        } else {
            if (expectedThrowableCauseClass == null) {
                assertThatThrownBy(
                        () -> {
                            new Bnr(potentialCdv11String);
                        })
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasNoCause();
            } else {
                assertThatThrownBy(
                        () -> {
                            new Bnr(potentialCdv11String);
                        })
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasCauseInstanceOf(expectedThrowableCauseClass);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideLocalDatesAndCounters")
    void when_generating_a_bnr_from_date_and_counter_then_an_expected_cdv11_string_should_be_created_if_possible(
            final String potentialCdv11String,
            final LocalDate localDate,
            final int counter,
            final Class<Throwable> expectedThrowableClass,
            final Class<Throwable> expectedThrowableCauseClass,
            final String expectedMsg
            ) throws DateBasedCdv11StringFormatException {

        if (expectedThrowableClass == null) {
            assertThatCode(() -> new Bnr(localDate, counter)).doesNotThrowAnyException();
            assertThat(new Bnr(localDate, counter).getValue()).isEqualTo(potentialCdv11String);
        } else {
            if (expectedThrowableCauseClass == null) {
                assertThatThrownBy(() -> new Bnr(localDate, counter))
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasNoNullFieldsOrProperties();
            } else {
                assertThatThrownBy(() -> new Bnr(localDate, counter))
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasCauseInstanceOf(expectedThrowableCauseClass);
            }
        }
    }
}
