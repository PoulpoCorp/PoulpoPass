package fr.poulpocorp.poulpopass.app.tag;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Tag {

    protected final JButton button;

    protected String name;
    protected boolean selected;

    public Tag(String name, boolean selected) {
        this.name = name;
        this.selected = selected;

        button = new JButton(name);
        button.setFocusable(false);
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
    }

    public void addActionListener(ActionListener listener) {
        button.addActionListener(listener);
    }

    public void removeActionListener(ActionListener listener) {
        button.removeActionListener(listener);
    }

    public void setName(String name) {
        this.name = name;
        button.setText(name);
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return selected;
    }

    JButton getButton() {
        return button;
    }
}