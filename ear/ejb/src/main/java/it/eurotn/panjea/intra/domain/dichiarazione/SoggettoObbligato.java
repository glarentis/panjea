package it.eurotn.panjea.intra.domain.dichiarazione;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SoggettoObbligato implements Serializable {
	private static final long serialVersionUID = -2326276016818267639L;
	@Column(length = 20)
	private String partitaIva;
	@Column(length = 30)
	private String cognome;
	@Column(length = 30)
	private String nome;
	@Column(length = 100)
	private String soggetto;
	private boolean elenchiPrecedenti;
	private boolean cessazioneAttivita;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public SoggettoObbligato() {
		elenchiPrecedenti = false;
		cessazioneAttivita = false;
	}

	/**
	 * @return Returns the cognome.
	 */
	public String getCognome() {
		return cognome;
	}

	/**
	 * @return Returns the nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @return Returns the partitaIva.
	 */
	public String getPartitaIva() {
		return partitaIva;
	}

	/**
	 * @return Returns the soggetto.
	 */
	public String getSoggetto() {
		return soggetto;
	}

	/**
	 * @return Returns the cessazioneAttivita.
	 */
	public boolean isCessazioneAttivita() {
		return cessazioneAttivita;
	}

	/**
	 * @return Returns the elenchiPrecedenti.
	 */
	public boolean isElenchiPrecedenti() {
		return elenchiPrecedenti;
	}

	/**
	 * @param cessazioneAttivita
	 *            The cessazioneAttivita to set.
	 */
	public void setCessazioneAttivita(boolean cessazioneAttivita) {
		this.cessazioneAttivita = cessazioneAttivita;
	}

	/**
	 * @param cognome
	 *            The cognome to set.
	 */
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	/**
	 * @param elenchiPrecedenti
	 *            The elenchiPrecedenti to set.
	 */
	public void setElenchiPrecedenti(boolean elenchiPrecedenti) {
		this.elenchiPrecedenti = elenchiPrecedenti;
	}

	/**
	 * @param nome
	 *            The nome to set.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @param partitaIva
	 *            The partitaIva to set.
	 */
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	/**
	 * @param soggetto
	 *            The soggetto to set.
	 */
	public void setSoggetto(String soggetto) {
		this.soggetto = soggetto;
	}
}
