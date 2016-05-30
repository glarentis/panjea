package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class CodiceIvaConverter extends PanjeaCompositeConverter<CodiceIva> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(CodiceIva value) {
		return value.getDescrizioneInterna();
	}

	@Override
	protected String getCampo2(CodiceIva value) {
		return value.getCodice();
	}

	@Override
	public Class<CodiceIva> getClasse() {
		return CodiceIva.class;
	}

	@Override
	protected Comparator<CodiceIva> getComparatorCampo1() {
		return new Comparator<CodiceIva>() {

			@Override
			public int compare(CodiceIva o1, CodiceIva o2) {
				return o1.getDescrizioneInterna().compareTo(o2.getDescrizioneInterna());
			}
		};
	}

	@Override
	protected Comparator<CodiceIva> getComparatorCampo2() {
		return new Comparator<CodiceIva>() {

			@Override
			public int compare(CodiceIva o1, CodiceIva o2) {
				return o1.getCodice().compareTo(o2.getCodice());
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
