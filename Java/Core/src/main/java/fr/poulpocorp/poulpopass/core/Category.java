package fr.poulpocorp.poulpopass.core;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author PoulpoGaz
 * @author DarkMiMolle
 */
public class Category extends PasswordManagerElement implements ICategory {

    private Set<Password> passwords;

    /**
     * Creates a new category.
     * Only password managers can create a category
     *
     * @param name the name of this category
     */
    Category(String name) {
        super(name);
        passwords = new LinkedHashSet<>();
    }

    @Override
    public boolean setName(String name) {
        if (name == null || this.name.equals(name) || passwordManager.containsCategoryWithName(name)) {
            return false;
        }

        this.name = name;

        return true;
    }

    @Override
    public boolean associateWith(Password password) {
        password.notifyAssociation(this);

        return passwords.add(password);
    }

    void notifyAssociation(Password password) {
        passwords.add(password);
    }

    @Override
    public boolean dissociateWith(Password password) {
        password.notifyDissociation(this);

        return passwords.remove(password);
    }

    void notifyDissociation(Password password) {
        passwords.remove(password);
    }

    @Override
    public Set<Password> getPasswords() {
        return Collections.unmodifiableSet(passwords);
    }

    @Override
    public int getNumberOfPasswords() {
        return passwords.size();
    }
}