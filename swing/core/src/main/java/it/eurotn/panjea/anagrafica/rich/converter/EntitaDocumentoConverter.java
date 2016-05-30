package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import com.jidesoft.converter.ConverterContext;

public class EntitaDocumentoConverter extends PanjeaCompositeConverter<EntitaDocumento> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(EntitaDocumento value) {
		return value.getAnagrafica().getDenominazione();
	}

	@Override
	protected String getCampo2(EntitaDocumento value) {
		return value.getCodice() == null ? "" : value.getCodice().toString();
	}

	@Override
	public Class<EntitaDocumento> getClasse() {
		return EntitaDocumento.class;
	}

	@Override
	protected Comparator<EntitaDocumento> getComparatorCampo1() {
		return new Comparator<EntitaDocumento>() {

			@Override
			public int compare(EntitaDocumento o1, EntitaDocumento o2) {
				return o1.getAnagrafica().getDenominazione().compareTo(o2.getAnagrafica().getDenominazione());
			}
		};
	}

	@Override
	protected Comparator<EntitaDocumento> getComparatorCampo2() {
		return new Comparator<EntitaDocumento>() {

			@Override
			public int compare(EntitaDocumento o1, EntitaDocumento o2) {
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
