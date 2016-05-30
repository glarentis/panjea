package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import java.awt.Dimension;

import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;
import it.eurotn.rich.editors.PanjeaTitledPageApplicationDialog;

/**
 *
 *
 * @author fattazzo
 * @version 1.0, 16/apr/07
 *
 */
public class ContoTitledPageApplicationDialog extends PanjeaTitledPageApplicationDialog {

    private Conto contoSalvato;

    /**
     *
     * Costruttore.
     *
     * @param conto
     *            conto
     * @param contabilitaAnagraficaBD
     *            bd
     */
    public ContoTitledPageApplicationDialog(final Conto conto, final IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        super(new ContoPage(conto, contabilitaAnagraficaBD));
        contoSalvato = (Conto) ((FormBackedDialogPageEditor) this.getDialogPage()).getForm().getFormObject();
        setPreferredSize(new Dimension(600, 250));
    }

    /**
     * @return conto salvato
     */
    public Conto getContoSalvato() {
        return contoSalvato;
    }

    @Override
    protected String getTitle() {
        return getMessage("contoTitledPageApplicationDialog.title");
    }

    @Override
    protected void onAboutToShow() {
        ContoPage contoPage = (ContoPage) this.getDialogPage();
        contoPage.getLockCommand().execute();
    }

    @Override
    protected void onCancel() {
        ContoPage contoPage = (ContoPage) this.getDialogPage();
        contoPage.getUndoCommand().execute();
        contoSalvato = (Conto) contoPage.getForm().getFormObject();
        super.onCancel();
    }

    @Override
    protected boolean onFinish() {
        ContoPage contoPage = (ContoPage) this.getDialogPage();
        if (contoPage.getForm().isDirty()) {
            contoPage.onSave();
        }
        contoSalvato = (Conto) contoPage.getForm().getFormObject();
        return true;
    }

}
