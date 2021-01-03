package fr.poulpocorp.poulpopass.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Password extends PasswordManagerElement {

    private Set<String> urls;
    private Set<Category> categories;

    private char[] password;

     Password(String name, char[] password) {
         super(name);
         this.password = password;

         urls = new HashSet<>();
         categories = new HashSet<>();
     }

    public boolean associateWith(Category category) {
        category.notifyAssociation(this);

        return categories.add(category);
    }

    void notifyAssociation(Category category) {
         categories.add(category);
    }

    public boolean dissociateWith(Category category) {
         category.notifyDissociation(this);

        return categories.remove(category);
    }

    void notifyDissociation(Category category) {
         categories.remove(category);
    }

    public Category getCategoryByName(String name) {
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }

        return null;
    }

    public Set<Category> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    public int getNumberOfCategories() {
        return categories.size();
    }

    public void addURL(String url) {
        urls.add(url);
    }

    public void removeURL(String url) {
        urls.remove(url);
    }

    public String[] getURLs() {
        return urls.toArray(new String[0]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public IPasswordManager getPasswordManager() {
        return passwordManager;
    }
}