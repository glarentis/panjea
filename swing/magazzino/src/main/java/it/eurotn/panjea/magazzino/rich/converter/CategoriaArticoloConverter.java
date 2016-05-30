package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class CategoriaArticoloConverter extends PanjeaCompositeConverter<Categoria> implements Comparator<Categoria> {

	@Override
	public int compare(Categoria o1, Categoria o2) {
		return CategoriaArticoloConverter.this.toString(o1, null).compareTo(
				CategoriaArticoloConverter.this.toString(o2, null));
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(Categoria value) {
		return value.getCodice();
	}

	@Override
	protected String getCampo2(Categoria value) {
		return value.getDescrizione();
	}

	@Override
	public Class<Categoria> getClasse() {
		return Categoria.class;
	}

	@Override
	protected Comparator<Categoria> getComparatorCampo1() {
		return this;
	}

	@Override
	protected Comparator<Categoria> getComparatorCampo2() {
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
