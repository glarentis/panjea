package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class UnitaMisuraConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		UnitaMisura um = new UnitaMisura();
		um.setCodice(arg0);
		return um;
	}

	@Override
	public Class<?> getClasse() {
		return UnitaMisura.class;
	}

	@Override
	public boolean supportFromString(String arg0, ConverterContext arg1) {
		return true;
	}

	@Override
	public boolean supportToString(Object arg0, ConverterContext arg1) {
		return true;
	}

	@Override
	public String toString(Object value, ConverterContext arg1) {

		String result = "";

		if (value != null && value instanceof UnitaMisura) {
			UnitaMisura um = (UnitaMisura) value;
			result = um.getDescrizione() + (um.getDescrizione().isEmpty() ? "" : " - ") + um.getCodice();

		}
		return result;
	}

}
