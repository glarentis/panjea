package it.eurotn.panjea.rate.rich.converter;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.rate.rich.forms.calendarirate.ClienteLitePM;
import it.eurotn.rich.converter.PanjeaConverter;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.converter.ConverterContext;

public class ClienteLitePMConverter extends PanjeaConverter {

	private class ObjectToClienteLiteConverter extends AbstractConverter {

		@SuppressWarnings("rawtypes")
		@Override
		protected Object doConvert(Object arg0, Class arg1, ConversionContext arg2) throws Exception {

			ClienteLitePM clienteLitePM = (ClienteLitePM) arg0;
			ClienteLite clienteLite = clienteLitePM.getClienti();

			return clienteLite;
		}

		@Override
		public Class<?>[] getSourceClasses() {
			return new Class[] { ClienteLitePM.class };
		}

		@Override
		public Class<?>[] getTargetClasses() {
			return new Class[] { ClienteLite.class };
		}
	}

	/**
	 * Costruttore.
	 * 
	 */
	public ClienteLitePMConverter() {
		super();

		addSpringConverter(new ObjectToClienteLiteConverter());
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return ClienteLitePM.class;
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
