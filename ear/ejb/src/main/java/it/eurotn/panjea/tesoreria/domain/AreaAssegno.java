/**
 * 
 */
package it.eurotn.panjea.tesoreria.domain;

import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.tesoreria.util.ImmagineAssegno;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

/**
 * @author leonardo
 */
@Entity
@Audited
@DiscriminatorValue("AS")
@NamedQueries({
		@NamedQuery(name = "AreaAssegno.carica", query = "select distinct aa from AreaAssegno aa join fetch aa.pagamenti pags join fetch pags.rata where aa.id = :paramIdAreaAssegno"),
		@NamedQuery(name = "AreaAssegno.caricaByAreaAccredito", query = "select distinct a from AreaAssegno a join fetch a.pagamenti pags join fetch pags.rata where pags.areaAccreditoAssegno.id=:paramIdAreaAccreditoAssegno") })
@AuditableProperties(properties = { "documento" })
public class AreaAssegno extends AreaPagamenti {

	/**
	 * Lo stato dell'assegno.
	 * <ul>
	 * <li>IN_LAVORAZIONE: indica che l'assegno è stato registrato, ma non ancora accreditato; non si ha parte contabile
	 * a questo stato</li>
	 * <li>CHIUSO: l'assegno risulta accreditato, a questo stato è presente anche la parte contabile dei documenti</li>
	 * </ul>
	 * 
	 * @author leonardo
	 */
	public enum StatoAssegno {
		IN_LAVORAZIONE, CHIUSO
	}

	private static final long serialVersionUID = 4785374401162348343L;

	@Transient
	private StatoAssegno statoAssegno;

	@Column(length = 20)
	private String numeroAssegno;

	@Column(length = 5)
	private String abi;

	@Column(length = 5)
	private String cab;

	@ManyToOne
	private RapportoBancarioAzienda rapportoBancarioAzienda;

	@Transient
	private ImmagineAssegno immagineAssegno;

	/**
	 * Valorizza i valori transient di AreaAssegno.
	 * <ul>
	 * <li>StatoAssegno: a seconda se i pagamenti hanno la data pagamento impostata oppure no.
	 * </ul>
	 */
	private void aggiornaValoriTransient() {
		if (getPagamenti() == null) {
			return;
		}
		Pagamento pagamento = getPagamenti().iterator().next();
		if (pagamento.getDataPagamento() != null) {
			statoAssegno = StatoAssegno.CHIUSO;
		}
		if (pagamento.getDataPagamento() == null) {
			statoAssegno = StatoAssegno.IN_LAVORAZIONE;
		}
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
	 * @return the immagineAssegno
	 */
	public ImmagineAssegno getImmagineAssegno() {
		return immagineAssegno;
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
	 * @return the statoAssegno
	 */
	public StatoAssegno getStatoAssegno() {
		if (statoAssegno == null) {
			aggiornaValoriTransient();
		}
		return statoAssegno;
	}

	/**
	 * @param abi
	 *            the abi to set
	 */
	public void setAbi(String abi) {
		this.abi = abi;
	}

	/**
	 * @param cab
	 *            the cab to set
	 */
	public void setCab(String cab) {
		this.cab = cab;
	}

	/**
	 * @param immagineAssegno
	 *            the immagineAssegno to set
	 */
	public void setImmagineAssegno(ImmagineAssegno immagineAssegno) {
		this.immagineAssegno = immagineAssegno;
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
	 * @param statoAssegno
	 *            the statoAssegno to set
	 */
	public void setStatoAssegno(StatoAssegno statoAssegno) {
		this.statoAssegno = statoAssegno;
	}

}
