package net.barakiroth.cdv11;

import java.util.Objects;

public class IntSpan {
    public static final IntSpan EMPTY = new IntSpan();
    final int lo;
    final int hi;
    private IntSpan() {
        this(0, -1);
    }
    private IntSpan(final int lo, final int hi) {
        this.lo = lo;
        this.hi = hi;
    }
    public static IntSpan of(final int lo, final int hi) {
        return lo > hi ? IntSpan.EMPTY : new IntSpan(lo, hi);
    }
    public boolean contains(final int elm) {
        return elm >= lo && elm <= hi;
    }
    public Integer next(final int counter) {
        final Integer next =
                (this == IntSpan.EMPTY)
                ?
                null
                :
                ((contains(counter))) && (counter < this.hi) ? counter + 1 : this.lo;
        return next;
    }
    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        final IntSpan otherIntSpan = (IntSpan) other;
        return this.lo == otherIntSpan.lo && this.hi == otherIntSpan.hi;
    }
    @Override
    public int hashCode() {
        return Objects.hash(lo, hi);
    }
    @Override
    public String toString() {
        return IntSpan.EMPTY.equals(this) ? "IntSpan.EMPTY" : "IntSpan{lo=" + lo +", hi=" + hi + "}" ;
    }
}