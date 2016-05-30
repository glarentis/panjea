package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.rich.converter.PanjeaConverter;

import java.text.SimpleDateFormat;

import com.jidesoft.converter.ConverterContext;

public class DatiGenerazioneConverter extends PanjeaConverter {

	private final SimpleDateFormat simpleDateFormat;

	{
		simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return DatiGenerazione.class;
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

		if (arg0 != null && arg0 instanceof DatiGenerazione) {

			DatiGenerazione datiGenerazione = (DatiGenerazione) arg0;
			StringBuilder sb = new StringBuilder();

			if (datiGenerazione.getDataGenerazione() != null) {
				sb.append(simpleDateFormat.format(datiGenerazione.getDataGenerazione()) + " ");
			}
			if (datiGenerazione.getUtente() != null) {
				sb.append("( " + datiGenerazione.getUtente() + " )");
			}

			returnStr = sb.toString();
		}

		return returnStr;
	}
}
