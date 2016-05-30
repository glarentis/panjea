package it.eurotn.panjea.protocolli.rich.converter;

import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class ProtocolloConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		Protocollo protocollo = new Protocollo();
		protocollo.setId(1);
		return protocollo;
	}

	@Override
	public Class<?> getClasse() {
		return Protocollo.class;
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
	public String toString(Object arg0, ConverterContext arg1) {
		String result = "";

		if (arg0 != null && ((Protocollo) arg0).getId() != null) {
			result = ((Protocollo) arg0).getCodice() + " - " + ((Protocollo) arg0).getDescrizione();
		}
		return result;
	}

}
