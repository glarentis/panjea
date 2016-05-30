package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class DepositoLiteConverter extends PanjeaCompositeConverter<DepositoLite> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(DepositoLite value) {
		return value.getCodice();
	}

	@Override
	protected String getCampo2(DepositoLite value) {
		return value.getDescrizione();
	}

	@Override
	public Class<DepositoLite> getClasse() {
		return DepositoLite.class;
	}

	@Override
	protected Comparator<DepositoLite> getComparatorCampo1() {
		return new Comparator<DepositoLite>() {

			@Override
			public int compare(DepositoLite o1, DepositoLite o2) {
				return DepositoLiteConverter.this.toString(o1, null).compareTo(
						DepositoLiteConverter.this.toString(o2, null));
			}
		};
	}

	@Override
	protected Comparator<DepositoLite> getComparatorCampo2() {
		return new Comparator<DepositoLite>() {

			@Override
			public int compare(DepositoLite o1, DepositoLite o2) {
				return DepositoLiteConverter.this.toString(o1, null).compareTo(
						DepositoLiteConverter.this.toString(o2, null));
			}
		};
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