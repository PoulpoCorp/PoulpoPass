package fr.poulpocorp.poulpopass.core;

import java.io.IOException;

/**
 * Signals that the password manager can't read the file
 *
 * @author PoulpoGaz
 */
public class ParseException extends IOException {

    public ParseException() {

    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }
}