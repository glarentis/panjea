package it.eurotn.panjea.anagrafica.rich.editors.tabelle.riepilogodatibancari;

import java.util.List;

import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.AnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public class SostituzioneDialog extends PanjeaTitledPageApplicationDialog {

    private IAnagraficaBD anagraficaBD;

    private List<RapportoBancarioSedeEntita> rapporti;

    /**
     * Costruttore.
     *
     */
    public SostituzioneDialog() {
        super(new SostituzioneDatiBancariForm(), null);
        // setPreferredSize(new Dimension(800, 600));

        this.anagraficaBD = RcpSupport.getBean(AnagraficaBD.BEAN_ID);
    }

    @Override
    protected String getTitle() {
        return "Sostituzione dati bancari";
    }

    @Override
    protected boolean isMessagePaneVisible() {
        return false;
    }

    @Override
    protected void onAboutToShow() {
        super.onAboutToShow();

        ((FormBackedDialogPage) getDialogPage()).getBackingFormPage().setFormObject(new RapportoBancarioSedeEntita());
    }

    @Override
    protected boolean onFinish() {
        ((FormBackedDialogPage) getDialogPage()).getBackingFormPage().commit();
        RapportoBancarioSedeEntita rapporto = (RapportoBancarioSedeEntita) ((FormBackedDialogPage) getDialogPage())
                .getBackingFormPage().getFormObject();

        anagraficaBD.sostituisciDatiBancari(rapporti, rapporto.getBanca(), rapporto.getFiliale());

        return true;
    }

    /**
     * @param rapporti
     *            the rapporti to set
     */
    public void setRapporti(List<RapportoBancarioSedeEntita> rapporti) {
        this.rapporti = rapporti;
    }

}