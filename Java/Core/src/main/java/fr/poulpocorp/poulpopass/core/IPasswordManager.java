package fr.poulpocorp.poulpopass.core;

import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.util.List;

/**
 * A password manager hold a set of passwords and categories.
 * Passwords and categories can be created using, respectively
 * {@link #getOrCreatePassword(String, char[])} and {@link #getOrCreateCategory(String)}
 *
 *
 * @author PoulpoGaz
 * @author DarkMiMolle
 */
public interface IPasswordManager {

    /**
     * Returns the existing category with the
     * specified name or create a new one
     *
     * @param name the name of the category
     * @return the category
     */
    Category getOrCreateCategory(String name);

    /**
     * Returns the existing password with the
     * specified name or create a new one
     *
     * @param name the name of the password
     * @param password the password char array
     * @return the password
     */
    Password getOrCreatePassword(String name, char[] password);

    /**
     * @param name the name of the category
     * @return the category or {@code null} if
     *          a category with this name doesn't
     *          exist
     */
    Category getCategoryIfExists(String name);

    /**
     * @param name the name of the password
     * @return the password or {@code null} if
     *          a category with this name doesn't
     *          password
     */
    Password getPasswordIfExists(String name);

    /**
     * @param name the name of the password
     * @return {@code true} if the password manager contains
     *          a password with the specified name
     */
    boolean containsPasswordWithName(String name);

    /**
     * @param name the name of the category
     * @return {@code true} if the password manager contains
     *          a category with the specified name
     */
    boolean containsCategoryWithName(String name);

    /**
     * @param password the password to remove
     * @return {@code true} if the password has been removed
     */
    boolean removePassword(Password password);

    /**
     * @param category the category to remove
     * @return {@code true} if the category has been removed
     */
    boolean removeCategory(Category category);

    /**
     * @return the number of categories in this password manager
     */
    int getNumberOfCategories();

    /**
     * @return the number of passwords in this password manager
     */
    int getNumberOfPasswords();

    /**
     * @return an unmodifiable list of category present in this password manager
     */
    List<Category> getCategories();

    /**
     * @return an unmodifiable list of password present in this password manager
     */
    List<Password> getPasswords();

    /**
     * @return the master password with which file is encrypted
     */
    char[] getMasterPassword();

    /**
     * @param masterPassword the new master password
     * @return {@code true} if the specified char array is valid.
     *          A valid char array is not null and have a length
     *          greater than 0
     */
    boolean setMasterPassword(char[] masterPassword);

    /**
     * @return the path where the file is saved
     */
    Path getPath();

    /**
     * @param path the path where the file is saved
     */
    void setPath(Path path);

    /**
     * Save and encrypt this password manager
     *
     * @throws IOException if an I/O exception occurs
     * @throws InvalidKeyException if the specified password is incorrect
     */
    void save() throws IOException, InvalidKeyException;
}
