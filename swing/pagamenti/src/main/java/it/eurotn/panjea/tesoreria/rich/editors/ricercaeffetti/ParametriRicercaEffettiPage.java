/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.core.Guarded;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.tesoreria.domain.Effetto.StatoEffetto;
import it.eurotn.panjea.tesoreria.rich.editors.ricercaeffetti.EffettiLayoutManager.Mode;
import it.eurotn.panjea.tesoreria.rich.forms.ricercaeffetti.ParametriRicercaEffettiForm;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaEffetti;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * @author Leonardo
 *
 */
public class ParametriRicercaEffettiPage extends FormBackedDialogPageEditor {

    /**
     * Command per lanciare la ricerca aree partite lanciando solo
     * firePropertyChange(OBJECT_CHANGED,formObj).
     * 
     * @author Leonardo
     */
    private class CercaEffettiCommand extends ActionCommand implements Guarded {

        /**
         * Costruttore.
         */
        public CercaEffettiCommand() {
            super("searchCommand");
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            this.setSecurityControllerId(getPageEditorId() + CERCA_AREE_PARTITE_COMMAND);
            c.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            ParametriRicercaEffettiPage.logger.debug("--> Cerca aree partite");
            ParametriRicercaEffetti parametriRicercaEffetti = (ParametriRicercaEffetti) getBackingFormPage()
                    .getFormObject();
            parametriRicercaEffetti.setEffettuaRicerca(true);
            getForm().getFormModel().commit();
            ParametriRicercaEffettiPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                    getBackingFormPage().getFormObject());
        }

    }

    private static Logger logger = Logger.getLogger(ParametriRicercaEffettiPage.class);
    private static final String CERCA_AREE_PARTITE_COMMAND = ".cercaEffettiCommand";
    private static final String RESET_PARAMETRI_RICERCA_COMMAND = ".resetParametriRicercaCommand";
    public static final String PAGE_ID = "parametriRicercaEffettiPage";
    private CercaEffettiCommand cercaEffettiCommand = null;
    private AbstractCommand resetRicercaCommand = null;

    private Mode mode;

    /**
     * Costruttore.
     * 
     * @param aziendaCorrente
     *            azienda corrente
     */
    public ParametriRicercaEffettiPage(final AziendaCorrente aziendaCorrente) {
        super(PAGE_ID, new ParametriRicercaEffettiForm(aziendaCorrente));
        new PanjeaFormGuard(getBackingFormPage().getFormModel(), getCercaEffettiCommand());
    }

    /**
     * Passo i parametriRicerca all'editor lanciando il firePropertyChange sulla proprieta'
     * IPageLifecycleAdvisor.OBJECT_CHANGED Nota che in questo command viene solo lanciato il
     * property change, la ricerca effettiva viene eseguita nella page
     * RisultatiRicercaAreePartitePage, dove viene presentata la lista in una treeTable.
     * 
     * @return il command per la ricerca aree partite
     */
    public ActionCommand getCercaEffettiCommand() {
        if (cercaEffettiCommand == null) {
            cercaEffettiCommand = new CercaEffettiCommand();
        }
        return cercaEffettiCommand;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getResetParametriRicercaCommand(),
                getCercaEffettiCommand() };
        return abstractCommands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return getResetParametriRicercaCommand();
    }

    @Override
    public AbstractCommand getEditorSaveCommand() {
        return getCercaEffettiCommand();
    }

    /**
     * vedi bug 440 Sovrascrivo il metodo ritornando null per evitare il normale comportamento di
     * this (FormBackedDialogPageEditor). Questo metodo e' usato nel metodo onNew() e se ritorna il
     * valore di default (getBackingFormPage().getFormObject()) viene lanciata una propertychange e
     * quindi la page RisultatiRicercaAreePartitePage esegue una ricerca e solo dopo viene lanciato
     * il propertychange con oggetto a null per azzerare le righe visualizzate (vedi
     * doExecuteCommand di this.resetRicercaCommand)
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
     * @return comando per il reset dei parametri
     */
    public AbstractCommand getResetParametriRicercaCommand() {
        if (resetRicercaCommand == null) {
            resetRicercaCommand = new ActionCommand("resetParametriRicercaCommand") {

                @Override
                protected void doExecuteCommand() {
                    ParametriRicercaEffettiPage.logger.debug("--> Reset command");
                    ParametriRicercaEffettiPage.this.toolbarPageEditor.getNewCommand().execute();
                    ParametriRicercaEffettiPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                            null);
                }
            };
            CommandConfigurer c = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            resetRicercaCommand.setSecurityControllerId(getPageEditorId() + RESET_PARAMETRI_RICERCA_COMMAND);
            c.configure(resetRicercaCommand);
        }
        return resetRicercaCommand;
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
        // evita di inizializzare i parametri se arriva un oggetto dall'esterno con i dati impostati
        // per la ricerca
        if (!((ParametriRicercaEffetti) getBackingFormPage().getFormObject()).isEffettuaRicerca()) {
            // richiamo la execute del ResetParametriRicercaCommand perche' appena apro
            // la pagina posso subito inserire i parametri
            getResetParametriRicercaCommand().execute();
        }
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
     * @return <code>true</code> se il salvataggio è andato a buon fine
     */
    @Override
    public boolean onSave() {
        return true;
    }

    /**
     * Sovrascrivo questo metodo per non eseguire l'undo command premendo ctrl + Z che ? abilitato
     * di default nella form backed dialog page.
     * 
     * @return boolean
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
     * @param mode
     *            the mode to set
     */
    public void setMode(Mode mode) {
        this.mode = mode;

        ValidatingFormModel formModel = getBackingFormPage().getFormModel();

        // resetto i parametri di ricerca
        getResetParametriRicercaCommand().execute();
        // formModel.getFieldMetadata("statiEffetto").setEnabled(true);
        // formModel.getFieldMetadata("daImporto").setEnabled(true);
        // formModel.getFieldMetadata("AImporto").setEnabled(true);
        // formModel.getFieldMetadata("entita").setReadOnly(false);
        // formModel.getFieldMetadata("numeroDocumento").setEnabled(true);

        List<StatoEffetto> statiEffetto;
        switch (this.mode) {
        case ACCREDITI:
        case ANTICIPI:
            statiEffetto = new ArrayList<StatoEffetto>();
            statiEffetto.add(StatoEffetto.PRESENTATO);
            statiEffetto.add(StatoEffetto.INSOLUTO);
            formModel.getValueModel("statiEffetto").setValue(statiEffetto);

            // formModel.getFieldMetadata("statiEffetto").setEnabled(false);
            // formModel.getFieldMetadata("daImporto").setEnabled(false);
            // formModel.getFieldMetadata("AImporto").setEnabled(false);
            // formModel.getFieldMetadata("entita").setReadOnly(true);
            // formModel.getFieldMetadata("numeroDocumento").setEnabled(false);
            break;
        case INSOLUTI:
            statiEffetto = new ArrayList<StatoEffetto>();
            statiEffetto.add(StatoEffetto.PRESENTATO);
            statiEffetto.add(StatoEffetto.CHIUSO);
            formModel.getValueModel("statiEffetto").setValue(statiEffetto);

            // formModel.getFieldMetadata("statiEffetto").setEnabled(false);
            break;
        default:
            // throw new UnsupportedOperationException("Mode non supportato");
            break;
        }
    }

}
