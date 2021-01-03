package net.barakiroth.cdv11;

import net.barakiroth.cdv11.exceptions.Cdv11StringFormatException;
import net.barakiroth.cdv11.exceptions.DateBasedCdv11StringFormatException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DnrUnitTest {

    private static Stream<Arguments> providePotentialDnrStrings() {
        return Stream.of(
                Arguments.of("15010112345", Cdv11StringFormatException.class, null, "The provided string does not validate as to control digits"),
                Arguments.of("01200112345", Cdv11StringFormatException.class, null, "The provided string does not validate as to control digits"),
                Arguments.of("41125512303", null, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("providePotentialDnrStrings")
    void when_instantiating_with_an_invalid_string_then_an_exception_should_be_thrown(
            final String potentialCdv11String,
            final Class<Throwable> expectedThrowableClass,
            final Class<Throwable> expectedThrowableCauseClass,
            final String expectedMsg) throws Cdv11StringFormatException, DateBasedCdv11StringFormatException {

        if (expectedThrowableClass == null) {

            assertThatCode(() -> new Dnr(potentialCdv11String))
                    .doesNotThrowAnyException();

            final Dnr dnr = new Dnr(potentialCdv11String);
            assertThat(dnr.getValue()).isEqualTo(potentialCdv11String);
        } else {
            if (expectedThrowableCauseClass == null) {
                assertThatThrownBy(
                        () -> {
                            new Dnr(potentialCdv11String);
                        })
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasNoCause();
            } else {
                assertThatThrownBy(
                        () -> {
                            new Dnr(potentialCdv11String);
                        })
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasCauseInstanceOf(expectedThrowableCauseClass);
            }
        }
    }
}
