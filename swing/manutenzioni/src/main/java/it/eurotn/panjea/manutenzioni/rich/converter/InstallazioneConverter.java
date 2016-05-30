package it.eurotn.panjea.manutenzioni.rich.converter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.manutenzioni.domain.Installazione;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

public class InstallazioneConverter extends PanjeaCompositeConverter<Installazione>
        implements Comparator<Installazione> {

    @Override
    public int compare(Installazione o1, Installazione o2) {
        return InstallazioneConverter.this.toString(o1, null).compareTo(InstallazioneConverter.this.toString(o2, null));
    }

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    protected String getCampo1(Installazione value) {
        return value.getCodice();
    }

    @Override
    protected String getCampo2(Installazione value) {
        return value.getDescrizione();
    }

    @Override
    public Class<Installazione> getClasse() {
        return Installazione.class;
    }

    @Override
    protected Comparator<Installazione> getComparatorCampo1() {
        return this;
    }

    @Override
    protected Comparator<Installazione> getComparatorCampo2() {
        return this;
    }

    @Override
    public boolean supportFromString(String arg0, ConverterContext arg1) {
        return false;
    }

    @Override
    public boolean supportToString(Object arg0, ConverterContext arg1) {
        return true;
    }
}
