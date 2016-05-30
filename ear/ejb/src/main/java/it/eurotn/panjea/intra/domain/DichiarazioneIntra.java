package it.eurotn.panjea.intra.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.intra.domain.dichiarazione.FileDichiarazione;
import it.eurotn.panjea.intra.domain.dichiarazione.SoggettoDelegato;
import it.eurotn.panjea.intra.domain.dichiarazione.SoggettoObbligato;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;

@Entity
@Audited
@Table(name = "intr_dichiarazioni", uniqueConstraints = @UniqueConstraint(columnNames = { "codice", "anno",
		"TIPO_DICHIARAZIONE" }))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_DICHIARAZIONE", discriminatorType = DiscriminatorType.STRING, length = 1)
@DiscriminatorValue("N")
@NamedQueries({
		@NamedQuery(name = "DichiarazioneIntra.caricaDichiarazioniByClasse", query = "select d from DichiarazioneIntra d where d.class=:classeDichiarazione order by d.codice desc"),
		@NamedQuery(name = "DichiarazioneIntra.caricaUltimaDichiarazione", query = "select d from DichiarazioneIntra d order by d.codice desc"),
		@NamedQuery(name = "DichiarazioneIntra.caricaDichiarazioni", query = "select d from DichiarazioneIntra d order by d.codice desc"),
		@NamedQuery(name = "DichiarazioneIntra.caricaDichiarazioniDaPresentare", query = "select d from DichiarazioneIntra d where d.fileDichiarazione is null order by d.codice desc") })
public class DichiarazioneIntra extends EntityBase {

	public enum PeriodoRiferimentoTrimestre {
		MESE1, MESE12, MESE123;
	}

	public enum TipoDichiarazione {
		ACQUISTI, VENDITE;

		/**
		 * @return classe della dichiarazione.
		 */
		public Class<? extends DichiarazioneIntra> getClasseDichiarazione() {
			switch (this.ordinal()) {
			case 0:
				return DichiarazioneIntraAcquisti.class;
			case 1:
				return DichiarazioneIntraVendite.class;
			default:
				throw new IllegalArgumentException("Tipo dichiarazione non prevista: " + this.name());
			}
		}

		/**
		 * @return istanza della tipologia di dichiarazione basata sull'enum
		 */
		public DichiarazioneIntra istanziaDichiarazione() {
			DichiarazioneIntra dichiarazioneIntra = null;
			switch (this.ordinal()) {
			case 0:
				dichiarazioneIntra = new DichiarazioneIntraAcquisti();
				break;
			case 1:
				dichiarazioneIntra = new DichiarazioneIntraVendite();
				break;
			default:
				throw new IllegalArgumentException("Tipo dichiarazione non prevista: " + this.name());
			}
			DateTime dataIntra = DateTime.now();
			dichiarazioneIntra.setAnno(dataIntra.getYear());
			int month = dataIntra.getMonthOfYear();
			dichiarazioneIntra.setMese(month);
			int tri = ((month - 1) / 3) + 1;
			dichiarazioneIntra.setTrimestre(tri);
			return dichiarazioneIntra;
		}
	}

	private static final long serialVersionUID = -3059318766658443876L;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	private FileDichiarazione fileDichiarazione;
	private Integer mese;

	private Integer trimestre;

	private Integer anno;
	private PeriodoRiferimentoTrimestre periodoRiferimentoTrimestre;
	private Integer codice;
	@Embedded
	private SoggettoObbligato soggettoObbligato;
	@Embedded
	private SoggettoDelegato soggettoDelegato;
	@Transient
	private TotaliDichiarazione totali;

	private Date data;

	@Enumerated
	private TipoPeriodo tipoPeriodo;

	private Integer percValoreStatistico;

	/**
	 * Costruttore.
	 */
	public DichiarazioneIntra() {
		soggettoObbligato = new SoggettoObbligato();
		soggettoDelegato = new SoggettoDelegato();
		periodoRiferimentoTrimestre = PeriodoRiferimentoTrimestre.MESE123;
		data = Calendar.getInstance().getTime();
		codice = 1;
		trimestre = 0;
		tipoPeriodo = TipoPeriodo.M;
		percValoreStatistico = 0;
	}

	/**
	 * Riempie i dati di questa dichiarazione partendo dai dati dell'ultima dichiarazione.
	 * 
	 * @param ultimaDichiarazione
	 *            ultimaDichiarazione generata
	 */
	public void fillByDichiarazione(DichiarazioneIntra ultimaDichiarazione) {
		if (ultimaDichiarazione == null) {
			return;
		}
		PanjeaEJBUtil.copyProperties(this, ultimaDichiarazione);
		setVersion(null);
		setId(null);
		codice++;
	}

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
	 * 
	 * @return casi particolare per l'esportazione del file
	 */
	public String getCasiParticolari() {
		if (!soggettoObbligato.isElenchiPrecedenti()) {
			if (soggettoObbligato.isCessazioneAttivita()) {
				return "9";
			} else {
				return "7";
			}
		}
		if (soggettoObbligato.isCessazioneAttivita()) {
			return "8";
		}
		return "0";
	}

	/**
	 * @return Returns the codice.
	 */
	public Integer getCodice() {
		return codice;
	}

	/**
	 * @return Returns the data.
	 */
	public Date getData() {
		return data;
	}

	/**
	 * 
	 * @return data iniziale della dichiarazione basata sulla periodicità e mese o trimestre impostato
	 */
	public Date getDataFinale() {
		int meseFinale = -1;
		if (tipoPeriodo.equals(TipoPeriodo.M)) {
			meseFinale = mese;
		} else {
			meseFinale = trimestre * 3;
		}
		return new DateTime(anno, meseFinale, 1, 0, 0).dayOfMonth().withMaximumValue().toDate();
	}

	/**
	 * 
	 * @return data iniziale della dichiarazione basata sulla periodicità e mese o trimestre impostato
	 */
	public Date getDataIniziale() {
		int meseIniziale = -1;
		if (tipoPeriodo.equals(TipoPeriodo.M)) {
			meseIniziale = mese;
		} else {
			meseIniziale = trimestre * 3 - 2;
		}
		return new DateTime(anno, meseIniziale, 1, 0, 0).toDate();
	}

	/**
	 * 
	 * @return stringa contenente alcuni dati della dichiarazione. Utile per stampare.
	 */
	public String getDescrizioneDichiarazione() {
		return "";
	}

	/**
	 * @return Returns the fileDichiarazione.
	 */
	public FileDichiarazione getFileDichiarazione() {
		return fileDichiarazione;
	}

	/**
	 * @return Returns the mese.
	 */
	public Integer getMese() {
		return mese;
	}

	/**
	 * 
	 * @return se presente il soggetto delegato piva di quest'ultimo, altrimenti la piva del sogg. obbligato
	 */
	public String getPartitaIvaPresentatore() {
		if (soggettoDelegato != null && !StringUtils.isBlank(soggettoDelegato.getPartitaIvaSoggettoDelegato())) {
			return soggettoDelegato.getPartitaIvaSoggettoDelegato();
		}
		return soggettoObbligato.getPartitaIva();
	}

	/**
	 * @return the percValoreStatistico
	 */
	public Integer getPercValoreStatistico() {
		return percValoreStatistico;
	}

	/**
	 * 
	 * @return mese o trimestre in base a {@link TipoPeriodo}
	 */
	public int getPeriodo() {
		if (tipoPeriodo == TipoPeriodo.M) {
			return mese;
		}
		return trimestre;
	}

	/**
	 * @return Returns the periodoRiferimentoTrimestre.
	 */
	public PeriodoRiferimentoTrimestre getPeriodoRiferimentoTrimestre() {
		return periodoRiferimentoTrimestre;
	}

	/**
	 * @return Returns the soggettoDelegato.
	 */
	public SoggettoDelegato getSoggettoDelegato() {
		return soggettoDelegato;
	}

	/**
	 * @return Returns the soggettoObbligato.
	 */
	public SoggettoObbligato getSoggettoObbligato() {
		return soggettoObbligato;
	}

	/**
	 * 
	 * @return tipo di dichiarazione
	 */
	public TipoDichiarazione getTipoDichiarazione() {
		throw new UnsupportedOperationException("non posso chiaramare un istanza diretta di dichiarazioneIntra");
	}

	/**
	 * @return Returns the tipoPeriodo.
	 */
	public TipoPeriodo getTipoPeriodo() {
		return tipoPeriodo;
	}

	/**
	 * 
	 * @return carattere di descrizione utilizzato per l'esportazione del file
	 */
	public String getTipoRiepilogo() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return Returns the totali.
	 */
	public TotaliDichiarazione getTotali() {
		return totali;
	}

	/**
	 * @return Returns the trimestre.
	 */
	public Integer getTrimestre() {
		return trimestre;
	}

	@PostLoad
	private void init() {
		if (soggettoDelegato == null) {
			soggettoDelegato = new SoggettoDelegato();
		}
		if (soggettoObbligato == null) {
			soggettoObbligato = new SoggettoObbligato();
		}
	}

	/**
	 * @param anno
	 *            The anno to set.
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	/**
	 * @param codice
	 *            The codice to set.
	 */
	public void setCodice(Integer codice) {
		this.codice = codice;
	}

	/**
	 * @param data
	 *            The data to set.
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @param fileDichiarazione
	 *            The fileDichiarazione to set.
	 */
	public void setFileDichiarazione(FileDichiarazione fileDichiarazione) {
		this.fileDichiarazione = fileDichiarazione;
	}

	/**
	 * @param mese
	 *            The mese to set.
	 */
	public void setMese(Integer mese) {
		this.mese = mese;
	}

	/**
	 * @param percValoreStatistico
	 *            the percValoreStatistico to set
	 */
	public void setPercValoreStatistico(Integer percValoreStatistico) {
		this.percValoreStatistico = percValoreStatistico;
	}

	/**
	 * @param periodoRiferimentoTrimestre
	 *            The periodoRiferimentoTrimestre to set.
	 */
	public void setPeriodoRiferimentoTrimestre(PeriodoRiferimentoTrimestre periodoRiferimentoTrimestre) {
		this.periodoRiferimentoTrimestre = periodoRiferimentoTrimestre;
	}

	/**
	 * @param soggettoDelegato
	 *            The soggettoDelegato to set.
	 */
	public void setSoggettoDelegato(SoggettoDelegato soggettoDelegato) {
		this.soggettoDelegato = soggettoDelegato;
	}

	/**
	 * @param soggettoObbligato
	 *            The soggettoObbligato to set.
	 */
	public void setSoggettoObbligato(SoggettoObbligato soggettoObbligato) {
		this.soggettoObbligato = soggettoObbligato;
	}

	/**
	 * @param tipoPeriodo
	 *            The tipoPeriodo to set.
	 */
	public void setTipoPeriodo(TipoPeriodo tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
		if (tipoPeriodo != null) {
			if (tipoPeriodo.equals(TipoPeriodo.M)) {
				trimestre = 0;
			} else {
				mese = 0;
			}
		}
	}

	/**
	 * @param totali
	 *            The totali to set.
	 */
	public void setTotali(TotaliDichiarazione totali) {
		this.totali = totali;
	}

	/**
	 * @param trimestre
	 *            The trimestre to set.
	 */
	public void setTrimestre(Integer trimestre) {
		this.trimestre = trimestre;
	}

}
