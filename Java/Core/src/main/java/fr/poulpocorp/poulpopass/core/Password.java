package fr.poulpocorp.poulpopass.core;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author PoulpoGaz
 * @author DarkMiMolle
 */
public class Password extends PasswordManagerElement implements IPassword {

    private final List<String> urls;
    private final Set<Category> categories;

    private char[] password;

    /**
     * Creates a new password.
     * Only password managers can create a password
     *
     * @param name the name of this password
     * @param password the password array
     */
    Password(String name, char[] password) {
        super(name);
        this.password = Objects.requireNonNull(password);

        urls = new ArrayList<>();
        categories = new LinkedHashSet<>();
    }

    /**
     * Set the name of this password
     * @param name the new name of this password
     * @return returns {@code false} if:
     *              -the name is {@code null}
     *              -the new name is the same as the old name
     *              -the password manager doesn't contains a password
     *               with the same name
     *         otherwise returns {@code true}
     */
    @Override
    public boolean setName(String name) {
        if (name == null || this.name.equals(name) || passwordManager.containsPasswordWithName(name)) {
            return false;
        }

        this.name = name;

        return true;
    }

    /**
     * @return the name of this password
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the password of this object
     * @param password the new password
     * @return returns {@code false} if:
     *              -the password is {@code null}
     *              -the new password is the same as the old password
     *              -the new password length is 0
     *         otherwise returns {@code true}
     */
    public boolean setPassword(char[] password) {
        if (password != null && !Arrays.equals(this.password, password) && password.length > 0) {
            this.password = password;

            return true;
        }

        return false;
    }

    /**
     * Users can modify the content of the char array
     *
     * @return the password char array
     */
    public char[] getPassword() {
        return password;
    }

    /**
     * Associate this password with the specified category
     * Implementations should update the category set
     *
     * @param category the category with which the password will be associated
     * @return returns {@code true} if this password can be associated with the category
     */
    @Override
    public boolean associateWith(Category category) {
        category.notifyAssociation(this);

        return categories.add(category);
    }

    void notifyAssociation(Category category) {
         categories.add(category);
    }

    /**
     * Dissociate this password with the specified category
     * Implementations should update the category set
     *
     * @param category the category with which the password will be dissociated
     * @return returns {@code true} if this password can be dissociated with the category
     */
    @Override
    public boolean dissociateWith(Category category) {
         category.notifyDissociation(this);

        return categories.remove(category);
    }

    void notifyDissociation(Category category) {
         categories.remove(category);
    }

    /**
     * @return An unmodifiable set of categories with which this password
     *         is associated
     */
    @Override
    public Set<Category> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    /**
     * @return the number of categories with which this password is associated
     */
    @Override
    public int getNumberOfCategories() {
        return categories.size();
    }

    /**
     * Add the specified url to the list
     *
     * @param url the url to add to the list
     */
    @Override
    public void addURL(String url) {
        urls.add(url);
    }

    /**
     * Add the specified url at the specified index to the list
     *
     * @param index the index to add the url
     * @param url the url to add to the list
     */
    @Override
    public void addURL(int index, String url) {
        urls.add(index, url);
    }

    /**
     * Set the specified url at the specified index
     *
     * @param index the index to set the url
     * @param url the url
     */
    @Override
    public void setURL(int index, String url) {
        urls.set(index, url);
    }

    /**
     * Remove the specified url
     *
     * @param url the url to removed
     * @return {@code true} if the url has been removed
     */
    @Override
    public boolean removeURL(String url) {
        return urls.remove(url);
    }

    /**
     * Remove the url at the specified index
     *
     * @param index the index of the url to remove
     */
    @Override
    public void removeURL(int index) {
        urls.remove(index);
    }

    /**
     * Remove all urls
     */
    @Override
    public void removeAllURL() {
        urls.clear();
    }

    /**
     * Replace all urls by the specified array of urls
     *
     * @param urls the new urls
     */
    @Override
    public void setURLs(String[] urls) {
         if (urls != null) {
             this.urls.clear();

             Collections.addAll(this.urls, urls);
         }
    }

    /**
     * Add all urls
     *
     * @param c the collection of urls to add
     */
    @Override
    public void addAll(Collection<? extends String> c) {
        urls.addAll(c);
    }

    /**
     * @return an unmodifiable list of urls
     */
    @Override
    public List<String> getURLs() {
        return Collections.unmodifiableList(urls);
    }

    /**
     * @return the number of urls
     */
    @Override
    public int getNumberOfURL() {
        return urls.size();
    }
}