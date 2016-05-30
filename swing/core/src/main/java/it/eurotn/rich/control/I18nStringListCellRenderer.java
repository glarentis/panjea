/**
 * 
 */
package it.eurotn.rich.control;

import java.awt.Component;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;

/**
 * Tramite il message source internazionalizza un valore di tipo stringa.
 * 
 * @author fattazzo
 * 
 */
public class I18nStringListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = 65400090199481049L;

    MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        label.setText(messageSource.getMessage(label.getText(), new Object[] {}, Locale.getDefault()));

        return label;
    }

}
