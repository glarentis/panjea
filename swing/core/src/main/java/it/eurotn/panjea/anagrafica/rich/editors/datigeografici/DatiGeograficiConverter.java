package it.eurotn.panjea.anagrafica.rich.editors.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.rich.converter.PanjeaConverter;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.converter.ConverterContext;

public class DatiGeograficiConverter extends PanjeaConverter {

	public class DatiGeograficiToCapConverter extends AbstractConverter {

		@Override
		protected Object doConvert(Object obj, Class class1, ConversionContext conversioncontext) throws Exception {
			return ((DatiGeografici) obj).getCap();
		}

		@Override
		public Class[] getSourceClasses() {
			return new Class[] { DatiGeografici.class };
		}

		@Override
		public Class[] getTargetClasses() {
			return new Class[] { Cap.class };
		}

	}

	public class DatiGeograficiToLocalitaConverter extends AbstractConverter {

		@Override
		protected Object doConvert(Object obj, Class class1, ConversionContext conversioncontext) throws Exception {
			return ((DatiGeografici) obj).getLocalita();
		}

		@Override
		public Class[] getSourceClasses() {
			return new Class[] { DatiGeografici.class };
		}

		@Override
		public Class[] getTargetClasses() {
			return new Class[] { Localita.class };
		}

	}

	/**
	 * 
	 * Costruttore.
	 */
	public DatiGeograficiConverter() {
		this.addSpringConverter(new DatiGeograficiToCapConverter());
		this.addSpringConverter(new DatiGeograficiToLocalitaConverter());
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return DatiGeografici.class;
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
