package it.eurotn.panjea.anagrafica.rich.converter.datigeografici;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

/**
 * @author fattazzo
 * 
 */
public class NazioneConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Nazione.class;
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
		if (arg0 != null && arg0 instanceof Nazione) {
			Nazione nazione = (Nazione) arg0;
			return nazione.getCodice();
			// return new StringBuilder().append(nazione.getDescrizione()).append(" - ").append(nazione.getCodice())
			// .toString();
		} else {
			return "";
		}
	}

}
