package it.eurotn.rich.binding.convert.support;

import java.math.BigDecimal;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.AbstractValueModelAdapter;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.rich.components.ImportoTextField;

/**
 * Adapter per legare il componente {@link ImportoTextField} ad un oggetto {@link Importo}. <br>
 * viene gestito il value model di Importo.importoInValuta e alla sua variazione attraverso l'editazione viene
 * aggiornato l'oggetto importo nel formModel
 *
 * @author adriano
 * @version 1.0, 03/giu/08
 *
 */
public class ImportoComponentAdapter extends AbstractValueModelAdapter implements DocumentListener {

    private static final Logger LOGGER = Logger.getLogger(ImportoComponentAdapter.class);

    private final ImportoTextField importoTextField;

    private final FormModel formModel;

    private Integer nrOfDecimalsAzienda = 2;

    private boolean settingValue = false;

    private final String propertyName;

    /**
     * Costruttore.
     *
     * @param importoTextField
     *            componente dell'importo
     * @param formModel
     *            formModel
     * @param valueModelImportoInValuta
     *            .
     * @param propertyName
     *            nome della proprietà bindata
     */
    public ImportoComponentAdapter(final ImportoTextField importoTextField, final FormModel formModel,
            final ValueModel valueModelImportoInValuta, final String propertyName) {
        super(valueModelImportoInValuta);
        this.formModel = formModel;
        this.importoTextField = importoTextField;
        this.importoTextField.getTextField().getDocument().addDocumentListener(this);
        this.propertyName = propertyName;
    }

    @Override
    protected void adaptedValueChanged(Object newValue) {
        super.adaptedValueChanged(newValue);
        updateImporto();
    }

    @Override
    public void changedUpdate(DocumentEvent event) {
        LOGGER.debug("--> Enter changedUpdate formattingText " + importoTextField.isFormattingText() + " settingText "
                + importoTextField.isSettingText());
        if ((!importoTextField.isFormattingText()) && (!importoTextField.isSettingText())) {
            controlImportoValueChanged();
        }
    }

    /**
     * metodo incaricato di recuperare l'oggetto dal ValueModel e aggiornalo con il valore presente all'interno della
     * {@link ImportoTextField}.
     *
     */
    protected void controlImportoValueChanged() {
        LOGGER.debug("--> Enter controlImportoValueChanged " + propertyName + " " + settingValue);
        if (!settingValue) {
            BigDecimal value = importoTextField.getValue();
            // visto che al momento l'importo non puo' essere null,se non controllo il valore viene lanciata una
            // eccezione nel calcolaImportoValutaAzienda della classe Importo
            // Togliere questo controllo quando si permettera' l'inserimento di valori null nell'importoTextField
            adaptedValueChanged(value);
        }
        LOGGER.debug("--> Exit controlImportoValueChanged");
    }

    @Override
    public void insertUpdate(DocumentEvent event) {
        LOGGER.debug("--> Enter insertUpdate formattingText " + importoTextField.isFormattingText() + " settingText "
                + importoTextField.isSettingText());
        if ((!importoTextField.isFormattingText()) && (!importoTextField.isSettingText())) {
            controlImportoValueChanged();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent event) {
        LOGGER.debug("--> Enter removeUpdate formattingText " + importoTextField.isFormattingText() + " settingText "
                + importoTextField.isSettingText());
        if ((!importoTextField.isFormattingText()) && (!importoTextField.isSettingText())) {
            controlImportoValueChanged();
        }
    }

    /**
     * @param nrOfDecimalsAzienda
     *            The nrOfDecimalsAzienda to set.
     */
    public void setNrOfDecimalsAzienda(Integer nrOfDecimalsAzienda) {
        this.nrOfDecimalsAzienda = nrOfDecimalsAzienda;
    }

    /**
     * Aggiorno il value model.
     */
    protected void updateImporto() {
        Importo importo = (Importo) formModel.getValueModel(propertyName).getValue();
        if (importo != null) {
            importo.calcolaImportoValutaAzienda(nrOfDecimalsAzienda);
            formModel.getValueModel(propertyName).setValue(importo.clone());
        }
    }

    @Override
    protected void valueModelValueChanged(Object newValue) {
        LOGGER.debug("--> Enter valueModelValueChanged");
        try {
            settingValue = true;
            importoTextField.setValue((BigDecimal) newValue);
        } finally {
            settingValue = false;
        }
        LOGGER.debug("--> Exit valueModelValueChanged");
    }
}
