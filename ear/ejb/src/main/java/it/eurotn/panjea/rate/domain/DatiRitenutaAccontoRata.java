/**
 * 
 */
package it.eurotn.panjea.rate.domain;

import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * @author fattazzo
 * 
 */
@Embeddable
public class DatiRitenutaAccontoRata implements Serializable {

	private static final long serialVersionUID = 708235870272653630L;

	private String tributo;

	private String sezione;

	/**
	 * Indica il pagamento a cui fa riferimento la rata della ritenuta d'acconto.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Pagamento pagamentoRiferimento;

	/**
	 * Costruttore.
	 */
	public DatiRitenutaAccontoRata() {
		super();
	}

	/**
	 * @return the pagamentoRiferimento
	 */
	public Pagamento getPagamentoRiferimento() {
		return pagamentoRiferimento;
	}

	/**
	 * @return the sezione
	 */
	public String getSezione() {
		return sezione;
	}

	/**
	 * @return the tributo
	 */
	public String getTributo() {
		return tributo;
	}

	/**
	 * @param pagamentoRiferimento
	 *            the pagamentoRiferimento to set
	 */
	public void setPagamentoRiferimento(Pagamento pagamentoRiferimento) {
		this.pagamentoRiferimento = pagamentoRiferimento;
	}

	/**
	 * @param sezione
	 *            the sezione to set
	 */
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	/**
	 * @param tributo
	 *            the tributo to set
	 */
	public void setTributo(String tributo) {
		this.tributo = tributo;
	}

}
