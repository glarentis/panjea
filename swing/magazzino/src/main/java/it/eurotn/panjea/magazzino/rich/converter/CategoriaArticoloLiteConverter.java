package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class CategoriaArticoloLiteConverter extends PanjeaCompositeConverter<CategoriaLite> implements
		Comparator<CategoriaLite> {

	@Override
	public int compare(CategoriaLite o1, CategoriaLite o2) {
		return CategoriaArticoloLiteConverter.this.toString(o1, null).compareTo(
				CategoriaArticoloLiteConverter.this.toString(o2, null));
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(CategoriaLite value) {
		return value.getCodice();
	}

	@Override
	protected String getCampo2(CategoriaLite value) {
		return value.getDescrizione();
	}

	@Override
	public Class<CategoriaLite> getClasse() {
		return CategoriaLite.class;
	}

	@Override
	protected Comparator<CategoriaLite> getComparatorCampo1() {
		return this;
	}

	@Override
	protected Comparator<CategoriaLite> getComparatorCampo2() {
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
