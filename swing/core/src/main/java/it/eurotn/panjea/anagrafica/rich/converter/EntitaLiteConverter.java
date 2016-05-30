package it.eurotn.panjea.anagrafica.rich.converter;

import java.util.Comparator;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.ClientePotenzialeLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

public class EntitaLiteConverter extends PanjeaCompositeConverter<EntitaLite>implements Comparator<EntitaLite> {

    private class EntitaLiteToEntitaConverter extends AbstractConverter {

        @Override
        protected Object doConvert(Object entitaLite, Class arg1, ConversionContext arg2) throws Exception {
            if (entitaLite == null) {
                return null;
            }
            EntitaLite entitaToConvert = (EntitaLite) entitaLite;
            return entitaToConvert.creaProxyEntita();
        }

        @Override
        public Class[] getSourceClasses() {
            return new Class<?>[] { EntitaLite.class };
        }

        @Override
        public Class[] getTargetClasses() {
            return new Class<?>[] { Entita.class };
        }

    }

    private class EntitaLiteToEntitaLiteImplConverter extends AbstractConverter {

        @SuppressWarnings("rawtypes")
        @Override
        protected Object doConvert(Object entitaLite, Class class1, ConversionContext conversioncontext)
                throws Exception {

            if (entitaLite == null) {
                return null;
            }

            EntitaLite entitaToConvert = (EntitaLite) entitaLite;
            @SuppressWarnings("unchecked")
            EntitaLite entitaLiteConvert = entitaToConvert.convertToEntitaLite(class1);
            return entitaLiteConvert;
        }

        @Override
        public Class<?>[] getSourceClasses() {
            return new Class<?>[] { EntitaLite.class };
        }

        @Override
        public Class<?>[] getTargetClasses() {
            return new Class<?>[] { AgenteLite.class, FornitoreLite.class, VettoreLite.class, ClienteLite.class,
                    ClientePotenzialeLite.class };
        }

    }

    /**
     * Costruttore.
     */
    public EntitaLiteConverter() {
        super();
        addSpringConverter(new EntitaLiteToEntitaLiteImplConverter());
        addSpringConverter(new EntitaLiteToEntitaConverter());
    }

    @Override
    public int compare(EntitaLite e1, EntitaLite e2) {
        int result = -1;
        if (e1 == null && e2 != null) {
            result = -1;
        } else if (e1 == null && e2 == null) {
            result = 0;
        } else if (e1 != null && e2 == null) {
            result = 1;
        } else if (e1 == null) {
            result = -1;
        } else {
            result = e1.getAnagrafica().getDenominazione().compareTo(e2.getAnagrafica().getDenominazione());
        }
        return result;
    }

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    protected String getCampo1(EntitaLite value) {
        return value.getAnagrafica().getDenominazione();
    }

    @Override
    protected String getCampo2(EntitaLite value) {
        return value.getCodice() == null ? "" : value.getCodice().toString();
    }

    @Override
    public Class<EntitaLite> getClasse() {
        return EntitaLite.class;
    }

    @Override
    public Comparator<EntitaLite> getComparator() {
        return this;
    }

    @Override
    protected Comparator<EntitaLite> getComparatorCampo1() {
        return new Comparator<EntitaLite>() {

            @Override
            public int compare(EntitaLite o1, EntitaLite o2) {
                return o1.getAnagrafica().getDenominazione().compareTo(o2.getAnagrafica().getDenominazione());
            }
        };
    }

    @Override
    protected Comparator<EntitaLite> getComparatorCampo2() {
        return new Comparator<EntitaLite>() {

            @Override
            public int compare(EntitaLite o1, EntitaLite o2) {
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
