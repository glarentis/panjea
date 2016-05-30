/**
 *
 */
package it.eurotn.panjea.offerte.util;

import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.offerte.domain.TipoAreaOfferta;
import it.eurotn.panjea.sicurezza.domain.Utente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Leonardo
 */
public class ParametriRicercaOfferte implements Serializable {

	private static final long serialVersionUID = 3382249647479898760L;

	// flag utilizzato per sapere se effettuare la ricerca o no
	private boolean effettuaRicerca = false;

	private Date daDataDocumento = null;

	private Date aDataDocumento = null;

	private Integer daNumeroDocumento = null;

	private Integer aNumeroDocumento = null;

	private Utente utente = null;
	private EntitaLite entita = null;

	private TipoAreaOfferta tipoAreaOfferta = null;
	private ArticoloLite articoloLite = null;
	private List<Boolean> accettata = null;

	{
		this.accettata = new ArrayList<Boolean>();
		this.accettata.add(Boolean.TRUE);
		this.accettata.add(Boolean.FALSE);
	}

	/**
	 * Costruttore.
	 */
	public ParametriRicercaOfferte() {
		super();
	}

	/**
	 * @return the accettata
	 */
	public List<Boolean> getAccettata() {
		return accettata;
	}

	/**
	 * @return the aDataDocumento
	 */
	public Date getADataDocumento() {
		return aDataDocumento;
	}

	/**
	 * @return the aNumeroDocumento
	 */
	public Integer getANumeroDocumento() {
		return aNumeroDocumento;
	}

	/**
	 * @return the articoloLite
	 */
	public ArticoloLite getArticoloLite() {
		return articoloLite;
	}

	/**
	 * @return the daDataDocumento
	 */
	public Date getDaDataDocumento() {
		return daDataDocumento;
	}

	/**
	 * @return the daNumeroDocumento
	 */
	public Integer getDaNumeroDocumento() {
		return daNumeroDocumento;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the tipoAreaOfferta
	 */
	public TipoAreaOfferta getTipoAreaOfferta() {
		return tipoAreaOfferta;
	}

	/**
	 * @return the utente
	 */
	public Utente getUtente() {
		return utente;
	}

	/**
	 * @return the effettuaRicerca
	 */
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @param accettata
	 *            the accettata to set
	 */
	public void setAccettata(List<Boolean> accettata) {
		this.accettata = accettata;
	}

	/**
	 * @param dataDocumento
	 *            the aDataDocumento to set
	 */
	public void setADataDocumento(Date dataDocumento) {
		aDataDocumento = dataDocumento;
	}

	/**
	 * @param numeroDocumento
	 *            the aNumeroDocumento to set
	 */
	public void setANumeroDocumento(Integer numeroDocumento) {
		aNumeroDocumento = numeroDocumento;
	}

	/**
	 * @param articoloLite
	 *            the articoloLite to set
	 */
	public void setArticoloLite(ArticoloLite articoloLite) {
		this.articoloLite = articoloLite;
	}

	/**
	 * @param daDataDocumento
	 *            the daDataDocumento to set
	 */
	public void setDaDataDocumento(Date daDataDocumento) {
		this.daDataDocumento = daDataDocumento;
	}

	/**
	 * @param daNumeroDocumento
	 *            the daNumeroDocumento to set
	 */
	public void setDaNumeroDocumento(Integer daNumeroDocumento) {
		this.daNumeroDocumento = daNumeroDocumento;
	}

	/**
	 * @param effettuaRicerca
	 *            the effettuaRicerca to set
	 */
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param tipoAreaOfferta
	 *            the tipoAreaOfferta to set
	 */
	public void setTipoAreaOfferta(TipoAreaOfferta tipoAreaOfferta) {
		this.tipoAreaOfferta = tipoAreaOfferta;
	}

	/**
	 * @param utente
	 *            the utente to set
	 */
	public void setUtente(Utente utente) {
		this.utente = utente;
	}

}
