package it.eurotn.panjea.anagrafica.rich.editors.rapportobancarioentita;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.forms.RapportoBancarioSedeEntitaForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class RapportoBancarioSedeEntitaPage extends FormBackedDialogPageEditor {
    private static final String PAGE_ID = "rapportoBancarioSedeEntitaPage";
    private Logger logger = Logger.getLogger(RapportoBancarioSedeEntitaPage.class);
    private IAnagraficaBD anagraficaBD;
    private SedeEntita sedeEntita;

    /**
     * Costruttore.
     * 
     */
    public RapportoBancarioSedeEntitaPage() {
        super(PAGE_ID, new RapportoBancarioSedeEntitaForm());
    }

    @Override
    protected Object doDelete() {
        RapportoBancarioSedeEntita rapportoBancarioSedeEntita = (RapportoBancarioSedeEntita) this.getBackingFormPage()
                .getFormObject();
        anagraficaBD.cancellaRapportoBancarioSedeEntita(rapportoBancarioSedeEntita);
        return rapportoBancarioSedeEntita;
    }

    @Override
    protected Object doSave() {
        logger.debug("--> Enter doSave");
        // inserisco l'azienda corrente
        RapportoBancarioSedeEntita rapportoBancarioSedeEntita = (RapportoBancarioSedeEntita) this.getBackingFormPage()
                .getFormObject();
        rapportoBancarioSedeEntita.setSedeEntita(sedeEntita);
        rapportoBancarioSedeEntita = anagraficaBD.salvaRapportoBancarioSedeEntita(rapportoBancarioSedeEntita);
        logger.debug("--> Exit doSave");
        return rapportoBancarioSedeEntita;
    }

    /**
     * @return the anagraficaBD
     */
    public IAnagraficaBD getAnagraficaBD() {
        return anagraficaBD;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
        return abstractCommands;
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onPostPageOpen() {

    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /**
     * @param sedeEntita
     *            the sedeEntita to set
     */
    public void setSedeEntita(SedeEntita sedeEntita) {
        this.sedeEntita = sedeEntita;
    }

}
