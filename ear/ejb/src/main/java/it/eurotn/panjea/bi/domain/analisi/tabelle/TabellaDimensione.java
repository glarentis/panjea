package it.eurotn.panjea.bi.domain.analisi.tabelle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestisce le dimensioni del cubo.
 *
 * @author giangi
 * @version 1.0, 21/mag/2012
 *
 */
public class TabellaDimensione extends Tabella {
	public class ColumnLink implements Serializable {
		private static final long serialVersionUID = 5209405529140730358L;
		private String columnDimension;
		private String columnFact;

		/**
		 *
		 * Costruttore.
		 *
		 * @param columnDimension
		 *            colonna del link per la dimensione.
		 * @param columnFact
		 *            colonna del link della tabella dei fatti
		 */
		public ColumnLink(final String columnFact, final String columnDimension) {
			super();
			this.columnDimension = columnDimension;
			this.columnFact = columnFact;
		}

		/**
		 * @return Returns the columnDimension.
		 */
		public String getColumnDimension() {
			return columnDimension;
		}

		/**
		 * @return Returns the columnFact.
		 */
		public String getColumnFact() {
			return columnFact;
		}
	}

	private static final long serialVersionUID = -1050805579139630864L;

	private Map<String, ColumnLink> linkColumn;

	/**
	 *
	 * Costruttore.
	 *
	 * @param nome
	 *            nome della tabella
	 * @param prefisso
	 *            prefisso della tabella
	 * @param where
	 *            eventuale where se la tabella è un filtro su una tabella (null se non usato)
	 * @param alias
	 *            alias della tabella (null se non usato).
	 */
	public TabellaDimensione(final String nome, final String prefisso, final String where, final String alias) {
		super(nome, prefisso, where, alias);
		linkColumn = new HashMap<String, ColumnLink>();
	}

	/**
	 *
	 * @param factTable
	 *            tabella dei fatti al quale appartiene il link
	 * @param columnFact
	 *            colonna dei fatti
	 * @param columnDimension
	 *            colonna dimensione.
	 */
	public void addLinkToFactTable(String factTable, String columnFact, String columnDimension) {
		linkColumn.put(factTable, new ColumnLink(columnFact, columnDimension));
	}

	/**
	 *
	 * @param factTable
	 *            tabella dei fatti per il quale recuperare il link
	 * @return colonna link verso la tabella dei fatti factTable.
	 */
	public ColumnLink getLinkToFactTable(String factTable) {
		return linkColumn.get(factTable);
	}
}