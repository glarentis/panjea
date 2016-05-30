package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class TipoMezzoTrasportoConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return TipoMezzoTrasporto.class;
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

		if (value != null && value instanceof TipoMezzoTrasporto) {
			result = ((TipoMezzoTrasporto) value).getCodice() + " - " + ((TipoMezzoTrasporto) value).getDescrizione();
		}

		return result;
	}
}
