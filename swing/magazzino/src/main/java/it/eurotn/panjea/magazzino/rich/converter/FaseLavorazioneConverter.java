package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazione;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class FaseLavorazioneConverter extends PanjeaConverter<FaseLavorazione> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<FaseLavorazione> getClasse() {
		return FaseLavorazione.class;
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
		if (arg0 instanceof FaseLavorazione) {
			FaseLavorazione fase = (FaseLavorazione) arg0;
			return fase.getCodice() + " - " + fase.getDescrizione();
		}
		return "";
	}

}
