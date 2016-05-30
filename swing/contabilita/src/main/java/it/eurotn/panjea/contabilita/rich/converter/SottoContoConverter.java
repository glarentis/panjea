package it.eurotn.panjea.contabilita.rich.converter;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.rich.converter.PanjeaConverter;

public class SottoContoConverter extends PanjeaConverter<SottoConto> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<SottoConto> getClasse() {
        return SottoConto.class;
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
    public String toString(Object arg0, ConverterContext arg1) {
        String result = "";
        if (arg0 instanceof SottoConto) {
            if (arg1 != null) {
                result = ((SottoConto) arg0).getSottoContoCodice();
            } else {
                try {
                    result = ((SottoConto) arg0).getSottoContoCodice() + " - " + ((SottoConto) arg0).getDescrizione();
                } catch (Exception e) {
                    result = ((SottoConto) arg0).getCodice() + " - " + ((SottoConto) arg0).getDescrizione();
                }
            }
        }
        return result;
    }
}
