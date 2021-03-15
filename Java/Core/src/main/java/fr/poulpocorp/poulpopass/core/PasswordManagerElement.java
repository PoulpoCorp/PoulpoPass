package fr.poulpocorp.poulpopass.core;

import java.util.Objects;

/**
 * The base class for {@link Category} and {@link Password}
 *
 * @author PoulpoGaz
 */
public abstract class PasswordManagerElement {

    IPasswordManager passwordManager;

    protected String name;

    /**
     * Create a new PasswordManagerElement.
     *
     * @param name the name of this element
     */
    PasswordManagerElement(String name) {
        this.name = Objects.requireNonNull(name);
    }

    /**
     * @return The name of this element
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the new name
     * @return {@code true} if the specified name is valid
     */
    public abstract boolean setName(String name);

    /**
     * @return The {@link PasswordManager} in which this element is
     */
    public IPasswordManager getPasswordManager() {
        return passwordManager;
    }
}