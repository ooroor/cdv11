package net.barakiroth.cdv11;

import net.barakiroth.cdv11.exceptions.Cdv11StringFormatException;
import net.barakiroth.cdv11.exceptions.DateBasedCdv11StringFormatException;

import java.time.LocalDate;

public class Fnr extends AbstractDateBasedCdv11String {

    private static final int MONTH_DELTA = 0;
    private static final int DAY_DELTA = 0;

    public Fnr(final String value) throws Cdv11StringFormatException, DateBasedCdv11StringFormatException {
        super(value, Fnr.MONTH_DELTA, Fnr.DAY_DELTA);
    }

    public Fnr(final LocalDate localDate, final int counter) throws DateBasedCdv11StringFormatException {
        super(localDate, counter, Fnr.MONTH_DELTA, Fnr.DAY_DELTA);
    }
}