package fr.poulpocorp.poulpopass.app.text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JLabelLink extends JLabel {

    private Color linkColor;

    private boolean underlined = false;
    private Rectangle textRectangle;

    public JLabelLink(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        init();
    }

    public JLabelLink(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        init();
    }

    public JLabelLink(String text) {
        super(text);
        init();
    }

    public JLabelLink(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        init();
    }

    public JLabelLink(Icon image) {
        super(image);
        init();
    }

    public JLabelLink() {
        init();
    }

    private void init() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireActionListener();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                underlined = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                underlined = false;
                repaint();
            }
        });

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForeground(getTextColor());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getText() != null && underlined) {
            g.setColor(getTextColor());

            Rectangle bounds = getTextBounds();

            int lineY = getUI().getBaseline(this, getWidth(), getHeight()) + 1;
            g.drawLine(bounds.x, lineY, bounds.x + bounds.width, lineY);
        }
    }

    public Color getTextColor() {
        if (linkColor == null) {
            linkColor = UIManager.getColor("Component.linkColor");
        }

        return linkColor;
    }

    private Rectangle getTextBounds() {
        if (textRectangle == null) {
            computeLayoutRectangle();
        }

        return textRectangle;
    }

    private void computeLayoutRectangle() {
        final Insets insets = getInsets(null);

        textRectangle = new Rectangle();

        Rectangle viewRectangle = new Rectangle();
        viewRectangle.x = insets.left;
        viewRectangle.y = insets.top;
        viewRectangle.width = getWidth() - (insets.left + insets.right);
        viewRectangle.height = getHeight() - (insets.top + insets.bottom);

        SwingUtilities.layoutCompoundLabel(this,
                getFontMetrics(getFont()),
                getText(),
                isEnabled() ? getIcon() : getDisabledIcon(),
                getVerticalAlignment(),
                getHorizontalAlignment(),
                getVerticalTextPosition(),
                getHorizontalTextPosition(),
                viewRectangle,
                new Rectangle(),
                textRectangle,
                getIconTextGap());
    }

    private void fireActionListener() {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);

        ActionEvent event = null;
        for (ActionListener listener : listeners) {
            if (event == null) {
                event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getText());
            }

            listener.actionPerformed(event);
        }
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        textRectangle = null;
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }
}