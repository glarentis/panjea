/**
 * 
 */
package it.eurotn.rich.form.builder.support;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Classe che estende da <code>OverlayHelper</code> e posiziona il compomente di overlay non in base al su centro ma in
 * base allo posizione del componente a cui si aggiunge.<br>
 * 
 * @author fattazzo
 * 
 */
public class JECOverlayHelper extends OverlayHelper {

    /**
     * Aggiunge l'overlay in basso a sinistra rispetto al componente.
     * 
     * @param overlay
     *            overlay
     * @param overlayTarget
     *            target
     * @param xOffset
     *            offset x rispetto al overlayTarget
     * @param yOffset
     *            offset y rispetto al overlayTarget
     */
    public static void attachOverlay(JComponent overlay, JComponent overlayTarget, int xOffset, int yOffset) {
        new JECOverlayHelper(overlay, overlayTarget, SwingConstants.SOUTH_WEST, xOffset, yOffset);
    }

    int xOffset;
    int yOffset;

    int allign;

    public JECOverlayHelper(JComponent overlay, JComponent overlayTarget, int allign, int offset, int offset2) {
        super(overlay, overlayTarget, allign, offset, offset2);
        xOffset = offset;
        yOffset = offset2;
        this.allign = allign;
    }

    @Override
    public void positionOverlay(JLayeredPane layeredPane) {
        int xOverlay = xOffset;
        int yOverlay = yOffset;
        Rectangle overlayTargetBounds = new Rectangle(0, 0, overlayTarget.getWidth(), overlayTarget.getHeight());
        switch (allign) {
        case SwingConstants.NORTH:
        case SwingConstants.NORTH_WEST:
        case SwingConstants.NORTH_EAST:
            yOverlay += overlayTargetBounds.y;
            break;
        case SwingConstants.CENTER:
        case SwingConstants.EAST:
        case SwingConstants.WEST:
            yOverlay += overlayTargetBounds.y + overlayTargetBounds.height;
            break;
        case SwingConstants.SOUTH:
        case SwingConstants.SOUTH_EAST:
        case SwingConstants.SOUTH_WEST:
            yOverlay += overlayTargetBounds.y + overlayTargetBounds.height;
            break;
        default:
            throw new IllegalArgumentException("Unknown value for center [" + allign + "]");
        }
        switch (allign) {
        case SwingConstants.WEST:
        case SwingConstants.NORTH_WEST:
        case SwingConstants.SOUTH_WEST:
            xOverlay += overlayTargetBounds.x;
            break;
        case SwingConstants.CENTER:
        case SwingConstants.NORTH:
        case SwingConstants.SOUTH:
            xOverlay += overlayTargetBounds.x + overlayTargetBounds.width;
            break;
        case SwingConstants.EAST:
        case SwingConstants.NORTH_EAST:
        case SwingConstants.SOUTH_EAST:
            xOverlay += overlayTargetBounds.x + overlayTargetBounds.width;
            break;
        default:
            throw new IllegalArgumentException("Unknown value for center [" + allign + "]");
        }
        Dimension size = overlay.getPreferredSize();
        Rectangle newBound = new Rectangle(xOverlay, yOverlay, size.width, size.height);
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
}
