package it.eurotn.panjea.rateirisconti.rich.editors.elenco;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.contabilita.rich.editors.righecontabili.rateorisconto.RigheRateoRiscontoAnnoTable;
import it.eurotn.panjea.rateirisconti.rich.bd.IRateiRiscontiBD;
import it.eurotn.panjea.rateirisconti.rich.bd.RateiRiscontiBD;

public class RateiRiscontiAnnoDialog extends MessageDialog {

    private IRateiRiscontiBD rateiRiscontiBD;

    private RigheRateoRiscontoAnnoTable riscontiAnnoTable;

    /**
     * Costruttore.
     */
    public RateiRiscontiAnnoDialog() {
        super("Elenco risconti presenti", "_");
        setDialogScaleFactor(1.0f);
        setPreferredSize(new Dimension(1000, 200));

        this.rateiRiscontiBD = RcpSupport.getBean(RateiRiscontiBD.BEAN_ID);
        this.riscontiAnnoTable = new RigheRateoRiscontoAnnoTable();
    }

    @Override
    protected JComponent createDialogContentPane() {
        JPanel rootPanel = getComponentFactory().createPanel(new BorderLayout());
        rootPanel.add(riscontiAnnoTable.getComponent(), BorderLayout.CENTER);
        return rootPanel;
    }

    /**
     * @param idRigaRateoRisconto
     *            the idRigaRateoRisconto to set
     */
    public void setRigaRateoRisconto(Integer idRigaRateoRisconto) {
        RigaRateoRisconto rigaRateoRisconto = rateiRiscontiBD.caricaRigaRateoRisconto(idRigaRateoRisconto);

        riscontiAnnoTable.setRows(rigaRateoRisconto.getRateiRiscontiAnno());
    }

}
