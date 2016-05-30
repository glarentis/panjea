package it.eurotn.panjea.intra.domain;

import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue("4")
public class RigaSezione4Intra extends RigaSezioneIntra {
	private static final long serialVersionUID = -7582072736826335745L;
	@ManyToOne
	private Servizio servizio;
	private int numeroFattura;
	@Temporal(TemporalType.DATE)
	private Date dataFattura;
	private ModalitaErogazione modalitaErogazione;
	private ModalitaIncasso modalitaIncasso;
	private String paesePagamento;
	private String sezioneDoganale;
	private Integer anno;
	private String protocolloDic;
	private String progrSezione3;

	/**
	 * @return Returns the anno.
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @return l'anno per esportazione per la presentazione intra, solo gli ultimi due caratteri di anno
	 */
	public String getAnnoPerEsportazione() {
		String annoPerExp = new String("");
		if (anno != null && anno > 1900) {
			annoPerExp = anno.toString();
			annoPerExp = annoPerExp.substring(2);
		}
		return annoPerExp;
	}

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
	 * @return Returns the progrSezione3.
	 */
	public String getProgrSezione3() {
		return progrSezione3;
	}

	/**
	 * @return Returns the protocolloDic.
	 */
	public String getProtocolloDic() {
		return protocolloDic;
	}

	/**
	 * @return Returns the servizio.
	 */
	public Servizio getServizio() {
		return servizio;
	}

	/**
	 * @return Returns the sezioneDoganale.
	 */
	public String getSezioneDoganale() {
		return sezioneDoganale;
	}

	/**
	 * @param anno
	 *            The anno to set.
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param dataFattura
	 *            The dataFattura to set.
	 */
	public void setDataFattura(Date dataFattura) {
		this.dataFattura = dataFattura;
	}

	/**
	 * @param modalitaErogazione
	 *            The modalitaErogazione to set.
	 */
	public void setModalitaErogazione(ModalitaErogazione modalitaErogazione) {
		this.modalitaErogazione = modalitaErogazione;
	}

	/**
	 * @param modalitaIncasso
	 *            The modalitaIncasso to set.
	 */
	public void setModalitaIncasso(ModalitaIncasso modalitaIncasso) {
		this.modalitaIncasso = modalitaIncasso;
	}

	/**
	 * @param numeroFattura
	 *            The numeroFattura to set.
	 */
	public void setNumeroFattura(int numeroFattura) {
		this.numeroFattura = numeroFattura;
	}

	/**
	 * @param paesePagamento
	 *            The paesePagamento to set.
	 */
	public void setPaesePagamento(String paesePagamento) {
		this.paesePagamento = paesePagamento;
	}

	/**
	 * @param progrSezione3
	 *            The progrSezione3 to set.
	 */
	public void setProgrSezione3(String progrSezione3) {
		this.progrSezione3 = progrSezione3;
	}

	/**
	 * @param protocolloDic
	 *            The protocolloDic to set.
	 */
	public void setProtocolloDic(String protocolloDic) {
		this.protocolloDic = protocolloDic;
		getCrc().update(protocolloDic);
	}

	/**
	 * @param servizio
	 *            The servizio to set.
	 */
	public void setServizio(Servizio servizio) {
		this.servizio = servizio;
	}

	/**
	 * @param sezioneDoganale
	 *            The sezioneDoganale to set.
	 */
	public void setSezioneDoganale(String sezioneDoganale) {
		this.sezioneDoganale = sezioneDoganale;
		getCrc().update(sezioneDoganale);
	}
}
