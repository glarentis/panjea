package it.eurotn.panjea.exporter.typehandlers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.apache.log4j.Logger;

public class DoubleFormaTypeHandler extends NumberFormatTypeHandler {

    private static final Logger LOGGER = Logger.getLogger(DoubleFormaTypeHandler.class);

    @Override
    protected Number createNumber(BigDecimal bigdecimal) {
        return bigdecimal;
    }

    @Override
    protected Number createNumber(String valuteToParse) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(getDecimalSeparator().charAt(0));
        DecimalFormat df = new DecimalFormat("###.#", symbols);
        try {
            return df.parse(valuteToParse);
        } catch (ParseException e) {
            LOGGER.error("-->errore nel fare il parse del double " + valuteToParse, e);
            throw new RuntimeException("-->errore nel fare il parse del double " + valuteToParse, e);
        }
    }

    @Override
    public Class<?> getType() {
        return Double.class;
    }

}
