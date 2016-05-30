package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class DepositoConverter extends PanjeaCompositeConverter<Deposito> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(Deposito value) {
		return value.getCodice();
	}

	@Override
	protected String getCampo2(Deposito value) {
		return value.getDescrizione();
	}

	@Override
	public Class<Deposito> getClasse() {
		return Deposito.class;
	}

	@Override
	protected Comparator<Deposito> getComparatorCampo1() {
		return new Comparator<Deposito>() {

			@Override
			public int compare(Deposito o1, Deposito o2) {
				return DepositoConverter.this.toString(o1, null).compareTo(DepositoConverter.this.toString(o2, null));
			}
		};
	}

	@Override
	protected Comparator<Deposito> getComparatorCampo2() {
		return new Comparator<Deposito>() {

			@Override
			public int compare(Deposito o1, Deposito o2) {
				return DepositoConverter.this.toString(o1, null).compareTo(DepositoConverter.this.toString(o2, null));
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
