package it.eurotn.panjea.preventivi.rich.converter;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.preventivi.domain.documento.AreaPreventivo;
import it.eurotn.panjea.preventivi.util.AreaPreventivoRicerca;
import it.eurotn.rich.converter.PanjeaConverter;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverterManager;

public class AreaPreventivoRicercaConverter extends PanjeaConverter<AreaPreventivoRicerca> {
	private class AreaPreventivoRicercaToAreaPreventivoConverter extends AbstractConverter {

		@SuppressWarnings("rawtypes")
		@Override
		protected Object doConvert(Object areaPreventivoRicerca, Class class1, ConversionContext conversioncontext)
				throws Exception {

			if (areaPreventivoRicerca == null) {
				return null;
			}

			AreaPreventivoRicerca areaPreventivoRicercaToConvert = (AreaPreventivoRicerca) areaPreventivoRicerca;
			AreaPreventivo areaPreventivo = new AreaPreventivo();
			areaPreventivo.setId(areaPreventivoRicercaToConvert.getIdAreaDocumento());
			areaPreventivo.setDocumento(areaPreventivoRicercaToConvert.getDocumento());
			areaPreventivo.setTipoAreaPreventivo(areaPreventivoRicercaToConvert.getTipoAreaDocumento());
			return areaPreventivo;
		}

		@Override
		public Class<?>[] getSourceClasses() {
			return new Class<?>[] { AreaPreventivoRicerca.class };
		}

		@Override
		public Class<?>[] getTargetClasses() {
			return new Class<?>[] { AreaPreventivo.class };
		}

	}

	/**
	 * Costruttore.
	 */
	public AreaPreventivoRicercaConverter() {
		addSpringConverter(new AreaPreventivoRicercaToAreaPreventivoConverter());
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<AreaPreventivoRicerca> getClasse() {
		return AreaPreventivoRicerca.class;
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
		Documento doc = (Documento) value;
		StringBuffer sb = new StringBuffer();
		if (doc.getTipoDocumento() != null) {
			sb.append(doc.getTipoDocumento().getCodice());
		}
		sb.append(" numero " + doc.getCodice().getCodice());
		sb.append(" del " + ObjectConverterManager.toString(doc.getDataDocumento()));
		return sb.toString();
	}

}
