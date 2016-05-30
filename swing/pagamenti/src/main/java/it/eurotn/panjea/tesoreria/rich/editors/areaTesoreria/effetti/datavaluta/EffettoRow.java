package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta;

import java.math.BigDecimal;
import java.util.Date;

import com.jidesoft.grid.DefaultExpandableRow;

import it.eurotn.panjea.tesoreria.domain.Effetto;

public class EffettoRow extends DefaultExpandableRow {

    private final Effetto effetto;

    /**
     * Costruisce una riga del table basata su un effetto.
     *
     * @param effetto
     *            effetto descritto dalla riga
     */
    public EffettoRow(final Effetto effetto) {
        this.effetto = effetto;
    }

    @Override
    public Class<?> getCellClassAt(int column) {
        switch (column) {
        case 0:
            return Date.class;
        case 1:
            return Date.class; // data valuta
        case 2:
            return BigDecimal.class; // gg banca
        case 3:
            return BigDecimal.class; // importo effetto/pagamento/rata
        default:
            throw new UnsupportedOperationException();
        }
    }

    /**
     *
     * @return effetto contenuto nella riga.
     */
    public Effetto getEffetto() {
        return effetto;
    }

    @Override
    public Object getValueAt(int column) {
        switch (column) {
        case 0:
            return effetto.getDataScadenza();
        case 1:
            return effetto.getDataValuta();
        case 2:
            return new BigDecimal(effetto.getGiorniBanca());
        case 3:
            return effetto.getImporto().getImportoInValutaAzienda();
        default:
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean isCellEditable(int i) {
        switch (i) {
        case 1:
        case 2:
            return true;
        default:
            return false;
        }
    }

    @Override
    public void setValueAt(Object arg0, int arg1) {
        switch (arg1) {
        case 1:
            effetto.setDataValuta((Date) arg0);
            break;
        case 2:
            // NPE MAIL: posso cancellare i giorni banca e quindi arriva null
            BigDecimal value = (BigDecimal) arg0;
            if (value == null) {
                value = BigDecimal.ZERO;
            }
            effetto.addGiorniBanca(value.intValue());
            break;
        default:
            throw new UnsupportedOperationException();
        }
    }

}
