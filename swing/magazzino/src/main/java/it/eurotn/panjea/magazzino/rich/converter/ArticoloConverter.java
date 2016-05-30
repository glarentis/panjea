package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class ArticoloConverter extends PanjeaCompositeConverter<Articolo> implements Comparator<Articolo> {

	@Override
	public int compare(Articolo o1, Articolo o2) {
		return ArticoloConverter.this.toString(o1, null).compareTo(ArticoloConverter.this.toString(o2, null));
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(Articolo value) {
		return value.getCodice();
	}

	@Override
	protected String getCampo2(Articolo value) {
		return value.getDescrizione();
	}

	@Override
	public Class<Articolo> getClasse() {
		return Articolo.class;
	}

	@Override
	protected Comparator<Articolo> getComparatorCampo1() {
		return this;
	}

	@Override
	protected Comparator<Articolo> getComparatorCampo2() {
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
