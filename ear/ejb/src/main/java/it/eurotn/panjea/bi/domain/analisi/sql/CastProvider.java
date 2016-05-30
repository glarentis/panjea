package it.eurotn.panjea.bi.domain.analisi.sql;

import it.eurotn.panjea.bi.domain.analisi.tabelle.Colonna;

import java.util.HashMap;
import java.util.Map;

public class CastProvider {

	private final Map<Class<?>, String> castMapping = new HashMap<Class<?>, String>();

	{
		castMapping.put(Character.class, " char ");
		// castMapping.put(int.class, " UNSIGNED ");
	}

	/**
	 *
	 * @param sb
	 *            stringBuilder
	 * @param colonna
	 *            colonna da castare
	 * @return sb con append della select con un evantuale cast.
	 */
	public StringBuilder castString(StringBuilder sb, Colonna colonna) {
		String replace = castMapping.get(colonna.getColumnClass());
		if (replace != null) {
			sb.append(" cast(");
		}
		sb.append(colonna.getNome());
		if (replace != null) {
			sb.append(" as ");
			sb.append(replace);
			sb.append(" ) ");
		}
		return sb;
	}

}
