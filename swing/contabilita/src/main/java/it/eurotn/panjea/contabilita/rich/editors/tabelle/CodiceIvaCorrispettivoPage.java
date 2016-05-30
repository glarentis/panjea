package it.eurotn.panjea.contabilita.rich.editors.tabelle;

import org.apache.log4j.Logger;
import org.springframework.richclient.command.AbstractCommand;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.contabilita.domain.CodiceIvaCorrispettivo;
import it.eurotn.panjea.contabilita.rich.bd.IContabilitaAnagraficaBD;
import it.eurotn.rich.editors.FormBackedDialogPageEditor;

/**
 *
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
public class CodiceIvaCorrispettivoPage extends FormBackedDialogPageEditor {

    private static final Logger LOGGER = Logger.getLogger(CodiceIvaCorrispettivoPage.class);

    public static final String PAGE_ID = "codiceIvaCorrispettivoPage";

    private IContabilitaAnagraficaBD contabilitaAnagraficaBD;

    private TipoDocumento tipoDocumento = null;

    /**
     * Costruttore.
     */
    public CodiceIvaCorrispettivoPage() {
        super(PAGE_ID, new CodiceIvaCorrispettivoForm(new CodiceIvaCorrispettivo()));
    }

    @Override
    protected Object doDelete() {
        contabilitaAnagraficaBD
                .cancellaCodiceIvaCorrispettivo((CodiceIvaCorrispettivo) getBackingFormPage().getFormObject());
        return getBackingFormPage().getFormObject();
    }

    @Override
    protected Object doSave() {
        LOGGER.debug("--> Enter doSave");

        CodiceIvaCorrispettivo codiceIvaCorrispettivo = (CodiceIvaCorrispettivo) getBackingFormPage().getFormObject();
        codiceIvaCorrispettivo.setTipoDocumento(tipoDocumento);

        LOGGER.debug("--> Salvo il codice iva corrispettivo: " + codiceIvaCorrispettivo);
        codiceIvaCorrispettivo = contabilitaAnagraficaBD.salvaCodiceIvaCorrispettivo(codiceIvaCorrispettivo);
        getBackingFormPage().setFormObject(codiceIvaCorrispettivo);
        LOGGER.debug("--> Codice iva corrispettivo salvato: " + codiceIvaCorrispettivo);

        LOGGER.debug("--> Exit doSave");
        return codiceIvaCorrispettivo;
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

    /**
     * @param tipoDocumento
     *            tipoDocumento to set
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
