package it.eurotn.panjea.ordini.domain.documento.evasione;

import it.eurotn.entity.EntityBase;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "ordi_riga_distinta_carico_lotto")
@Audited
@NamedQueries({ @NamedQuery(name = "RigaDistintaCaricoLotto.caricaByRigaDistinta", query = "select rl  from RigaDistintaCaricoLotto rl  where rl.rigaDistintaCarico.id = :idRigaDistinta") })
public class RigaDistintaCaricoLotto extends EntityBase {
	private static final long serialVersionUID = 2385257932268036665L;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private RigaDistintaCarico rigaDistintaCarico;

	private String codiceLotto;

	private Double quantita;

	private Date dataScadenza;

	/**
	 * @return Returns the codiceLotto.
	 */
	public String getCodiceLotto() {
		return codiceLotto;
	}

	/**
	 * @return Returns the dataScadenza.
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @return Returns the quantita.
	 */
	public Double getQuantita() {
		return quantita;
	}

	/**
	 * @return Returns the rigaDistintaCarico.
	 */
	public RigaDistintaCarico getRigaDistintaCarico() {
		return rigaDistintaCarico;
	}

	/**
	 * @param codiceLotto
	 *            The codiceLotto to set.
	 */
	public void setCodiceLotto(String codiceLotto) {
		this.codiceLotto = codiceLotto;
	}

	/**
	 * 
	 * @param dataScadenza
	 *            scadenza del lotto
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @param quantita
	 *            The quantita to set.
	 */
	public void setQuantita(Double quantita) {
		this.quantita = quantita;
	}

	/**
	 * @param rigaDistintaCarico
	 *            The rigaDistintaCarico to set.
	 */
	public void setRigaDistintaCarico(RigaDistintaCarico rigaDistintaCarico) {
		this.rigaDistintaCarico = rigaDistintaCarico;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RigaDistintaCaricoLotto [codiceLotto=" + codiceLotto + ", quantita=" + quantita + ", dataScadenza="
				+ dataScadenza + "]";
	}

}
