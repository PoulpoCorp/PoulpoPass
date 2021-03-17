package fr.poulpocorp.poulpopass.app.viewer;

import com.formdev.flatlaf.FlatClientProperties;
import fr.poulpocorp.poulpopass.app.layout.*;
import fr.poulpocorp.poulpopass.app.utils.Icons;
import fr.poulpocorp.poulpopass.app.utils.WrapBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public abstract class AbstractViewer extends JComponent {

    protected final PasswordExplorer explorer;

    protected final ViewerBorder viewerBorder;

    public AbstractViewer(PasswordExplorer explorer) {
        this.explorer = explorer;

        viewerBorder = new ViewerBorder();

        setBorder(new WrapBorder(viewerBorder, BorderFactory.createEmptyBorder(5, 5, 5, 5)));
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

        // edit button
        JButton edit = new JButton(Icons.EDIT);
        edit.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        edit.addActionListener(this::edit);

        constraint.endComponent = false;
        constraint.fillXAxis = false;
        constraint.orientation = VCOrientation.BOTTOM;
        constraint.xAlignment = 1;
        add(edit, constraint);
    }

    protected abstract Component getTopComponent();

    protected abstract void initFields();

    protected abstract void edit(ActionEvent e);

    protected abstract Icon getIcon();

    public ViewerBorder getViewerBorder() {
        return viewerBorder;
    }
}