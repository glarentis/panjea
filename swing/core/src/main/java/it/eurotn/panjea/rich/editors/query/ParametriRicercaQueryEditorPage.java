package it.eurotn.panjea.rich.editors.query;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Page per l'inserimento dei {@link ParametriRicercaAreaRifornimento} attraverso il Form
 * {@link ParametriRicercaAreaRifornimentoForm}.
 *
 */
public class ParametriRicercaQueryEditorPage<T> extends FormBackedDialogPageEditor {

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

        @SuppressWarnings("unchecked")
        @Override
        protected void doExecuteCommand() {
            ParametriRicercaQueryEditorPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    ((QueryEditorForm<T>) getForm()).getSeachRow());
        }
    }

    private class ToggleUnusedTreePropertyCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public ToggleUnusedTreePropertyCommand() {
            super("toggleUnusedTreePropertyCommand");
            RcpSupport.configure(this);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void doExecuteCommand() {
            ((QueryEditorForm<T>) getForm()).toggleTreePropertyFilter();
        }

    }

    private ResetParametriRicercaCommand resetParametriRicercaCommand;
    private SearchCommand searchAreaCommand;
    private ToggleUnusedTreePropertyCommand toggleUnusedTreePropertyCommand;

    /**
     *
     * @param idPage
     *            id della pagina
     * @param ricercaForm
     *            form con i controlli per la ricerca
     */
    public ParametriRicercaQueryEditorPage(final String idPage, final QueryEditorForm<T> ricercaForm) {
        super(idPage, ricercaForm);
        new PanjeaFormGuard(getBackingFormPage().getFormModel(), getSearchAreaCommand());
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { getResetParametriRicercaCommand(), getSearchAreaCommand(),
                getToggleUnusedTreePropertyCommand() };
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getResetParametriRicercaCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getSearchAreaCommand();
    }

    /**
     * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di
     * this (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il
     * valore di default (getBackingFormPage().getFormObject()) viene lanciata una propertychange e
     * quindi la page RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo
     * dopo viene lanciato il propertychange con oggetto a null per azzerare le righe visualizzate
     * (vedi doExecuteCommand di this.resetRicercaCommand).
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
     * @return the searchAreaCommand
     */
    protected SearchCommand getSearchAreaCommand() {
        if (searchAreaCommand == null) {
            searchAreaCommand = new SearchCommand();
        }
        return searchAreaCommand;
    }

    /**
     * @return the toggleUnusedTreePropertyCommand
     */
    private ToggleUnusedTreePropertyCommand getToggleUnusedTreePropertyCommand() {
        if (toggleUnusedTreePropertyCommand == null) {
            toggleUnusedTreePropertyCommand = new ToggleUnusedTreePropertyCommand();
        }

        return toggleUnusedTreePropertyCommand;
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
     * Sovrascrivo questo metodo per non eseguire nulla ed evitare il salvataggio premendo la
     * combinazione ctrl + S che è abilitata di default nella form backed dialog page.
     *
     * @return <code>true</code>
     */
    @Override
    public boolean onSave() {
        return true;
    }

    /**
     * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che è abilitato
     * di default nella form backed dialog page.
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

    @Override
    public void setFormObject(Object object) {
        if (!(object instanceof String)) {
            // super.setFormObject(new AreaRifornimento());
            // } else {
            super.setFormObject(object);
        }
    }
}
