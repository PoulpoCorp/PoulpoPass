package fr.poulpocorp.poulpopass.core;

import javax.xml.catalog.Catalog;
import java.util.List;

public interface IPasswordManager {

    Category getOrCreateCategory(String name);

    int getNumberOfCategories();

    Password getOrCreatePassword(String name, char[] password);

    int getNumberOfPasswords();

    List<Category> getCategories();

    List<Password> getPasswords();

    char[] getMasterPassword();

    void setMasterPassword(char[] masterPassword);
}
