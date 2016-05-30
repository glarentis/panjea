package it.eurotn.panjea.spedizioni.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DatiSpedizioniDocumento implements Serializable {

	public enum Consegne {
		NESSUNA(""), PER_APPUNTAMENTO("A"), FUORI_MISURA("F"), AI_PIANI("P"), SUPERMERCATI("S");

		private final String codice;

		/**
		 * Costruttore.
		 * 
		 * @param codice
		 *            codice.
		 */
		Consegne(final String codice) {
			this.codice = codice;
		}

		/**
		 * @return the codice
		 */
		public String getCodice() {
			return codice;
		}
	}

	public enum ModalitaIncasso {
		NESSUNA(""), ASS_BANC_MITTENTE("BM"), ASS_CIRC_MITTENTE("CM"), ASS_BANC_CORRIERE("BB"), ASS_CIRC_CORRIERE("CB"), ASS_MITTENTE_ORIGINALE(
				"OM"), ASS_CIRC_MITTENTE_ORIGINALE("OC"), CONTANTE("");

		private final String codice;

		/**
		 * Costruttore.
		 * 
		 * @param codice
		 *            codice.
		 */
		ModalitaIncasso(final String codice) {
			this.codice = codice;
		}

		/**
		 * @return the codice
		 */
		public String getCodice() {
			return codice;
		}
	}

	private static final long serialVersionUID = 9210423894858835295L;

	private Consegne consegna;

	@Column(length = 35)
	private String noteSpedizione;

	@Column(precision = 12, scale = 6)
	private BigDecimal importoContrassegnoSpedizione;

	private ModalitaIncasso modalitaIncasso;

	private boolean rendicontatoAlVettore;

	private Date dataConsegna;

	/**
	 * Costruttore.
	 * 
	 */
	public DatiSpedizioniDocumento() {
		super();
		this.rendicontatoAlVettore = false;
	}

	/**
	 * @return the consegna
	 */
	public Consegne getConsegna() {
		return consegna;
	}

	/**
	 * @return the dataConsegna
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return the dataConsegna
	 */
	public String getDataConsegnaString() {

		String result = "00000000";

		if (dataConsegna != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			result = dateFormat.format(dataConsegna);
		}

		return result;
	}

	/**
	 * @return the importoContrassegnoSpedizione
	 */
	public BigDecimal getImportoContrassegnoSpedizione() {
		return importoContrassegnoSpedizione;
	}

	/**
	 * @return the modalitaIncasso
	 */
	public ModalitaIncasso getModalitaIncasso() {
		return modalitaIncasso;
	}

	/**
	 * @return the noteSpedizione
	 */
	public String getNoteSpedizione() {
		return noteSpedizione;
	}

	/**
	 * @return the rendicontatoAlVettore
	 */
	public boolean isRendicontatoAlVettore() {
		return rendicontatoAlVettore;
	}

	/**
	 * @param consegna
	 *            the consegna to set
	 */
	public void setConsegna(Consegne consegna) {
		this.consegna = consegna;
	}

	/**
	 * @param dataConsegna
	 *            the dataConsegna to set
	 */
	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * Metodo creato solamente perchè richiesto dal framework di esportazione.
	 * 
	 * @param dataConsegnaString
	 *            the dataConsegna to set
	 */
	public void setDataConsegnaString(String dataConsegnaString) {

	}

	/**
	 * @param importoContrassegnoSpedizione
	 *            the importoContrassegnoSpedizione to set
	 */
	public void setImportoContrassegnoSpedizione(BigDecimal importoContrassegnoSpedizione) {
		this.importoContrassegnoSpedizione = importoContrassegnoSpedizione;
	}

	/**
	 * @param modalitaIncasso
	 *            the modalitaIncasso to set
	 */
	public void setModalitaIncasso(ModalitaIncasso modalitaIncasso) {
		this.modalitaIncasso = modalitaIncasso;
	}

	/**
	 * @param noteSpedizione
	 *            the noteSpedizione to set
	 */
	public void setNoteSpedizione(String noteSpedizione) {
		this.noteSpedizione = noteSpedizione;
	}

	/**
	 * @param rendicontatoAlVettore
	 *            the rendicontatoAlVettore to set
	 */
	public void setRendicontatoAlVettore(boolean rendicontatoAlVettore) {
		this.rendicontatoAlVettore = rendicontatoAlVettore;
	}

}
