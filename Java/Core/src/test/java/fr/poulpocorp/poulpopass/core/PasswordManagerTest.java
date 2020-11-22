package fr.poulpocorp.poulpopass.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordManagerTest {

    /**
     * Add a category to a password manager
     */
    @Test
    void addCategory1() {
        PasswordManager manager = new PasswordManager();

        assertEquals(0, manager.getNumberOfCategories());
        manager.addCategory(new Category("category"));
        assertEquals(1, manager.getNumberOfCategories());
    }

    /**
     * Add a category which is associated width a password to a password manager
     */
    @Test
    void addCategory2() {
        PasswordManager manager = new PasswordManager();

        Category category = new Category("category");
        category.associateWith(new Password("pass", "word".toCharArray()));

        assertEquals(0, manager.getNumberOfCategories());
        assertEquals(0, manager.getNumberOfPasswords());

        manager.addCategory(category);

        assertEquals(1, manager.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPasswords());
    }

    /**
     * Add a category which is associated width a password
     * which is associated with another category to a password manager
     */
    @Test
    void addCategory3() {
        PasswordManager manager = new PasswordManager();

        Password password = new Password("pass", "word".toCharArray());
        Category category1 = new Category("category1");
        Category category2 = new Category("category2");

        password.associateWith(category1);
        password.associateWith(category2);

        assertEquals(0, manager.getNumberOfCategories());
        assertEquals(0, manager.getNumberOfPasswords());

        manager.addCategory(category1);

        assertEquals(2, manager.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPasswords());
    }

    /**
     * Remove a category from a password manager
     */
    @Test
    void removeCategory1() {
        PasswordManager manager = new PasswordManager();

        Category category = new Category("category");

        manager.addCategory(category);

        assertEquals(1, manager.getNumberOfCategories());
        manager.removeCategory(category);
        assertEquals(0, manager.getNumberOfCategories());
    }

    /**
     * Remove a category which is associated with a password from a password manager
     */
    @Test
    void removeCategory2() {
        PasswordManager manager = new PasswordManager();

        Category category = new Category("category");
        category.associateWith(new Password("pass", "word".toCharArray()));

        manager.addCategory(category);

        assertEquals(1, manager.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPasswords());
        assertEquals(1, category.size());
        manager.removeCategory(category);
        assertEquals(0, manager.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPasswords());
        assertEquals(0, category.size());
    }


    /**
     * Add a password to a password manager
     */
    @Test
    void addPassword1() {
        PasswordManager manager = new PasswordManager();

        assertEquals(0, manager.getNumberOfPasswords());
        manager.addPassword(new Password("pass", "word".toCharArray()));
        assertEquals(1, manager.getNumberOfPasswords());
    }

    /**
     * Add a password which is associated width a category to a password manager
     */
    @Test
    void addPassword2() {
        PasswordManager manager = new PasswordManager();

        Password password = new Password("pass", "word".toCharArray());
        password.associateWith(new Category("category"));

        assertEquals(0, manager.getNumberOfCategories());
        assertEquals(0, manager.getNumberOfPasswords());

        manager.addPassword(password);

        assertEquals(1, manager.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPasswords());
    }

    /**
     * Add a password which is associated width a category
     * which is associated with another password to a password manager
     */
    @Test
    void addPassword3() {
        PasswordManager manager = new PasswordManager();

        Category category = new Category("category");
        Password password1 = new Password("pass", "word".toCharArray());
        Password password2 = new Password("pass", "word".toCharArray());

        category.associateWith(password1);
        category.associateWith(password2);

        assertEquals(0, manager.getNumberOfCategories());
        assertEquals(0, manager.getNumberOfPasswords());

        manager.addPassword(password1);

        assertEquals(1, manager.getNumberOfCategories());
        assertEquals(2, manager.getNumberOfPasswords());
    }

    /**
     * Remove a password from a password manager
     */
    @Test
    void removePassword1() {
        PasswordManager manager = new PasswordManager();

        Password password = new Password("pass", "word".toCharArray());

        manager.addPassword(password);

        assertEquals(1, manager.getNumberOfPasswords());
        manager.removePassword(password);
        assertEquals(0, manager.getNumberOfPasswords());
    }

    /**
     * Remove a password which is associated with a category from a password manager
     */
    @Test
    void removePassword2() {
        PasswordManager manager = new PasswordManager();

        Password password = new Password("pass", "word".toCharArray());
        password.associateWith(new Category("category"));

        manager.addPassword(password);

        assertEquals(1, manager.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPasswords());
        assertEquals(1, password.getNumberOfCategories());
        manager.removePassword(password);
        assertEquals(1, manager.getNumberOfCategories());
        assertEquals(0, manager.getNumberOfPasswords());
        assertEquals(0, password.getNumberOfCategories());
    }
}