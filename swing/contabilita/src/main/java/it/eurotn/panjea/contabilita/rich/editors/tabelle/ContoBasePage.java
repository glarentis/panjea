package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.forms.ContoBaseForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 * @author fattazzo
 * @version 1.0, 27/ago/07
 *
 */
public class ContoBasePage extends FormBackedDialogPageEditor {

    private static final String PAGE_ID = "contoBasePage";

    private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

    /**
     * Costruttore.
     */
    public ContoBasePage() {
        super(PAGE_ID, new ContoBaseForm(new ContoBase()));
    }

    @Override
    protected Object doDelete() {
        contabilitaAnagraficaBD.cancellaContoBase((ContoBase) getBackingFormPage().getFormObject());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        ContoBase contoBase = (ContoBase) getBackingFormPage().getFormObject();
        contoBase = contabilitaAnagraficaBD.salvaContoBase(contoBase);
        return contoBase;
    }

    @Override
    protected AbstractCommand[] getCommand() {
        return toolbarPageEditor.getDefaultCommand(true);
    }

    @Override
    public void loadData() {
        // non faccio niente
    }

    @Override
    public void onPostPageOpen() {
        // non faccio niente
    }

    @Override
    public boolean onPrePageOpen() {
        return true;
    }

    @Override
    public void refreshData() {
        // non faccio niente
    }

    /**
     * @param contabilitaAnagraficaBD
     *            contabilitaAnagraficaBD to set
     */
    public void setContabilitaAnagraficaBD(IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }
}
