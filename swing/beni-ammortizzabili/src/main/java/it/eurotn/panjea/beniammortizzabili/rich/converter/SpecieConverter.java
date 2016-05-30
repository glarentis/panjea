/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.converter;

import it.eurotn.panjea.beniammortizzabili2.domain.Specie;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

/**
 * @author fattazzo
 * 
 */
public class SpecieConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Specie.class;
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

		if (object != null && object instanceof Specie) {
			sb.append(((Specie) object).getCodice());
			sb.append("  - ");
			sb.append(((Specie) object).getDescrizione());
		}

		return sb.toString();
	}

}
