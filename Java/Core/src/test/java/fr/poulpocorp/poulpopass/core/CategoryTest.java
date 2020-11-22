package fr.poulpocorp.poulpopass.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryTest {

    /**
     * Associate a password with a category
     */
    @Test
    void associateWith1() {
        Password password = new Password("pass", "word".toCharArray());
        Category category = new Category("category");

        assertEquals(0, category.size());
        assertEquals(0, password.getNumberOfCategories());

        category.associateWith(password);
        assertEquals(1, category.size());
        assertEquals(1, password.getNumberOfCategories());
    }

    /**
     * Associate a password with a category.
     * The category is at first added to a password manager.
     * This means that the associateWith method should add the password
     * to the password manager
     */
    @Test
    void associateWith2() {
        PasswordManager manager = new PasswordManager();

        Password password = new Password("pass", "word".toCharArray());
        Category category = new Category("category");

        manager.addCategory(category);

        assertEquals(0, category.size());
        assertEquals(0, password.getNumberOfCategories());
        assertEquals(0, manager.getNumberOfPassword());
        assertEquals(1, manager.getNumberOfCategories());

        category.associateWith(password);
        assertEquals(1, category.size());
        assertEquals(1, password.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPassword());
        assertEquals(1, manager.getNumberOfCategories());
    }

    /**
     * Associate a password with a category.
     * The password is at first added to a password manager.
     * This means that the associateWith method should add the category
     * to the password manager
     */
    @Test
    void associateWith3() {
        PasswordManager manager = new PasswordManager();

        Password password = new Password("pass", "word".toCharArray());
        Category category = new Category("category");

        manager.addPassword(password);

        assertEquals(0, category.size());
        assertEquals(0, password.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPassword());
        assertEquals(0, manager.getNumberOfCategories());

        category.associateWith(password);
        assertEquals(1, category.size());
        assertEquals(1, password.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPassword());
        assertEquals(1, manager.getNumberOfCategories());
    }

    /**
     * Associate a password with a category.
     * The password and the category are added at first to a password manager
     * This means that the associateWith method should add the category
     * to the password manager
     */
    @Test
    void associateWith4() {
        PasswordManager manager = new PasswordManager();

        Password password = new Password("pass", "word".toCharArray());
        Category category = new Category("category");

        manager.addPassword(password);
        manager.addCategory(category);

        assertEquals(0, category.size());
        assertEquals(0, password.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPassword());
        assertEquals(1, manager.getNumberOfCategories());

        category.associateWith(password);
        assertEquals(1, category.size());
        assertEquals(1, password.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPassword());
        assertEquals(1, manager.getNumberOfCategories());
    }

    /**
     * Disassociate a category with a password
     */
    @Test
    void disassociateWith1() {
        Password password = new Password("pass", "word".toCharArray());
        Category category = new Category("category");

        category.associateWith(password);

        assertEquals(1, category.size());
        assertEquals(1, password.getNumberOfCategories());

        category.disassociateWith(password);

        assertEquals(0, category.size());
        assertEquals(0, password.getNumberOfCategories());
    }

    /**
     * Disassociate a category with a password.
     * The category is at first added to a password manager.
     * This means that the disassociateWith method should <strong>not</strong>
     * remove the password to the password manager
     */
    @Test
    void disassociateWith2() {
        PasswordManager manager = new PasswordManager();

        Password password = new Password("pass", "word".toCharArray());
        Category category = new Category("category");

        manager.addCategory(category);
        category.associateWith(password);

        assertEquals(1, category.size());
        assertEquals(1, password.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPassword());
        assertEquals(1, manager.getNumberOfCategories());

        category.disassociateWith(password);
        assertEquals(0, category.size());
        assertEquals(0, password.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPassword());
        assertEquals(1, manager.getNumberOfCategories());
    }

    /**
     * Disassociate a category with a password.
     * The password is at first added to a password manager.
     * This means that the disassociateWith method should <strong>not</strong>
     * remove the password to the password manager
     */
    @Test
    void disassociateWith3() {
        PasswordManager manager = new PasswordManager();

        Password password = new Password("pass", "word".toCharArray());
        Category category = new Category("category");

        manager.addPassword(password);
        category.associateWith(password);

        assertEquals(1, category.size());
        assertEquals(1, password.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPassword());
        assertEquals(1, manager.getNumberOfCategories());

        category.disassociateWith(password);
        assertEquals(0, category.size());
        assertEquals(0, password.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPassword());
        assertEquals(1, manager.getNumberOfCategories());
    }

    /**
     * Disassociate a category with a password.
     * The category and the password are at first added to a password manager.
     * This means that the disassociateWith method should <strong>not</strong>
     * remove the password to the password manager
     */
    @Test
    void disassociateWith4() {
        PasswordManager manager = new PasswordManager();

        Password password = new Password("pass", "word".toCharArray());
        Category category = new Category("category");

        manager.addCategory(category);
        manager.addPassword(password);
        category.associateWith(password);

        assertEquals(1, category.size());
        assertEquals(1, password.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPassword());
        assertEquals(1, manager.getNumberOfCategories());

        category.disassociateWith(password);
        assertEquals(0, category.size());
        assertEquals(0, password.getNumberOfCategories());
        assertEquals(1, manager.getNumberOfPassword());
        assertEquals(1, manager.getNumberOfCategories());
    }
}