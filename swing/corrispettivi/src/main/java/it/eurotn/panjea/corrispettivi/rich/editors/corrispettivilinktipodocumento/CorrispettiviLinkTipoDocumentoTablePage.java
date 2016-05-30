package it.eurotn.panjea.corrispettivi.rich.editors.corrispettivilinktipodocumento;

import java.util.Collection;

import it.eurotn.panjea.corrispettivi.domain.CorrispettivoLinkTipoDocumento;
import it.eurotn.panjea.corrispettivi.rich.bd.ICorrispettiviBD;
import it.eurotn.rich.editors.AbstractTablePageEditor;

public class CorrispettiviLinkTipoDocumentoTablePage extends AbstractTablePageEditor<CorrispettivoLinkTipoDocumento> {

    public static final String PAGE_ID = "corrispettiviLinkTipoDocumentoTablePage";

    private ICorrispettiviBD corrispettiviBD = null;

    /**
     * Costruttore di default.
     */
    public CorrispettiviLinkTipoDocumentoTablePage() {
        super(PAGE_ID, new String[] { "tipoDocumentoOrigine", "tipoDocumentoDestinazione" },
                CorrispettivoLinkTipoDocumento.class);
    }

    @Override
    public Collection<CorrispettivoLinkTipoDocumento> loadTableData() {
        return corrispettiviBD.caricaCorrispettiviLinkTipoDocumento();
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
    public Collection<CorrispettivoLinkTipoDocumento> refreshTableData() {
        return loadTableData();
    }

    /**
     * @param corrispettiviBD
     *            the corrispettiviBD to set
     */
    public void setCorrispettiviBD(ICorrispettiviBD corrispettiviBD) {
        this.corrispettiviBD = corrispettiviBD;
    }

    @Override
    public void setFormObject(Object object) {
        // Non utilizzato
    }

}
