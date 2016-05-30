package it.eurotn.panjea.rich.factory.table;

import it.eurotn.panjea.anagrafica.domain.Importo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;

import org.apache.log4j.Logger;

/**
 * Renderizza l'importo con il numero decimali recuperati<br>
 * da una proprietà del bean contenuto nella riga<br>
 * La proprietà viene passata sul costruttore
 * 
 * @author giangi
 * 
 */
public class CustomImportoDecimaliVariabiliCellRender extends AbstractCustomTableCellRenderer {
    private static final long serialVersionUID = -5821573644725375130L;
    static Logger logger = Logger.getLogger(CustomImportoDecimaliVariabiliCellRender.class);

    // private String propNumDecimali;

    // public CustomImportoDecimaliVariabiliCellRender(String propNumDecimali) {
    // super();
    // this.propNumDecimali = propNumDecimali;
    // }

    @Override
    public String getIconKey(Object value, boolean isSelected, boolean hasFocus) {
        return null;
    }

    @Override
    public String getRendererText(Object value, boolean isSelected, boolean hasFocus) {
        String renderedImportoValue = "";
        String symbol = "";

        Importo importo = (Importo) value;
        if (importo.getCodiceValuta() != null) {
            Currency currency = Currency.getInstance(importo.getCodiceValuta());
            symbol = currency.getSymbol();
            if ("EUR".equals(symbol)) {
                symbol = "€";
            }
        } else {
            logger.warn("--> codice valuta non trovato.");
        }
        BigDecimal importoInValuta = importo.getImportoInValuta();
        // org.springframework.util.Assert.isInstanceOf(GlazedTableModel.class, table.getModel(),
        // "Il render può essere installato solamente su un table model GlazedTableModel");
        // GlazedTableModel tableModel = (GlazedTableModel) table.getModel();
        // Object element = tableModel.getElementAt(row);
        // int numerDecimali = getValueNumeroDecimaleProperty(element);
        NumberFormat numberFormat = new DecimalFormat("###,###,###,##0.###########");

        renderedImportoValue = numberFormat.format(importoInValuta);

        return renderedImportoValue + " " + symbol;
    }

    @Override
    public String getRendererToolTipText(Object value, boolean isSelected, boolean hasFocus) {
        String toolTipText = "";
        String symbol = "";

        Importo importo = (Importo) value;
        if (importo.getCodiceValuta() != null) {
            Currency currency = Currency.getInstance(importo.getCodiceValuta());
            symbol = currency.getSymbol();
            if ("EUR".equals(symbol)) {
                symbol = "€";
            }
        } else {
            logger.warn("--> codice valuta non trovato.");
        }
        BigDecimal importoInValutaAzienda = importo.getImportoInValutaAzienda();
        // org.springframework.util.Assert.isInstanceOf(GlazedTableModel.class, table.getModel(),
        // "Il render può essere installato solamente su un table model GlazedTableModel");
        // GlazedTableModel tableModel = (GlazedTableModel) table.getModel();
        // Object element = tableModel.getElementAt(row);
        // int numerDecimali = getValueNumeroDecimaleProperty(element);
        NumberFormat numberFormat = new DecimalFormat("###,###,###,##0.###########");

        toolTipText = numberFormat.format(importoInValutaAzienda) + " (" + importo.getTassoDiCambio() + ")";

        return toolTipText;
    }
}
