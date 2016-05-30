package it.eurotn.panjea.magazzino.service.exception;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;

import java.util.Set;

public class DocumentiExporterException extends Exception {

	private static final long serialVersionUID = 4282768415964267026L;

	private Set<SedeEntita> sediSenzaCodice = null;

	private String nomeTipoEsportazione;

	/**
	 * Costruttore.
	 * 
	 * @param sediSenzaCodice
	 *            sedi entità che non hanno un codice settato
	 * @param nomeTipoEsportazione
	 *            nomeTipoEsportazione che non ha il codice settato
	 */
	public DocumentiExporterException(final Set<SedeEntita> sediSenzaCodice, final String nomeTipoEsportazione) {
		super();
		this.sediSenzaCodice = sediSenzaCodice;
		this.nomeTipoEsportazione = nomeTipoEsportazione;
	}

	/**
	 * @return Returns the nomeTipoEsportazione.
	 */
	public String getNomeTipoEsportazione() {
		return nomeTipoEsportazione;
	}

	/**
	 * @return the sediSenzaCodice
	 */
	public Set<SedeEntita> getSediSenzaCodice() {
		return sediSenzaCodice;
	}
}
