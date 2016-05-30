/**
 *
 */
package it.eurotn.panjea.aton.domain.wrapper;

import java.util.Map;

/**
 * @author leonardo
 */
public class CodiciEsportazioneContratti {

	private Map<Integer, String> categorieCommercialiArticolo;

	private Map<Integer, String> categorieSedeMagazzino;

	private Map<Integer, Integer> clienti;

	private Map<Integer, String> codiciArticolo;

	private Map<Integer, Integer> codiciEntitaSedeMagazzino;

	private Map<Integer, String> codiciSedeEntitaSedeMagazzino;

	/**
	 * @return the categorieCommercialiArticolo
	 */
	public Map<Integer, String> getCategorieCommercialiArticolo() {
		return categorieCommercialiArticolo;
	}

	/**
	 * @return the categorieSedeMagazzino
	 */
	public Map<Integer, String> getCategorieSedeMagazzino() {
		return categorieSedeMagazzino;
	}

	/**
	 * @return the clienti
	 */
	public Map<Integer, Integer> getClienti() {
		return clienti;
	}

	/**
	 * @return the codiciArticolo
	 */
	public Map<Integer, String> getCodiciArticolo() {
		return codiciArticolo;
	}

	/**
	 * @return the codiciEntitaSedeMagazzino
	 */
	public Map<Integer, Integer> getCodiciEntitaSedeMagazzino() {
		return codiciEntitaSedeMagazzino;
	}

	/**
	 * @return the codiciSedeEntitaSedeMagazzino
	 */
	public Map<Integer, String> getCodiciSedeEntitaSedeMagazzino() {
		return codiciSedeEntitaSedeMagazzino;
	}

	/**
	 * @param categorieCommercialiArticolo
	 *            the categorieCommercialiArticolo to set
	 */
	public void setCategorieCommercialiArticolo(Map<Integer, String> categorieCommercialiArticolo) {
		this.categorieCommercialiArticolo = categorieCommercialiArticolo;
	}

	/**
	 * @param categorieSedeMagazzino
	 *            the categorieSedeMagazzino to set
	 */
	public void setCategorieSedeMagazzino(Map<Integer, String> categorieSedeMagazzino) {
		this.categorieSedeMagazzino = categorieSedeMagazzino;
	}

	/**
	 * @param clienti
	 *            the clienti to set
	 */
	public void setClienti(Map<Integer, Integer> clienti) {
		this.clienti = clienti;
	}

	/**
	 * @param codiciArticolo
	 *            the codiciArticolo to set
	 */
	public void setCodiciArticolo(Map<Integer, String> codiciArticolo) {
		this.codiciArticolo = codiciArticolo;
	}

	/**
	 * @param codiciEntitaSedeMagazzino
	 *            the codiciEntitaSedeMagazzino to set
	 */
	public void setCodiciEntitaSedeMagazzino(Map<Integer, Integer> codiciEntitaSedeMagazzino) {
		this.codiciEntitaSedeMagazzino = codiciEntitaSedeMagazzino;
	}

	/**
	 * @param codiciSedeEntitaSedeMagazzino
	 *            the codiciSedeEntitaSedeMagazzino to set
	 */
	public void setCodiciSedeEntitaSedeMagazzino(Map<Integer, String> codiciSedeEntitaSedeMagazzino) {
		this.codiciSedeEntitaSedeMagazzino = codiciSedeEntitaSedeMagazzino;
	}

}
