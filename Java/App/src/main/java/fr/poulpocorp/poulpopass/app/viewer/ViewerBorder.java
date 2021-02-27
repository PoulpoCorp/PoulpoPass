package fr.poulpocorp.poulpopass.app.viewer;

import com.formdev.flatlaf.ui.FlatRoundBorder;
import com.formdev.flatlaf.util.Animator;

import javax.swing.*;
import java.awt.*;

public class ViewerBorder extends FlatRoundBorder {

    private static final BorderAnimator ANIMATOR = new BorderAnimator();

    /**
     * The color of the border when the animation start and end
     */
    protected final Color highlightColorIn = UIManager.getColor("ViewerBorder.highlightColorIn");

    /**
     * The color of the border when the animation has reached half
     */
    protected final Color highlightColorHalf = UIManager.getColor("ViewerBorder.highlightColorHalf");

    protected boolean highlight = false;
    protected Color highlightColor;

    @Override
    protected Color getOutlineColor(Component c) {
        if (highlight && highlightColor != null) {
            return highlightColor;
        } else {
            return super.getOutlineColor(c);
        }
    }

    public static void startAnimation(JComponent parent, ViewerBorder border) {
        if (ANIMATOR.isRunning()) {
            ANIMATOR.stop();
        }

        if (border == null || parent == null) {
            return;
        }

        ANIMATOR.setTarget(parent, border);
        ANIMATOR.start();
    }

    protected static class BorderAnimator extends Animator implements Animator.TimingTarget {

        private static final int ANIMATION_DURATION = 1500; // 1 sec
        private static final int RESOLUTION = 50; // every 50 ms

        private Component parent;
        private ViewerBorder target;

        protected BorderAnimator() {
            super(ANIMATION_DURATION);
            setResolution(RESOLUTION);
            addTarget(this);
        }

        @Override
        public void begin() {
            target.highlight = true;
            target.highlightColor = target.highlightColorIn;
            parent.repaint();
        }

        @Override
        public void timingEvent(float fraction) {
            float t;

            if (fraction <= 0.5) {
                t = fraction * 2;
            } else { // reverse
                // t = 1 - (fraction - 0.5f) * 2;
                // t = 1 - 2 * fraction + 1;
                t = 2 * (1 - fraction);
            }

            Color in = target.highlightColorIn;
            Color half = target.highlightColorHalf;

            target.highlightColor = new Color(
                    lerp(in.getRed(), half.getRed(), t),
                    lerp(in.getGreen(), half.getGreen(), t),
                    lerp(in.getBlue(), half.getBlue(), t));
            parent.repaint();
        }

        private int lerp(int a, int b, float t) {
            return (int) (a + t * (b - a));
        }

        @Override
        public void end() {
            target.highlight = false;
            target.highlightColor = null;

            parent.repaint();
        }

        public void setTarget(Component parent, ViewerBorder target) {
            this.parent = parent;
            this.target = target;
        }
    }
}