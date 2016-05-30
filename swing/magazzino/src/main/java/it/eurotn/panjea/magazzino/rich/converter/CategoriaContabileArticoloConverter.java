package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class CategoriaContabileArticoloConverter extends PanjeaConverter {

	@Override
	public Class<?> getClasse() {
		return CategoriaContabileArticolo.class;
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
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
		String result = "";
		if (arg0 instanceof CategoriaContabileArticolo) {
			result = ((CategoriaContabileArticolo) arg0).getCodice();

		}
		return result;
	}

}
