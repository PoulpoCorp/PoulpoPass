package fr.poulpocorp.poulpopass.core;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * File format
 *
 * n                                        4 bytes, number of categories
 * for each categories:
 *     n2                                   4 bytes, category name length
 *     category                             n2 bytes
 * end for
 * for each password:
 *     n                                    4 bytes, password name length
 *     password name                        n bytes
 *     n                                    4 bytes, password length
 *     password                             n bytes
 *     n                                    4 bytes, number of urls
 *     for each urls:
 *         n2                               4 bytes, url length
 *         url                              n2 bytes
 *     end for
 *     n                                    4 bytes, number of categories
 *     for each password's categories:
 *         i                                4 bytes, category index
 *     end for
 * end for
 *
 */
public class PasswordManager implements IPasswordManager {

    private List<Password> passwords;
    private List<Category> categories;
    private char[] masterPassword;

    public PasswordManager(Path path, byte[] masterPassword) {
        passwords = new ArrayList<>();
        categories = new ArrayList<>();

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

    /***
    * @brief parseCategories ajoute les catégories au tableau this.categories.
     * @param file  : le fichier déchiffré.
     * @param from  : index de début pour lire le tableau de byte (file)
     *
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
        return new char[0];
    }

    @Override
    public void setMasterPassword(char[] masterPassword) {
        this.masterPassword = masterPassword;
    }
}