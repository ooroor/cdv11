package net.barakiroth.cdv11;

import net.barakiroth.cdv11.exceptions.Cdv11StringFormatException;
import net.barakiroth.cdv11.exceptions.DateBasedCdv11StringFormatException;

import java.time.LocalDate;

public class Bnr extends AbstractDateBasedCdv11String {

    private static final int MONTH_DELTA = 20;
    private static final int DAY_DELTA = 0;

    public Bnr(final String value) throws Cdv11StringFormatException, DateBasedCdv11StringFormatException {
        super(value, Bnr.MONTH_DELTA, Bnr.DAY_DELTA);
    }

    public Bnr(final LocalDate localDate, final int counter) throws DateBasedCdv11StringFormatException {
        super(localDate, counter, Bnr.MONTH_DELTA, Bnr.DAY_DELTA);
    }
}