/**
 *
 */
package it.eurotn.panjea.beniammortizzabili.rich.converter;

import it.eurotn.panjea.beniammortizzabili2.domain.SottoSpecie;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

/**
 * @author fattazzo
 *
 */
public class SottoSpecieConverter extends PanjeaConverter<SottoSpecie> {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<SottoSpecie> getClasse() {
		return SottoSpecie.class;
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
	public String toString(Object object, ConverterContext arg1) {
		StringBuilder sb = new StringBuilder();

		if (object != null && object instanceof SottoSpecie) {
			sb.append(((SottoSpecie) object).getCodice());
			sb.append("  - ");
			sb.append(((SottoSpecie) object).getDescrizione());
		}

		return sb.toString();
	}

}
