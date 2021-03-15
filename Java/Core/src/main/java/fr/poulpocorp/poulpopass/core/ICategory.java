package fr.poulpocorp.poulpopass.core;

import java.util.Set;

/**
 * @author PoulpoGaz
 */
public interface ICategory {

    /**
     * Set the name of this category
     * @param name the new name of this category
     * @return returns {@code false} if:<br>
     *              -the name is {@code null}<br>
     *              -the new name is the same as the old name<br>
     *              -the password manager doesn't contains a category<br>
     *               with the same name<br>
     *         otherwise returns {@code true}.
     */
    boolean setName(String name);

    /**
     * @return the name of this category.
     */
    String getName();

    /**
     * Associate this category with the specified password.
     * Implementations should update the password set.
     *
     * @param password the password with which the category will be associated.
     * @return returns {@code true} if this category can be associated with the password.
     */
    boolean associateWith(Password password);

    /**
     * Dissociate this category with the specified password.
     * Implementations should update the password set.
     *
     * @param password the password with which the category will be dissociated.
     * @return returns {@code true} if this category can be dissociated with the password.
     */
    boolean dissociateWith(Password password);

    /**
     * @return An unmodifiable set of passwords with which this category
     *         is associated
     */
    Set<Password> getPasswords();

    /**
     * @return the number of passwords with which this category is associated
     */
    int getNumberOfPasswords();
}