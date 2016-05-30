package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeAnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;

import java.io.Serializable;
import java.math.BigDecimal;

public class FatturatoDTO implements Serializable {

	private static final long serialVersionUID = 6588590200334407411L;

	private EntitaLite entita;
	private SedeEntitaLite sedeEntita;

	private BigDecimal totaleImponibile;
	private BigDecimal totaleIVA;

	private Integer numeroDocumenti;

	{
		// inizializzo un cliente tanto per ora questa classe viene utilizzata solo per il report e non mi serve sapere
		// il tipo
		this.entita = new ClienteLite();
		this.sedeEntita = new SedeEntitaLite();
		this.sedeEntita.setSede(new SedeAnagraficaLite());
	}

	/**
	 * @return Returns the entita.
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return Returns the numeroDocumenti.
	 */
	public Integer getNumeroDocumenti() {
		return numeroDocumenti;
	}

	/**
	 * @return the sedeEntita
	 */
	public SedeEntitaLite getSedeEntita() {
		return sedeEntita;
	}

	/**
	 * @return Returns the totale.
	 */
	public BigDecimal getTotale() {
		return totaleImponibile.add(totaleIVA);
	}

	/**
	 * @return Returns the totaleImponibile.
	 */
	public BigDecimal getTotaleImponibile() {
		return totaleImponibile;
	}

	/**
	 * @return Returns the totaleIVA.
	 */
	public BigDecimal getTotaleIVA() {
		return totaleIVA;
	}

	/**
	 * @return se è una riga di dettaglio sede o una normale riga di entità
	 */
	public boolean isDettaglioSede() {
		return this.sedeEntita.getId() != null;
	}

	/**
	 * @param codiceEntita
	 *            The codiceEntita to set.
	 */
	public void setEntitaCodice(Integer codiceEntita) {
		this.entita.setCodice(codiceEntita);
	}

	/**
	 * @param denominazioneEntita
	 *            The denominazioneEntita to set.
	 */
	public void setEntitaDenominazione(String denominazioneEntita) {
		this.entita.getAnagrafica().setDenominazione(denominazioneEntita);
	}

	/**
	 * @param idEntita
	 *            The idEntita to set.
	 */
	public void setEntitaId(Integer idEntita) {
		this.entita.setId(idEntita);
	}

	/**
	 * @param numeroDocumenti
	 *            The numeroDocumenti to set.
	 */
	public void setNumeroDocumenti(Integer numeroDocumenti) {
		this.numeroDocumenti = numeroDocumenti;
	}

	/**
	 * @param sedeEntitaCodice
	 *            The sedeEntitaCodice to set.
	 */
	public void setSedeEntitaCodice(String sedeEntitaCodice) {
		this.sedeEntita.setCodice(sedeEntitaCodice);
	}

	/**
	 * @param sedeEntitaDescrizione
	 *            The sedeEntitaDescrizione to set.
	 */
	public void setSedeEntitaDescrizione(String sedeEntitaDescrizione) {
		this.sedeEntita.getSede().setDescrizione(sedeEntitaDescrizione);
	}

	/**
	 * @param sedeEntitaId
	 *            The sedeEntitaId to set.
	 */
	public void setSedeEntitaId(Integer sedeEntitaId) {
		this.sedeEntita.setId(sedeEntitaId);
	}

	/**
	 * @param sedeEntitaIndirizzo
	 *            The sedeEntitaIndirizzo to set.
	 */
	public void setSedeEntitaIndirizzo(String sedeEntitaIndirizzo) {
		this.sedeEntita.getSede().setIndirizzo(sedeEntitaIndirizzo);
	}

	/**
	 * @param totaleImponibile
	 *            The totaleImponibile to set.
	 */
	public void setTotaleImponibile(BigDecimal totaleImponibile) {
		this.totaleImponibile = totaleImponibile;
	}

	/**
	 * @param totaleIVA
	 *            The totaleIVA to set.
	 */
	public void setTotaleIVA(BigDecimal totaleIVA) {
		this.totaleIVA = totaleIVA;
	}

}
