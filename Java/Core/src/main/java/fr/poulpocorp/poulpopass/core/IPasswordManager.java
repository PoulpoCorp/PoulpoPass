package fr.poulpocorp.poulpopass.core;

public interface IPasswordManager {

    int getNumberOfCategories();

    int getNumberOfPasswords();

    char[] getMasterPassword();

    void setMasterPassword(char[] masterPassword);
}
