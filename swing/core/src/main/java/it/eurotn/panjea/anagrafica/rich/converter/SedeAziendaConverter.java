package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class SedeAziendaConverter extends PanjeaConverter<SedeAzienda> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<SedeAzienda> getClasse() {
		return SedeAzienda.class;
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

		if (value != null && value instanceof SedeAzienda) {
			result = ((SedeAzienda) value).getSede().getIndirizzo() + " - "
					+ ((SedeAzienda) value).getSede().getDatiGeografici().getDescrizioneLocalita();
		}
		return result;
	}

}
