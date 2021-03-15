package fr.poulpocorp.poulpopass.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * File format
 *
 * n                                        4 bytes, number of categories
 * for each categories:
 *     n2                                   4 bytes, category name length
 *     category                             n2 bytes
 *     end for
 * n                                        4 bytes, number of passwords
 * for each password:
 *     n2                                   4 bytes, password name length
 *     password name                        n bytes
 *     n2                                   4 bytes, password length
 *     password                             n bytes
 *     n2                                   4 bytes, number of urls
 *     for each urls:
 *         n3                               4 bytes, url length
 *         url                              n2 bytes
 *         end for
 *     n2                                   4 bytes, number of categories
 *     for each password's categories:
 *         i                                4 bytes, category index
 *         end for
 *     end
 *
 * @author PoulpoGaz
 * @author DarkMiMolle
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

    @Override
    public Category getOrCreateCategory(String name) {
        for (Category c : categories) {
            if (c.getName().equals(name)) {
                return c;
            }
        }

        Category category = new Category(name);
        category.passwordManager = this;

        categories.add(category);

        return category;
    }

    @Override
    public int getNumberOfCategories() {
        return categories.size();
    }

    @Override
    public Password getOrCreatePassword(String name, char[] password) {
        Password pass = getPasswordIfExists(name);

        if (pass == null) {
            pass = new Password(name, password);
            pass.passwordManager = this;

            passwords.add(pass);
        }

        return pass;
    }

    @Override
    public Category getCategoryIfExists(String name) {
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }

        return null;
    }

    @Override
    public Password getPasswordIfExists(String name) {
        for (Password password : passwords) {
            if (password.getName().equals(name)) {
                return password;
            }
        }

        return null;
    }

    @Override
    public boolean containsPasswordWithName(String name) {
        return getPasswordIfExists(name) != null;
    }

    @Override
    public boolean containsCategoryWithName(String name) {
        return getCategoryIfExists(name) != null;
    }

    @Override
    public boolean removePassword(Password password) {
        if (passwords.remove(password)) {
            password.passwordManager = null;

            password.getCategories().forEach((category -> {
                category.dissociateWith(password);
            }));

            return true;
        }

        return false;
    }

    @Override
    public boolean removeCategory(Category category) {
        if (categories.remove(category)) {
            category.passwordManager = null;

            category.getPasswords().forEach((password -> {
                password.dissociateWith(category);
            }));

            return true;
        }

        return false;
    }

    @Override
    public int getNumberOfPasswords() {
        return passwords.size();
    }

    @Override
    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    @Override
    public List<Password> getPasswords() {
        return Collections.unmodifiableList(passwords);
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

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public void setPath(Path path) {
        this.path = path;
    }

    /**
     * SAVE
     */

    @Override
    public void save() throws IOException, InvalidKeyException {
        if (path == null || masterPassword == null) {
            throw new NullPointerException("path is null");
        }

        if (masterPassword.length == 0) {
            throw new InvalidKeyException("Master password is too short");
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            writeCategories(os);

            writeInteger(os, passwords.size());
            for (Password password : passwords) {
                writePassword(os, password);
            }

        } catch (IOException ignored) {} // ByteArrayOutputStream never throws IOException

        byte[] encryptedData = CryptoUtils.encrypt(os.toByteArray(), masterPassword);

        Path parent = path.getParent();
        if (parent != null) {
            if (Files.notExists(parent)) {
                Files.createDirectories(path.getParent());
            }
        }

        Files.write(path, encryptedData, StandardOpenOption.CREATE);
    }

    protected void writeCategories(ByteArrayOutputStream os) throws IOException {
        writeInteger(os, categories.size());

        for (Category category : categories) {
            writeString(os, category.getName());
        }
    }

    protected void writePassword(ByteArrayOutputStream os, Password password) throws IOException {
        writeString(os, password.getName());
        writeString(os, String.valueOf(password.getPassword()));

        List<String> urls = password.getURLs();

        writeInteger(os, urls.size());
        for (String url : urls) {
            writeString(os, url);
        }

        Set<Category> categories = password.getCategories();
        writeInteger(os, categories.size());
        for (Category category : categories) {
            writeInteger(os, this.categories.indexOf(category));
        }
    }

    protected void writeString(ByteArrayOutputStream os, String str) throws IOException {
        writeInteger(os, str.length());
        os.write(str.getBytes());
    }

    protected void writeInteger(ByteArrayOutputStream os, int value) {
        os.write((value >> 24) & 0xFF);
        os.write((value >> 16) & 0xFF);
        os.write((value >>  8) & 0xFF);
        os.write( value        & 0xFF);
    }

    /**
     * READ
     */

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

            passwords.add(new Password(name, password.toCharArray()));
            from = parseURLS(file, from, i);
            from = linkCategories(file, from, i);
        }
        return from;
    }

    private void initAfterDecrypt(byte[] decryptedFile) throws ParseException {
        int idxStart = 0, idxEnd = idxStart + 4;
        int catNumber = bytesToInt(decryptedFile, idxStart, idxEnd);

        categories = new Vector<>(catNumber);
        idxStart = idxEnd;
        idxStart = parseCategories(decryptedFile, idxStart);

        int passNumber = bytesToInt(decryptedFile, idxStart, idxStart + 4);
        passwords = new Vector<>(passNumber);

        idxStart += 4;
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
}