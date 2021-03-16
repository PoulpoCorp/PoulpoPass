package fr.poulpocorp.poulpopass.core;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Implementation should use a {@link java.util.LinkedHashSet} for storing categories
 * and a {@link java.util.List} for storing urls.
 *
 * @author PoulpoGaz
 */
public interface IPassword {

    /**
     * Set the name of this password.
     * @param name the new name of this password
     * @return returns {@code false} if:<br>
     *              -the name is {@code null}<br>
     *              -the new name is the same as the old name<br>
     *              -the password manager doesn't contains a password<br>
     *               with the same name<br>
     *         otherwise returns {@code true}.
     */
    boolean setName(String name);

    /**
     * @return the name of this password
     */
    String getName();

    /**
     * Set the password of this object.
     * @param password the new password
     * @return returns {@code false} if:<br>
     *              -the password is {@code null}<br>
     *              -the new password is the same as the old password<br>
     *              -the new password length is 0<br>
     *         otherwise returns {@code true}.
     */
    boolean setPassword(char[] password);

    /**
     * Users can modify the content of the char array.
     *
     * @return the password char array
     */
    char[] getPassword();

    /**
     * Associate this password with the specified category.
     * Implementations should update the category set.
     *
     * @param category the category with which the password will be associated
     * @return returns {@code true} if this password can be associated with the category.
     */
    boolean associateWith(Category category);

    /**
     * Dissociate this password with the specified category.
     * Implementations should update the category set.
     *
     * @param category the category with which the password will be dissociated
     * @return returns {@code true} if this password can be dissociated with the category.
     */
    boolean dissociateWith(Category category);

    /**
     * @return An unmodifiable set of categories with which this password
     *         is associated
     */
    Set<Category> getCategories();

    /**
     * @return the number of categories with which this password is associated
     */
    int getNumberOfCategories();

    /**
     * Add the specified url to the list.
     *
     * @param url the url to add to the list
     */
    void addURL(String url);

    /**
     * Add the specified url at the specified index to the list.
     *
     * @param index the index to add the url
     * @param url the url to add to the list
     */
    void addURL(int index, String url);

    /**
     * Set the specified url at the specified index.
     *
     * @param index the index to set the url
     * @param url the url
     */
    void setURL(int index, String url);

    /**
     * Remove the specified url.
     *
     * @param url the url to removed
     * @return {@code true} if the url has been removed
     */
    boolean removeURL(String url);

    /**
     * Remove the url at the specified index.
     *
     * @param index the index of the url to remove
     */
    void removeURL(int index);

    /**
     * Remove all urls.
     */
    void removeAllURL();

    /**
     * Add all urls.
     *
     * @param c the collection of urls to add
     */
    void addAll(Collection<? extends String> c);

    /**
     * Replace all urls by the specified array of urls
     *
     * @param urls the new urls.
     *             {@code null} clear the list
     */
    void setURLs(String[] urls);

    /**
     * Replace all urls by the specified list of urls
     *
     * @param urls the new urls.
     *             {@code null} clear the list
     */
    void setURLs(List<String> urls);

    /**
     * @return an unmodifiable list of urls
     */
    List<String> getURLs();

    /**
     * @return the number of urls
     */
    int getNumberOfURL();
}
