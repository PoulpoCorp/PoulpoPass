package fr.poulpocorp.poulpopass.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Category extends PasswordManagerElement {

    private Set<Password> passwords;

    Category(String name) {
        super(name);
        passwords = new HashSet<>();
    }

    public boolean associateWith(Password password) {
        password.notifyAssociation(this);

        return passwords.add(password);
    }

    void notifyAssociation(Password password) {
        passwords.add(password);
    }

    public boolean dissociateWith(Password password) {
        password.notifyDissociation(this);

        return passwords.remove(password);
    }

    void notifyDissociation(Password password) {
        passwords.remove(password);
    }

    public Password getPasswordByName(String name) {
        for (Password password : passwords) {
            if (password.getName().equals(name)) {
                return password;
            }
        }

        return null;
    }

    public Set<Password> getPasswords() {
        return Collections.unmodifiableSet(passwords);
    }

    public int size() {
        return passwords.size();
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