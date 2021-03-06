package it.eurotn.rich.control;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

public class JTableRowHeaderResizer extends MouseInputAdapter implements Serializable, ContainerListener {
    /**
     * 
     */
    private static final long serialVersionUID = 3830014117194673550L;
    private JScrollPane pane;
    private JViewport viewport;
    private JTable rowHeader;
    private Component corner;
    private JTable view;

    private boolean enabled;

    public JTableRowHeaderResizer(JScrollPane pane) {
        this.pane = pane;

        this.pane.addContainerListener(this);
    }

    public void setEnabled(boolean what) {
        if (enabled == what) {
            return;
        }

        enabled = what;

        if (enabled) {
            addListeners();
        } else {
            removeListeners();
        }
    }

    protected void addListeners() {
        if (corner != null) {
            corner.addMouseListener(this);
            corner.addMouseMotionListener(this);
        }
    }

    protected void removeListeners() {
        if (corner != null) {
            corner.removeMouseListener(this);
            corner.removeMouseMotionListener(this);
        }
    }

    protected void lookupComponents() {
        this.view = (JTable) pane.getViewport().getView();
        this.viewport = pane.getRowHeader();
        if (viewport == null) {
            this.rowHeader = null;
        } else {
            this.rowHeader = (JTable) viewport.getView();
        }
        this.corner = pane.getCorner(ScrollPaneConstants.UPPER_LEFT_CORNER);
    }

    @Override
    public void componentAdded(ContainerEvent e) {
        componentRemoved(e);
    }

    @Override
    public void componentRemoved(ContainerEvent e) {
        if (enabled) {
            removeListeners();
        }

        lookupComponents();

        if (enabled) {
            addListeners();
        }
    }

    private boolean active;

    private int startX, startWidth;

    private int minWidth, maxWidth;

    private Dimension size;

    private static final int PIXELS = 10;

    private static final Cursor RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);

    private Cursor oldCursor;

    @Override
    public void mouseExited(MouseEvent e) {
        if (oldCursor != null) {
            corner.setCursor(oldCursor);
            oldCursor = null;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (corner.getWidth() - e.getX() <= PIXELS) {
            if (oldCursor == null) {
                oldCursor = corner.getCursor();
                corner.setCursor(RESIZE_CURSOR);
            }
        } else if (oldCursor != null) {
            corner.setCursor(oldCursor);
            oldCursor = null;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();

        startWidth = rowHeader.getWidth();

        if (startWidth - startX > PIXELS) {
            return;
        }

        active = true;

        if (oldCursor == null) {
            oldCursor = corner.getCursor();
            corner.setCursor(RESIZE_CURSOR);
        }

        minWidth = rowHeader.getMinimumSize().width;
        maxWidth = rowHeader.getMaximumSize().width;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        active = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!active) {
            return;
        }

        size = viewport.getPreferredSize();

        int newX = e.getX();

        size.width = startWidth + e.getX() - startX;

        if (size.width < minWidth) {
            size.width = minWidth;
        } else if (size.width > maxWidth) {
            size.width = maxWidth;
        }

        // This isn't too clean, it assumes the width bubbles up to
        // viewport.getPreferredSize().width without changes.
        rowHeader.getColumnModel().getColumn(0).setPreferredWidth(size.width);

        view.sizeColumnsToFit(-1);

        pane.revalidate();
    }
}
