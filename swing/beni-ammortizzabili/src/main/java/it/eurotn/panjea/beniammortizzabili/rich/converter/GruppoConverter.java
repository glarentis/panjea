/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.converter;

import it.eurotn.panjea.beniammortizzabili2.domain.Gruppo;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

/**
 * @author fattazzo
 * 
 */
public class GruppoConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Gruppo.class;
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

		if (object != null && object instanceof Gruppo) {
			sb.append(((Gruppo) object).getCodice());
			sb.append(" - ");
			sb.append(((Gruppo) object).getDescrizione());
		}

		return sb.toString();
	}

}
