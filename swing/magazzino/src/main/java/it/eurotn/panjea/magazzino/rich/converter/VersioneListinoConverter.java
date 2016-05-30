package it.eurotn.panjea.magazzino.rich.converter;

import java.text.SimpleDateFormat;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.rich.converter.PanjeaConverter;

public class VersioneListinoConverter extends PanjeaConverter {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return new VersioneListino();
    }

    @Override
    public Class<?> getClasse() {
        return VersioneListino.class;
    }

    @Override
    public boolean supportFromString(String arg0, ConverterContext arg1) {
        return true;
    }

    @Override
    public boolean supportToString(Object arg0, ConverterContext arg1) {
        return true;
    }

    @Override
    public String toString(Object arg0, ConverterContext arg1) {

        StringBuilder sb = new StringBuilder();

        if (arg0 != null && arg0 instanceof VersioneListino) {
            sb.append("N. ");
            sb.append(((VersioneListino) arg0).getCodice());
            sb.append(" ");
            sb.append(RcpSupport.getMessage("dataVigore"));
            sb.append(" ");
            VersioneListino versione = (VersioneListino) arg0;
            if (versione.getDataVigore() != null) {
                sb.append(new SimpleDateFormat("dd/MM/yyyy").format(versione.getDataVigore()));
            }
        }

        return sb.toString();
    }

}
