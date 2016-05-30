package it.eurotn.rich.editors.table.renderer;

import java.awt.Component;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.springframework.context.MessageSource;

/**
 * TableCellRendered per gli oggetti di dominio.<br>
 * Nel caso di oggetto null o di un'istanza vuota è possibile specificare un messagesSource da cui recuperare un
 * messaggio generico <br>
 * con chiave messageKey
 *
 * @author adriano
 * @version 1.0, 24/lug/08
 *
 */
public class BeanPropertyTableCellRendered extends DefaultTableCellRenderer {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private MessageSource messageSource;
    private String messageKey = "all";

    /**
     * @param messageSource
     *            message source
     */
    public BeanPropertyTableCellRendered(final MessageSource messageSource) {
        super();
        this.messageSource = messageSource;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
     * java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value == null) {
            label.setText("");
            label.setText(messageSource.getMessage(messageKey, new Object[] {}, Locale.getDefault()));
        }
        return label;
    }

    /**
     * @param messageKey
     *            The messageKey to set.
     */
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

}
