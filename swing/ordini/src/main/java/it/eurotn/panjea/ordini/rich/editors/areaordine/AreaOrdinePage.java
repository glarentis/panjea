package it.eurotn.panjea.ordini.rich.editors.areaordine;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.HorizontalLayout;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.core.Message;
import org.springframework.richclient.core.Severity;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.rich.bd.IVendingAreaRifornimentoBD;
import it.eurotn.panjea.magazzino.rich.commands.documento.StatiAreaMagazzinoCommandController;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine.StatoAreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.forms.areaordine.AreaOrdineAltroForm;
import it.eurotn.panjea.ordini.rich.forms.areaordine.AreaOrdineForm;
import it.eurotn.panjea.ordini.rich.forms.areaordine.DatiAccessoriForm;
import it.eurotn.panjea.ordini.rich.forms.areaordine.PiedeAreaOrdiniForm;
import it.eurotn.panjea.ordini.rich.forms.areaordine.TipoAreaOrdinePropertyChange;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.IEditorListener;
import it.eurotn.panjea.rich.PanjeaLifecycleApplicationEvent;
import it.eurotn.panjea.rich.editors.documento.StampaAreaDocumentoSplitCommand;
import it.eurotn.panjea.rich.editors.documentograph.OpenDocumentoGraphEditorCommand;
import it.eurotn.panjea.rich.focuspolicy.PanjeaFocusTraversalPolicy;
import it.eurotn.panjea.stampe.domain.LayoutStampa;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.command.SeparatorActionCommand;
import it.eurotn.rich.dialog.MessageAlert;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class AreaOrdinePage extends FormsBackedTabbedDialogPageEditor implements InitializingBean, IEditorListener {

    private class AreaOrdineChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) evt.getNewValue();
            statoEvasioneOrdineLabel.aggiornaStato(areaOrdineFullDTO.getAreaOrdine());
        }
    }

    private class CopiaAreaOrdineCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) getBackingFormPage().getFormObject();
            command.addParameter(CopiaAreaOrdineCommand.PARAM_AREA_ORDINE_ID,
                    areaOrdineFullDTO.getAreaOrdine().getId());
            return true;
        }
    }

    private class DocumentoGraphInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {
            command.addParameter(OpenDocumentoGraphEditorCommand.PARAM_ID_DOCUMENTO,
                    ((AreaOrdineFullDTO) getForm().getFormObject()).getAreaOrdine().getDocumento().getId());
            return true;
        }
    }

    private class EliminaAreaOrdineCommandInterceptor implements ActionCommandInterceptor {
        @Override
        public void postExecution(ActionCommand arg0) {
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) getForm().getFormObject();
            arg0.addParameter(EliminaAreaOrdineCommand.PARAM_AREA_ORDINE, areaOrdineFullDTO.getAreaOrdine());
            return true;
        }
    }

    public class SaveCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand arg0) {
            AreaOrdinePage.this.firePropertyChange(VALIDA_AREA_ORDINE, false, true);
        }

        @Override
        public boolean preExecution(ActionCommand arg0) {
            return true;
        }
    }

    /**
     * Property change che intercetta il cambio di stato eseguito dalla
     * {@link StatiAreaOrdineCommandController} per aggiornare le aree presenti nell'editor.
     */
    private class StatoAreaOrdineChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AreaOrdine areaOrdine = ((AreaOrdine) evt.getNewValue());

            AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) getForm().getFormObject();
            areaOrdineFullDTO.setAreaOrdine(areaOrdine);
            setFormObject(areaOrdineFullDTO);
            firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null, areaOrdineFullDTO);
        }
    }

    private class TipoAreaOrdineChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            stampaDocumentoSplitCommand.updateCommand(((AreaOrdineFullDTO) getForm().getFormObject()).getAreaOrdine());
        }
    }

    private static Logger logger = Logger.getLogger(AreaOrdinePage.class);

    public static final String PAGE_ID = "areaOrdinePage";
    public static final String VALIDA_AREA_ORDINE = "validaAreaOrdine";

    private static final String INVALID_RIGHE_TITLE = "invalidRighe.title";

    private static final String INVALID_RIGHE_MESSAGE = "invalidRighe.message";
    private AziendaCorrente aziendaCorrente;

    private IAnagraficaBD anagraficaBD;

    private IOrdiniDocumentoBD ordiniDocumentoBD;

    private StatiAreaOrdineCommandController statiAreaOrdineCommandController;
    private StatoAreaOrdineChangeListener statoAreaOrdineChangeListener;
    private StatoEvasioneOrdineLabel statoEvasioneOrdineLabel;

    private EliminaAreaOrdineCommand eliminaAreaOrdineCommand;
    private EliminaAreaOrdineCommandInterceptor eliminaAreaOrdineCommandInterceptor;

    private CopiaAreaOrdineCommand copiaAreaOrdineCommand;
    private OpenDocumentoGraphEditorCommand openDocumentoGraphEditorCommand;

    private DocumentoGraphInterceptor documentoGraphInterceptor;

    private AreaOrdineChangeListener areaOrdineChangeListener;

    private StampaAreaDocumentoSplitCommand stampaDocumentoSplitCommand;

    private TipoAreaOrdineChangeListener tipoAreaDocumentoChangeListener;

    private IVendingAreaRifornimentoBD vendingAreaRifornimentoBD = null;

    /**
     * Costruttore.
     */
    public AreaOrdinePage() {
        super(PAGE_ID, new AreaOrdineForm());
    }

    @Override
    public void addForms() {
        AreaOrdineAltroForm areaOrdineAltroForm = new AreaOrdineAltroForm(getBackingFormPage().getFormModel());
        addForm(areaOrdineAltroForm);

        DatiAccessoriForm datiAccessoriForm = new DatiAccessoriForm(getBackingFormPage().getFormModel(),
                this.anagraficaBD);
        addForm(datiAccessoriForm);

        PiedeAreaOrdiniForm piedeAreaOrdiniForm = new PiedeAreaOrdiniForm(getBackingFormPage().getFormModel(),
                aziendaCorrente);
        addForm(piedeAreaOrdiniForm);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AreaOrdineForm areaOrdineForm = (AreaOrdineForm) getForm();
        areaOrdineForm.setAziendaCorrente(aziendaCorrente);
        installPropertyChange();
        statoAreaOrdineChangeListener = new StatoAreaOrdineChangeListener();
        statiAreaOrdineCommandController = new StatiAreaOrdineCommandController();
        statiAreaOrdineCommandController.addPropertyChangeListener(
                StatiAreaMagazzinoCommandController.PROPERTY_STATO_AREA_MAGAZZINO, statoAreaOrdineChangeListener);
        setExternalCommandLineEnd(statiAreaOrdineCommandController.getCommands());
    }

    /**
     * Controlla se ho un cambio stato dell'areaOrdine.<br/>
     * Se ho avuto il cambio avviso.
     *
     * @param areaOrdineFullDTOCheck
     *            area ordine da controllare
     */
    private void avvisaCambioStato(AreaOrdineFullDTO areaOrdineFullDTOCheck) {
        AreaOrdine areaOrdineOld = ((AreaOrdineFullDTO) getForm().getFormObject()).getAreaOrdine();
        AreaOrdine areaOrdine = areaOrdineFullDTOCheck.getAreaOrdine();

        if (areaOrdine != null && areaOrdine.getId() != null && areaOrdine.getId().equals(areaOrdineOld.getId())) {

            if (!areaOrdine.getVersion().equals(areaOrdineOld.getVersion())) {
                // se la version risulta incrementata,verifico lo stato prima e dopo.
                StatoAreaOrdine statoOld = areaOrdineOld.getStatoAreaOrdine();
                StatoAreaOrdine stato = areaOrdine.getStatoAreaOrdine();

                // verifica del cambio dello stato da forzato o confermato a provvisorio
                if (!statoOld.equals(stato) && stato == StatoAreaOrdine.PROVVISORIO) {
                    String titolo = getMessageSource().getMessage(INVALID_RIGHE_TITLE, new Object[] {},
                            Locale.getDefault());
                    String messaggio = getMessageSource().getMessage(INVALID_RIGHE_MESSAGE, new Object[] {},
                            Locale.getDefault());

                    Message message = new DefaultMessage(titolo + "\n" + messaggio, Severity.INFO);
                    MessageAlert messageAlert = new MessageAlert(message);
                    messageAlert.showAlert();
                }
            }
        } else if (areaOrdine != null && areaOrdine.isNew()) {
            this.toolbarPageEditor.getNewCommand().execute();
        }
    }

    @Override
    public JComponent createControl() {

        JPanel panelCommands = getComponentFactory().createPanel(new HorizontalLayout());
        panelCommands.add(getOpenDocumentoGraphEditorCommand().createButton());
        statoEvasioneOrdineLabel = new StatoEvasioneOrdineLabel();
        panelCommands.add(statoEvasioneOrdineLabel);

        JComponent mainControl = super.createControl();
        getErrorBar().add(panelCommands, BorderLayout.LINE_END);
        return mainControl;
    }

    @Override
    public void dispose() {
        getErrorBar().removeAll();
        eliminaAreaOrdineCommand.removeCommandInterceptor(eliminaAreaOrdineCommandInterceptor);
        openDocumentoGraphEditorCommand.removeCommandInterceptor(documentoGraphInterceptor);

        AreaOrdineForm areaOrdineForm = (AreaOrdineForm) getForm();
        areaOrdineForm.removeFormObjectChangeListener(getAreaOrdineChangeListener());

        super.dispose();
    }

    @Override
    protected Object doSave() {
        AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) getBackingFormPage().getFormObject();

        // clone di AreaOrdineFullDTO per permettere il revert del form model se
        // il salvataggio solleva una exception
        areaOrdineFullDTO = (AreaOrdineFullDTO) PanjeaSwingUtil.cloneObject(areaOrdineFullDTO);
        areaOrdineFullDTO = save(areaOrdineFullDTO);
        // Aggiorno se ho un cambio stato. Ricordarsi che la doSave non chiama
        // la preSetFormObejct della stessa pagina.
        statiAreaOrdineCommandController.updateAreaOrdine(areaOrdineFullDTO.getAreaOrdine());
        avvisaCambioStato(areaOrdineFullDTO);

        return areaOrdineFullDTO;
    }

    /**
     * @return AreaOrdineChangeListener
     */
    private AreaOrdineChangeListener getAreaOrdineChangeListener() {
        if (areaOrdineChangeListener == null) {
            areaOrdineChangeListener = new AreaOrdineChangeListener();
        }
        return areaOrdineChangeListener;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), getEliminaCommand(), getStampaDocumentoCommand(),
                SeparatorActionCommand.getInstance(), getCopiaAreaOrdineCommand() };
        toolbarPageEditor.getSaveCommand().addCommandInterceptor(new SaveCommandInterceptor());
        return abstractCommands;
    }

    /**
     * @return the copiaAreaOrdineCommand
     */
    public CopiaAreaOrdineCommand getCopiaAreaOrdineCommand() {
        if (copiaAreaOrdineCommand == null) {
            copiaAreaOrdineCommand = new CopiaAreaOrdineCommand();
            copiaAreaOrdineCommand.addCommandInterceptor(new CopiaAreaOrdineCommandInterceptor());
        }
        return copiaAreaOrdineCommand;
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

    /**
     * @return command per l'eliminazione dell'area magazzino
     */
    public EliminaAreaOrdineCommand getEliminaCommand() {
        if (eliminaAreaOrdineCommand == null) {
            eliminaAreaOrdineCommand = new EliminaAreaOrdineCommand(PAGE_ID);
            eliminaAreaOrdineCommand.setOrdiniDocumentoBD(ordiniDocumentoBD);
            eliminaAreaOrdineCommandInterceptor = new EliminaAreaOrdineCommandInterceptor();
            eliminaAreaOrdineCommand.addCommandInterceptor(eliminaAreaOrdineCommandInterceptor);
        }
        return eliminaAreaOrdineCommand;
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
     * @return comando per la stampa
     */
    public StampaAreaDocumentoSplitCommand getStampaDocumentoCommand() {
        if (stampaDocumentoSplitCommand == null) {
            stampaDocumentoSplitCommand = new StampaAreaDocumentoSplitCommand();
            tipoAreaDocumentoChangeListener = new TipoAreaOrdineChangeListener();
            getForm().getValueModel("areaOrdine.tipoAreaOrdine")
                    .addValueChangeListener(tipoAreaDocumentoChangeListener);
        }
        return stampaDocumentoSplitCommand;
    }

    /**
     * Installa il property change da attivare al cambio dell'oggetto nel form.
     */
    private void installPropertyChange() {
        AreaOrdineForm areaOrdineForm = (AreaOrdineForm) getForm();
        areaOrdineForm.addFormObjectChangeListener(getAreaOrdineChangeListener());
    }

    @Override
    public void loadData() {
    }

    @Override
    public void onEditorEvent(ApplicationEvent event) {
        // se l'evento e' del ciclo di vita dell'applicazione
        if (event instanceof PanjeaLifecycleApplicationEvent) {

            // il ciclo di vita dell'applicazione per recuperare il tipo
            Object eventObject = event.getSource();

            if ((eventObject instanceof LayoutStampa)) {
                getStampaDocumentoCommand()
                        .updateCommand(((AreaOrdineFullDTO) getForm().getFormObject()).getAreaOrdine());
            }
        }
    }

    @Override
    public ILock onLock() {
        ILock lock = super.onLock();
        ((AreaOrdineForm) getForm()).requestFocusForFormComponent();
        return lock;
    }

    @Override
    public void onNew() {
        super.onNew();
        AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) ((AreaOrdineForm) getForm()).getFormObject();
        AreaOrdine areaOrdine = areaOrdineFullDTO.getAreaOrdine();

        TipoAreaOrdinePropertyChange propertyChange = RcpSupport.getBean("areaOrdine.tipoAreaOrdinePropertyChange");
        propertyChange.setFormModel(((AreaOrdineForm) getForm()).getFormModel());
        propertyChange.propertyChange(
                new PropertyChangeEvent(this, "areaOrdine.tipoAreaOrdine", null, areaOrdine.getTipoAreaOrdine()));
        statiAreaOrdineCommandController.updateAreaOrdine(areaOrdine);
        getStampaDocumentoCommand().updateCommand(areaOrdine);
        setTabForm(0);
        ((AreaOrdineForm) getForm()).requestFocusForFormComponent();
    }

    @Override
    public void onPostPageOpen() {
        /*
         * devo accedere al com.jidesoft.docking.DockableFrame[key=areaContabilePage
         * ,mode=DOCK,side=EAST] della page AreaContabilePage per impostare la focusTraversalPolicy
         * personalizzata, ad altri livelli non funziona;<br> dato che la traversal policy richiede
         * una lista di componenti ordinata, devo costruirla e quindi associare i components sul
         * form AreaContabileForm, ma devo impostarla qui nella page dato che i controlli sono stati
         * creati e quindi posso accedere al Parent del control (ad es. nella createControl del Form
         * ho i componenti ma non ho il parent dato che il control non e' stato aggiunto al control
         * della page, arrivando a cascata fino al DockableFrame)
         */
        PanjeaFocusTraversalPolicy areaContabileFocusTraversalPolicy = ((AreaOrdineForm) getBackingFormPage())
                .getAreaOrdineFocusTraversalPolicy();
        getControl().getParent().getParent().getParent().getParent()
                .setFocusTraversalPolicy(areaContabileFocusTraversalPolicy);
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void postSetFormObject(Object object) {
        AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) object;
        AreaOrdine areaOrdine = areaOrdineFullDTO.getAreaOrdine();

        getEliminaCommand().addParameter(EliminaAreaOrdineCommand.PARAM_AREA_ORDINE, areaOrdineFullDTO.getAreaOrdine());

        statiAreaOrdineCommandController.updateAreaOrdine(areaOrdine);
        setReadOnly(areaOrdine.getStatoAreaOrdine() == StatoAreaOrdine.CONFERMATO && areaOrdine.isEvaso());
        getNewCommand().setEnabled(true);
        getCopiaAreaOrdineCommand().setEnabled(areaOrdine.getId() != null);
        getCopiaAreaOrdineCommand()
                .setVisible(areaOrdine.getId() != null && !areaOrdine.getTipoAreaOrdine().isOrdineProduzione());
        stampaDocumentoSplitCommand.updateCommand(areaOrdine);
    }

    @Override
    public void preSetFormObject(Object object) {
        AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) object;
        avvisaCambioStato((AreaOrdineFullDTO) object);
        AreaOrdine areaOrdine = areaOrdineFullDTO.getAreaOrdine();

        if (areaOrdine.isNew()) {
            this.toolbarPageEditor.getNewCommand().execute();
        }
    }

    @Override
    public void refreshData() {
    }

    /**
     * Salva un'area ordine full DTO. se viene modificata la data di consegna devo decidere se
     * aggiornare tutte le righe o solo le righe con data uguale.
     *
     * @param areaOrdineFullDTO
     *            area ordine da salvare
     * @return area ordine salvata
     */
    private AreaOrdineFullDTO save(AreaOrdineFullDTO areaOrdineFullDTO) {
        logger.debug("--> Enter save");

        AreaRate areaRate = null;
        if (areaOrdineFullDTO.isAreaRateEnabled()) {
            areaRate = areaOrdineFullDTO.getAreaRate();
        }

        if (vendingAreaRifornimentoBD != null) {
            areaOrdineFullDTO = vendingAreaRifornimentoBD.salvaAreaOrdine(areaOrdineFullDTO.getAreaOrdine(), areaRate,
                    areaOrdineFullDTO.getAreaRifornimento());
        } else {
            areaOrdineFullDTO = ordiniDocumentoBD.salvaAreaOrdine(areaOrdineFullDTO.getAreaOrdine(), areaRate);
        }

        logger.debug("--> Exit save");
        return areaOrdineFullDTO;
    }

    /**
     * @param anagraficaBD
     *            The anagraficaBD to set.
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    /**
     * @param ordiniDocumentoBD
     *            the ordiniDocumentoBD to set
     */
    public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
        this.ordiniDocumentoBD = ordiniDocumentoBD;
    }

    /**
     * @param vendingAreaRifornimentoBD
     *            The vendingAreaRifornimentoBD to set.
     */
    public void setVendingAreaRifornimentoBD(IVendingAreaRifornimentoBD vendingAreaRifornimentoBD) {
        this.vendingAreaRifornimentoBD = vendingAreaRifornimentoBD;
    }
}
