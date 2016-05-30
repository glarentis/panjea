package it.eurotn.panjea.aton.domain.wrapper;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;

public class ArticoloAton {

	public static final String ATTRIBUTO_CATEGORIA_ATON = "cataton";
	public static final String ATTRIBUTO_NR_PEZZI = "nrpezzi";

	private Articolo articolo;

	private Double giacenza;

	/**
	 * Costruttore.
	 * 
	 * @param articolo
	 *            articolo
	 */
	public ArticoloAton(final Articolo articolo) {
		super();
		this.articolo = articolo;
	}

	/**
	 * Costruttore.
	 * 
	 * @param articolo
	 *            articolo
	 * @param giacenza
	 *            giacenza
	 */
	public ArticoloAton(final Articolo articolo, final Double giacenza) {
		super();
		this.articolo = articolo;
		this.giacenza = giacenza;
	}

	/**
	 * @return Returns the articolo.
	 */
	public Articolo getArticolo() {
		return articolo;
	}

	/**
	 * @return the attributo categoria aton
	 */
	public String getCategoriaAton() {
		String categoriaAton = "";
		AttributoArticolo attributo = articolo.getAttributo(ATTRIBUTO_CATEGORIA_ATON);
		if (attributo != null) {
			categoriaAton = attributo.getValore();
		}
		return categoriaAton;
	}

	/**
	 * @return Entita abituale o una istanza vuota se non c'è alcuna entità abituale
	 */
	public EntitaLite getFornitoreAbituale() {
		EntitaLite entita = new FornitoreLite();
		for (CodiceArticoloEntita codiceArticoloEntita : getArticolo().getCodiciArticoloEntita()) {
			if (codiceArticoloEntita.isEntitaPrincipale() && codiceArticoloEntita.getEntita().getTipo().equals("F")) {
				entita = codiceArticoloEntita.getEntita();
			}
		}
		return entita;
	}

	/**
	 * @return Returns the giacenza.
	 */
	public Double getGiacenza() {
		return giacenza;
	}

	/**
	 * @return il valore dell'attributo con codice nrpezzi
	 */
	public String getNumeroPezzi() {
		String numeroPezzi = "";
		AttributoArticolo attributo = articolo.getAttributo(ATTRIBUTO_NR_PEZZI);
		if (attributo != null) {
			numeroPezzi = attributo.getValore();
		}
		return numeroPezzi;
	}

	/**
	 * @param articolo
	 *            The articolo to set.
	 */
	public void setArticolo(Articolo articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param categoriaAton
	 *            fake method for BeanIO
	 */
	public void setCategoriaAton(String categoriaAton) {

	}

	/**
	 * @param fornitoreAbituale
	 *            fake method for BeanIO
	 */
	public void setFornitoreAbituale(EntitaLite fornitoreAbituale) {

	}

	/**
	 * @param giacenza
	 *            The giacenza to set.
	 */
	public void setGiacenza(Double giacenza) {
		this.giacenza = giacenza;
	}

	/**
	 * @param numeroPezzi
	 *            metodo vuoto, serve per beanio
	 */
	public void setNumeroPezzi(String numeroPezzi) {

	}

}
