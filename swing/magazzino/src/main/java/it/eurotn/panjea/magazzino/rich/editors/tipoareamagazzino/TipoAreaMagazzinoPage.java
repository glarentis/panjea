package it.eurotn.panjea.magazzino.rich.editors.tipoareamagazzino;

import javax.swing.AbstractButton;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.form.FormGuard;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.rich.bd.IAnagraficaBD;
import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.documento.DatoAccompagnatorioMagazzinoMetaData;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.forms.tipoareamagazzinoform.TipoAreaMagazzinoForm;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.rich.dialog.ButtonCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * Visualizza il form per l'area magazzino nel tipo documento
 *
 * <br>
 * se la classe del documento non supporta l'Area magazzino la pagina si nasconde.<br>
 * se l'area magazzino è supportata ma non ancora creata disabilita la pagina e sulla sua apertura chiede conferma se
 * creare una nuova area magazzino. <br>
 * <b>NB</b>Il pulsante nuovo non è stato inserito perchè può esistere solamente un solo {@link TipoAreaMagazzino} per
 * {@link TipoDocumento} <br>
 *
 * @see {@link TipoAreaMagazzino}, {@link TipoDocumento}
 *
 * @author giangi
 * @version 1.0, 08/lug/08
 *
 */
public class TipoAreaMagazzinoPage extends FormBackedDialogPageEditor implements InitializingBean {

    private class CreaAreaConfirmationDialog extends ConfirmationDialog {

        private boolean canOpen;

        /**
         * Costruttore.
         *
         * @param title
         *            titolo dialogo
         * @param message
         *            messaggio del dialogo
         */
        public CreaAreaConfirmationDialog(final String title, final String message) {
            super(title, message);
        }

        /**
         *
         * @return true se posso aprire l'area magazzino.
         */
        public boolean isCanOpen() {
            return canOpen;
        }

        @Override
        protected void onConfirm() {
            getNuovoTipoAreaMagazzinoCommand().execute();
            canOpen = true;
            TipoAreaMagazzinoPage.logger
                    .debug("--> Object del form creato" + TipoAreaMagazzinoPage.this.getForm().getFormObject());
            TipoAreaMagazzinoPage.logger.debug("--> TipoDocumento " + TipoAreaMagazzinoPage.this.tipoDocumento);
        }

    }

    /**
     * ActionCommand per la cancellazione di una tipoAreaDocumento.
     *
     * @author adriano
     * @version 1.0, 09/lug/08
     *
     */
    private class EliminaTipoAreaMagazzinoCommand extends ActionCommand {

        private static final String COMMAND_ID = "deleteCommand";

        /**
         * Costruttore.
         */
        public EliminaTipoAreaMagazzinoCommand() {
            super(COMMAND_ID);
            setSecurityControllerId(TipoAreaMagazzinoPage.PAGE_ID + ".controller");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            TipoAreaMagazzinoPage.this.eliminaTipoAreaMagazzino();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(PAGE_ID + "." + COMMAND_ID);
        }

    }

    private class NuovoTipoAreaMagazzinoCommand extends ActionCommand {

        private static final String COMMAND_ID = "newCommand";

        /**
         * Costruttore.
         */
        public NuovoTipoAreaMagazzinoCommand() {
            super(getPageEditorId() + "." + COMMAND_ID);
            setSecurityControllerId(getPageEditorId() + ".controller");
        }

        @Override
        protected void doExecuteCommand() {
            TipoAreaMagazzino tipoAreaMagazzino = caricaTipoAreaMagazzino(TipoAreaMagazzinoPage.this.tipoDocumento);
            tipoAreaMagazzino.setTipoDocumento(TipoAreaMagazzinoPage.this.tipoDocumento);
            TipoAreaMagazzinoPage.super.setFormObject(tipoAreaMagazzino);
        }
    }

    private static Logger logger = Logger.getLogger(TipoAreaMagazzinoPage.class);

    private static final String PAGE_ID = "tipoAreaMagazzinoPage";

    private static final String TITLE_CONFIRMATION = "tipoAreaMagazzinoPage.ask.new.tipoAreaMagazzino.title";
    private static final String MESSAGE_CONFIRMATION = "tipoAreaMagazzinoPage.ask.new.tipoAreaMagazzino.message";
    protected TipoDocumento tipoDocumento = null;

    private IMagazzinoDocumentoBD magazzinoDocumentoBD;

    private IAnagraficaBD anagraficaBD;

    private AziendaCorrente aziendaCorrente;
    private ActionCommand nuovoTipoAreaMagazzinoCommand = null;
    private ActionCommand eliminaTipoAreaMagazzinoCommand = null;

    /**
     * Costruttore.
     */
    public TipoAreaMagazzinoPage() {
        super(PAGE_ID, new TipoAreaMagazzinoForm(new TipoAreaMagazzino()));
        logger.debug("--> Enter TipoAreaMagazzinoPage");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TipoAreaMagazzinoForm tipoAreaMagazzinoForm = (TipoAreaMagazzinoForm) getForm();
        tipoAreaMagazzinoForm.setMagazzinoDocumentoBD(magazzinoDocumentoBD);
        tipoAreaMagazzinoForm.setAziendaCorrente(aziendaCorrente);
    }

    /**
     * @param tipoDocumentoDaCaricare
     *            il tipo Documento di cui caricare il tipo area magazzino
     * @return TipoAreaMagazzino caricata o una nuova
     */
    private TipoAreaMagazzino caricaTipoAreaMagazzino(TipoDocumento tipoDocumentoDaCaricare) {
        TipoAreaMagazzino tipoAreaMagazzino = magazzinoDocumentoBD
                .caricaTipoAreaMagazzinoPerTipoDocumento(tipoDocumentoDaCaricare.getId());
        return tipoAreaMagazzino;
    }

    @Override
    protected Object doSave() {
        logger.debug("--> Enter doSave");
        TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) getForm().getFormObject();
        tipoAreaMagazzino.setTipoDocumento(tipoDocumento);

        for (DatoAccompagnatorioMagazzinoMetaData datoAccompagnatorio : tipoAreaMagazzino
                .getDatiAccompagnatoriMetaData()) {
            datoAccompagnatorio.setTipoAreaMagazzino(tipoAreaMagazzino);
        }

        TipoAreaMagazzino tipoAreaMagazzinoSalvata = magazzinoDocumentoBD.salvaTipoAreaMagazzino(tipoAreaMagazzino);
        super.setFormObject(tipoAreaMagazzinoSalvata);
        if (tipoAreaMagazzino.isNew()) {
            firePropertyChange("pageValid", false, true);
        }
        logger.debug("--> Exit doSave");
        return tipoAreaMagazzino.getTipoDocumento();
    }

    /**
     * elimina tipo area magazino.
     */
    public void eliminaTipoAreaMagazzino() {
        TipoAreaMagazzino tipoAreaMagazzino = (TipoAreaMagazzino) getForm().getFormObject();
        if (tipoAreaMagazzino.getId() != null) {
            magazzinoDocumentoBD.cancellaTipoAreaMagazzino(tipoAreaMagazzino);
            firePropertyChange(JecCompositeDialogPage.SHOW_INIT_PAGE, null, null);
            firePropertyChange("pageValid", true, false);
            getNuovoTipoAreaMagazzinoCommand().execute();
        }
    }

    /**
     *
     * @return anagrafica db.
     */
    public IAnagraficaBD getAnagraficaBD() {
        return anagraficaBD;
    }

    /**
     *
     * @return azienda corrente.
     */
    public AziendaCorrente getAziendaCorrente() {
        return aziendaCorrente;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        AbstractCommand[] abstractCommands = new AbstractCommand[] { toolbarPageEditor.getLockCommand(),
                toolbarPageEditor.getSaveCommand(), toolbarPageEditor.getUndoCommand(),
                getEliminaTipoAreaMagazzinoCommand() };
        return abstractCommands;
    }

    @Override
    public AbstractCommand getEditorNewCommand() {
        return null;
    }

    /**
     *
     * @return elimina area magazzino command.
     */
    private AbstractCommand getEliminaTipoAreaMagazzinoCommand() {
        if (eliminaTipoAreaMagazzinoCommand == null) {
            eliminaTipoAreaMagazzinoCommand = new EliminaTipoAreaMagazzinoCommand();
            new PanjeaFormGuard(getForm().getFormModel(), eliminaTipoAreaMagazzinoCommand, FormGuard.ON_NOERRORS);
        }
        return eliminaTipoAreaMagazzinoCommand;
    }

    /**
     * restituice il BD documenti magazzino.
     *
     * @return IMagazzinoDocumentoBD.
     */
    public IMagazzinoDocumentoBD getMagazzinoDocumentoBD() {
        return magazzinoDocumentoBD;
    }

    /**
     *
     * @return command nuovo tipo area magazzino.
     */
    private ActionCommand getNuovoTipoAreaMagazzinoCommand() {
        if (nuovoTipoAreaMagazzinoCommand == null) {
            nuovoTipoAreaMagazzinoCommand = new NuovoTipoAreaMagazzinoCommand();
        }
        return nuovoTipoAreaMagazzinoCommand;
    }

    @Override
    public void loadData() {
        logger.debug("--> Enter loadData");
        if ((tipoDocumento == null) || (tipoDocumento.getClasseTipoDocumento() == null)) {
            firePropertyChange("pageValid", true, false);
            firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, true, false);
            return;
        }

        if (tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree().contains(TipoAreaMagazzino.class.getName())) {
            // fire property change con proprieta "pageVisible" per comunicare
            // al command della pagina corrente
            // di rendersi visibile
            firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, false, true);

            TipoAreaMagazzino tam = (TipoAreaMagazzino) getForm().getFormObject();

            if (tam.isNew()) {
                firePropertyChange("pageValid", true, false);
            } else {
                firePropertyChange("pageValid", false, true);
            }
        } else {
            firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, true, false);
        }
        logger.debug("--> Exit loadData");
    }

    @Override
    public void onPostPageOpen() {
    }

    @Override
    public boolean onPrePageOpen() {
        logger.debug("--> Enter onPrePageOpen");
        boolean canOpen = true;
        Integer idTipoDoc = tipoDocumento.getId();
        // se non c'e' un tipo documet caricato non posso caricare il tipo area
        // contabile
        if (idTipoDoc == null) {
            return false;
        }

        if (!tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree().contains(TipoAreaMagazzino.class.getName())) {
            return false;
        }

        TipoAreaMagazzino tam = (TipoAreaMagazzino) getForm().getFormObject();

        // Se non esiste l'area contabile chiedo se crearla
        if (tam.isNew()) {
            if (!PanjeaSwingUtil.hasPermission("modificaTipoAreaMagazzino")) {
                throw new java.lang.SecurityException();
            }
            String title = getMessage(TITLE_CONFIRMATION);
            String message = getMessage(MESSAGE_CONFIRMATION);
            CreaAreaConfirmationDialog confirmationDialog = new CreaAreaConfirmationDialog(title, message);
            confirmationDialog.showDialog();
            canOpen = confirmationDialog.isCanOpen();
        }
        logger.debug("--> Exit onPrePageOpen con risultato " + canOpen);
        return canOpen;
    }

    @Override
    public boolean onUndo() {
        boolean undo = super.onUndo();

        // HACK usato solamente perchè il table binding ( in questo caso quello sui layout di stampa ) non gestisce il
        // ripristino dei dati sul revert del form se si occupa anche della loro modifica ( non se gestisce solo
        // inserimento e cancellazione )
        TipoDocumento tipoDocumentoCorrente = tipoDocumento;
        tipoDocumento = null;
        setFormObject(tipoDocumentoCorrente);
        return undo;
    }

    @Override
    public void publishCreateEvent(Object obj) {
    }

    @Override
    public void refreshData() {
        logger.debug("--> Enter refreshData");
        loadData();
        logger.debug("--> Exit refreshData");
    }

    /**
     *
     * @param anagraficaBD
     *            .
     */
    public void setAnagraficaBD(IAnagraficaBD anagraficaBD) {
        this.anagraficaBD = anagraficaBD;
    }

    /**
     *
     * @param aziendaCorrente
     *            azinda da settare.
     */
    public void setAziendaCorrente(AziendaCorrente aziendaCorrente) {
        this.aziendaCorrente = aziendaCorrente;
    }

    @Override
    public void setFormObject(Object object) {
        logger.debug("--> Enter setFormObject");
        TipoDocumento tipoDocumentoCorrente = (TipoDocumento) object;

        if (tipoDocumentoCorrente.getId() != null && tipoDocumentoCorrente.getClasseTipoDocumentoInstance()
                .getTipiAree().contains(TipoAreaMagazzino.class.getName())) {
            // la carico solamente se è abilitata
            TipoAreaMagazzino tipoAreaMagazzinoCorrente = caricaTipoAreaMagazzino(tipoDocumentoCorrente);
            super.setFormObject(tipoAreaMagazzinoCorrente);
        }

        this.tipoDocumento = tipoDocumentoCorrente;
        logger.debug("--> Exit setFormObject");
    }

    /**
     *
     * @param magazzinoDocumentoBD
     *            da settare.
     */
    public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
        this.magazzinoDocumentoBD = magazzinoDocumentoBD;
    }
}
