package it.eurotn.panjea.mrp.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * Identifica un conflitto durante il calcolo dell'mrp per utente.
 *
 * @author giangi
 * @version 1.0, 11/mag/2015
 *
 */
public class ConflittoMrp implements Serializable {
	private static final long serialVersionUID = -3094599708694833382L;
	private String codiceDistinta;
	private String descrizioneDistinta;
	private String user;
	private BigInteger timeStamp;

	private int idArticolo;

	private int numeroRiga;

	private int entitaCodice;

	private String entitaDenominazione;

	private Date documentoData;
	private String documentoCodice;

	/**
	 * @return Returns the codiceDistinta.
	 */
	public String getCodiceDistinta() {
		return codiceDistinta;
	}

	/**
	 * @return Returns the descrizioneDistinta.
	 */
	public String getDescrizioneDistinta() {
		return descrizioneDistinta;
	}

	/**
	 * @return Returns the documentoCodice.
	 */
	public String getDocumentoCodice() {
		return documentoCodice;
	}

	/**
	 * @return Returns the documentoData.
	 */
	public Date getDocumentoData() {
		return documentoData;
	}

	/**
	 * @return Returns the entitaCodice.
	 */
	public int getEntitaCodice() {
		return entitaCodice;
	}

	/**
	 * @return Returns the entitaDenominazione.
	 */
	public String getEntitaDenominazione() {
		return entitaDenominazione;
	}

	/**
	 * @return Returns the idArticolo.
	 */
	public int getIdArticolo() {
		return idArticolo;
	}

	/**
	 * @return Returns the numeroRiga.
	 */
	public int getNumeroRiga() {
		return numeroRiga;
	}

	/**
	 * @return Returns the timeStamp.
	 */
	public BigInteger getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param codiceDistinta
	 *            The codiceDistinta to set.
	 */
	public void setCodiceDistinta(String codiceDistinta) {
		this.codiceDistinta = codiceDistinta;
	}

	/**
	 * @param descrizioneDistinta
	 *            The descrizioneDistinta to set.
	 */
	public void setDescrizioneDistinta(String descrizioneDistinta) {
		this.descrizioneDistinta = descrizioneDistinta;
	}

	/**
	 * @param documentoCodice
	 *            The documentoCodice to set.
	 */
	public void setDocumentoCodice(String documentoCodice) {
		this.documentoCodice = documentoCodice;
	}

	/**
	 * @param documentoData
	 *            The documentoData to set.
	 */
	public void setDocumentoData(Date documentoData) {
		this.documentoData = documentoData;
	}

	/**
	 * @param entitaCodice
	 *            The entitaCodice to set.
	 */
	public void setEntitaCodice(int entitaCodice) {
		this.entitaCodice = entitaCodice;
	}

	/**
	 * @param entitaDenominazione
	 *            The entitaDenominazione to set.
	 */
	public void setEntitaDenominazione(String entitaDenominazione) {
		this.entitaDenominazione = entitaDenominazione;
	}

	/**
	 * @param idArticolo
	 *            The idArticolo to set.
	 */
	public void setIdArticolo(int idArticolo) {
		this.idArticolo = idArticolo;
	}

	/**
	 * @param numeroRiga
	 *            The numeroRiga to set.
	 */
	public void setNumeroRiga(int numeroRiga) {
		this.numeroRiga = numeroRiga;
	}

	/**
	 * @param timeStamp
	 *            The timeStamp to set.
	 */
	public void setTimeStamp(BigInteger timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @param user
	 *            The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}

}
