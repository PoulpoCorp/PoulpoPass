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

    public boolean associateWith(Password password) {
        return false;
    }

    public boolean dissociateWith(Password password) {
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