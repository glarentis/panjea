package it.eurotn.rich.control.table.renderer;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;

import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

public class ImportoContextSensitiveCellRenderer extends IconContextSensitiveCellRenderer {

    private static final long serialVersionUID = -2644515998750453100L;

    private ValutaAziendaCache valutaAziendaCache;

    /**
     * Costruttore.
     * 
     */
    public ImportoContextSensitiveCellRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.RIGHT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof Importo) {
            Importo importo = (Importo) value;
            label.setHorizontalTextPosition(getTextPosition());
            label.setToolTipText(null);
            if (importo != null && !valutaAziendaCache.caricaValutaAziendaCorrente().getCodiceValuta()
                    .equals(importo.getCodiceValuta())) {
                setTooltip(label, importo);
            }
        }
        return label;
    }

    @Override
    public int getTextPosition() {
        return SwingConstants.LEFT;
    }

    /**
     * @param label
     *            label da settare
     * @param importo
     *            importo con i dati
     */
    protected void setTooltip(JLabel label, Importo importo) {
        StringBuilder sb = new StringBuilder("<HTML><B>");
        sb.append(valutaAziendaCache.caricaValutaAziendaCorrente().getCodiceValuta());
        sb.append(": </B> ");
        sb.append(importo.getImportoInValutaAzienda());
        if (importo.getTassoDiCambio().compareTo(BigDecimal.ONE) != 0) {
            sb.append("<br><b>Cambio:</>");
            sb.append(importo.getTassoDiCambio().toString());
        }
        label.setToolTipText(sb.toString());
    }

    /**
     * @param valutaAziendaCache
     *            The valutaAziendaCache to set.
     */
    public void setValutaAziendaCache(ValutaAziendaCache valutaAziendaCache) {
        this.valutaAziendaCache = valutaAziendaCache;
    }
}
