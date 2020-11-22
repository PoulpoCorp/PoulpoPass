package fr.poulpocorp.poulpopass.core;

import java.util.ArrayList;
import java.util.List;

public class Password {

    IPasswordManager passwordManager;

    private String name;
    private List<String> urls;
    private List<Category> categories;

    private char[] password;

    public Password(String name, char[] password) {
        this.name = name;
        this.password = password;

        urls = new ArrayList<>();
        categories = new ArrayList<>();
    }

    public boolean associateWith(Category category) {
        if (!categories.contains(category)) {
            categories.add(category);
            category.associateWith(this);

            if (passwordManager != null) {
                passwordManager.addCategory(category);
            }

            return true;
        }

        return false;
    }

    public boolean dissociateWith(Category category) {
        if (categories.remove(category)) {
            category.dissociateWith(this);

            return true;
        }

        return false;
    }

    public Category getCategoryByName(String name) {
        for (Category category : categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }

        return null;
    }

    public List<Category> getCategories() {
        return categories;
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