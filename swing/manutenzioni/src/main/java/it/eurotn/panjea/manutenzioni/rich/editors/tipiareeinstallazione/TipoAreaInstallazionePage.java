package it.eurotn.panjea.manutenzioni.rich.editors.tipiareeinstallazione;

import javax.swing.AbstractButton;

import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.form.FormGuard;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.manutenzioni.domain.documento.TipoAreaInstallazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.panjea.rich.forms.PanjeaFormGuard;
import it.eurotn.rich.dialog.ButtonCompositeDialogPage;
import it.eurotn.rich.dialog.JecCompositeDialogPage;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class TipoAreaInstallazionePage extends FormBackedDialogPageEditor {

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
            getNuovoTipoAreaInstallazioneCommand().execute();
            canOpen = true;
        }

    }

    /**
     * ActionCommand per la cancellazione di una tipoAreaDocumento.
     *
     * @author adriano
     * @version 1.0, 09/lug/08
     *
     */
    private class EliminaTipoAreaInstallazioneCommand extends ActionCommand {

        private static final String COMMAND_ID = "deleteCommand";

        /**
         * Costruttore.
         */
        public EliminaTipoAreaInstallazioneCommand() {
            super(COMMAND_ID);
            setSecurityControllerId(TipoAreaInstallazionePage.PAGE_ID + ".controller");
            RcpSupport.configure(this);
        }

        @Override
        protected void doExecuteCommand() {
            TipoAreaInstallazionePage.this.eliminaTipoAreaInstallazione();
        }

        @Override
        protected void onButtonAttached(AbstractButton button) {
            super.onButtonAttached(button);
            button.setName(PAGE_ID + "." + COMMAND_ID);
        }

    }

    private class NuovoTipoAreaInstallazioneCommand extends ActionCommand {

        private static final String COMMAND_ID = "newCommand";

        /**
         * Costruttore.
         */
        public NuovoTipoAreaInstallazioneCommand() {
            super(getPageEditorId() + "." + COMMAND_ID);
            setSecurityControllerId(getPageEditorId() + ".controller");
        }

        @Override
        protected void doExecuteCommand() {
            TipoAreaInstallazione tipoAreaInstallazione = caricaTipoAreaInstallazione(
                    TipoAreaInstallazionePage.this.tipoDocumento);
            tipoAreaInstallazione.setTipoDocumento(TipoAreaInstallazionePage.this.tipoDocumento);
            TipoAreaInstallazionePage.super.setFormObject(tipoAreaInstallazione);
        }
    }

    private static final String PAGE_ID = "tipoAreaInstallazionePage";

    private static final String TITLE_CONFIRMATION = "tipoAreaInstallazionePage.ask.new.tipoAreaInstallazione.title";

    private static final String MESSAGE_CONFIRMATION = "tipoAreaInstallazionePage.ask.new.tipoAreaInstallazione.message";

    private IManutenzioniBD manutenzioniBD = null;
    private TipoDocumento tipoDocumento;

    private ActionCommand nuovoTipoAreaInstallazioneCommand = null;
    private ActionCommand eliminaTipoAreaInstallazioneCommand = null;

    /**
     * Costruttore.
     */
    public TipoAreaInstallazionePage() {
        super(PAGE_ID, new TipoAreaInstallazioneForm());
    }

    /**
     * @param tipoDocumentoDaCaricare
     *            il tipo Documento di cui caricare il tipo area magazzino
     * @return TipoAreaMagazzino caricata o una nuova
     */
    private TipoAreaInstallazione caricaTipoAreaInstallazione(TipoDocumento tipoDocumentoDaCaricare) {
        TipoAreaInstallazione tipoAreaInstallazione = manutenzioniBD
                .caricaTipoAreaInstallazioneByTipoDocumento(tipoDocumentoDaCaricare.getId());
        return tipoAreaInstallazione;
    }

    @Override
    protected Object doSave() {
        TipoAreaInstallazione tipoAreaInstallazione = (TipoAreaInstallazione) this.getForm().getFormObject();
        tipoAreaInstallazione.setTipoDocumento(tipoDocumento);

        TipoAreaInstallazione tipoAreaInstallazioneSalvata = manutenzioniBD
                .salvaTipoAreaInstallazione(tipoAreaInstallazione);
        super.setFormObject(tipoAreaInstallazioneSalvata);
        if (tipoAreaInstallazione.isNew()) {
            firePropertyChange("pageValid", false, true);
        }
        return tipoAreaInstallazione.getTipoDocumento();
    }

    /**
     * elimina tipo area magazino.
     */
    public void eliminaTipoAreaInstallazione() {
        TipoAreaInstallazione tipoAreaInstallazione = (TipoAreaInstallazione) getForm().getFormObject();
        if (tipoAreaInstallazione.getId() != null) {
            manutenzioniBD.cancellaTipoAreaInstallazione(tipoAreaInstallazione.getId());
            firePropertyChange(JecCompositeDialogPage.SHOW_INIT_PAGE, null, null);
            firePropertyChange("pageValid", true, false);
            getNuovoTipoAreaInstallazioneCommand().execute();
        }
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return new AbstractCommand[] { toolbarPageEditor.getLockCommand(), toolbarPageEditor.getSaveCommand(),
                toolbarPageEditor.getUndoCommand(), getEliminaTipoAreaInstallazioneCommand() };
    }

    /**
     *
     * @return elimina area magazzino command.
     */
    private AbstractCommand getEliminaTipoAreaInstallazioneCommand() {
        if (eliminaTipoAreaInstallazioneCommand == null) {
            eliminaTipoAreaInstallazioneCommand = new EliminaTipoAreaInstallazioneCommand();
            new PanjeaFormGuard(getForm().getFormModel(), eliminaTipoAreaInstallazioneCommand, FormGuard.ON_NOERRORS);
        }
        return eliminaTipoAreaInstallazioneCommand;
    }

    /**
     *
     * @return command nuovo tipo area magazzino.
     */
    private ActionCommand getNuovoTipoAreaInstallazioneCommand() {
        if (nuovoTipoAreaInstallazioneCommand == null) {
            nuovoTipoAreaInstallazioneCommand = new NuovoTipoAreaInstallazioneCommand();
        }
        return nuovoTipoAreaInstallazioneCommand;
    }

    @Override
    public void loadData() {
        if ((tipoDocumento == null) || (tipoDocumento.getClasseTipoDocumento() == null)) {
            firePropertyChange("pageValid", true, false);
            firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, true, false);
            return;
        }

        if (tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree()
                .contains(TipoAreaInstallazione.class.getName())) {
            // fire property change con proprieta "pageVisible" per comunicare
            // al command della pagina corrente
            // di rendersi visibile
            firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, false, true);

            TipoAreaInstallazione tai = (TipoAreaInstallazione) getForm().getFormObject();

            if (tai.isNew()) {
                firePropertyChange("pageValid", true, false);
            } else {
                firePropertyChange("pageValid", false, true);
            }
        } else {
            firePropertyChange(ButtonCompositeDialogPage.PAGE_VISIBLE, true, false);
        }
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public boolean onPrePageOpen() {
        logger.debug("--> Enter onPrePageOpen");
        boolean canOpen = true;
        // se non c'e' un tipo documeto caricato non posso caricare il tipo area
        // contabile
        if (tipoDocumento.isNew()) {
            return false;
        }

        if (!tipoDocumento.getClasseTipoDocumentoInstance().getTipiAree()
                .contains(TipoAreaInstallazione.class.getName())) {
            return false;
        }

        TipoAreaInstallazione tai = (TipoAreaInstallazione) getForm().getFormObject();

        // Se non esiste l'area contabile chiedo se crearla
        if (tai.isNew()) {
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
    public void refreshData() {
        loadData();
    }

    @Override
    public void setFormObject(Object object) {
        TipoDocumento tipoDocumentoCorrente = (TipoDocumento) object;

        if (!tipoDocumentoCorrente.isNew() && tipoDocumentoCorrente.getClasseTipoDocumentoInstance().getTipiAree()
                .contains(TipoAreaInstallazione.class.getName())) {
            super.setFormObject(
                    manutenzioniBD.caricaTipoAreaInstallazioneByTipoDocumento(tipoDocumentoCorrente.getId()));
        }
        tipoDocumento = tipoDocumentoCorrente;
    }

    /**
     * @param manutenzioniBD
     *            the manutenzioniBD to set
     */
    public void setManutenzioniBD(IManutenzioniBD manutenzioniBD) {
        this.manutenzioniBD = manutenzioniBD;
    }

}
