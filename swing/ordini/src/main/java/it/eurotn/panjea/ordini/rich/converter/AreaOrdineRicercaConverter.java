package it.eurotn.panjea.ordini.rich.converter;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.util.AreaOrdineRicerca;
import it.eurotn.rich.converter.PanjeaConverter;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverterManager;

public class AreaOrdineRicercaConverter extends PanjeaConverter<AreaOrdineRicerca> {

	private class AreaOrdineRicercaToAreaOderConverter extends AbstractConverter {

		@SuppressWarnings("rawtypes")
		@Override
		protected Object doConvert(Object areaOrdineRicerca, Class class1, ConversionContext conversioncontext)
				throws Exception {

			if (areaOrdineRicerca == null) {
				return null;
			}

			AreaOrdineRicerca areaOrdineRicercaToConvert = (AreaOrdineRicerca) areaOrdineRicerca;
			AreaOrdine areaOrdine = new AreaOrdine();
			areaOrdine.setId(areaOrdineRicercaToConvert.getIdAreaOrdine());
			areaOrdine.setDocumento(areaOrdineRicercaToConvert.getDocumento());
			areaOrdine.setTipoAreaOrdine(areaOrdineRicercaToConvert.getTipoAreaOrdine());
			return areaOrdine;
		}

		@Override
		public Class<?>[] getSourceClasses() {
			return new Class<?>[] { AreaOrdineRicerca.class };
		}

		@Override
		public Class<?>[] getTargetClasses() {
			return new Class<?>[] { AreaOrdine.class };
		}

	}

	/**
	 * Costruttore.
	 */
	public AreaOrdineRicercaConverter() {
		super();
		addSpringConverter(new AreaOrdineRicercaToAreaOderConverter());
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<AreaOrdineRicerca> getClasse() {
		return AreaOrdineRicerca.class;
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
		sb.append(" numero " + doc.getCodice());
		sb.append(" del " + ObjectConverterManager.toString(doc.getDataDocumento()));
		return sb.toString();
	}

}
