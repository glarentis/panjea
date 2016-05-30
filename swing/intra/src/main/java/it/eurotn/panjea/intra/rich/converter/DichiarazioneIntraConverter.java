package it.eurotn.panjea.intra.rich.converter;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class DichiarazioneIntraConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return DichiarazioneIntra.class;
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
		if (arg0 == null) {
			return "";
		}
		DichiarazioneIntra dichiarazioneIntra = (DichiarazioneIntra) arg0;
		return dichiarazioneIntra.getDescrizioneDichiarazione();
	}

}
