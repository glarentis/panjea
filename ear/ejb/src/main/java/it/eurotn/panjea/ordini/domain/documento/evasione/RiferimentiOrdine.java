package it.eurotn.panjea.ordini.domain.documento.evasione;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RiferimentiOrdine implements Serializable {

	public enum ModalitaRicezione {
		FAX, WEB, MAIL, POSTA, ATON, MRP, TELEFONO
	}

	private static final long serialVersionUID = -1867772278965569036L;

	private Date dataOrdine;

	@Column(length = 20)
	private String numeroOrdine;

	private ModalitaRicezione modalitaRicezione;

	/**
	 * Costruttore.
	 *
	 */
	public RiferimentiOrdine() {
		super();
		numeroOrdine = "";
	}

	/**
	 * @return the dataOrdine
	 */
	public Date getDataOrdine() {
		return dataOrdine;
	}

	/**
	 * @return the modalitaRicezione
	 */
	public ModalitaRicezione getModalitaRicezione() {
		return modalitaRicezione;
	}

	/**
	 * @return the numeroOrdine
	 */
	public String getNumeroOrdine() {
		return numeroOrdine;
	}

	/**
	 * @param dataOrdine
	 *            the dataOrdine to set
	 */
	public void setDataOrdine(Date dataOrdine) {
		this.dataOrdine = dataOrdine;
	}

	/**
	 * @param modalitaRicezione
	 *            the modalitaRicezione to set
	 */
	public void setModalitaRicezione(ModalitaRicezione modalitaRicezione) {
		this.modalitaRicezione = modalitaRicezione;
	}

	/**
	 * @param numeroOrdine
	 *            the numeroOrdine to set
	 */
	public void setNumeroOrdine(String numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
	}
}
