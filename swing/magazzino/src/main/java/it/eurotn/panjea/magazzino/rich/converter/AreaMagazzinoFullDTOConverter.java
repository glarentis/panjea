package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.converter.DocumentoConverter;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;

public class AreaMagazzinoFullDTOConverter extends PanjeaConverter {

	@Override
	public Object fromString(String s, ConverterContext convertercontext) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return AreaMagazzinoFullDTO.class;
	}

	@Override
	public boolean supportFromString(String s, ConverterContext convertercontext) {
		return false;
	}

	@Override
	public boolean supportToString(Object obj, ConverterContext convertercontext) {
		return true;
	}

	@Override
	public String toString(Object obj, ConverterContext convertercontext) {
		StringBuilder result = new StringBuilder();
		if (obj != null && obj instanceof AreaMagazzinoFullDTO) {
			AreaMagazzinoFullDTO area = (AreaMagazzinoFullDTO) obj;
			Documento documento = area.getAreaMagazzino().getDocumento();
			ObjectConverter converter = ObjectConverterManager.getConverter(documento.getClass());
			if (converter != null
					&& converter.supportToString(documento, DocumentoConverter.DOCUMENTO_ABBREVIATO_CONVERTER_CONTEXT)) {
				String convertedString = converter.toString(documento,
						DocumentoConverter.DOCUMENTO_ABBREVIATO_CONVERTER_CONTEXT);
				result.append(convertedString);
			}
		}
		return result.toString();
	}

}
