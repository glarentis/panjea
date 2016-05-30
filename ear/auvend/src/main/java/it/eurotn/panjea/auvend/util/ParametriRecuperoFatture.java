/**
 * 
 */
package it.eurotn.panjea.auvend.util;

import it.eurotn.panjea.auvend.domain.LetturaFlussoAuVend;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Classe di utilita' per impostare i parametri per l'esecuzione del recupero fatture da terminalino da AuVend.
 * 
 * @author adriano
 * @version 1.0, 28/gen/2009
 * 
 */
public class ParametriRecuperoFatture implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dataRiferimento;

	private List<LetturaFlussoAuVend> lettureFlussoAuVend;

	private boolean effettuaRicerca;

	/**
	 * 
	 */
	public ParametriRecuperoFatture() {
		super();
	}

	/**
	 * @return Returns the dataRiferimento.
	 */
	public Date getDataRiferimento() {
		return dataRiferimento;
	}

	/**
	 * @return Returns the lettureFlussoAuVend.
	 */
	public List<LetturaFlussoAuVend> getLettureFlussoAuVend() {
		return lettureFlussoAuVend;
	}

	/**
	 * @return Returns the effettuaRicerca.
	 */
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @param dataRiferimento
	 *            The dataRiferimento to set.
	 */
	public void setDataRiferimento(Date dataRiferimento) {
		this.dataRiferimento = dataRiferimento;
	}

	/**
	 * @param effettuaRicerca
	 *            The effettuaRicerca to set.
	 */
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param lettureFlussoAuVend
	 *            The lettureFlussoAuVend to set.
	 */
	public void setLettureFlussoAuVend(List<LetturaFlussoAuVend> lettureFlussoAuVend) {
		this.lettureFlussoAuVend = lettureFlussoAuVend;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	@Override
	public String toString() {

		StringBuffer retValue = new StringBuffer();

		retValue.append("ParametriRecuperoFatture[ ").append(super.toString());
		retValue.append(" dataRiferimento = ").append(this.dataRiferimento);
		retValue.append(" effettuaRicerca = ").append(this.effettuaRicerca);
		retValue.append(" lettureFlussoAuVend = ").append(this.lettureFlussoAuVend);
		retValue.append(" ]");

		return retValue.toString();
	}

}
