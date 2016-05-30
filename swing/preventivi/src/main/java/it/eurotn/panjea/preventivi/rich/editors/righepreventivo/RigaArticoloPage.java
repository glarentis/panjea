package it.eurotn.panjea.preventivi.rich.editors.righepreventivo;

import java.util.Locale;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;

import com.jidesoft.swing.JideTabbedPane;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.rich.bd.IModuloPrezzoBD;
import it.eurotn.panjea.magazzino.rich.editors.righemagazzino.SaveCommandInterceptor;
import it.eurotn.panjea.preventivi.domain.AttributoRiga;
import it.eurotn.panjea.preventivi.domain.RigaArticolo;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo.StatoAreaPreventivo;
import it.eurotn.panjea.preventivi.rich.bd.IPreventiviBD;
import it.eurotn.panjea.preventivi.rich.forms.righepreventivo.NoteRigaPreventivoForm;
import it.eurotn.panjea.preventivi.rich.forms.righepreventivo.RigaArticoloForm;
import it.eurotn.panjea.preventivi.rich.forms.righepreventivo.RighePreventivoCollegateForm;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.panjea.preventivi.util.RigaArticoloDTO;
import it.eurotn.rich.editors.FormsBackedTabbedDialogPageEditor;

public class RigaArticoloPage extends FormsBackedTabbedDialogPageEditor implements InitializingBean {

    private static Logger logger = Logger.getLogger(RigaArticoloPage.class);
    private static final String PAGE_ID = "rigaArticoloPage";
    private AziendaCorrente aziendaCorrente;

    private JideTabbedPane pane;

    private AreaPreventivoFullDTO areaPreventivoFullDTO;
    private IPreventiviBD preventiviBD;
    private IModuloPrezzoBD moduloPrezzoBD;
    private SaveCommandInterceptor saveCommandInterceptor;

    private RighePreventivoCollegateForm righeCollegateForm;
    private String titleRigheCollegateFormTab;

    /**
     * costruttore.
     */
    public RigaArticoloPage() {
        super(PAGE_ID, new RigaArticoloForm());
    }

    @Override
    public void addForms() {
        NoteRigaPreventivoForm noteRigaForm = new NoteRigaPreventivoForm(getBackingFormPage().getFormModel());
        addForm(noteRigaForm);

        righeCollegateForm = new RighePreventivoCollegateForm(getBackingFormPage().getFormModel());
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
    protected Object doDelete() {
        RigaPreventivo rigaPreventivo = (RigaPreventivo) getBackingFormPage().getFormObject();
        rigaPreventivo.setAreaPreventivo((areaPreventivoFullDTO.getAreaPreventivo()));
        AreaPreventivo areaPreventivo = preventiviBD.cancellaRigaPreventivo(rigaPreventivo);
        rigaPreventivo.setAreaPreventivo(areaPreventivo);
        return rigaPreventivo;
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
        rigaArticolo = (RigaArticolo) preventiviBD.salvaRigaPreventivo(rigaArticolo, true);
        rigaArticolo.getArticolo().setCodiceEntita(codiceArticoloEntita);
        logger.debug("--> Exit doSave");
        return rigaArticolo;
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
     * @return the moduloPrezzoBD
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
        if (areaPreventivoFullDTO != null) {
            rigaArticolo.setAreaPreventivo(areaPreventivoFullDTO.getAreaPreventivo());
            updateRigaArticoloForm();
        }
        return rigaArticolo;
    }

    @Override
    public String getPageSecurityEditorId() {
        return "rigaPreventivoArticoloPage";
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
     * @param areaPreventivoFullDTO
     *            the areaPreventivoFullDTO to set
     */
    public void setAreaPreventivoFullDTO(AreaPreventivoFullDTO areaPreventivoFullDTO) {
        this.areaPreventivoFullDTO = areaPreventivoFullDTO;
        updateRigaArticoloForm();
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
                rigaArticolo = (RigaArticolo) rigaArtDTO.getRigaPreventivo();
                rigaArticolo = (RigaArticolo) preventiviBD.caricaRigaPreventivo(rigaArticolo);

                if (rigaArticolo == null) {
                    logger.error("RigaArticolo caricata null; rigaDTO id: " + rigaArtDTO.getId() + ", art: "
                            + rigaArtDTO.getArticolo() + ", qta: " + rigaArtDTO.getQta() + ", area id: "
                            + areaPreventivoFullDTO.getId());
                    return;
                }

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
     *            the moduloPrezzoBD to set
     */
    public void setModuloPrezzoBD(IModuloPrezzoBD moduloPrezzoBD) {
        this.moduloPrezzoBD = moduloPrezzoBD;
    }

    /**
     * @param preventiviBD
     *            the preventiviBD to set
     */
    public void setPreventiviBD(IPreventiviBD preventiviBD) {
        this.preventiviBD = preventiviBD;
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
    public void stateChanged(ChangeEvent e) {
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

        // se il preventivo è accettato non posso più modificare le righe
        boolean enable = areaPreventivoFullDTO != null && (areaPreventivoFullDTO.getAreaDocumento()
                .getStatoAreaPreventivo() == StatoAreaPreventivo.PROVVISORIO
                || areaPreventivoFullDTO.getAreaDocumento().getStatoAreaPreventivo() == StatoAreaPreventivo.IN_ATTESA);

        toolbarPageEditor.getLockCommand().setEnabled(enable);
        toolbarPageEditor.getDeleteCommand().setEnabled(enable);
    }

    /**
     * ..
     */
    private void updateRigaArticoloForm() {
        RigaArticoloForm form = (RigaArticoloForm) getBackingFormPage();
        form.setAreaPreventivoCorrente(areaPreventivoFullDTO.getAreaPreventivo());
        form.setCodicePagamentoCorrente(areaPreventivoFullDTO.getAreaRate().getCodicePagamento());
    }
}
