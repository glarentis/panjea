package it.eurotn.rich.binding.codice;

import org.springframework.binding.form.FormModel;
import org.springframework.rules.closure.Closure;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 *
 */
public class ApplyCodiceClosure implements Closure {

    private FormModel formModel;
    private String codiceFormPropertyPath;
    private String valoreProtocolloFormPropertyPath;

    /**
     * Costruttore.
     * 
     * @param formModel
     *            form model
     * @param codiceFormPropertyPath
     *            path del codice
     * @param valoreProtocolloFormPropertyPath
     *            path del valore protocollo
     */
    public ApplyCodiceClosure(final FormModel formModel, final String codiceFormPropertyPath,
            final String valoreProtocolloFormPropertyPath) {
        super();
        this.formModel = formModel;
        this.codiceFormPropertyPath = codiceFormPropertyPath;
        this.valoreProtocolloFormPropertyPath = valoreProtocolloFormPropertyPath;
    }

    @Override
    public Object call(Object arg0) {
        CodiceDocumentoPM codiceDocumentoPM = (CodiceDocumentoPM) arg0;

        if (codiceDocumentoPM.isProtocolloPresente()) {
            formModel.getValueModel(valoreProtocolloFormPropertyPath).setValue(codiceDocumentoPM.getValoreProtocollo());
        }
        CodiceDocumento codiceDocumento = new CodiceDocumento();
        PanjeaEJBUtil.copyProperties(codiceDocumento, codiceDocumentoPM);
        formModel.getValueModel(codiceFormPropertyPath).setValue(codiceDocumento);

        return null;
    }

}
