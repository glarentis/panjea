package it.eurotn.panjea.preventivi.rich.converter;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.rich.converter.DocumentoConverter;
import it.eurotn.panjea.preventivi.util.AreaPreventivoFullDTO;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;

public class AreaPreventivoFullDTOConverter extends PanjeaConverter<AreaPreventivoFullDTO> {
	@Override
	public Object fromString(String s, ConverterContext convertercontext) {
		return null;
	}

	@Override
	public Class<AreaPreventivoFullDTO> getClasse() {
		return AreaPreventivoFullDTO.class;
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

		if (obj != null && obj instanceof AreaPreventivoFullDTO) {
			AreaPreventivoFullDTO area = (AreaPreventivoFullDTO) obj;
			Documento documento = area.getAreaPreventivo().getDocumento();
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
