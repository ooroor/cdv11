package net.barakiroth.cdv11;

import net.barakiroth.cdv11.exceptions.Cdv11StringFormatException;
import net.barakiroth.cdv11.exceptions.DateBasedCdv11StringFormatException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DateTimeException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FnrUnitTest {

    private static String makeMsg(
            final int counter,
            final int lastTwoDigitsOfYear,
            final int day,
        final int month,
            final int year,
            final String cdv11String) {
        final String msgtemplate = "The combination of the counter %d and the lastTwoDigitsOfYear %d with day %d and month %d gives a calculated year of %d which altogether would bring about an invalid date from: %s";

        return String.format(msgtemplate, counter, lastTwoDigitsOfYear, day, month, year, cdv11String);
    }

    private static Stream<Arguments> providePotentialBnrStrings() {

        return Stream.of(
                // [000, 499] <-> [1900, 1999]
                Arguments.of("01990000073", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(0, 0, 1, 99, 1900, "01990000073")),
                Arguments.of("01994900044", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(0, 49, 1, 99, 1949, "01994900044")),
                Arguments.of("01999900085", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(0, 99, 1, 99, 1999, "01999900085")),
                Arguments.of("15034700076", null, null, null),

                // [000, 499] <-> [1900, 1999]
                Arguments.of("77010012387", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(123, 0, 77, 1, 1900, "77010012387")),
                Arguments.of("66010112398", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(123, 1, 66, 1, 1901, "66010112398")),
                Arguments.of("01760112344", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(123, 1, 1, 76, 1901, "01760112344")),
                Arguments.of("01760612339", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(123, 6, 1, 76, 1906, "01760612339")),
                Arguments.of("01761212393", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(123, 12, 1, 76, 1912, "01761212393")),
                Arguments.of("15034712392", null, null, null),

                // [000, 499] <-> [1900, 1999]
                Arguments.of("01990125097", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(250, 1, 1, 99, 1901, "01990125097")),
                Arguments.of("01990625081", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(250, 6, 1, 99, 1906, "01990625081")),
                Arguments.of("01991225036", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(250, 12, 1, 99, 1912, "01991225036")),
                Arguments.of("21042221454", null, null, null),

                // [000, 499] <-> [1900, 1999]
                Arguments.of("01990149972", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(499, 1, 1, 99, 1901, "01990149972")),
                Arguments.of("01995049992", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(499, 50, 1, 99, 1950, "01995049992")),
                Arguments.of("01999949963", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(499, 99, 1, 99, 1999, "01999949963")),
                Arguments.of("21042221454", null, null, null),

                // [500, 749] <-> [2000, 2039]
                Arguments.of("01990050097", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(500, 0, 1, 99, 2000, "01990050097")),
                Arguments.of("01992050043", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(500, 20, 1, 99, 2020, "01992050043")),
                Arguments.of("01763950073", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(500, 39, 1, 76, 2039, "01763950073")),
                Arguments.of("19125750012", null, null, null),

                // [500, 749] <-> [1900, 1999]
                Arguments.of("01990062575", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(625, 0, 1, 99, 2000, "01990062575")),
                Arguments.of("01992062521", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(625, 20, 1, 99, 2020, "01992062521")),
                Arguments.of("01993962518", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(625, 39, 1, 99, 2039, "01993962518")),
                Arguments.of("19127762557", null, null, null),

                // [500, 749] <-> [2000, 2039]
                Arguments.of("01990074972", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(749, 0, 1, 99, 2000, "01990074972")),
                Arguments.of("01992074929", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(749, 20, 1, 99, 2020, "01992074929")),
                Arguments.of("01993974915", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(749, 39, 1, 99, 2039, "01993974915")),
                Arguments.of("19122174908", null, null, null),

                // [500, 749] <-> [..40, ..53] ILLEGAL!
                Arguments.of("01994074985", DateBasedCdv11StringFormatException.class, null, "The counter 749 cannot be combined with a year ending with 40"),
                Arguments.of("01995374966", DateBasedCdv11StringFormatException.class, null, "The counter 749 cannot be combined with a year ending with 53"),

                // [500, 749] <-> [1854, 1899]
                Arguments.of("01995474987", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(749, 54, 1, 99, 1854, "01995474987")),
                Arguments.of("01997774996", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(749, 77, 1, 99, 1877, "01997774996")),
                Arguments.of("01999974984", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(749, 99, 1, 99, 1899, "01999974984")),
                Arguments.of("19127774954", null, null, null),

                // [500, 749] <-> [1854, 1899]
                Arguments.of("01995474987", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(749, 54, 1, 99, 1854, "01995474987")),
                Arguments.of("01997774996", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(749, 77, 1, 99, 1877, "01997774996")),
                Arguments.of("01999974984", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(749, 99, 1, 99, 1899, "01999974984")),

                // [750, 899] <-> [2000, 2039]
                Arguments.of("01890075041", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(750, 00, 1, 89, 2000, "01890075041")),
                Arguments.of("01892175019", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(750, 21, 1, 89, 2021, "01892175019")),
                Arguments.of("01893975094", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(750, 39, 1, 89, 2039, "01893975094")),
                Arguments.of("19120075058", null, null, null),

                // [750, 899] <-> [..40, ..99] ILLEGAL!
                Arguments.of("01894075054", DateBasedCdv11StringFormatException.class, null, "The counter 750 cannot be combined with a year ending with 40"),
                Arguments.of("01897075039", DateBasedCdv11StringFormatException.class, null, "The counter 750 cannot be combined with a year ending with 70"),
                Arguments.of("01899975053", DateBasedCdv11StringFormatException.class, null, "The counter 750 cannot be combined with a year ending with 99"),

                // [900, 999] <-> [2000, 2099] ILLEGAL!
                Arguments.of("01990090048", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(900, 00, 1, 99, 2000, "01990090048")),
                Arguments.of("01992190015", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(900, 21, 1, 99, 2021, "01992190015")),
                Arguments.of("01993990090", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(900, 39, 1, 99, 2039, "01993990090")),

                // [900, 999] <-> [2000, 2099] ILLEGAL!
                Arguments.of("01994090050", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(900, 40, 1, 99, 1940, "01994090050")),
                Arguments.of("01997090035", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(900, 70, 1, 99, 1970, "01997090035")),
                Arguments.of("01779990083", DateBasedCdv11StringFormatException.class, DateTimeException.class, FnrUnitTest.makeMsg(900, 99, 1, 77, 1999, "01779990083"))
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

            assertThatCode(() -> new Fnr(potentialCdv11String))
                    .doesNotThrowAnyException();

            final Fnr fnr = new Fnr(potentialCdv11String);
            assertThat(fnr.getValue()).isEqualTo(potentialCdv11String);
        } else {
            if (expectedThrowableCauseClass == null) {
                assertThatThrownBy(
                        () -> {
                            new Fnr(potentialCdv11String);
                        })
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasNoCause();
            } else {
                assertThatThrownBy(
                        () -> {
                            new Fnr(potentialCdv11String);
                        })
                        .isInstanceOf(expectedThrowableClass)
                        .hasMessageContaining(expectedMsg)
                        .hasCauseInstanceOf(expectedThrowableCauseClass);
            }
        }
    }
}
