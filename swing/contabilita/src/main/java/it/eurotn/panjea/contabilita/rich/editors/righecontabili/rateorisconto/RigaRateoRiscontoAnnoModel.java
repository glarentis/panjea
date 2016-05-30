package it.eurotn.panjea.contabilita.rich.editors.righecontabili.rateorisconto;

import java.awt.Color;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CellStyle;

import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRiscontoAnno;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;

public class RigaRateoRiscontoAnnoModel extends DefaultBeanEditableTableModel<RigaRiscontoAnno> {
    private static NumberWithDecimalConverterContext numberPrezzoContext;
    private static final long serialVersionUID = 2450755704901458942L;
    private static CellStyle styleRigaContabileCreata;
    private static CellStyle styleRigaContabileDaCreare;

    static {
        numberPrezzoContext = new NumberWithDecimalConverterContext();
        numberPrezzoContext.setUserObject(new Integer(2));
        styleRigaContabileCreata = new CellStyle();
        styleRigaContabileCreata.setForeground(Color.BLACK);
        styleRigaContabileCreata.setFontStyle(0);

        styleRigaContabileDaCreare = new CellStyle();
        styleRigaContabileDaCreare.setForeground(Color.DARK_GRAY);
        styleRigaContabileDaCreare.setFontStyle(1);
    }

    /**
     * Costruttore.
     */
    public RigaRateoRiscontoAnnoModel() {
        super("rigaRateoRiscontoAnnoModel", new String[] { "anno", "giorni", "importo", "giorniSuccessivo",
                "importoSuccessivo", "rigaContabile.areaContabile.documento" }, RigaRiscontoAnno.class);
    }

    @Override
    public CellStyle getCellStyleAt(int row, int column) {
        switch (column) {
        case 3:
        case 4:
            return styleRigaContabileDaCreare;
        default:
            if (getElementAt(row).getRigaContabile() == null) {
                return styleRigaContabileDaCreare;
            }
            return styleRigaContabileCreata;
        }
    }

    @Override
    public ConverterContext getConverterContextAt(int i, int j) {
        switch (j) {
        case 2:
        case 4:
            return numberPrezzoContext;
        default:
            return super.getConverterContextAt(i, j);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public boolean isCellStyleOn() {
        return true;
    }

}
