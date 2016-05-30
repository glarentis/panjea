package it.eurotn.panjea.fatturepa.rich.editors.ricerca;

import javax.swing.AbstractButton;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.util.Assert;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.fatturepa.util.ParametriRicercaFatturePA;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class ParametriRicercaFatturePAPage extends FormBackedDialogPageEditor implements InitializingBean {

    private class ResetParametriRicercaCommand extends ActionCommand {

        private static final String COMMAND_ID = "resetCommand";

        /**
         * Costruttore.
         *
         */
        public ResetParametriRicercaCommand() {
            super(COMMAND_ID);
            this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
            RcpSupport.configure(this);

        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaFatturePAPage.this.toolbarPageEditor.getNewCommand().execute();
            ((ParametriRicercaFatturePA) getBackingFormPage().getFormObject()).getDataRegistrazione()
                    .setTipoPeriodo(TipoPeriodo.NESSUNO);
            ((ParametriRicercaFatturePA) getBackingFormPage().getFormObject()).getDataDocumento()
                    .setTipoPeriodo(TipoPeriodo.MESE_CORRENTE);
            ParametriRicercaFatturePAPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, null);
        }

    }

    private class SearchFatturePACommand extends ActionCommand {

        private static final String COMMAND_ID = "searchCommand";

        /**
         * Costruttore.
         *
         */
        public SearchFatturePACommand() {
            super(COMMAND_ID);
            this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        public void attach(AbstractButton button) {
            super.attach(button);
            button.setName("searchFatturePACommand");
        }

        @Override
        protected void doExecuteCommand() {
            LOGGER.debug("--> Enter doExecuteCommand");
            ParametriRicercaFatturePA parametriRicerca = (ParametriRicercaFatturePA) getBackingFormPage()
                    .getFormObject();
            parametriRicerca.setEffettuaRicerca(true);
            getForm().getFormModel().commit();
            ParametriRicercaFatturePAPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    parametriRicerca);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(ParametriRicercaFatturePAPage.class);

    private static final String PAGE_ID = "parametriRicercaFatturePAPage";
    private AziendaCorrente aziendaCorrente;
    private ResetParametriRicercaCommand resetParametriRicercaCommand;
    private SearchFatturePACommand searchFatturePACommand;
    private PanjeaFormGuard searchAreaMagazzinoFormGuard;

    /**
     * Costruttore.
     */
    public ParametriRicercaFatturePAPage() {
        super(PAGE_ID, new ParametriRicercaFatturePAForm());
        searchAreaMagazzinoFormGuard = new PanjeaFormGuard(getBackingFormPage().getFormModel(),
                getSearchFatturePACommand());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(aziendaCorrente, "aziendaCorrente deve essere assegnato");
        ParametriRicercaFatturePAForm parametriRicercaFatturePAForm = (ParametriRicercaFatturePAForm) getBackingFormPage();
        parametriRicercaFatturePAForm.setAziendaCorrente(aziendaCorrente);
    }

    @Override
    public void dispose() {
        super.dispose();

        searchAreaMagazzinoFormGuard.clear();
        searchAreaMagazzinoFormGuard = null;

        resetParametriRicercaCommand = null;
        searchFatturePACommand = null;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { getResetParametriRicercaCommand(), getSearchFatturePACommand() };
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getResetParametriRicercaCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getSearchFatturePACommand();
    }

    /**
     * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di this
     * (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il valore di default
     * (getBackingFormPage().getFormObject()) viene lanciata una propertychange e quindi la page
     * RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo dopo viene lanciato il propertychange
     * con oggetto a null per azzerare le righe visualizzate (vedi doExecuteCommand di this.resetRicercaCommand)
     *
     * @return nuovo oggetto
     */
    @Override
    protected Object getNewEditorObject() {
        return null;
    }

    /**
     * @return resetParametriRicercaCommand
     */
    protected ResetParametriRicercaCommand getResetParametriRicercaCommand() {
        if (resetParametriRicercaCommand == null) {
            resetParametriRicercaCommand = new ResetParametriRicercaCommand();
        }
        return resetParametriRicercaCommand;
    }

    /**
     * @return the searchFatturePACommand
     */
    private SearchFatturePACommand getSearchFatturePACommand() {
        if (searchFatturePACommand == null) {
            searchFatturePACommand = new SearchFatturePACommand();
        }

        return searchFatturePACommand;
    }

    @Override
    protected boolean insertControlInScrollPane() {
        return false;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void loadData() {
        if (!((ParametriRicercaFatturePA) getBackingFormPage().getFormObject()).isEffettuaRicerca()) {
            getResetParametriRicercaCommand().execute();
        }
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
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
     * @return true
     */
    @Override
    public boolean onSave() {
        return true;
    }

    /**
     * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che è abilitato di default nella form
     * backed dialog page.
     *
     * @return true
     */
    @Override
    public boolean onUndo() {
        return true;
    }

    @Override
    public void refreshData() {
        loadData();
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

}
