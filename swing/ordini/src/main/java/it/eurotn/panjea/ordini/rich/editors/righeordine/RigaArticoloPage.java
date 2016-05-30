package it.eurotn.panjea.ordini.rich.editors.righeordine;

import java.math.BigDecimal;
import java.util.Locale;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.ActionCommandInterceptor;

import com.jidesoft.swing.JideTabbedPane;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.locking.IDefProperty;
import it.eurotn.locking.ILock;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.rich.bd.IModuloPrezzoBD;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.SaveCommandInterceptor;
import it.eurotn.panjea.ordini.domain.AttributoRiga;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.rich.bd.IOrdiniDocumentoBD;
import it.eurotn.panjea.ordini.rich.forms.righeordine.GestioneConfigurazioneArticoloCommand;
import it.eurotn.panjea.ordini.rich.forms.righeordine.NoteRigaForm;
import it.eurotn.panjea.ordini.rich.forms.righeordine.RigaArticolo2Form;
import it.eurotn.panjea.ordini.rich.forms.righeordine.RigaArticoloForm;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.RigaArticoloDTO;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;
import it.eurotn.rich.editors.IPageLifecycleAdvisor;

public class RigaArticoloPage extends FormsBackedTabbedDialogPageEditor implements InitializingBean {

    private class AggiornaRigaArticoloDistintaCommandInterceptor implements ActionCommandInterceptor {

        @Override
        public void postExecution(ActionCommand paramActionCommand) {
            RigaArticolo rigaArticoloAggiornata = ((GestioneConfigurazioneArticoloCommand) paramActionCommand)
                    .getRigaArticolo();

            if (rigaArticoloAggiornata != null) {
                setFormObject(rigaArticoloAggiornata);
                // notifico la modifica della rigaArticolo alle altre pagine dell'editor
                RigaArticoloPage.this.firePropertyChange(IPageLifecycleAdvisor.OBJECT_CHANGED, null,
                        rigaArticoloAggiornata);
                RigaArticoloPage.this.firePropertyChange(RigaArticoloPage.CONFIGURATION_DISTINTA_CHANGED, null, true);
            }
        }

        @Override
        public boolean preExecution(ActionCommand paramActionCommand) {
            return true;
        }
    }

    public static final String CONFIGURATION_DISTINTA_CHANGED = "configDistintaChanged";
    private static Logger logger = Logger.getLogger(RigaArticoloPage.class);

    private static final String PAGE_ID = "rigaArticoloPage";
    private AziendaCorrente aziendaCorrente;

    private AreaOrdineFullDTO areaOrdineFullDTO;
    private String titleRigheCollegateFormTab;

    private SaveCommandInterceptor saveCommandInterceptor = null;
    private PluginManager pluginManager;
    private IOrdiniDocumentoBD ordiniDocumentoBD;

    private IModuloPrezzoBD moduloPrezzoBD;

    private RigheOrdineCollegateForm righeCollegateForm;
    private JideTabbedPane pane;

    private AggiornaRigaArticoloDistintaCommandInterceptor aggiornaRigaArticoloDistintaCommandInterceptor;

    /**
     * Costruttore.
     */
    public RigaArticoloPage() {
        super(PAGE_ID, new RigaArticoloForm());
    }

    @Override
    public void addForms() {
        RigaArticolo2Form rigaArticolo2Form = new RigaArticolo2Form(getBackingFormPage().getFormModel());
        boolean isContabilitaPresente = pluginManager.isPresente("panjeaContabilita");
        rigaArticolo2Form.setContabilitaPluginEnabled(isContabilitaPresente);
        addForm(rigaArticolo2Form);

        NoteRigaForm noteRigaForm = new NoteRigaForm(getBackingFormPage().getFormModel());
        addForm(noteRigaForm);

        righeCollegateForm = new RigheOrdineCollegateForm(getBackingFormPage().getFormModel());
        titleRigheCollegateFormTab = getMessageSource().getMessage(
                getId() + ".tab." + righeCollegateForm.getId() + ".title", new Object[] {}, Locale.getDefault());
        addForm(righeCollegateForm);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ((RigaArticoloForm) getBackingFormPage()).setAziendaCorrente(aziendaCorrente);
        ((RigaArticoloForm) getBackingFormPage()).getConfigurazioneDistintaCommand()
                .addCommandInterceptor(getAggiornaRigaArticoloDistintaCommandInterceptor());
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
        ((RigaArticoloForm) getBackingFormPage()).getConfigurazioneDistintaCommand()
                .removeCommandInterceptor(getAggiornaRigaArticoloDistintaCommandInterceptor());
        super.dispose();
    }

    @Override
    protected Object doDelete() {
        RigaOrdine rigaOrdine = (RigaOrdine) getBackingFormPage().getFormObject();
        rigaOrdine.setAreaOrdine(areaOrdineFullDTO.getAreaOrdine());
        AreaOrdine areaOrdine = ordiniDocumentoBD.cancellaRigaOrdine(rigaOrdine);

        rigaOrdine.setAreaOrdine(areaOrdine);
        return rigaOrdine;
    }

    @Override
    protected Object doSave() {
        logger.debug("--> Enter doSave");
        RigaArticolo rigaArticolo = (RigaArticolo) getBackingFormPage().getFormObject();

        for (AttributoRiga attributoRiga : rigaArticolo.getAttributi()) {
            attributoRiga.setRigaArticolo(rigaArticolo);
        }

        // risetto il codice entità che è transiente solo per visualizzarlo
        // nella tabella delle righe altrimenti andrei a perderlo finchè non
        // faccio un ricarica
        String codiceArticoloEntita = rigaArticolo.getArticolo().getCodiceEntita();
        rigaArticolo = (RigaArticolo) ordiniDocumentoBD.salvaRigaOrdine(rigaArticolo);
        rigaArticolo.getArticolo().setCodiceEntita(codiceArticoloEntita);
        logger.debug("--> Exit doSave");
        return rigaArticolo;
    }

    /**
     * @return AggiornaRigaArticoloDistintaCommandInterceptor
     */
    private AggiornaRigaArticoloDistintaCommandInterceptor getAggiornaRigaArticoloDistintaCommandInterceptor() {
        if (aggiornaRigaArticoloDistintaCommandInterceptor == null) {
            aggiornaRigaArticoloDistintaCommandInterceptor = new AggiornaRigaArticoloDistintaCommandInterceptor();
        }
        return aggiornaRigaArticoloDistintaCommandInterceptor;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        logger.debug("--> Enter getCommand");
        AbstractCommand[] abstractCommands = new AbstractCommand[] { getNewCommand(),
                toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), toolbarPageEditor.getDeleteCommand() };
        if (saveCommandInterceptor == null) {
            saveCommandInterceptor = new SaveCommandInterceptor();
            toolbarPageEditor.getSaveCommand().addCommandInterceptor(saveCommandInterceptor);
        }
        return abstractCommands;
    }

    /**
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
        if (areaOrdineFullDTO != null) {
            rigaArticolo.setAreaOrdine(areaOrdineFullDTO.getAreaOrdine());
            ((RigaArticoloForm) getBackingFormPage()).setAreaOrdineCorrente(areaOrdineFullDTO.getAreaOrdine());
            ((RigaArticoloForm) getBackingFormPage())
                    .setCodicePagamentoCorrente(areaOrdineFullDTO.getAreaRate().getCodicePagamento());
        }

        return rigaArticolo;
    }

    @Override
    public String getPageSecurityEditorId() {
        return "rigaOrdineArticoloPage";
    }

    /**
     * @return PluginManager
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    @Override
    public void loadData() {
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
            public void failure(Throwable throwable) {
                logger.error("--> Errore nel calcolo della politicaPrezzo.NV", throwable);
                ((RigaArticoloForm) getForm()).showPoliticaPrezzoIndicator(false);
            }

            @Override
            public Object run() throws Exception {
                ((RigaArticoloForm) getForm()).showPoliticaPrezzoIndicator(true);
                ArticoloLite articolo = (ArticoloLite) getForm().getFormModel().getValueModel("articolo").getValue();
                AreaOrdine areaOrdine = (AreaOrdine) getForm().getFormModel().getValueModel("areaOrdine").getValue();
                CodicePagamento codicePagamento = (CodicePagamento) getForm().getFormModel()
                        .getValueModel("codicePagamento").getValue();
                Integer idListino = null;
                Integer idListinoAlternativo = null;
                Integer idSedeEntita = null;
                Integer idTipoMezzo = null;
                Integer idAgente = null;
                BigDecimal percentualeScontoCommerciale = null;

                if (areaOrdine.getListino() != null) {
                    idListino = areaOrdine.getListino().getId();
                }

                if (areaOrdine.getListinoAlternativo() != null) {
                    idListinoAlternativo = areaOrdine.getListinoAlternativo().getId();
                }

                if (areaOrdine.getDocumento().getSedeEntita() != null) {
                    idSedeEntita = areaOrdine.getDocumento().getSedeEntita().getId();
                }
                if (areaOrdine.getAgente() != null) {
                    idAgente = areaOrdine.getAgente().getId();
                }

                if (codicePagamento != null) {
                    percentualeScontoCommerciale = codicePagamento.getPercentualeScontoCommerciale();
                }

                ParametriCalcoloPrezzi parametriCalcoloPrezzi = new ParametriCalcoloPrezzi(articolo.getId(),
                        areaOrdine.getDocumento().getDataDocumento(), idListino, idListinoAlternativo, null,
                        idSedeEntita, null, null, ProvenienzaPrezzo.LISTINO, idTipoMezzo, null,
                        articolo.getProvenienzaPrezzoArticolo(),
                        areaOrdine.getDocumento().getTotale().getCodiceValuta(), idAgente,
                        percentualeScontoCommerciale);
                return moduloPrezzoBD.calcola(parametriCalcoloPrezzi);
            }

            @Override
            public void success(Object politicaPrezzo) {
                logger.debug("--> Enter success nel calcolo della politicaPrezzo");
                // Può capitare che prima di recuperare la politicaPrezzo dal
                // server l'utente abbia cambiato l'articolo.
                // In questo caso avrei una politicaPrezzo non più a null ma con
                // la politicaPrezzo corretta. Non devo
                // cambiarla con quella che ho calcolato altrimenti la
                // sostituirei con una politicaPrezzo dell'articolo
                // precedente.
                if (getForm().getFormModel().getValueModel("politicaPrezzo").getValue() == null) {

                    // Controllo se il form è dirty. Se non è dirty facendo la
                    // setValue sporco il form. Quindi devo fare
                    // un commit per resettarlo
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
        updateCommands();
        return super.onUndo();
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
    public void refreshData() {

    }

    /**
     * @param areaOrdineFullDTO
     *            The areaOrdineFullDTO to set.
     */
    public void setAreaOrdineFullDTO(AreaOrdineFullDTO areaOrdineFullDTO) {
        this.areaOrdineFullDTO = areaOrdineFullDTO;
        // RigaArticolo rigaArticolo = (RigaArticolo)
        // getBackingFormPage().getFormObject();
        // rigaArticolo.setAreaOrdine(areaOrdineFullDTO.getAreaOrdine());
        // getBackingFormPage().getFormModel().getValueModel("areaOrdine").setValue(areaOrdineFullDTO.getAreaOrdine());
        ((RigaArticoloForm) getBackingFormPage()).setAreaOrdineCorrente(areaOrdineFullDTO.getAreaOrdine());
        ((RigaArticoloForm) getBackingFormPage())
                .setCodicePagamentoCorrente(areaOrdineFullDTO.getAreaRate().getCodicePagamento());
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
        RigaArticolo rigaArticolo = null;
        if (object != null) {
            if (object instanceof RigaArticoloDTO) {
                RigaArticoloDTO rigaArtDTO = (RigaArticoloDTO) object;
                rigaArticolo = (RigaArticolo) rigaArtDTO.getRigaOrdine();
                rigaArticolo = (RigaArticolo) ordiniDocumentoBD.caricaRigaOrdine(rigaArticolo);

                // NPE Mail
                if (rigaArticolo == null) {
                    logger.error("RigaArticolo caricata null; rigaDTO id: " + rigaArtDTO.getId() + ", art: "
                            + rigaArtDTO.getArticolo() + ", qta: " + rigaArtDTO.getQta() + ", area id: "
                            + areaOrdineFullDTO.getId());
                            // String descrizione = rigaArtDTO.getArticolo() != null
                            // && rigaArtDTO.getArticolo().getDescrizione() != null ? rigaArtDTO.getArticolo()
                            // .getDescrizione() : "";
                            // throw new GenericException("Riga articolo " + descrizione
                            // + " non più disponibile, ricaricare le righe del documento. " +
                            // areaOrdineFullDTO.getId());

                    // la riga articolo sembra risultare null solo quando nell'editor è presente una area che ho
                    // cancellato e ne carico un'altra o ne preparo una nuova. Praticamente viene selezionata una riga
                    // che non esiste più
                    return;
                }

                // tornato dal carica devo rivalorizzare la qta evasa che sulla carica
                // non viene calcolata
                rigaArticolo.setQtaEvasa(rigaArtDTO.getQtaChiusa());
            } else if (object instanceof RigaArticolo) {
                rigaArticolo = (RigaArticolo) object;
            }
            super.setFormObject(rigaArticolo);
        }
        // TRICK perchè non ho la minima idea del perchè sto c***o di form
        // diventa dirty mentre fa la set formobject
        // nota che è uguale identico al magazzino eppure questo, al contrario
        // dell'altro, si sporca per colpa del
        // prezzo netto (in realtà è il binding che formattando il valore rende
        // dirty il form) provo questo trick
        // per vedere se funziona tutto
        if (getBackingFormPage().getFormModel().isCommittable()) {
            getBackingFormPage().commit();
        }
    }

    /**
     * @param moduloPrezzoBD
     *            moduloPrezzoBD
     */
    public void setModuloPrezzoBD(IModuloPrezzoBD moduloPrezzoBD) {
        this.moduloPrezzoBD = moduloPrezzoBD;
    }

    /**
     * @param ordiniDocumentoBD
     *            the ordiniDocumentoBD to set
     */
    public void setOrdiniDocumentoBD(IOrdiniDocumentoBD ordiniDocumentoBD) {
        this.ordiniDocumentoBD = ordiniDocumentoBD;
    }

    /**
     * @param pluginManager
     *            setter for pluginManager
     */
    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);

        if (getExternalCommandStart() != null) {
            for (AbstractCommand abstractCommand : getExternalCommandStart()) {
                abstractCommand.setEnabled(readOnly);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        JTabbedPane tabbedPane = (JTabbedPane) event.getSource();
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
        // verifico se chiusa perchè isEvasa controlla anche se la riga è forzata, in caso di forzatura, devo poterla
        // modificare per poter togliere l'evasione forzata
        boolean isRigaChiusa = ((RigaArticolo) getBackingFormPage().getFormObject()).isChiusa();
        boolean inEdit = !getForm().getFormModel().isReadOnly();
        boolean isNew = ((IDefProperty) getForm().getFormModel().getFormObject()).isNew();
        toolbarPageEditor.getLockCommand().setEnabled(!isRigaChiusa && !inEdit && !isNew);
        toolbarPageEditor.getDeleteCommand().setEnabled(!isRigaChiusa);
    }
}
