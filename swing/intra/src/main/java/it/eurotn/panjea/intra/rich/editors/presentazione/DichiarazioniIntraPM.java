package it.eurotn.panjea.intra.rich.editors.presentazione;

import it.eurotn.panjea.intra.domain.DichiarazioneIntra;

public class DichiarazioniIntraPM {
	private Boolean selezionata;
	private DichiarazioneIntra dichiarazioneIntra;

	/**
	 * 
	 * Costruttore.
	 * 
	 */
	public DichiarazioniIntraPM() {
		selezionata = false;
	}

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param dichiarazioneIntra
	 *            dichiarazione "wrappata"
	 */
	public DichiarazioniIntraPM(final DichiarazioneIntra dichiarazioneIntra) {
		this.dichiarazioneIntra = dichiarazioneIntra;
		this.selezionata = false;
	}

	/**
	 * @return Returns the dichiarazioneIntra.
	 */
	public DichiarazioneIntra getDichiarazioneIntra() {
		return dichiarazioneIntra;
	}

	/**
	 * @return Returns the selezionata.
	 */
	public Boolean getSelezionata() {
		return selezionata;
	}

	/**
	 * @param selezionata
	 *            The selezionata to set.
	 */
	public void setSelezionata(Boolean selezionata) {
		this.selezionata = selezionata;
	}

}
