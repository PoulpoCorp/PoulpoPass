package fr.poulpocorp.poulpopass.core;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * File format
 *
 * for each categories:
 *     n2                       4 bytes, category name length
 *     category                 n2 bytes
 *     end
 * for each password:
 *     n                            4 bytes, password name length
 *     password name                n bytes
 *     n                            4 bytes, password length
 *     password                     n bytes
 *     n                            4 bytes, number of urls
 *     for each urls:
 *         n2                       4 bytes, url length
 *         url                      n2 bytes
 *         end
 *     n                            4 bytes, number of categories
 *     for each password's categories:
 *         i                        4 bytes, category index
 *         end
 *     end
 */
public class PasswordManager implements IPasswordManager{

    private final List<Password> passwords;
    private final List<Category> categories;

    private Path file;
    private char[] masterPassword;

    private boolean open = false;

    public PasswordManager() {
        passwords = new ArrayList<>();
        categories = new ArrayList<>();
    }

    @Override
    public void open(Path file, char[] password) throws PasswordManagerException {

    }

    @Override
    public void open(Path file, char[] password, boolean closeIfOpen) throws PasswordManagerException {

    }

    @Override
    public boolean addCategory(Category category) {
        if (!categories.contains(category)) {
            if (category.passwordManager != null) {
                category.passwordManager.removeCategory(category);
            }

            category.passwordManager = this;

            categories.add(category);

            List<Password> passwords = category.getPasswords();

            for (Password password : passwords) {
                addPassword(password);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean removeCategory(Category category) {
        if (categories.remove(category)) {
            category.passwordManager = null;

            for (Password password : category.getPasswords()) {
                password.dissociateWith(category);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean addPassword(Password password) {
        if (!passwords.contains(password)) {
            if (password.passwordManager != null) {
                password.passwordManager.removePassword(password);
            }

            password.passwordManager = this;

            passwords.add(password);

            List<Category> categories = password.getCategories();

            for (Category category : categories) {
                addCategory(category);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean removePassword(Password password) {
        if (passwords.remove(password)) {
            password.passwordManager = null;

            for (Category category : password.getCategories()) {
                category.dissociateWith(password);
            }

            return true;
        }

        return false;
    }

    @Override
    public int getNumberOfCategories() {
        return categories.size();
    }

    @Override
    public int getNumberOfPassword() {
        return passwords.size();
    }

    @Override
    public char[] getMasterPassword() {
        return new char[0];
    }

    @Override
    public void setMasterPassword(char[] masterPassword) {
        this.masterPassword = masterPassword;
    }

    @Override
    public void close() throws PasswordManagerException {

    }

    @Override
    public void close(Path out) throws PasswordManagerException {

    }

    @Override
    public boolean isOpen() {
        return false;
    }
}