/**
 *
 */
package it.eurotn.panjea.magazzino.rich.editors.areamagazzino;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.form.FormModel;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.config.CommandButtonConfigurer;
import org.springframework.richclient.command.config.CommandButtonLabelInfo;
import org.springframework.richclient.command.config.CommandConfigurer;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.factory.ButtonFactory;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.spring.richclient.docking.editor.OpenEditorEvent;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.commands.documento.DocumentiDocumentoCommand;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaEsportazioneFlusso;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoContabilizzazioneBD;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.IVendingAreaRifornimentoBD;
import it.eurotn.panjea.magazzino.rich.commands.ContabilizzaCommand;
import it.eurotn.panjea.magazzino.rich.commands.EliminaAreaMagazzinoCommand;
import it.eurotn.panjea.magazzino.rich.commands.documento.StatiAreaMagazzinoCommandController;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.AreaMagazzinoAltroForm;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.AreaMagazzinoForm;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.DatiAccessoriForm;
import it.eurotn.panjea.magazzino.rich.forms.areamagazzino.PiedeAreaMagazzinoForm;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.editors.documentograph.OpenDocumentoGraphEditorCommand;
import it.eurotn.panjea.rich.focuspolicy.PanjeaFocusTraversalPolicy;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;
import it.eurotn.rich.form.PanjeaAbstractForm;

/**
 * Page per la gestione di {@link AreaMagazzino}.
 *
 * @author adriano
 * @version 1.0, 29/ago/2008
 */
public class AreaMagazzinoPage extends FormsBackedTabbedDialogPageEditor implements InitializingBean, IEditorListener {

    /**
     * Visualizza i dati di un'eventuale area contabile e apre il documento contabile se premuto.
     *
     * @author giangi
     */
    public class AreaContabileCommand extends ActionCommand {
        private static final String COMMAND_ID = "areaContabileCommand";
        private AreaContabileLite areaContabileLite;

        /**
         * Costruttore.
         */
        public AreaContabileCommand() {
            super(COMMAND_ID);
            setSecurityControllerId(COMMAND_ID);
            CommandConfigurer controller = (CommandConfigurer) ApplicationServicesLocator.services()
                    .getService(CommandConfigurer.class);
            controller.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            LifecycleApplicationEvent event = new OpenEditorEvent(areaContabileLite);
            Application.instance().getApplicationContext().publishEvent(event);
        }

        /**
         * @return the areaContabileLite
         */
        public AreaContabileLite getAreaContabileLite() {
            return areaContabileLite;
        }

        /**
         * @param areaContabileLite
         *            the areaContabileLite to set
         */
        public void setAreaContabileLite(AreaContabileLite areaContabileLite) {
            this.areaContabileLite = areaContabileLite;
            if (areaContabileLite == null) {
                getFaceDescriptor().setCaption("");
                getFaceDescriptor().setIcon(null);
                setVisible(false);
            } else {
                CommandButtonLabelInfo labelInfo = new CommandButtonLabelInfo("");
                getFaceDescriptor().setLabelInfo(labelInfo);
                Icon icon = getIconSource().getIcon(areaContabileLite.getDomainClassName());
                getFaceDescriptor().setIcon(icon);
                setVisible(true);
            }
        }
    }

    /**
     * Command wrapper per AreaIntraCommand nell'editor del magazzino;<br>
     * essendo in un plugin esterno e dovendo nascondere il command nel caso in cui il documento non gestisce area
     * intra, mi servo di questo wrapper per utilizzare il command come areaContabileCommand.
     *
     * @author leonardo
     */
    private class AreaIntraMagazzinoCommand extends ActionCommand {

        private Documento documento = null;
        private AziendaCorrente aziendaCorrente = null;
        private ActionCommand areaIntraCommand = null;

        /**
         * Costruttore, inizializzo areaIntraCommand e AziendaCorrente.
         */
        public AreaIntraMagazzinoCommand() {
            super("areaIntraCommand");
            RcpSupport.configure(this);
            aziendaCorrente = RcpSupport.getBean(AziendaCorrente.BEAN_ID);
            areaIntraCommand = RcpSupport.getCommand("areaIntraCommand");
        }

        @Override
        protected void doExecuteCommand() {
            if (areaIntraCommand != null) {
                areaIntraCommand.addParameter("documento", documento);
                areaIntraCommand.execute();
            }
        }

        /**
         * @param documento
         *            the documento to set
         */
        public void setDocumento(Documento documento) {
            this.documento = documento;
            setVisible(documento.isAreaIntraAbilitata(aziendaCorrente.getNazione().getCodice()));
        }

    }

    private class AreaMagazzinoChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) evt.getNewValue();

            getAreaIntraMagazzinoCommand().setDocumento(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento());
            getAreaContabileCommand().setAreaContabileLite(areaMagazzinoFullDTO.getAreaContabileLite());
            getDocumentiDocumentoCommand().setDocumento(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento());
            getFatturaPAMagazzinoCommand().setAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());

            getOpenSelezioneDocumentoCommand().setVisible(areaMagazzinoFullDTO.getAreaMagazzino().isNew());
            getStampaAreaMagazzinoSplitCommand()
                    .updateCommand(((AreaMagazzinoFullDTO) getForm().getFormObject()).getAreaMagazzino());

            getContabilizzaCommand().setAreaMagazzinoFullDTO(areaMagazzinoFullDTO);

            if (areaMagazzinoFullDTO.getAreaMagazzino().isNew()
                    || areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino()
                            .getStrategiaEsportazioneFlusso() == StrategiaEsportazioneFlusso.NESSUNO
                    || !areaMagazzinoFullDTO.getAreaMagazzino().getDatiValidazioneRighe().isValid()) {
                getEsportaDocumentoCommand().setVisible(false);
            } else {
                getEsportaDocumentoCommand().setVisible(true);
            }
        }
    }

    private class ContabilizzaCommandChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            contabilizzaCommand.setEnabled((Boolean) evt.getNewValue());
        }
    }

    /**
     * Imposta al command {@link ContabilizzaCommand} l' {@link AreaMagazzinoFullDTO} da contabilizzare.
     */
    private class ContabilizzaCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand command) {
            AreaMagazzinoFullDTO areaMagazzinoFullDTO = ((ContabilizzaCommand) command).getAreaMagazzinoFullDTO();
            preSetFormObject(areaMagazzinoFullDTO);
            setFormObject(areaMagazzinoFullDTO);
            postSetFormObject(areaMagazzinoFullDTO);
            AreaMagazzinoPage.this.firePropertyChange(OBJECT_CHANGED, null, areaMagazzinoFullDTO);
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            ((ContabilizzaCommand) command).setAreaMagazzinoFullDTO((AreaMagazzinoFullDTO) getForm().getFormObject());
            return true;
        }
    }

    /**
     * @author leonardo
     */
    private class DocumentiDocumentoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
            AreaMagazzinoFullDTO areaMagazzinoFullDTOOld = (AreaMagazzinoFullDTO) AreaMagazzinoPage.this
                    .getBackingFormPage().getFormObject();

            AreaMagazzinoFullDTO areaMagazzinoFullDTO = magazzinoDocumentoBD
                    .caricaAreaMagazzinoFullDTO(areaMagazzinoFullDTOOld.getAreaMagazzino());
            AreaMagazzinoPage.this.setFormObject(areaMagazzinoFullDTO);
            firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, areaMagazzinoFullDTO);
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            DocumentiDocumentoCommand documentoCommand = (DocumentiDocumentoCommand) arg0;
            AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) AreaMagazzinoPage.this
                    .getBackingFormPage().getFormObject();
            documentoCommand.setDocumento(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento());
            return true;
        }
    }

    private class DocumentoGraphInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            command.addParameter(OpenDocumentoGraphEditorCommand.PARAM_ID_DOCUMENTO,
                    ((AreaMagazzinoFullDTO) getForm().getFormObject()).getAreaMagazzino().getDocumento().getId());
            return true;
        }
    }

    private class EliminaAreaMagazzinoCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand arg0) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("postExecution");
            }
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) getForm().getFormObject();
            command.addParameter(EliminaAreaMagazzinoCommand.PARAM_AREA_CONTABILE,
                    (areaMagazzinoFullDTO.getAreaContabileLite() != null
                            && areaMagazzinoFullDTO.getAreaContabileLite().getId() != null));
            command.addParameter(EliminaAreaMagazzinoCommand.PARAM_AREA_MAGAZZINO,
                    areaMagazzinoFullDTO.getAreaMagazzino());
            return true;
        }
    }

    private class EsportaDocumentoCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand arg0) {
            LOGGER.debug("postExecution");
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) getForm().getFormObject();
            command.addParameter(EsportaDocumentoCommand.PARAM_AREA_MAGAZZINO, areaMagazzinoFullDTO.getAreaMagazzino());
            return true;
        }
    }

    /**
     * Command wrapper per OpenAreaFatturaElettronicaTypeCommand nell'editor del magazzino;<br>
     * essendo in un plugin esterno e dovendo nascondere il command nel caso in cui il documento non gestisca la
     * fatturazione PA mi servo di questo wrapper.
     *
     */
    private class FatturaPAMagazzinoCommand extends ActionCommand {

        private ActionCommand openAreaFatturaElettronicaTypeCommand = null;

        private AreaMagazzino areaMagazzino;

        /**
         * Costruttore, inizializzo areaIntraCommand e AziendaCorrente.
         */
        public FatturaPAMagazzinoCommand() {
            super("fatturaPAMagazzinoCommand");
            RcpSupport.configure(this);
            openAreaFatturaElettronicaTypeCommand = RcpSupport.getCommand("openAreaFatturaElettronicaTypeCommand");
        }

        @Override
        public AbstractButton createButton(String faceDescriptorId, ButtonFactory buttonFactory,
                CommandButtonConfigurer buttonConfigurer) {
            AbstractButton button = super.createButton(faceDescriptorId, buttonFactory, buttonConfigurer);

            if (openAreaFatturaElettronicaTypeCommand != null) {
                button.setIcon(openAreaFatturaElettronicaTypeCommand.getIcon());
                button.setToolTipText(openAreaFatturaElettronicaTypeCommand.createButton().getToolTipText());
            }

            return button;
        }

        @Override
        protected void doExecuteCommand() {
            if (openAreaFatturaElettronicaTypeCommand != null) {
                openAreaFatturaElettronicaTypeCommand.addParameter("paramAreaMagazzino", areaMagazzino);
                openAreaFatturaElettronicaTypeCommand.execute();
            }
        }

        /**
         * @param areaMagazzino
         *            the areaMagazzino to set
         */
        public void setAreaMagazzino(AreaMagazzino areaMagazzino) {
            this.areaMagazzino = areaMagazzino;
            setVisible(areaMagazzino.isFatturaPA());
        }

    }

    /**
     * Confirmation Dialog per richiedere all'utonto se forzare il salvataggio di {@link AreaMagazzino} se non esiste
     * l'area contabile.
     *
     * @author adriano
     * @version 1.0, 09/set/2008
     */
    private class ForzaSalvataggioAreaMagazzinoDialog extends ConfirmationDialog {

        private boolean confirm = false;

        /**
         * Default constructor.
         *
         * @param title
         *            title
         * @param message
         *            message
         */
        public ForzaSalvataggioAreaMagazzinoDialog(final String title, final String message) {
            super(title, message);
        }

        /**
         * @return confirm
         */
        public boolean isConfirm() {
            return confirm;
        }

        @Override
        protected void onConfirm() {
            confirm = true;
        }
    }

    public class SaveCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
            AreaMagazzinoPage.this.firePropertyChange(VALIDA_AREA_MAGAZZINO, false, true);
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            return true;
        }

    }

    /**
     * Property change che intercetta il cambio di stato eseguito dalla {@link StatiAreaMagazzinoCommandController} per
     * aggiornare le aree presenti nell'editor.
     */
    private class StatoAreaMagazzinoChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AreaMagazzino areaMagazzino = (AreaMagazzino) evt.getNewValue();

            AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) getForm().getFormObject();
            areaMagazzinoFullDTO.setAreaMagazzino(areaMagazzino);
            setFormObject(areaMagazzinoFullDTO);
            firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, areaMagazzinoFullDTO);
        }
    }

    private class TipoAreamagazzinoChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            stampaAreaMagazzinoSplitCommand
                    .updateCommand(((AreaMagazzinoFullDTO) getForm().getFormObject()).getAreaMagazzino());

            TipoAreaMagazzino tam = ((AreaMagazzinoFullDTO) getForm().getFormObject()).getAreaMagazzino()
                    .getTipoAreaMagazzino();

            // Tab del vending
            if (getTabbedPane().getTabCount() == 5) {
                getTabbedPane().remove(4);
            }
            if (tam != null && tam.isGestioneVending()) {
                getTabbedPane().addTab(getMessageSource().getMessage(getId() + ".tab." + vendingForm.getId() + ".title",
                        new Object[] {}, Locale.getDefault()), null, vendingForm.getControl());
            }

        }
    }

    private static Logger LOGGER = Logger.getLogger(AreaMagazzinoPage.class);

    public static final String PAGE_ID = "areaMagazzinoPage";

    private static final String TITLE_AREAMAGAZZINO_DIALOG = PAGE_ID + ".dialog.title";
    private static final String MESSAGE_AREAMAGAZZINO_AVVISA_DIALOG = PAGE_ID + ".dialog.avvisa.message";

    private static final String INVALID_RIGHE_TITLE = "invalidRighe.title";
    private static final String INVALID_RIGHE_MESSAGE = "invalidRighe.message";

    public static final String VALIDA_AREA_MAGAZZINO = "validaAreaMagazzino";

    private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;
    private IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD = null;
    private IAnagraficaBD anagraficaBD = null;
    private IVendingAreaRifornimentoBD vendingAreaRifornimentoBD = null;

    private final StatiAreaMagazzinoCommandController statiAreaMagazzinoController;

    private OpenSelezioneDocumentoCommand openSelezioneDocumentoCommand = null;
    private AreaContabileCommand areaContabileCommand = null;
    private AreaIntraMagazzinoCommand areaIntraMagazzinoCommand = null;
    private DocumentiDocumentoCommand documentiDocumentoCommand = null;
    private ContabilizzaCommand contabilizzaCommand = null;
    private EsportaDocumentoCommand esportaDocumentoCommand = null;
    private EliminaAreaMagazzinoCommand eliminaAreaMagazzinoCommand = null;
    private StampaAreaMagazzinoSplitCommand stampaAreaMagazzinoSplitCommand = null;
    private OpenDocumentoGraphEditorCommand openDocumentoGraphEditorCommand = null;
    private FatturaPAMagazzinoCommand fatturaPAMagazzinoCommand;

    private DocumentiDocumentoCommandInterceptor documentiDocumentoCommandInterceptor = null;
    private EliminaAreaMagazzinoCommandInterceptor eliminaAreaMagazzinoCommandInterceptor = null;
    private EsportaDocumentoCommandInterceptor esportaDocumentoCommandInterceptor = null;
    private DocumentoGraphInterceptor documentoGraphInterceptor = null;

    private final StatoAreaMagazzinoChangeListener statoAreaMagazzinoChangeListener;
    private ContabilizzaCommandChangeListener contabilizzaCommandChangeListener = null;
    private AreaMagazzinoChangeListener areaMagazzinoChangeListener = null;
    private TipoAreamagazzinoChangeListener tipoAreamagazzinoChangeListener = null;

    private PluginManager pluginManager = null;

    private JLabel statoAreaMagazzinoLabel;

    private PanjeaAbstractForm vendingForm = null;

    /**
     * Costruttore.
     */
    public AreaMagazzinoPage() {
        super(PAGE_ID, new AreaMagazzinoForm());

        statoAreaMagazzinoChangeListener = new StatoAreaMagazzinoChangeListener();
        statiAreaMagazzinoController = new StatiAreaMagazzinoCommandController();
        statiAreaMagazzinoController.addPropertyChangeListener(
                StatiAreaMagazzinoCommandController.PROPERTY_STATO_AREA_MAGAZZINO, statoAreaMagazzinoChangeListener);

        setExternalCommandLineEnd(statiAreaMagazzinoController.getCommands());
        statoAreaMagazzinoLabel = new JLabel(RcpSupport.getIcon("rigaChiusa.icon"));
        pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
    }

    @Override
    public void addForms() {
        AreaMagazzinoAltroForm areaMagazzinoAltroForm = new AreaMagazzinoAltroForm(getBackingFormPage().getFormModel());
        addForm(areaMagazzinoAltroForm);

        DatiAccessoriForm datiAccessoriForm = new DatiAccessoriForm(getBackingFormPage().getFormModel(),
                this.anagraficaBD);
        addForm(datiAccessoriForm);

        PiedeAreaMagazzinoForm piedeAreaMagazzinoForm = new PiedeAreaMagazzinoForm(getBackingFormPage().getFormModel());
        addForm(piedeAreaMagazzinoForm);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        installPropertyChange();

        if (pluginManager.isPresente(PluginManager.PLUGIN_VENDING)) {
            if (vendingForm == null) {
                vendingForm = RcpSupport.getBean("lettureVendingForm");
                vendingForm.setFormModel(getBackingFormPage().getFormModel());
                vendingForm.getControl();
            }
        }
    }

    /**
     * Controlla se ho un cambio stato dell'areaMagazzino.<br/>
     * Se ho avuto il cambio avviso.
     *
     * @param areaMagazzinoFullDTOCheck
     *            areaMagazzinoFullDTOCheck
     */
    private void avvisaCambioStato(AreaMagazzinoFullDTO areaMagazzinoFullDTOCheck) {
        AreaMagazzino areaMagazzinoOld = ((AreaMagazzinoFullDTO) getForm().getFormObject()).getAreaMagazzino();
        AreaMagazzino areaMagazzino = areaMagazzinoFullDTOCheck.getAreaMagazzino();

        if (areaMagazzino != null && !areaMagazzino.isNew() && areaMagazzino.getId().equals(areaMagazzinoOld.getId())
                && !areaMagazzino.getVersion().equals(areaMagazzinoOld.getVersion())) {

            // se la version risulta incrementata,verifico lo stato prima e
            // dopo.
            StatoAreaMagazzino statoOld = areaMagazzinoOld.getStatoAreaMagazzino();
            StatoAreaMagazzino stato = areaMagazzino.getStatoAreaMagazzino();

            // verifica del cambio dello stato da forzato o confermato a
            // provvisorio
            boolean cambioStato = (StatoAreaMagazzino.CONFERMATO.equals(statoOld)
                    || StatoAreaMagazzino.FORZATO.equals(statoOld)) && StatoAreaMagazzino.PROVVISORIO.equals(stato);
            if (cambioStato) {
                String titolo = getMessageSource().getMessage(INVALID_RIGHE_TITLE, new Object[] {},
                        Locale.getDefault());
                String messaggio = getMessageSource().getMessage(INVALID_RIGHE_MESSAGE, new Object[] {},
                        Locale.getDefault());

                Message message = new DefaultMessage(titolo + "\n" + messaggio, Severity.INFO);
                MessageAlert messageAlert = new MessageAlert(message);
                messageAlert.showAlert();
            }
        }
    }

    @Override
    public JComponent createControl() {
        JComponent component = super.createControl();
        ComponentFactory componentFactory = (ComponentFactory) Application.services()
                .getService(ComponentFactory.class);
        JPanel panelAreeCollegateCommand = componentFactory.createPanel(new HorizontalLayout());
        // commands di collegamento a aree e documenti collegati
        panelAreeCollegateCommand.add(getOpenDocumentoGraphEditorCommand().createButton());
        panelAreeCollegateCommand.add(getDocumentiDocumentoCommand().createButton());
        panelAreeCollegateCommand.add(getAreaContabileCommand().createButton());
        panelAreeCollegateCommand.add(statoAreaMagazzinoLabel);

        if (pluginManager.isPresente(PluginManager.PLUGIN_INTRA)) {
            panelAreeCollegateCommand.add(getAreaIntraMagazzinoCommand().createButton());
        }

        if (pluginManager.isPresente(PluginManager.PLUGIN_FATTURAZIONE_PA)) {
            panelAreeCollegateCommand.add(getFatturaPAMagazzinoCommand().createButton());
        }

        getErrorBar().add(panelAreeCollegateCommand, BorderLayout.LINE_END);
        return component;
    }

    @Override
    public void dispose() {

        getErrorBar().removeAll();
        areaContabileCommand = null;
        areaIntraMagazzinoCommand = null;
        fatturaPAMagazzinoCommand = null;

        statiAreaMagazzinoController.removePropertyChangeListener(statoAreaMagazzinoChangeListener);

        getBackingFormPage().getFormModel().removePropertyChangeListener(FormModel.READONLY_PROPERTY,
                contabilizzaCommandChangeListener);

        eliminaAreaMagazzinoCommand.removeCommandInterceptor(eliminaAreaMagazzinoCommandInterceptor);
        esportaDocumentoCommand.removeCommandInterceptor(esportaDocumentoCommandInterceptor);
        openDocumentoGraphEditorCommand.removeCommandInterceptor(documentoGraphInterceptor);

        getForm().removeFormObjectChangeListener(areaMagazzinoChangeListener);
        getForm().getValueModel("areaMagazzino.tipoAreaMagazzino")
                .removeValueChangeListener(tipoAreamagazzinoChangeListener);

        super.dispose();
    }

    @Override
    protected Object doSave() {
        LOGGER.debug("--> Enter doSave");
        getForm().commit();
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) getBackingFormPage().getFormObject();

        int versionDocumentoOld = -1;
        if (areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getVersion() != null) {
            versionDocumentoOld = areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getVersion().intValue();
        }

        // clone di AreaMagazzinoFullDTO per permettere il revert del form model
        // se il salvataggio solleva una exception
        AreaMagazzinoFullDTO areaMagazzinoFullDTOSalvato = (AreaMagazzinoFullDTO) PanjeaSwingUtil
                .cloneObject(areaMagazzinoFullDTO);
        areaMagazzinoFullDTOSalvato = save(areaMagazzinoFullDTOSalvato, false);
        int versionDocumentoNew = areaMagazzinoFullDTOSalvato.getAreaMagazzino().getDocumento().getVersion().intValue();

        // Aggiorno se ho un cambio stato. Ricordarsi che la doSave non chiama
        // la preSetFormObejct della stessa pagina.
        statiAreaMagazzinoController.updateAreaMagazzino(areaMagazzinoFullDTOSalvato.getAreaMagazzino());
        avvisaCambioStato(areaMagazzinoFullDTOSalvato);

        if (versionDocumentoOld != -1 && versionDocumentoOld != versionDocumentoNew) {
            publishDocumento(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento());
        }

        getContabilizzaCommand().setAreaMagazzinoFullDTO(areaMagazzinoFullDTO);
        // areaMagazzinoFullDTOSalvato.setRifornimento(areaMagazzinoFullDTO.isRifornimento());

        LOGGER.debug("--> Exit doSave");
        return areaMagazzinoFullDTOSalvato;
    }

    /**
     * @return comando che apre l'area contabile collegata al documento
     */
    private AreaContabileCommand getAreaContabileCommand() {
        if (areaContabileCommand == null) {
            areaContabileCommand = new AreaContabileCommand();
        }
        return areaContabileCommand;
    }

    /**
     * @return AreaIntraCommand o null se il plugin non è presente
     */
    private AreaIntraMagazzinoCommand getAreaIntraMagazzinoCommand() {
        if (areaIntraMagazzinoCommand == null) {
            areaIntraMagazzinoCommand = new AreaIntraMagazzinoCommand();
        }
        return areaIntraMagazzinoCommand;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        // crea e recupera i commands da StatiAreaContabileCommandController
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(), getEliminaCommand(),
                toolbarPageEditor.getUndoCommand(), getStampaAreaMagazzinoSplitCommand(),
                getOpenSelezioneDocumentoCommand(), getContabilizzaCommand(), getEsportaDocumentoCommand() };
        toolbarPageEditor.getSaveCommand().addCommandInterceptor(new SaveCommandInterceptor());
        return abstractCommands;
    }

    /**
     * @return comando per la contabilizzazione dell'area magazzino
     */
    private ContabilizzaCommand getContabilizzaCommand() {
        if (contabilizzaCommand == null) {
            contabilizzaCommand = new ContabilizzaCommand(PAGE_ID);
            contabilizzaCommand.setMagazzinoContabilizzazioneBD(magazzinoContabilizzazioneBD);
            contabilizzaCommand.setMagazzinoDocumentoBD(magazzinoDocumentoBD);
            contabilizzaCommand.addCommandInterceptor(new ContabilizzaCommandInterceptor());
            contabilizzaCommandChangeListener = new ContabilizzaCommandChangeListener();
            getBackingFormPage().getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY,
                    contabilizzaCommandChangeListener);
        }
        return contabilizzaCommand;
    }

    /**
     * @return the {@link DocumentiDocumentoCommand} to get
     */
    private DocumentiDocumentoCommand getDocumentiDocumentoCommand() {
        if (documentiDocumentoCommand == null) {
            documentiDocumentoCommand = new DocumentiDocumentoCommand();
            documentiDocumentoCommand.addCommandInterceptor(getDocumentiDocumentoCommandInterceptor());
        }
        return documentiDocumentoCommand;
    }

    /**
     * @return documentiDocumentoCommandInterceptor
     */
    private DocumentiDocumentoCommandInterceptor getDocumentiDocumentoCommandInterceptor() {
        if (documentiDocumentoCommandInterceptor == null) {
            documentiDocumentoCommandInterceptor = new DocumentiDocumentoCommandInterceptor();
        }
        return documentiDocumentoCommandInterceptor;
    }

    /**
     * @return Returns the documentoGraphInterceptor.
     */
    private DocumentoGraphInterceptor getDocumentoGraphInterceptor() {
        if (documentoGraphInterceptor == null) {
            documentoGraphInterceptor = new DocumentoGraphInterceptor();
        }

        return documentoGraphInterceptor;
    }

    @Override
    public AbstractCommand getEditorDeleteCommand() {
        return getEliminaCommand();
    }

    /**
     *
     * @return command per l'eliminazione dell'area magazzino
     */
    public EliminaAreaMagazzinoCommand getEliminaCommand() {
        if (eliminaAreaMagazzinoCommand == null) {
            eliminaAreaMagazzinoCommand = new EliminaAreaMagazzinoCommand(PAGE_ID);
            eliminaAreaMagazzinoCommand.setMagazzinoDocumentoBD(magazzinoDocumentoBD);
            eliminaAreaMagazzinoCommandInterceptor = new EliminaAreaMagazzinoCommandInterceptor();
            eliminaAreaMagazzinoCommand.addCommandInterceptor(eliminaAreaMagazzinoCommandInterceptor);
        }

        return eliminaAreaMagazzinoCommand;
    }

    /**
     * @return the esportaDocumentoCommand
     */
    public EsportaDocumentoCommand getEsportaDocumentoCommand() {
        if (esportaDocumentoCommand == null) {
            esportaDocumentoCommand = new EsportaDocumentoCommand(this.magazzinoDocumentoBD);
            esportaDocumentoCommandInterceptor = new EsportaDocumentoCommandInterceptor();
            esportaDocumentoCommand.addCommandInterceptor(esportaDocumentoCommandInterceptor);
        }
        return esportaDocumentoCommand;
    }

    /**
     * @return FatturaPAMagazzinoCommand o null se il plugin non è presente
     */
    private FatturaPAMagazzinoCommand getFatturaPAMagazzinoCommand() {
        if (fatturaPAMagazzinoCommand == null) {
            fatturaPAMagazzinoCommand = new FatturaPAMagazzinoCommand();
        }
        return fatturaPAMagazzinoCommand;
    }

    /**
     * @return magazzinoContabilizzazioneBD
     */
    public IMagazzinoContabilizzazioneBD getMagazzinoContabilizzazioneBD() {
        return magazzinoContabilizzazioneBD;
    }

    /**
     * @return Returns the openDocumentoGraphEditorCommand.
     */
    private OpenDocumentoGraphEditorCommand getOpenDocumentoGraphEditorCommand() {
        if (openDocumentoGraphEditorCommand == null) {
            openDocumentoGraphEditorCommand = new OpenDocumentoGraphEditorCommand();
            openDocumentoGraphEditorCommand.addCommandInterceptor(getDocumentoGraphInterceptor());
        }

        return openDocumentoGraphEditorCommand;
    }

    /**
     * @return openSelezioneDocumentoCommand
     */
    private OpenSelezioneDocumentoCommand getOpenSelezioneDocumentoCommand() {
        if (openSelezioneDocumentoCommand == null) {
            openSelezioneDocumentoCommand = new OpenSelezioneDocumentoCommand(magazzinoDocumentoBD,
                    getForm().getFormModel());
        }
        return openSelezioneDocumentoCommand;
    }

    /**
     *
     * @return stampaAreaMagazzinoSplitCommand
     */
    private StampaAreaMagazzinoSplitCommand getStampaAreaMagazzinoSplitCommand() {

        if (stampaAreaMagazzinoSplitCommand == null) {
            stampaAreaMagazzinoSplitCommand = new StampaAreaMagazzinoSplitCommand();

            tipoAreamagazzinoChangeListener = new TipoAreamagazzinoChangeListener();
            getForm().getValueModel("areaMagazzino.tipoAreaMagazzino")
                    .addValueChangeListener(tipoAreamagazzinoChangeListener);
        }

        return stampaAreaMagazzinoSplitCommand;
    }

    /**
     * Installa il property change da attivare al cambio dell'oggetto nel form.
     */
    private void installPropertyChange() {
        AreaMagazzinoForm areaMagazzinoForm = (AreaMagazzinoForm) getForm();
        areaMagazzinoChangeListener = new AreaMagazzinoChangeListener();
        areaMagazzinoForm.addFormObjectChangeListener(areaMagazzinoChangeListener);

    }

    @Override
    public void loadData() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> load data");
        }
    }

    @Override
    public void onEditorEvent(ApplicationEvent event) {
        // se l'evento e' del ciclo di vita dell'applicazione
        if (event instanceof PanjeaLifecycleApplicationEvent) {

            Object eventObject = event.getSource();

            if (eventObject instanceof LayoutStampa) {
                getStampaAreaMagazzinoSplitCommand()
                        .updateCommand(((AreaMagazzinoFullDTO) getForm().getFormObject()).getAreaMagazzino());
            }
        }
    }

    @Override
    public ILock onLock() {
        LOGGER.debug("--> Enter onLock");
        ILock lock = super.onLock();
        LOGGER.debug("--> Exit onLock");
        return lock;
    }

    @Override
    public void onNew() {
        LOGGER.debug("--> Enter onNew");
        AreaMagazzinoFullDTO areaMagazzinoFullDTOOld = (AreaMagazzinoFullDTO) ((AreaMagazzinoForm) getForm())
                .getFormObject();

        super.onNew();
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) ((AreaMagazzinoForm) getForm())
                .getFormObject();
        areaMagazzinoFullDTO.initializedNewObject(areaMagazzinoFullDTOOld);
        AreaMagazzino areaMagazzino = areaMagazzinoFullDTO.getAreaMagazzino();

        // clono e reimposto al form model il tipo area magazzino in modo da
        // attivare il property change che va a
        // impostare tutti i dati di default per quel tam.
        TipoAreaMagazzino tam = (TipoAreaMagazzino) PanjeaSwingUtil
                .cloneObject(areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino());
        if (areaMagazzino.getTipoAreaMagazzino() != null && areaMagazzino.getTipoAreaMagazzino().getId() != null) {
            getBackingFormPage().getValueModel("areaMagazzino.tipoAreaMagazzino").setValue(tam);
        }

        statiAreaMagazzinoController.updateAreaMagazzino(areaMagazzino);
        setTabForm(0);
        // creo un deposito lite per caricare il deposito principale.
        if (areaMagazzino.getDepositoOrigine() == null) {
            Deposito deposito = anagraficaBD.caricaDepositoPrincipale();
            if (deposito != null) {
                areaMagazzino.setDepositoOrigine(deposito.creaLite());
            }
        }

        LOGGER.debug("--> Exit onNew");
    }

    @Override
    public void onPostPageOpen() {
        /*
         * devo accedere al com.jidesoft.docking.DockableFrame[key=areaContabilePage ,mode=DOCK,side=EAST] della page
         * AreaContabilePage per impostare la focusTraversalPolicy personalizzata, ad altri livelli non funziona;<br>
         * dato che la traversal policy richiede una lista di componenti ordinata, devo costruirla e quindi associare i
         * components sul form AreaContabileForm, ma devo impostarla qui nella page dato che i controlli sono stati
         * creati e quindi posso accedere al Parent del control (ad es. nella createControl del Form ho i componenti ma
         * non ho il parent dato che il control non e' stato aggiunto al control della page, arrivando a cascata fino al
         * DockableFrame)
         */
        PanjeaFocusTraversalPolicy areaContabileFocusTraversalPolicy = ((AreaMagazzinoForm) getBackingFormPage())
                .getAreaMagazzinoFocusTraversalPolicy();
        getControl().getParent().getParent().getParent().getParent()
                .setFocusTraversalPolicy(areaContabileFocusTraversalPolicy);
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) object;
        AreaMagazzino areaMagazzino = areaMagazzinoFullDTO.getAreaMagazzino();

        statiAreaMagazzinoController.updateAreaMagazzino(areaMagazzino);
        AreaContabileLite areaContabileLite = (((AreaMagazzinoFullDTO) getForm().getFormObject())
                .getAreaContabileLite());

        // Non posso selezionare un documento se è presente l'area contabile
        // o se ho già salvato la testata
        openSelezioneDocumentoCommand.setEnabled(areaContabileLite == null && areaMagazzinoFullDTO.isNew());

        getOpenDocumentoGraphEditorCommand().setEnabled(areaMagazzinoFullDTO.getAreaMagazzino().getId() != null);

        getEliminaCommand().addParameter(EliminaAreaMagazzinoCommand.PARAM_AREA_MAGAZZINO,
                areaMagazzinoFullDTO.getAreaMagazzino());
        getContabilizzaCommand().setAreaMagazzinoFullDTO(areaMagazzinoFullDTO);
        statoAreaMagazzinoLabel.setVisible(areaMagazzino.isChiuso());

        setReadOnly(areaMagazzino.isBloccata() || !areaMagazzino.isModificabile());
        getNewCommand().setEnabled(true);
    }

    @Override
    public void preSetFormObject(Object object) {

        avvisaCambioStato((AreaMagazzinoFullDTO) object);

    }

    /**
     * @param documento
     *            il documento da lanciare
     */
    private void publishDocumento(Documento documento) {
        PanjeaLifecycleApplicationEvent documentoUpdateEvent = new PanjeaLifecycleApplicationEvent(
                LifecycleApplicationEvent.MODIFIED, documento, AreaMagazzinoPage.this);
        Application.instance().getApplicationContext().publishEvent(documentoUpdateEvent);
    }

    @Override
    public void refreshData() {
        Documento documento = ((AreaMagazzinoFullDTO) getBackingFormPage().getFormObject()).getAreaMagazzino()
                .getDocumento();
        publishDocumento(documento);
    }

    /**
     * Salva l'area magazzino.
     *
     * @param areaMagazzinoFullDTO
     *            area magazzino da salvare
     * @param forceSave
     *            indica se forzare il salvataggio dell'area
     * @return area magazzino salvata
     */
    private AreaMagazzinoFullDTO save(AreaMagazzinoFullDTO areaMagazzinoFullDTO, boolean forceSave) {
        LOGGER.debug("--> Enter save");
        try {
            AreaRate areaRate = null;
            if (areaMagazzinoFullDTO.isAreaRateEnabled()) {
                areaRate = areaMagazzinoFullDTO.getAreaRate();
            }
            if (vendingAreaRifornimentoBD != null) {
                areaMagazzinoFullDTO = vendingAreaRifornimentoBD.salvaAreaMagazzino(
                        areaMagazzinoFullDTO.getAreaMagazzino(), areaRate, areaMagazzinoFullDTO.getAreaRifornimento(),
                        forceSave);
            } else {
                areaMagazzinoFullDTO = magazzinoDocumentoBD.salvaAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino(),
                        areaRate, forceSave);
            }
        } catch (DocumentoAssenteBloccaException e) {
            LOGGER.debug("--> errore DocumentoAssenteBlocca in doSave", e);
            throw new PanjeaRuntimeException(e);
        } catch (DocumentoAssenteAvvisaException e) {
            LOGGER.debug("--> errore DocumentoAssenteAvvisa in doSave", e);
            boolean forzaSalvataggio = showConfirmationDocumentoAssenteAvvisa();
            if (forzaSalvataggio) {
                // riesegue il salvataggio eseguendo il force del salvataggio
                areaMagazzinoFullDTO = save(areaMagazzinoFullDTO, true);
                return areaMagazzinoFullDTO;
            }
            throw new PanjeaRuntimeException(e);
        } catch (DocumentiEsistentiPerAreaMagazzinoException e) {
            LOGGER.debug("--> errore DocumentiEsistenti in doSave");
            getOpenSelezioneDocumentoCommand().addParameter(OpenSelezioneDocumentoCommand.PARAM_DOCUMENTI_ESISTENTI,
                    e.getDocumenti());
            getOpenSelezioneDocumentoCommand().execute();
            // il dialogo mi setta il documento scelto scelto nel formModel. Da Cambiare!!!
            boolean documentoScelto = getOpenSelezioneDocumentoCommand().isDocumentoScelto();

            areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) getForm().getFormObject();
            if (documentoScelto) {
                areaMagazzinoFullDTO = save(areaMagazzinoFullDTO, true);
            } else {
                // HACK lancio una panjeaRuntimeException con una
                // areaContabileDuplicataException per segnalare che
                // esiste gi� una area contabile e per impedire il normale ciclo
                // dell'applicazione visto che il dato non
                // e' stato salvato.
                AreaContabile areaPerMessaggioEccezione = new AreaContabile();
                areaPerMessaggioEccezione.setCodice(areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getCodice());
                throw new PanjeaRuntimeException(
                        new AreaContabileDuplicateException("Area Contabile duplicata ", areaPerMessaggioEccezione));

            }
            // restituisco l'area salvata
            return areaMagazzinoFullDTO;
        }
        LOGGER.debug("--> Exit save");
        return areaMagazzinoFullDTO;
    }

    /**
     * @param anagraficaBD
     *            The anagraficaBD to set.
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    @Override
    public void setFormObject(Object object) {
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) object;
        if (areaMagazzinoFullDTO.isNew()) {
            // boolean isRifornimento =
            areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) ((AreaMagazzinoForm) getForm()).createNewObject();
            setTabForm(0);
        }
        super.setFormObject(areaMagazzinoFullDTO);
    }

    /**
     *
     * @param magazzinoContabilizzazioneBD
     *            magazzinoContabilizzazioneBD
     */
    public void setMagazzinoContabilizzazioneBD(IMagazzinoContabilizzazioneBD magazzinoContabilizzazioneBD) {
        this.magazzinoContabilizzazioneBD = magazzinoContabilizzazioneBD;
    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    /**
     * @param vendingAreaRifornimentoBD
     *            the vendingAreaRifornimentoBD to set
     */
    public void setVendingAreaRifornimentoBD(IVendingAreaRifornimentoBD vendingAreaRifornimentoBD) {
        this.vendingAreaRifornimentoBD = vendingAreaRifornimentoBD;
    }

    /**
     * Visualizza il dialogo per informare l'utente che l'area contabile è assente e chiede se salvare comunque l'area
     * magazzino.
     *
     * @return <true> se si deve forzare il salvataggio
     */
    private boolean showConfirmationDocumentoAssenteAvvisa() {
        LOGGER.debug("--> Enter showConfirmationDocumentoAssenteAvvisa");
        String className = getBackingFormPage().getFormObject().getClass().getName();
        MessageSource messageSource = (MessageSource) ApplicationServicesLocator.services()
                .getService(MessageSource.class);
        String title = messageSource.getMessage(TITLE_AREAMAGAZZINO_DIALOG, null, Locale.getDefault());
        String entityName = messageSource.getMessage(className, null, Locale.getDefault());
        Object[] params = new Object[] { entityName };
        String message = messageSource.getMessage(MESSAGE_AREAMAGAZZINO_AVVISA_DIALOG, params, Locale.getDefault());
        ForzaSalvataggioAreaMagazzinoDialog forzaSalvataggioAreaMagazzinoDialog = new ForzaSalvataggioAreaMagazzinoDialog(
                title, message);
        forzaSalvataggioAreaMagazzinoDialog.showDialog();
        LOGGER.debug("--> Exit showConfirmationDocumentoAssenteAvvisa");
        return forzaSalvataggioAreaMagazzinoDialog.isConfirm();
    }

    @Override
    public void updateCommands() {
        super.updateCommands();
        if (this.isControlCreated()) {
            getContabilizzaCommand().updateEnabled();
        }
    }
}
