package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class ArticoloRicercaConverter extends PanjeaConverter {

	/**
	 * Costruttore.
	 * 
	 */
	public ArticoloRicercaConverter() {
		super();
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return ArticoloRicerca.class;
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

		if (value != null && value instanceof ArticoloRicerca) {
			ArticoloRicerca articoloRicerca = (ArticoloRicerca) value;
			result = (articoloRicerca.getCodice() == null) ? "" : articoloRicerca.getCodice() + " - ";
			result = result.concat((articoloRicerca.getDescrizione() == null) ? "" : articoloRicerca.getDescrizione());
		}

		return result;
	}

}
