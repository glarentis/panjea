package it.eurotn.panjea.rich.factory.table;

import java.awt.Component;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.JTable;

/**
 * Classe che renderizza un valore di tipo <code>BigDecimal</code>. Se viene specificato il simbolo valuta questo verrà
 * visualizzato dopo il valore.
 * 
 * @author fattazzo
 * 
 */
public class CustomBigDecimalCellRenderer extends AbstractCustomTableCellRenderer {

    private static final long serialVersionUID = 9217453065990280535L;

    // stringa che mi serve per ottenere il formato per la formattazione. Questo lo ottengo
    // facendo una substring con il numero di decimali
    public static final String strZeri = "0000000000000000000000000";

    // numero di decimali da visualizzare. Di default sono 2
    protected int numeroDecimali = 2;

    private String simboloValuta;

    public CustomBigDecimalCellRenderer() {
        super();
    }

    public CustomBigDecimalCellRenderer(int numeroDecimali) {
        this(numeroDecimali, null);
    }

    public CustomBigDecimalCellRenderer(int numeroDecimali, String simboloValuta) {
        this.numeroDecimali = numeroDecimali;
        this.simboloValuta = simboloValuta;
    }

    public CustomBigDecimalCellRenderer(String simboloValuta) {
        this(2, simboloValuta);
    }

    @Override
    public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
        return null;
    }

    @Override
    public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String renderedValue = "";

        if (value != null && !value.toString().isEmpty() && value instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) value;
            Format format = new DecimalFormat("###,###,###,##0." + strZeri.substring(0, numeroDecimali));
            renderedValue = format.format(bigDecimal);

            if (simboloValuta != null && !value.toString().isEmpty()) {
                renderedValue = renderedValue + " " + simboloValuta;
            }
        }

        setValue(renderedValue);

        return cell;
    }

}
