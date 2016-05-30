package it.eurotn.panjea.rich.converter;

import it.eurotn.rich.converter.PanjeaConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Comparator;

import com.jidesoft.comparator.NumberComparator;
import com.jidesoft.converter.ConverterContext;

public abstract class NumberConverter extends PanjeaConverter {

    private Format customFormat = null;

    // per ottimizzare invece di crearmi ogni volta il format creo 7 istanze per i diversi decimali
    private static final Format[] DECIMAL_FORMAT = new Format[7];

    static {
        DECIMAL_FORMAT[0] = new DecimalFormat("###,###,###,##0");
        DECIMAL_FORMAT[1] = new DecimalFormat("###,###,###,##0.0");
        DECIMAL_FORMAT[2] = new DecimalFormat("###,###,###,##0.00");
        DECIMAL_FORMAT[3] = new DecimalFormat("###,###,###,##0.000");
        DECIMAL_FORMAT[4] = new DecimalFormat("###,###,###,##0.0000");
        DECIMAL_FORMAT[5] = new DecimalFormat("###,###,###,##0.00000");
        DECIMAL_FORMAT[6] = new DecimalFormat("###,###,###,##0.000000");
    }

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Comparator<Object> getComparator() {
        return NumberComparator.getInstance();
    }

    /**
     * @param customFormat
     *            the customFormat to set
     */
    public void setCustomFormat(Format customFormat) {
        this.customFormat = customFormat;
    }

    @Override
    public boolean supportFromString(String arg0, ConverterContext arg1) {
        return false;
    }

    @Override
    public boolean supportToString(Object arg0, ConverterContext arg1) {
        return true;
    }

    @Override
    public String toString(Object value, ConverterContext context) {
        StringBuilder sb = new StringBuilder();
        try {
            if (value != null) {
                int numeroDecimali = 0;
                if (context != null && context.getUserObject() != null) {
                    numeroDecimali = (Integer) context.getUserObject();
                }
                Format decimalFormat = customFormat != null ? customFormat : DECIMAL_FORMAT[numeroDecimali];
                sb.append(decimalFormat.format(value));
                if (context instanceof NumberWithDecimalConverterContext) {
                    NumberWithDecimalConverterContext contextNumber = ((NumberWithDecimalConverterContext) context);
                    String postfisso = contextNumber.getPostfisso();
                    if (postfisso != null && !postfisso.isEmpty()) {
                        sb.append(" ");
                        sb.append(postfisso);
                    }
                    if (!contextNumber.getVisualizzaZero()
                            && BigDecimal.ZERO.compareTo(new BigDecimal(value.toString())) == 0) {
                        sb = new StringBuilder();
                    }
                }
            }
        } catch (Exception e) {
            sb.append(value.toString());
        }
        return sb.toString();
    }
}
