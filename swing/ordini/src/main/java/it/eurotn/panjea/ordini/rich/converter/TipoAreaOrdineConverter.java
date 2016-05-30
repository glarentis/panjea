package it.eurotn.panjea.ordini.rich.converter;

import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class TipoAreaOrdineConverter extends PanjeaCompositeConverter<TipoAreaOrdine> {

	private static Comparator<TipoAreaOrdine> comparatorCampo1 = null;
	private static Comparator<TipoAreaOrdine> comparatorCampo2 = null;

	static {
		comparatorCampo1 = new Comparator<TipoAreaOrdine>() {

			@Override
			public int compare(TipoAreaOrdine o1, TipoAreaOrdine o2) {
				return o1.getTipoDocumento().getDescrizione().compareTo(o2.getTipoDocumento().getDescrizione());
			}
		};
		comparatorCampo2 = new Comparator<TipoAreaOrdine>() {

			@Override
			public int compare(TipoAreaOrdine o1, TipoAreaOrdine o2) {
				return o1.getTipoDocumento().getCodice().compareTo(o2.getTipoDocumento().getCodice());
			}
		};
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(TipoAreaOrdine value) {
		return value.getTipoDocumento().getDescrizione();
	}

	@Override
	protected String getCampo2(TipoAreaOrdine value) {
		return value.getTipoDocumento().getCodice();
	}

	@Override
	public Class<TipoAreaOrdine> getClasse() {
		return TipoAreaOrdine.class;
	}

	@Override
	protected Comparator<TipoAreaOrdine> getComparatorCampo1() {
		return comparatorCampo1;
	}

	@Override
	protected Comparator<TipoAreaOrdine> getComparatorCampo2() {
		return comparatorCampo2;
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
