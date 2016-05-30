package it.eurotn.panjea.magazzino.rich.converter;

import it.eurotn.panjea.magazzino.domain.RulesValidationErrors;
import it.eurotn.rich.converter.PanjeaConverter;

import org.springframework.richclient.util.RcpSupport;

import com.jidesoft.converter.ConverterContext;

public class RulesValidationErrorsConverter extends PanjeaConverter {

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return RulesValidationErrors.class;
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
	public String toString(Object arg0, ConverterContext arg1) {
		StringBuffer result = new StringBuffer();

		if (arg0 != null && arg0 instanceof RulesValidationErrors) {

			RulesValidationErrors rules = (RulesValidationErrors) arg0;
			for (String rule : rules.getRules()) {
				if (result.length() > 0) {
					result.append(", ");
				}
				result.append(RcpSupport.getMessage(rule));
			}
		}

		return result.toString();
	}

}
