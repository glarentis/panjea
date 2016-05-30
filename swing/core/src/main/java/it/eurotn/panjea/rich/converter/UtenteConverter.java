/**
 * 
 */
package it.eurotn.panjea.rich.converter;

import it.eurotn.panjea.sicurezza.domain.Utente;
import it.eurotn.rich.converter.PanjeaConverter;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import com.jidesoft.converter.ConverterContext;

/**
 * @author fattazzo
 * 
 */
public class UtenteConverter extends PanjeaConverter<Utente> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<Utente> getClasse() {
        return Utente.class;
    }

    @Override
    public Comparator<Utente> getComparator() {
        return new Comparator<Utente>() {

            @Override
            public int compare(Utente o1, Utente o2) {
                return o1.getUserName().compareTo(o2.getUserName());
            }
        };
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

        if (value != null && value instanceof Utente) {
            Utente utente = (Utente) value;
            result = StringUtils.defaultString(utente.getUserName());
        }
        return result;
    }

}
