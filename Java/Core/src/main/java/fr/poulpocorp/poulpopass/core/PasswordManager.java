package fr.poulpocorp.poulpopass.core;

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
    private char[] masterPassword;

    public PasswordManager() {
        passwords = new ArrayList<>();
        categories = new ArrayList<>();
    }

    @Override
    public int getNumberOfCategories() {
        return categories.size();
    }

    @Override
    public int getNumberOfPasswords() {
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
}