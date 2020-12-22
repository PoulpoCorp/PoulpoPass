package fr.poulpocorp.poulpopass.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

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

    public static PasswordManager newPasswordManager(Path path, char[] masterPassword) throws IOException, InvalidKeyException {
        byte[] data = CryptoUtils.decrypt(Files.readAllBytes(path), masterPassword);

        // parse

        return new PasswordManager(path, masterPassword, null, null);
    }

    private static int idxToNewLine(byte[] str, int from) {
        var idx = from;
        for (; idx < str.length && str[idx] != '\n'; idx++);
        return idx;
    }

    private static int idxToNewLine(byte[] str) {
        return idxToNewLine(str, 0);
    }

    private static int bytesToInt(byte[] str, int from, int to) throws Exception {
        int return_v = 0;
        for (; from < to; from++) {
            return_v *= 10;
            if (str[from] < '0' || str[from] > '9') {
                throw new Exception("not a number");
            }
            return_v += Byte.toUnsignedInt(str[from]);
        }
        return return_v;
    }

    /**
     * Ajoute les catégories au tableau this.categories.
     *
     * @param file  : le fichier déchiffré.
     * @param from  : index de début pour lire le tableau de byte (file)
     * @return renvoie le nouveau <i>from</i>.
     *
     * tips: idxToNewLine(byte[], int from) te renvoie l'index du prochain '\n' après from dans le tableau de byte.
     */
    private int parseCategories(byte[] file, int from) {
        // TODO: Valent
        return 0;
    }

    private void initAfterDecrypt(byte[] decryptedFile) {
        int idxStart = 0, idxEnd = idxToNewLine(decryptedFile);
        int catNumber = 0;
        try {
            catNumber = bytesToInt(decryptedFile, idxStart, idxStart);
        } catch (Exception e) {
            // TODO expected the number of categories
        }
        categories = new ArrayList<>(catNumber);
        idxStart = idxEnd + 1;
        idxStart = parseCategories(decryptedFile, idxStart);
        idxEnd = idxToNewLine(decryptedFile, idxStart);
    }

    private Path path;
    private char[] masterPassword;

    private List<Password> passwords;
    private List<Category> categories;

    public PasswordManager(char[] masterPassword) {
        this.masterPassword = masterPassword;

        path = null;
        passwords = new ArrayList<>();
        categories = new ArrayList<>();
    }

    protected PasswordManager(Path path, char[] masterPassword, List<Password> passwords, List<Category> categories) {
        this.path = path;
        this.masterPassword = masterPassword;
        this.passwords = passwords;
        this.categories = categories;
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
}