package it.eurotn.panjea.manutenzioni.rich.editors.righeinstallazione;

import java.awt.Dimension;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.panjea.manutenzioni.rich.bd.IManutenzioniBD;
import it.eurotn.panjea.manutenzioni.rich.bd.ManutenzioniBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.IFormPageEditor;
import it.eurotn.rich.editors.PagesTabbedDialogPageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public class ApriInstallazioneCommand extends ActionCommand {

    private class InstallazioneDialog extends PanjeaTitledPageApplicationDialog {
        private PagesTabbedDialogPageEditor tabbedPage;
        private Installazione installazione;
        private IManutenzioniBD manutenzioniBD;

        /**
         *
         * @param installazione
         *            installazione da modificare
         */
        public InstallazioneDialog(final Installazione installazione) {
            super();
            this.installazione = installazione;
            tabbedPage = (PagesTabbedDialogPageEditor) RcpSupport.getBean("installazioniCompositePage");
            manutenzioniBD = RcpSupport.getBean(ManutenzioniBD.BEAN_ID);
            setDialogPage(tabbedPage);
            ((FormBackedDialogPageEditor) tabbedPage.getDialogPages().get(0)).getNewCommand().setVisible(false);
        }

        @Override
        protected Dimension getPreferredSize() {
            return new Dimension(1000, 450);
        }

        @Override
        protected void onAboutToShow() {
            super.onAboutToShow();
            installazione = manutenzioniBD.caricaInstallazioneById(installazione.getId());
            tabbedPage.setFormObject(installazione);
            ((FormBackedDialogPageEditor) tabbedPage.getDialogPages().get(0)).getEditorLockCommand().execute();
        }

        @Override
        protected boolean onFinish() {
            tabbedPage.onSave();
            installazione = (Installazione) ((IFormPageEditor) tabbedPage.getDialogPages().get(0)).getPageObject();
            return true;
        }
    }

    public static final String SELECT_INSTALLAZIONE_KEY = "instSelezionata";

    private Installazione inst;

    /**
     * Costruttore
     */
    public ApriInstallazioneCommand() {
        super("apriInstallazioneCommand");
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        inst = (Installazione) getParameter(SELECT_INSTALLAZIONE_KEY);
        if (inst == null) {
            return;
        }
        InstallazioneDialog dialog = new InstallazioneDialog(inst);
        dialog.showDialog();
    }

    /**
     * @return Returns the inst.
     */
    public Installazione getInst() {
        return inst;
    }

}
