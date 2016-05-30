package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.rich.converter.PanjeaConverter;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import com.jidesoft.converter.ConverterContext;

public class CodiceDocumentoConverter extends PanjeaConverter<CodiceDocumento> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<CodiceDocumento> getClasse() {
		return CodiceDocumento.class;
	}

	@Override
	public Comparator<CodiceDocumento> getComparator() {
		return new Comparator<CodiceDocumento>() {

			@Override
			public int compare(CodiceDocumento o1, CodiceDocumento o2) {
				return o1.compareTo(o2);
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

	@Override
	public String toString(Object value, ConverterContext context) {

		String result = "";

		if (value != null && value instanceof CodiceDocumento) {
			CodiceDocumento codiceDocumento = (CodiceDocumento) value;
			result = StringUtils.defaultString(codiceDocumento.getCodice());
		}
		return result;
	}
}
