package it.eurotn.panjea.magazzino.rich.editors.rendicontazione.anagrafica;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.converter.ConverterContext;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.rich.converter.PanjeaConverter;

public class TipoAreaMagazzinoPMConverter extends PanjeaConverter<TipoAreaMagazzinoPM> {

    public class TipoAreaMagazzinoPMToTAMConverter extends AbstractConverter {

        @SuppressWarnings("rawtypes")
        @Override
        protected Object doConvert(Object obj, Class class1, ConversionContext conversioncontext) throws Exception {
            return ((TipoAreaMagazzinoPM) obj).getTipoAreaMagazzino();
        }

        @Override
        public Class<?>[] getSourceClasses() {
            return new Class[] { TipoAreaMagazzinoPM.class };
        }

        @Override
        public Class<?>[] getTargetClasses() {
            return new Class[] { TipoAreaMagazzino.class };
        }

    }

    /**
     *
     * Costruttore.
     */
    public TipoAreaMagazzinoPMConverter() {
        this.addSpringConverter(new TipoAreaMagazzinoPMToTAMConverter());
    }

    @Override
    public Object fromString(String arg0, ConverterContext arg1) {
        return null;
    }

    @Override
    public Class<TipoAreaMagazzinoPM> getClasse() {
        return TipoAreaMagazzinoPM.class;
    }

    @Override
    public boolean supportFromString(String arg0, ConverterContext arg1) {
        return false;
    }

    @Override
    public boolean supportToString(Object arg0, ConverterContext arg1) {
        return false;
    }

    @Override
    public String toString(Object arg0, ConverterContext arg1) {
        return null;
    }

}
