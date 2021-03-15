package fr.poulpocorp.poulpopass.core;

import java.util.Objects;

/**
 * @author PoulpoGaz
 */
public abstract class PasswordManagerElement {

    IPasswordManager passwordManager;

    protected String name;

    public PasswordManagerElement(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    public abstract boolean setName(String name);

    public IPasswordManager getPasswordManager() {
        return passwordManager;
    }
}