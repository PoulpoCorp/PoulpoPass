package fr.poulpocorp.poulpopass.app.viewer;

import com.formdev.flatlaf.FlatClientProperties;
import fr.poulpocorp.poulpopass.app.layout.*;
import fr.poulpocorp.poulpopass.app.model.Model;
import fr.poulpocorp.poulpopass.app.tag.JTagComponent;
import fr.poulpocorp.poulpopass.app.utils.Icons;
import fr.poulpocorp.poulpopass.app.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public abstract class AbstractEditDialog<MODEL extends Model> extends JDialog {

    protected final MODEL model;

    protected JScrollPane scrollPane;
    protected JPanel content;

    protected JTextField nameField;
    protected JTagComponent associations;

    protected JPanel bottomPanel;

    public AbstractEditDialog(Frame parent, String name, MODEL model) {
        super(parent, name, true);

        this.model = model;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    /**
     * Layout description
     *
     * JPanel mainContent with BorderLayout
     *   -> JScrollPane scrollPane
     *        -> JPanel content with VerticalLayout
     *             -> JLabel "Name"
     *             -> PPTextField nameField
     *             -> Separator
     *             -> fields added by the {@link #initFields(JPanel, VerticalConstraint, Color)} method
     *             -> JTagComponents associations
     *   -> JPanel bottomPanel with HorizontalLayout
     *        -> JButton saveButton (at right)
     */
    private void initComponents() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        content = new JPanel();
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.setLayout(new VerticalLayout(5, 5));

        // Name
        JLabel nameLabel = new JLabel("Name");
        Color titleLabelColor = Utils.applyThemeColorFunction(nameLabel.getForeground());

        nameLabel.setForeground(titleLabelColor);

        nameField = new JTextField(getModelName());

        // Bottom panel
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new HorizontalLayout(3, 3));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Save
        JButton save = new JButton(Icons.SAVE);
        save.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        save.addActionListener(this::save);

        // Layout
        VerticalConstraint constraint = new VerticalConstraint();

        // name
        constraint.xAlignment = 0;
        content.add(nameLabel, constraint);
        constraint.fillXAxis = true;
        content.add(nameField, constraint);

        content.add(new JSeparator());

        initFields(content, constraint, titleLabelColor);

        associations = new JTagComponent();
        fillAssociations();

        constraint.xAlignment = 0;
        constraint.endComponent = true;
        constraint.fillXAxis = false;
        content.add(associations, constraint);

        scrollPane.setViewportView(content);

        // Bottom layout
        // Save button
        HorizontalConstraint hConstraint = new HorizontalConstraint();
        hConstraint.orientation = HCOrientation.RIGHT;

        // Add in reverse order
        bottomPanel.add(save, hConstraint);
        bottomPanel.add(new JLabel("Save"), hConstraint);

        // Finish layout
        mainContent.add(scrollPane, BorderLayout.CENTER);
        mainContent.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainContent);
    }

    protected abstract String getModelName();

    protected abstract void initFields(JPanel content, VerticalConstraint constraint, Color titleLabelColor);

    protected abstract void fillAssociations();

    protected abstract void save(ActionEvent actionEvent);
}