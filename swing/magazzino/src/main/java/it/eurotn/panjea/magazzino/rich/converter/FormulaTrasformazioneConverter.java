package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class FormulaTrasformazioneConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return FormulaTrasformazione.class;
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

		if (arg0 != null && arg0 instanceof FormulaTrasformazione) {
			FormulaTrasformazione formula = (FormulaTrasformazione) arg0;
			if (formula != null && formula.getId() != null) {
				result = formula.getCodice();
			}
		}

		return result;
	}
}
