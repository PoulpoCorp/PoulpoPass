package fr.poulpocorp.poulpopass.app.text;

import com.formdev.flatlaf.ui.FlatPanelUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

public class ErrorTextFieldUI extends FlatPanelUI {

    private boolean defaultInitialized = false;

    protected Color errorBorderColor;
    protected Color warningBorderColor;

    public static ComponentUI createUI(JComponent c) {
        return new ErrorTextFieldUI();
    }

    @Override
    protected void installDefaults(JPanel p) {
        super.installDefaults(p);

        if (!defaultInitialized) {
            errorBorderColor = UIManager.getColor("Component.error.borderColor");
            warningBorderColor = UIManager.getColor("Component.warning.borderColor");

            defaultInitialized = true;
        }
    }

    public Color getErrorBorderColor() {
        return errorBorderColor;
    }

    public Color getWarningBorderColor() {
        return warningBorderColor;
    }
}