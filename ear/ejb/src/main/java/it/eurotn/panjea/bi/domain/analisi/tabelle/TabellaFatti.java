package it.eurotn.panjea.bi.domain.analisi.tabelle;

public class TabellaFatti extends Tabella {
	private static final long serialVersionUID = -609485922250208919L;

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
	public TabellaFatti(final String nome, final String prefisso, final String where, final String alias) {
		super(nome, prefisso, where, alias);
	}
}
