package it.eurotn.panjea.manutenzioni.rich.editors.areeinstallazioni.ricerca;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.manutenzioni.manager.areeinstallazioni.ParametriRicercaAreeInstallazione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Page per l'inserimento dei {@link ParametriRicercaAreaOrdine} attraverso il Form
 * {@link ParametriRicercaAreaOrdineForm}.
 *
 * @author adriano
 * @version 1.0, 30/set/2008
 */
public class ParametriRicercaAreaInstallazionePage extends FormBackedDialogPageEditor {

    private class ResetParametriRicercaCommand extends ActionCommand {

        private static final String COMMAND_ID = "resetCommand";

        /**
         * Costruttore.
         */
        public ResetParametriRicercaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            getNewCommand().execute();
        }

    }

    private class SearchCommand extends ActionCommand {

        private static final String COMMAND_ID = "searchCommand";

        /**
         * Costruttore.
         */
        public SearchCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaAreeInstallazione parametriRicerca = (ParametriRicercaAreeInstallazione) getBackingFormPage()
                    .getFormObject();
            parametriRicerca.setEffettuaRicerca(true);
            getForm().getFormModel().commit();
            ParametriRicercaAreaInstallazionePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    parametriRicerca);
        }
    }

    private static final String PAGE_ID = "parametriRicercaAreeInstallazionePage";

    private ResetParametriRicercaCommand resetParametriRicercaCommand;
    private SearchCommand searchAreaOrdineCommand;

    /**
     * Costruttore.
     */
    public ParametriRicercaAreaInstallazionePage() {
        super(PAGE_ID, new ParametriRicercaAreaInstallazioneForm(new ParametriRicercaAreeInstallazione()));
        new PanjeaFormGuard(getBackingFormPage().getFormModel(), getSearchAreaOrdineCommand());
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { getResetParametriRicercaCommand(), getSearchAreaOrdineCommand() };
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getResetParametriRicercaCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getSearchAreaOrdineCommand();
    }

    /**
     * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di this
     * (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il valore di default
     * (getBackingFormPage().getFormObject()) viene lanciata una propertychange e quindi la page
     * RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo dopo viene lanciato il propertychange
     * con oggetto a null per azzerare le righe visualizzate (vedi doExecuteCommand di this.resetRicercaCommand).
     *
     * @return new object
     */
    @Override
    protected Object getNewEditorObject() {
        return null;
    }

    /**
     * @return the resetParametriRicercaCommand
     **/
    protected ResetParametriRicercaCommand getResetParametriRicercaCommand() {
        if (resetParametriRicercaCommand == null) {
            resetParametriRicercaCommand = new ResetParametriRicercaCommand();
        }
        return resetParametriRicercaCommand;
    }

    /**
     * @return the searchAreaOrdineCommand
     */
    protected SearchCommand getSearchAreaOrdineCommand() {
        if (searchAreaOrdineCommand == null) {
            searchAreaOrdineCommand = new SearchCommand();
        }
        return searchAreaOrdineCommand;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void loadData() {
        // nu
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public boolean onPrePageOpen() {
        ((PanjeaAbstractForm) getBackingFormPage()).getFormModel().setReadOnly(false);
        return true;
    }

    /**
     * Sovrascrivo questo metodo per non eseguire nulla ed evitare il salvataggio premendo la combinazione ctrl + S che
     * è abilitata di default nella form backed dialog page.
     *
     * @return <code>true</code>
     */
    @Override
    public boolean onSave() {
        return true;
    }

    /**
     * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che è abilitato di default nella form
     * backed dialog page.
     *
     * @return <code>true</code>
     */
    @Override
    public boolean onUndo() {
        return true;
    }

    @Override
    public void refreshData() {
        loadData();
    }
}
