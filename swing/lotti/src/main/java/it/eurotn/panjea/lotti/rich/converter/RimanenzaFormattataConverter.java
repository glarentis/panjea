package it.eurotn.panjea.lotti.rich.converter;

import it.eurotn.panjea.lotti.domain.Lotto.RimanenzaFormattata;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class RimanenzaFormattataConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return RimanenzaFormattata.class;
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

		String returnStr = "";

		if (arg0 != null && arg0 instanceof RimanenzaFormattata) {

			returnStr = ((RimanenzaFormattata) arg0).getValore();
		}

		return returnStr;
	}

}
