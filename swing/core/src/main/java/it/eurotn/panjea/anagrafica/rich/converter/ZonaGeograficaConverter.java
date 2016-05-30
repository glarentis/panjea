package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

/**
 * Converter per la zona geografica, visualizza codice e descrizione.
 * 
 * @author Leonardo
 */
public class ZonaGeograficaConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return ZonaGeografica.class;
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

		if (value != null && value instanceof ZonaGeografica) {
			if (((ZonaGeografica) value).getCodice() != null) {
				result = ((ZonaGeografica) value).getCodice() + " - " + ((ZonaGeografica) value).getDescrizione();
			}
		}

		return result;
	}
}
