/**
 * 
 */
package it.eurotn.rich.form.binding.swing;

import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.springframework.richclient.form.binding.swing.ComboBoxBinder;

/**
 * Assume lo stesso comportament o della classe <code>ComboBoxBinder</code> restituendo però il controllo non editabile.
 * 
 * @author fattazzo
 * 
 */
public class ReadOnlyComboBoxBinder extends ComboBoxBinder {

    @Override
    protected JComponent createControl(Map context) {
        JComboBox comboBox = getComponentFactory().createComboBox();
        // setto il controllo non editabile
        comboBox.setEditable(false);
        return comboBox;
    }

}
