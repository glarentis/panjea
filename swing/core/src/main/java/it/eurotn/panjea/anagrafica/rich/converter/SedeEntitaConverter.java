package it.eurotn.panjea.anagrafica.rich.converter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

public class SedeEntitaConverter extends PanjeaCompositeConverter<SedeEntita> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    protected String getCampo1(SedeEntita sedeEntita) {
        return sedeEntita.getSede().getDescrizioneEstesa();
    }

    @Override
    protected String getCampo2(SedeEntita sedeEntita) {
        if (sedeEntita.isNew()) {
            return "";
        }

        return sedeEntita.getCodice();
    }

    @Override
    public Class<SedeEntita> getClasse() {
        return SedeEntita.class;
    }

    @Override
    protected Comparator<SedeEntita> getComparatorCampo1() {
        return new Comparator<SedeEntita>() {

            @Override
            public int compare(SedeEntita o1, SedeEntita o2) {
                return SedeEntitaConverter.this.getCampo1(o1).compareTo(SedeEntitaConverter.this.getCampo1(o1));
            }
        };
    }

    @Override
    protected Comparator<SedeEntita> getComparatorCampo2() {
        return new Comparator<SedeEntita>() {

            @Override
            public int compare(SedeEntita o1, SedeEntita o2) {
                return o1.getCodice().compareTo(o2.getCodice());
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
}
