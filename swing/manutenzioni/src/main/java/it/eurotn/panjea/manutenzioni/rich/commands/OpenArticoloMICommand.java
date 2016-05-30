package it.eurotn.panjea.manutenzioni.rich.commands;

import java.awt.Dimension;

import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.dialog.DialogPage;
import org.springframework.richclient.util.RcpSupport;

import it.eurotn.panjea.manutenzioni.domain.ArticoloMI;
import it.eurotn.rich.editors.IFormPageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

public abstract class OpenArticoloMICommand extends ActionCommand {

    private ArticoloMI articoloMISalvato;

    private ArticoloMI articoloMIToOpen;

    /**
     * Costruttore.
     */
    public OpenArticoloMICommand() {
        this("openArticoloMICommand");
    }

    /**
     * Costruttore.
     *
     * @param commandId
     *            id comando
     */
    public OpenArticoloMICommand(final String commandId) {
        super(commandId);
        RcpSupport.configure(this);
    }

    @Override
    protected void doExecuteCommand() {
        articoloMISalvato = null;

        PanjeaTitledPageApplicationDialog dialog = new PanjeaTitledPageApplicationDialog(getArticoloMIPage()) { // NOSONAR

            @Override
            protected Dimension getPreferredSize() {
                return new Dimension(900, 600);
            }

            @Override
            protected void onAboutToShow() {
                ((IFormPageEditor) getDialogPage()).setFormObject(articoloMIToOpen);
                ((IFormPageEditor) getDialogPage()).getEditorNewCommand().setVisible(false);
                super.onAboutToShow();
            }

            @Override
            protected void onCancel() {
                articoloMISalvato = null;
                super.onCancel();
            }

            @Override
            protected boolean onFinish() {
                IFormPageEditor articoloMIPage = (IFormPageEditor) getDialogPage();
                boolean finish = ((IFormPageEditor) getDialogPage()).isCommittable() && articoloMIPage.onSave();
                if (finish) {
                    articoloMISalvato = (ArticoloMI) articoloMIPage.getPageObject();
                }
                return finish;
            }
        };
        dialog.showDialog();
    }

    protected abstract DialogPage getArticoloMIPage();

    /**
     * @return the articoloMISalvato
     */
    public final ArticoloMI getArticoloMISalvato() {
        return articoloMISalvato;
    }

    /**
     * @param articoloMIToOpen
     *            the articoloMIToOpen to set
     */
    public final void setArticoloMIToOpen(ArticoloMI articoloMIToOpen) {
        this.articoloMIToOpen = articoloMIToOpen;
    }

}
