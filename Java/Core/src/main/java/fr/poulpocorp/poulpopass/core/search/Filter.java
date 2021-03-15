package fr.poulpocorp.poulpopass.core.search;

import fr.poulpocorp.poulpopass.core.PasswordManagerElement;

import java.util.function.Predicate;

/**
 * @author PoulpoGaz
 */
@FunctionalInterface
public interface Filter<T extends PasswordManagerElement> extends Predicate<T> {

}