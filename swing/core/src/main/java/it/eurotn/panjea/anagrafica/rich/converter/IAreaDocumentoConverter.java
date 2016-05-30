package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;

public class IAreaDocumentoConverter extends PanjeaConverter<IAreaDocumento> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<IAreaDocumento> getClasse() {
		return IAreaDocumento.class;
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
		StringBuilder result = new StringBuilder();
		if (value != null && value instanceof IAreaDocumento) {
			IAreaDocumento area = (IAreaDocumento) value;
			Documento documento = area.getDocumento();
			ObjectConverter converter = ObjectConverterManager.getConverter(documento.getClass());
			if (converter != null && converter.supportToString(documento, null)) {
				String convertedString = converter.toString(documento, null);
				result.append(convertedString);
			}
		}
		return result.toString();
	}
}
