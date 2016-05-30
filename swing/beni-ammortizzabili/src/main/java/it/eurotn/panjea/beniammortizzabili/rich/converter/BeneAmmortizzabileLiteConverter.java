package it.eurotn.panjea.beniammortizzabili.rich.converter;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class BeneAmmortizzabileLiteConverter extends PanjeaConverter<BeneAmmortizzabileLite> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<BeneAmmortizzabileLite> getClasse() {
		return BeneAmmortizzabileLite.class;
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
			return ((BeneAmmortizzabileLite) arg0).getCodice() + " - "
					+ ((BeneAmmortizzabileLite) arg0).getDescrizione();
		} else {
			return "";
		}
	}

}
