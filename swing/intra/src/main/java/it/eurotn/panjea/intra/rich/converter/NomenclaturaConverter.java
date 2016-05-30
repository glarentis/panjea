package it.eurotn.panjea.intra.rich.converter;

import it.eurotn.panjea.intra.domain.Nomenclatura;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class NomenclaturaConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Nomenclatura.class;
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
	public String toString(Object arg0, ConverterContext arg1) {
		if (arg0 != null) {
			return ((Nomenclatura) arg0).getCodice() + "-" + ((Nomenclatura) arg0).getDescrizione();
		} else {
			return "";
		}
	}

}
