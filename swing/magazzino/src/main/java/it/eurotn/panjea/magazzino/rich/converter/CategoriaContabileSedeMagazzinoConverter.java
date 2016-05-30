package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.CategoriaContabileSedeMagazzino;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class CategoriaContabileSedeMagazzinoConverter extends PanjeaConverter {

	@Override
	public Class<?> getClasse() {
		return CategoriaContabileSedeMagazzino.class;
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
		if (arg0 instanceof CategoriaContabileSedeMagazzino) {
			result = ((CategoriaContabileSedeMagazzino) arg0).getCodice();
		}
		return result;
	}

}
