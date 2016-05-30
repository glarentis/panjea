/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.centricosto.domain.CentroCosto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * Classe di dominio che mappa un mastro.
 * 
 * @author fattazzo
 * @version 1.0, 11/apr/07
 */
@NamedQueries({
		@NamedQuery(name = "Mastro.caricaTuttiMastri", query = "from Mastro m inner join fetch m.conti cont inner join fetch cont.sottoConti where m.codiceAzienda = :codiceAzienda"),
		@NamedQuery(name = "Mastro.caricaMastroByCodice", query = "from Mastro m where m.codiceAzienda = :codiceAzienda and m.codice=:codiceMastro") })
@Entity
@Audited
@Table(name = "cont_mastri", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda", "codice" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pianoDeiConti")
public class Mastro extends EntityBase implements java.io.Serializable {

	private static final long serialVersionUID = -1362575974727489154L;

	public static final String DEFAULT_CODICE = "000";

	/**
	 * @uml.property name="codiceAzienda"
	 */
	@Column(length = 10, nullable = false)
	@Index(name = "index_codiceAzienda")
	private String codiceAzienda;

	/**
	 * @uml.property name="codice"
	 */
	@Column(length = 3, nullable = false)
	@Index(name = "index_codice")
	private String codice;

	/**
	 * @uml.property name="descrizione"
	 */
	@Column(length = 120)
	private String descrizione;

	/**
	 * @uml.property name="conti"
	 */
	@OneToMany(mappedBy = "mastro", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@OrderBy("codice")
	private List<Conto> conti;

	@Transient
	private boolean soggettoCentroCosto;

	@Transient
	private CentroCosto centroCosto;

	/**
	 * Costruttore.
	 * 
	 */
	public Mastro() {
		initialize();
	}

	/**
	 * @return the centroCosto
	 */
	public CentroCosto getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @return codice
	 * @uml.property name="codice"
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return Returns the codiceAzienda.
	 * @uml.property name="codiceAzienda"
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return conti
	 * @uml.property name="conti"
	 */
	public List<Conto> getConti() {
		return conti;
	}

	/**
	 * @return descrizione
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		this.codice = Mastro.DEFAULT_CODICE;
		this.soggettoCentroCosto = false;
	}

	/**
	 * @return the soggettoCentroCosto
	 */
	public boolean isSoggettoCentroCosto() {
		return soggettoCentroCosto;
	}

	/**
	 * @param centroCosto
	 *            the centroCosto to set
	 */
	public void setCentroCosto(CentroCosto centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @param codice
	 *            the codice to set
	 * @uml.property name="codice"
	 */
	public void setCodice(String codice) {
		if ((codice != null) && (!codice.equals(""))) {
			NumberFormat numberFormat = new DecimalFormat("000");
			this.codice = numberFormat.format(new Integer(codice));
		} else {
			this.codice = codice;
		}
	}

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 * @uml.property name="codiceAzienda"
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param conti
	 *            the conti to set
	 * @uml.property name="conti"
	 */
	public void setConti(List<Conto> conti) {
		this.conti = conti;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 * @uml.property name="descrizione"
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param soggettoCentroCosto
	 *            the soggettoCentroCosto to set
	 */
	public void setSoggettoCentroCosto(boolean soggettoCentroCosto) {
		this.soggettoCentroCosto = soggettoCentroCosto;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Mastro[");
		buffer.append(super.toString());
		buffer.append("azienda = ").append(codiceAzienda);
		buffer.append(" codice = ").append(codice);
		buffer.append(" descrizione = ").append(descrizione);
		buffer.append("]");
		return buffer.toString();
	}
}
