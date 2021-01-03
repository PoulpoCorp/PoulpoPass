package fr.poulpocorp.poulpopass.core;

public abstract class PasswordManagerElement {

    IPasswordManager passwordManager;

    protected String name;

    public PasswordManagerElement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IPasswordManager getPasswordManager() {
        return passwordManager;
    }
}