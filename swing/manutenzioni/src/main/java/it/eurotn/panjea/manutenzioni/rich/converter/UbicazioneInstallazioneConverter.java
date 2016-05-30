package it.eurotn.panjea.manutenzioni.rich.converter;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.manutenzioni.domain.UbicazioneInstallazione;
import it.eurotn.rich.converter.PanjeaConverter;

public class UbicazioneInstallazioneConverter extends PanjeaConverter<UbicazioneInstallazione> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<UbicazioneInstallazione> getClasse() {
        return UbicazioneInstallazione.class;
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
        if (arg0 == null) {
            return "";
        }
        return ((UbicazioneInstallazione) arg0).getDescrizione();
    }
}