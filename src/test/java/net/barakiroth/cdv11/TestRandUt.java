package net.barakiroth.cdv11;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class TestRandUt {

    private static LocalDateTime EARLIEST_LOCAL_DATETIME_WELL_INTO_THE_FUTURE =
            LocalDateTime.of(2030, 1, 1, 0, 0, 0);

    private static LocalDateTime LATEST_LOCAL_DATETIME_WELL_BACK_IN_TIME =
            LocalDateTime.of(2020, 1, 1, 0, 0, 0);

    public static String randomAlphabetic(int count, final Random random) {
        return TestRandUt.random(count, true, false, random);
    }

    public static String randomNumeric(int count, final Random random) {
        return TestRandUt.random(count, false, true, random);
    }

    public static int nextInt(final int startInclusive, final int endExclusive, final Random random) {

        Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.", new Object[0]);
        Validate.isTrue(startInclusive >= 0, "Both range values must be non-negative.", new Object[0]);
        return startInclusive == endExclusive ? startInclusive : startInclusive + random.nextInt(endExclusive - startInclusive);
    }

    public static long nextLong(final Random random) {
        return TestRandUt.nextLong(0L, 9223372036854775807L, random);
    }

    public static LocalDateTime randomLocalDateTimeWellBackInTime(final Random random) {

        final LocalDateTime latestLocalDateTimeWellBackInTime = TestRandUt.LATEST_LOCAL_DATETIME_WELL_BACK_IN_TIME;
        final long randomMillisecondsGreaterThanTwoDays = randomMillisecondsBetweenTwoDaysAndOneYear(random);
        final LocalDateTime randomLocalDateTimeWellBackInTime =
                ChronoUnit.MILLIS.addTo(latestLocalDateTimeWellBackInTime, -randomMillisecondsGreaterThanTwoDays);

        return randomLocalDateTimeWellBackInTime;
    }

    public static LocalDateTime randomLocalDateTimeWellBackInTimeAfter(final LocalDateTime localDateTime, final Random random) {
        if (localDateTime.isAfter(TestRandUt.LATEST_LOCAL_DATETIME_WELL_BACK_IN_TIME)) {
            throw new RuntimeException(
                    "Error in the test data. You cannot ask for a LocalDateTime well back in time after "
                            + TestRandUt.LATEST_LOCAL_DATETIME_WELL_BACK_IN_TIME);
        }
        return TestRandUt.randomLocalDateTimeBetween(localDateTime, TestRandUt.LATEST_LOCAL_DATETIME_WELL_BACK_IN_TIME, random);
    }

    public static LocalDate randomLocalDateWellBackInTime(final Random random) {

        final LocalDate latestLocalDateWellBackInTime = TestRandUt.getLatestLocalDateWellBackInTime();
        final int randomNumberOfDaysGreaterThanTwoDays = TestRandUt.randomNumberOfDaysBetweenTwoDaysAndOneYear(random);
        final LocalDate randomLocalDateWellBackInTime =
                ChronoUnit.DAYS.addTo(latestLocalDateWellBackInTime, -randomNumberOfDaysGreaterThanTwoDays);

        return randomLocalDateWellBackInTime;
    }

    public static LocalDate randomLocalDateWellIntoTheFuture(final Random random) {
        final LocalDate earliestLocalDateWellIntoTheFuture = TestRandUt.getEarliestLocalDateWellIntoTheFuture();
        final int randomDaysGreaterThanTwoDays = TestRandUt.randomDaysBetweenTwoDaysAndOneYear(random);
        final LocalDate randomLocalDateWellIntoTheFuture =
                ChronoUnit.DAYS.addTo(earliestLocalDateWellIntoTheFuture, randomDaysGreaterThanTwoDays);

        return randomLocalDateWellIntoTheFuture;
    }

    public static LocalDate randomLocalDateWellIntoTheFutureBefore(final LocalDate localDate, final Random random) {

        final LocalDate earliestLocalDateWellIntoTheFuture = getEarliestLocalDateWellIntoTheFuture();
        if (localDate.isBefore(earliestLocalDateWellIntoTheFuture)) {
            throw new RuntimeException(
                    "Error in the test data. You cannot ask for a LocalDate well into the future before "
                            + earliestLocalDateWellIntoTheFuture);
        }
        final LocalDate randomLocalDateWellIntoTheFutureBefore =
                TestRandUt.randomLocalDateBetween(earliestLocalDateWellIntoTheFuture, localDate, random);

        return randomLocalDateWellIntoTheFutureBefore;
    }

    public static LocalDate randomLocalDateWellIntoTheFutureAfter(final LocalDate localDate, final Random random) {

        final int randomDaysBetweenTwoDaysAndOneYear = TestRandUt.randomDaysBetweenTwoDaysAndOneYear(random);
        final LocalDate earliestLocalDateWellIntoTheFuture = TestRandUt.getEarliestLocalDateWellIntoTheFuture();
        final LocalDate randomLocalDateWellIntoTheFutureAfter =
                ChronoUnit.DAYS.addTo(
                        localDate.isBefore(earliestLocalDateWellIntoTheFuture) ? earliestLocalDateWellIntoTheFuture : localDate,
                        randomDaysBetweenTwoDaysAndOneYear);

        return randomLocalDateWellIntoTheFutureAfter;
    }

    public static LocalDateTime randomLocalDateTimeWellIntoTheFutureAfter(final LocalDate localDate, final Random random) {

        // TODO: randomLocalDateWellIntoTheFutureAfter should call randomLocalDateTimeWellIntoTheFutureAfter, not the other way around
        final LocalDate randomLocalDateWellIntoTheFutureAfter =
                randomLocalDateWellIntoTheFutureAfter(localDate, random);
        final LocalDateTime randomLocalDateTimeWellIntoTheFutureAfter =
                LocalDateTime.of(randomLocalDateWellIntoTheFutureAfter, LocalTime.of(0, 0, 0));

        return randomLocalDateTimeWellIntoTheFutureAfter;
    }

    public static LocalDate randomLocalDateWellIntoTheFutureAfter(final LocalDateTime localDateTime, final Random random) {

        final LocalDate randomLocalDateWellIntoTheFutureAfter =
                TestRandUt.randomLocalDateWellIntoTheFutureAfter(localDateTime.toLocalDate(), random);
        return randomLocalDateWellIntoTheFutureAfter;
    }

    private static LocalDateTime getLatestLocalDateTimeWellBackInTime() {
        return TestRandUt.LATEST_LOCAL_DATETIME_WELL_BACK_IN_TIME;
    }

    private static LocalDate getLatestLocalDateWellBackInTime() {
        return TestRandUt.getLatestLocalDateTimeWellBackInTime().toLocalDate();
    }

    private static LocalDateTime getEarliestLocalDateTimeWellIntoTheFuture() {
        return TestRandUt.EARLIEST_LOCAL_DATETIME_WELL_INTO_THE_FUTURE;
    }

    private static LocalDate getEarliestLocalDateWellIntoTheFuture() {
        return getEarliestLocalDateTimeWellIntoTheFuture().toLocalDate();
    }

    private static long nextLong(final long startInclusive, final long endExclusive, final Random random) {
        Validate.isTrue(endExclusive >= startInclusive, "Start value must be smaller or equal to end value.", new Object[0]);
        Validate.isTrue(startInclusive >= 0L, "Both range values must be non-negative.", new Object[0]);
        return startInclusive == endExclusive ? startInclusive : (long) TestRandUt.nextDouble((double)startInclusive, (double)endExclusive, random);
    }

    private static double nextDouble(double startInclusive, double endInclusive, final Random random) {
        Validate.isTrue(endInclusive >= startInclusive, "Start value must be smaller or equal to end value.", new Object[0]);
        Validate.isTrue(startInclusive >= 0.0D, "Both range values must be non-negative.", new Object[0]);
        return startInclusive == endInclusive ? startInclusive : startInclusive + (endInclusive - startInclusive) * random.nextDouble();
    }

    private static String random(int count, boolean letters, boolean numbers, final Random random) {
        return TestRandUt.random(count, 0, 0, letters, numbers, random);
    }

    private static String random(int count, int start, int end, boolean letters, boolean numbers, final Random random) {
        return RandomStringUtils.random(count, start, end, letters, numbers, (char[])null, random);
    }

    private static long randomMillisecondsBetweenTwoDaysAndOneYear(final Random random) {
        final long milliSecondsInTwoDays = 2L * 24L * 60L * 60L * 1000L;
        final long milliSecondsInOneYear = 365L * 24L * 60L * 60L * 1000L;
        return TestRandUt.nextLong(milliSecondsInTwoDays, milliSecondsInOneYear + 1, random);
    }

    private static int randomNumberOfDaysBetweenTwoDaysAndOneYear(final Random random) {
        return 2 + random.nextInt(364);
    }

    private static int randomDaysBetweenTwoDaysAndOneYear(final Random random) {
        return TestRandUt.nextInt(2, 366, random);
    }

    private static LocalDate randomLocalDateBetween(
            final LocalDate localDate1,
            final LocalDate localDate2,
            final Random    random) {

        final long daysBetween = Math.abs(ChronoUnit.DAYS.between(localDate1, localDate2));
        final long randomDaysOffset = TestRandUt.nextLong(0L, daysBetween + 1L, random);
        final LocalDate randomLocalDateBetween =
                ChronoUnit.DAYS.addTo(localDate1.isBefore(localDate2) ? localDate1 : localDate2, randomDaysOffset);

        return randomLocalDateBetween;
    }

    private static LocalDate randomLocalDateBetween(
            final LocalDate     localDate,
            final LocalDateTime localDateTime,
            final Random        random) {
        return TestRandUt.randomLocalDateBetween(localDate, localDateTime.toLocalDate(), random);
    }

    private static LocalDate randomLocalDateBetween(
            final LocalDateTime localDateTime,
            final LocalDate     localDate,
            final Random        random) {
        return TestRandUt.randomLocalDateBetween(localDateTime.toLocalDate(), localDate, random);
    }

    private static LocalDate randomLocalDateBetween(
            final LocalDateTime localDateTime1,
            final LocalDateTime localDateTime2,
            final Random        random) {
        return TestRandUt.randomLocalDateBetween(localDateTime1.toLocalDate(), localDateTime2.toLocalDate(), random);
    }

    private static LocalDateTime randomLocalDateTimeBetween(
            final LocalDateTime localDateTime1,
            final LocalDateTime localDateTime2,
            final Random        random) {

        final long milliSecondsBetween = Math.abs(ChronoUnit.MILLIS.between(localDateTime1, localDateTime2));
        final long randomMilliSecondsOffset = TestRandUt.nextLong(0L, milliSecondsBetween + 1L, random);
        final LocalDateTime randomLocalDateTimeBetween =
                ChronoUnit.MILLIS.addTo(localDateTime1.isBefore(localDateTime2) ? localDateTime1 : localDateTime2, randomMilliSecondsOffset);

        return randomLocalDateTimeBetween;
    }

    private static LocalDateTime randomLocalDateTimeBetween(
            final LocalDateTime localDateTime,
            final LocalDate localDate,
            final Random        random) {
        return TestRandUt
                .randomLocalDateTimeBetween(
                        localDateTime,
                        LocalDateTime.of(localDate, LocalTime.of(0, 0)),
                        random);
    }

    private static LocalDateTime randomLocalDateTimeBetween(
            final LocalDate     localDate,
            final LocalDateTime localDateTime,
            final Random        random) {
        return TestRandUt
                .randomLocalDateTimeBetween(
                        LocalDateTime.of(localDate, LocalTime.of(0, 0)),
                        localDateTime,
                        random);
    }

    private static LocalDateTime randomLocalDateTimeBetween(
            final LocalDate localDate1,
            final LocalDate localDate2,
            final Random    random) {
        return TestRandUt
                .randomLocalDateTimeBetween(
                        LocalDateTime.of(localDate1, LocalTime.of(0, 0)),
                        LocalDateTime.of(localDate2, LocalTime.of(0, 0)),
                        random);
    }
}
