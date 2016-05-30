package it.eurotn.panjea.anagrafica.util.parametriricerca;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.partite.domain.CategoriaRata;

import java.io.Serializable;
import java.util.List;

public class ParametriRicercaEntita implements Serializable {

	public enum FieldSearch {
		CODICE, DESCRIZIONE, PARTITAIVA, CODICEFISCALE, NONE
	}

	private static final long serialVersionUID = -6905056599744180194L;

	private TipoEntita tipoEntita;

	private String codice;

	private String descrizione;

	private boolean includiEntitaPotenziali;

	private String partitaIva;

	private FieldSearch fieldSearch;

	private String codiceFiscale;

	private Boolean abilitato = null;

	private List<CategoriaRata> categorieRate = null;

	private List<TipoEntita> tipiEntitaList = null;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaEntita() {
		super();
		this.fieldSearch = FieldSearch.CODICE;
		this.includiEntitaPotenziali = Boolean.FALSE;
	}

	/**
	 * @return the abilitato
	 */
	public Boolean getAbilitato() {
		return abilitato;
	}

	/**
	 * @return the categorieRate
	 */
	public List<CategoriaRata> getCategorieRate() {
		return categorieRate;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return Returns the codiceFiscale.
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the fieldSearch
	 */
	public FieldSearch getFieldSearch() {
		return fieldSearch;
	}

	/**
	 * @return the partitaIva
	 */
	public String getPartitaIva() {
		return partitaIva;
	}

	/**
	 * @return the tipiEntitaList
	 */
	public List<TipoEntita> getTipiEntitaList() {
		return tipiEntitaList;
	}

	/**
	 * @return the tipoEntita
	 */
	public TipoEntita getTipoEntita() {
		return tipoEntita;
	}

	/**
	 * @return the includiEntitaPotenziali
	 */
	public boolean isIncludiEntitaPotenziali() {
		return includiEntitaPotenziali;
	}

	/**
	 * @param abilitato
	 *            the abilitato to set
	 */
	public void setAbilitato(Boolean abilitato) {
		this.abilitato = abilitato;
	}

	/**
	 * @param categorieRate
	 *            the categorieRate to set
	 */
	public void setCategorieRate(List<CategoriaRata> categorieRate) {
		this.categorieRate = categorieRate;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceFiscale
	 *            the codiceFiscale to set
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param fieldSearch
	 *            the fieldSearch to set
	 */
	public void setFieldSearch(FieldSearch fieldSearch) {
		this.fieldSearch = fieldSearch;
	}

	/**
	 * @param includiEntitaPotenziali
	 *            the includiEntitaPotenziali to set
	 */
	public void setIncludiEntitaPotenziali(boolean includiEntitaPotenziali) {
		this.includiEntitaPotenziali = includiEntitaPotenziali;
	}

	/**
	 * @param partitaIva
	 *            the partitaIva to set
	 */
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	/**
	 * @param tipiEntitaList
	 *            the tipiEntitaList to set
	 */
	public void setTipiEntitaList(List<TipoEntita> tipiEntitaList) {
		this.tipiEntitaList = tipiEntitaList;
	}

	/**
	 * @param tipoEntita
	 *            the tipoEntita to set
	 */
	public void setTipoEntita(TipoEntita tipoEntita) {
		this.tipoEntita = tipoEntita;
	}

}
