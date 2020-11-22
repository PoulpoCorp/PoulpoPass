package fr.poulpocorp.poulpopass.core;

public class PasswordManagerException extends Exception {

    public PasswordManagerException() {
    }

    public PasswordManagerException(String message) {
        super(message);
    }

    public PasswordManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordManagerException(Throwable cause) {
        super(cause);
    }

    public PasswordManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}