package fr.poulpocorp.poulpopass.core.search;

import fr.poulpocorp.poulpopass.core.Category;
import fr.poulpocorp.poulpopass.core.Password;
import fr.poulpocorp.poulpopass.core.PasswordManager;
import fr.poulpocorp.poulpopass.core.PasswordManagerElement;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Search {

    public static List<Category> searchInCategory(PasswordManager manager, Filter<Category> filter) {
        return search(manager.getCategories().stream(), filter);
    }

    public static List<Password> searchInPassword(PasswordManager manager, Filter<Password> filter) {
        return search(manager.getPasswords().stream(), filter);
    }

    public static List<PasswordManagerElement> searchEverywhere(PasswordManager manager, Filter<PasswordManagerElement> filter) {
        Stream<PasswordManagerElement> stream = Stream.concat(manager.getCategories().stream(), manager.getPasswords().stream());

        return search(stream, filter);
    }

    private static <T extends PasswordManagerElement> List<T> search(Stream<T> stream, Filter<T> filter) {
        return stream.parallel().filter(filter).collect(Collectors.toList());
    }
}