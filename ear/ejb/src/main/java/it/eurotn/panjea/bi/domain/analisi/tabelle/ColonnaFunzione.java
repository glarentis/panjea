package it.eurotn.panjea.bi.domain.analisi.tabelle;

/**
 * Costruisce una colonna data da una funzione sql.
 *
 * @author giangi
 * @version 1.0, 08/mag/2013
 *
 */
public class ColonnaFunzione extends Colonna {
	private static final long serialVersionUID = 683732156546652930L;

	/**
	 *
	 * Costruttore.
	 *
	 * @param funzione
	 *            funzione di calcolo. Per inserire l'alias della tabella dove è legata la funzione inserire
	 *            $aliasTabella$
	 * @param columnClass
	 *            classe della colonna
	 * @param tabella
	 *            tabella proprietaria della colonna
	 * @param title
	 *            della tabella
	 */
	public ColonnaFunzione(final String funzione, final Class<?> columnClass, final Tabella tabella, final String title) {
		super(funzione, columnClass, tabella, title);
		if (title == null) {
			throw new IllegalArgumentException("L'alias per una colonna funzione è obbligatorio");
		}
		this.nome = this.nome.replaceAll("\\$aliasTabella\\$", tabella.getAlias());
	}

	/**
	 *
	 * Costruttore.
	 *
	 * @param funzione
	 *            funzione di calcolo
	 * @param columnClass
	 *            classe della colonna
	 * @param tabella
	 *            tabella proprietaria della colonna
	 * @param title
	 *            della tabella @param isSeparatorVisible separatore Visibile
	 *
	 */
	public ColonnaFunzione(final String funzione, final Class<?> columnClass, final Tabella tabella,
			final String title, final boolean isSeparatorVisible) {
		super(funzione, columnClass, tabella, title, isSeparatorVisible);
		if (title == null) {
			throw new IllegalArgumentException("L'alias per una colonna funzione è obbligatorio");
		}
	}

	@Override
	public String getSql() {
		return nome;
	}

}
