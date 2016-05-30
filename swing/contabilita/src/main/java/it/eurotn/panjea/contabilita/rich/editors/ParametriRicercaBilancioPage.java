package it.eurotn.panjea.contabilita.rich.editors;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.Guarded;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.rich.forms.ParametriRicercaBilancioForm;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class ParametriRicercaBilancioPage extends FormBackedDialogPageEditor {

    private class CaricaBilancioCommand extends ActionCommand implements Guarded {

        /**
         * Costruttore.
         *
         */
        public CaricaBilancioCommand() {
            super("searchCommand");
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            this.setSecurityControllerId(getPageEditorId() + CARICA_BILANCIO_COMMAND);
            c.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaBilancioPage.logger.debug("--> Carica bilancio");
            getBackingFormPage().commit();
            ParametriRicercaBilancio parametriRicercaBilancio = (ParametriRicercaBilancio) getBackingFormPage()
                    .getFormObject();
            parametriRicercaBilancio.setEffettuaRicerca(true);
            ParametriRicercaBilancioPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    parametriRicercaBilancio);
        }
    }

    private static Logger logger = Logger.getLogger(ParametriRicercaBilancioPage.class);
    private static final String PAGE_ID = "parametriRicercaBilancioPage";
    private static final String RESET_PARAMETRI_RICERCA_COMMAND = ".resetParametriRicercaCommand";
    private static final String CARICA_BILANCIO_COMMAND = ".caricaBilancioCommand";
    private AbstractCommand resetRicercaCommand;

    private AbstractCommand caricaBilancioCommand;

    /**
     * Costruttore.
     *
     * @param parametriRicercaBilancio
     *            parametri di ricerca
     * @param aziendaCorrente
     *            azienda corrente
     */
    public ParametriRicercaBilancioPage(final ParametriRicercaBilancio parametriRicercaBilancio,
            final AziendaCorrente aziendaCorrente) {
        super(PAGE_ID, new ParametriRicercaBilancioForm(parametriRicercaBilancio, aziendaCorrente));
        new PanjeaFormGuard(getBackingFormPage().getFormModel(), getCaricaBilancioCommand());
    }

    /**
     * Passa i parametriRicercaBilancio all'editor lanciando il firePropertyChange sulla propriet�
     * IPageLifecycleAdvisor.OBJECT_CHANGED Nota che in questo command viene solo lanciato il
     * property change, la ricerca effettiva viene eseguita nella page RisultatiRicercaBilancioPage,
     * dove viene presentata la lista in una jTable.
     *
     * @return il command per la ricerca righe contabili
     */
    public AbstractCommand getCaricaBilancioCommand() {
        if (caricaBilancioCommand == null) {
            caricaBilancioCommand = new CaricaBilancioCommand();
        }
        return caricaBilancioCommand;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getResetParametriRicercaCommand(),
                getCaricaBilancioCommand() };
        return abstractCommands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getResetParametriRicercaCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getCaricaBilancioCommand();
    }

    /**
     * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di
     * this (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il
     * valore di default (getBackingFormPage().getFormObject()) viene lanciata una propertychange e
     * quindi la page RisultatiRicercaControlloMovimentoContabilitaPage esegue una ricerca e solo
     * dopo viene lanciato il propertychange con oggetto a null per azzerare le righe visualizzate
     * (vedi doExecuteCommand di this.resetRicercaCommand)
     *
     * @return new object
     */
    @Override
    protected Object getNewEditorObject() {
        return null;
    }

    /**
     * Esegue il reset dei parametri ricerca, assegnando i valori di default per ogni campo
     * visualizzato.
     *
     * @return resetRicercaCommand
     */
    public AbstractCommand getResetParametriRicercaCommand() {
        if (resetRicercaCommand == null) {
            resetRicercaCommand = new ActionCommand("resetParametriRicercaCommand") {

                @Override
                protected void doExecuteCommand() {
                    ParametriRicercaBilancioPage.logger.debug("--> Reset command");
                    // lancio null Object_changed per ripulire i risultati
                    ParametriRicercaBilancioPage.this.toolbarPageEditor.getNewCommand().execute();
                    ParametriRicercaBilancioPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                            null);
                    ((ParametriRicercaBilancioForm) getBackingFormPage()).getFormModel().setReadOnly(false);
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
     * Sovrascritto per dare il focus alla data invece che al primo campo disponibile come avviene
     * di default sulla formbackeddialogpageeditor.
     */
    @Override
    public void grabFocus() {
        ((ParametriRicercaBilancioForm) getBackingFormPage()).requestFocusForData();
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void loadData() {
        getResetParametriRicercaCommand().execute();
    }

    @Override
    public void onPostPageOpen() {
        ParametriRicercaBilancio parametriRicercaBilancio = (ParametriRicercaBilancio) getForm().getFormObject();
        if (!parametriRicercaBilancio.isEffettuaRicerca()) {
            // richiamo la execute del ResetParametriRicercaCommand perche' appena apro
            // la pagina posso subito inserire i parametri
            getResetParametriRicercaCommand().execute();
        } else {
            // richiamo la execute di EseguiRicercaCommand per effettuare immediatamente la ricerca
            // all'apertuta della
            // Page
            getCaricaBilancioCommand().execute();
        }
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
     * @return onSave
     */
    @Override
    public boolean onSave() {
        return true;
    }

    /**
     * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che � abilitato
     * di default nella form backed dialog page.
     *
     * @return onUndo
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
