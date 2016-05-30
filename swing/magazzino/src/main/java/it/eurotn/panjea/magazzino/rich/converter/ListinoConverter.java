package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class ListinoConverter extends PanjeaCompositeConverter<Listino> implements Comparator<Listino> {

	@Override
	public int compare(Listino o1, Listino o2) {
		return ListinoConverter.this.toString(01, null).compareTo(ListinoConverter.this.toString(02, null));
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(Listino value) {
		return value.getCodice();
	}

	@Override
	protected String getCampo2(Listino value) {
		return value.getDescrizione();
	}

	@Override
	public Class<Listino> getClasse() {
		return Listino.class;
	}

	@Override
	protected Comparator<Listino> getComparatorCampo1() {
		return this;
	}

	@Override
	protected Comparator<Listino> getComparatorCampo2() {
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
