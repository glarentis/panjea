package it.eurotn.panjea.lotti.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;

@Entity
@Audited
@DiscriminatorValue("I")
@NamedQueries({ @NamedQuery(name = "LottoInterno.caricaLottiByArticolo", query = "select lottoInt from LottoInterno lottoInt join fetch lottoInt.articolo where lottoInt.articolo.id = :idArticolo and lottoInt.codiceAzienda = :codiceAzienda") })
public class LottoInterno extends Lotto {

	private static final long serialVersionUID = 5792304181564175604L;

	@ManyToOne(optional = true)
	private Lotto lotto;

	/**
	 * @return the lotto
	 */
	public Lotto getLotto() {
		return lotto;
	}

	/**
	 * @param lotto
	 *            the lotto to set
	 */
	public void setLotto(Lotto lotto) {
		this.lotto = lotto;
	}
}
