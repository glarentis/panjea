package it.eurotn.panjea.tesoreria.rich.editors.areaTesoreria.effetti.datavaluta;

import java.math.BigDecimal;
import java.util.Date;

import com.jidesoft.grid.DefaultExpandableRow;

import it.eurotn.panjea.anagrafica.domain.Importo;

public class BancaRow extends DefaultExpandableRow {

    private final String banca;
    private final Date dataValuta;
    private Importo importo;

    /**
     *
     * @param banca
     *            banca da visualizzare
     */
    public BancaRow(final String banca) {
        this.banca = banca;
        this.importo = new Importo();
        this.dataValuta = null;
        importo.setImportoInValuta(BigDecimal.ZERO);
        importo.calcolaImportoValutaAzienda(2);
    }

    @Override
    public Object addChild(Object arg0) {
        final EffettoRow effettoRow = (EffettoRow) arg0;
        if (importo.getCodiceValuta() == null) {
            importo.setCodiceValuta(effettoRow.getEffetto().getImporto().getCodiceValuta());
        }
        importo = importo.add(effettoRow.getEffetto().getImporto(), 2);
        return super.addChild(arg0);
    }

    @Override
    public Class<?> getCellClassAt(int column) {
        switch (column) {
        case 0:
            return String.class;
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

    @Override
    public Object getValueAt(int column) {
        switch (column) {
        case 0:
            return banca;
        case 1:
            return dataValuta;
        case 3:
            return importo.getImportoInValutaAzienda();
        default:
            return "";
        }
    }

    @Override
    public boolean isCellEditable(int i) {
        switch (i) {
        case 1:
            return true;
        default:
            return false;
        }
    }

    @Override
    public void setValueAt(Object arg0, int col) {
        if (col == 1) {
            final Date nuovaDataValuta = (Date) arg0;
            for (final Object children : getChildren()) {
                ((EffettoRow) children).setValueAt(nuovaDataValuta, col);
            }
        }
        super.setValueAt(arg0, col);
    }

}
