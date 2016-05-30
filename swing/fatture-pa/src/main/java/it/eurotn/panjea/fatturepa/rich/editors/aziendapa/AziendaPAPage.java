package it.eurotn.panjea.fatturepa.rich.editors.aziendapa;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.dialog.MessageDialog;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.rich.bd.IFatturePAAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * @author fattazzo
 *
 */
public class AziendaPAPage extends FormBackedDialogPageEditor {

    private static final Logger LOGGER = Logger.getLogger(AziendaPAPage.class);

    private Azienda azienda;

    private IFatturePAAnagraficaBD fatturePAAnagraficaBD;

    private AziendaCorrente aziendaCorrente;

    /**
     * Costruttore.
     */
    public AziendaPAPage() {
        super("aziendaPAPage", new AziendaPAForm());
    }

    @Override
    protected Object doSave() {
        AziendaFatturaPA aziendaFatturaPA = (AziendaFatturaPA) this.getForm().getFormObject();
        aziendaFatturaPA = fatturePAAnagraficaBD.salvaAziendaFatturaPA(aziendaFatturaPA);
        return aziendaFatturaPA;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand() };
    }

    @Override
    public void loadData() {
        // non faccio niente
    }

    @Override
    public void onPostPageOpen() {
        getBackingFormPage().setFormObject(fatturePAAnagraficaBD.caricaAziendaFatturaPA());
        if (((AziendaFatturaPA) getBackingFormPage().getFormObject()).isNew()) {
            toolbarPageEditor.getLockCommand().execute();

            // inizializzo la nazione
            getBackingFormPage().getFormModel().getValueModel("stabileOrganizzazione.datiGeografici.nazione")
                    .setValue(aziendaCorrente.getNazione());

        }
    }

    @Override
    public boolean onPrePageOpen() {
        LOGGER.debug("--> Enter onPrePageOpen");
        boolean canOpen = true;

        if (azienda.isNew()) {
            new MessageDialog("Attenzione", "Salvare l'azienda prima di poter proseguire.").showDialog();
            canOpen = false;
        }
        LOGGER.debug("--> Exit onPrePageOpen con risultato " + canOpen);
        return canOpen;
    }

    @Override
    public void refreshData() {
        // Non faccio niente
    }

    /**
     * @param aziendaCorrente
     *            the aziendaCorrente to set
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param fatturePAAnagraficaBD
     *            the fatturePAAnagraficaBD to set
     */
    public void setFatturePAAnagraficaBD(IFatturePAAnagraficaBD fatturePAAnagraficaBD) {
        this.fatturePAAnagraficaBD = fatturePAAnagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
        if (object instanceof AziendaAnagraficaDTO) {
            azienda = ((AziendaAnagraficaDTO) object).getAzienda();
        } else if (object instanceof AziendaFatturaPA) {
            super.setFormObject(object);
        }
    }

}
