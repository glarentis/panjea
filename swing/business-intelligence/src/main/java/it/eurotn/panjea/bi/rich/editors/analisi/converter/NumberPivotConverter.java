package it.eurotn.panjea.bi.rich.editors.analisi.converter;

import java.text.DecimalFormat;

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;

public class NumberPivotConverter implements ObjectConverter {

	// per ottimizzare invece di crearmi ogni volta il format creo 6 istanze per i diversi decimali
	public static final DecimalFormat[] DECIMAL_FORMAT = new DecimalFormat[8];

	static {
		DECIMAL_FORMAT[0] = new DecimalFormat("0");
		DECIMAL_FORMAT[0].setGroupingSize(3);

		DECIMAL_FORMAT[1] = new DecimalFormat("0.0");
		DECIMAL_FORMAT[1].setGroupingSize(3);

		DECIMAL_FORMAT[2] = new DecimalFormat("0.00");
		DECIMAL_FORMAT[2].setGroupingSize(3);

		DECIMAL_FORMAT[3] = new DecimalFormat("0.000");
		DECIMAL_FORMAT[3].setGroupingSize(3);

		DECIMAL_FORMAT[4] = new DecimalFormat("0.0000");
		DECIMAL_FORMAT[4].setGroupingSize(3);

		DECIMAL_FORMAT[5] = new DecimalFormat("0.00000");
		DECIMAL_FORMAT[5].setGroupingSize(3);

		DECIMAL_FORMAT[6] = new DecimalFormat("0.000000");
		DECIMAL_FORMAT[6].setGroupingSize(3);
	}

	@Override
	public Object fromString(String paramString, ConverterContext paramConverterContext) {
		return new Integer(paramString).intValue();
	}

	@Override
	public boolean supportFromString(String paramString, ConverterContext paramConverterContext) {
		return true;
	}

	@Override
	public boolean supportToString(Object paramObject, ConverterContext paramConverterContext) {
		return true;
	}

	@Override
	public String toString(Object paramObject, ConverterContext paramConverterContext) {
		String result = paramObject == null ? "" : paramObject.toString();
		if (result.isEmpty()) {
			return result;
		}
		if ("(Vuoto)".equals(result)) {
			return "";
		}
		if (paramConverterContext instanceof NumberPivotContext) {
			NumberPivotContext context = (NumberPivotContext) paramConverterContext;
			DecimalFormat format = DECIMAL_FORMAT[0];
			if (context.getNumeroDecimali() < 7) {
				format = DECIMAL_FORMAT[context.getNumeroDecimali()];
			}
			format.setGroupingUsed(context.isSeparatoreVisible());
			try {
				result = format.format(paramObject);
				format.setGroupingUsed(context.isSeparatoreVisible());
				if (context.getPostFisso() != null && !context.getPostFisso().isEmpty()) {
					result = new StringBuilder(context.getPostFisso()).append(" ").append(result).toString();
				}
			} catch (Exception ex) {
				System.out.println("IntegerPivotConverter-toString:ERRORE " + result);
			}
		}
		return result;
	}
}
