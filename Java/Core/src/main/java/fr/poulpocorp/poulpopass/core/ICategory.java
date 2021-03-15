package fr.poulpocorp.poulpopass.core;

import java.util.Set;

/**
 * @author PoulpoGaz
 */
public interface ICategory {

    boolean setName(String name);

    String getName();

    boolean associateWith(Password password);

    boolean dissociateWith(Password password);

    Set<Password> getPasswords();

    int getNumberOfPasswords();
}