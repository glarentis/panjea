package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaContabileRateiRisconti;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoAnno;
import it.eurotn.panjea.contabilita.domain.rateirisconti.RigaRateoRisconto;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

/**
 * La gestione del sottoconto nullo del conto avere / dare viene gestita con la variabile SottoContoNull che viene
 * istanziata sul costruttore e restituita sui get in modo da restituire la stessa istanza.
 *
 * @author fattazzo
 * @version 1.0, 06/giu/07
 */
@Entity
@Audited
@Table(name = "cont_righe_contabili")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING, length = 2)
@DiscriminatorValue("C")
@NamedQueries({
		@NamedQuery(name = "RigaContabile.caricaRigheByArea", query = "select distinct r from RigaContabile r left join fetch r.righeCentroCosto  where r.areaContabile.id = :paramIdAreaContabile order by r.ordinamento,r.class,r.id "),
		@NamedQuery(name = "RigaContabile.caricaRigheBySottoConto", query = "select r from RigaContabile r where r.areaContabile.id = :paramIdAreaContabile and r.conto.id = :paramIdSottoConto  order by r.ordinamento  "),
		@NamedQuery(name = "RigaContabile.eliminaRigheContabiliByArea", query = "delete from RigaContabile r where r.areaContabile.id = :paramIdAreaContabile "),
		@NamedQuery(name = "RigaContabile.numeroRigheByArea", query = "select count(r.id) from RigaContabile r inner join r.areaContabile a where a.id = :paramIdAreaContabile"),
		@NamedQuery(name = "RigaContabile.aggiornaLibroGiornale", query = "update RigaContabile r set r.version=r.version+1, r.paginaGiornale = :paramPaginaGiornale, r.numeroRigaGiornale = :paramNumeroRigaGiornale where r.id = :paraRigaContabileId"),
		@NamedQuery(name = "RigaContabile.invalidaLibroGiornale", query = "update RigaContabile r set r.version=r.version+1, r.paginaGiornale=0, r.numeroRigaGiornale=NULL where r.areaContabile.id=:paramIdAreaContabile ") })
public class RigaContabile extends EntityBase {

	public enum EContoInsert {
		NONE, DARE, AVERE
	}

	private static final long serialVersionUID = 5843697509025274685L;

	/**
	 * Crea una nuova riga contabile.
	 *
	 * @param areaContabile
	 *            area contabile
	 * @param sottoConto
	 *            sotto conto
	 * @param isDare
	 *            dare o avere
	 * @param importo
	 *            importo
	 * @param note
	 *            note
	 * @param rigaAutomatica
	 *            <code>true</code> se automatica
	 * @return RigaContabile
	 */
	public static RigaContabile creaRigaContabile(AreaContabile areaContabile, SottoConto sottoConto, boolean isDare,
			BigDecimal importo, String note, boolean rigaAutomatica) {
		return creaRigaContabile(new RigaContabile(), areaContabile, sottoConto, isDare, importo, note, Calendar
				.getInstance().getTimeInMillis(), rigaAutomatica);
	}

	/**
	 * Crea una nuova riga contabile.
	 *
	 * @param areaContabile
	 *            area contabile
	 * @param sottoConto
	 *            sotto conto
	 * @param isDare
	 *            dare o avere
	 * @param importo
	 *            importo
	 * @param note
	 *            note
	 * @param ordinamento
	 *            ordinamento
	 * @param rigaAutomatica
	 *            <code>true</code> se automatica
	 * @return RigaContabile
	 */
	public static RigaContabile creaRigaContabile(AreaContabile areaContabile, SottoConto sottoConto, boolean isDare,
			BigDecimal importo, String note, long ordinamento, boolean rigaAutomatica) {
		return creaRigaContabile(new RigaContabile(), areaContabile, sottoConto, isDare, importo, note, ordinamento,
				rigaAutomatica);
	}

	/**
	 * Crea una nuova riga contabile.
	 *
	 * @param rigaContabileNew
	 *            .
	 * @param areaContabile
	 *            area contabile
	 * @param sottoConto
	 *            sotto conto
	 * @param isDare
	 *            dare o avere
	 * @param importo
	 *            importo
	 * @param note
	 *            note
	 * @param rigaAutomatica
	 *            <code>true</code> se automatica
	 * @return RigaContabile
	 */
	public static RigaContabile creaRigaContabile(RigaContabile rigaContabileNew, AreaContabile areaContabile,
			SottoConto sottoConto, boolean isDare, BigDecimal importo, String note, boolean rigaAutomatica) {
		return creaRigaContabile(rigaContabileNew, areaContabile, sottoConto, isDare, importo, note, Calendar
				.getInstance().getTimeInMillis(), rigaAutomatica);
	}

	/**
	 * Crea una nuova riga contabile.
	 *
	 * @param rigaContabileNew
	 *            .
	 *
	 * @param areaContabile
	 *            area contabile
	 * @param sottoConto
	 *            sotto conto
	 * @param isDare
	 *            dare o avere
	 * @param importo
	 *            importo
	 * @param note
	 *            note
	 * @param ordinamento
	 *            ordinamento
	 * @param rigaAutomatica
	 *            <code>true</code> se automatica
	 * @return RigaContabile
	 */
	public static RigaContabile creaRigaContabile(RigaContabile rigaContabileNew, AreaContabile areaContabile,
			SottoConto sottoConto, boolean isDare, BigDecimal importo, String note, long ordinamento,
			boolean rigaAutomatica) {
		RigaContabile rigaContabile = rigaContabileNew;
		rigaContabile.setAreaContabile(areaContabile);
		rigaContabile.setOrdinamento(ordinamento);
		rigaContabile.setImporto(importo);
		if (isDare) {
			rigaContabile.setContoDare(sottoConto);
		} else {
			rigaContabile.setContoAvere(sottoConto);
		}
		rigaContabile.setNote(note);
		Set<RigaCentroCosto> righeCentroCosto = new HashSet<RigaCentroCosto>();
		if (sottoConto != null) {
			rigaContabile.setDescrizione(sottoConto.getDescrizione());

			if (sottoConto.isSoggettoCentroCosto() && sottoConto.getCentroCosto() != null) {
				RigaCentroCosto rigaCentroCosto = new RigaCentroCosto();
				rigaCentroCosto.setCentroCosto(sottoConto.getCentroCosto());
				rigaCentroCosto.setRigaContabile(rigaContabile);
				rigaCentroCosto.setImporto(importo);

				righeCentroCosto.add(rigaCentroCosto);
			}
		}
		rigaContabile.setRigheCentroCosto(righeCentroCosto);
		rigaContabile.setAutomatica(rigaAutomatica);

		return rigaContabile;
	}

	private long ordinamento;

	@ManyToOne(fetch = FetchType.LAZY)
	private AreaContabile areaContabile;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rigaContabile", cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<RigaRateoRisconto> righeRateoRisconto;

	private boolean rateiRiscontiAttivi;

	private BigDecimal importoDare;

	private BigDecimal importoAvere;

	private BigDecimal importoRateoRisconto;// importo della riga per creare i ratei/risconti.

	@Transient
	private BigDecimal importoTmp;

	@ManyToOne
	private SottoConto conto;

	@Column(length = 120)
	private String descrizione;

	private int paginaGiornale;

	private Integer numeroRigaGiornale;

	@Column(length = 250)
	private String note;

	@Transient
	private SottoConto sottoContoNull;

	private EContoInsert contoInsert = EContoInsert.NONE;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rigaContabile", cascade = CascadeType.ALL)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<RigaCentroCosto> righeCentroCosto;

	@ManyToMany(fetch = FetchType.LAZY)
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Set<Pagamento> pagamenti;

	private boolean automatica;

	{
		this.conto = null;
		this.sottoContoNull = null;
		this.righeCentroCosto = new HashSet<RigaCentroCosto>();
		this.importoAvere = BigDecimal.ZERO;
		this.importoDare = BigDecimal.ZERO;
		this.importoTmp = BigDecimal.ZERO;
		this.paginaGiornale = 0;
		// come ordinamento delle righe contabili uso la data di inserimento
		this.ordinamento = Calendar.getInstance().getTimeInMillis();
		this.pagamenti = new HashSet<Pagamento>();
		this.automatica = Boolean.FALSE;
	}

	/**
	 * Costruisce e inizializza i valore per una riga contabile.
	 *
	 */
	public RigaContabile() {
		super();
	}

	/**
	 *
	 */
	public void aggiornaImportoRateoRisconto() {
		this.importoRateoRisconto = contoInsert == EContoInsert.DARE ? importoDare : importoAvere;
	}

	/**
	 * @return areaContabile della riga
	 */
	public AreaContabile getAreaContabile() {
		return areaContabile;
	}

	/**
	 * @return conto legato alla riga
	 */
	public SottoConto getConto() {
		return conto;
	}

	/**
	 * @return conto avere
	 */
	public SottoConto getContoAvere() {
		if (this.contoInsert == EContoInsert.AVERE) {
			return this.conto;
		}
		return sottoContoNull;
	}

	/**
	 * @return conto avere
	 */
	public SottoConto getContoDare() {
		if (this.contoInsert == EContoInsert.DARE) {
			return this.conto;
		}
		return sottoContoNull;
	}

	/**
	 * @return the contoInsert
	 */
	public EContoInsert getContoInsert() {
		return contoInsert;
	}

	/**
	 * @return descrizione del sottoConto
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return importo della riga contabile
	 */
	public BigDecimal getImporto() {
		if (this.contoInsert == EContoInsert.AVERE) {
			this.importoTmp = this.importoAvere;
		} else if (this.contoInsert == EContoInsert.DARE) {
			this.importoTmp = this.importoDare;
		}
		return this.importoTmp;
	}

	/**
	 * @return Returns the importoAvere.
	 */
	public BigDecimal getImportoAvere() {
		return importoAvere;
	}

	/**
	 * @return Importo per il costo del rateo/risconto nell'anno del documento
	 */
	public BigDecimal getImportoCostoRateoRiscontoAnnoDocumento() {
		BigDecimal importoRiga = new BigDecimal(0);
		if (righeRateoRisconto == null) {
			return getImporto();
		}
		if (righeRateoRisconto.isEmpty()) {
			return getImporto();
		}

		if (righeRateoRisconto.get(0).getRateiRiscontiAnno().isEmpty()) {
			return getImporto();
		}
		boolean rateo = righeRateoRisconto.get(0).getRateiRiscontiAnno().get(0) instanceof RigaRateoAnno;

		for (RigaRateoRisconto rigaRateoRisconto : getRigheRateoRisconto()) {
			if (!rigaRateoRisconto.getRateiRiscontiAnno().isEmpty()) {
				if (rateo && rigaRateoRisconto.getRateiRiscontiAnno().size() > 1) {
					importoRiga = importoRiga.add(rigaRateoRisconto.getRateiRiscontiAnno().get(1).getImporto());
				} else {
					importoRiga = importoRiga.add(rigaRateoRisconto.getRateiRiscontiAnno().get(0).getImporto());
				}
			}
		}
		return importoRiga;
	}

	/**
	 * @return Returns the importoDare.
	 */
	public BigDecimal getImportoDare() {
		return importoDare;
	}

	/**
	 * @return Returns the importoRateoRisconto.
	 */
	public BigDecimal getImportoRateoRisconto() {
		return importoRateoRisconto;
	}

	/**
	 * @return somma degli importi successivi al primo anno del rateo. Diventa l'importo della riga.
	 */
	public BigDecimal getImportoSuccessivoFromRateoPrimoAnno() {
		boolean rateo = righeRateoRisconto.get(0).getRateiRiscontiAnno().get(0) instanceof RigaRateoAnno;
		BigDecimal importoRiga = new BigDecimal(0);
		for (RigaRateoRisconto rigaRateoRisconto : getRigheRateoRisconto()) {
			if (rateo) {
				importoRiga = importoRiga.add(rigaRateoRisconto.getRateiRiscontiAnno().get(0).getImporto());
			} else {
				importoRiga = importoRiga.add(rigaRateoRisconto.getRateiRiscontiAnno().get(0).getImportoSuccessivo());
			}
		}
		return importoRiga;
	}

	/**
	 * @return note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return numero della riga del giornare
	 */
	public Integer getNumeroRigaGiornale() {
		return numeroRigaGiornale;
	}

	/**
	 * @return ordinamento
	 */
	public long getOrdinamento() {
		return ordinamento;
	}

	/**
	 * @return the pagamenti
	 */
	public Set<Pagamento> getPagamenti() {
		return pagamenti;
	}

	/**
	 * @return pagina del giornale
	 */
	public int getPaginaGiornale() {
		return paginaGiornale;
	}

	/**
	 *
	 * @return rigaRateo collegata alla riga contabile e contenuta nello stesso documento
	 */
	public RigaContabileRateiRisconti getRigaRateoRiscontoDocumento() {
		if (righeRateoRisconto.size() > 0) {
			boolean rateo = righeRateoRisconto.get(0).getRateiRiscontiAnno().get(0) instanceof RigaRateoAnno;
			if (righeRateoRisconto.get(0).getRateiRiscontiAnno().size() > 0) {
				if (rateo) {
					return righeRateoRisconto.get(0).getRateiRiscontiAnno().get(1).getRigaContabile();
				} else {
					return righeRateoRisconto.get(0).getRateiRiscontiAnno().get(0).getRigaContabile();
				}
			}
		}
		return null;
	}

	/**
	 * @return Returns the righeCentroCosto.
	 */
	public Set<RigaCentroCosto> getRigheCentroCosto() {
		return righeCentroCosto;
	}

	/**
	 * @return Returns the righeRateoRisconto.
	 */
	public List<RigaRateoRisconto> getRigheRateoRisconto() {
		if (righeRateoRisconto == null) {
			return new ArrayList<>();
		}
		return righeRateoRisconto;
	}

	/**
	 * @return Returns the sottoContoNull.
	 */
	public SottoConto getSottoContoNull() {
		return sottoContoNull;
	}

	/**
	 * @return the automatica
	 */
	public boolean isAutomatica() {
		return automatica;
	}

	/**
	 * @return Returns the rateiRiscontiAttivi.
	 */
	public boolean isRateiRiscontiAttivi() {
		return rateiRiscontiAttivi;
	}

	/**
	 * La riga risulta valida se la somma dei centri di costo corrisponde. Se il conto non gestisce i centri di costo è
	 * sempre valida.
	 *
	 * @return true se la riga è valida.
	 */
	public boolean isValid() {
		// if (isRateiRiscontiAttivi()) {
		// BigDecimal totale = BigDecimal.ZERO;
		// for (RigaRateoRisconto rigaRateoRisconto : righeRateoRisconto) {
		// totale = totale.add(rigaRateoRisconto.getImporto());
		// }
		// if (totale.compareTo(getImportoRateoRisconto()) != 0) {
		// return false;
		// }
		// }

		if (conto != null && conto.isSoggettoCentroCosto()) {
			BigDecimal totale = BigDecimal.ZERO;
			for (RigaCentroCosto rigaCentroCosto : righeCentroCosto) {
				totale = totale.add(rigaCentroCosto.getImporto());
			}
			if (totale.compareTo(getImporto()) != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param areaContabile
	 *            areaContabile della riga
	 */
	public void setAreaContabile(AreaContabile areaContabile) {
		this.areaContabile = areaContabile;
	}

	/**
	 * @param automatica
	 *            the automatica to set
	 */
	public void setAutomatica(boolean automatica) {
		this.automatica = automatica;
	}

	/**
	 * @param contoAvere
	 *            conto avere della riga. Se settato azzera il conto dare
	 */
	public void setContoAvere(SottoConto contoAvere) {
		if ((this.getContoAvere() == null && contoAvere == null)
				|| (this.getContoAvere() != null && this.getContoAvere().equals(contoAvere))) {
			return;
		}
		if (contoAvere != null) {
			this.conto = contoAvere;
			this.importoAvere = this.importoTmp != null ? this.importoTmp : BigDecimal.ZERO;
			this.importoDare = BigDecimal.ZERO;
			this.contoInsert = EContoInsert.AVERE;
		} else {
			this.conto = null;
		}
	}

	/**
	 * @param contoDare
	 *            conto dare della riga. Se settato azzera il conto avere
	 */
	public void setContoDare(SottoConto contoDare) {
		if ((this.getContoDare() == null && contoDare == null)
				|| (this.getContoDare() != null && this.getContoDare().equals(contoDare))) {
			return;
		}
		if (contoDare != null) {
			this.conto = contoDare;
			this.importoDare = this.importoTmp != null ? this.importoTmp : BigDecimal.ZERO;
			this.importoAvere = BigDecimal.ZERO;
			this.contoInsert = EContoInsert.DARE;
		} else {
			this.conto = null;
		}
	}

	/**
	 * @param contoInsert
	 *            the contoInsert to set
	 */
	public void setContoInsert(EContoInsert contoInsert) {
		this.contoInsert = contoInsert;
	}

	/**
	 * @param descrizione
	 *            descrizione della riga. Solitamente la descrizione del sottoconto.
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param importo
	 *            importo della riga
	 */
	public void setImporto(BigDecimal importo) {
		// setta l'importo, se modifico una riga devo impostare se c'è conto
		// dare o avere scelto
		if (!this.isNew()) {
			if (this.importoDare.compareTo(BigDecimal.ZERO) != 0) {
				this.contoInsert = EContoInsert.DARE;
			} else if (this.importoAvere.compareTo(BigDecimal.ZERO) != 0) {
				this.contoInsert = EContoInsert.AVERE;
			} else {
				this.contoInsert = EContoInsert.NONE;
			}
		}
		this.importoAvere = BigDecimal.ZERO;
		this.importoDare = BigDecimal.ZERO;
		this.importoTmp = BigDecimal.ZERO;

		if (importo != null) {
			this.importoTmp = importo;

			if (getContoAvere() != null) {
				this.importoAvere = this.importoTmp;
				this.importoDare = BigDecimal.ZERO;
			} else {
				if (getContoDare() != null) {
					this.importoDare = this.importoTmp;
					this.importoAvere = BigDecimal.ZERO;
				}
			}
		}
	}

	/**
	 * @param importoRateoRisconto
	 *            The importoRateoRisconto to set.
	 */
	public void setImportoRateoRisconto(BigDecimal importoRateoRisconto) {
		this.importoRateoRisconto = importoRateoRisconto;
	}

	/**
	 * @param note
	 *            note della riga
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param numeroRigaGiornale
	 *            numero riga del giornale
	 */
	public void setNumeroRigaGiornale(Integer numeroRigaGiornale) {
		this.numeroRigaGiornale = numeroRigaGiornale;
	}

	/**
	 * @param ordinamento
	 *            ordinamento
	 */
	public void setOrdinamento(long ordinamento) {
		this.ordinamento = ordinamento;
	}

	/**
	 * @param pagamenti
	 *            the pagamenti to set
	 */
	public void setPagamenti(Set<Pagamento> pagamenti) {
		this.pagamenti = pagamenti;
	}

	/**
	 * @param paginaGiornale
	 *            pagina del giornale
	 */
	public void setPaginaGiornale(int paginaGiornale) {
		this.paginaGiornale = paginaGiornale;
	}

	/**
	 * @param paramRateiRiscontiAttivi
	 *            The rateiRiscontiAttivi to set.
	 */
	public void setRateiRiscontiAttivi(boolean paramRateiRiscontiAttivi) {
		if (!this.rateiRiscontiAttivi && paramRateiRiscontiAttivi) {
			importoRateoRisconto = getImporto();
		}
		this.rateiRiscontiAttivi = paramRateiRiscontiAttivi;
	}

	/**
	 * @param righeCentroCosto
	 *            The righeCentroCosto to set.
	 */
	public void setRigheCentroCosto(Set<RigaCentroCosto> righeCentroCosto) {
		this.righeCentroCosto = righeCentroCosto;
	}

	/**
	 * @param righeRateoRisconto
	 *            The righeRateoRisconto to set.
	 */
	public void setRigheRateoRisconto(List<RigaRateoRisconto> righeRateoRisconto) {
		this.righeRateoRisconto = righeRateoRisconto;
	}

	@Override
	public String toString() {
		return "RigaContabile [importoDare=" + importoDare + ", importoAvere=" + importoAvere + ", conto=" + conto
				+ ", getContoAvere()=" + getContoAvere() + ", getContoDare()=" + getContoDare() + "]";
	}

}
