package it.eurotn.panjea.intra.domain;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.joda.time.DateTime;

@Entity
@Audited
@DiscriminatorValue("3")
public class RigaSezione3Intra extends RigaSezioneIntra {
	private static final long serialVersionUID = -7582072736826335745L;
	@ManyToOne
	private Servizio servizio;
	private int numeroFattura;
	@Temporal(TemporalType.DATE)
	private Date dataFattura;
	private ModalitaErogazione modalitaErogazione;
	private ModalitaIncasso modalitaIncasso;
	private String paesePagamento;

	/**
	 * @return Returns the dataFattura.
	 */
	public Date getDataFattura() {
		return dataFattura;
	}

	/**
	 * @return Returns the modalitaErogazione.
	 */
	public ModalitaErogazione getModalitaErogazione() {
		return modalitaErogazione;
	}

	/**
	 * @return Returns the modalitaIncasso.
	 */
	public ModalitaIncasso getModalitaIncasso() {
		return modalitaIncasso;
	}

	/**
	 * @return Returns the numeroFattura.
	 */
	public int getNumeroFattura() {
		return numeroFattura;
	}

	/**
	 * @return Returns the paesePagamento.
	 */
	public String getPaesePagamento() {
		return paesePagamento;
	}

	/**
	 * @return Returns the servizio.
	 */
	public Servizio getServizio() {
		return servizio;
	}

	/**
	 * @param dataFattura
	 *            The dataFattura to set.
	 */
	public void setDataFattura(Date dataFattura) {
		this.dataFattura = dataFattura;
		getCrc().update(new DateTime(dataFattura).getMillis());
	}

	/**
	 * @param modalitaErogazione
	 *            The modalitaErogazione to set.
	 */
	public void setModalitaErogazione(ModalitaErogazione modalitaErogazione) {
		this.modalitaErogazione = modalitaErogazione;
		if (modalitaErogazione != null) {
			getCrc().update(modalitaErogazione.ordinal());
		}
	}

	/**
	 * @param modalitaIncasso
	 *            The modalitaIncasso to set.
	 */
	public void setModalitaIncasso(ModalitaIncasso modalitaIncasso) {
		this.modalitaIncasso = modalitaIncasso;
		if (modalitaIncasso != null) {
			getCrc().update(modalitaIncasso.ordinal());
		}
	}

	/**
	 * @param numeroFattura
	 *            The numeroFattura to set.
	 */
	public void setNumeroFattura(int numeroFattura) {
		this.numeroFattura = numeroFattura;
		getCrc().update(numeroFattura);
	}

	/**
	 * @param paesePagamento
	 *            The paesePagamento to set.
	 */
	public void setPaesePagamento(String paesePagamento) {
		this.paesePagamento = paesePagamento;
		getCrc().update(paesePagamento);
	}

	/**
	 * @param servizio
	 *            The servizio to set.
	 */
	public void setServizio(Servizio servizio) {
		this.servizio = servizio;
		getCrc().update(servizio);
	}
}
