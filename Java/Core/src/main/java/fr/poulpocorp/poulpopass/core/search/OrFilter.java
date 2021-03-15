package fr.poulpocorp.poulpopass.core.search;

import fr.poulpocorp.poulpopass.core.PasswordManagerElement;

/**
 * @author PoulpoGaz
 */
public class OrFilter<T extends PasswordManagerElement> implements Filter<T> {

    private Filter<T> left;
    private Filter<T> right;

    public OrFilter(Filter<T> left, Filter<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean test(T t) {
        return left.test(t) || right.test(t);
    }

    public Filter<T> getLeft() {
        return left;
    }

    public void setLeft(Filter<T> left) {
        this.left = left;
    }

    public Filter<T> getRight() {
        return right;
    }

    public void setRight(Filter<T> right) {
        this.right = right;
    }
}