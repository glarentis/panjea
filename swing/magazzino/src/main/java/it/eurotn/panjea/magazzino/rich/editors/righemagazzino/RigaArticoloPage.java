package it.eurotn.panjea.magazzino.rich.editors.righemagazzino;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.Locale;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.support.ActionCommandInterceptorAdapter;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.form.Form;

import com.jidesoft.swing.JideTabbedPane;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.locking.IDefProperty;
import it.eurotn.locking.ILock;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AttributoRiga;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.bd.IModuloPrezzoBD;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.NoteRigaForm;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RigaArticolo2Form;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RigaArticoloForm;
import it.eurotn.panjea.magazzino.rich.forms.rigamagazzino.RigheCollegateForm;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.editors.controllers.DefaultController;
import it.eurotn.rich.form.PanjeaAbstractForm;

public class RigaArticoloPage extends FormsBackedTabbedDialogPageEditor
        implements InitializingBean, PropertyChangeListener {

    private class OpenSituazioneCauzioniCommandInterceptor extends ActionCommandInterceptorAdapter {

        @Override
        public boolean preExecution(ActionCommand command) {

            command.addParameter(OpenSituazioneCauzioniCommand.PARAM_ID_ENTITA,
                    areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getEntita());

            ArticoloLite articolo = ((RigaArticolo) getBackingFormPage().getFormObject()).getArticolo();
            command.addParameter(OpenSituazioneCauzioniCommand.PARAM_ID_ARTICOLO, articolo);
            return true;
        }
    }

    private final class QtaLottiMaggioreDialog extends ConfirmationDialog {

        private boolean ricalcolaLottiRiga = false;

        /**
         * Costruttore.
         */
        private QtaLottiMaggioreDialog() {
            super("ATTENZIONE",
                    "La quantità assegnata ai lotti risulta essere superiore a quella dell'articolo.<br> Ricalcolare automaticamente i lotti?<br><br> Se i lotti non verranno ricalcolati dovranno essere modificati manualmente.");
            setPreferredSize(new Dimension(550, 150));
        }

        /**
         * @return Returns the ricalcolaLottiRiga.
         */
        public boolean isRicalcolaLottiRiga() {
            return ricalcolaLottiRiga;
        }

        @Override
        protected void onConfirm() {
            ricalcolaLottiRiga = true;
        }

    }

    private static final int TAB_LOTTO = 3;

    private static Logger logger = Logger.getLogger(RigaArticoloPage.class);

    public static final String PAGE_ID = "rigaArticoloPage";
    private AziendaCorrente aziendaCorrente;

    private AreaMagazzinoFullDTO areaMagazzinoFullDTO;
    private PluginManager pluginManager;

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;
    private IModuloPrezzoBD moduloPrezzoBD;
    private String righeLottiFormClass = null;

    private String righeConaiComponenteForm = null;
    private RigheCollegateForm righeCollegateForm;

    private SaveCommandInterceptor saveCommandInterceptor = null;

    private JideTabbedPane pane;
    private RigaArticolo2Form rigaArticolo2Form;
    private OpenSituazioneCauzioniCommand openSituazioneCauzioniCommand;

    private OpenSituazioneCauzioniCommandInterceptor openSituazioneCauzioniCommandInterceptor;

    private String titleRigheCollegateFormTab;

    /**
     * Costruttore.
     */
    public RigaArticoloPage() {
        this(PAGE_ID, new RigaArticoloForm());
    }

    /**
     * Costruttore.
     *
     * @param parentPageId
     *            idPagina
     * @param backingFormPage
     *            form
     */
    public RigaArticoloPage(final String parentPageId, final Form backingFormPage) {
        super(parentPageId, backingFormPage);
    }

    @Override
    public void addForms() {
        rigaArticolo2Form = new RigaArticolo2Form(getBackingFormPage().getFormModel());
        boolean isContabilitaPresente = pluginManager.isPresente("panjeaContabilita");
        rigaArticolo2Form.setContabilitaPluginEnabled(isContabilitaPresente);
        rigaArticolo2Form.getFormModel().getValueModel("articolo").addValueChangeListener(this);
        addForm(rigaArticolo2Form);

        NoteRigaForm noteRigaForm = new NoteRigaForm(getBackingFormPage().getFormModel());
        addForm(noteRigaForm);

        // se è stato configurato il form per la gestione dei lotti e scadenze
        // lo aggiungo alla pagina.
        // Istanzio il form con un classForName per non legare il plugin del
        // magazzino a quello dei lotti e per poter usare lo stesso formmodel. (
        // la setFormmodel è protetta )
        if (righeLottiFormClass != null) {
            PanjeaAbstractForm lottiForm = null;
            try {
                lottiForm = (PanjeaAbstractForm) BeanUtils.instantiateClass(
                        Class.forName(righeLottiFormClass).getConstructor(FormModel.class),
                        new Object[] { getBackingFormPage().getFormModel() });
            } catch (Exception e) {
                logger.error("-->errore durante la creazione del form per la gestione dei lotti.", e);
                PanjeaSwingUtil.checkAndThrowException(e);
            }
            addForm(lottiForm);
        }

        if (righeConaiComponenteForm != null) {
            PanjeaAbstractForm conaiComponentiForm = null;
            try {
                conaiComponentiForm = (PanjeaAbstractForm) BeanUtils.instantiateClass(
                        Class.forName(righeConaiComponenteForm).getConstructor(FormModel.class),
                        new Object[] { getBackingFormPage().getFormModel() });
            } catch (Exception e) {
                logger.error("-->errore durante la creazione del form per la gestione conai.", e);
                PanjeaSwingUtil.checkAndThrowException(e);
            }
            addForm(conaiComponentiForm);
        }

        righeCollegateForm = new RigheCollegateForm(getBackingFormPage().getFormModel());
        titleRigheCollegateFormTab = getMessageSource().getMessage(
                getId() + ".tab." + righeCollegateForm.getId() + ".title", new Object[] {}, Locale.getDefault());
        addForm(righeCollegateForm);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ((RigaArticoloForm) getBackingFormPage()).setAziendaCorrente(aziendaCorrente);
    }

    @Override
    protected void configureTabbedPane(JTabbedPane tabbedPane) {
        pane = (JideTabbedPane) tabbedPane;

        pane.setTabShape(JideTabbedPane.SHAPE_BOX);
        pane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
        pane.setTabResizeMode(JideTabbedPane.RESIZE_MODE_FIT);
        pane.setTabPlacement(SwingConstants.LEFT);
    }

    @Override
    public void dispose() {

        openSituazioneCauzioniCommand.removeCommandInterceptor(openSituazioneCauzioniCommandInterceptor);
        openSituazioneCauzioniCommand = null;

        rigaArticolo2Form.getFormModel().getValueModel("articolo").removeValueChangeListener(this);
        rigaArticolo2Form = null;

        for (int i = 0; i < pane.getTabCount() - 1; i++) {
            pane.removeTabAt(i);
        }
        pane.removeAll();

        super.dispose();

        pane = new JideTabbedPane();
    }

    @Override
    protected Object doSave() {
        logger.debug("--> Enter doSave");
        RigaArticolo rigaArticolo = (RigaArticolo) getBackingFormPage().getFormObject();
        for (RigaLotto rigaLotto : rigaArticolo.getRigheLotto()) {
            rigaLotto.setRigaArticolo(rigaArticolo);
        }
        for (AttributoRiga attributoRiga : rigaArticolo.getAttributi()) {
            attributoRiga.setRigaArticolo(rigaArticolo);
        }

        // risetto il codice entità che è transiente solo per visualizzarlo
        // nella tabella delle righe altrimenti andrei a perderlo finchè non
        // faccio un ricarica
        String codiceArticoloEntita = null;
        // NPE da mail, controllo che l'articolo non sia null
        if (rigaArticolo.getArticolo() != null) {
            codiceArticoloEntita = rigaArticolo.getArticolo().getCodiceEntita();
        }

        try {
            rigaArticolo = (RigaArticolo) magazzinoDocumentoBD.salvaRigaMagazzino(rigaArticolo);
            rigaArticolo.getArticolo().setCodiceEntita(codiceArticoloEntita);
            setTabForm(0);
        } catch (RimanenzaLottiNonValidaException e) {
            if (!e.getRigheLotto().isEmpty()) {
                getBackingFormPage().getValueModel("righeLotto").setValue(e.getRigheLotto());
            }
            setTabForm(TAB_LOTTO);
            PanjeaSwingUtil.checkAndThrowException(e);
        } catch (RigheLottiNonValideException e) {
            setTabForm(TAB_LOTTO);
            PanjeaSwingUtil.checkAndThrowException(e);
        } catch (QtaLottiMaggioreException e) {
            setTabForm(TAB_LOTTO);
            QtaLottiMaggioreDialog dialog = new QtaLottiMaggioreDialog();
            dialog.showDialog();
            if (dialog.isRicalcolaLottiRiga()) {
                rigaArticolo.getRigheLotto().clear();
                try {
                    rigaArticolo = (RigaArticolo) magazzinoDocumentoBD.salvaRigaMagazzino(rigaArticolo);
                    rigaArticolo.getArticolo().setCodiceEntita(codiceArticoloEntita);
                } catch (RimanenzaLottiNonValidaException e1) {
                    // sò che se mi arriva questa eccezione è stata generata
                    // dalla gestione automatica dei lotti quindi
                    // prendo
                    // le righe lotti generate e le aggiungo alla riga articolo
                    if (!e1.getRigheLotto().isEmpty()) {
                        getBackingFormPage().getValueModel("righeLotto").setValue(e1.getRigheLotto());
                    }
                    setTabForm(3);
                    PanjeaSwingUtil.checkAndThrowException(e1);
                } catch (RigheLottiNonValideException e1) {
                    setTabForm(3);
                    PanjeaSwingUtil.checkAndThrowException(e);
                } catch (QtaLottiMaggioreException e1) {
                    // non mi verrà mai lanciata perchè ho tolto i lotti dalla
                    // riga prima di salvarla
                    PanjeaSwingUtil.checkAndThrowException(e1);
                }
            } else {
                return null;
            }
        }

        logger.debug("--> Exit doSave");
        return rigaArticolo;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        logger.debug("--> Enter getCommand");
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand(),
                getOpenSituazioneCauzioniCommand() };

        if (saveCommandInterceptor == null) {
            saveCommandInterceptor = new SaveCommandInterceptor();
            toolbarPageEditor.getSaveCommand().addCommandInterceptor(saveCommandInterceptor);
        }

        return abstractCommands;
    }

    /**
     *
     * @return IModuloPrezzoBD
     */
    public IModuloPrezzoBD getModuloPrezzoBD() {
        return moduloPrezzoBD;
    }

    @Override
    protected Object getNewEditorObject() {
        // la do save mi ha creato il nuovo oggetto tramite in comando standard
        // getNewFormObjectCommand().execute();
        // devo solamente aggiornare l'area magazzino
        RigaArticolo rigaArticolo = (RigaArticolo) super.getNewEditorObject();
        rigaArticolo
                .setAreaMagazzino((AreaMagazzino) PanjeaSwingUtil.cloneObject(areaMagazzinoFullDTO.getAreaMagazzino()));

        ((RigaArticoloForm) getBackingFormPage()).setAreaMagazzinoCorrente(areaMagazzinoFullDTO.getAreaMagazzino());
        ((RigaArticoloForm) getBackingFormPage())
                .setCodicePagamentoCorrente(areaMagazzinoFullDTO.getAreaRate().getCodicePagamento());

        if (pluginManager.isPresente(PluginManager.PLUGIN_LOTTI)) {
            pane.setEnabledAt(TAB_LOTTO, true);
        }

        return rigaArticolo;
    }

    /**
     * @return Returns the openSituazioneCauzioniCommand.
     */
    public OpenSituazioneCauzioniCommand getOpenSituazioneCauzioniCommand() {
        if (openSituazioneCauzioniCommand == null) {
            openSituazioneCauzioniCommand = new OpenSituazioneCauzioniCommand();
            openSituazioneCauzioniCommand.addCommandInterceptor(getOpenSituazioneCauzioniCommandInterceptor());
        }

        return openSituazioneCauzioniCommand;
    }

    /**
     * @return Returns the openSituazioneCauzioniCommandInterceptor.
     */
    public OpenSituazioneCauzioniCommandInterceptor getOpenSituazioneCauzioniCommandInterceptor() {
        if (openSituazioneCauzioniCommandInterceptor == null) {
            openSituazioneCauzioniCommandInterceptor = new OpenSituazioneCauzioniCommandInterceptor();
        }

        return openSituazioneCauzioniCommandInterceptor;
    }

    /**
     *
     * @return PluginManager
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public void loadData() {

    }

    /**
     * @param rigaArticolo
     *            riga articolo
     */
    private void lottiPanelChangeEnabled(RigaArticolo rigaArticolo) {
        if (pluginManager.isPresente(PluginManager.PLUGIN_LOTTI)) {
            ArticoloLite articolo = rigaArticolo.getArticolo();
            boolean enableLotti = articolo != null && articolo.getId() != null
                    && articolo.getTipoLotto() != TipoLotto.NESSUNO
                    && areaMagazzinoFullDTO.getAreaMagazzino().getId() != null
                    && areaMagazzinoFullDTO.getAreaMagazzino().getDocumento().getTipoDocumento().isGestioneLotti()
                    && areaMagazzinoFullDTO.getAreaMagazzino().getTipoAreaMagazzino()
                            .getTipoMovimento() != TipoMovimento.NESSUNO;
            pane.setEnabledAt(TAB_LOTTO, enableLotti);
        }
    }

    /**
     * Quando vado in modifica recupero la politicaPrezzo per la riga-articolo. Se modifica la qta devo modificare anche
     * il prezzo unitario.
     *
     * @return lock per l'oggetto.
     */
    @Override
    public ILock onLock() {
        // Recupero su una chiamata asincrona di FoxTrot
        ((RigaArticoloForm) getForm()).showPoliticaPrezzoIndicator(true);
        AsyncWorker.post(new AsyncTask() {
            @Override
            public void failure(Throwable error) {
                logger.error("--> Errore nel calcolo della politicaPrezzo", error);
                ((RigaArticoloForm) getForm()).showPoliticaPrezzoIndicator(false);
            }

            @Override
            public Object run() throws Exception {
                ((RigaArticoloForm) getForm()).showPoliticaPrezzoIndicator(true);
                AreaMagazzino areaMagazzino = (AreaMagazzino) getForm().getFormModel().getValueModel("areaMagazzino")
                        .getValue();
                Integer idListino = null;
                Integer idListinoAlternativo = null;
                Integer idSedeEntita = null;
                Integer idTipoMezzo = null;
                Integer idAgente = null;
                BigDecimal percentualeScontoCommerciale = null;

                if (areaMagazzino.getListino() != null) {
                    idListino = areaMagazzino.getListino().getId();
                }

                if (areaMagazzino.getListinoAlternativo() != null) {
                    idListinoAlternativo = areaMagazzino.getListinoAlternativo().getId();
                }

                if (areaMagazzino.getDocumento().getSedeEntita() != null) {
                    idSedeEntita = areaMagazzino.getDocumento().getSedeEntita().getId();
                }

                if (areaMagazzino.getMezzoTrasporto() != null
                        && areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto() != null) {
                    idTipoMezzo = areaMagazzino.getMezzoTrasporto().getTipoMezzoTrasporto().getId();
                }

                AgenteLite agente = (AgenteLite) getForm().getFormModel().getValueModel("agente").getValue();
                if (agente != null) {
                    idAgente = agente.getId();
                }

                CodicePagamento codicePagamento = (CodicePagamento) getForm().getFormModel()
                        .getValueModel("codicePagamento").getValue();
                if (codicePagamento != null) {
                    percentualeScontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
                }

                ArticoloLite articolo = (ArticoloLite) getForm().getFormModel().getValueModel("articolo").getValue();
                ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(articolo.getId(),
                        areaMagazzino.getDocumento().getDataDocumento(), idListino, idListinoAlternativo, null,
                        idSedeEntita, null, null, areaMagazzino.getTipoAreaMagazzino().getProvenienzaPrezzo(),
                        idTipoMezzo, areaMagazzino.getIdZonaGeografica(), articolo.getProvenienzaPrezzoArticolo(),
                        areaMagazzino.getDocumento().getTotale().getCodiceValuta(), idAgente,
                        percentualeScontoCommerciale);
                return moduloPrezzoBD.calcola(parametriCalcoloPrezzi);
            }

            @Override
            public void success(Object politicaPrezzo) {
                logger.debug("--> Enter success nel calcolo della politicaPrezzo");
                // Può capitare che prima di recuperare la politicaPrezzo dal
                // server l'utente
                // abbia cambiato l'articolo. In questo caso avrei una
                // politicaPrezzo non più a null
                // ma con la politicaPrezzo corretta. Non devo cambiarla con
                // quella che ho calcolato altrimenti
                // la sostituirei con una politicaPrezzo dell'articolo
                // precedente.
                if (getForm().getFormModel().getValueModel("politicaPrezzo").getValue() == null) {

                    // Controllo se il form è dirty. Se non è dirty facendo la
                    // setValue
                    // sporco il form. Quindi devo fare un commit per resettarlo
                    boolean isDirty = getForm().isDirty();
                    getForm().getFormModel().getValueModel("politicaPrezzo").setValue(politicaPrezzo);
                    if (!isDirty) {
                        getForm().commit();
                    }
                    ((RigaArticoloForm) getForm()).showPoliticaPrezzoIndicator(false);
                }
                logger.debug("--> Exit success nel calcolo della politicaPrezzo");
            }
        });

        return super.onLock();
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public boolean onUndo() {
        // Disabilito temporaneamente i listener sulla modifica della qta
        // per evitare che vengano calcolate le formule di trasformazione
        // e generato un errore per valori non validi (come divisione per zero)
        // solo perché il valore viene modificato durante il revert del formModel
        DefaultController controller = getDefaultController();
        try {
            controller.disableListener("qta");
            updateCommands();
            return super.onUndo();
        } finally {
            controller.enableListener("qta");
        }
    }

    @Override
    public void postSetFormObject(Object object) {
        getDefaultController().register();
        super.postSetFormObject(object);

        updateCommands();
    }

    @Override
    public void preSetFormObject(Object object) {
        getDefaultController().unregistrer();
        super.preSetFormObject(object);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RigaArticolo rigaArticolo = (RigaArticolo) getBackingFormPage().getFormObject();
        lottiPanelChangeEnabled(rigaArticolo);
    }

    @Override
    public void refreshData() {

    }

    /**
     * @param areaMagazzinoFullDTO
     *            The areaMagazzinoFullDTO to set.
     */
    public void setAreaMagazzinoFullDTO(AreaMagazzinoFullDTO areaMagazzinoFullDTO) {
        // devo azzerare l'agente quando viene impostata la nuova area magazzino
        // per l'editor per evitare di ritrovare
        // l'agente del documento precedente
        if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)
                && !areaMagazzinoFullDTO.equals(this.areaMagazzinoFullDTO)) {
            ((RigaArticoloForm) getBackingFormPage()).getFormModel().getValueModel("agente").setValue(null);
        }

        this.areaMagazzinoFullDTO = areaMagazzinoFullDTO;
        // Potrebbe venire salvata dalla riga se cambia stato
        // quindi "preparo" l'area magazzino per un eventuale salvataggio.
        ((RigaArticoloForm) getBackingFormPage()).setAreaMagazzinoCorrente(areaMagazzinoFullDTO.getAreaMagazzino());
        ((RigaArticoloForm) getBackingFormPage())
                .setCodicePagamentoCorrente(areaMagazzinoFullDTO.getAreaRate().getCodicePagamento());

        Integer idInstallazione = null;
        if (areaMagazzinoFullDTO.getAreaRifornimento() != null) {
            BeanWrapper areaRifWrapper = new BeanWrapperImpl(areaMagazzinoFullDTO.getAreaRifornimento());
            idInstallazione = (Integer) areaRifWrapper.getPropertyValue("installazione.id");
        }
        getBackingFormPage().getValueModel("idInstallazione").setValue(idInstallazione);

    }

    /**
     * @param aziendaCorrente
     *            The aziendaCorrente to set.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    @Override
    public void setFormObject(Object object) {

        // la setFormObject mi setta rigaArticoloDTO se viene dalla table o
        // rigaArticolo se modifico o salvo una nuova
        // riga; se dto devo caricare la rigaArticolo altrimenti la imposto come
        // formObject

        RigaArticolo rigaArticolo = null;
        if (object != null) {
            if (object instanceof RigaArticoloDTO) {
                RigaArticoloDTO rigaArtDTO = (RigaArticoloDTO) object;
                rigaArticolo = (RigaArticolo) rigaArtDTO.getRigaMagazzino();
                rigaArticolo = (RigaArticolo) magazzinoDocumentoBD.caricaRigaMagazzino(rigaArticolo);
                rigaArticolo.setAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino());
                // NPE MAIL:
                if (rigaArticolo == null) {
                    logger.error("RigaArticolo caricata null; rigaDTO id: " + rigaArtDTO.getId() + ", art: "
                            + rigaArtDTO.getArticolo() + ", qta: " + rigaArtDTO.getQta() + ", area id: "
                            + areaMagazzinoFullDTO.getId());
                    // la riga articolo sembra risultare null solo quando nell'editor è presente una
                    // area che ho
                    // cancellato e ne carico un'altra o ne preparo una nuova. Praticamente viene
                    // selezionata una riga
                    // che non esiste più
                    return;
                }
                rigaArticolo.setQtaChiusa(rigaArtDTO.getQtaChiusa());
                rigaArticolo.setCodiceEntita(rigaArtDTO.getCodiceEntita());
                getEditorLockCommand().setAuthorized(!rigaArticolo.isChiusa());
                getEditorDeleteCommand().setAuthorized(!rigaArticolo.isChiusa());
            } else if (object instanceof RigaArticolo) {
                rigaArticolo = (RigaArticolo) object;
            }
            super.setFormObject(rigaArticolo);
            lottiPanelChangeEnabled(rigaArticolo);
        }
    }

    /**
     * @param magazzinoDocumentoBD
     *            The magazzinoDocumentoBD to set.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }

    /**
     *
     * @param moduloPrezzoBD
     *            moduloPrezzoBD
     */
    public void setModuloPrezzoBD(IModuloPrezzoBD moduloPrezzoBD) {
        this.moduloPrezzoBD = moduloPrezzoBD;
    }

    /**
     *
     * @param pluginManager
     *            setter for pluginManager
     */
    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        for (AbstractCommand abstractCommand : getExternalCommandStart()) {
            abstractCommand.setEnabled(readOnly);
        }
    }

    /**
     * @param righeConaiComponenteForm
     *            the righeConaiComponenteForm to set
     */
    public void setRigheConaiComponenteForm(String righeConaiComponenteForm) {
        this.righeConaiComponenteForm = righeConaiComponenteForm;
    }

    /**
     * @param righeLottiFormClass
     *            the righeLottiFormClass to set
     */
    public void setRigheLottiFormClass(String righeLottiFormClass) {
        this.righeLottiFormClass = righeLottiFormClass;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        super.stateChanged(e);
        JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
        Integer indexSelected = tabbedPane.getSelectedIndex();
        if (indexSelected > 0) {
            if (titleRigheCollegateFormTab.equals(tabbedPane.getTitleAt(indexSelected))) {
                righeCollegateForm.reloadData();
            }
        }
    }

    @Override
    public void updateCommands() {
        super.updateCommands();
        boolean inEdit = !getForm().getFormModel().isReadOnly();
        boolean isNew = ((IDefProperty) getForm().getFormModel().getFormObject()).isNew();
        toolbarPageEditor.getLockCommand().setEnabled(!inEdit & !isNew);
        toolbarPageEditor.getNewCommand().setEnabled(inEdit);
    }
}
