/**
 * 
 */
package it.eurotn.panjea.rich.factory.table;

import java.awt.Color;
import java.awt.Component;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

/**
 * {@link AbstractHighlighter} che modifica background e foreground del component che e' ogni cella della treetable che
 * soddisfa il predicate {@link HighlightPredicate}. Per mantenere invariato il component si puo' impostare background e
 * foreground a null.
 * 
 * @author Leonardo
 */
public class ComponentColorHighlighter extends AbstractHighlighter {

    static Logger logger = Logger.getLogger(ComponentColorHighlighter.class);
    private Color background = null;
    private Color foreground = null;

    /**
     * @param predicate
     */
    public ComponentColorHighlighter(HighlightPredicate predicate, Color background, Color foreground) {
        super(predicate);
        this.background = background;
        this.foreground = foreground;
    }

    @Override
    protected Component doHighlight(Component component, ComponentAdapter arg1) {
        if (background != null) {
            component.setBackground(background);
        }
        if (foreground != null) {
            component.setForeground(foreground);
        }
        return component;
    }

}
