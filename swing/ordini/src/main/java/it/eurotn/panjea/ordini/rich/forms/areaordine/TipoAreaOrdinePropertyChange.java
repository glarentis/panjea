package it.eurotn.panjea.ordini.rich.forms.areaordine;

import java.beans.PropertyChangeEvent;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.rich.form.FormModelPropertyChangeListeners;
import it.eurotn.rich.form.PanjeaFormModel;

public class TipoAreaOrdinePropertyChange implements FormModelPropertyChangeListeners {

    private static Logger logger = Logger.getLogger(TipoAreaOrdinePropertyChange.class);

    private FormModel formModel;

    /**
     * Assegna deposito come depositoOrigine; non viene assegnato se il depositoOrigine e'
     * valorizzato.
     *
     * @param deposito
     *            il deposito da settare al value model
     */
    private void assegnaDepositoOrigine(DepositoLite deposito) {
        if (formModel.isReadOnly()) {
            return;
        }
        DepositoLite depositoOrigine = (DepositoLite) formModel
                .getValueModel("areaOrdine.tipoAreaOrdine.depositoOrigine").getValue();
        if ((depositoOrigine != null) && (depositoOrigine.getId() != null)) {
            formModel.getValueModel("areaOrdine.depositoOrigine").setValueSilently(deposito, this);
        }
    }

    /**
     * metodo che esegue la copia del valore di dataRegistrazione in dataDocumento.
     */
    private void copyDataRegistrazioneToDataDocumento() {
        if (formModel.isReadOnly()) {
            return;
        }

        Date dataRegistrazione = (Date) formModel.getValueModel("areaOrdine.dataRegistrazione").getValue();

        formModel.getValueModel("areaOrdine.documento.dataDocumento").setValueSilently(dataRegistrazione, this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.debug("--> Enter propertyChange");
        if (evt.getNewValue() == null || ((PanjeaFormModel) formModel).isAdjustingMode()) {
            // null value non esegue nulla
            logger.debug("--> Exit propertyChange newValue == null ");
            return;
        }
        ((ValidatingFormModel) formModel).validate();
        TipoAreaOrdine tipoAreaOrdine = (TipoAreaOrdine) formModel.getValueModel("areaOrdine.tipoAreaOrdine")
                .getValue();

        if (!formModel.isReadOnly()) {
            formModel.getValueModel("areaOrdine.documento.entita").setValue(null);
        }

        // controllo di entita'
        if (TipoEntita.CLIENTE.equals(tipoAreaOrdine.getTipoDocumento().getTipoEntita())
                || (TipoEntita.FORNITORE.equals(tipoAreaOrdine.getTipoDocumento().getTipoEntita()))) {
            logger.debug("--> abilita' entita'");
            formModel.getFieldMetadata("areaOrdine.documento.entita").setEnabled(true);
        } else {
            logger.debug("--> disabilita entita'");
            // devo annullare una eventuale entita' impostata nel formModel
            formModel.getFieldMetadata("areaOrdine.documento.entita").setEnabled(false);
        }

        // Disabilito i controlli per i dati riferimento ordine
        boolean enableRiferimentoOrdine = TipoEntita.FORNITORE.equals(tipoAreaOrdine.getTipoDocumento().getTipoEntita())
                || TipoEntita.CLIENTE.equals(tipoAreaOrdine.getTipoDocumento().getTipoEntita());
        formModel.getFieldMetadata("areaOrdine.riferimentiOrdine.dataOrdine").setEnabled(enableRiferimentoOrdine);
        formModel.getFieldMetadata("areaOrdine.riferimentiOrdine.numeroOrdine").setEnabled(enableRiferimentoOrdine);
        formModel.getFieldMetadata("areaOrdine.riferimentiOrdine.modalitaRicezione")
                .setEnabled(enableRiferimentoOrdine);

        // controllo di deposito origine assegnato
        formModel.getFieldMetadata("areaOrdine.depositoOrigine").setReadOnly(false);

        if (tipoAreaOrdine.getDepositoOrigine() != null && (tipoAreaOrdine.getDepositoOrigine().getId() != null)) {

            logger.debug("--> assegna deposito origine " + tipoAreaOrdine.getDepositoOrigine());
            assegnaDepositoOrigine(tipoAreaOrdine.getDepositoOrigine());

            if (tipoAreaOrdine.isDepositoOrigineBloccato()) {
                logger.debug("--> disable componente deposito origine ");
                formModel.getFieldMetadata("areaOrdine.depositoOrigine").setReadOnly(true);
            }
        } else {
            formModel.getFieldMetadata("areaOrdine.depositoOrigine").setEnabled(true);
        }

        // copia la data registrazione in data documento
        if (tipoAreaOrdine.isDataDocLikeDataReg()) {
            logger.debug("--> copia data registrazione su data documento ");
            copyDataRegistrazioneToDataDocumento();
        }

        logger.debug("--> Exit propertyChange");
    }

    @Override
    public void setFormModel(FormModel formModel) {
        this.formModel = formModel;
    }

}