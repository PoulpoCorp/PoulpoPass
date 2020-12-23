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
            return_v = return_v << 8;
            return_v |= str[from];
        }
        if (return_v < 0) {
            throw new Exception("the value must be > 0");
        }
        return return_v;
    }

    private static String bytesToString(byte[] str, int from, int len) {
        var return_v = "";
        for (; from < len; from++) {
            return_v += Byte.toString(str[from]);
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

    private int parsePasswords(byte[] file, int from) {
        int end = from + 4;
        int nameLen = 0;
        try {
            nameLen = bytesToInt(file, from, end);
            from = end;
        } catch (Exception e) {
            // TODO: expected a number with 4 bytes (0 0 0 0 to 9 9 9 9)
        }
        String name = bytesToString(file, from, nameLen);
        from = nameLen;
        int passwordLen = 0;
        try {
            passwordLen = bytesToInt(file, from, from + 4);
            from = from + 4;
        } catch (Exception e) {
            e.printStackTrace();
        }
        String password = bytesToString(file, from, passwordLen);

        return from;
    }

    private void initAfterDecrypt(byte[] decryptedFile) {
        int idxStart = 0, idxEnd = idxStart + 4;
        int catNumber = 0;
        try {
            catNumber = bytesToInt(decryptedFile, idxStart, idxEnd);
        } catch (Exception e) {
            // TODO expected the number of categories
        }
        categories = new ArrayList<>(catNumber);
        idxStart = idxEnd;
        idxStart = parseCategories(decryptedFile, idxStart);

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