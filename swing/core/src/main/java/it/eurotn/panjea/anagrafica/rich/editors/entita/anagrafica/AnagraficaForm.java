package it.eurotn.panjea.anagrafica.rich.editors.entita.anagrafica;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.dialog.MessageDialog;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsManager;
import org.springframework.richclient.util.GuiStandardUtils;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;
import org.springframework.rules.constraint.Constraint;

import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;
import com.jidesoft.swing.DefaultOverlayable;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.editors.entita.anagrafica.AnagraficaPage.AggiornamentoAutomaticoInformazioniAnagraficaCommand;
import it.eurotn.panjea.anagrafica.rich.editors.entita.builder.AnagraficaWebBuilder;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita.FieldSearch;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.binding.DatiGeograficiBinding;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.dialog.InputApplicationDialog;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormLayoutBuilder;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * Form di {@link Anagrafica}.
 *
 * @author adriano
 */
public class AnagraficaForm extends PanjeaAbstractForm implements Focussable {

    private class AggiornaInformazioniAnagraficaCommand extends ActionCommand {

        public static final String COMMAND_ID = "aggiornaInformazioniAnagraficaCommand";

        /**
         * Costruttore.
         */
        public AggiornaInformazioniAnagraficaCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
            setEnabled(false);
        }

        @Override
        protected void doExecuteCommand() {
            aggiornaInformazioniAnagrafica();
        }

    }

    private final class AggiornamentoAsyncTask extends AsyncTask {
        private final String partitaIva;
        private final String codiceStato;

        private boolean cancel;

        /**
         *
         * Costruttore.
         *
         * @param partitaIva
         *            partita iva
         * @param codiceStato
         *            codice dello stato
         */
        private AggiornamentoAsyncTask(final String partitaIva, final String codiceStato) {
            this.partitaIva = partitaIva;
            this.codiceStato = codiceStato;
            this.cancel = false;
        }

        /**
         * Ripristina lo stato del form e setta il task come cancellato.
         */
        public void cancel() {
            this.cancel = Boolean.TRUE;
            AnagraficaForm.this.getFormModel().setReadOnly(false);
            overlayable.setOverlayVisible(false);
        }

        @Override
        public void failure(Throwable throwable) {
            overlayable.setOverlayVisible(Boolean.FALSE);
            AnagraficaForm.this.getFormModel().setReadOnly(false);
        }

        @Override
        public Object run() throws Exception {
            AnagraficaWebBuilder anagraficaWebBuilder = new AnagraficaWebBuilder();
            Anagrafica anagrafica = anagraficaWebBuilder.createAnagrafica(codiceStato, partitaIva);
            return anagrafica;
        }

        @Override
        public void success(Object obj) {
            if (!cancel) {
                overlayable.setOverlayVisible(Boolean.FALSE);
                Anagrafica anagrafica = (Anagrafica) obj;
                AnagraficaForm.this.getFormModel().setReadOnly(false);
                if (anagrafica != null) {
                    getValueModel("anagrafica.denominazione").setValue(anagrafica.getDenominazione());
                    getValueModel("anagrafica.sedeAnagrafica.indirizzo")
                            .setValue(anagrafica.getSedeAnagrafica().getIndirizzo());
                    getValueModel("anagrafica.sedeAnagrafica.datiGeografici.cap")
                            .setValue(anagrafica.getSedeAnagrafica().getDatiGeografici().getCap());

                    getValueModel("anagrafica.sedeAnagrafica.datiGeografici.localita")
                            .setValue(anagrafica.getSedeAnagrafica().getDatiGeografici().getLocalita());
                    for (Component component : ((JPanel) codiceFiscaleComponent).getComponents()) {
                        if (component instanceof JTextComponent) {
                            component.requestFocusInWindow();
                            break;
                        }
                    }
                }
            }
        }
    }

    private class AnnullaAggiornamentoCommand extends ActionCommand {

        public static final String COMMAND_ID = "annullaAggiornamentoCommand";

        /**
         * Costruttore.
         */
        public AnnullaAggiornamentoCommand() {
            super(COMMAND_ID);
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            if (aggiornamentoAsyncTask != null && !aggiornamentoAsyncTask.isCompleted()) {
                aggiornamentoAsyncTask.cancel();
            }
        }
    }

    private class CambioCodiceCommand extends ActionCommand {

        /**
         * Costruttore.
         */
        public CambioCodiceCommand() {
            super("newCommand");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            InputApplicationDialog inputDialog = new InputApplicationDialog("Input Codice", (Window) null);
            Integer codice = (Integer) getFormModel().getValueModel("codice").getValue();
            inputDialog.setInputField(new JTextField(codice == null ? "" : codice.toString()));
            inputDialog.setInputLabelMessage("Codice");
            inputDialog.setInputConstraint(new Constraint() {

                @Override
                public boolean test(Object paramObject) {
                    String objStr = (String) paramObject;
                    if (paramObject == null || StringUtils.isNumeric(objStr)) {
                        return true;
                    }

                    new MessageDialog("ATTENZIONE", "Il codice inserito deve essere numerico").showDialog();
                    return false;
                }
            });
            inputDialog.setFinishAction(new Closure() {

                @Override
                public Object call(Object paramObject) {
                    Integer newCodice = null;

                    if (paramObject != null && !"".equals(((String) paramObject).trim())) {
                        newCodice = new Integer((String) paramObject);
                    }
                    getFormModel().getValueModel("codice").setValue(newCodice);
                    return null;
                }
            });
            inputDialog.showDialog();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setText("");
        }
    }

    /**
     * Property change per il campo codice fiscale, esegue la ricerca di entità o anagrafica già esistente per lo stesso
     * codice fiscale; viene richiesto se continuare con l'operazione od annullare la modifica. Nel caso in cui annullo
     * la modifica, lancio un timer per ripristinare lo stato originale dell'entità.
     *
     * @see DatiEsistentiConfirmationDialog
     * @see RevertFormModelTimerTask
     * @author leonardo
     */
    private class CodiceFiscaleChangeListeners implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            deactivateListeners();
            try {
                boolean isValid = getFormModel().getValidationResults()
                        .getMessages("anagrafica." + Anagrafica.PROP_CODICE_FISCALE).size() == 0;
                String codiceFiscale = (String) getFormModel()
                        .getValueModel("anagrafica." + Anagrafica.PROP_CODICE_FISCALE).getValue();
                String partitaIva = (String) getFormModel().getValueModel("anagrafica." + Anagrafica.PROP_PARTITE_I_V_A)
                        .getValue();
                String codiceStato = (String) getFormModel()
                        .getValueModel("anagrafica.sedeAnagrafica.datiGeografici.nazione.codice").getValue();
                // se non ho errori verifico se ho un'altra partiva Iva uguale
                if (isValid && codiceFiscale != null && !codiceFiscale.isEmpty() && "IT".equals(codiceStato)) {
                    // ParametriRicercaEntita parametri = new
                    // ParametriRicercaEntita();
                    // parametri.setCodiceFiscale(codiceFiscale);
                    // parametri.setFieldSearch(FieldSearch.CODICEFISCALE);
                    List<EntitaLite> entitaTrovate = anagraficaBD.ricercaEntita(codiceFiscale, partitaIva);
                    Entita entitaCorrente = (Entita) getFormObject();
                    AnagraficaLite anagraficaPresente = null;
                    EntitaLite entitaPresente = null;
                    for (EntitaLite entitaTrovata : entitaTrovate) {
                        if (!entitaTrovata.getId().equals(entitaCorrente.getId())) {
                            if (entitaTrovata.getTipo().equals(entitaCorrente.getTipo().name())) {
                                entitaPresente = entitaTrovata;
                            } else {
                                anagraficaPresente = entitaTrovata.getAnagrafica();
                            }
                        }
                    }
                    if (entitaPresente != null) {
                        // carico l'entità trovata
                        String title = RcpSupport.getMessage("", "entitaPresente", "title");
                        String message = RcpSupport.getMessage("", "entitaPresente", "message");
                        DatiEsistentiConfirmationDialog dialog = new DatiEsistentiConfirmationDialog(title, message,
                                false);
                        dialog.showDialog();
                        if (dialog.isConfirmed()) {
                            getNewFormObjectCommand().execute();
                            LifecycleApplicationEvent event = new OpenEditorEvent(
                                    anagraficaBD.caricaEntita(entitaPresente, false));
                            Application.instance().getApplicationContext().publishEvent(event);
                        }
                    } else if (anagraficaPresente != null) {
                        String title = RcpSupport.getMessage("", "anagraficaPresente", "title");
                        String message = RcpSupport.getMessage("", "anagraficaPresente", "message");
                        DatiEsistentiConfirmationDialog dialog = new DatiEsistentiConfirmationDialog(title, message,
                                false);
                        dialog.showDialog();
                        if (dialog.isConfirmed()) {
                            // metto il form in readonly perchè in questo modo
                            // posso settare l'anagrafica senza
                            // scatenare
                            // tutti i listener
                            AnagraficaForm.this.getFormModel().setReadOnly(true);
                            getValueModel("anagrafica")
                                    .setValue(anagraficaBD.caricaAnagrafica(anagraficaPresente.getId()));
                            AnagraficaForm.this.getFormModel().setReadOnly(false);
                        }
                    }
                }
            } finally {
                activateListeners();
            }

        }
    }

    /**
     * Confirmation dialog che nel caso in cui non si voglia portare a termine l'operazione richiesta, si preoccupa di
     * schedulare il revert del form per riportare allo stato originale i dati dell'entità su cui si sta lavorando
     * correntemente nel form.
     *
     * @author leonardo
     */
    private class DatiEsistentiConfirmationDialog extends ConfirmationDialog {

        private class ContinuaCommand extends ActionCommand {

            public static final String COMMAND_ID = "continuaCommand";

            /**
             * Costruttore.
             */
            public ContinuaCommand() {
                super(COMMAND_ID);
                RcpSupport.configure(this);
            }

            @Override
            protected void doExecuteCommand() {
                DatiEsistentiConfirmationDialog.this.continua = true;
                DatiEsistentiConfirmationDialog.this.onCancel();
            }

        }

        private boolean confirmed = false;
        private boolean continua = false;

        private boolean abilitaContinua;

        private ContinuaCommand continuaCommand;

        /**
         * Costruttore di default.
         *
         * @param title
         *            il titolo del dialog
         * @param message
         *            il messaggio di richiesta per confermare o annullare l'operazione
         * @param abilitaContinua
         *            indica se deve essere presente il pulsante di continua
         */
        public DatiEsistentiConfirmationDialog(final String title, final String message,
                final boolean abilitaContinua) {
            super(title, message);
            this.abilitaContinua = abilitaContinua;
        }

        @Override
        protected Object[] getCommandGroupMembers() {
            if (abilitaContinua) {
                return new AbstractCommand[] { getFinishCommand(), getCancelCommand(), getContinuaCommand() };
            } else {
                return new AbstractCommand[] { getFinishCommand(), getCancelCommand() };
            }
        }

        /**
         * @return Returns the continuaCommand.
         */
        public ContinuaCommand getContinuaCommand() {
            if (continuaCommand == null) {
                continuaCommand = new ContinuaCommand();
            }

            return continuaCommand;
        }

        /**
         * @return the confirmed
         */
        public boolean isConfirmed() {
            return confirmed;
        }

        @Override
        protected void onCancel() {
            if (!continua) {
                confirmed = false;
                Timer timer = new Timer();
                timer.schedule(new RevertFormModelTimerTask(), 100);
            }
            super.onCancel();
        }

        @Override
        protected void onConfirm() {
            confirmed = true;
        }

    }

    private class FatturazionePAListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            setReadOnlyDatiFatturazionePA();
        }

    }

    /**
     * Property change per il campo partita IVA, esegue la ricerca di entità o anagrafica già esistente per la stessa
     * partita IVA; viene richiesto se continuare con l'operazione od annullare la modifica. Nel caso in cui annullo la
     * modifica, lancio un timer per ripristinare lo stato originale dell'entità.
     *
     * @see DatiEsistentiConfirmationDialog
     * @see RevertFormModelTimerTask
     * @author leonardo
     */
    private class PartitaIvaChangeListeners implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            deactivateListeners();
            try {
                boolean isValid = getFormModel().getValidationResults()
                        .getMessages("anagrafica." + Anagrafica.PROP_PARTITE_I_V_A).size() == 0;
                String partitaIva = (String) getFormModel().getValueModel("anagrafica." + Anagrafica.PROP_PARTITE_I_V_A)
                        .getValue();
                String codiceStato = (String) getFormModel()
                        .getValueModel("anagrafica.sedeAnagrafica.datiGeografici.nazione.codice").getValue();
                // se non ho errori verifico se ho un'altra partiva Iva uguale
                if (isValid && partitaIva != null && !partitaIva.isEmpty() && "IT".equals(codiceStato)) {
                    ParametriRicercaEntita parametri = new ParametriRicercaEntita();
                    parametri.setPartitaIva(partitaIva);
                    parametri.setFieldSearch(FieldSearch.PARTITAIVA);
                    List<EntitaLite> entitaTrovate = anagraficaBD.ricercaEntita(parametri);
                    Entita entitaCorrente = (Entita) getFormObject();
                    AnagraficaLite anagraficaPresente = null;
                    EntitaLite entitaPresente = null;
                    for (EntitaLite entitaTrovata : entitaTrovate) {
                        if (!entitaTrovata.getId().equals(entitaCorrente.getId())) {
                            // NPE Mail, verifico quale variabile risulta null
                            Entita entitaProxy = entitaTrovata.creaProxyEntita();
                            if (entitaProxy != null && entitaProxy.getTipo() != null) {
                                if (entitaProxy.getTipo().equals(entitaCorrente.getTipo())) {
                                    entitaPresente = entitaTrovata;
                                } else {
                                    anagraficaPresente = entitaTrovata.getAnagrafica();
                                }
                            } else {
                                logger.error("PartitaIvaChangeListeners: entitaProxy " + entitaProxy
                                        + ", entitaCorrente " + entitaCorrente);
                            }
                        }
                    }
                    if (entitaPresente != null) {
                        // carico l'entità trovata
                        String title = RcpSupport.getMessage("", "anagraficaPresente", "title");
                        String message = RcpSupport.getMessage("", "entitaPresente", "message");
                        DatiEsistentiConfirmationDialog dialog = new DatiEsistentiConfirmationDialog(title, message,
                                true);
                        dialog.showDialog();
                        if (dialog.isConfirmed()) {
                            getNewFormObjectCommand().execute();
                            LifecycleApplicationEvent event = new OpenEditorEvent(
                                    anagraficaBD.caricaEntita(entitaPresente, false));
                            Application.instance().getApplicationContext().publishEvent(event);
                        }
                    } else if (anagraficaPresente != null) {
                        String title = RcpSupport.getMessage("", "anagraficaPresente", "title");
                        String message = RcpSupport.getMessage("", "anagraficaPresente", "message");
                        DatiEsistentiConfirmationDialog dialog = new DatiEsistentiConfirmationDialog(title, message,
                                true);
                        dialog.showDialog();
                        if (dialog.isConfirmed()) {
                            // metto il form in readonly perchè in questo modo
                            // posso settare l'anagrafica senza
                            // scatenare
                            // tutti i listener
                            AnagraficaForm.this.getFormModel().setReadOnly(true);
                            getValueModel("anagrafica")
                                    .setValue(anagraficaBD.caricaAnagrafica(anagraficaPresente.getId()));
                            AnagraficaForm.this.getFormModel().setReadOnly(false);
                        }
                    } else {
                        // non ho un'anagrafica già presente quindi cerco di
                        // ottenerla dalla partita iva
                        try {
                            if (settingsManager.getUserSettings().getBoolean(
                                    AggiornamentoAutomaticoInformazioniAnagraficaCommand.AGGIORNAMENTO_AUTOMATICO_INFORMAZIONI_ANAGRAFICA)) {
                                aggiornaInformazioniAnagrafica();
                            }
                        } catch (SettingsException e) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("--> " + e.getMessage());
                            }
                        }
                    }
                }
            } finally {
                activateListeners();
            }
        }
    }

    /**
     * Timer che chiama la revert del form model per annullare eventuali modifiche effettuate. Viene utilizzato un timer
     * a causa del fatto che quando eseguo una modifica sul document del text field scelto (ad es. partita iva), il
     * document viene bloccato (writeLock enabled); scatta il property change legato al document che esegue la revert
     * del form model, ma eseguendo la revert, viene modificato il value model che modifica a sua volta, tramite il
     * binding, il testo contenuto nel text field (e quindi del document) della partita iva che però si trova in
     * writeLock. Viene sollevata giustamente una exception.<br/>
     * Questa situazione non è risolvibile in altro modo se non chiamando il revert una volta concluse le operazioni del
     * property change.
     *
     * @author leonardo
     */
    private class RevertFormModelTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        getFormModel().revert();
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }

    public static final String FORM_ID = "anagraficaForm";

    private final String className;

    protected IAnagraficaBD anagraficaBD;

    private PartitaIvaChangeListeners partitaIvaChangeListeners;
    private CodiceFiscaleChangeListeners codiceFiscaleChangeListeners;

    protected PanjeaFormLayoutBuilder builder;

    private JComponent focusComponent;
    private JComponent codiceFiscaleComponent;

    private AggiornaInformazioniAnagraficaCommand aggiornaInformazioniAnagraficaCommand;

    private SettingsManager settingsManager;

    private DefaultOverlayable overlayable;

    private AggiornamentoAsyncTask aggiornamentoAsyncTask;
    private AnnullaAggiornamentoCommand annullaAggiornamentoCommand;

    private JTextField codiceEntitaTextField;
    private CambioCodiceCommand cambioCodiceCommand;

    private PluginManager pluginManager;

    private FatturazionePAListener fatturazionePAListener;

    /**
     * Costruttore.
     *
     * @param entita
     *            entità
     * @param anagraficaBD
     *            anagraficaBD
     */
    public AnagraficaForm(final Entita entita, final IAnagraficaBD anagraficaBD) {
        this.className = getMessage(entita.getClass().getName());
        ValidatingFormModel validatingFormModel = (ValidatingFormModel) PanjeaFormModelHelper.createFormModel(entita,
                false, className + "FormModel");
        setId(FORM_ID);
        this.anagraficaBD = anagraficaBD;
        setFormModel(validatingFormModel);
        settingsManager = (SettingsManager) ApplicationServicesLocator.services().getService(SettingsManager.class);
        pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
    }

    /**
     * Attiva tutti i listener sulle proprietà del formmodel.
     */
    public void activateListeners() {
        addFormValueChangeListener("anagrafica." + Anagrafica.PROP_PARTITE_I_V_A, partitaIvaChangeListeners);
        addFormValueChangeListener("anagrafica." + Anagrafica.PROP_CODICE_FISCALE, codiceFiscaleChangeListeners);
        addFormValueChangeListener("fatturazionePA", fatturazionePAListener);
        addFormObjectChangeListener(fatturazionePAListener);
    }

    /**
     * Aggiorna le informazioni dell'anagrafica in base alla partita iva inserita.
     */
    public void aggiornaInformazioniAnagrafica() {

        overlayable.setOverlayVisible(Boolean.TRUE);
        AnagraficaForm.this.getFormModel().setReadOnly(true);

        final String partitaIva = (String) getFormModel().getValueModel("anagrafica." + Anagrafica.PROP_PARTITE_I_V_A)
                .getValue();
        final String codiceStato = (String) getFormModel()
                .getValueModel("anagrafica.sedeAnagrafica.datiGeografici.nazione.codice").getValue();

        try {
            aggiornamentoAsyncTask = new AggiornamentoAsyncTask(partitaIva, codiceStato);
            AsyncWorker.post(aggiornamentoAsyncTask);
        } catch (Exception e) {
            overlayable.setOverlayVisible(Boolean.FALSE);
            AnagraficaForm.this.getFormModel().setReadOnly(false);
            PanjeaSwingUtil.checkAndThrowException(new GenericException(
                    "Errore durante il caricamento dei dati dell'anagrafica tramite partita iva."));
        }
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        FormLayout layout = new FormLayout(
                "90dlu,4dlu,left:100dlu, 10dlu, right:pref,4dlu,left:60dlu, 10dlu, right:pref,4dlu,fill:120dlu, 10dlu, right:pref,4dlu,fill:default:grow",
                "3dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default,1dlu,default");
        builder = new PanjeaFormLayoutBuilder(bf, layout);
        builder.setLabelAttributes("r, c");

        builder.nextRow();
        builder.setRow(2);

        Binding nazioneBinding = bf.createBoundSearchText("anagrafica.sedeAnagrafica.datiGeografici.nazione",
                new String[] { "codice" });
        builder.addLabel("anagrafica.sedeAnagrafica.datiGeografici.nazione", 1);
        SearchPanel nazioneSearchPanel = (SearchPanel) builder.addBinding(nazioneBinding, 3);
        nazioneSearchPanel.getTextFields().get("codice").setColumns(3);
        builder.nextRow();

        builder.setComponentAttributes("l,f");
        focusComponent = builder.addPropertyAndLabel("anagrafica." + Anagrafica.PROP_PARTITE_I_V_A, 1)[1];
        ((JTextField) focusComponent).setColumns(15);

        builder.addComponent(getAggiornaInformazioniAnagraficaCommand().createButton(), 4);

        builder.setComponentAttributes("f,f");
        builder.addLabel("anagrafica." + Anagrafica.PROP_CODICE_FISCALE, 9);
        Binding bindingCodFisc = bf.createBoundCodiceFiscale("anagrafica." + Anagrafica.PROP_CODICE_FISCALE,
                "anagrafica." + Anagrafica.PROP_DENOMINAZIONE, null, null, null, null);
        codiceFiscaleComponent = builder.addBinding(bindingCodFisc, 11);
        codiceFiscaleComponent.setPreferredSize(new Dimension(200, 19));
        builder.setComponentAttributes("f,c");

        builder.nextRow();
        codiceEntitaTextField = (JTextField) builder.addPropertyAndLabel(Entita.PROP_CODICE, 1)[1];
        codiceEntitaTextField.setColumns(15);
        cambioCodiceCommand = new CambioCodiceCommand();
        cambioCodiceCommand.setEnabled(false);
        builder.addComponent(cambioCodiceCommand.createButton(), 4);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("anagrafica." + Anagrafica.PROP_DENOMINAZIONE, 1, 9, 1)[1])
                .setColumns(45);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO, 1, 9,
                1)[1]).setColumns(45);
        builder.nextRow();

        DatiGeograficiBinding bindingDatiGeografici = (DatiGeograficiBinding) bf
                .createDatiGeograficiBinding("anagrafica.sedeAnagrafica.datiGeografici", "right:90dlu", false);
        builder.addBinding(bindingDatiGeografici, 1, 11, 1);
        builder.nextRow();

        ((JTextField) builder.addPropertyAndLabel("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_TELEFONO, 1)[1])
                .setColumns(10);
        ((JTextField) builder.addPropertyAndLabel("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_FAX, 9)[1])
                .setColumns(10);
        builder.nextRow();

        builder.addPropertyAndLabel("anagrafica.sedeAnagrafica.indirizzoPEC", 1);
        builder.addPropertyAndLabel("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_INDIRIZZO_MAIL, 9);
        builder.nextRow();

        builder.addPropertyAndLabel(Entita.PROP_ABILITATO, 1);
        builder.addPropertyAndLabel("anagrafica.sedeAnagrafica." + SedeAnagrafica.PROP_WEB, 9);
        builder.nextRow();

        if (getFormModel().getFormObject().getClass().getName().equals(Vettore.class.getName())) {
            ((JTextField) builder.addPropertyAndLabel("numeroCopiePerStampa", 1)[1]).setColumns(3);
            ((JTextField) builder.addPropertyAndLabel("numeroIscrizioneAlbo", 9)[1]).setColumns(16);
            builder.nextRow();
        } else if (getFormModel().getFormObject().getClass().getName().equals(Cliente.class.getName())
                || getFormModel().getFormObject().getClass().getName().equals(Fornitore.class.getName())) {
            builder.addPropertyAndLabel("bloccoSede.blocco", 1);
            builder.addPropertyAndLabel("bloccoSede.noteBlocco", 9);
            builder.nextRow();
            if (pluginManager.isPresente(PluginManager.PLUGIN_FATTURAZIONE_PA)
                    && getFormModel().getFormObject().getClass().getName().equals(Cliente.class.getName())) {
                builder.addHorizontalSeparator("Fatturazione Pubblica Amministrazione", 4);
                builder.addHorizontalSeparator("Note compilazione fattura P.A.", 7, 5);
                builder.nextRow();
                builder.addPropertyAndLabel("fatturazionePA", 1);
                builder.setComponentAttributes("f,f");
                builder.addBinding(bf.createBoundHTMLEditor("noteFatturaPA"), 7, 5, 5);
                builder.setComponentAttributes("f,c");
                builder.nextRow();
                builder.addPropertyAndLabel("codiceEori", 1);
                builder.nextRow();
                builder.addPropertyAndLabel("codiceIdentificativoFiscale", 1);
                builder.nextRow();
            }
            if (pluginManager.isPresente(PluginManager.PLUGIN_VENDING)
                    && getFormModel().getFormObject().getClass().getName().equals(Cliente.class.getName())) {
                builder.addHorizontalSeparator("Vending", 11);
                builder.nextRow();
                builder.addPropertyAndLabel("fatturaPalmare", 1);
            }
        }

        overlayable = new DefaultOverlayable();
        overlayable.setActualComponent(GuiStandardUtils.attachBorder(builder.getPanel()));
        JPanel panelOverlay = getComponentFactory().createPanel(new BorderLayout());
        panelOverlay.setPreferredSize(new Dimension(550, 100));
        panelOverlay.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
                "Aggiornamento informazioni"));
        panelOverlay.add(getAnnullaAggiornamentoCommand().createButton(), BorderLayout.CENTER);
        overlayable.addOverlayComponent(panelOverlay, SwingConstants.CENTER, 0);
        overlayable.setOverlayVisible(false);
        getValueModel("bloccoSede").getValue();

        return overlayable;
    }

    /**
     * Disattiva tutti i listeners.
     */
    public void deactivateListeners() {
        removeFormValueChangeListener("anagrafica." + Anagrafica.PROP_PARTITE_I_V_A, partitaIvaChangeListeners);
        removeFormValueChangeListener("anagrafica." + Anagrafica.PROP_CODICE_FISCALE, codiceFiscaleChangeListeners);
        removeFormValueChangeListener("fatturazionePA", fatturazionePAListener);
        removeFormObjectChangeListener(fatturazionePAListener);
    }

    /**
     * @return Returns the aggiornaInformazioniAnagraficaCommand.
     */
    private AggiornaInformazioniAnagraficaCommand getAggiornaInformazioniAnagraficaCommand() {
        if (aggiornaInformazioniAnagraficaCommand == null) {
            aggiornaInformazioniAnagraficaCommand = new AggiornaInformazioniAnagraficaCommand();
        }

        return aggiornaInformazioniAnagraficaCommand;
    }

    /**
     * @return Returns the annullaAggiornamentoCommand.
     */
    public AnnullaAggiornamentoCommand getAnnullaAggiornamentoCommand() {
        if (annullaAggiornamentoCommand == null) {
            annullaAggiornamentoCommand = new AnnullaAggiornamentoCommand();
        }

        return annullaAggiornamentoCommand;
    }

    @Override
    public void grabFocus() {
        if (focusComponent != null) {
            focusComponent.requestFocusInWindow();
        }
    }

    /**
     * Crea tutti i listeners.
     */
    private void initListeners() {
        // definizione dei listener incaricati di gestire le attività legate
        // alla variazione di codice fiscale e partita iva
        partitaIvaChangeListeners = new PartitaIvaChangeListeners();
        codiceFiscaleChangeListeners = new CodiceFiscaleChangeListeners();
        fatturazionePAListener = new FatturazionePAListener();

        getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                getAggiornaInformazioniAnagraficaCommand().setEnabled(!((Boolean) evt.getNewValue()));
                codiceEntitaTextField.setEditable(false);
                cambioCodiceCommand.setEnabled(!((Boolean) evt.getNewValue()));

                setReadOnlyDatiFatturazionePA();
            }
        });
    }

    /**
     * @param anagraficaBD
     *            the anagraficaBD to set
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /**
     * override di setFormObject per la creazione dei PropertyChange solo dopo la prima esecuzione.
     *
     * @param formObject
     *            oggetto gestito dal form
     */
    @Override
    public void setFormObject(Object formObject) {
        super.setFormObject(formObject);
        if (partitaIvaChangeListeners == null) {
            initListeners();
        }
    }

    private void setReadOnlyDatiFatturazionePA() {

        boolean fatturaizonePA = (boolean) getFormModel().getValueModel("fatturazionePA").getValue();

        getFormModel().getFieldMetadata("codiceEori").setReadOnly(!fatturaizonePA);
        getFormModel().getFieldMetadata("codiceIdentificativoFiscale").setReadOnly(!fatturaizonePA);
        getFormModel().getFieldMetadata("noteFatturaPA").setReadOnly(!fatturaizonePA);
    }

}
