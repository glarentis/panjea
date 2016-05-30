package it.eurotn.panjea.anagrafica.rich.converter;

import java.util.Comparator;

import org.hibernate.Hibernate;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

public class EntitaConverter extends PanjeaCompositeConverter<Entita> {

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    protected String getCampo1(Entita value) {
        if (!Hibernate.isInitialized(value.getAnagrafica())) {
            return "Not initialized";
        }
        return value.getAnagrafica().getDenominazione();
    }

    @Override
    protected String getCampo2(Entita value) {
        return value.getCodice() == null ? "" : value.getCodice().toString();
    }

    @Override
    public Class<Entita> getClasse() {
        return Entita.class;
    }

    @Override
    protected Comparator<Entita> getComparatorCampo1() {
        return new Comparator<Entita>() {

            @Override
            public int compare(Entita o1, Entita o2) {
                return o1.getAnagrafica().getDenominazione().compareTo(o2.getAnagrafica().getDenominazione());
            }
        };
    }

    @Override
    protected Comparator<Entita> getComparatorCampo2() {
        return new Comparator<Entita>() {

            @Override
            public int compare(Entita o1, Entita o2) {
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
