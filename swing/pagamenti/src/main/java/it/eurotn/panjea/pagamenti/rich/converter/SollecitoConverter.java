package it.eurotn.panjea.pagamenti.rich.converter;

import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.rich.converter.PanjeaConverter;

import java.text.DecimalFormat;
import java.util.Date;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverterManager;

public class SollecitoConverter extends PanjeaConverter {

	private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Sollecito.class;
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
	public String toString(Object value, ConverterContext arg1) {

		StringBuilder result = new StringBuilder();

		if (value != null && value instanceof Sollecito) {

			Sollecito sollecito = (Sollecito) value;
			result.append(ObjectConverterManager.toString(new Date(sollecito.getTimeStamp())));
			result.append(" - ");
			result.append(decimalFormat.format(sollecito.getImporto().getImportoInValutaAzienda()));
			result.append(" " + sollecito.getImporto().getCodiceValuta());
		}

		return result.toString();
	}
}
