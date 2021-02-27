package fr.poulpocorp.poulpopass.app.viewer;

import fr.poulpocorp.poulpopass.app.layout.*;
import fr.poulpocorp.poulpopass.app.utils.WrapBorder;
import fr.poulpocorp.poulpopass.core.PasswordManagerElement;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractViewer<E extends PasswordManagerElement> extends JComponent {

    protected final PasswordExplorer explorer;
    protected E element;

    protected final ViewerBorder viewerBorder;

    public AbstractViewer(PasswordExplorer explorer, E element) {
        this.explorer = explorer;
        this.element = element;

        viewerBorder = new ViewerBorder();

        setBorder(new WrapBorder(viewerBorder, BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        initComponents();
    }

    protected void initComponents() {
        setLayout(new VerticalLayout(5, 5));

        VerticalConstraint constraint = new VerticalConstraint();
        constraint.fillXAxis = true;

        JPanel top = new JPanel();
        top.setLayout(new HorizontalLayout());

        HorizontalConstraint hConstraint = new HorizontalConstraint();
        hConstraint.orientation = HCOrientation.RIGHT;
        hConstraint.yAlignment = 0;

        top.add(getTopComponent());
        top.add(new JLabel(getIcon()), hConstraint);

        add(top, constraint);

        initFields();
    }

    protected abstract Component getTopComponent();

    protected abstract void initFields();

    protected abstract Icon getIcon();

    public ViewerBorder getViewerBorder() {
        return viewerBorder;
    }
}