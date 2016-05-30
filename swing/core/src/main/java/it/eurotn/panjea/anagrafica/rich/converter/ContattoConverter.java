package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.domain.Contatto;
import it.eurotn.rich.converter.PanjeaConverter;

import org.apache.commons.lang3.ObjectUtils;

import com.jidesoft.converter.ConverterContext;

public class ContattoConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Contatto.class;
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
	public String toString(Object value, ConverterContext arg1) {

		String result = "";

		if (value != null && value instanceof Contatto) {

			Contatto contatto = (Contatto) value;
			result = contatto.getNome() + " " + ObjectUtils.defaultIfNull(contatto.getCognome(), "");
		}

		return result;
	}

}
