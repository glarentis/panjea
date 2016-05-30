package it.eurotn.panjea.contabilita.rich.editors;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.Guarded;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.rich.forms.ParametriRicercaSituazioneEPForm;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSituazioneEP;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 *
 * @author giangi,Leonardo
 */
public class ParametriRicercaSituazioneEPPage extends FormBackedDialogPageEditor {

    private class CaricaSituazioneCommand extends ActionCommand implements Guarded {

        /**
         * Costruttore.
         *
         */
        public CaricaSituazioneCommand() {
            super("searchCommand");
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            this.setSecurityControllerId(getPageEditorId() + CARICA_SITUAZIONE_COMMAND);
            c.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaSituazioneEPPage.logger.debug("--> Carica situazione EP");
            getBackingFormPage().commit();
            ParametriRicercaSituazioneEP parametriRicercaSituazioneEP = (ParametriRicercaSituazioneEP) getBackingFormPage()
                    .getFormObject();
            parametriRicercaSituazioneEP.setEffettuaRicerca(true);
            ParametriRicercaSituazioneEPPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    parametriRicercaSituazioneEP);
        }
    }

    private static Logger logger = Logger.getLogger(ParametriRicercaSituazioneEPPage.class);
    private static final String PAGE_ID = "parametriRicercaSituazioneEPPage";
    private static final String RESET_PARAMETRI_RICERCA_COMMAND = ".resetParametriRicercaCommand";
    private static final String CARICA_SITUAZIONE_COMMAND = ".caricaSituazioneCommand";
    private AbstractCommand resetRicercaCommand = null;

    private AbstractCommand caricaSituazioneEPCommand = null;

    /**
     * Costruttore.
     *
     * @param parametriRicercaSituazioneEP
     *            parametri di ricerca
     * @param aziendaCorrente
     *            azienda corrente
     */
    public ParametriRicercaSituazioneEPPage(final ParametriRicercaSituazioneEP parametriRicercaSituazioneEP,
            final AziendaCorrente aziendaCorrente) {
        super(PAGE_ID, new ParametriRicercaSituazioneEPForm(parametriRicercaSituazioneEP, aziendaCorrente));
        new PanjeaFormGuard(getBackingFormPage().getFormModel(), getCaricaSituazioneCommand());
    }

    /**
     * Passa i parametriRicercaSituazioneEP all'editor lanciando il firePropertyChange sulla
     * propriet� IPageLifecycleAdvisor.OBJECT_CHANGED Nota che in questo command viene solo lanciato
     * il property change, la ricerca effettiva viene eseguita nella page
     * RisultatiRicercaSituazioneEPPage, dove viene presentata la lista in una jTable.
     *
     * @return il command per la ricerca righe contabili
     */
    public AbstractCommand getCaricaSituazioneCommand() {
        if (caricaSituazioneEPCommand == null) {
            caricaSituazioneEPCommand = new CaricaSituazioneCommand();
        }
        return caricaSituazioneEPCommand;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getResetParametriRicercaCommand(),
                getCaricaSituazioneCommand() };
        return abstractCommands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getResetParametriRicercaCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getCaricaSituazioneCommand();
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
                    ParametriRicercaSituazioneEPPage.logger.debug("--> Reset command");
                    // lancio null Object_changed per ripulire i risultati
                    ParametriRicercaSituazioneEPPage.this.toolbarPageEditor.getNewCommand().execute();
                    ParametriRicercaSituazioneEPPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                            null);
                    ((ParametriRicercaSituazioneEPForm) getBackingFormPage()).getFormModel().setReadOnly(false);
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
        ((ParametriRicercaSituazioneEPForm) getBackingFormPage()).requestFocusForData();
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
        // richiamo la execute del ResetParametriRicercaCommand perch� appena apro
        // la pagina posso subito inserire i parametri
        getResetParametriRicercaCommand().execute();
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
     * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che è abilitato
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
