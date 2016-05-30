package it.eurotn.panjea.bi.rich.editors.datawarehouse.model.filter;

import it.eurotn.panjea.exceptions.PanjeaRuntimeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jidesoft.filter.BetweenFilter;
import com.jidesoft.filter.Filter;
import com.jidesoft.filter.SqlFilterSupport;

public final class FilterSqlSupport {
	/**
	 * 
	 * @param data
	 *            data da formattare in formato dd-MMM-yyyy
	 * @return data formattata in formato yyyy-MM-dd
	 */
	private static String formattaData(String data) {
		SimpleDateFormat pivotFormat = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dataTemp = null;
		try {
			dataTemp = pivotFormat.parse(data);
		} catch (ParseException e) {
			throw new PanjeaRuntimeException(e);
		}
		return " '" + sqlFormat.format(dataTemp) + "' ";
	}

	/**
	 * Restituisce l'Sql basato sul filtro passato.
	 * 
	 * @param filtro
	 *            filtro impostato
	 * @return stringa formattata come richiesto da Sql
	 */
	public static String getSql(Filter filtro) {
		String sql = "";
		if (filtro instanceof SqlFilterSupport) {
			SqlFilterSupport sqlFilter = (SqlFilterSupport) filtro;
			sql = sqlFilter.getOperator();
			String data = "";
			if (filtro instanceof BetweenFilter) {
				// devo trovare le due date e formattarle
				Object[] date = new Object[2];
				date[0] = ((BetweenFilter) filtro).getValue1();
				date[1] = ((BetweenFilter) filtro).getValue2();
				data = formattaData(date[0].toString()) + " and " + formattaData(date[1].toString());
			} else {
				data = formattaData(filtro.getName().split("\\|")[1]);
			}
			sql += data;
			return sql;
		}
		return "Not available";
	}

	/**
	 * Costruttore privato. <br>
	 * Non devo averne uno pubblico perchè devo usare solamente i sui metodi statici
	 */
	private FilterSqlSupport() {

	}
}