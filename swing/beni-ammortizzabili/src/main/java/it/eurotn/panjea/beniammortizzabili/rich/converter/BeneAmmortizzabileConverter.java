package it.eurotn.panjea.beniammortizzabili.rich.converter;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class BeneAmmortizzabileConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return BeneAmmortizzabile.class;
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
			return ((BeneAmmortizzabile) arg0).getCodice() + " - " + ((BeneAmmortizzabile) arg0).getDescrizione();
		} else {
			return "";
		}
	}

}
