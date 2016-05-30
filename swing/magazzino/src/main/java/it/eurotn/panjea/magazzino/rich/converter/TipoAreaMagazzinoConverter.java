package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.rich.converter.PanjeaCompositeConverter;

import java.util.Comparator;

import org.springframework.binding.convert.ConversionContext;
import org.springframework.binding.convert.support.AbstractConverter;

import com.jidesoft.converter.ConverterContext;

public class TipoAreaMagazzinoConverter extends PanjeaCompositeConverter<TipoAreaMagazzino> {

	public class TipoAreaMagazzinoToTipoDocumentoConverter extends AbstractConverter {

		@Override
		protected Object doConvert(Object paramObject, Class paramClass, ConversionContext paramConversionContext)
				throws Exception {
			if (paramObject == null) {
				return null;
			}
			return ((TipoAreaMagazzino) paramObject).getTipoDocumento();
		}

		@Override
		public Class[] getSourceClasses() {
			return new Class[] { TipoAreaMagazzino.class };
		}

		@Override
		public Class[] getTargetClasses() {
			return new Class[] { TipoDocumento.class };
		}

	}

	private static Comparator<TipoAreaMagazzino> comparatorCampo1 = null;
	private static Comparator<TipoAreaMagazzino> comparatorCampo2 = null;

	static {
		comparatorCampo1 = new Comparator<TipoAreaMagazzino>() {

			@Override
			public int compare(TipoAreaMagazzino o1, TipoAreaMagazzino o2) {
				return o1.getTipoDocumento().getDescrizione().compareTo(o2.getTipoDocumento().getDescrizione());
			}
		};
		comparatorCampo2 = new Comparator<TipoAreaMagazzino>() {

			@Override
			public int compare(TipoAreaMagazzino o1, TipoAreaMagazzino o2) {
				return o1.getTipoDocumento().getCodice().compareTo(o2.getTipoDocumento().getCodice());
			}
		};
	}

	/**
	 * Costruttore.
	 */
	public TipoAreaMagazzinoConverter() {
		addSpringConverter(new TipoAreaMagazzinoToTipoDocumentoConverter());
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	protected String getCampo1(TipoAreaMagazzino value) {
		return value.getTipoDocumento().getDescrizione();
	}

	@Override
	protected String getCampo2(TipoAreaMagazzino value) {
		return value.getTipoDocumento().getCodice();
	}

	@Override
	public Class<TipoAreaMagazzino> getClasse() {
		return TipoAreaMagazzino.class;
	}

	@Override
	protected Comparator<TipoAreaMagazzino> getComparatorCampo1() {
		return comparatorCampo1;
	}

	@Override
	protected Comparator<TipoAreaMagazzino> getComparatorCampo2() {
		return comparatorCampo2;
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
