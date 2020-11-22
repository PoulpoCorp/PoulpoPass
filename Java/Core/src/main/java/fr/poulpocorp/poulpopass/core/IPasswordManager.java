package fr.poulpocorp.poulpopass.core;

import java.nio.file.Path;

public interface IPasswordManager {

    void open(Path file, char[] masterPassword) throws PasswordManagerException;

    void open(Path file, char[] masterPassword, boolean closeIfOpen) throws PasswordManagerException;

    boolean addCategory(Category category);

    boolean removeCategory(Category category);

    boolean addPassword(Password password);

    boolean removePassword(Password password);

    char[] getMasterPassword();

    void setMasterPassword(char[] masterPassword);

    void close() throws PasswordManagerException;

    void close(Path out) throws PasswordManagerException;

    boolean isOpen();
}
