package it.eurotn.panjea.contabilita.rich.editors.areacontabile;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.event.LifecycleApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.rich.bd.IDocumentiBD;
import it.eurotn.panjea.anagrafica.rich.commands.documento.DocumentiDocumentoCommand;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaBD;
import it.eurotn.panjea.contabilita.rich.commands.EliminaAreaContabileCommand;
import it.eurotn.panjea.contabilita.rich.commands.StatiAreaContabileCommandController;
import it.eurotn.panjea.contabilita.rich.editors.righecontabili.RigheContabiliBuilder;
import it.eurotn.panjea.contabilita.rich.forms.areacontabile.AreaContabileForm;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.editors.documentograph.OpenDocumentoGraphEditorCommand;
import it.eurotn.panjea.rich.focuspolicy.PanjeaFocusTraversalPolicy;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class AreaContabilePage extends FormBackedDialogPageEditor implements InitializingBean {

    /**
     * Property change associato al cambio di area contabile.
     */
    private class AreaContabileChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) evt.getNewValue();

            getAreaIntraContabilitaCommand().setDocumento(areaContabileFullDTO.getAreaContabile().getDocumento());

            getAreaTesoreriaCommand().setAreaTesoreriaPresente(areaContabileFullDTO.isAreaTesoreriaPresente(),
                    areaContabileFullDTO.getAreaContabile().getDocumento());

            getAreaMagazzinoCommand().setAreaMagazzinoLite(areaContabileFullDTO.getAreaMagazzinoLite());

            getDocumentiDocumentoCommand().setDocumento(areaContabileFullDTO.getAreaContabile().getDocumento());

            getDatiRitenutaAccontoCommand().setAreaContabile(areaContabileFullDTO.getAreaContabile());
        }
    }

    /**
     * Command wrapper per AreaIntraCommand nell'editor della contabilità;<br>
     * essendo in un plugin esterno e dovendo nascondere il command nel caso in cui il documento non gestisce area
     * intra, mi servo di questo wrapper per utilizzare il command come areaMagazzinoCommand.
     *
     * @author leonardo
     */
    private class AreaIntraContabilitaCommand extends ActionCommand {

        private Documento documento = null;
        private AziendaCorrente aziendaCorrente = null;
        private ActionCommand areaIntraCommand = null;

        /**
         * Costruttore, inizializzo areaIntraCommand e AziendaCorrente.
         */
        public AreaIntraContabilitaCommand() {
            super("areaIntraCommand");
            RcpSupport.configure(this);
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
         * @param aziendaCorrente
         *            the aziendaCorrente to set
         */
        public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
            this.aziendaCorrente = aziendaCorrente;
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

    /**
     * @author leonardo
     */
    private class DocumentiDocumentoCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
            AreaContabileFullDTO areaContabileFullDTOOld = (AreaContabileFullDTO) AreaContabilePage.this
                    .getBackingFormPage().getFormObject();

            AreaContabileFullDTO areaContabileFullDTO = contabilitaBD
                    .caricaAreaContabileFullDTO(areaContabileFullDTOOld.getId());
            AreaContabilePage.this.setFormObject(areaContabileFullDTO);
            // Se l'area è in stato verificato disabilito la modifica
            toolbarPageEditor.getLockCommand().setEnabled(!areaContabileFullDTO.getAreaContabile()
                    .getStatoAreaContabile().equals(StatoAreaContabile.VERIFICATO));
            firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, areaContabileFullDTO);
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            DocumentiDocumentoCommand documentoCommand = (DocumentiDocumentoCommand) arg0;
            AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) AreaContabilePage.this
                    .getBackingFormPage().getFormObject();
            documentoCommand.setDocumento(areaContabileFullDTO.getAreaContabile().getDocumento());
            return true;
        }
    }

    private class DocumentoGraphInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            command.addParameter(OpenDocumentoGraphEditorCommand.PARAM_ID_DOCUMENTO,
                    ((AreaContabileFullDTO) getForm().getFormObject()).getAreaContabile().getDocumento().getId());
            return true;
        }
    }

    private class EliminaAreaContabileCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand arg0) {
        }

        @Override
        public boolean preExecution(ActionCommand command) {
            AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) getForm().getFormObject();
            command.addParameter(EliminaAreaContabileCommand.PARAM_AREA_CONTABILE,
                    areaContabileFullDTO.getAreaContabile());
            command.addParameter(EliminaAreaContabileCommand.PARAM_AREA_MAGAZZINO,
                    areaContabileFullDTO.getAreaMagazzinoLite() != null
                            && areaContabileFullDTO.getAreaMagazzinoLite().getId() != null);
            command.addParameter(EliminaAreaContabileCommand.PARAM_AREA_TESORERIA,
                    areaContabileFullDTO.getAreaTesoreria() != null
                            && areaContabileFullDTO.getAreaTesoreria().getId() != null);
            return areaContabileFullDTO.getId() != null;
        }
    }

    private class LockCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            return checkDataBloccoContabilita();
        }
    }

    public class SaveCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
            AreaContabilePage.this.firePropertyChange(VALIDA_AREA_CONTABILE, false, true);
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            return checkDataBloccoContabilita();
        }

    }

    /**
     * Property change che intercetta il cambio di stato eseguito dalla {@link StatiAreaContabileCommandController} per
     * aggiornare le aree presenti nell'editor.
     *
     * @author
     */
    private class StatoAreaContabileChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AreaContabile areaContabile = (AreaContabile) evt.getNewValue();

            AreaContabileFullDTO areaContabileFullDTO = contabilitaBD.caricaAreaContabileFullDTO(areaContabile.getId());
            AreaContabilePage.this.setFormObject(areaContabileFullDTO);
            // Se l'area è in stato verificato disabilito la modifica
            toolbarPageEditor.getLockCommand()
                    .setEnabled(!areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.VERIFICATO));
            firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, areaContabileFullDTO);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(AreaContabilePage.class);

    public static final String PAGE_ID = "areaContabilePage";
    private static final String INVALID_RIGHE_TITLE = ".invalidRighe.title";
    private static final String INVALID_RIGHE_MESSAGE = ".invalidRighe.message";

    public static final String VALIDA_AREA_CONTABILE = "validaAreaContabile";
    private IContabilitaAnagraficaBD contabilitaAnagraficaBD = null;
    private IContabilitaBD contabilitaBD = null;

    private IDocumentiBD documentiBD = null;

    private final StatiAreaContabileCommandController statiAreaContabileCommandController;
    private AreaTesoreriaCommand areaTesoreriaCommand = null;
    private AreaMagazzinoCommand areaMagazzinoCommand = null;
    private AreaIntraContabilitaCommand areaIntraContabilitaCommand = null;
    private DocumentiDocumentoCommand documentiDocumentoCommand = null;
    private EliminaAreaContabileCommand eliminaAreaContabileCommand = null;
    private OpenDocumentoGraphEditorCommand openDocumentoGraphEditorCommand = null;

    private DatiRitenutaAccontoCommand datiRitenutaAccontoCommand = null;
    private DocumentiDocumentoCommandInterceptor documentiDocumentoCommandInterceptor = null;
    private EliminaAreaContabileCommandInterceptor eliminaAreaContabileCommandInterceptor = null;
    private SaveCommandInterceptor saveCommandInterceptor = null;

    private ActionCommand entitaCointestateCommand = null;

    private LockCommandInterceptor lockCommandInterceptor = null;
    private StatoAreaContabileChangeListener statoAreaContabileChangeListener = null;

    private AreaContabileChangeListener areaContabileChangeListener = null;
    private PluginManager pluginManager = null;

    private AziendaCorrente aziendaCorrente = null;

    /**
     * Costruttore.
     *
     * @param aziendaCorrente
     *            azienda loggata
     */
    public AreaContabilePage(final AziendaCorrente aziendaCorrente) {
        super(PAGE_ID, new AreaContabileForm(aziendaCorrente));

        statiAreaContabileCommandController = new StatiAreaContabileCommandController(
                ((AreaContabileFullDTO) super.getForm().getFormObject()).getAreaContabile());

        statiAreaContabileCommandController.addPropertyChangeListener(
                StatiAreaContabileCommandController.PROPERTY_STATO_AREA_CONTABILE,
                getStatoAreaContabileChangeListener());

        setExternalCommandLineEnd(statiAreaContabileCommandController.getCommands());

        this.aziendaCorrente = aziendaCorrente;
        this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.statiAreaContabileCommandController.setContabilitaBD(contabilitaBD);
        ((AreaContabileForm) getBackingFormPage()).setContabilitaBD(contabilitaBD);
        installPropertyChange();
    }

    /**
     * Verifica se la data dell'area contabile è valida ripetto alla data di blocco se è stata impostata.
     *
     * @return true se valida
     */
    private boolean checkDataBloccoContabilita() {
        AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) getForm().getFormObject();
        ContabilitaSettings contabilitaSettings = contabilitaAnagraficaBD.caricaContabilitaSettings();

        Date dataBlocco = null;
        if (contabilitaSettings.getDataBloccoContabilita() == null) {
            dataBlocco = contabilitaSettings.getDataBloccoIva();
        } else if (contabilitaSettings.getDataBloccoIva() == null) {
            dataBlocco = contabilitaSettings.getDataBloccoContabilita();
        } else {
            if (contabilitaSettings.getDataBloccoContabilita().compareTo(contabilitaSettings.getDataBloccoIva()) >= 0) {
                dataBlocco = contabilitaSettings.getDataBloccoContabilita();
            } else {
                dataBlocco = contabilitaSettings.getDataBloccoIva();
            }
        }

        if (dataBlocco != null && areaContabileFullDTO.getAreaContabile() != null
                && !dataBlocco.before(areaContabileFullDTO.getAreaContabile().getDataRegistrazione())) {
            PanjeaSwingUtil.checkAndThrowException(new GenericException(
                    "Blocco documenti presente. Impossibile modificare o inserire il\ndocumento con data precedente o uguale a "
                            + new SimpleDateFormat("dd/MM/yyyy").format(dataBlocco)));
            return false;
        }

        return true;
    }

    @Override
    public JComponent createControl() {
        ComponentFactory componentFactory = (ComponentFactory) Application.services()
                .getService(ComponentFactory.class);
        JPanel panelAreeColegateCommand = componentFactory.createPanel(new HorizontalLayout());

        // commands di collegamento a aree e documenti collegati
        panelAreeColegateCommand.add(getOpenDocumentoGraphEditorCommand().createButton());
        panelAreeColegateCommand.add(getDocumentiDocumentoCommand().createButton());
        panelAreeColegateCommand.add(getAreaTesoreriaCommand().createButton());
        panelAreeColegateCommand.add(getAreaMagazzinoCommand().createButton());
        panelAreeColegateCommand.add(getDatiRitenutaAccontoCommand().createButton());

        if (pluginManager.isPresente(PluginManager.PLUGIN_INTRA)) {
            panelAreeColegateCommand.add(getAreaIntraContabilitaCommand().createButton());
        }

        if (pluginManager.isPresente(PluginManager.PLUGIN_COMUNICAZIONE_POLIVALENTE)) {
            panelAreeColegateCommand.add(getEntitaCointestateCommand().createButton());
        }

        JComponent pageControl = super.createControl();
        getErrorBar().add(panelAreeColegateCommand, BorderLayout.LINE_END);

        return pageControl;
    }

    @Override
    public void dispose() {
        AreaContabileForm areaContabileForm = (AreaContabileForm) getForm();
        areaContabileForm.removeFormObjectChangeListener(getAreaContabileChangeListener());
        areaContabileChangeListener = null;

        getDocumentiDocumentoCommand().removeCommandInterceptor(getDocumentiDocumentoCommandInterceptor());
        getEliminaAreaContabileCommand().removeCommandInterceptor(getEliminaAreaContabileCommandInterceptor());

        toolbarPageEditor.getSaveCommand().removeCommandInterceptor(getSaveCommandInterceptor());

        statiAreaContabileCommandController.removePropertyChangeListener(
                StatiAreaContabileCommandController.PROPERTY_STATO_AREA_CONTABILE,
                getStatoAreaContabileChangeListener());

        super.dispose();
    }

    @Override
    protected Object doSave() {
        LOGGER.debug("--> Salvo la testata dell'area contabile.");
        AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) getBackingFormPage().getFormObject();
        AreaContabile areaContabile = areaContabileFullDTO.getAreaContabile();
        int versionDocumentoOld = -1;
        if (areaContabile.getDocumento().getVersion() != null) {
            versionDocumentoOld = areaContabile.getDocumento().getVersion().intValue();
        }

        AreaRate areaRate = null;
        TipoAreaPartita tipoAreaPartita = contabilitaBD
                .caricaTipoAreaPartitaPerTipoDocumento(areaContabile.getTipoAreaContabile().getTipoDocumento());
        boolean salvaAreaRate = (tipoAreaPartita != null) && (tipoAreaPartita.getId() != null)
                && ((TipoAreaPartita.TipoOperazione.GENERA.equals(tipoAreaPartita.getTipoOperazione()))
                        || (TipoAreaPartita.TipoOperazione.NESSUNA.equals(tipoAreaPartita.getTipoOperazione())));
        if (salvaAreaRate) {
            // Devo salvare anche il codice di pagamento che sta sull'area rate
            areaRate = areaContabileFullDTO.getAreaRate();
        }
        try {
            areaContabileFullDTO = contabilitaBD.salvaAreaContabile(areaContabile, areaRate);

        } catch (AreaContabileDuplicateException e) {
            // HACK rilancio l'eccezione all'interno di una Runtime per farla
            // rientrare all'interno
            // del framework dell'eccezioni
            // in futuro l'eccezione andra' gestita diversamente
            throw new RuntimeException(e);
        } catch (DocumentoDuplicateException e) {
            // HACK rilancio l'eccezione all'interno di una Runtime per farla
            // rientrare all'interno
            // del framework dell'eccezioni
            // in futuro l'eccezione andra' gestita diversamente
            throw new RuntimeException(e);
        }

        int versionDocumentoNew = areaContabileFullDTO.getAreaContabile().getDocumento().getVersion().intValue();
        if (versionDocumentoOld != versionDocumentoNew) {
            publishDocumento(areaContabileFullDTO.getAreaContabile().getDocumento());
        }

        // una volta salvato devo aggiornare stati controller
        statiAreaContabileCommandController.updateAreaContabile(areaContabileFullDTO.getAreaContabile());
        return areaContabileFullDTO;
    }

    /**
     * @return areaContabileChangeListener
     */
    private AreaContabileChangeListener getAreaContabileChangeListener() {
        if (areaContabileChangeListener == null) {
            areaContabileChangeListener = new AreaContabileChangeListener();
        }
        return areaContabileChangeListener;
    }

    /**
     * @return the areaIntraContabilitaCommand
     */
    public AreaIntraContabilitaCommand getAreaIntraContabilitaCommand() {
        if (areaIntraContabilitaCommand == null) {
            areaIntraContabilitaCommand = new AreaIntraContabilitaCommand();
            areaIntraContabilitaCommand.setAziendaCorrente(aziendaCorrente);
        }
        return areaIntraContabilitaCommand;
    }

    /**
     * @return command per aprire l'area magazzino
     */
    private AreaMagazzinoCommand getAreaMagazzinoCommand() {
        if (areaMagazzinoCommand == null) {
            areaMagazzinoCommand = new AreaMagazzinoCommand();
        }
        return areaMagazzinoCommand;
    }

    /**
     * @return the areaRateCommand
     */
    public AreaTesoreriaCommand getAreaTesoreriaCommand() {
        if (areaTesoreriaCommand == null) {
            areaTesoreriaCommand = new AreaTesoreriaCommand();
        }
        return areaTesoreriaCommand;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), getEliminaAreaContabileCommand() };
        // Dopo la save command devo lanciare l'evento di area validata
        toolbarPageEditor.getSaveCommand().addCommandInterceptor(getSaveCommandInterceptor());
        toolbarPageEditor.getLockCommand().addCommandInterceptor(getLockCommandInterceptor());
        return abstractCommands;
    }

    /**
     * @return Returns the contabilitaBD.
     */
    public IContabilitaBD getContabilitaBD() {
        return contabilitaBD;
    }

    /**
     * @return the datiRitenutaAccontoCommand
     */
    public DatiRitenutaAccontoCommand getDatiRitenutaAccontoCommand() {
        if (datiRitenutaAccontoCommand == null) {
            datiRitenutaAccontoCommand = new DatiRitenutaAccontoCommand();
            datiRitenutaAccontoCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
                @Override
                public void postExecution(ActionCommand command) {
                    final AreaContabile areaContabile = ((DatiRitenutaAccontoCommand) command).getAreaContabile();
                    // controllo se l'area contabile è cambiata ( modifica dei dati della ritenuta
                    // d'acconto)
                    final AreaContabileFullDTO fullDTO = (AreaContabileFullDTO) getBackingFormPage().getFormObject();
                    if (fullDTO.getAreaContabile().getId().compareTo(areaContabile.getId()) == 0
                            && fullDTO.getAreaContabile().getVersion().compareTo(areaContabile.getVersion()) != 0) {

                        fullDTO.setAreaContabile(areaContabile);

                        // se ho i requisiti per generare le righe contabili chiedo se farlo
                        if (!fullDTO.isAreaIvaEnable() || (fullDTO.isAreaIvaEnable() && fullDTO.isAreaIvaValid())) {
                            ConfirmationDialog dialog = new ConfirmationDialog("Attenzione",
                                    "Ricalcolare le righe contabili già esistenti?") {

                                @Override
                                protected void onConfirm() {
                                    contabilitaBD.cancellaRigheContabili(fullDTO.getAreaContabile());
                                    RigheContabiliBuilder righeContabiliBuilder = new RigheContabiliBuilder(
                                            contabilitaAnagraficaBD, contabilitaBD, fullDTO, null);
                                    righeContabiliBuilder.generaRigheContabili();
                                }
                            };
                            dialog.showDialog();
                        }

                        firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                                contabilitaBD.caricaAreaContabileFullDTO(areaContabile.getId()));
                    }
                }
            });
        }

        return datiRitenutaAccontoCommand;
    }

    /**
     * @return the documentiBD
     */
    public IDocumentiBD getDocumentiBD() {
        return documentiBD;
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
     * @return the eliminaAreaContabileCommand
     */
    private EliminaAreaContabileCommand getEliminaAreaContabileCommand() {
        if (eliminaAreaContabileCommand == null) {
            eliminaAreaContabileCommand = new EliminaAreaContabileCommand(PAGE_ID);
            eliminaAreaContabileCommand.setContabilitaBD(contabilitaBD);
            eliminaAreaContabileCommand.addCommandInterceptor(getEliminaAreaContabileCommandInterceptor());
        }
        return eliminaAreaContabileCommand;
    }

    /**
     * @return eliminaAreaContabileCommandInterceptor
     */
    private EliminaAreaContabileCommandInterceptor getEliminaAreaContabileCommandInterceptor() {
        if (eliminaAreaContabileCommandInterceptor == null) {
            eliminaAreaContabileCommandInterceptor = new EliminaAreaContabileCommandInterceptor();
        }
        return eliminaAreaContabileCommandInterceptor;
    }

    /**
     * @return the entitaCointestateCommand
     */
    private ActionCommand getEntitaCointestateCommand() {
        if (entitaCointestateCommand == null) {
            entitaCointestateCommand = RcpSupport.getCommand("aggiungiEntitaCointestateCommand");
            entitaCointestateCommand.addCommandInterceptor(new ActionCommandInterceptorAdapter() {
                @Override
                public boolean preExecution(ActionCommand command) {
                    AreaContabileFullDTO areaContabileFullDTO = (AreaContabileFullDTO) AreaContabilePage.this
                            .getBackingFormPage().getFormObject();
                    command.addParameter("paramAreaContabile", areaContabileFullDTO.getAreaContabile());
                    return true;
                }
            });
        }

        return entitaCointestateCommand;
    }

    /**
     * @return the lockCommandInterceptor
     */
    private LockCommandInterceptor getLockCommandInterceptor() {
        if (lockCommandInterceptor == null) {
            lockCommandInterceptor = new LockCommandInterceptor();
        }

        return lockCommandInterceptor;
    }

    /**
     * @return Returns the openDocumentoGraphEditorCommand.
     */
    private OpenDocumentoGraphEditorCommand getOpenDocumentoGraphEditorCommand() {
        if (openDocumentoGraphEditorCommand == null) {
            openDocumentoGraphEditorCommand = new OpenDocumentoGraphEditorCommand();
            openDocumentoGraphEditorCommand.addCommandInterceptor(new DocumentoGraphInterceptor());
        }

        return openDocumentoGraphEditorCommand;
    }

    /**
     * @return SaveCommandInterceptor
     */
    private SaveCommandInterceptor getSaveCommandInterceptor() {
        if (saveCommandInterceptor == null) {
            saveCommandInterceptor = new SaveCommandInterceptor();
        }
        return saveCommandInterceptor;
    }

    /**
     * @return statoAreaContabileChangeListener
     */
    private StatoAreaContabileChangeListener getStatoAreaContabileChangeListener() {
        if (statoAreaContabileChangeListener == null) {
            statoAreaContabileChangeListener = new StatoAreaContabileChangeListener();
        }
        return statoAreaContabileChangeListener;
    }

    @Override
    protected boolean insertControlInScrollPane() {
        return false;
    }

    /**
     * Installa il propertyChange sull'area aontabile.
     */
    private void installPropertyChange() {
        AreaContabileForm areaContabileForm = (AreaContabileForm) getForm();
        areaContabileForm.addFormObjectChangeListener(getAreaContabileChangeListener());
    }

    @Override
    public void loadData() {
        LOGGER.debug("--> Enter loadData");
    }

    @Override
    public void onNew() {
        AreaContabileFullDTO oldFullDTO = (AreaContabileFullDTO) getBackingFormPage().getFormObject();

        super.onNew();

        AreaContabileFullDTO fullDTO = (AreaContabileFullDTO) getBackingFormPage().getFormObject();
        fullDTO.initializedNewObject(oldFullDTO);
        AreaContabile areaContabile = fullDTO.getAreaContabile();

        // clono e reimposto al form model il tipo area contabile in modo da
        // attivare il property change che va a impostare tutti i dati di default per quel tac.
        TipoAreaContabile tac = (TipoAreaContabile) PanjeaSwingUtil
                .cloneObject(fullDTO.getAreaContabile().getTipoAreaContabile());
        if (areaContabile.getTipoAreaContabile() != null && !areaContabile.getTipoAreaContabile().isNew()) {
            getBackingFormPage().getValueModel("areaContabile.tipoAreaContabile").setValue(tac);
        }

        // Devo aggiornare il controller degli stati dato che quando viene
        // preparato il nuovo oggetto l'evento object changed viene lanciato
        // da this e quindi this non viene inclusa nel processo
        // di aggiornamento delle pagine che sono contenute nella dockingpage
        statiAreaContabileCommandController.updateAreaContabile(fullDTO.getAreaContabile());
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
        PanjeaFocusTraversalPolicy areaContabileFocusTraversalPolicy = ((AreaContabileForm) getBackingFormPage())
                .getAreaContabileFocusTraversalPolicy();
        getControl().getParent().getParent().getParent().getParent()
                .setFocusTraversalPolicy(areaContabileFocusTraversalPolicy);
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
        super.postSetFormObject(object);

        getOpenDocumentoGraphEditorCommand().setEnabled(!((AreaContabileFullDTO) object).isNew());

        ((AreaContabileForm) getBackingFormPage()).registerPropertyChange();
    }

    @Override
    public void preSetFormObject(Object object) {
        super.preSetFormObject(object);
        ((AreaContabileForm) getBackingFormPage()).unregisterPropertyChange();
    }

    /**
     * @param documento
     *            il documento da lanciare
     */
    private void publishDocumento(Documento documento) {
        PanjeaLifecycleApplicationEvent documentoUpdateEvent = new PanjeaLifecycleApplicationEvent(
                LifecycleApplicationEvent.MODIFIED, documento, AreaContabilePage.this);
        Application.instance().getApplicationContext().publishEvent(documentoUpdateEvent);
    }

    @Override
    public void refreshData() {
        Documento documento = ((AreaContabileFullDTO) getBackingFormPage().getFormObject()).getAreaContabile()
                .getDocumento();
        publishDocumento(documento);
    }

    /**
     * @param contabilitaAnagraficaBD
     *            the contabilitaAnagraficaBD to set
     */
    public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    /**
     * @param contabilitaBD
     *            the contabilitaBD to set
     */
    public void setContabilitaBD(IContabilitaBD contabilitaBD) {
        this.contabilitaBD = contabilitaBD;
    }

    /**
     * @param documentiBD
     *            the documentiBD to set
     */
    public void setDocumentiBD(IDocumentiBD documentiBD) {
        this.documentiBD = documentiBD;
    }

    @Override
    public void setFormObject(Object object) {
        LOGGER.debug("--> Enter setFormObject");
        AreaContabile areaContabile = ((AreaContabileFullDTO) object).getAreaContabile();
        if (areaContabile.isNew()) {
            this.toolbarPageEditor.getNewCommand().execute();
        } else {
            // Messaggi di notifica: controllo quali proprieta' dell'area
            // contabile sono cambiate
            AreaContabile areaContabileOld = ((AreaContabileFullDTO) getForm().getFormObject()).getAreaContabile();
            if (!areaContabile.getVersion().equals(areaContabileOld.getVersion())) {

                if (areaContabileOld.getStatoAreaContabile().equals(StatoAreaContabile.CONFERMATO)
                        && areaContabile.getStatoAreaContabile().equals(StatoAreaContabile.PROVVISORIO)) {
                    String titolo = getMessageSource().getMessage(PAGE_ID + INVALID_RIGHE_TITLE, new Object[] {},
                            Locale.getDefault());
                    String messaggio = getMessageSource().getMessage(PAGE_ID + INVALID_RIGHE_MESSAGE, new Object[] {},
                            Locale.getDefault());

                    Message message = new DefaultMessage(titolo + "\n" + messaggio, Severity.INFO);
                    MessageAlert messageAlert = new MessageAlert(message);
                    messageAlert.showAlert();
                }
            }
            super.setFormObject(object);

            StatoAreaContabile statoAreaContabile = ((AreaContabileFullDTO) object).getAreaContabile()
                    .getStatoAreaContabile();
            getEliminaAreaContabileCommand().setEnabled(!StatoAreaContabile.VERIFICATO.equals(statoAreaContabile));
            // Se l'area è in stato verificato disabilito la modifica
            toolbarPageEditor.getLockCommand().setEnabled(!StatoAreaContabile.VERIFICATO.equals(statoAreaContabile));
        }
        statiAreaContabileCommandController.updateAreaContabile(areaContabile);
    }

    @Override
    public void updateCommands() {
        super.updateCommands();

        getDatiRitenutaAccontoCommand().setEnabled(getBackingFormPage().getFormModel().isReadOnly());
    }

}
