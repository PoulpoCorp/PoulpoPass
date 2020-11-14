package fr.poulpocorp.poulpopass.core;

public final class Passwords {
    public static final class Password {

    }

    private Passwords() {}
    private static Passwords instance = new Passwords();
    public static Passwords getInstance() {
        return instance;
    }
}
