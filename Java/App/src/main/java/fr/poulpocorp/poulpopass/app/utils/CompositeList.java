package fr.poulpocorp.poulpopass.app.utils;

import java.util.AbstractList;
import java.util.List;
import java.util.function.Consumer;

public class CompositeList<E> extends AbstractList<E> implements ICompositeList<E> {

    private final List<E> list1;
    private final List<E> list2;

    public CompositeList(List<E> list1, List<E> list2) {
        this.list1 = list1;
        this.list2 = list2;
    }

    @Override
    public E get(int index) {
        if (index < list1.size()) {
            return list1.get(index);
        }

        return list2.get(index - list1.size());
    }

    @Override
    public boolean remove(Object o) {
        return list1.remove(o) || list2.remove(o);
    }

    @Override
    public E remove(int index) {
        if (index < list1.size()) {
            return list1.remove(index);
        }

        return list2.remove(index - list1.size());
    }

    @Override
    public int size() {
        return list1.size() + list2.size();
    }

    @Override
    public void addFirst(E e) {
        list1.add(e);
    }

    @Override
    public void addSecond(E e) {
        list2.add(e);
    }

    @Override
    public int firstSize() {
        return list1.size();
    }

    @Override
    public int secondSize() {
        return list2.size();
    }

    @Override
    public List<E> getFirst() {
        return list1;
    }

    @Override
    public List<E> getSecond() {
        return list2;
    }

    @Override
    public void forEachInFirstList(Consumer<? super E> action) {
        list1.forEach(action);
    }

    @Override
    public void forEachInSecondList(Consumer<? super E> action) {
        list2.forEach(action);
    }
}