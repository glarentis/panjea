/**
 * 
 */
package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.tesoreria.domain.Effetto.StatoEffetto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Parametri per la ricerca degli effetti.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 */
public class ParametriRicercaEffetti implements Serializable {

	private static final long serialVersionUID = 6485882565233416093L;

	// flag utilizzato per sapere se effettuare la ricerca o no
	private boolean effettuaRicerca = false;

	private Date daDataValuta = null;
	private Date aDataValuta = null;
	private RapportoBancarioAzienda rapportoBancarioAzienda = null;
	private CodiceDocumento numeroDocumento = null;
	private Importo daImporto = null;
	private Importo aImporto = null;
	private EntitaLite entita = null;
	private List<StatoEffetto> statiEffetto = null;

	private boolean escludiEffettiAccreditati;

	/**
	 * Costruttore di default.
	 */
	public ParametriRicercaEffetti() {
		initailize();
	}

	/**
	 * @return the aDataValuta
	 */
	public Date getADataValuta() {
		return aDataValuta;
	}

	/**
	 * @return the aImporto
	 */
	public Importo getAImporto() {
		return aImporto;
	}

	/**
	 * @return the daDataValuta
	 */
	public Date getDaDataValuta() {
		return daDataValuta;
	}

	/**
	 * @return the daImporto
	 */
	public Importo getDaImporto() {
		return daImporto;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the numeroDocumento
	 */
	public CodiceDocumento getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @return the rapportoBancarioAzienda
	 */
	public RapportoBancarioAzienda getRapportoBancarioAzienda() {
		return rapportoBancarioAzienda;
	}

	/**
	 * @return the statiEffetto
	 */
	public List<StatoEffetto> getStatiEffetto() {
		return statiEffetto;
	}

	/**
	 * Init delle propriet� che non devono essere null.
	 */
	private void initailize() {
		removeNullValue();
		this.statiEffetto.add(StatoEffetto.PRESENTATO);
		this.escludiEffettiAccreditati = false;
		this.numeroDocumento = new CodiceDocumento();
	}

	/**
	 * @return the effettuaRicerca
	 */
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @return the escludiEffettiAccreditati
	 */
	public boolean isEscludiEffettiAccreditati() {
		return escludiEffettiAccreditati;
	}

	/**
	 * Istanzia gli oggetti a null del server per il client.
	 */
	private void removeNullValue() {
		this.rapportoBancarioAzienda = new RapportoBancarioAzienda();
		this.daImporto = new Importo();
		this.aImporto = new Importo();
		this.entita = new ClienteLite();
		this.statiEffetto = new ArrayList<StatoEffetto>();

	}

	/**
	 * @param paramADataValuta
	 *            the aDataValuta to set
	 */
	public void setADataValuta(Date paramADataValuta) {
		this.aDataValuta = paramADataValuta;
	}

	/**
	 * @param paramAImporto
	 *            the aImporto to set
	 */
	public void setAImporto(Importo paramAImporto) {
		this.aImporto = paramAImporto;
	}

	/**
	 * @param daDataValuta
	 *            the daDataValuta to set
	 */
	public void setDaDataValuta(Date daDataValuta) {
		this.daDataValuta = daDataValuta;
	}

	/**
	 * @param daImporto
	 *            the daImporto to set
	 */
	public void setDaImporto(Importo daImporto) {
		this.daImporto = daImporto;
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
	 * @param escludiEffettiAccreditati
	 *            the escludiEffettiAccreditati to set
	 */
	public void setEscludiEffettiAccreditati(boolean escludiEffettiAccreditati) {
		this.escludiEffettiAccreditati = escludiEffettiAccreditati;
	}

	/**
	 * @param numeroDocumento
	 *            the numeroDocumento to set
	 */
	public void setNumeroDocumento(CodiceDocumento numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @param rapportoBancarioAzienda
	 *            the rapportoBancarioAzienda to set
	 */
	public void setRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
		this.rapportoBancarioAzienda = rapportoBancarioAzienda;
	}

	/**
	 * @param statiEffetto
	 *            the statiEffetto to set
	 */
	public void setStatiEffetto(List<StatoEffetto> statiEffetto) {
		this.statiEffetto = statiEffetto;
	}

}
