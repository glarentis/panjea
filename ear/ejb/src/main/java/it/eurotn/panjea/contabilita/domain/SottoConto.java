/**
 *
 */
package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.centricosto.domain.CentroCosto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

/**
 * Classe di dominio che mappa un sottoconto.
 *
 * @author fattazzo
 * @version 1.0, 11/apr/07
 */
@NamedQueries({
	@NamedQuery(name = "SottoConto.caricaByCodiceMastro", query = "from SottoConto s where s.conto.mastro.codice = :codiceMastro and s.conto.mastro.codiceAzienda = :codiceAzienda"),
	@NamedQuery(name = "SottoConto.caricaAll", query = "from SottoConto s where s.codiceAzienda = :paramCodiceAzienda"),
	@NamedQuery(name = "SottoConto.caricaAllOrdinati", query = "from SottoConto s where s.codiceAzienda = :paramCodiceAzienda order by s.conto.mastro.codice,s.conto.codice,s.codice"),
	@NamedQuery(name = "SottoConto.caricaByEntita", query = "select s from SottoConto s inner join s.conto c where s.codiceAzienda = :paramCodiceAzienda and s.codice = :paramCodice and c.sottotipoConto = :paramSottotipoConto "),
	@NamedQuery(name = "SottoConto.caricaByCodici", query = "from SottoConto s where s.conto.mastro.codice = :codiceMastro and s.conto.mastro.codiceAzienda = :codiceAzienda and s.conto.codice = :codiceConto and s.codice = :codiceSottoConto") })
@Entity
@Audited
@Table(name = "cont_sottoconti", uniqueConstraints = @UniqueConstraint(columnNames = { "conto_id", "codice" }))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "pianoDeiConti")
public class SottoConto extends EntityBase implements java.io.Serializable {

	public enum ClassificazioneConto {
		NESSUNO, BANCA, IMMOBILIZZAZIONE, RIMANENZE_INIZIALI, RIMANENZE_FINALI, IVA
	}

	private static final long serialVersionUID = -3959603016802615203L;
	public static final String DEFAULT_CODICE = "000000";

	/**
	 * Rappresenta la lunghezza del codice mastro + codice conto + codice sottoconto comprensivo dei punti di
	 * separazione.
	 */
	public static final int LUNGHEZZACODICESOTTOCONTO = 14;

	@Column(length = 10, nullable = false)
	@Index(name = "index_codiceAzienda")
	private String codiceAzienda;

	@Embedded
	@AttributeOverride(name = "abilitato", column = @Column(name = "stile_abilitato"))
	private StileSottoConto stileSaldo;

	@Column(length = 6, nullable = false)
	@Index(name = "index_codice")
	private String codice;

	/**
	 *
	 * @return codice del {@link SottoConto} con il seguente formato:MMM.CCC.SSSSSS<br/>
	 *         MMM = Codice Mastro <br/>
	 *         CCC = Codice Conto <br/>
	 *         SSSSSS = Codice Sottoconto
	 */
	@Transient
	private String sottoContoCodice;

	@Index(name = "index_soggettoCentroCosto")
	private boolean soggettoCentroCosto = false;

	@ManyToOne(optional = true)
	private CentroCosto centroCosto;

	@Column(length = 120)
	private String descrizione;

	@Enumerated
	private ClassificazioneConto classificazioneConto = ClassificazioneConto.NESSUNO;;

	private Boolean flagIRAP = Boolean.FALSE;

	@ManyToOne
	private Conto conto = new Conto();

	private boolean abilitato = true;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<CodiceImportazioneSottoConto> codiciImportazione = new ArrayList<CodiceImportazioneSottoConto>();

	/**
	 * Costruttore.
	 */
	public SottoConto() {
		initialize();
	}

	/**
	 * @return Returns the centroCosto.
	 */
	public CentroCosto getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @return Returns the classificazioneConto.
	 */
	public ClassificazioneConto getClassificazioneConto() {
		return classificazioneConto;
	}

	/**
	 * @return Returns the codice.
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return Returns the codiceAzienda.
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return Returns the codiciImportazione.
	 */
	public List<CodiceImportazioneSottoConto> getCodiciImportazione() {
		return codiciImportazione;
	}

	/**
	 * @return Returns the conto.
	 */
	public Conto getConto() {
		return conto;
	}

	/**
	 * @return Returns the descrizione.
	 */
	public String getDescrizione() {
		// npe
		if (descrizione == null) {
			return "";
		}
		return descrizione;
	}

	/**
	 * @return Returns the flagIRAP.
	 */
	public Boolean getFlagIRAP() {
		return flagIRAP;
	}

	/**
	 *
	 * @return codice del {@link SottoConto} con il seguente formato:MMM.CCC.SSSSSS<br/>
	 *         MMM = Codice Mastro <br/>
	 *         CCC = Codice Conto <br/>
	 *         SSSSSS = Codice Sottoconto
	 */
	public String getSottoContoCodice() {
		if (sottoContoCodice == null) {
			StringBuilder sb = new StringBuilder();
			if (codice != null && !codice.isEmpty()) {
				sb.append(getConto().getMastro().getCodice()).append(".").append(getConto().getCodice()).append(".")
				.append(getCodice());
				sottoContoCodice = sb.toString();
			}
		}
		return sottoContoCodice;
	}

	/**
	 * @return Returns the stileSaldo.
	 */
	public StileSottoConto getStileSaldo() {
		if (stileSaldo == null) {
			stileSaldo = new StileSottoConto();
		}
		return stileSaldo;
	}

	/**
	 * Init valori iniziali.
	 */
	@PostLoad
	private void initialize() {
	}

	/**
	 * @return the abilitato
	 * @uml.property name="abilitato"
	 */
	public boolean isAbilitato() {
		return abilitato;
	}

	/**
	 *
	 * @return true se è il {@link SottoConto} generato di default al momento della creazione di un nuovo conto.
	 */
	public boolean isDefault() {
		return this.getCodice().equals(SottoConto.DEFAULT_CODICE);
	}

	/**
	 * @return Returns the soggettoCentroCosto.
	 */
	public boolean isSoggettoCentroCosto() {
		return soggettoCentroCosto;
	}

	/**
	 * @param importo
	 *            importo da testare
	 * @return true se lo stile del sottoConto deve essere applicato per l'importo passato come parametro
	 */
	public boolean isStileSaldoEnabled(BigDecimal importo) {
		if (importo != null && getStileSaldo().isAbilitato() && getStileSaldo().getImporto() != null) {
			if ("=".equals(stileSaldo.getCondizione()) && importo.compareTo(stileSaldo.getImporto()) == 0) {
				return true;
			}
			if (">=".equals(stileSaldo.getCondizione()) && importo.compareTo(stileSaldo.getImporto()) >= 0) {
				return true;
			}
			if (">".equals(stileSaldo.getCondizione()) && importo.compareTo(stileSaldo.getImporto()) > 0) {
				return true;
			}
			if ("<=".equals(stileSaldo.getCondizione()) && importo.compareTo(stileSaldo.getImporto()) <= 0) {
				return true;
			}
			if ("<".equals(stileSaldo.getCondizione()) && importo.compareTo(stileSaldo.getImporto()) < 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param abilitato
	 *            The abilitato to set.
	 */
	public void setAbilitato(boolean abilitato) {
		this.abilitato = abilitato;
	}

	/**
	 * @param centroCosto
	 *            The centroCosto to set.
	 */
	public void setCentroCosto(CentroCosto centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @param classificazioneConto
	 *            calssificazione del conto
	 * @uml.property name="classificazioneConto"
	 */
	public void setClassificazioneConto(ClassificazioneConto classificazioneConto) {
		this.classificazioneConto = classificazioneConto;
	}

	/**
	 * @param codice
	 *            codice del conto. se null o stringa vuota viene settato il codice del sottoconto base ("000000")
	 * @uml.property name="codice"
	 */
	public void setCodice(String codice) {
		if ((codice != null) && (!codice.equals(""))) {
			NumberFormat numberFormat = new DecimalFormat("000000");
			this.codice = numberFormat.format(new Integer(codice));
		} else {
			this.codice = codice;
		}
	}

	/**
	 * @param codiceAzienda
	 *            The codiceAzienda to set.
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param codiciImportazione
	 *            The codiciImportazione to set.
	 */
	public void setCodiciImportazione(List<CodiceImportazioneSottoConto> codiciImportazione) {
		this.codiciImportazione = codiciImportazione;
	}

	/**
	 * @param conto
	 *            The conto to set.
	 */
	public void setConto(Conto conto) {
		this.conto = conto;
	}

	/**
	 * @param descrizione
	 *            The descrizione to set.
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param flagIRAP
	 *            The flagIRAP to set.
	 */
	public void setFlagIRAP(Boolean flagIRAP) {
		this.flagIRAP = flagIRAP;
	}

	/**
	 * @param soggettoCentroCosto
	 *            The soggettoCentroCosto to set.
	 */
	public void setSoggettoCentroCosto(boolean soggettoCentroCosto) {
		this.soggettoCentroCosto = soggettoCentroCosto;
	}

	/**
	 * @param sottoContoCodice
	 *            the sottoContoCodice to set
	 */
	public void setSottoContoCodice(String sottoContoCodice) {
		this.sottoContoCodice = sottoContoCodice;
	}

	/**
	 * @param stileSaldo
	 *            The stileSaldo to set.
	 */
	public void setStileSaldo(StileSottoConto stileSaldo) {
		this.stileSaldo = stileSaldo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SottoConto [codice=" + codice + ", descrizione=" + descrizione + "," + getId() + "]";
	}
}
