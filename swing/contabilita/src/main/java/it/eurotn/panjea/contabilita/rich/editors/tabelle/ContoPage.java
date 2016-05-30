package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.panjea.contabilita.rich.forms.ContoForm;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 *
 *
 * @author fattazzo
 * @version 1.0, 16/apr/07
 *
 */
public class ContoPage extends FormBackedDialogPageEditor {

    private static final Logger LOGGER = Logger.getLogger(ContoPage.class);

    private static final String PAGE_ID = "contoPage";

    private final IContabilitaAnagraficaBD contabilitaAnagraficaBD;

    /**
     *
     * Costruttore.
     *
     * @param conto
     *            conto
     * @param contabilitaAnagraficaBD
     *            bd
     */
    public ContoPage(final Conto conto, final IContabilitaAnagraficaBD contabilitaAnagraficaBD) {
        super(PAGE_ID, new ContoForm(conto));
        this.contabilitaAnagraficaBD = contabilitaAnagraficaBD;
    }

    @Override
    protected Object doSave() {
        Conto conto = (Conto) getBackingFormPage().getFormObject();
        LOGGER.debug("--> Salvo l'oggetto: " + conto);
        Conto contoSalvato = contabilitaAnagraficaBD.salvaConto(conto);
        setFormObject(contoSalvato);
        return contoSalvato;
    }

    /**
     * @return lock command
     */
    public AbstractCommand getLockCommand() {
        return toolbarPageEditor.getLockCommand();
    }

    /**
     * @return undo command
     */
    public AbstractCommand getUndoCommand() {
        return toolbarPageEditor.getUndoCommand();
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

}
