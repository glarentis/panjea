package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype;

import java.util.ArrayList;
import java.util.List;

import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.AreaFatturaElettronicaPage;
import it.eurotn.panjea.rich.editors.DefaultEditor;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

public class AreaFatturaElettronicaTypeEditor extends DefaultEditor {

    /**
     * Costruttore.
     */
    public AreaFatturaElettronicaTypeEditor() {
        super();
    }

    private List<String> getIdPagesForVersion(IFatturaElettronicaType fatturaElettronicaType) {

        FormatoTrasmissioneType formatoTrasmissione = null;
        for (FormatoTrasmissioneType formato : FormatoTrasmissioneType.values()) {
            if (formato.getCodice().equals(fatturaElettronicaType.getVersione())) {
                formatoTrasmissione = formato;
                break;
            }
        }

        List<String> idPages = new ArrayList<String>();
        if (formatoTrasmissione != null) {
            switch (formatoTrasmissione) {
            case SDI_10:
                idPages.add(AreaFatturaElettronicaPage.PAGE_ID);
                break;
            case SDI_11:
                idPages.add(
                        it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1.AreaFatturaElettronicaPage.PAGE_ID);
                break;
            default:
                throw new GenericException("Versione dell'XML non gestita");
            }
        }

        return idPages;
    }

    @Override
    public void setEditorInput(Object paramEditorObject) {

        setIdPages(getIdPagesForVersion(((AreaFatturaElettronicaType) paramEditorObject).getFatturaElettronicaType()));

        super.setEditorInput(paramEditorObject);
    }
}
