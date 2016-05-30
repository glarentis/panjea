package it.eurotn.panjea.anagrafica.documenti.util;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;

import java.io.Serializable;

public class ParametriRicercaDocumento implements Serializable {

	private static final long serialVersionUID = 3716220936091752644L;

	private Integer codice;
	private String dataDocumento;

	private Integer idContratto;

	private EntitaLite entita;
	private Documento documento;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaDocumento() {
		super();
	}

	/**
	 * @return the codice
	 */
	public Integer getCodice() {
		return codice;
	}

	/**
	 * @return the dataDocumento
	 */
	public String getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return the documento
	 */
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return the entita
	 */
	public EntitaLite getEntita() {
		return entita;
	}

	/**
	 * @return the idContratto
	 */
	public Integer getIdContratto() {
		return idContratto;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(Integer codice) {
		this.codice = codice;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(String dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param documento
	 *            the documento to set
	 */
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	/**
	 * @param entita
	 *            the entita to set
	 */
	public void setEntita(EntitaLite entita) {
		this.entita = entita;
	}

	/**
	 * @param idContratto
	 *            the idContratto to set
	 */
	public void setIdContratto(Integer idContratto) {
		this.idContratto = idContratto;
	}

}
