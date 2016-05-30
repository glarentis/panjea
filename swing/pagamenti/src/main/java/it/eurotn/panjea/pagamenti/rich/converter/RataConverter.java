package it.eurotn.panjea.pagamenti.rich.converter;

import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.rich.converter.PanjeaConverter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.jidesoft.converter.ConverterContext;

public class RataConverter extends PanjeaConverter {

	@Override
	public Object fromString(String string, ConverterContext context) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Rata.class;
	}

	@Override
	public boolean supportFromString(String string, ConverterContext context) {
		return false;
	}

	@Override
	public boolean supportToString(Object object, ConverterContext context) {
		return true;
	}

	@Override
	public String toString(Object value, ConverterContext context) {

		StringBuilder result = new StringBuilder();

		if (value != null && value instanceof Rata) {
			Rata rata = (Rata) value;

			result.append("N. ");
			result.append(rata.getNumeroRata());
			result.append(" importo: ");
			result.append(new DecimalFormat("#,##0.00").format(rata.getImporto().getImportoInValutaAzienda()));
			result.append(" €, ");
			result.append("data sc.: ");
			if (rata.getDataScadenza() != null) {
				result.append(new SimpleDateFormat("dd/MM/yyyy").format(rata.getDataScadenza()));
			}
		}

		return result.toString();
	}
}
