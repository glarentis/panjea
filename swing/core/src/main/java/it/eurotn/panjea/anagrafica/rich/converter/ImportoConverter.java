package it.eurotn.panjea.anagrafica.rich.converter;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;
import it.eurotn.panjea.rich.converter.NumberWithDecimalConverterContext;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.converter.PanjeaConverter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Comparator;

import javax.swing.text.DefaultFormatterFactory;

import com.jidesoft.converter.ConverterContext;

public class ImportoConverter extends PanjeaConverter {

	private static final String NUMBER_FORMAT = "###,###,###,##0";
	private ValutaAziendaCache valutaCache;
	public static final ConverterContext HTML_CONVERTER_CONTEXT = new ConverterContext("HTML");
	private static DefaultFormatterFactory[] formatterFactory;

	static {
		// Per prestazione creo dei formatter predefiniti
		formatterFactory = new DefaultNumberFormatterFactory[7];
		formatterFactory[0] = new DefaultNumberFormatterFactory(NUMBER_FORMAT, 0, BigDecimal.class);
		formatterFactory[1] = new DefaultNumberFormatterFactory(NUMBER_FORMAT, 1, BigDecimal.class);
		formatterFactory[2] = new DefaultNumberFormatterFactory(NUMBER_FORMAT, 2, BigDecimal.class);
		formatterFactory[3] = new DefaultNumberFormatterFactory(NUMBER_FORMAT, 3, BigDecimal.class);
		formatterFactory[4] = new DefaultNumberFormatterFactory(NUMBER_FORMAT, 4, BigDecimal.class);
		formatterFactory[5] = new DefaultNumberFormatterFactory(NUMBER_FORMAT, 5, BigDecimal.class);
		formatterFactory[6] = new DefaultNumberFormatterFactory(NUMBER_FORMAT, 6, BigDecimal.class);
	}

	@Override
	public Object fromString(String arg0, ConverterContext arg1) {
		return null;
	}

	@Override
	public Class<?> getClasse() {
		return Importo.class;
	}

	@Override
	public Comparator<Object> getComparator() {
		return new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				return ((Importo) o1).getImportoInValuta().compareTo(((Importo) o2).getImportoInValuta());
			}
		};
	}

	/**
	 * @param valutaCache
	 *            The valutaCache to set.
	 */
	public void setValutaCache(ValutaAziendaCache valutaCache) {
		this.valutaCache = valutaCache;
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
	public String toString(Object value, ConverterContext context) {
		String result = "";
		if (value != null && value instanceof Importo && ((Importo) value).getImportoInValuta() != null) {
			Importo importo = (Importo) value;
			if (context != null && HTML_CONVERTER_CONTEXT.getName().equals(context.getName())) {
				StringBuilder sb = new StringBuilder("<HTML><B>");
				sb.append(valutaCache.caricaValutaAziendaCorrente().getSimbolo());
				sb.append(" </B>");
				DefaultFormatterFactory format = formatterFactory[valutaCache.caricaValutaAziendaCorrente()
						.getNumeroDecimali()];
				try {
					sb.append(format.getDisplayFormatter().valueToString(importo.getImportoInValutaAzienda()));
				} catch (ParseException e) {
					sb.append("errore nel formattare l'importo aziendale");
				}
				sb.append("<br><b>Cambio:</>");
				sb.append(importo.getTassoDiCambio());
				result = sb.toString();
			} else {
				String simboloValuta = "";
				int numeroDecimali = 6;

				ValutaAzienda valutaAzienda = valutaCache.caricaValutaAzienda(importo.getCodiceValuta());
				if (valutaAzienda != null) {
					simboloValuta = valutaAzienda.getSimbolo();
				}

				if (context instanceof NumberWithDecimalConverterContext) {
					numeroDecimali = (Integer) ((NumberWithDecimalConverterContext) context).getUserObject();
				} else {
					if (valutaAzienda != null) {
						numeroDecimali = valutaAzienda.getNumeroDecimali();
					}
				}

				String importoString = "";
				try {
					if (numeroDecimali > 6) {
						numeroDecimali = 6;
					}
					importoString = formatterFactory[numeroDecimali].getDisplayFormatter().valueToString(
							importo.getImportoInValuta());
				} catch (ParseException e) {
					importoString = importo.getImportoInValuta().toString();
				}
				result = new StringBuilder(importoString).append(" ").append(simboloValuta).toString();
			}
		}
		return result;
	}
}
