package it.eurotn.panjea.rich.components;

import java.awt.Dimension;

import javax.swing.JSplitPane;

/**
 * @author Leonardo
 *
 */
public class JecSplitPane extends JSplitPane {

    private static final long serialVersionUID = -1095449649729983312L;

    /**
     * Costruttore.
     * 
     * @param newOrientation
     *            orientamento
     */
    public JecSplitPane(final int newOrientation) {
        super(newOrientation);
        setOneTouchExpandable(true);
        setContinuousLayout(true);
        setResizeWeight(1.0);
    }

    /**
     * Esegue il collapse dello splitpane.
     */
    public void collapse() {
        // Hide right or bottom
        getRightComponent().setMinimumSize(new Dimension());
        setDividerLocation(1.0d);
    }

    /**
     * Esegue l'expand dello splitpane.
     * 
     * @param location
     *            location
     */
    public void expand(double location) {
        setDividerLocation(location);
    }

}
