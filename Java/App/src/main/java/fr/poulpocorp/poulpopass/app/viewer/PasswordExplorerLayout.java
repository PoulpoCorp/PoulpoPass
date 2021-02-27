package fr.poulpocorp.poulpopass.app.viewer;

import java.awt.*;

public class PasswordExplorerLayout implements LayoutManager {

    private static final boolean PREFERRED = true;
    private static final boolean MINIMUM = false;

    private static final int GAP_BETWEEN_COMPONENTS = 5;

    private int numCols = 4;

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return getSize(parent, PREFERRED);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return getSize(parent, MINIMUM);
    }

    protected Dimension getSize(Container parent, boolean preferred) {
        Dimension dim = new Dimension();

        int rowWidth = 0;
        int rowHeight = 0;
        int x = 0;

        Component[] components = parent.getComponents();
        for (int i = 0, count = components.length; i < count; i++) {
            Component comp = components[i];
            Dimension size = getSize(comp, preferred);

            rowWidth += size.width;
            rowHeight = Math.max(rowHeight, size.height);

            if (x == 3) {
                dim.width = Math.max(rowWidth, dim.width) + x * GAP_BETWEEN_COMPONENTS;
                dim.height += rowHeight + GAP_BETWEEN_COMPONENTS;

                x = 0;
                rowWidth = 0;
                rowHeight = 0;
            } else {
                x++;
            }
        }

        if (x != 0) {
            dim.width = Math.max(rowWidth, dim.width) + x * GAP_BETWEEN_COMPONENTS;
            dim.height += rowHeight;
        }

        Insets insets = parent.getInsets();
        dim.width += insets.left + insets.right;
        dim.height += insets.top + insets.bottom;

        return dim;
    }

    protected Dimension getSize(Component component, boolean preferred) {
        if (preferred) {
            return component.getPreferredSize();
        } else {
            return component.getMinimumSize();
        }
    }

    @Override
    public void layoutContainer(Container parent) {
        Dimension dim = parent.getSize();
        Insets insets = parent.getInsets();

        dim.width -= insets.left - insets.right;
        dim.height -= insets.top - insets.bottom;

        int x = insets.left;
        int column = 0;

        int y = insets.top;

        int width = (dim.width - (numCols - 1) * GAP_BETWEEN_COMPONENTS) / numCols;
        int rowHeight = 0;

        Component[] components = parent.getComponents();
        for (int i = 0, count = components.length; i < count; i++) {
            if (column == 0) {
                // new row, compute height
                int max = Math.min(i + numCols, components.length);
                for (int j = i; j < max; j++) {
                    rowHeight = Math.max(rowHeight, components[j].getPreferredSize().height);
                }
            }

            Component component = components[i];

            component.setBounds(x, y, width, rowHeight);

            if (column == 3 || i + 1 == count) {
                x = insets.left;
                y += rowHeight + GAP_BETWEEN_COMPONENTS;

                column = 0;
            } else {
                x += width + GAP_BETWEEN_COMPONENTS;
                column++;
            }
        }
    }

    public int getNumCols() {
        return numCols;
    }

    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }
}