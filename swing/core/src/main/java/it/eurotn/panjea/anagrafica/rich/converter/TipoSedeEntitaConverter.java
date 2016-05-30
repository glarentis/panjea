package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.rich.converter.PanjeaConverter;

import com.jidesoft.converter.ConverterContext;

public class TipoSedeEntitaConverter extends PanjeaConverter {

	@Override
	public Object fromString(String s, ConverterContext convertercontext) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return TipoSedeEntita.class;
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

		String result = "";

		if (obj != null && obj instanceof TipoSedeEntita) {
			TipoSedeEntita tipoSedeEntita = (TipoSedeEntita) obj;
			result = (tipoSedeEntita.getCodice() == null) ? "" : tipoSedeEntita.getCodice() + " - ";
			result = result.concat((tipoSedeEntita.getDescrizione() == null) ? "" : tipoSedeEntita.getDescrizione());
		}

		return result;
	}

}
