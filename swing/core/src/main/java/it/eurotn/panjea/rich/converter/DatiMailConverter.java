/**
 *
 */
package it.eurotn.panjea.rich.converter;

import it.eurotn.panjea.sicurezza.domain.DatiMail;
import it.eurotn.rich.converter.PanjeaConverter;

import org.apache.commons.lang3.StringUtils;

import com.jidesoft.converter.ConverterContext;

/**
 * @author fattazzo
 *
 */
public class DatiMailConverter extends PanjeaConverter<DatiMail> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<DatiMail> getClasse() {
        return DatiMail.class;
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

        String result = "";

        if (value != null && value instanceof DatiMail) {
            DatiMail datiMail = (DatiMail) value;
            result = StringUtils.defaultString(datiMail.getNomeAccount());
            if (!StringUtils.isBlank(datiMail.getEmail())) {
                result = result.concat(" (").concat(datiMail.getEmail()).concat(")");
            }
        }
        return result;
    }

}
