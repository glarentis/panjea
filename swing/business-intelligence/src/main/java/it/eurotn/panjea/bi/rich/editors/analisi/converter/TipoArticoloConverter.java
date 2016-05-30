package it.eurotn.panjea.bi.rich.editors.analisi.converter;

import it.eurotn.rich.converter.PanjeaConverter;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import com.jidesoft.comparator.NumberComparator;
import com.jidesoft.converter.ConverterContext;

public class TipoArticoloConverter extends PanjeaConverter {

	public static final ConverterContext CONTEXT_TIPO_ARTICOLO = new ConverterContext("TipoArticolo");

	/**
	 * Costruttore.
	 */
	public TipoArticoloConverter() {
		super(false);
		// evito di registrare il converter come converter di default di spring senza context altrimenti viene
		// registrato questo per gli integer al posto di quello di default
	}

	@Override
	public Object fromString(String s, ConverterContext convertercontext) {
		if ("Fisico".equals(s)) {
			return 0;
		} else if ("Padre".equals(s)) {
			return 1;
		} else if ("Servizi".equals(s)) {
			return 2;
		} else if ("Accessori".equals(s)) {
			return 3;
		} else if ("Spese Trasp.".equals(s)) {
			return 4;
		} else if ("Altre spese".equals(s)) {
			return 5;
		}
		return "";
	}

	@Override
	public Class<?> getClasse() {
		return Integer.class;
	}

	@Override
	public Comparator<Object> getComparator() {
		return NumberComparator.getInstance();
	}

	@Override
	public boolean supportFromString(String s, ConverterContext convertercontext) {
		return true;
	}

	@Override
	public boolean supportToString(Object obj, ConverterContext convertercontext) {
		return true;
	}

	@Override
	public String toString(Object obj, ConverterContext convertercontext) {
		if (obj == null || obj.toString().isEmpty()) {
			return "";
		}
		if (!StringUtils.isNumeric(obj.toString())) {
			return "RICARICARE DATI";
		}
		Integer tipoArticolo = new Integer(obj.toString());
		switch (tipoArticolo) {
		case 0:
			return "Fisico";
		case 1:
			return "Padre";
		case 2:
			return "Servizi";
		case 3:
			return "Accessori";
		case 4:
			return "Spese Trasp.";
		case 5:
			return "Altre spese";
		default:
			return "";
		}
	}

}
