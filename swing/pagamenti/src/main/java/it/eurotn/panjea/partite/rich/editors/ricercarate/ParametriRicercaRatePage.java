/**
 *
 */
package it.eurotn.panjea.partite.rich.editors.ricercarate;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.Guarded;

import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Pagina che presenta i parametri ricerca rate.
 *
 * @author Leonardo
 */
public class ParametriRicercaRatePage extends FormBackedDialogPageEditor {

    /**
     * Command per lanciare la ricerca rate lanciando solo
     * firePropertyChange(OBJECT_CHANGED,formObj).
     *
     * @author Leonardo
     */
    private class CercaRateCommand extends ActionCommand implements Guarded {

        /**
         * Costruttore.
         */
        public CercaRateCommand() {
            super("searchCommand");
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            this.setSecurityControllerId(getPageEditorId() + CERCA_RATE_COMMAND);
            c.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaRatePage.logger.debug("--> Cerca rate");
            ParametriRicercaRate parametriRicerca = (ParametriRicercaRate) getBackingFormPage().getFormObject();
            parametriRicerca.setEffettuaRicerca(true);

            getForm().getFormModel().commit();
            ParametriRicercaRatePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    parametriRicerca);
        }

    }

    private static Logger logger = Logger.getLogger(ParametriRicercaRatePage.class);
    private static final String CERCA_RATE_COMMAND = ".cercaRateCommand";
    private static final String RESET_PARAMETRI_RICERCA_COMMAND = ".resetParametriRicercaCommand";
    public static final String PAGE_ID = "parametriRicercaRatePage";
    private CercaRateCommand cercaRateCommand = null;
    private AbstractCommand resetRicercaCommand = null;
    private StampaSituazioneRateCommand stampaSituazioneRateCommand;

    /**
     * Costruttore.
     *
     */
    public ParametriRicercaRatePage() {
        super(PAGE_ID, new ParametriRicercaRateForm(new ParametriRicercaRate()));
        new PanjeaFormGuard(getBackingFormPage().getFormModel(), getCercaRateCommand());
    }

    /**
     * Passo i parametriRicerca all'editor lanciando il firePropertyChange sulla proprieta'
     * IPageLifecycleAdvisor.OBJECT_CHANGED Nota che in questo command viene solo lanciato il
     * property change, la ricerca effettiva viene eseguita nella page RisultatiRicercaRatePage,
     * dove viene presentata la lista in una treeTable.
     *
     * @return il command per la ricerca rate
     */
    public CercaRateCommand getCercaRateCommand() {
        if (cercaRateCommand == null) {
            cercaRateCommand = new CercaRateCommand();
        }
        return cercaRateCommand;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getResetParametriRicercaCommand(),
                getCercaRateCommand(), getStampaSituazioneRateCommand() };
        return abstractCommands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getResetParametriRicercaCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getCercaRateCommand();
    }

    /**
     * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di
     * this (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il
     * valore di default (getBackingFormPage().getFormObject()) viene lanciata una propertychange e
     * quindi la page RisultatiRicercaRatePage esegue una ricerca e solo dopo viene lanciato il
     * propertychange con oggetto a null per azzerare le righe visualizzate (vedi doExecuteCommand
     * di this.resetRicercaCommand)
     *
     * @return nuova istanza
     */
    @Override
    protected Object getNewEditorObject() {
        return null;
    }

    /**
     * Esegue il reset dei parametri ricerca, assegnando i valori di default per ogni campo
     * visualizzato.
     *
     * @return command
     */
    public AbstractCommand getResetParametriRicercaCommand() {
        if (resetRicercaCommand == null) {
            resetRicercaCommand = new ActionCommand("resetParametriRicercaCommand") {

                @Override
                protected void doExecuteCommand() {
                    ParametriRicercaRatePage.logger.debug("--> Reset command");
                    ParametriRicercaRatePage.this.toolbarPageEditor.getNewCommand().execute();
                    ParametriRicercaRatePage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                            new ParametriRicercaRate());
                }
            };
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            resetRicercaCommand.setSecurityControllerId(getPageEditorId() + RESET_PARAMETRI_RICERCA_COMMAND);
            c.configure(resetRicercaCommand);
        }
        return resetRicercaCommand;
    }

    /**
     * @return the stampaSituazioneRateCommand
     */
    private StampaSituazioneRateCommand getStampaSituazioneRateCommand() {
        if (stampaSituazioneRateCommand == null) {
            stampaSituazioneRateCommand = new StampaSituazioneRateCommand(this);
        }

        return stampaSituazioneRateCommand;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void loadData() {

    }

    @Override
    public void onPostPageOpen() {
        // richiamo la execute del ResetParametriRicercaCommand perche' appena
        // apro
        // la pagina posso subito inserire i parametri
        // if (!(Boolean) getForm().getValueModel("effettuaRicerca").getValue())
        // {
        // getResetParametriRicercaCommand().execute();
        // }
    }

    @Override
    public boolean onPrePageOpen() {
        ((PanjeaAbstractForm) getBackingFormPage()).getFormModel().setReadOnly(false);
        return true;
    }

    /**
     * Sovrascrivo questo metodo per non eseguire nulla ed evitare il salvataggio premendo la
     * combinazione ctrl + S che e' abilitata di default nella form backed dialog page.
     *
     * @return true
     */
    @Override
    public boolean onSave() {
        return true;
    }

    /**
     * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che ? abilitato
     * di default nella form backed dialog page.
     *
     * @return true
     */
    @Override
    public boolean onUndo() {
        return true;
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void setFormObject(Object object) {
        super.setFormObject(object);
        PanjeaSwingUtil.giveFocusToComponent(getBackingFormPage().getControl().getComponents());
    }

}
