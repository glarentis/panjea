package it.eurotn.panjea.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.MessageDialog;

import com.jgoodies.forms.debug.FormDebugUtils;
import com.jgoodies.forms.layout.FormLayout;

/**
 * A panel that paints grid bounds if and only if the panel's layout manager is a {@link FormLayout}. You can tweak the
 * debug paint process by setting a custom grid color, painting optional diagonals and painting the grid in the
 * background.
 * <p>
 *
 * This class is not intended to be extended. However, it is not marked as <code>final</code> to allow users to subclass
 * it for debugging purposes. In general it is recommended to <em>use</em> JPanel instances, not <em>extend</em> them.
 * You can see this implementation style in the Forms tutorial classes. Rarely there's a need to extend JPanel; for
 * example if you provide a custom behavior for <code>#paintComponent</code> or <code>#updateUI</code>.
 *
 * @author Karsten Lentzsch
 * @version $Revision$
 *
 * @see FormDebugUtils
 */
public class FormDebugPanelNumbered extends JPanel {

    private final class PanelMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            // // Paint the column bounds.
            double xpoint = event.getPoint().getX();
            double ypoint = event.getPoint().getY();
            FormLayout.LayoutInfo layoutInfo = FormDebugUtils.getLayoutInfo(FormDebugPanelNumbered.this);
            int ncol = 0;
            int nrow = 0;
            for (int col = 0; col < layoutInfo.columnOrigins.length - 1; col++) {
                if (xpoint > layoutInfo.columnOrigins[col] && xpoint < layoutInfo.columnOrigins[col + 1]) {
                    ncol = col;
                    break;
                }
            }
            for (int row = 0; row < layoutInfo.rowOrigins.length - 1; row++) {
                if (ypoint > layoutInfo.rowOrigins[row] && ypoint < layoutInfo.rowOrigins[row + 1]) {
                    nrow = row;
                    break;
                }
            }
            ncol++;
            nrow++;
            MessageDialog dialog = new MessageDialog("Colonna, riga",
                    new DefaultMessage("C" + ncol + ":" + "R" + nrow));
            dialog.showDialog();
        }
    }

    private static final long serialVersionUID = -8433725263752586162L;

    /**
     * The default color used to paint the form's debug grid.
     */
    private static final Color DEFAULT_GRID_COLOR = Color.red;

    /**
     * Specifies whether the grid shall be painted in the background. Is off by default and so the grid is painted in
     * the foreground.
     */
    private boolean paintInBackground;

    /**
     * Specifies whether the container's diagonals should be painted.
     */
    private boolean paintDiagonals;

    /**
     * Holds the color used to paint the debug grid.
     */
    private Color gridColor = DEFAULT_GRID_COLOR;

    // Instance Creation ****************************************************

    /**
     * Constructs a FormDebugPanel with all options turned off.
     */
    public FormDebugPanelNumbered() {
        this(null);
        this.addMouseListener(new PanelMouseListener());
    }

    /**
     * Constructs a FormDebugPanel on the given FormLayout using the specified settings that are otherwise turned off.
     *
     * @param paintInBackground
     *            true to paint grid lines in the background, false to paint the grid in the foreground
     * @param paintDiagonals
     *            true to paint diagonals, false to not paint them
     */
    public FormDebugPanelNumbered(final boolean paintInBackground, final boolean paintDiagonals) {
        this(null, paintInBackground, paintDiagonals);
    }

    /**
     * Constructs a FormDebugPanel on the given FormLayout instance that paints the grid in the foreground and paints no
     * diagonals.
     *
     * @param layout
     *            the panel's FormLayout instance
     */
    public FormDebugPanelNumbered(final FormLayout layout) {
        this(layout, false, false);
    }

    /**
     * Constructs a FormDebugPanel on the given FormLayout using the specified settings that are otherwise turned off.
     *
     * @param layout
     *            the panel's FormLayout instance
     * @param paintInBackground
     *            true to paint grid lines in the background, false to paint the grid in the foreground
     * @param paintDiagonals
     *            true to paint diagonals, false to not paint them
     */
    public FormDebugPanelNumbered(final FormLayout layout, final boolean paintInBackground,
            final boolean paintDiagonals) {
        super(layout);
        setPaintInBackground(paintInBackground);
        setPaintDiagonals(paintDiagonals);
        setGridColor(DEFAULT_GRID_COLOR);
    }

    // Accessors ************************************************************

    /**
     * Paints the panel. If the panel's layout manager is a FormLayout it paints the form's grid lines.
     *
     * @param graphics
     *            the Graphics object to paint on
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (!paintInBackground) {
            paintGrid(graphics);
        }
    }

    /**
     * Paints the component and - if background painting is enabled - the grid
     *
     * @param graphics
     *            the Graphics object to paint on
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (paintInBackground) {
            paintGrid(graphics);
        }
    }

    /**
     * Paints the form's grid lines and diagonals.
     *
     * @param g
     *            the Graphics object used to paint
     */
    private void paintGrid(Graphics graphics) {
        if (!(getLayout() instanceof FormLayout)) {
            return;
        }
        FormLayout.LayoutInfo layoutInfo = FormDebugUtils.getLayoutInfo(this);
        int left = layoutInfo.getX();
        int top = layoutInfo.getY();
        int width = layoutInfo.getWidth();
        int height = layoutInfo.getHeight();

        graphics.setColor(gridColor);
        // Paint the column bounds.
        for (int col = 0; col < layoutInfo.columnOrigins.length; col++) {
            graphics.fillRect(layoutInfo.columnOrigins[col], top, 1, height);
        }

        // Paint the row bounds.
        for (int row = 0; row < layoutInfo.rowOrigins.length; row++) {
            graphics.fillRect(left, layoutInfo.rowOrigins[row], width, 1);
        }

        if (paintDiagonals) {
            graphics.drawLine(left, top, left + width, top + height);
            graphics.drawLine(left, top + height, left + width, top);
        }
    }

    // Painting *************************************************************

    /**
     * Sets the debug grid's color.
     *
     * @param color
     *            the color used to paint the debug grid
     */
    public void setGridColor(Color color) {
        gridColor = color;
    }

    /**
     * Enables or disables to paint the panel's diagonals.
     *
     * @param paint
     *            true to paint diagonals, false to not paint them
     */
    public void setPaintDiagonals(boolean paint) {
        paintDiagonals = paint;
    }

    /**
     * Specifies to paint in background or foreground.
     *
     * @param paint
     *            true to paint in the background, false for the foreground
     */
    public void setPaintInBackground(boolean paint) {
        paintInBackground = paint;
    }
}