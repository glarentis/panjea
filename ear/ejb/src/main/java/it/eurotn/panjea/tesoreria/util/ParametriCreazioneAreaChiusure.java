/**
 *
 */
package it.eurotn.panjea.tesoreria.util;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Gestisce i parametri per la creazione dell'area chiusure (AreaEffetti e AreaPagamenti).
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 */
public class ParametriCreazioneAreaChiusure implements Serializable {

	private static final long serialVersionUID = 2098960428964046587L;

	private Date dataDocumento;

	private TipoAreaPartita tipoAreaPartita;

	private Date dataEffetto; // se ho raggruppato per date scadenze delle rate diverse chiedo la data dell'effetto

	private Date dataScadenzaAnticipoFatture;

	private boolean generaSingoloEffetto;

	/**
	 * Il tipo partita mi serve sul client per filtrare i tipiAreaPartita, non considerare questo parametro per la
	 * generazione del/i documento/i.
	 */
	private TipoPartita tipoPartita;

	private RapportoBancarioAzienda rapportoBancarioAzienda;

	private BigDecimal speseIncasso;

	/**
	 * Per il TipoAreaPartita con TipoOperazione GESTIONE_ASSEGNO.
	 */
	private String numeroAssegno;

	/**
	 * Per il TipoAreaPartita con TipoOperazione GESTIONE_ASSEGNO.
	 */
	private String abi;

	/**
	 * Per il TipoAreaPartita con TipoOperazione GESTIONE_ASSEGNO.
	 */
	private String cab;

	private ImmagineAssegno immagine;

	private String noteContabili;

	private boolean compensazioneRate;

	private boolean anticipoFattura;

	/**
	 * Richiama il metodo initialize.
	 */
	public ParametriCreazioneAreaChiusure() {
		super();
		initialize();
	}

	/**
	 * @return the abi
	 */
	public String getAbi() {
		return abi;
	}

	/**
	 * @return the cab
	 */
	public String getCab() {
		return cab;
	}

	/**
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return dataDocumento;
	}

	/**
	 * @return Returns the dataEffetto.
	 */
	public Date getDataEffetto() {
		return dataEffetto;
	}

	/**
	 * @return the dataScadenzaAnticipoFatture
	 */
	public Date getDataScadenzaAnticipoFatture() {
		return dataScadenzaAnticipoFatture;
	}

	/**
	 * @return Returns the immagine.
	 */
	public ImmagineAssegno getImmagine() {
		return immagine;
	}

	/**
	 * @return Returns the noteContabili.
	 */
	public String getNoteContabili() {
		return noteContabili;
	}

	/**
	 * @return the numeroAssegno
	 */
	public String getNumeroAssegno() {
		return numeroAssegno;
	}

	/**
	 * @return the rapportoBancarioAzienda
	 */
	public RapportoBancarioAzienda getRapportoBancarioAzienda() {
		return rapportoBancarioAzienda;
	}

	/**
	 * @return Returns the speseIncasso.
	 */
	public BigDecimal getSpeseIncasso() {
		return speseIncasso;
	}

	/**
	 * @return the tipoAreaPartita
	 */
	public TipoAreaPartita getTipoAreaPartita() {
		return tipoAreaPartita;
	}

	/**
	 * @return the tipoPartita
	 */
	public TipoPartita getTipoPartita() {
		return tipoPartita;
	}

	/**
	 * Init.
	 */
	private void initialize() {
		tipoAreaPartita = null;
		speseIncasso = BigDecimal.ZERO;
		compensazioneRate = Boolean.FALSE;
	}

	/**
	 * @return the anticipoFattura
	 */
	public boolean isAnticipoFattura() {
		return anticipoFattura;
	}

	/**
	 * @return the compensazioneRate
	 */
	public boolean isCompensazioneRate() {
		return compensazioneRate;
	}

	/**
	 * @return Returns the generaSingoloEffetto.
	 */
	public boolean isGeneraSingoloEffetto() {
		return generaSingoloEffetto;
	}

	/**
	 * @param abi
	 *            the abi to set
	 */
	public void setAbi(String abi) {
		this.abi = abi;
	}

	/**
	 * @param anticipoFattura
	 *            the anticipoFattura to set
	 */
	public void setAnticipoFattura(boolean anticipoFattura) {
		this.anticipoFattura = anticipoFattura;
	}

	/**
	 * @param cab
	 *            the cab to set
	 */
	public void setCab(String cab) {
		this.cab = cab;
	}

	/**
	 * @param compensazioneRate
	 *            the compensazioneRate to set
	 */
	public void setCompensazioneRate(boolean compensazioneRate) {
		this.compensazioneRate = compensazioneRate;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @param dataEffetto
	 *            The dataEffetto to set.
	 */
	public void setDataEffetto(Date dataEffetto) {
		this.dataEffetto = dataEffetto;
	}

	/**
	 * @param dataScadenzaAnticipoFatture
	 *            the dataScadenzaAnticipoFatture to set
	 */
	public void setDataScadenzaAnticipoFatture(Date dataScadenzaAnticipoFatture) {
		this.dataScadenzaAnticipoFatture = dataScadenzaAnticipoFatture;
	}

	/**
	 * @param generaSingoloEffetto
	 *            The generaSingoloEffetto to set.
	 */
	public void setGeneraSingoloEffetto(boolean generaSingoloEffetto) {
		this.generaSingoloEffetto = generaSingoloEffetto;
	}

	/**
	 * 
	 * @param immagine
	 *            immagine del fronte e retro del pagamento. Per adesso solo l'assegno.
	 */
	public void setImmagine(ImmagineAssegno immagine) {
		this.immagine = immagine;
	}

	/**
	 * @param noteContabili
	 *            The noteContabili to set.
	 */
	public void setNoteContabili(String noteContabili) {
		this.noteContabili = noteContabili;
	}

	/**
	 * @param numeroAssegno
	 *            the numeroAssegno to set
	 */
	public void setNumeroAssegno(String numeroAssegno) {
		this.numeroAssegno = numeroAssegno;
	}

	/**
	 * @param rapportoBancarioAzienda
	 *            the rapportoBancarioAzienda to set
	 */
	public void setRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda) {
		this.rapportoBancarioAzienda = rapportoBancarioAzienda;
	}

	/**
	 * @param speseIncasso
	 *            The speseIncasso to set.
	 */
	public void setSpeseIncasso(BigDecimal speseIncasso) {
		this.speseIncasso = speseIncasso;
	}

	/**
	 * @param tipoAreaPartita
	 *            the tipoAreaPartita to set
	 */
	public void setTipoAreaPartita(TipoAreaPartita tipoAreaPartita) {
		this.tipoAreaPartita = tipoAreaPartita;
	}

	/**
	 * @param tipoPartita
	 *            the tipoPartita to set
	 */
	public void setTipoPartita(TipoPartita tipoPartita) {
		this.tipoPartita = tipoPartita;
	}

}
