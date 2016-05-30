package it.eurotn.panjea.anagrafica.rich.converter.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class LocalitaConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Localita.class;
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
		if (value != null && value instanceof Localita) {
			result = ((Localita) value).getDescrizione();
		}
		return result;
	}

}
