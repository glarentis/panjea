package it.eurotn.panjea.intra.rich.converter;

import it.eurotn.panjea.intra.domain.Servizio;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

/**
 * Converter per la classe servizio.
 * 
 * @author giangi
 * @version 1.0, 18/ott/2012
 * 
 */
public class ServizioConverter extends PanjeaConverter {

	@Override
	public Object fromString(String paramString, ConverterContext paramConverterContext) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Servizio.class;
	}

	@Override
	public boolean supportFromString(String paramString, ConverterContext paramConverterContext) {
		return false;
	}

	@Override
	public boolean supportToString(Object paramObject, ConverterContext paramConverterContext) {
		return true;
	}

	@Override
	public String toString(Object servizio, ConverterContext paramConverterContext) {
		if (servizio != null) {
			return ((Servizio) servizio).getCodice() + "-" + ((Servizio) servizio).getDescrizione();
		} else {
			return "";
		}
	}

}
