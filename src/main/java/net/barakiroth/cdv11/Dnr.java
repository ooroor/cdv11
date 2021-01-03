package net.barakiroth.cdv11;

import net.barakiroth.cdv11.exceptions.Cdv11StringFormatException;
import net.barakiroth.cdv11.exceptions.DateBasedCdv11StringFormatException;

import java.time.LocalDate;

public class Dnr extends AbstractDateBasedCdv11String {

    private static final int MONTH_DELTA = 0;
    private static final int DAY_DELTA = 40;

    public Dnr(final String value) throws Cdv11StringFormatException, DateBasedCdv11StringFormatException {
        super(value, Dnr.MONTH_DELTA, Dnr.DAY_DELTA);
    }

    public Dnr(final LocalDate localDate, final int counter) throws DateBasedCdv11StringFormatException {
        super(localDate, counter, Dnr.MONTH_DELTA, Dnr.DAY_DELTA);
    }
}