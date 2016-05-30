/**
 * 
 */
package it.eurotn.panjea.rich.factory;

import javax.swing.JButton;

import com.jidesoft.swing.ButtonStyle;
import com.jidesoft.swing.JideButton;

/**
 * Factory specifico per creare JIdeButton con stile Hyperlink.
 * 
 * @author Leonardo
 */
public class PanjeaLinkButtonFactory extends PanjeaButtonFactory {

    /**
     * Costruttore.
     * 
     */
    public PanjeaLinkButtonFactory() {
        super();
    }

    @Override
    public JButton createButton() {
        JideButton button = new JideButton();
        button.setButtonStyle(ButtonStyle.HYPERLINK_STYLE);
        return button;
    }

}
