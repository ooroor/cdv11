package net.barakiroth.cdv11;

import net.barakiroth.cdv11.exceptions.Cdv11StringFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Cdv11StringUnitTest {

    private static Stream<Arguments> providePotentialCdv11Strings() {
        return Stream.of(
                Arguments.of(null, Cdv11StringFormatException.class, NullPointerException.class, "A CDV 11 string cannot be null"),
                Arguments.of("a1b2c3d4e5f", Cdv11StringFormatException.class, NumberFormatException.class, "For input string: \"a1\""),
                Arguments.of("abc", Cdv11StringFormatException.class, IllegalStateException.class, "A CDV 11 string must be 11 characters long"),
                Arguments.of("", Cdv11StringFormatException.class, IllegalStateException.class, "A CDV 11 string must be 11 characters long"),
                Arguments.of("  ", Cdv11StringFormatException.class, IllegalStateException.class, "A CDV 11 string must be 11 characters long"),
                Arguments.of("123", Cdv11StringFormatException.class, IllegalStateException.class, "A CDV 11 string must be 11 characters long"),
                Arguments.of(" 21042221454 ", Cdv11StringFormatException.class, IllegalStateException.class, "A CDV 11 string must be 11 characters long"),
                Arguments.of("123456789012", Cdv11StringFormatException.class, IllegalStateException.class, "A CDV 11 string must be 11 characters long"),
                Arguments.of("2104 2221454", Cdv11StringFormatException.class, IllegalStateException.class, "A CDV 11 string must be 11 characters long"),
                Arguments.of("21042221453", Cdv11StringFormatException.class, null, "The provided string does not validate as to control digits"),
                Arguments.of("21042221454", null, null, null)
        );
    }

    private static Stream<Arguments> providePotentialCdv11StringPositionalNumbers() {
        return Stream.of(
                Arguments.of(-1, 0, 0, 0, null, Cdv11StringFormatException.class, null, "firstPositionNumber must be in the interval [0, 99], but it was: -1"),
                Arguments.of(100, 0, 0, 0, null, Cdv11StringFormatException.class, null, "firstPositionNumber must be in the interval [0, 99], but it was: 100"),

                Arguments.of(0, -1, 0, 0, null, Cdv11StringFormatException.class, null, "secondPositionNumber must be in the interval [0, 99], but it was: -1"),
                Arguments.of(0, 100, 0, 0, null, Cdv11StringFormatException.class, null, "secondPositionNumber must be in the interval [0, 99], but it was: 100"),

                Arguments.of(0, 0, -1, 0, null, Cdv11StringFormatException.class, null, "thirdPositionNumber must be in the interval [0, 99], but it was: -1"),
                Arguments.of(0, 0, 100, 0, null, Cdv11StringFormatException.class, null, "thirdPositionNumber must be in the interval [0, 99], but it was: 100"),

                Arguments.of(0, 0, 0, -1, null, Cdv11StringFormatException.class, null, "fourthPositionNumber must be in the interval [0, 999], but it was: -1"),
                Arguments.of(0, 0, 0, 1000, null, Cdv11StringFormatException.class, null, "fourthPositionNumber must be in the interval [0, 999], but it was: 1000"),

                Arguments.of(1, 2, 3, 12, null, Cdv11StringFormatException.class, null, "Could not calculate a valid first control digit"),
                Arguments.of(1, 2, 3, 8, null, Cdv11StringFormatException.class, null, "Could not calculate a valid second control digit"),

                Arguments.of(0, 0, 0, 0, "00000000000", null, null, null),
                Arguments.of(21, 4, 22, 214, "21042221454", null, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("providePotentialCdv11Strings")
    void when_instantiating_with_an_invalid_string_then_an_exception_should_be_thrown(
            final String potentialCdv11String,
            final Class<Throwable> expectedThrowableClass,
            final Class<Throwable> expectedThrowableCauseClass,
            final String expectedMsg) throws Cdv11StringFormatException {

        if (expectedThrowableClass == null) {

            assertThatCode(() -> new Cdv11String(potentialCdv11String))
                    .doesNotThrowAnyException();

            final Cdv11String cdv11String = new Cdv11String(potentialCdv11String);
            assertThat(cdv11String.getValue()).isEqualTo(potentialCdv11String);
        } else {
            if (expectedThrowableCauseClass == null) {
                assertThatThrownBy(
                        () -> {
                            new Cdv11String(potentialCdv11String);
                        })
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasNoCause();
            } else {
                assertThatThrownBy(
                        () -> {
                            new Cdv11String(potentialCdv11String);
                        })
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasCauseInstanceOf(expectedThrowableCauseClass);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("providePotentialCdv11StringPositionalNumbers")
    void when_instantiating_with_invalid_positional_numbers_then_an_exception_should_be_thrown(
            final int firstPositionNumber,
            final int secondPositioNnumber,
            final int thirdPositionNumber,
            final int fourthPositionNumber,
            final String potentialCdv11String,
            final Class<Throwable> expectedThrowableClass,
            final Class<Throwable> expectedThrowableCauseClass,
            final String expectedMsg) throws Cdv11StringFormatException {

        if (expectedThrowableClass == null) {

            assertThatCode(() -> new Cdv11String(firstPositionNumber, secondPositioNnumber, thirdPositionNumber, fourthPositionNumber))
                    .doesNotThrowAnyException();

            final Cdv11String cdv11String = new Cdv11String(firstPositionNumber, secondPositioNnumber, thirdPositionNumber, fourthPositionNumber);
            assertThat(cdv11String.getValue()).isEqualTo(potentialCdv11String);
        } else {
            if (expectedThrowableCauseClass == null) {
                assertThatThrownBy(
                        () -> new Cdv11String(firstPositionNumber, secondPositioNnumber, thirdPositionNumber, fourthPositionNumber))
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasNoCause();
            } else {
                assertThatThrownBy(
                        () -> new Cdv11String(firstPositionNumber, secondPositioNnumber, thirdPositionNumber, fourthPositionNumber))
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasCauseInstanceOf(expectedThrowableCauseClass);
            }
        }
    }

    @Test
    void when_creating_a_valid_instance_then_toString_should_return_its_string_value() throws Cdv11StringFormatException {
        final Cdv11String cdv11String = new Cdv11String("21042221454");
        assertThat(cdv11String.getValue()).isEqualTo(cdv11String.toString());
    }
}
