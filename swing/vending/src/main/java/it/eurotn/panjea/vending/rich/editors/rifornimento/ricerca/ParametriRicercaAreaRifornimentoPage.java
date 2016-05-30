package it.eurotn.panjea.vending.rich.editors.rifornimento.ricerca;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.vending.manager.arearifornimento.ParametriRicercaAreeRifornimento;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Page per l'inserimento dei {@link ParametriRicercaAreaRifornimento} attraverso il Form
 * {@link ParametriRicercaAreaRifornimentoForm}.
 *
 */
public class ParametriRicercaAreaRifornimentoPage extends FormBackedDialogPageEditor {

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
            ParametriRicercaAreeRifornimento parametriRicerca = new ParametriRicercaAreeRifornimento();
            setFormObject(parametriRicerca);
            ParametriRicercaAreaRifornimentoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    null);
        }

    }

    private class SearchCommand extends ActionCommand {

        private static final String COMMAND_ID = "searchCommand";

        /**
         * Costruttore.
         *
         */
        public SearchCommand() {
            super(COMMAND_ID);
            this.setSecurityControllerId(getPageEditorId() + COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        public void attach(AbstractButton button) {
            super.attach(button);
            button.setName("searchAreeRifornimentoCommand");
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaAreeRifornimento parametriRicerca = (ParametriRicercaAreeRifornimento) getBackingFormPage()
                    .getFormObject();
            parametriRicerca.setEffettuaRicerca(true);
            getForm().getFormModel().commit();
            ParametriRicercaAreaRifornimentoPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    parametriRicerca);
        }
    }

    private ResetParametriRicercaCommand resetParametriRicercaCommand;
    private SearchCommand searchCommand;
    private PanjeaFormGuard searchFormGuard;

    /**
     * Costruttore.
     */
    public ParametriRicercaAreaRifornimentoPage() {
        super("parametriRicercaAreaRifornimentoPage", new ParametriRicercaAreaRifornimentoForm());
        searchFormGuard = new PanjeaFormGuard(getBackingFormPage().getFormModel(), getSearchCommand());
    }

    @Override
    public void dispose() {
        super.dispose();

        searchFormGuard.clear();
        searchFormGuard = null;

        resetParametriRicercaCommand = null;
        searchCommand = null;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { getResetParametriRicercaCommand(), getSearchCommand() };
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getResetParametriRicercaCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getSearchCommand();
    }

    /**
     * @return resetParametriRicercaCommand
     */
    private ResetParametriRicercaCommand getResetParametriRicercaCommand() {
        if (resetParametriRicercaCommand == null) {
            resetParametriRicercaCommand = new ResetParametriRicercaCommand();
        }
        return resetParametriRicercaCommand;
    }

    /**
     * @return searchCommand
     */
    private SearchCommand getSearchCommand() {
        if (searchCommand == null) {
            searchCommand = new SearchCommand();
        }
        return searchCommand;
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
        if (!((ParametriRicercaAreeRifornimento) getBackingFormPage().getFormObject()).isEffettuaRicerca()) {
            getResetParametriRicercaCommand().execute();
        }
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
}