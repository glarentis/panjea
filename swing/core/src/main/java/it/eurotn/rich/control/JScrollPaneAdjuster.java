package it.eurotn.rich.control;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JScrollPaneAdjuster implements PropertyChangeListener, Serializable {
    private static class Adjuster implements ChangeListener, Runnable {
        public static final int X = 1, Y = 2;

        private JViewport viewport, header;
        private int type;

        public Adjuster(JViewport viewport, JViewport header, int type) {
            this.viewport = viewport;
            this.header = header;
            this.type = type;

            if (header != null) {
                header.addChangeListener(this);
            }
        }

        public void dispose() {
            if (header != null) {
                header.removeChangeListener(this);
            }

            viewport = header = null;
        }

        @Override
        public void run() {
            if (viewport == null || header == null) {
                return;
            }

            Point v = viewport.getViewPosition(), h = header.getViewPosition();

            if (type == X) {
                if (v.x != h.x) {
                    viewport.setViewPosition(new Point(h.x, v.y));
                }
            } else {
                if (v.y != h.y) {
                    viewport.setViewPosition(new Point(v.x, h.y));
                }
            }
        }

        public void setHeader(JViewport newHeader) {
            if (header != null) {
                header.removeChangeListener(this);
            }

            header = newHeader;

            if (header != null) {
                header.addChangeListener(this);
            }
        }

        public void setViewport(JViewport newViewport) {
            viewport = newViewport;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            if (viewport == null || header == null) {
                return;
            }

            if (type == X) {
                if (viewport.getViewPosition().x != header.getViewPosition().x) {
                    SwingUtilities.invokeLater(this);
                }
            } else {
                if (viewport.getViewPosition().y != header.getViewPosition().y) {
                    SwingUtilities.invokeLater(this);
                }
            }
        }
    }

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private JScrollPane pane;

    private transient Adjuster x, y;

    public JScrollPaneAdjuster(JScrollPane pane) {
        this.pane = pane;

        this.x = new Adjuster(pane.getViewport(), pane.getColumnHeader(), Adjuster.X);
        this.y = new Adjuster(pane.getViewport(), pane.getRowHeader(), Adjuster.Y);

        pane.addPropertyChangeListener(this);
    }

    public void dispose() {
        x.dispose();
        y.dispose();

        pane.removePropertyChangeListener(this);
        pane = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String name = e.getPropertyName();

        if (name.equals("viewport")) {
            x.setViewport((JViewport) e.getNewValue());
            y.setViewport((JViewport) e.getNewValue());
        } else if (name.equals("rowHeader")) {
            y.setHeader((JViewport) e.getNewValue());
        } else if (name.equals("columnHeader")) {
            x.setHeader((JViewport) e.getNewValue());
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        x = new Adjuster(pane.getViewport(), pane.getColumnHeader(), Adjuster.X);
        y = new Adjuster(pane.getViewport(), pane.getRowHeader(), Adjuster.Y);
    }
}
