package fr.poulpocorp.poulpopass.app.viewer;

import com.formdev.flatlaf.ui.FlatRoundBorder;
import fr.poulpocorp.poulpopass.app.utils.WrapBorder;

import javax.swing.*;

public abstract class AbstractPasswordViewer extends JComponent {

    private final PasswordExplorer explorer;

    public AbstractPasswordViewer(PasswordExplorer explorer) {
        this.explorer = explorer;

        setBorder(new WrapBorder(new FlatRoundBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    protected abstract void initComponents();
}