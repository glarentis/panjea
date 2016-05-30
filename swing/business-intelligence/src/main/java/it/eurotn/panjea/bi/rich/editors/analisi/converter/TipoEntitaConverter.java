package it.eurotn.panjea.bi.rich.editors.analisi.converter;

import it.eurotn.rich.converter.PanjeaConverter;

import java.text.Collator;
import java.util.Comparator;

import com.jidesoft.comparator.ComparatorContext;
import com.jidesoft.converter.ConverterContext;

public class TipoEntitaConverter extends PanjeaConverter {

	public static final ConverterContext CONTEXT_TIPO_ENTITA = new ConverterContext("TipoEntita");

	/**
	 * Costruttore.
	 */
	public TipoEntitaConverter() {
		super(false);
	}

	@Override
	public Object fromString(String s, ConverterContext convertercontext) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return String.class;
	}

	@Override
	public Comparator<Object> getComparator() {
		return Collator.getInstance();
	}

	@Override
	public ComparatorContext getComparatorContext() {
		return new ComparatorContext("tipoEntitaComparator");
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
		String codiceTipoEntita = (String) obj;
		if ("C".equals(codiceTipoEntita)) {
			return "Cliente";
		}
		if ("F".equals(codiceTipoEntita)) {
			return "Forn.";
		}
		if ("V".equals(codiceTipoEntita)) {
			return "Vettore";
		}
		return "Azienda";
	}

}
