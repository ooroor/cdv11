package net.barakiroth.cdv11.exceptions;

public class Cdv11StringFormatException extends Exception {

    public Cdv11StringFormatException(final String msg) {
        super(msg);
    }

    public Cdv11StringFormatException(final Throwable e) {
        super(e);
    }
}