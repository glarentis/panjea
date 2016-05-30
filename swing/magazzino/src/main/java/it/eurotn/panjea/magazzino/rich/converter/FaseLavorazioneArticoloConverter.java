package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class FaseLavorazioneArticoloConverter extends PanjeaConverter<FaseLavorazioneArticolo> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<FaseLavorazioneArticolo> getClasse() {
		return FaseLavorazioneArticolo.class;
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
		FaseLavorazioneArticolo faseArticolo = (FaseLavorazioneArticolo) arg0;
		if (faseArticolo != null && faseArticolo.getFaseLavorazione() != null) {
			return faseArticolo.getFaseLavorazione().getCodice();
		}
		return "";
	}

}
