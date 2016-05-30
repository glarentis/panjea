package it.eurotn.panjea.contabilita.rich.converter;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.rich.converter.PanjeaConverter;

public class AreaContabileFullDTOConverter extends PanjeaConverter {

    @Override
    public Object fromString(String string, ConverterContext convertercontext) {
        return null;
    }

    @Override
    public Class<?> getClasse() {
        return AreaContabileFullDTO.class;
    }

    @Override
    public boolean supportFromString(String string, ConverterContext convertercontext) {
        return false;
    }

    @Override
    public boolean supportToString(Object obj, ConverterContext convertercontext) {
        return true;
    }

    @Override
    public String toString(Object obj, ConverterContext convertercontext) {

        StringBuilder result = new StringBuilder();

        if (obj != null && obj instanceof AreaContabileFullDTO) {
            AreaContabileFullDTO area = (AreaContabileFullDTO) obj;

            result.append(area.getAreaContabile().getTipoAreaContabile().getTipoDocumento().getCodice());
            result.append(" numero ");
            result.append(area.getAreaContabile().getDocumento().getCodice().getCodice());
        }

        return result.toString();
    }
}
