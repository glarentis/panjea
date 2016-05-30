package it.eurotn.panjea.vending.rich.converter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.vending.domain.TipoComunicazione;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

public class TipoComunicazioneConverter extends PanjeaCompositeConverter<TipoComunicazione> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    protected String getCampo1(TipoComunicazione value) {
        return value.getDescrizione();
    }

    @Override
    protected String getCampo2(TipoComunicazione value) {
        return value.getCodice();
    }

    @Override
    public Class<TipoComunicazione> getClasse() {
        return TipoComunicazione.class;
    }

    @Override
    protected Comparator<TipoComunicazione> getComparatorCampo1() {
        return new Comparator<TipoComunicazione>() {

            @Override
            public int compare(TipoComunicazione o1, TipoComunicazione o2) {
                return o1.getDescrizione().compareTo(o2.getDescrizione());
            }
        };
    }

    @Override
    protected Comparator<TipoComunicazione> getComparatorCampo2() {
        return new Comparator<TipoComunicazione>() {

            @Override
            public int compare(TipoComunicazione o1, TipoComunicazione o2) {
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
