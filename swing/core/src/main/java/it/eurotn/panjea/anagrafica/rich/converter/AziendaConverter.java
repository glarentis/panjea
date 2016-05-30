package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class AziendaConverter extends PanjeaConverter<AziendaCorrente> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<AziendaCorrente> getClasse() {
		return AziendaCorrente.class;
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

		if (value != null && value instanceof AziendaCorrente) {
			result = ((AziendaCorrente) value).getDenominazione();
		}

		return result;
	}

}
