package net.barakiroth.cdv11;

import lombok.Getter;
import net.barakiroth.cdv11.exceptions.Cdv11StringFormatException;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Cdv11String {

    private static final int[] WEIGHTS_FOR_THE_9_FIRST_DIGITS = new int[] {3, 7, 6, 1, 8, 9, 4, 5, 2};
    private static final int[] WEIGHTS_FOR_THE_10_FIRST_DIGITS = new int[] {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

    @Getter
    private final String value;

    public Cdv11String(final String value) throws Cdv11StringFormatException {

        try {
            if (value == null) {
                throw new NullPointerException("A CDV 11 string cannot be null");
            }
            if (value.length() != 11) {
                throw new IllegalStateException("A CDV 11 string must be 11 characters long");
            }
            final String calculatedCdv11String =
                    Cdv11String.calculateValue(
                            Integer.parseInt(value.substring(0, 2)),
                            Integer.parseInt(value.substring(2, 4)),
                            Integer.parseInt(value.substring(4, 6)),
                            Integer.parseInt(value.substring(6, 9))
                    );

            if (value.equals(calculatedCdv11String)) {
                this.value = value;
            } else {
                throw new Cdv11StringFormatException("The provided string does not validate as to control digits");
            }
        } catch (Cdv11StringFormatException e) {
            throw e;
        } catch (Throwable e) {
            throw new Cdv11StringFormatException(e);
        }
    }

    public Cdv11String(
            final int firstPositionNumber,
            final int secondPositionNumber,
            final int thirdPositionNumber,
            final int fourthPositionNumber) throws Cdv11StringFormatException {

        if (firstPositionNumber < 0 || firstPositionNumber > 99) {
            throw new Cdv11StringFormatException("firstPositionNumber must be in the interval [0, 99], but it was: " + firstPositionNumber);
        }
        if (secondPositionNumber < 0 || secondPositionNumber > 99) {
            throw new Cdv11StringFormatException("secondPositionNumber must be in the interval [0, 99], but it was: " + secondPositionNumber);
        }
        if (thirdPositionNumber < 0 || thirdPositionNumber > 99) {
            throw new Cdv11StringFormatException("thirdPositionNumber must be in the interval [0, 99], but it was: " + thirdPositionNumber);
        }
        if (fourthPositionNumber < 0 || fourthPositionNumber > 999) {
            throw new Cdv11StringFormatException("fourthPositionNumber must be in the interval [0, 999], but it was: " + fourthPositionNumber);
        }

        this.value = Cdv11String.calculateValue(firstPositionNumber, secondPositionNumber, thirdPositionNumber, fourthPositionNumber);
    }

    private static String calculateValue(
            final int firstPositionNumber,
            final int secondPositionNumber,
            final int thirdPositionNumber,
            final int fourthPositionNumber) throws Cdv11StringFormatException {

        final int[] valueDigits = new int[11];

        valueDigits[0] = firstPositionNumber / 10;
        valueDigits[1] = firstPositionNumber % 10;

        valueDigits[2] = secondPositionNumber / 10;
        valueDigits[3] = secondPositionNumber % 10;

        valueDigits[4] = thirdPositionNumber / 10;
        valueDigits[5] = thirdPositionNumber % 10;

        valueDigits[6] = fourthPositionNumber / 100;
        valueDigits[7] = (fourthPositionNumber % 100) / 10;
        valueDigits[8] = fourthPositionNumber % 10;

        final AtomicInteger ix1 = new AtomicInteger(0);
        final int weightedSum1 = 11 - Arrays.stream(WEIGHTS_FOR_THE_9_FIRST_DIGITS).map(w -> w * valueDigits[ix1.getAndIncrement()]).sum() % 11;
        if (weightedSum1 == 10) {
            throw new Cdv11StringFormatException("Could not calculate a valid first control digit. firstPositionNumber: " + firstPositionNumber + ", secondPositionNumber: " + secondPositionNumber + ", thirdPositionNumber: " + thirdPositionNumber + ", fourthPositionNumber: " + fourthPositionNumber);
        }
        valueDigits[ 9] = weightedSum1 == 11 ? 0 : weightedSum1;

        final AtomicInteger ix2 = new AtomicInteger(0);
        final int weightedSum2 = 11 - Arrays.stream(WEIGHTS_FOR_THE_10_FIRST_DIGITS).map(w -> w * valueDigits[ix2.getAndIncrement()]).sum() % 11;
        if (weightedSum2 == 10) {
            throw new Cdv11StringFormatException("Could not calculate a valid second control digit. firstPositionNumber: " + firstPositionNumber + ", secondPositionNumber: " + secondPositionNumber + ", thirdPositionNumber: " + thirdPositionNumber + ", fourthPositionNumber: " + fourthPositionNumber);
        }
        valueDigits[10] = weightedSum2 == 11 ? 0 : weightedSum2;

        return Arrays.stream(valueDigits).mapToObj(String::valueOf).collect(Collectors.joining());
    }

    @Override
    public String toString() {
        return this.value;
    }
}