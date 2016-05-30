package it.eurotn.panjea.contabilita.util;

import java.io.Serializable;
import java.util.Date;

public class ParametriCreazioneComPolivalente implements Serializable {

	public class Intermediario implements Serializable {

		private static final long serialVersionUID = 9207016713188498182L;

		private String codiceFiscale;

		private String numeroIscrizioneCAF;

		private boolean impegnoATrasmettere;

		private Date dataImpegno;

		{
			impegnoATrasmettere = Boolean.FALSE;
		}

		/**
		 * @return the codiceFiscale
		 */
		public String getCodiceFiscale() {
			return codiceFiscale;
		}

		/**
		 * @return the dataImpegno
		 */
		public Date getDataImpegno() {
			return dataImpegno;
		}

		/**
		 * @return the numeroIscrizioneCAF
		 */
		public String getNumeroIscrizioneCAF() {
			return numeroIscrizioneCAF;
		}

		/**
		 * @return the impegnoATrasmettere
		 */
		public boolean isImpegnoATrasmettere() {
			return impegnoATrasmettere;
		}

		/**
		 * @param codiceFiscale
		 *            the codiceFiscale to set
		 */
		public void setCodiceFiscale(String codiceFiscale) {
			this.codiceFiscale = codiceFiscale;
		}

		/**
		 * @param dataImpegno
		 *            the dataImpegno to set
		 */
		public void setDataImpegno(Date dataImpegno) {
			this.dataImpegno = dataImpegno;
		}

		/**
		 * @param impegnoATrasmettere
		 *            the impegnoATrasmettere to set
		 */
		public void setImpegnoATrasmettere(boolean impegnoATrasmettere) {
			this.impegnoATrasmettere = impegnoATrasmettere;
		}

		/**
		 * @param numeroIscrizioneCAF
		 *            the numeroIscrizioneCAF to set
		 */
		public void setNumeroIscrizioneCAF(String numeroIscrizioneCAF) {
			this.numeroIscrizioneCAF = numeroIscrizioneCAF;
		}

	}

	/**
	 * Tipologia dei dati.
	 * 
	 * @author fattazzo
	 */
	public enum TipologiaDati {
		AGGREGATI, ANALITICI
	}

	/**
	 * Tipologia invio.<br>
	 * L'ordine deve essere:
	 * <ul>
	 * <li>0=INVIO_ORDINARIO</li>
	 * <li>1=INVIO_SOSTITUIVO</li>
	 * <li>2=ANNULLAMENTO</li>
	 * </ul>
	 * 
	 * @author leonardo
	 */
	public enum TipologiaInvio {
		INVIO_ORDINARIO, INVIO_SOSTITUIVO, ANNULLAMENTO
	}

	private static final long serialVersionUID = -2692161293538856755L;

	/**
	 * Obbligatorio.
	 */
	private TipologiaInvio tipologiaInvio;

	/**
	 * Utilizzati solo in caso di {@link TipologiaInvio#INVIO_SOSTITUIVO} o {@link TipologiaInvio#ANNULLAMENTO}.
	 */
	private Long protocolloComunicazione;
	private Integer protocolloDocumento;

	/**
	 * Obbligatorio.
	 */
	private TipologiaDati tipologiaDati;

	/**
	 * Anno nel formato "yyyy". Obbligatorio.
	 */
	private Integer annoRiferimento;

	private boolean intermediarioPresente;

	private Intermediario intermediario;

	private boolean effettuaRicerca;

	{
		tipologiaInvio = TipologiaInvio.INVIO_ORDINARIO;
		tipologiaDati = TipologiaDati.AGGREGATI;

		intermediarioPresente = Boolean.FALSE;
		intermediario = new Intermediario();

		effettuaRicerca = Boolean.FALSE;
	}

	/**
	 * @return the annoRiferimento
	 */
	public Integer getAnnoRiferimento() {
		return annoRiferimento;
	}

	/**
	 * @return the intermediario
	 */
	public Intermediario getIntermediario() {
		return intermediario;
	}

	/**
	 * @return the protocolloComunicazione
	 */
	public Long getProtocolloComunicazione() {
		return protocolloComunicazione;
	}

	/**
	 * @return the protocolloDocumento
	 */
	public Integer getProtocolloDocumento() {
		return protocolloDocumento;
	}

	/**
	 * @return the tipologiaDati
	 */
	public TipologiaDati getTipologiaDati() {
		return tipologiaDati;
	}

	/**
	 * @return the tipologiaInvio
	 */
	public TipologiaInvio getTipologiaInvio() {
		return tipologiaInvio;
	}

	/**
	 * @return the effettuaRicerca
	 */
	public boolean isEffettuaRicerca() {
		return effettuaRicerca;
	}

	/**
	 * @return the intermediarioPresente
	 */
	public boolean isIntermediarioPresente() {
		return intermediarioPresente;
	}

	/**
	 * @param annoRiferimento
	 *            the annoRiferimento to set
	 */
	public void setAnnoRiferimento(Integer annoRiferimento) {
		this.annoRiferimento = annoRiferimento;
	}

	/**
	 * @param effettuaRicerca
	 *            the effettuaRicerca to set
	 */
	public void setEffettuaRicerca(boolean effettuaRicerca) {
		this.effettuaRicerca = effettuaRicerca;
	}

	/**
	 * @param intermediario
	 *            the intermediario to set
	 */
	public void setIntermediario(Intermediario intermediario) {
		this.intermediario = intermediario;
	}

	/**
	 * @param intermediarioPresente
	 *            the intermediarioPresente to set
	 */
	public void setIntermediarioPresente(boolean intermediarioPresente) {
		this.intermediarioPresente = intermediarioPresente;
	}

	/**
	 * @param protocolloComunicazione
	 *            the protocolloComunicazione to set
	 */
	public void setProtocolloComunicazione(Long protocolloComunicazione) {
		this.protocolloComunicazione = protocolloComunicazione;
	}

	/**
	 * @param protocolloDocumento
	 *            the protocolloDocumento to set
	 */
	public void setProtocolloDocumento(Integer protocolloDocumento) {
		this.protocolloDocumento = protocolloDocumento;
	}

	/**
	 * @param tipologiaDati
	 *            the tipologiaDati to set
	 */
	public void setTipologiaDati(TipologiaDati tipologiaDati) {
		this.tipologiaDati = tipologiaDati;
	}

	/**
	 * @param tipologiaInvio
	 *            the tipologiaInvio to set
	 */
	public void setTipologiaInvio(TipologiaInvio tipologiaInvio) {
		this.tipologiaInvio = tipologiaInvio;
	}

}
