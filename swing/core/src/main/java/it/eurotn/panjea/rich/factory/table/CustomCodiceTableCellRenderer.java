/**
 * 
 */
package it.eurotn.panjea.rich.factory.table;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;

/**
 * Visualizza il numero del documento se questo è maggiore di 0, altrimenti viene mostrato il messaggio "non assegnato".
 * 
 * @author fattazzo
 * 
 */
public class CustomCodiceTableCellRenderer extends AbstractCustomTableCellRenderer {

    private static final long serialVersionUID = 623055229975274368L;

    private final MessageSource messageSource = (MessageSource) Application.services().getService(MessageSource.class);

    @Override
    public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
        return null;
    }

    @Override
    public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {

        Integer codice = (Integer) value;

        String text = "";

        if (codice != null) {
            text = codice.toString();

            if (codice < 0) {
                text = messageSource.getMessage("nonDefinito", new Object[] {}, Locale.getDefault());
            }
        }

        return text;
    }
}
