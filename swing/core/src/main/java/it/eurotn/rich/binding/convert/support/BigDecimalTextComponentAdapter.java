package it.eurotn.rich.binding.convert.support;

import java.math.BigDecimal;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.AbstractValueModelAdapter;

import it.eurotn.rich.components.ImportoTextField;

/**
 * Adapter per legare il componente {@link ImportoTextField} ad un oggetto {@link BigDecimal}
 *
 * @author adriano
 * @version 1.0, 03/giu/08
 *
 */
public class BigDecimalTextComponentAdapter extends AbstractValueModelAdapter implements DocumentListener {

    private static final Logger LOGGER = Logger.getLogger(BigDecimalTextComponentAdapter.class);

    private ImportoTextField importoTextField;

    private boolean settingValue = false;

    private String propertyName;

    /**
     * Costruttore di {@link BigDecimalTextComponentAdapter}
     *
     * @param importoTextField
     *            componente bindato all'attributo importoInValuta
     * @param valueModel
     *            ValueModel dell'attributo importoInValuta
     * @param propertyName
     *            property name
     */
    public BigDecimalTextComponentAdapter(final ImportoTextField importoTextField, final ValueModel valueModel,
            final String propertyName) {
        super(valueModel);
        this.importoTextField = importoTextField;
        this.importoTextField.getTextField().getDocument().addDocumentListener(this);
        this.propertyName = propertyName;
    }

    @Override
    public void changedUpdate(DocumentEvent event) {
        if ((!importoTextField.isFormattingText()) && (!importoTextField.isSettingText())) {
            controlImportoValueChanged();
        }
    }

    /**
     * metodo incaricato di recuperare l'oggetto dal ValueModel e aggiornalo con il valore presente all'interno della
     * {@link ImportoTextField}
     *
     */
    protected void controlImportoValueChanged() {
        LOGGER.debug("--> Enter controlImportoValueChanged " + propertyName + " " + settingValue);
        if (!settingValue) {
            BigDecimal value = importoTextField.getValue();
            LOGGER.debug("--> set value from importoTextField " + value);
            adaptedValueChanged(value);
        }
        LOGGER.debug("--> Exit controlImportoValueChanged");
    }

    @Override
    public void insertUpdate(DocumentEvent event) {
        if ((!importoTextField.isFormattingText()) && (!importoTextField.isSettingText())) {
            controlImportoValueChanged();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent event) {
        if ((!importoTextField.isFormattingText()) && (!importoTextField.isSettingText())) {
            controlImportoValueChanged();
        }
    }

    @Override
    protected void valueModelValueChanged(Object newValue) {
        LOGGER.debug("--> Enter valueModelValueChanged");

        try {
            BigDecimal value = (BigDecimal) ObjectUtils.defaultIfNull(newValue, BigDecimal.ZERO);
            settingValue = true;
            LOGGER.debug("--> valorizzazione di importoTextField " + propertyName
                    + " con il valore dell' oggetto newValue : " + value);
            importoTextField.setValue(value);
        } finally {
            settingValue = false;
        }
        LOGGER.debug("--> Exit valueModelValueChanged");
    }

}
