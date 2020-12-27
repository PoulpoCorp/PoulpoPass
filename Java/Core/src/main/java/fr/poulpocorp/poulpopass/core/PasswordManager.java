package fr.poulpocorp.poulpopass.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * File format
 *
 * n                                        4 bytes, number of categories
 * for each categories:
 *     n2                                   4 bytes, category name length
 *     category                             n2 bytes
 *     end for
 * for each password:
 *     n                                    4 bytes, password name length
 *     password name                        n bytes
 *     n                                    4 bytes, password length
 *     password                             n bytes
 *     n                                    4 bytes, number of urls
 *     for each urls:
 *         n2                               4 bytes, url length
 *         url                              n2 bytes
 *         end for
 *     n                                    4 bytes, number of categories
 *     for each password's categories:
 *         i                                4 bytes, category index
 *         end for
 *     end
 */
public class PasswordManager implements IPasswordManager {

    public static PasswordManager newPasswordManager(Path path, char[] masterPassword) throws IOException, InvalidKeyException, ParseException {
        byte[] data = CryptoUtils.decrypt(Files.readAllBytes(path), masterPassword);

        return new PasswordManager(data, path, masterPassword);
    }

    private Path path;
    private char[] masterPassword;

    private Vector<Password> passwords;
    private Vector<Category> categories;

    public PasswordManager(char[] masterPassword) {
        this.masterPassword = masterPassword;

        path = null;
        passwords = new Vector<>();
        categories = new Vector<>();
    }

    protected PasswordManager(byte[] decryptedFile, Path path, char[] masterPassword) throws ParseException {
        this.path = path;
        this.masterPassword = masterPassword;

        initAfterDecrypt(decryptedFile);
    }

    private int bytesToInt(byte[] str, int from, int to) {
        int return_v = 0;
        for (; from < to; from++) {
            return_v *= 10;
            if (str[from] < '0' || str[from] > '9') {
                //throw new Exception("not a number");
            }
            return_v += Byte.toUnsignedInt(str[from]);
        }
        return return_v;
    }

    private void initAfterDecrypt(byte[] decryptedFile) throws ParseException {
        int idxStart = 0;

        int catNumber = bytesToInt(decryptedFile, idxStart, idxStart += Integer.BYTES);

        categories = new Vector<>(catNumber);
        idxStart = parseCategories(decryptedFile, idxStart, catNumber);
    }

    /**
     * Ajoute les catégories au tableau this.categories.
     *
     * @param file  : le fichier déchiffré.
     * @param from  : index de début pour lire le tableau de byte (file)
     * @return renvoie le nouveau <i>from</i>.
     */
    private int parseCategories(byte[] file, int from, int n) {
        for (int i = 0; i < n; i++) {
            int nameLength = bytesToInt(file, from, from += 4);

            String name = new String(file, from, nameLength);

            categories.add(new Category(name));

            from += nameLength;
        }

        return from;
    }

    public Category getOrCreate(String name) {
        for (Category c : categories) {
            if (c.getName().equals(name)) {
                return c;
            }
        }

        Category category = new Category(name);
        categories.add(category);

        return category;
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
        return masterPassword;
    }

    @Override
    public void setMasterPassword(char[] masterPassword) {
        if (masterPassword != null) {
            this.masterPassword = masterPassword;
        }
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}