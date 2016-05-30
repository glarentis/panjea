package it.eurotn.panjea.corrispettivi.rich.editors.corrispettivilinktipodocumento;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.corrispettivi.domain.CorrispettivoLinkTipoDocumento;
import it.eurotn.panjea.corrispettivi.rich.bd.ICorrispettiviBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

public class CorrispettivoLinkTipoDocumentoPage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "corrispettivoLinkTipoDocumentoPage";

    private ICorrispettiviBD corrispettiviBD = null;

    /**
     * Costruttore.
     */
    public CorrispettivoLinkTipoDocumentoPage() {
        super(PAGE_ID, new CorrispettivoLinkTipoDocumentoForm());
    }

    @Override
    protected Object doDelete() {
        corrispettiviBD.cancellaCorrispettivoLinkTipoDocumento(((CorrispettivoLinkTipoDocumento) getBackingFormPage().getFormObject()).getId());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        CorrispettivoLinkTipoDocumento corrispettivoLinkTipoDocumento = (CorrispettivoLinkTipoDocumento) this.getForm().getFormObject();
        return corrispettiviBD.salvaCorrispettivoLinkTipoDocumento(corrispettivoLinkTipoDocumento);
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return toolbarPageEditor.getDefaultCommand(true);
    }

    @Override
    public void loadData() {
        // Non utilizzato
    }

    @Override
    public void onPostPageOpen() {
        // Non utilizzato
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {
        // Non utilizzato
    }

    /**
     * @param corrispettiviBD
     *            the corrispettiviBD to set
     */
    public void setCorrispettiviBD(ICorrispettiviBD corrispettiviBD) {
        this.corrispettiviBD = corrispettiviBD;
    }

}
