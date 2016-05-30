package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.Contratto;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class ContrattoConverter extends PanjeaCompositeConverter<Contratto> implements Comparator<Contratto> {

	@Override
	public int compare(Contratto o1, Contratto o2) {
		return toString(o1, null).compareTo(toString(o2, null));
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(Contratto value) {
		return value.getCodice();
	}

	@Override
	protected String getCampo2(Contratto value) {
		return value.getDescrizione();
	}

	@Override
	public Class<Contratto> getClasse() {
		return Contratto.class;
	}

	@Override
	protected Comparator<Contratto> getComparatorCampo1() {
		return this;
	}

	@Override
	protected Comparator<Contratto> getComparatorCampo2() {
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

}
