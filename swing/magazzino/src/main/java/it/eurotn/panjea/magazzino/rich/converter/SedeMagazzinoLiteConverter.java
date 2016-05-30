package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.rich.converter.PanjeaConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class SedeMagazzinoLiteConverter extends PanjeaConverter implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		SedeMagazzinoLite s1 = (SedeMagazzinoLite) o1;
		SedeMagazzinoLite s2 = (SedeMagazzinoLite) o2;
		return s1.getSedeEntita().getEntita().getCodice().compareTo(s2.getSedeEntita().getEntita().getCodice());
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return SedeMagazzinoLite.class;
	}

	@Override
	public Comparator<Object> getComparator() {
		return this;
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

		if (value != null && value instanceof SedeMagazzinoLite) {
			result = ((SedeMagazzinoLite) value).getSedeEntita().getEntita().getAnagrafica().getDenominazione() + " - "
					+ ((SedeMagazzinoLite) value).getSedeEntita().getSede().getIndirizzo();
		}

		return result;
	}
}
