package it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1;

import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.AbstractAreaFatturaElettronicaBodyPage;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1.datiGenerali.DatiBeniServiziForm;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1.datigenerali.DatiGeneraliForm;
import it.eurotn.panjea.fatturepa.rich.editors.fatturaelettronicatype.v1_1.datigenerali.DatiPagamentoForm;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaBodyType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.DatiPagamentoType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.FatturaElettronicaBodyType;
import it.gov.fatturapa.sdi.fatturapa.v1_1.FatturaElettronicaType;

public class AreaFatturaElettronicaBodyPage extends AbstractAreaFatturaElettronicaBodyPage {

    public static final String PAGE_ID = "areaFatturaElettronicaBodyPageV1_1";

    private DatiPagamentoForm datiPagamentoForm;

    /**
     * Costruttore.
     */
    public AreaFatturaElettronicaBodyPage() {
        super(PAGE_ID, new DatiGeneraliForm());
        datiPagamentoForm = new DatiPagamentoForm();
    }

    @Override
    public void addForms() {
        DatiBeniServiziForm datiBeniServiziForm = new DatiBeniServiziForm(getBackingFormPage().getFormModel());
        addForm(datiBeniServiziForm);

        addForm(datiPagamentoForm);
    }

    @Override
    protected IFatturaElettronicaBodyType getFatturaElettronicaBodyTypeToSave() {
        getBackingFormPage().commit();

        FatturaElettronicaBodyType body = (FatturaElettronicaBodyType) getBackingFormPage().getFormObject();
        body.getDatiPagamento().clear();

        DatiPagamentoType datiPagamento = (DatiPagamentoType) datiPagamentoForm.getFormObject();
        if (datiPagamento.getCondizioniPagamento() != null) {
            body.getDatiPagamento().add(datiPagamento);
        }

        return body;
    }

    @Override
    public void setFormObject(Object object) {

        FatturaElettronicaType fatturaElettronicaType = (FatturaElettronicaType) object;
        super.setFormObject(fatturaElettronicaType.getFatturaElettronicaBody().get(0));

        // per il momento gestiamo solo 1 dato pagamento
        DatiPagamentoType datiPagamento = new DatiPagamentoType();
        if (!fatturaElettronicaType.getFatturaElettronicaBody().get(0).getDatiPagamento().isEmpty()) {
            datiPagamento = fatturaElettronicaType.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0);
        }
        datiPagamentoForm.setFormObject(datiPagamento);
    }

    @Override
    public void setIdAreaMagazzino(Integer idAreaMagazzino) {
        ((DatiGeneraliForm) getForm()).setIdAreaMagazzino(idAreaMagazzino);
    }

}
