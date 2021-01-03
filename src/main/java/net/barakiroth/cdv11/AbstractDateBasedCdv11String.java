package net.barakiroth.cdv11;

import net.barakiroth.cdv11.exceptions.Cdv11StringFormatException;
import net.barakiroth.cdv11.exceptions.DateBasedCdv11StringFormatException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * https://www.skatteetaten.no/en/person/foreign/norwegian-identification-number/d-number/
 * https://www.skatteetaten.no/en/person/national-registry/birth-and-name-selection/children-born-in-norway/national-id-number/
 */
abstract class AbstractDateBasedCdv11String {

    private static final Map<IntSpan, Map<IntSpan, Integer>> centuryOn2YearSpanAndCounterSpan;
    private static final Map<IntSpan, Set<IntSpan>> counterSpansOnYearSpan;

    static {
        final Map<IntSpan, Map<IntSpan, Integer>> centuryOn2YearSpanAndCounterSpanTemp = new HashMap<>();

        centuryOn2YearSpanAndCounterSpanTemp.put(IntSpan.of(0, 39), new HashMap<>() {{
            put(IntSpan.of(0, 499), 1900);
            put(IntSpan.of(500, 999), 2000);
        }});
        centuryOn2YearSpanAndCounterSpanTemp.put(IntSpan.of(40, 53), new HashMap<>() {{
            put(IntSpan.of(0, 499), 1900);
            put(IntSpan.of(900, 999), 1900);
        }});
        centuryOn2YearSpanAndCounterSpanTemp.put(IntSpan.of(54, 99), new HashMap<>() {{
            put(IntSpan.of(0, 499), 1900);
            put(IntSpan.of(500, 749), 1800);
            put(IntSpan.of(900, 999), 1900);
        }});
        centuryOn2YearSpanAndCounterSpan = Collections.unmodifiableMap(centuryOn2YearSpanAndCounterSpanTemp);
    }

    static {
        final Map<IntSpan, Set<IntSpan>> counterSpansOnYearSpanTemp = new HashMap<>();
        counterSpansOnYearSpanTemp
                .put(
                        IntSpan.of(1854, 1899),
                        new HashSet<>() {{
                            add(IntSpan.of(500, 749));
                        }}
                );
        counterSpansOnYearSpanTemp
                .put(
                        IntSpan.of(1900, 1939),
                        new HashSet<>() {{
                            add(IntSpan.of(0, 499));
                        }}
                );
        counterSpansOnYearSpanTemp
                .put(
                        IntSpan.of(1940, 1999),
                        new HashSet<>() {{
                            add(IntSpan.of(0, 499));
                            add(IntSpan.of(900, 999));
                        }}
                );
        counterSpansOnYearSpanTemp
                .put(
                        IntSpan.of(2000, 2039),
                        new HashSet<>() {{
                            add(IntSpan.of(500, 999));
                        }}
                );
        counterSpansOnYearSpan = Collections.unmodifiableMap(counterSpansOnYearSpanTemp);
    }

    private final Cdv11String cdv11String;

    protected AbstractDateBasedCdv11String(
            final String dateBasedCdv11String,
            final int monthDelta,
            final int dayDelta) throws Cdv11StringFormatException, DateBasedCdv11StringFormatException {

        final Cdv11String cdv11String = new Cdv11String(dateBasedCdv11String);

        final int lastTwoDigitsOfYear = Integer.parseInt(dateBasedCdv11String.substring(4, 6));
        final int counter = Integer.parseInt(dateBasedCdv11String.substring(6, 9));

        final Integer century = AbstractDateBasedCdv11String.calculateCenturyBasedOn(lastTwoDigitsOfYear, counter);
        if (century == null) {
            throw new DateBasedCdv11StringFormatException("The counter " + counter + " cannot be combined with a year ending with " + lastTwoDigitsOfYear);
        } else {
            final int day = Integer.parseInt(dateBasedCdv11String.substring(0, 2)) - dayDelta;
            final int month = Integer.parseInt(dateBasedCdv11String.substring(2, 4)) - monthDelta;
            final int year = century + lastTwoDigitsOfYear;
            try {
                LocalDate.of(year, month, day);
            } catch (Throwable e) {
                throw new DateBasedCdv11StringFormatException("The combination of the counter " + counter + " and the lastTwoDigitsOfYear " + lastTwoDigitsOfYear + " with day " + day + " and month " + month + " gives a calculated year of " + year + " which altogether would bring about an invalid date from: " + dateBasedCdv11String, e);
            }
            this.cdv11String = cdv11String;
        }
    }

    protected AbstractDateBasedCdv11String(
            final LocalDate localDate,
            final int counter,
            final int monthDelta,
            final int dayDelta) throws DateBasedCdv11StringFormatException {

        final Set<IntSpan> counterSpansFromYear =
                AbstractDateBasedCdv11String.calculateCounterSpansFromYear(localDate.getYear());

        if (counterSpansFromYear.size() == 0) {
            throw new DateBasedCdv11StringFormatException("No date based cdv11 string can be calculated for the year in the given date: " + localDate.toString());
        }

        final IntSpan actualCounterSpan =
            counterSpansFromYear
                    .stream()
                    .filter((counterSpan) -> counterSpan.contains(counter))
                    .findFirst()
                    .orElse(IntSpan.EMPTY);

        int chosenCounter;
        final IntSpan chosenCounterSpan;
        if (IntSpan.EMPTY.equals(actualCounterSpan)) {
            chosenCounterSpan = counterSpansFromYear.iterator().next();
            chosenCounter = chosenCounterSpan.lo;
        } else {
            chosenCounterSpan = actualCounterSpan;
            chosenCounter = counter;
        }

        Cdv11String cdv11String = null;
        do {
            try {
                final Cdv11String tempCdv11String =
                    new Cdv11String(
                            localDate.getDayOfMonth() + dayDelta,
                            localDate.getMonth().getValue() + monthDelta,
                            localDate.getYear() % 100,
                            chosenCounter
                    );
                cdv11String = tempCdv11String;
            } catch (Throwable e) {
                chosenCounter = chosenCounterSpan.next(chosenCounter);
            }
        } while (cdv11String == null);

        this.cdv11String = cdv11String;
    }

    /**
     * Calculate the century from the last
     * two digits of the year of birth (fødselsdato)
     * and the day counter (the first three digits of the personnummer)
     *
     * @param twoLastDigitsOfYear
     * @param counter
     * @return <code>null</code> if no such exists, otherwise the century
     */
    static Integer calculateCenturyBasedOn(
            final int twoLastDigitsOfYear,
            final int counter) {

        return
                AbstractDateBasedCdv11String.centuryOn2YearSpanAndCounterSpan
                        .getOrDefault(
                                AbstractDateBasedCdv11String.centuryOn2YearSpanAndCounterSpan
                                        .keySet()
                                        .stream()
                                        .filter(intSpan -> intSpan.contains(twoLastDigitsOfYear))
                                        .findFirst()
                                        .orElse(IntSpan.EMPTY),
                                new HashMap<>()
                        )
                        .get(
                                AbstractDateBasedCdv11String.centuryOn2YearSpanAndCounterSpan
                                        .getOrDefault(
                                                AbstractDateBasedCdv11String.centuryOn2YearSpanAndCounterSpan
                                                        .keySet()
                                                        .stream()
                                                        .filter(intSpan -> intSpan.contains(twoLastDigitsOfYear))
                                                        .findFirst()
                                                        .orElse(IntSpan.EMPTY),
                                                new HashMap<>()
                                        )
                                        .keySet()
                                        .stream()
                                        .filter((intSpan) -> intSpan.contains(counter))
                                        .findFirst()
                                        .orElse(IntSpan.EMPTY)
                        );
    }

    /**
     * Calculates the span(s) that the counter must be in for a given year
     *
     * @param year
     * @return the calculated span(s) that the counter must be in for a given year
     */
    static Set<IntSpan> calculateCounterSpansFromYear(final int year) {

        final Set<IntSpan> counterSpans =
                AbstractDateBasedCdv11String.counterSpansOnYearSpan
                        .getOrDefault(
                                AbstractDateBasedCdv11String.counterSpansOnYearSpan
                                        .keySet()
                                        .stream()
                                        .filter((yearSpan) -> yearSpan.contains(year))
                                        .findFirst()
                                        .orElse(IntSpan.EMPTY),
                                new HashSet<>()
                        );

        return counterSpans;
    }

    public String getValue() {
        return this.cdv11String.getValue();
    }
}
