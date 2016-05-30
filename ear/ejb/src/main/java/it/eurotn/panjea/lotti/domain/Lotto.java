package it.eurotn.panjea.lotti.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "maga_lotti", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda", "codice",
		"articolo_id", "dataScadenza" }))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_LOTTO", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue("F")
@NamedQueries({
		@NamedQuery(name = "Lotto.caricaLottiByArticolo", query = "select lott from Lotto lott join fetch lott.articolo where lott.articolo.id = :idArticolo and lott.codiceAzienda = :codiceAzienda and lott.class = Lotto"),
		@NamedQuery(name = "Lotto.cancellaLottiOrfani", query = "delete Lotto lot where lot.righeLotti is empty") })
public class Lotto extends EntityBase {

	/**
	 * 
	 * @author fattazzo
	 * 
	 *         Classe usata solo a livello UI per poter avere la rimanenza formattata con i decimali previsti. La classe
	 *         risulta essenziale mostrando la rimanenza nella search text, non avendo quindi la possibilità di mappare
	 *         un table model con un context per formattarla.
	 * 
	 */
	public class RimanenzaFormattata implements Serializable {

		private static final long serialVersionUID = -2926544272335314045L;

		private Double rimanenza;
		private Integer decimali;

		/**
		 * Costruttore.
		 * 
		 */
		public RimanenzaFormattata() {
			super();
		}

		/**
		 * @return valore formattato della rimanenza
		 */
		public String getValore() {
			DecimalFormat decimalFormat = new DecimalFormat(NUMBER_FORMAT + "." + STR_ZERI.substring(0, decimali));
			return decimalFormat.format(rimanenza);
		}

		/**
		 * @param decimali
		 *            the decimali to set
		 */
		protected void setDecimali(Integer decimali) {
			this.decimali = decimali;
		}

		/**
		 * @param rimanenza
		 *            the rimanenza to set
		 */
		protected void setRimanenza(Double rimanenza) {
			this.rimanenza = rimanenza;
		}

	}

	private static final long serialVersionUID = -447367955660033516L;

	private static final String STR_ZERI = "0000000000000000000000000";
	private static final String NUMBER_FORMAT = "###,###,###,##0";

	/**
	 * @uml.property name="codiceAzienda"
	 */
	@Column(length = 10, nullable = false)
	private String codiceAzienda;

	@Temporal(TemporalType.DATE)
	private Date dataScadenza;

	@Column(length = 20)
	private String codice;

	private Integer priorita;

	@ManyToOne(fetch = FetchType.EAGER)
	private ArticoloLite articolo;

	@Transient
	private Double rimanenza;

	@Transient
	private RimanenzaFormattata rimanenzaFormattata;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lotto")
	private List<RigaLotto> righeLotti;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lotto")
	private List<LottoInterno> lottiInterni;

	{
		this.dataScadenza = Calendar.getInstance().getTime();
		this.priorita = 0;
		rimanenza = 0.0;
		this.rimanenzaFormattata = new RimanenzaFormattata();
	}

	/**
	 * Costruttore.
	 * 
	 */
	public Lotto() {
		super();
	}

	/**
	 * @return the articolo
	 */
	public ArticoloLite getArticolo() {
		return articolo;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @return the codiceAzienda
	 */
	public String getCodiceAzienda() {
		return codiceAzienda;
	}

	/**
	 * @return the dataScadenza
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @return the lottiInterni
	 */
	public List<LottoInterno> getLottiInterni() {
		return lottiInterni;
	}

	/**
	 * @return the priorita
	 */
	public Integer getPriorita() {
		return priorita;
	}

	/**
	 * @return Returns the righeLotti.
	 */
	public List<RigaLotto> getRigheLotti() {
		return righeLotti;
	}

	/**
	 * @return the rimanenza
	 */
	public Double getRimanenza() {
		return rimanenza;
	}

	/**
	 * Restituisce la quantità formattata secondo il numero di decimali dell'articolo.
	 * 
	 * @return the rimanenzaFormattata
	 */
	public RimanenzaFormattata getRimanenzaFormattata() {
		rimanenzaFormattata.setDecimali(articolo.getNumeroDecimaliQta());
		rimanenzaFormattata.setRimanenza(rimanenza);
		return rimanenzaFormattata;
	}

	/**
	 * @param articolo
	 *            the articolo to set
	 */
	public void setArticolo(ArticoloLite articolo) {
		this.articolo = articolo;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceAzienda
	 *            the codiceAzienda to set
	 */
	public void setCodiceAzienda(String codiceAzienda) {
		this.codiceAzienda = codiceAzienda;
	}

	/**
	 * @param dataScadenza
	 *            the dataScadenza to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @param lottiInterni
	 *            the lottiInterni to set
	 */
	public void setLottiInterni(List<LottoInterno> lottiInterni) {
		this.lottiInterni = lottiInterni;
	}

	/**
	 * @param priorita
	 *            the priorita to set
	 */
	public void setPriorita(Integer priorita) {
		this.priorita = priorita;
	}

	/**
	 * @param righeLotti
	 *            The righeLotti to set.
	 */
	public void setRigheLotti(List<RigaLotto> righeLotti) {
		this.righeLotti = righeLotti;
	}

	/**
	 * @param rimanenza
	 *            the rimanenza to set
	 */
	public void setRimanenza(Double rimanenza) {
		this.rimanenza = rimanenza;
	}
}
