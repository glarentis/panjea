package it.eurotn.panjea.magazzino.exception;

/**
 *
 * Rilanciata se una tabella delle dimensioni non ha un link sulla tabella dei fatti. <br/>
 * Esempio: articoli->documenti ordine
 *
 * @author giangi
 * @version 1.0, 10/apr/2014
 *
 */
public class DataWarehouseVincoloException extends DataWarehouseException {
	private static final long serialVersionUID = -6485433341694629499L;

	private String tabellaFatti;
	private String tabellaDimensione;

	/**
	 *
	 * @param tabellaFatti
	 *            tabella dei fatti senza link
	 * @param tabellaDimensione
	 *            tabella dimensioni senza link
	 */
	public DataWarehouseVincoloException(final String tabellaFatti, final String tabellaDimensione) {
		super();
		this.tabellaFatti = tabellaFatti;
		this.tabellaDimensione = tabellaDimensione;
	}

	@Override
	public String getMessage() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("La dimensione ");
		stringBuilder.append(tabellaDimensione);
		stringBuilder.append(" non può essere utilizzata assieme alla tabella dei fatti ");
		stringBuilder.append(tabellaFatti);
		return stringBuilder.toString();
	}

}
