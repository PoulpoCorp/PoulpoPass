package fr.poulpocorp.poulpopass.app.utils;

import javax.swing.border.Border;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class WrapBorder implements Border, List<Border> {

    private final ArrayList<Border> borders;

    public WrapBorder(Border... borders) {
        this.borders = Arrays.stream(borders).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        for(Border border: borders) {
            border.paintBorder(c, g, x, y, width, height);

            Insets insets = border.getBorderInsets(c);

            x += insets.left;
            y += insets.top;

            width = width - insets.left - insets.right;
            height = height - insets.top - insets.bottom;
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        Insets insets = new Insets(0, 0, 0, 0);

        for(Border border: borders) {
            Insets borderInsets = border.getBorderInsets(c);

            insets.top += borderInsets.top;
            insets.left += borderInsets.left;
            insets.bottom += borderInsets.bottom;
            insets.right += borderInsets.right;
        }

        return insets;
    }

    @Override
    public boolean isBorderOpaque() {
        for(Border border: borders) {
            if(border.isBorderOpaque()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int size() {
        return borders.size();
    }

    @Override
    public boolean isEmpty() {
        return borders.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return borders.contains(o);
    }

    @Override
    public Iterator<Border> iterator() {
        return borders.iterator();
    }

    @Override
    public Object[] toArray() {
        return borders.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return borders.toArray(a);
    }

    @Override
    public boolean add(Border border) {
        return borders.add(border);
    }

    @Override
    public boolean remove(Object o) {
        return borders.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return borders.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Border> c) {
        return borders.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Border> c) {
        return borders.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return borders.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return borders.retainAll(c);
    }

    @Override
    public void clear() {
        borders.clear();
    }

    @Override
    public Border get(int index) {
        return borders.get(index);
    }

    @Override
    public Border set(int index, Border element) {
        return borders.set(index, element);
    }

    @Override
    public void add(int index, Border element) {
        borders.add(index, element);
    }

    @Override
    public Border remove(int index) {
        return borders.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return borders.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return borders.lastIndexOf(o);
    }

    @Override
    public ListIterator<Border> listIterator() {
        return borders.listIterator();
    }

    @Override
    public ListIterator<Border> listIterator(int index) {
        return borders.listIterator(index);
    }

    @Override
    public List<Border> subList(int fromIndex, int toIndex) {
        return borders.subList(fromIndex, toIndex);
    }
}