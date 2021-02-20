package fr.poulpocorp.poulpopass.core;

import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.util.List;

public interface IPasswordManager {

    Category getOrCreateCategory(String name);

    Password getOrCreatePassword(String name, char[] password);

    Category getCategoryIfExists(String name);

    Password getPasswordIfExists(String name);

    boolean containsPasswordWithName(String name);

    boolean containsCategoryWithName(String name);

    boolean removePassword(Password password);

    boolean removeCategory(Category category);

    int getNumberOfCategories();

    int getNumberOfPasswords();

    List<Category> getCategories();

    List<Password> getPasswords();

    char[] getMasterPassword();

    void setMasterPassword(char[] masterPassword);

    Path getPath();

    void setPath(Path path);

    void save() throws IOException, InvalidKeyException;
}
