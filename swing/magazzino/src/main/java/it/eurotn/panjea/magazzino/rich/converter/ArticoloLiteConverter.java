package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.converter.ConverterContext;

public class ArticoloLiteConverter extends PanjeaCompositeConverter<ArticoloLite> implements Comparator<ArticoloLite> {

	private class ArticoloFromArticoloLiteConverter extends AbstractConverter {

		@SuppressWarnings("rawtypes")
		@Override
		protected Object doConvert(Object arg0, Class arg1, ConversionContext arg2) throws Exception {
			ArticoloLite articoloLite = (ArticoloLite) arg0;
			Articolo articolo = null;
			if (articoloLite != null) {
				articolo = new Articolo();
				articolo.setId(articoloLite.getId());
				articolo.setVersion(articoloLite.getVersion());
				articolo.setCodice(articoloLite.getCodice());
				articolo.setDescrizione(articoloLite.getDescrizione());
			}
			return articolo;
		}

		@Override
		public Class<?>[] getSourceClasses() {
			return new Class[] { ArticoloLite.class };
		}

		@Override
		public Class<?>[] getTargetClasses() {
			return new Class[] { Articolo.class };
		}
	}

	/**
	 * Costruttore.
	 * 
	 */
	public ArticoloLiteConverter() {
		super();
		addSpringConverter(new ArticoloFromArticoloLiteConverter());
	}

	@Override
	public int compare(ArticoloLite o1, ArticoloLite o2) {
		return ArticoloLiteConverter.this.toString(o1, null).compareTo(ArticoloLiteConverter.this.toString(o2, null));
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(ArticoloLite value) {
		return value.getCodice();
	}

	@Override
	protected String getCampo2(ArticoloLite value) {
		return value.getDescrizione();
	}

	@Override
	public Class<ArticoloLite> getClasse() {
		return ArticoloLite.class;
	}

	@Override
	protected Comparator<ArticoloLite> getComparatorCampo1() {
		return this;
	}

	@Override
	protected Comparator<ArticoloLite> getComparatorCampo2() {
		return this;
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
