package fr.poulpocorp.poulpopass.app.utils;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import fr.poulpocorp.poulpopass.app.text.PPPasswordTextField;

import javax.swing.*;
import java.awt.*;

public class Utils {

    public static PPPasswordTextField createPasswordTextField(char[] password) {
        PPPasswordTextField field = new PPPasswordTextField();
        field.setText(String.valueOf(password));

        JButton see = new JButton(Icons.SEE);
        see.setToolTipText("Show");
        see.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);

        see.addActionListener((a) -> {
            if (field.echoCharIsSet()) {
                field.setEchoChar((char) 0);
                see.setToolTipText("Hide");
            } else {
                field.setEchoChar('•');
                see.setToolTipText("Show");
            }
        });

        field.setTrailingComponent(see);

        return field;
    }

    public static PPPasswordTextField createPasswordLabel(char[] password) {
        PPPasswordTextField field = createPasswordTextField(password);
        field.setEditable(false);
        field.setFocusable(false);
        field.setBorder(null);

        return field;
    }

    /**
     * If theme is dark, apply {@link Color#darker()}
     * If theme is light, apply {@link Color#brighter()}
     * If theme is not light nor black, apply {@link Color#darker()}
     */
    public static Color applyThemeColorFunction(Color color) {
        LookAndFeel laf = UIManager.getLookAndFeel();

        if (laf instanceof FlatLaf) {
            FlatLaf flatLaf = (FlatLaf) laf;

            if (!flatLaf.isDark()) {
                return color.brighter();
            }
        }

        return color.darker();
    }

    /**
     * The opposite of {@link #applyThemeColorFunction(Color)} method
     *
     * If theme is dark, apply {@link Color#brighter()}
     * If theme is light, apply {@link Color#darker()}
     * If theme is not light nor black, apply {@link Color#brighter()}
     */
    public static Color applyOppositeThemeColorFunction(Color color) {
        LookAndFeel laf = UIManager.getLookAndFeel();

        if (laf instanceof FlatLaf) {
            FlatLaf flatLaf = (FlatLaf) laf;

            if (!flatLaf.isDark()) {
                return color.darker();
            }
        }

        return color.brighter();
    }
}