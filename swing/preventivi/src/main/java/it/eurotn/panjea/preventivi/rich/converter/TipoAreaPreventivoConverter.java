package it.eurotn.panjea.preventivi.rich.converter;

import it.eurotn.panjea.preventivi.domain.documento.TipoAreaPreventivo;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class TipoAreaPreventivoConverter extends PanjeaCompositeConverter<TipoAreaPreventivo> {
	private static Comparator<TipoAreaPreventivo> comparatorCampo1 = null;
	private static Comparator<TipoAreaPreventivo> comparatorCampo2 = null;

	static {
		comparatorCampo1 = new Comparator<TipoAreaPreventivo>() {

			@Override
			public int compare(TipoAreaPreventivo o1, TipoAreaPreventivo o2) {
				return o1.getTipoDocumento().getDescrizione().compareTo(o2.getTipoDocumento().getDescrizione());
			}
		};
		comparatorCampo2 = new Comparator<TipoAreaPreventivo>() {

			@Override
			public int compare(TipoAreaPreventivo o1, TipoAreaPreventivo o2) {
				return o1.getTipoDocumento().getCodice().compareTo(o2.getTipoDocumento().getCodice());
			}
		};
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(TipoAreaPreventivo value) {
		return value.getTipoDocumento().getDescrizione();
	}

	@Override
	protected String getCampo2(TipoAreaPreventivo value) {
		return value.getTipoDocumento().getCodice();
	}

	@Override
	public Class<TipoAreaPreventivo> getClasse() {
		return TipoAreaPreventivo.class;
	}

	@Override
	protected Comparator<TipoAreaPreventivo> getComparatorCampo1() {
		return comparatorCampo1;
	}

	@Override
	protected Comparator<TipoAreaPreventivo> getComparatorCampo2() {
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
