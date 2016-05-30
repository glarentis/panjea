package it.eurotn.panjea.exporter.typehandlers;

import org.beanio.types.NumberTypeHandler;

public abstract class NumberFormatTypeHandler extends NumberTypeHandler {

    private String decimalSeparator;

    @Override
    public String format(Object value) {
        String result = super.format(value);

        if (decimalSeparator != null && result != null) {
            result = result.replaceAll(",", decimalSeparator);
        }

        return result;
    }

    /**
     * @return Returns the decimalSeparator.
     */
    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    /**
     * @param decimalSeparator
     *            The decimalSeparator to set.
     */
    public void setDecimalSeparator(String decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
    }
}
