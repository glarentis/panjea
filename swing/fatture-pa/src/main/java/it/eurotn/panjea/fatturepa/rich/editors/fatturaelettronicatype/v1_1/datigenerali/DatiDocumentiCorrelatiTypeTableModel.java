package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1.datigenerali;

import org.apache.commons.lang3.StringUtils;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.MagazzinoDocumentoBD;
import it.eurotn.rich.control.table.DefaultBeanEditableTableModel;
import it.gov.fatturapa.sdi.fatturapa.v1.DatiDocumentiCorrelatiType;

public class DatiDocumentiCorrelatiTypeTableModel extends DefaultBeanEditableTableModel<DatiDocumentiCorrelatiType> {

    private static final long serialVersionUID = 2706037394279730422L;

    private Integer idAreaMagazzino;

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    /**
     * Costruttore.
     *
     */
    public DatiDocumentiCorrelatiTypeTableModel() {
        super("datiContrattoTableModel", new String[] { "riferimentoNumeroLinea", "idDocumento", "numItem",
                "codiceCommessaConvenzione", "data", "codiceCUP", "codiceCIG" }, DatiDocumentiCorrelatiType.class);
        this.magazzinoDocumentoBD = RcpSupport.getBean(MagazzinoDocumentoBD.BEAN_ID);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    /**
     * @param idAreaMagazzino
     *            the idAreaMagazzino to set
     */
    public void setIdAreaMagazzino(Integer idAreaMagazzino) {
        this.idAreaMagazzino = idAreaMagazzino;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        super.setValueAt(value, row, column);

        // se viene inserito un dato ( non cup e cig ) e i valori di cup e cig sono vuoti vado a caricarli dalla sede ed
        // inserirli automaticamente
        if (column < 5 && (StringUtils.isBlank((CharSequence) getValueAt(row, 5))
                || StringUtils.isBlank((CharSequence) getValueAt(row, 6)))) {
            AreaMagazzino areaMagazzino = new AreaMagazzino();
            areaMagazzino.setId(idAreaMagazzino);
            areaMagazzino = magazzinoDocumentoBD.caricaAreaMagazzino(areaMagazzino);

            if (StringUtils.isBlank((CharSequence) getValueAt(row, 5))) {
                super.setValueAt(areaMagazzino.getDocumento().getSedeEntita().getCup(), row, 5);
            }
            if (StringUtils.isBlank((CharSequence) getValueAt(row, 6))) {
                super.setValueAt(areaMagazzino.getDocumento().getSedeEntita().getCig(), row, 6);
            }

        }
    }

}
