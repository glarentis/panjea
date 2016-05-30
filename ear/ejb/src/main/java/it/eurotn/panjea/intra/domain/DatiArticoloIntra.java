package it.eurotn.panjea.intra.domain;

import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Embeddable
public class DatiArticoloIntra implements Serializable {

	private static final long serialVersionUID = 800586292589621790L;

	@OneToOne
	private Nazione nazione;

	@Column(precision = 12, scale = 3)
	private BigDecimal massaNetta;

	@Column(precision = 12, scale = 3)
	private BigDecimal valoreUnitaMisuraSupplementare;

	@ManyToOne
	private Servizio servizio;

	@Enumerated
	private ModalitaErogazione modalitaErogazione;

	/**
	 * @return Returns the massaNetta.
	 */
	public BigDecimal getMassaNetta() {
		return massaNetta;
	}

	/**
	 * @return Returns the modalitaErogazione.
	 */
	public ModalitaErogazione getModalitaErogazione() {
		return modalitaErogazione;
	}

	/**
	 * @return Returns the nazione.
	 */
	public Nazione getNazione() {
		return nazione;
	}

	/**
	 * @return Returns the servizio.
	 */
	public Servizio getServizio() {
		return servizio;
	}

	/**
	 * @return the valoreUnitaMisuraSupplementare
	 */
	public BigDecimal getValoreUnitaMisuraSupplementare() {
		return valoreUnitaMisuraSupplementare;
	}

	/**
	 * @param massaNetta
	 *            The massaNetta to set.
	 */
	public void setMassaNetta(BigDecimal massaNetta) {
		this.massaNetta = massaNetta;
	}

	/**
	 * @param modalitaErogazione
	 *            The modalitaErogazione to set.
	 */
	public void setModalitaErogazione(ModalitaErogazione modalitaErogazione) {
		this.modalitaErogazione = modalitaErogazione;
	}

	/**
	 * @param nazione
	 *            The nazione to set.
	 */
	public void setNazione(Nazione nazione) {
		this.nazione = nazione;
	}

	/**
	 * @param servizio
	 *            The servizio to set.
	 */
	public void setServizio(Servizio servizio) {
		this.servizio = servizio;
	}

	/**
	 * @param valoreUnitaMisuraSupplementare
	 *            the valoreUnitaMisuraSupplementare to set
	 */
	public void setValoreUnitaMisuraSupplementare(BigDecimal valoreUnitaMisuraSupplementare) {
		this.valoreUnitaMisuraSupplementare = valoreUnitaMisuraSupplementare;
	}
}
