package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1;

import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.AbstractAreaFatturaElettronicaBodyPage;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.datiGenerali.DatiBeniServiziForm;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.datiGenerali.DatiGeneraliForm;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaBodyType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaType;

public class AreaFatturaElettronicaBodyPage extends AbstractAreaFatturaElettronicaBodyPage {

    public static final String PAGE_ID = "areaFatturaElettronicaBodyPageV1";

    /**
     * Costruttore.
     */
    public AreaFatturaElettronicaBodyPage() {
        super(PAGE_ID, new DatiGeneraliForm());
    }

    @Override
    public void addForms() {
        DatiBeniServiziForm datiBeniServiziForm = new DatiBeniServiziForm(getBackingFormPage().getFormModel());
        addForm(datiBeniServiziForm);
    }

    @Override
    protected IFatturaElettronicaBodyType getFatturaElettronicaBodyTypeToSave() {
        getBackingFormPage().commit();

        return (IFatturaElettronicaBodyType) getBackingFormPage().getFormObject();
    }

    @Override
    public void setFormObject(Object object) {

        FatturaElettronicaType fatturaElettronicaType = (FatturaElettronicaType) object;
        super.setFormObject(fatturaElettronicaType.getFatturaElettronicaBody().get(0));
    }

    @Override
    public void setIdAreaMagazzino(Integer idAreaMagazzino) {
        // Non utilizzato
    }

}
