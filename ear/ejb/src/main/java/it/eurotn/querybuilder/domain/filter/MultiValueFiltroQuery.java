package it.eurotn.querybuilder.domain.filter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;

import it.eurotn.querybuilder.domain.ProprietaEntity;
import it.eurotn.util.PanjeaEJBUtil;

public abstract class MultiValueFiltroQuery extends FiltroQuery {

    private static final Logger LOGGER = Logger.getLogger(MultiValueFiltroQuery.class);

    public static final String SEPARATOR = "\\|";

    /**
     * Esegue il parse del valore suddividendolo in base al separatore per ottenere tutti i valori da utilizzare nella
     * query.
     *
     * @param value
     *            valore
     * @param field
     *            proprietà
     * @return elenco dei valori ottenuti
     */
    public String[] parseValues(String value, ProprietaEntity field) {
        String[] values = new String[] {};

        String[] valueSplit = value.split(SEPARATOR);

        for (String val : valueSplit) {
            if (val == null || val.isEmpty()) {
                continue;
            }
            val = val.trim();

            if (String.class.equals(field.getType())) {
                val = PanjeaEJBUtil.addQuote(val);
            } else if (Date.class.equals(field.getType())) {
                // mi aspetto la data nel formato dd/MM/yyyy e la converto in yyyy/MM/dd
                DateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date data1 = dateParser.parse(val);
                    val = PanjeaEJBUtil.addQuote(DateFormatUtils.format(data1, "yyyy/MM/dd"));
                } catch (Exception e) {
                    LOGGER.error("--> errore durante il parse della data " + val, e);
                    continue;
                }
            }

            values = ArrayUtils.add(values, val);
        }

        return values;
    }
}
