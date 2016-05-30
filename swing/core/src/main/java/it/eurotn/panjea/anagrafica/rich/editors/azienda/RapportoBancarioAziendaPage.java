/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.editors.azienda;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.forms.RapportoBancarioAziendaForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * Pagina per gestire il rapporto bancario azienda.
 *
 * @author giangi
 * @version 1.0, 17/nov/06
 *
 */
public class RapportoBancarioAziendaPage extends FormBackedDialogPageEditor {

    private static Logger logger = Logger.getLogger(RapportoBancarioAziendaPage.class);

    private static final String PAGE_ID = "rapportoAziendaPage";

    private IAnagraficaBD anagraficaBD;
    private Azienda azienda;

    /**
     * Default constructor.
     */
    public RapportoBancarioAziendaPage() {
        super(PAGE_ID, new RapportoBancarioAziendaForm());
    }

    @Override
    protected Object doDelete() {
        RapportoBancarioAzienda rapportoBancarioAzienda = (RapportoBancarioAzienda) getBackingFormPage()
                .getFormObject();
        anagraficaBD.cancellaRapportoBancario(rapportoBancarioAzienda);
        return rapportoBancarioAzienda;
    }

    @Override
    protected Object doSave() {
        logger.debug("--> Salvo il rapporto bancario");
        // inserisco l'azienda corrente
        RapportoBancarioAzienda rapportoBancarioAzienda = (RapportoBancarioAzienda) this.getBackingFormPage()
                .getFormObject();
        AziendaLite aziendaLite = new AziendaLite();
        aziendaLite.setCodice(azienda.getCodice());
        aziendaLite.setId(azienda.getId());
        aziendaLite.setVersion(azienda.getVersion());
        rapportoBancarioAzienda.setAzienda(aziendaLite);

        for (SedeEntita sedeEntita : rapportoBancarioAzienda.getSediEntita()) {
            sedeEntita.setRapportoBancarioAzienda(rapportoBancarioAzienda);
        }

        rapportoBancarioAzienda = anagraficaBD.salvaRapportoBancarioAzienda(rapportoBancarioAzienda);
        return rapportoBancarioAzienda;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
        return abstractCommands;
    }

    @Override
    protected boolean insertControlInScrollPane() {
        return false;
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
     *            The anagraficaBD to set.
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /**
     * @param azienda
     *            the azienda to set
     */
    public void setAzienda(Azienda azienda) {
        this.azienda = azienda;
    }

    @Override
    public void setFormObject(Object object) {
        RapportoBancarioAzienda rapporto = ((RapportoBancarioAzienda) object);
        rapporto.removeNullValueRapportoBancarioAzienda();
        if (!rapporto.isNew()) {
            object = anagraficaBD.caricaRapportoBancario(((RapportoBancarioAzienda) object).getId(), true);
        }
        super.setFormObject(object);
    }

}
