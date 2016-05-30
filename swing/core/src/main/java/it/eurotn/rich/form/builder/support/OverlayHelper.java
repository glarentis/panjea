package it.eurotn.rich.form.builder.support;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * A helper class that attaches one component (the overlay) on top of another component.
 *
 * @author oliverh
 */
public class OverlayHelper implements SwingConstants {
    final class OverlayChangeHandler implements ComponentListener, PropertyChangeListener {
        @Override
        public void componentHidden(ComponentEvent event) {
            hideOverlay();
        }

        @Override
        public void componentMoved(ComponentEvent event) {
            // ignore
        }

        @Override
        public void componentResized(ComponentEvent event) {
            // ignore
        }

        @Override
        public void componentShown(ComponentEvent event) {
            updateOverlay();
        }

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if ("ancestor".equals(event.getPropertyName()) || "layeredContainerLayer".equals(event.getPropertyName())) {
                return;
            }
            updateOverlay();
        }
    }

    class OverlayTargetChangeHandler implements HierarchyListener, HierarchyBoundsListener, ComponentListener {
        @Override
        public void ancestorMoved(HierarchyEvent event) {
            updateOverlay();
        }

        @Override
        public void ancestorResized(HierarchyEvent event) {
            updateOverlay();
        }

        @Override
        public void componentHidden(ComponentEvent event) {
            hideOverlay();
        }

        @Override
        public void componentMoved(ComponentEvent event) {
            updateOverlay();
        }

        @Override
        public void componentResized(ComponentEvent event) {
            updateOverlay();
        }

        @Override
        public void componentShown(ComponentEvent event) {
            updateOverlay();
        }

        @Override
        public void hierarchyChanged(HierarchyEvent event) {
            updateOverlay();
        }
    }

    class OverlayUpdater implements Runnable {
        @Override
        public void run() {
            try {
                Container overlayCapableParent = getOverlayCapableParent(overlayTarget);
                if (overlayCapableParent == null || !overlayTarget.isShowing() || !overlay.isVisible()) {
                    hideOverlay();
                } else {
                    JLayeredPane layeredPane = getLayeredPane(overlayCapableParent);
                    if (layeredPane.isVisible() && layeredPane.isShowing()) {
                        putOverlay(layeredPane);
                        positionOverlay(layeredPane);
                    }
                }
            } finally {
                isUpdating = false;
            }
        }
    }

    public static class SingleComponentLayoutManager implements LayoutManager {
        private final Component singleComponent;

        /**
         *
         * @param singleComponent
         *            simgleComponent
         */
        public SingleComponentLayoutManager(final Component singleComponent) {
            this.singleComponent = singleComponent;
        }

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void layoutContainer(Container parent) {
            // Fix 5/12/06 AlD: we don't need to base this on the
            // preferred size of the singleComponent or the extentSize
            // of the viewport because the viewport will have already resized
            // the JLayeredPane and taken everything else into consideration.
            // It will have also honored the Scrollable flags, which is
            // something the original code here did not do.
            singleComponent.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return singleComponent.getMinimumSize();
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return singleComponent.getPreferredSize();
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }
    }

    private final OverlayTargetChangeHandler overlayTargetChangeHandler = new OverlayTargetChangeHandler();

    private final OverlayChangeHandler overlayChangeHandler = new OverlayChangeHandler();

    protected JComponent overlay;

    protected final JComponent overlayClipper;

    protected final JComponent overlayTarget;

    private final int center;

    private final int xoffset;

    private final int yoffset;

    boolean isUpdating;

    private final Runnable overlayUpdater = new OverlayUpdater();

    protected OverlayHelper(final JComponent overlay, final JComponent overlayTarget, final int center,
            final int xoffset, final int yoffset) {
        this.overlay = overlay;
        this.overlayTarget = overlayTarget;
        this.center = center;
        this.xoffset = xoffset;
        this.yoffset = yoffset;
        this.overlayClipper = new JPanel();
        this.overlayClipper.setLayout(null);
        this.overlayClipper.add(overlay);
        this.overlayClipper.setOpaque(false);
        installListeners();
    }

    /**
     * Attaches an overlay to the specified component.
     *
     * @param overlay
     *            the overlay component
     * @param overlayTarget
     *            the component over which <code>overlay</code> will be attached
     * @param center
     *            position relative to <code>overlayTarget</code> that overlay should be centered.
     *            May be one of the <code>SwingConstants</code> compass positions or
     *            <code>SwingConstants.CENTER</code>.
     * @param xoffset
     *            x offset from center
     * @param yOffset
     *            y offset from center
     * @return
     *
     * @see SwingConstants
     */
    public static OverlayHelper attachOverlay(JComponent overlay, JComponent overlayTarget, int center, int xoffset,
            int yoffset) {
        return new OverlayHelper(overlay, overlayTarget, center, xoffset, yoffset);
    }

    public void dispose() {
        overlayTarget.removeHierarchyListener(overlayTargetChangeHandler);
        overlayTarget.removeHierarchyBoundsListener(overlayTargetChangeHandler);
        overlayTarget.removeComponentListener(overlayTargetChangeHandler);
        overlay.removeComponentListener(overlayChangeHandler);
        overlay.removePropertyChangeListener(overlayChangeHandler);
    }

    /**
     * Searches up the component hierarchy to find the largest possible visible rect that can
     * enclose the entire rectangle.
     *
     * @param overlayRect
     *            rectangle whose largest enclosing visible rect to find
     * @return largest enclosing visible rect for the specified rectangle
     */
    protected Rectangle findLargestVisibleRectFor(final Rectangle overlayRect) {
        Rectangle visibleRect = null;
        int curxoffset = 0;
        int curyoffset = 0;
        for (JComponent comp = overlayTarget; comp != null && !(comp instanceof JViewport)
                && !(comp instanceof JScrollPane); comp = comp.getParent() instanceof JComponent
                        ? (JComponent) comp.getParent() : null) {
            visibleRect = comp.getVisibleRect();
            visibleRect.x -= curxoffset;
            visibleRect.y -= curyoffset;
            if (visibleRect.contains(overlayRect)) {
                return visibleRect;
            }
            curxoffset += comp.getX();
            curyoffset += comp.getY();
        }
        return visibleRect;
    }

    protected JLayeredPane getLayeredPane(Container overlayCapableParent) {
        if (overlayCapableParent instanceof JRootPane) {
            return ((JRootPane) overlayCapableParent).getLayeredPane();
        } else {
            throw new IllegalArgumentException(
                    "Don't know how to handle parent of type [" + overlayCapableParent.getClass().getName() + "].");
        }
    }

    protected Container getOverlayCapableParent(JComponent component) {
        Container overlayCapableParent = component.getParent();
        while (overlayCapableParent != null && !(overlayCapableParent instanceof JRootPane)) {
            overlayCapableParent = overlayCapableParent.getParent();
        }
        return overlayCapableParent;
    }

    /**
     * @return Returns the overlay.
     */
    public JComponent getOverlayComponent() {
        return overlay;
    }

    public void hideOverlay() {
        setOverlayBounds(new Rectangle(0, 0, 0, 0), 0, 0);
    }

    private void installListeners() {
        overlayTarget.addHierarchyListener(overlayTargetChangeHandler);
        overlayTarget.addHierarchyBoundsListener(overlayTargetChangeHandler);
        overlayTarget.addComponentListener(overlayTargetChangeHandler);
        overlay.addComponentListener(overlayChangeHandler);
        overlay.addPropertyChangeListener(overlayChangeHandler);
    }

    @SuppressWarnings("checkstyle:javancss")
    public void positionOverlay(JLayeredPane layeredPane) {
        int centerX = xoffset;
        int centerY = yoffset;
        Rectangle overlayTargetBounds = new Rectangle(0, 0, overlayTarget.getWidth(), overlayTarget.getHeight());
        switch (center) {
        case SwingConstants.NORTH:
        case SwingConstants.NORTH_WEST:
        case SwingConstants.NORTH_EAST:
            centerY += overlayTargetBounds.y;
            break;
        case SwingConstants.CENTER:
        case SwingConstants.EAST:
        case SwingConstants.WEST:
            centerY += overlayTargetBounds.y + (overlayTargetBounds.height / 2);
            break;
        case SwingConstants.SOUTH:
        case SwingConstants.SOUTH_EAST:
        case SwingConstants.SOUTH_WEST:
            centerY += overlayTargetBounds.y + overlayTargetBounds.height;
            break;
        default:
            throw new IllegalArgumentException("Unknown value for center [" + center + "]");
        }
        switch (center) {
        case SwingConstants.WEST:
        case SwingConstants.NORTH_WEST:
        case SwingConstants.SOUTH_WEST:
            centerX += overlayTargetBounds.x;
            break;
        case SwingConstants.CENTER:
        case SwingConstants.NORTH:
        case SwingConstants.SOUTH:
            centerX += overlayTargetBounds.x + (overlayTargetBounds.width / 2);
            break;
        case SwingConstants.EAST:
        case SwingConstants.NORTH_EAST:
        case SwingConstants.SOUTH_EAST:
            centerX += overlayTargetBounds.x + overlayTargetBounds.width;
            break;
        default:
            throw new IllegalArgumentException("Unknown value for center [" + center + "]");
        }
        Dimension size = overlay.getPreferredSize();
        Rectangle newBound = new Rectangle(centerX - (size.width / 2), centerY - (size.height / 2), size.width,
                size.height);
        Rectangle visibleRect = findLargestVisibleRectFor(newBound);

        int offsetx = 0;
        int offsety = 0;

        if (visibleRect != null) {
            if (newBound.y < visibleRect.y) {
                offsety += visibleRect.y - newBound.y;
            }
            if (newBound.x < visibleRect.x) {
                offsetx += visibleRect.x - newBound.x;
            }
            newBound = newBound.intersection(visibleRect);
        } else {
            newBound.width = newBound.height = 0;
        }
        Point pt = SwingUtilities.convertPoint(overlayTarget, newBound.x, newBound.y, layeredPane);
        newBound.x = pt.x;
        newBound.y = pt.y;
        setOverlayBounds(newBound, offsetx, offsety);
    }

    void putOverlay(final JLayeredPane layeredPane) {
        if (overlay.getParent() != overlayClipper) {
            JComponent parent = (JComponent) overlay.getParent();
            if (parent != null) {
                parent.remove(overlay);
            }
            overlayClipper.add(overlay);
        }
        if (overlayClipper != layeredPane) {
            JComponent parent = (JComponent) overlayClipper.getParent();
            if (parent != null) {
                parent.remove(overlayClipper);
            }
            layeredPane.add(overlayClipper);
            layeredPane.setLayer(overlayClipper, JLayeredPane.PALETTE_LAYER.intValue());
        }
    }

    public void removeOverlay() {
        if (overlay.getParent() != overlayClipper && overlay.getParent() != null) {
            overlay.getParent().remove(overlay);
        }
        if (overlayClipper.getParent() != null) {
            overlayClipper.getParent().remove(overlayClipper);
        }
    }

    protected void setOverlayBounds(Rectangle newBounds, int xoffsetParam, int yoffsetParam) {
        final Dimension preferred = overlay.getPreferredSize();
        final Rectangle overlayBounds = new Rectangle(-xoffsetParam, -yoffsetParam, preferred.width, preferred.height);
        if (!overlayBounds.equals(overlay.getBounds())) {
            overlay.setBounds(overlayBounds);
        }
        if (!newBounds.equals(overlayClipper.getBounds())) {
            overlayClipper.setBounds(newBounds);
        }
    }

    /**
     * @param overlay
     *            The overlay to set.
     */
    public void setOverlayComponent(JComponent paramOverlay) {
        this.overlay = paramOverlay;
    }

    void updateOverlay() {
        if (isUpdating) {
            return;
        }
        isUpdating = true;
        // updating the overlay at the end of the event queue to avoid race
        // conditions
        // see RCP-126
        // (http://opensource.atlassian.com/projects/spring/browse/RCP-216)
        SwingUtilities.invokeLater(overlayUpdater);
    }
}