/**
 * 
 */
package it.eurotn.panjea.rich.factory;

import javax.swing.AbstractButton;
import javax.swing.JButton;

import org.springframework.richclient.factory.DefaultButtonFactory;

import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideToggleButton;

/**
 * Factory per restituire Buttons e Menu personalizzati.<br>
 * Viene sovrascritto questo factory nel context.xml<br>
 * <br>
 * &lt;bean id="componentFactory" class="MyComponentFactory or DefaultComponentFactory"&gt;<br>
 * &lt;property name="buttonFactory"&gt;<br>
 * &lt;bean class="MyButtonFactory"&gt;&lt;/bean&gt;<br>
 * &lt;/property&gt;<br>
 * &lt;/bean&gt;<br>
 * 
 * @author Leonardo
 * 
 */
public class PanjeaButtonFactory extends DefaultButtonFactory {

    /**
     * Costruttore.
     * 
     */
    public PanjeaButtonFactory() {
        super();
    }

    @Override
    public JButton createButton() {
        return new JideButton();
    }

    @Override
    public AbstractButton createToggleButton() {
        return new JideToggleButton();
    }
}
