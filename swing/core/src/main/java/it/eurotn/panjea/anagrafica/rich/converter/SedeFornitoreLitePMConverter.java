package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.forms.SedeFornitoreLitePM;
import it.eurotn.rich.converter.PanjeaConverter;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.converter.ConverterContext;

public class SedeFornitoreLitePMConverter extends PanjeaConverter {

	private class ObjectToClienteLiteConverter extends AbstractConverter {

		@SuppressWarnings("rawtypes")
		@Override
		protected Object doConvert(Object arg0, Class arg1, ConversionContext arg2) throws Exception {

			SedeFornitoreLitePM sedeFornitoreLitePM = (SedeFornitoreLitePM) arg0;
			SedeEntita sedeEntita = sedeFornitoreLitePM.getSedeEntita();

			return sedeEntita;
		}

		@Override
		public Class<?>[] getSourceClasses() {
			return new Class[] { SedeFornitoreLitePM.class };
		}

		@Override
		public Class<?>[] getTargetClasses() {
			return new Class[] { SedeEntita.class };
		}
	}

	/**
	 * Costruttore.
	 *
	 */
	public SedeFornitoreLitePMConverter() {
		super();

		addSpringConverter(new ObjectToClienteLiteConverter());
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return SedeFornitoreLitePM.class;
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
