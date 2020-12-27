package fr.poulpocorp.poulpopass.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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

    private int bytesToInt(byte[] str, int from, int to) throws ParseException {
        int return_v = 0;
        for (; from < to; from++) {
            return_v = return_v << 8;
            return_v |= str[from];
        }
        if (return_v < 0) {
            throw new ParseException("the value must be > 0");
        }
        return return_v;
    }

    private static String bytesToString(byte[] str, int from, int len) {
        return new String(str, from, len);
    }

    private int parseURLS(byte[] file, int from, int idxPwd) throws ParseException {
        Password password = passwords.get(idxPwd);

        int n = bytesToInt(file, from, from + 4);
        from += 4;

        for (int i = 0; i < n; i++) {
            int urlLength = bytesToInt(file, from, from + 4);
            from += 4;

            String url = new String(file, from, urlLength);

            password.addURL(url);

            from += urlLength;
        }

        return from;
    }

    private int linkCategories(byte[] file, int from, int idxPwd) throws ParseException {
        int nbCat = bytesToInt(file, from, from + Integer.BYTES);
        from += Integer.BYTES;

        for (var i = 0; i < nbCat; i++) {
            passwords.get(idxPwd).associateWith(categories.get(bytesToInt(file, from, from + Integer.BYTES)));
            from += Integer.BYTES;
        }
        return from;
    }

    private int parsePasswords(byte[] file, int from) throws ParseException {
        for (var i = 0; i < passwords.capacity(); i++) {
            int nameLen = bytesToInt(file, from, from + Integer.BYTES);
            from += Integer.BYTES;

            String name = bytesToString(file, from, nameLen);
            from += nameLen;

            int passwordLen = bytesToInt(file, from, from + 4);
            from += 4;

            String password = bytesToString(file, from, passwordLen);
            from += passwordLen;

            passwords.set(i, new Password(name, password.toCharArray()));
            from = parseURLS(file, from, i);
            from = linkCategories(file, from, i);
        }
        return from;
    }

    private void initAfterDecrypt(byte[] decryptedFile) throws ParseException {
        int idxStart = 0, idxEnd = idxStart + 4;
        int catNumber = 0;
        try {
            catNumber = bytesToInt(decryptedFile, idxStart, idxEnd);
        } catch (Exception e) {
            // TODO expected the number of categories
        }
        categories = new Vector<>(catNumber);
        idxStart = idxEnd;
        idxStart = parseCategories(decryptedFile, idxStart);
        idxStart = parsePasswords(decryptedFile, idxStart);
    }

    /**
     * Ajoute les catégories au tableau this.categories.
     *
     * @param file  : le fichier déchiffré.
     * @param from  : index de début pour lire le tableau de byte (file)
     * @return renvoie le nouveau <i>from</i>.
     */
    private int parseCategories(byte[] file, int from) throws ParseException {
        int n = categories.capacity();

        for (int i = 0; i < n; i++) {
            int nameLength = bytesToInt(file, from, from + Integer.BYTES);
            from += Integer.BYTES;

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