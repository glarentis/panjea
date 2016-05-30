package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1;

import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.AbstractAreaFatturaElettronicaPage;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaBodyType;
import it.gov.fatturapa.sdi.fatturapa.v1.FatturaElettronicaType;

public class AreaFatturaElettronicaPage extends AbstractAreaFatturaElettronicaPage {

    public static final String PAGE_ID = "areaFatturaElettronicaPageV1";

    /**
     * Costruttore.
     */
    public AreaFatturaElettronicaPage() {
        super(PAGE_ID, new AreaFatturaElettronicaForm(new FatturaElettronicaType()),
                new AreaFatturaElettronicaBodyPage());
    }

    @Override
    protected IFatturaElettronicaType getFatturaElettronicaTypeToSave() {

        getBackingFormPage().commit();

        FatturaElettronicaType fatturaElettronicaType = (FatturaElettronicaType) getBackingFormPage().getFormObject();

        // recupero il body dalla sua pagina
        bodyPage.getBackingFormPage().commit();
        FatturaElettronicaBodyType bodyType = (FatturaElettronicaBodyType) bodyPage.getBackingFormPage()
                .getFormObject();

        fatturaElettronicaType.getFatturaElettronicaBody().clear();
        fatturaElettronicaType.getFatturaElettronicaBody().add(bodyType);

        return fatturaElettronicaType;
    }

}
