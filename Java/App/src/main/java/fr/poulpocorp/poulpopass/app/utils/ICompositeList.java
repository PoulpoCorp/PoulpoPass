package fr.poulpocorp.poulpopass.app.utils;

import java.util.List;
import java.util.function.Consumer;

public interface ICompositeList<E> extends List<E> {

    void addFirst(E e);

    void addSecond(E e);

    int firstSize();

    int secondSize();

    List<E> getFirst();

    List<E> getSecond();

    void forEachInFirstList(Consumer<? super E> action);

    void forEachInSecondList(Consumer<? super E> action);
}