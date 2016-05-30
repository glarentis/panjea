package it.eurotn.panjea.rich.converter;

import it.eurotn.rich.converter.PanjeaConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class MeseConverter extends PanjeaConverter<Short> {

    private final Calendar cal = Calendar.getInstance();
    private DateFormat defaultFormat = new SimpleDateFormat("MMM");
    public static final ConverterContext CONTEXT = new ConverterContext("MonthName");

    /**
     * Costruttore.
     */
    public MeseConverter() {
        super(false);
        // evito di registrare il converter come converter di default di spring senza context altrimenti viene
        // registrato questo per gli Short al posto di quello di default
    }

    @Override
    public Object fromString(String paramString, ConverterContext paramConverterContext) {
        return new Short(paramString);
    }

    /**
     * @param paramInt
     *            giorno da settare
     * @return calendar con il mese impostato
     */
    protected Calendar getCalendarByMonth(int paramInt) {
        cal.set(2, paramInt);
        return cal;
    }

    @Override
    public Class<Short> getClasse() {
        return short.class;
    }

    @Override
    public Comparator<Short> getComparator() {
        return new Comparator<Short>() {

            @Override
            public int compare(Short o1, Short o2) {
                return o1.compareTo(o2);
            }
        };
    }

    @Override
    public boolean supportFromString(String paramString, ConverterContext paramConverterContext) {
        return true;
    }

    @Override
    public boolean supportToString(Object paramObject, ConverterContext paramConverterContext) {
        return true;
    }

    @Override
    public String toString(Object paramObject, ConverterContext paramConverterContext) {
        if ((paramObject == null) || (!(paramObject instanceof Number))) {
            return "";
        }
        return this.defaultFormat.format(getCalendarByMonth((new Short(paramObject + "")).intValue() - 1).getTime())
                .toUpperCase();
    }

}
