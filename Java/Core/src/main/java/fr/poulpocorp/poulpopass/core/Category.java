package fr.poulpocorp.poulpopass.core;

import java.util.ArrayList;
import java.util.List;

public class Category {

    IPasswordManager passwordManager;

    private String name;
    private List<Password> passwords;

    public Category(String name) {
        this.name = name;
        passwords = new ArrayList<>();
    }

    public boolean addPassword(Password password) {
        if (!passwords.contains(password)) {
            passwords.add(password);
            password.addCategory(this);

            if (passwordManager != null) {
                passwordManager.addPassword(password);
            }

            return true;
        }

        return false;
    }

    public boolean removePassword(Password password) {
        if (passwords.remove(password)) {
            password.removeCategory(this);

            if (passwordManager != null) {
                passwordManager.removePassword(password);
            }

            return true;
        }

        return false;
    }

    public Password getPasswordByName(String name) {
        for (Password password : passwords) {
            if (password.getName().equals(name)) {
                return password;
            }
        }

        return null;
    }

    public List<Password> getPasswords() {
        return passwords;
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