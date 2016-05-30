package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;

public class RigaArticoloQtaPropertyChange implements FormModelPropertyChangeListeners {
    private static Logger logger = Logger.getLogger(RigaArticoloQtaPropertyChange.class);
    private FormModel formModel;

    private FormuleTrasformazioneRigaArticoloFormCalculator trasformazioneRigaArticoloFormCalculator;
    private String prezzoUnitarioProperty = "prezzoUnitarioReale";

    /**
     * Costruttore.
     */
    public RigaArticoloQtaPropertyChange() {
        trasformazioneRigaArticoloFormCalculator = new FormuleTrasformazioneRigaArticoloFormCalculator();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {

            if (formModel == null || formModel.isReadOnly()) {
                // se sono in sola lettura non devo fare nulla
                return;
            }

            trasformazioneRigaArticoloFormCalculator.calcola();

            IRigaArticoloDocumento riga = ((IRigaArticoloDocumento) formModel.getFormObject());
            // Risetto i value model
            Importo prezzoUnitario = (Importo) formModel.getValueModel(prezzoUnitarioProperty).getValue();

            // Se non ho cambiato articolo ed ho un pezzo unitario = 0 aggiorno
            // il prezzo unitario e le variazioni
            // Se ho degli scaglioni aggiorno anche se il prezzo unitario <>0
            if (BigDecimal.ZERO.compareTo(prezzoUnitario.getImportoInValuta()) == 0
                    || (riga.getPoliticaPrezzo() != null && riga.getPoliticaPrezzo().getScaglioni().size() > 1)) {
                if (riga.getArticolo() != null && riga.getArticolo().getId() != null
                        && riga.getPoliticaPrezzo() != null) {
                    riga.applicaPoliticaPrezzo();
                    prezzoUnitario = (Importo) formModel.getValueModel(prezzoUnitarioProperty).getValue();
                }
                formModel.getValueModel(prezzoUnitarioProperty).setValue(prezzoUnitario.clone());
                formModel.getValueModel("variazione1").setValue(formModel.getValueModel("variazione1").getValue());
                formModel.getValueModel("variazione2").setValue(formModel.getValueModel("variazione2").getValue());
                formModel.getValueModel("variazione3").setValue(formModel.getValueModel("variazione3").getValue());
                formModel.getValueModel("variazione4").setValue(formModel.getValueModel("variazione4").getValue());
            }
            formModel.getValueModel("prezzoDeterminato")
                    .setValue(formModel.getValueModel("prezzoDeterminato").getValue());
            formModel.getValueModel("numeroDecimaliPrezzo")
                    .setValue(formModel.getValueModel("numeroDecimaliPrezzo").getValue());
            formModel.getValueModel("sconto1Bloccato").setValue(formModel.getValueModel("sconto1Bloccato").getValue());
            formModel.getValueModel("percProvvigione").setValue(formModel.getValueModel("percProvvigione").getValue());

            if (formModel.hasValueModel("battute")) {
                formModel.getValueModel("battute").setValue(formModel.getValueModel("battute").getValue());
            }

            // Se la politica prezzo ha più scaglioni alla modifica della qta
            // visualizzo gli scaglioni.
            PoliticaPrezzo politicaPrezzo = (PoliticaPrezzo) formModel.getValueModel("politicaPrezzo").getValue();
            if (politicaPrezzo != null
                    && (politicaPrezzo.getPrezzi().size() > 1 || politicaPrezzo.getSconti().size() > 1)) {
                formModel.getFieldMetadata("politicaPrezzo").setEnabled(true);
            }

            ((ValidatingFormModel) formModel).validate();
        } catch (FormuleTipoAttributoException fte) {
            throw new RuntimeException(fte);
        } catch (Exception e) {
            logger.error("--> errore in propertyChange", e);
        }
    }

    @Override
    public void setFormModel(FormModel formModel) {
        this.formModel = formModel;
        trasformazioneRigaArticoloFormCalculator.setFormModel(formModel);
    }

    /**
     * @param prezzoUnitarioProperty
     *            prezzoUnitarioProperty
     */
    public void setPrezzoUnitarioProperty(String prezzoUnitarioProperty) {
        this.prezzoUnitarioProperty = prezzoUnitarioProperty;
    }

}
