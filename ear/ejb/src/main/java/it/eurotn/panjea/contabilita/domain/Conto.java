/**
 * 
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Cliente;
import it.eurotn.panjea.anagrafica.domain.ClientePotenziale;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Fornitore;
import it.eurotn.panjea.centricosto.domain.CentroCosto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
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
 * Classe di dominio che mappa un conto.
 * 
 * @author fattazzo
 * @version 1.0, 11/apr/07
 */
@Entity
@Audited
@Table(name = "cont_conti", uniqueConstraints = @UniqueConstraint(columnNames = { "mastro_id", "codice" }))
@NamedQueries({
		@NamedQuery(name = "Conto.caricaBySottoTipoConto", query = "from Conto c where c.codiceAzienda = :paramCodiceAzienda and c.sottotipoConto = :paramSottoTipoConto "),
		@NamedQuery(name = "Conto.caricaAll", query = "from Conto c where c.codiceAzienda = :paramCodiceAzienda ") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pianoDeiConti")
public class Conto extends EntityBase implements java.io.Serializable {

	/**
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum SottotipoConto {
		CLIENTE, FORNITORE, COSTO, RICAVO, VUOTO

	}

	/**
	 * @author giangi
	 * @version 1.0, 10/nov/2010
	 */
	public enum TipoConto {
		PATRIMONIALE, ECONOMICO, ORDINE
	}

	private static final long serialVersionUID = 7765097904050460695L;

	public static final String DEFAULT_CODICE = "000";

	/**
	 * 
	 * @param entita
	 *            entità per la quale ritornare il {@link SottotipoConto}
	 * @return sottoTipoConto {@link SottotipoConto} dipendente dalla tipologia dell'entità.<br/>
	 *         Null se l'entità non prevede un {@link SottotipoConto}
	 */
	public static SottotipoConto getSottoTipoConto(Entita entita) {
		SottotipoConto sottotipoConto = null;
		if (!(entita instanceof ClientePotenziale)) {
			if (entita instanceof Cliente) {
				sottotipoConto = SottotipoConto.CLIENTE;
			} else if (entita instanceof Fornitore) {
				sottotipoConto = SottotipoConto.FORNITORE;
			}
		}
		return sottotipoConto;
	}

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
	 * @uml.property name="tipoConto"
	 * @uml.associationEnd
	 */
	@Enumerated
	private TipoConto tipoConto;

	/**
	 * @uml.property name="sottotipoConto"
	 * @uml.associationEnd
	 */
	@Enumerated
	private SottotipoConto sottotipoConto;

	/**
	 * @uml.property name="mastro"
	 * @uml.associationEnd
	 */
	@ManyToOne
	private Mastro mastro;

	/**
	 * @uml.property name="sottoConti"
	 */
	@OneToMany(mappedBy = "conto", fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@OrderBy("codice")
	private Set<SottoConto> sottoConti;

	@Transient
	private boolean soggettoCentroCosto;

	@Transient
	private CentroCosto centroCosto;

	/**
	 * Costruttore.
	 * 
	 */
	public Conto() {
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
	 * 
	 * @return codiceConto
	 */
	public String getContoCodice() {
		StringBuffer codiceConto = new StringBuffer();
		if (codice.equals("")) {
			return codice;
		}
		codiceConto.append(getMastro().getCodice()).append(".").append(getCodice());
		return codiceConto.toString();
	}

	/**
	 * @return descrizione
	 * @uml.property name="descrizione"
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return mastro
	 * @uml.property name="mastro"
	 */
	public Mastro getMastro() {
		return mastro;
	}

	/**
	 * @return Returns the sottoConti.
	 * @uml.property name="sottoConti"
	 */
	public Set<SottoConto> getSottoConti() {
		return sottoConti;
	}

	/**
	 * @return sottotipoConto
	 * @uml.property name="sottotipoConto"
	 */
	public SottotipoConto getSottotipoConto() {
		return sottotipoConto;
	}

	/**
	 * @return tipoConto
	 * @uml.property name="tipoConto"
	 */
	public TipoConto getTipoConto() {
		return tipoConto;
	}

	/**
	 * Inizializza i valori di default.
	 */
	private void initialize() {
		this.mastro = new Mastro();
		this.sottoConti = new HashSet<SottoConto>();
		this.codice = Conto.DEFAULT_CODICE;
		this.soggettoCentroCosto = false;
	}

	/**
	 * Indica se è il conto generato di default al momento della creazione di un nuovo mastro.
	 * 
	 * @return <code>true</code> se è di default
	 */
	public boolean isDefault() {
		return this.getCodice().equals(Conto.DEFAULT_CODICE);
	};

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
	};

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 * @uml.property name="codiceAzienda"
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
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
	 * @param mastro
	 *            the mastro to set
	 * @uml.property name="mastro"
	 */
	public void setMastro(Mastro mastro) {
		this.mastro = mastro;
	}

	/**
	 * @param soggettoCentroCosto
	 *            the soggettoCentroCosto to set
	 */
	public void setSoggettoCentroCosto(boolean soggettoCentroCosto) {
		this.soggettoCentroCosto = soggettoCentroCosto;
	}

	/**
	 * @param sottoConti
	 *            The sottoConti to set.
	 * @uml.property name="sottoConti"
	 */
	public void setSottoConti(Set<SottoConto> sottoConti) {
		this.sottoConti = sottoConti;
	}

	/**
	 * @param sottotipoConto
	 *            the sottotipoConto to set
	 * @uml.property name="sottotipoConto"
	 */
	public void setSottotipoConto(SottotipoConto sottotipoConto) {
		this.sottotipoConto = sottotipoConto;
	}

	/**
	 * @param tipoConto
	 *            the tipoConto to set
	 * @uml.property name="tipoConto"
	 */
	public void setTipoConto(TipoConto tipoConto) {
		this.tipoConto = tipoConto;
	}

	/**
	 * 
	 * 
	 * @return the String representation
	 * @author fattazzo
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Conto[");
		buffer.append(super.toString());
		buffer.append("azienda = ").append(codiceAzienda);
		buffer.append(", codice = ").append(codice);
		buffer.append(", descrizione = ").append(descrizione);
		buffer.append(", sottotipoConto = ").append(sottotipoConto);
		buffer.append(", tipoConto = ").append(tipoConto);
		buffer.append("]");
		return buffer.toString();
	}
}
