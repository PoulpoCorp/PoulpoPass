package fr.poulpocorp.poulpopass.app.model;

import fr.poulpocorp.poulpopass.core.Password;

import java.util.EventObject;

public class PasswordEvent extends EventObject {

    public static final int NAME = 1;
    public static final int PASSWORD = 2;
    public static final int URLS = 4;
    public static final int ASSOCIATION = 8;

    private final int type;

    public PasswordEvent(Password source, int type) {
        super(source);

        this.type = type;
    }

    public int getType() {
        return type;
    }
}