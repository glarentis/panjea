package it.eurotn.panjea.anagrafica.rich.editors.entita.anagrafica;

import java.util.Locale;

import javax.swing.AbstractButton;

import org.apache.log4j.Logger;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.DefaultMessageAreaPane;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.editors.entita.OpenMapCommand;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.rich.command.JideToggleCommand;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;

/**
 * {@link FormBackedDialogPageEditor} di {@link Anagrafica}.
 *
 * @author adriano
 * @version 1.0, 18/dic/07
 */
public class AnagraficaPage extends FormsBackedTabbedDialogPageEditor {

    public class AggiornamentoAutomaticoInformazioniAnagraficaCommand extends JideToggleCommand {

        public static final String AGGIORNAMENTO_AUTOMATICO_INFORMAZIONI_ANAGRAFICA = "aggiornamentoAutomaticoInformazioniAnagrafica";

        public static final String COMMAND_ID = "aggiornamentoAutomaticoInformazioniAnagraficaCommand";

        private SettingsManager settingsManager;

        /**
         * Costruttore.
         */
        public AggiornamentoAutomaticoInformazioniAnagraficaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
            settingsManager = (SettingsManager) ApplicationServicesLocator.services().getService(SettingsManager.class);
        }

        @Override
        protected void onDeselection() {
            super.onDeselection();
            try {
                settingsManager.getUserSettings().setBoolean(AGGIORNAMENTO_AUTOMATICO_INFORMAZIONI_ANAGRAFICA,
                        Boolean.FALSE);
            } catch (SettingsException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("--> " + e.getMessage());
                }
            }
        }

        @Override
        protected void onSelection() {
            super.onSelection();
            try {
                settingsManager.getUserSettings().setBoolean(AGGIORNAMENTO_AUTOMATICO_INFORMAZIONI_ANAGRAFICA,
                        Boolean.TRUE);
            } catch (SettingsException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("--> " + e.getMessage());
                }
            }
        }
    }

    private class DeleteCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public DeleteCommand() {
            super(DELETE_COMMAND);

            this.setSecurityControllerId(getPageSecurityEditorId() + ".controller");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            final Entita entitaResult = (Entita) getBackingFormPage().getFormObject();
            MessageSourceAccessor messageSourceAccessor = (MessageSourceAccessor) ApplicationServicesLocator.services()
                    .getService(MessageSourceAccessor.class);
            Object[] parameters = new Object[] { messageSourceAccessor
                    .getMessage(entitaResult.getDomainClassName(), new Object[] {}, Locale.getDefault()).toLowerCase(),
                    entitaResult.getAnagrafica().getDenominazione() };
            String titolo = messageSourceAccessor.getMessage("entita.delete.confirm.title", new Object[] {},
                    Locale.getDefault());
            String messaggio = messageSourceAccessor.getMessage("entita.delete.confirm.message", parameters,
                    Locale.getDefault());
            ConfirmationDialog dialog = new ConfirmationDialog(titolo, messaggio) {

                @Override
                protected void onConfirm() {
                    if (entitaResult.getId() != null) {
                        getAnagraficaBD().cancellaEntita(entitaResult);
                        AnagraficaPage.this.publishDeleteEvent(getBackingFormPage().getFormObject());
                    }
                }
            };
            DefaultMessageAreaPane areaPane = new DefaultMessageAreaPane();
            areaPane.setMessage(new DefaultMessage(messaggio));
            dialog.setPreferredSize(areaPane.getControl().getPreferredSize());
            dialog.showDialog();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(PAGE_ID + "." + DELETE_COMMAND);
        }
    }

    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger(AnagraficaPage.class);
    private static final String PAGE_ID = "anagraficaPage";
    private static final String DELETE_COMMAND = "deleteCommand";
    private IAnagraficaBD anagraficaBD;

    private DeleteCommand deleteCommand = null;
    private AggiornamentoAutomaticoInformazioniAnagraficaCommand aggiornamentoAutomaticoInformazioniAnagraficaCommand = null;
    private OpenMapCommand openMapCommand;

    /**
     * Costruttore.
     *
     * @param entita
     *            entità
     * @param anagraficaBD
     *            bd per l'anagrafica
     */
    public AnagraficaPage(final Entita entita, final IAnagraficaBD anagraficaBD) {
        this(anagraficaBD, new AnagraficaForm(entita, anagraficaBD));
    }

    /**
     * Costruttore.
     *
     * @param anagraficaBD
     *            bd per l'anagrafica
     * @param anagraficaForm
     *            form per l'anagrafica
     */
    public AnagraficaPage(final IAnagraficaBD anagraficaBD, final AnagraficaForm anagraficaForm) {
        super(PAGE_ID, anagraficaForm);
        this.anagraficaBD = anagraficaBD;
        deleteCommand = new DeleteCommand();
    }

    @Override
    public void addForms() {

        PersonaFisicaGiuridicaForm personaFisicaGiuridicaForm = new PersonaFisicaGiuridicaForm(
                getBackingFormPage().getFormModel(), "personaFisicaGiuridicaForm");
        addForm(personaFisicaGiuridicaForm);

        LegaleRappresentanteForm legaleRappresentanteForm = new LegaleRappresentanteForm(
                getBackingFormPage().getFormModel(), "legaleRappresentanteForm");
        addForm(legaleRappresentanteForm);
    }

    /**
     * USE CASE: nuova entità 2 volte problema: <br>
     * a.clicco su nuova entit� da anagrafica esistente (entit� E1, anagrafica A1 v1) <br>
     * b.clicco di nuovo su nuova entit� da anagrafica esistente (stesso tipo entit� E2, anagrafica A1 v1) <br>
     * c.mi chiede di salvare le modifiche in sospeso (salvare E1 con A1 ?)<br>
     * d.rispondo si (E1 � salvato, A1 v2)<br>
     * e.ho una nuova entit� con l'anagrafica gi� utilizzata dall'entit� salvata (E2 con A1). <br>
     * f.salvo (voglio salvare E2 con A1) <br>
     * g.errore StaleObjectStateException TODO correggere Le operazioni qui riportate portano al salvataggio di due
     * entit� dello stesso tipo con la stessa anagrafica, cosa questa che si vuole assolutamente evitare.
     *
     * @return entità da salvare
     */
    @Override
    protected Object doSave() {

        Entita entitaDaSalvare = ((Entita) getBackingFormPage().getFormObject());
        Entita entitaResult = null;
        try {
            entitaResult = getAnagraficaBD().salvaEntita(entitaDaSalvare);

            // TODO verificare la necessita di questo setformobj
            getBackingFormPage().setFormObject(entitaResult);
            entitaResult.setCodicePrecedente(null);
        } catch (AnagraficheDuplicateException e) {
            MessageDialog messagio = new MessageDialog("ERRORE", "partita  iva e  codice fiscale duplicati");
            messagio.showDialog();
            getNewCommand().execute();
            entitaResult = (Entita) getForm().getFormObject();
        }

        return entitaResult;
    }

    /**
     * @return Returns the aggiornamentoAutomaticoInformazioniAnagraficaCommand.
     */
    public AggiornamentoAutomaticoInformazioniAnagraficaCommand getAggiornamentoAutomaticoInformazioniAnagraficaCommand() {
        if (aggiornamentoAutomaticoInformazioniAnagraficaCommand == null) {
            aggiornamentoAutomaticoInformazioniAnagraficaCommand = new AggiornamentoAutomaticoInformazioniAnagraficaCommand();
        }
        return aggiornamentoAutomaticoInformazioniAnagraficaCommand;
    }

    /**
     *
     * @return bd dell'anagrafica
     */
    public IAnagraficaBD getAnagraficaBD() {
        return anagraficaBD;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        // aggiungo i comandi base
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), deleteCommand,
                getAggiornamentoAutomaticoInformazioniAnagraficaCommand(), getOpenMapCommand() };
        return abstractCommands;
    }

    /**
     *
     * @return comand per aprire la mappa della sede
     */
    public OpenMapCommand getOpenMapCommand() {
        if (openMapCommand == null) {
            openMapCommand = new OpenMapCommand();
            openMapCommand.addCommandInterceptor(new ActionCommandInterceptor() {

                @Override
                public void postExecution(ActionCommand arg0) {
                }

                @Override
                public boolean preExecution(ActionCommand arg0) {
                    Entita entita = ((Entita) getBackingFormPage().getFormObject());
                    arg0.addParameter(OpenMapCommand.SEDE_ANAGRAFICA, entita.getAnagrafica().getSedeAnagrafica());
                    return true;
                }
            });
        }
        return openMapCommand;
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
    public void postSetFormObject(Object object) {
        ((AnagraficaForm) getBackingFormPage()).activateListeners();
    }

    @Override
    public void preSetFormObject(Object object) {
        ((AnagraficaForm) getBackingFormPage()).deactivateListeners();
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void restoreState(Settings settings) {
        super.restoreState(settings);

        if (!settings.contains(
                AggiornamentoAutomaticoInformazioniAnagraficaCommand.AGGIORNAMENTO_AUTOMATICO_INFORMAZIONI_ANAGRAFICA)) {
            settings.setBoolean(
                    AggiornamentoAutomaticoInformazioniAnagraficaCommand.AGGIORNAMENTO_AUTOMATICO_INFORMAZIONI_ANAGRAFICA,
                    Boolean.TRUE);
        }

        Boolean aggiornamentoAnagrafica = settings.getBoolean(
                AggiornamentoAutomaticoInformazioniAnagraficaCommand.AGGIORNAMENTO_AUTOMATICO_INFORMAZIONI_ANAGRAFICA);
        getAggiornamentoAutomaticoInformazioniAnagraficaCommand()
                .setSelected(aggiornamentoAnagrafica == null || aggiornamentoAnagrafica);
    }

    @Override
    public void updateCommands() {
        super.updateCommands();
        boolean isLockEnabled = toolbarPageEditor.getLockCommand().isEnabled();
        Entita entita = (Entita) ((AnagraficaForm) getBackingFormPage()).getFormObject();
        deleteCommand.setEnabled(isLockEnabled && entita.getId() != null);
    }
}
