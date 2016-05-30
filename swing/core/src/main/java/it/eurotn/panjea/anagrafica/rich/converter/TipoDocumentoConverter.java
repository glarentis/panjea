package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class TipoDocumentoConverter extends PanjeaCompositeConverter<TipoDocumento> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(TipoDocumento value) {
		return value.getDescrizione();
	}

	@Override
	protected String getCampo2(TipoDocumento value) {
		return value.getCodice();
	}

	@Override
	public Class<TipoDocumento> getClasse() {
		return TipoDocumento.class;
	}

	@Override
	protected Comparator<TipoDocumento> getComparatorCampo1() {
		return new Comparator<TipoDocumento>() {

			@Override
			public int compare(TipoDocumento o1, TipoDocumento o2) {
				return o1.getDescrizione().compareTo(o2.getDescrizione());
			}
		};
	}

	@Override
	protected Comparator<TipoDocumento> getComparatorCampo2() {
		return new Comparator<TipoDocumento>() {

			@Override
			public int compare(TipoDocumento o1, TipoDocumento o2) {
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
