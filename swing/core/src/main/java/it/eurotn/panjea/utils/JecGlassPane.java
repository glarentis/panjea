package it.eurotn.panjea.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class JecGlassPane extends JPanel implements MouseListener, MouseMotionListener, FocusListener {
    private static final long serialVersionUID = 1162288659882444856L;

    private Container contentPane;

    private boolean inDrag = false;

    // trigger for redispatching (allows external control)
    private boolean needToRedispatch = false;

    /**
     *
     * Costruttore.
     *
     * @param cp
     *            container
     * @param message
     *            messaggio da visualizzare
     */
    public JecGlassPane(final Container cp, final String message) {
        setLayout(new BorderLayout());
        contentPane = cp;
        addMouseListener(this);
        addMouseMotionListener(this);
        addFocusListener(this);

        JLabel messageLabel = new JLabel();
        messageLabel.setOpaque(false);
        messageLabel.setEnabled(false);
        StringBuilder sb = new StringBuilder();
        sb.append("<html><b><font size=6 bgcolor=#D3D3D3>");
        sb.append(message);
        sb.append("</font></b><html>");
        messageLabel.setText(sb.toString());
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel, BorderLayout.CENTER);

        setOpaque(false);
    }

    @Override
    public void focusGained(FocusEvent fe) {
        // non faccio niente
    }

    // Once we have focus, keep it if we're visible
    @Override
    public void focusLost(FocusEvent fe) {
        if (isVisible()) {
            requestFocus();
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if (needToRedispatch) {
            redispatchMouseEvent(event);
        }
    }

    /*
     * (Based on code from the Java Tutorial) We must forward at least the mouse drags that started with mouse presses
     * over the check box. Otherwise, when the user presses the check box then drags off, the check box isn't disarmed
     * -- it keeps its dark gray background or whatever its L&F uses to indicate that the button is currently being
     * pressed.
     */
    @Override
    public void mouseDragged(MouseEvent event) {
        if (needToRedispatch) {
            redispatchMouseEvent(event);
        }
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        if (needToRedispatch) {
            redispatchMouseEvent(event);
        }
    }

    @Override
    public void mouseExited(MouseEvent event) {
        if (needToRedispatch) {
            redispatchMouseEvent(event);
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        if (needToRedispatch) {
            redispatchMouseEvent(event);
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (needToRedispatch) {
            redispatchMouseEvent(event);
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (needToRedispatch) {
            redispatchMouseEvent(event);
            inDrag = false;
        }
    }

    /**
     * redispatchMouseEvent.
     *
     * @param e
     *            mouse event
     */
    private void redispatchMouseEvent(MouseEvent event) {
        boolean inButton = false;
        Point glassPanePoint = event.getPoint();
        Component component = null;
        Container container = contentPane;
        Point containerPoint = SwingUtilities.convertPoint(this, glassPanePoint, contentPane);
        int eventID = event.getID();

        // XXX: If the event is from a component in a popped-up menu,
        // XXX: then the container should probably be the menu's
        // XXX: JPopupMenu, and containerPoint should be adjusted
        // XXX: accordingly.
        component = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);

        if (component == null) {
            return;
        } else {
            inButton = true;
            if (eventID == MouseEvent.MOUSE_PRESSED) {
                inDrag = true;
            }
        }

        if (inButton || inDrag) {
            Point componentPoint = SwingUtilities.convertPoint(this, glassPanePoint, component);
            component.dispatchEvent(new MouseEvent(component, eventID, event.getWhen(), event.getModifiers(),
                    componentPoint.x, componentPoint.y, event.getClickCount(), event.isPopupTrigger()));
        }
    }

    /**
     * We only need to redispatch if we're not visible, but having full control over this might prove handy.
     *
     * @param need
     *            need
     */
    public void setNeedToRedispatch(boolean need) {
        needToRedispatch = need;
    }

    @Override
    public void setVisible(boolean visible) {
        // Make sure we grab the focus so that key events don't go astray.
        if (visible) {
            requestFocus();
        }
        super.setVisible(visible);
    }
}