package it.eurotn.panjea.contabilita.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.IStatoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "cont_area_contabile")
@NamedQueries({
		@NamedQuery(name = "AreaContabile.ricercaByDocumento", query = "select a.id from AreaContabile a inner join a.documento d where d.id = :paramIdDocumento "),
		@NamedQuery(name = "AreaContabile.ricercaAreaByDocumento", query = "select a from AreaContabile a inner join a.documento d where d.id = :paramIdDocumento "),
		@NamedQuery(name = "AreaContabile.ricercaByTipiAreaContabileAnnoContabile", query = "from AreaContabile a where a.annoMovimento<= :paramAnno and a.tipoAreaContabile in (:paramTipiAreaContabile) order by a.dataRegistrazione desc, a.id desc "),
		@NamedQuery(name = "AreaContabile.caricaIdAreeContabilePerGiornale", query = "select a.id from AreaContabile a where a.documento.codiceAzienda=:paramCodiceAzienda and a.dataRegistrazione between :paramDaDataRegistrazione and :paramADataRegistrazione and a.annoMovimento = :paramAnnoMovimento"),
		@NamedQuery(name = "AreaContabile.caricaAreeContabilePerLiquidazione", query = "select a from AreaContabile a where a.documento.codiceAzienda=:paramCodiceAzienda and a.dataRegistrazione between :paramDaDataRegistrazione and "
				+ ":paramADataRegistrazione and a.annoMovimento = :paramAnnoMovimento and a.tipoAreaContabile.tipoDocumento=:tipoDocumento") })
public class AreaContabile extends EntityBase implements java.io.Serializable, IAreaDocumento, Cloneable {

	public enum StatoAreaContabile implements IStatoDocumento {
		CONFERMATO(false), VERIFICATO(true), SIMULATO(false), PROVVISORIO(true);
		private boolean provvisorio;

		private StatoAreaContabile(boolean provvisorio) {
			this.provvisorio = provvisorio;
		}

		@Override
		public boolean isProvvisorio() {
			return provvisorio;
		}
	}

	private static final long serialVersionUID = 6644693852852070671L;

	@ManyToOne
	private TipoAreaContabile tipoAreaContabile;

	@Index(name = "stato")
	private StatoAreaContabile statoAreaContabile;

	@OneToOne
	private Documento documento;

	@Embedded
	private CodiceDocumento codice;

	private Integer valoreProtocollo;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "codice", column = @Column(name = "codiceCollegato", length = 30)),
			@AttributeOverride(name = "codiceOrder", column = @Column(name = "codiceOrderCollegato", length = 60)) })
	private CodiceDocumento codiceCollegato;

	private Integer valoreProtocolloCollegato;

	@Index(name = "dataRegistrazione")
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataRegistrazione;
	// DATI CONTABILI

	@Index(name = "annoMovimento")
	@Column(nullable = false)
	private Integer annoMovimento;

	private BigDecimal cambio;

	@Column(length = 250)
	private String note;

	private boolean validRigheContabili;

	@Column(length = 50)
	private String validUtenteRigheContabili;

	@Temporal(TemporalType.DATE)
	private Date validDataRigheContabili;

	private boolean squadratoRigheContabili;

	private Integer numeroPaginaGiornale;
	// DATI IVA

	@Index(name = "annoIva")
	private int annoIva;

	private boolean stampatoSuRegistro;

	/*
	 * attributo che identifica l'oggetto in base ai valori importati es. da EUROPA :
	 * dataRegistrazione|utenteRegistrazione|Progressivo
	 */
	@Index(name = "chiaveImportazione")
	private String chiaveImportazione;

	@OneToMany(mappedBy = "areaContabile", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<RigaContabile> righeContabili;

	@Embedded
	private DatiRitenutaAccontoAreaContabile datiRitenutaAccontoAreaContabile;

	@Embedded
	private DatiGenerazione datiGenerazione;

	{
		this.documento = new Documento();
		this.statoAreaContabile = StatoAreaContabile.PROVVISORIO;
		this.annoMovimento = -1;
		this.numeroPaginaGiornale = 0;
		this.stampatoSuRegistro = false;
		this.datiRitenutaAccontoAreaContabile = new DatiRitenutaAccontoAreaContabile();
		this.codice = new CodiceDocumento();
		this.codiceCollegato = new CodiceDocumento();
		this.tipoAreaContabile = new TipoAreaContabile();
		this.datiGenerazione = new DatiGenerazione();
	}

	/**
	 * Costruttore.
	 */
	public AreaContabile() {
		super();
	}

	/**
	 * Costruttore.
	 *
	 * @param annoMovimento
	 *            anno di movimento
	 * @param dataRegistrazione
	 *            data di registrazione
	 * @param tipoAreaContabile
	 *            tipo area contabile
	 */
	public AreaContabile(final int annoMovimento, final Date dataRegistrazione,
			final TipoAreaContabile tipoAreaContabile) {
		this();
		this.tipoAreaContabile = tipoAreaContabile;
		this.dataRegistrazione = dataRegistrazione;
		this.annoMovimento = annoMovimento;
		this.documento.setId(null);
		this.documento.setDataDocumento(dataRegistrazione);
		this.documento.setTipoDocumento(tipoAreaContabile.getTipoDocumento());
		this.documento.setRapportoBancarioAzienda(null);
		this.documento.setEntita(null);
		this.documento.setTotale(new Importo());
		this.documento.setContrattoSpesometro(null);
	}

	@Override
	public AreaContabile clone() throws CloneNotSupportedException {
		AreaContabile areaContabileNew = new AreaContabile();
		Documento documentoNew = new Documento();
		areaContabileNew.setDocumento(documentoNew);
		areaContabileNew.setAnnoIva(getAnnoIva());
		areaContabileNew.setAnnoMovimento(getAnnoMovimento());
		areaContabileNew.setId(getId());
		areaContabileNew.setNumeroPaginaGiornale(getNumeroPaginaGiornale());
		areaContabileNew.setVersion(getVersion());
		areaContabileNew.setCambio(getCambio());
		areaContabileNew.setChiaveImportazione(getChiaveImportazione());
		areaContabileNew.setDataRegistrazione(getDataRegistrazione());
		areaContabileNew.setNote(getNote());
		areaContabileNew.setCodice(getCodice());
		areaContabileNew.setStatoAreaContabile(getStatoAreaContabile());
		areaContabileNew.setTipoAreaContabile(getTipoAreaContabile());
		areaContabileNew.setValidDataRigheContabili(getValidDataRigheContabili());
		areaContabileNew.setValidUtenteRigheContabili(getValidUtenteRigheContabili());

		areaContabileNew.getDocumento().setCodice(getDocumento().getCodice());
		areaContabileNew.getDocumento().setId(getDocumento().getId());
		areaContabileNew.getDocumento().setVersion(getDocumento().getVersion());
		areaContabileNew.getDocumento().setCodiceAzienda(getDocumento().getCodiceAzienda());
		areaContabileNew.getDocumento().setDataDocumento(getDocumento().getDataDocumento());
		areaContabileNew.getDocumento().setEntita(getDocumento().getEntita());
		areaContabileNew.getDocumento().setRapportoBancarioAzienda(getDocumento().getRapportoBancarioAzienda());
		areaContabileNew.getDocumento().setTipoDocumento(getDocumento().getTipoDocumento());
		areaContabileNew.getDocumento().setTotale(getDocumento().getTotale().clone());
		return areaContabileNew;
	}

	@Override
	public Map<String, Object> fillVariables() {
		return new HashMap<String, Object>();
	}

	/**
	 * @return Returns the annoIva.
	 */
	public int getAnnoIva() {
		return annoIva;
	}

	/**
	 * @return Returns the annoMovimento.
	 */
	public Integer getAnnoMovimento() {
		return annoMovimento;
	}

	/**
	 * @return {@link AreaContabileLite} basata sull'area contabile.
	 */
	public AreaContabileLite getAreaContabileLite() {
		AreaContabileLite areaContabileLite = new AreaContabileLite();
		areaContabileLite.setId(this.getId());
		areaContabileLite.setVersion(this.getVersion());
		areaContabileLite.setCodice(this.getCodice());
		areaContabileLite.setDocumento(this.getDocumento());
		areaContabileLite.setStatoAreaContabile(this.getStatoAreaContabile());
		areaContabileLite.setTimeStamp(this.getTimeStamp());
		areaContabileLite.setUserInsert(this.getUserInsert());
		return areaContabileLite;
	}

	/**
	 * @return Returns the cambio.
	 */
	public BigDecimal getCambio() {
		return cambio;
	}

	/**
	 * @return Returns the chiaveImportazione.
	 */
	public String getChiaveImportazione() {
		return chiaveImportazione;
	}

	/**
	 * @return the codice
	 */
	public CodiceDocumento getCodice() {
		return codice;
	}

	/**
	 * @return the codiceCollegato
	 */
	public CodiceDocumento getCodiceCollegato() {
		return codiceCollegato;
	}

	/**
	 * @return Returns the dataRegistrazione.
	 */
	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return the datiGenerazione
	 */
	public DatiGenerazione getDatiGenerazione() {
		if (datiGenerazione == null) {
			datiGenerazione = new DatiGenerazione();
		}

		return datiGenerazione;
	}

	/**
	 * @return the datiRitenutaAccontoAreaContabile
	 */
	public DatiRitenutaAccontoAreaContabile getDatiRitenutaAccontoAreaContabile() {
		if (datiRitenutaAccontoAreaContabile == null) {
			datiRitenutaAccontoAreaContabile = new DatiRitenutaAccontoAreaContabile();
		}
		return datiRitenutaAccontoAreaContabile;
	}

	/**
	 * @return Returns the documento.
	 */
	@Override
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @return the numeroPaginaGiornale
	 */
	public Integer getNumeroPaginaGiornale() {
		return numeroPaginaGiornale;
	}

	/**
	 * @return the righeContabili
	 */
	public Set<RigaContabile> getRigheContabili() {
		return righeContabili;
	}

	@Override
	public IStatoDocumento getStato() {
		return getStatoAreaContabile();
	}

	/**
	 * @return the statoAreaContabile
	 */
	public StatoAreaContabile getStatoAreaContabile() {
		return statoAreaContabile;
	}

	@Override
	public StatoSpedizione getStatoSpedizione() {
		return null;
	}

	/**
	 * @return Returns the tipoAreaContabile.
	 */
	public TipoAreaContabile getTipoAreaContabile() {
		return tipoAreaContabile;
	}

	@Override
	public ITipoAreaDocumento getTipoAreaDocumento() {
		return getTipoAreaContabile();
	}

	/**
	 * @return Returns the validDataRigheContabili.
	 */
	public Date getValidDataRigheContabili() {
		return validDataRigheContabili;
	}

	/**
	 * @return Returns the validUtenteRigheContabili.
	 */
	public String getValidUtenteRigheContabili() {
		return validUtenteRigheContabili;
	}

	/**
	 * @return the valoreProtocollo
	 */
	public Integer getValoreProtocollo() {
		return valoreProtocollo;
	}

	/**
	 * @return the valoreProtocolloCollegato
	 */
	public Integer getValoreProtocolloCollegato() {
		return valoreProtocolloCollegato;
	}

	/**
	 * @return Returns the squadratoRigheContabili
	 */
	public boolean isSquadratoRigheContabili() {
		return squadratoRigheContabili;
	}

	/**
	 * @return Returns the stampatoSuRegistro.
	 */
	public boolean isStampatoSuRegistro() {
		return stampatoSuRegistro;
	}

	/**
	 * @return Returns the validRigheContabili.
	 */
	public boolean isValidRigheContabili() {
		return validRigheContabili;
	}

	/**
	 * @param annoIva
	 *            The annoIva to set.
	 */
	public void setAnnoIva(int annoIva) {
		this.annoIva = annoIva;
	}

	/**
	 * @param annoMovimento
	 *            The annoMovimento to set.
	 */
	public void setAnnoMovimento(Integer annoMovimento) {
		this.annoMovimento = annoMovimento;
	}

	/**
	 * @param cambio
	 *            The cambio to set.
	 */
	public void setCambio(BigDecimal cambio) {
		this.cambio = cambio;
	}

	/**
	 * @param chiaveImportazione
	 *            The chiaveImportazione to set.
	 */
	public void setChiaveImportazione(String chiaveImportazione) {
		this.chiaveImportazione = chiaveImportazione;
	}

	/**
	 * @param codice
	 *            the codice to set
	 */
	public void setCodice(CodiceDocumento codice) {
		this.codice = codice;
	}

	/**
	 * @param codiceCollegato
	 *            the codiceCollegato to set
	 */
	public void setCodiceCollegato(CodiceDocumento codiceCollegato) {
		this.codiceCollegato = codiceCollegato;
	}

	/**
	 * @param dataRegistrazione
	 *            The dataRegistrazione to set.
	 */
	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param datiGenerazione
	 *            the datiGenerazione to set
	 */
	public void setDatiGenerazione(DatiGenerazione datiGenerazione) {
		this.datiGenerazione = datiGenerazione;
	}

	/**
	 * @param datiRitenutaAccontoAreaContabile
	 *            the datiRitenutaAccontoAreaContabile to set
	 */
	public void setDatiRitenutaAccontoAreaContabile(DatiRitenutaAccontoAreaContabile datiRitenutaAccontoAreaContabile) {
		this.datiRitenutaAccontoAreaContabile = datiRitenutaAccontoAreaContabile;
	}

	/**
	 * @param documento
	 *            The documento to set.
	 */
	@Override
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	/**
	 * @param note
	 *            The note to set.
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @param numeroPaginaGiornale
	 *            The numeroPaginaGiornale to set.
	 */
	public void setNumeroPaginaGiornale(Integer numeroPaginaGiornale) {
		this.numeroPaginaGiornale = numeroPaginaGiornale;
	}

	/**
	 * @param righeContabili
	 *            the righeContabili to set
	 */
	public void setRigheContabili(Set<RigaContabile> righeContabili) {
		this.righeContabili = righeContabili;
	}

	/**
	 * @param squadratoRigheContabili
	 *            The squadratoRigheContabili to set.
	 */
	public void setSquadratoRigheContabili(boolean squadratoRigheContabili) {
		this.squadratoRigheContabili = squadratoRigheContabili;
	}

	/**
	 * @param stampatoSuRegistro
	 *            The stampatoSuRegistro to set.
	 */
	public void setStampatoSuRegistro(boolean stampatoSuRegistro) {
		this.stampatoSuRegistro = stampatoSuRegistro;
	}

	/**
	 * @param statoAreaContabile
	 *            The statoAreaContabile to set.
	 */
	public void setStatoAreaContabile(StatoAreaContabile statoAreaContabile) {
		this.statoAreaContabile = statoAreaContabile;
	}

	@Override
	public void setStatoSpedizione(StatoSpedizione statoSpedizione) {
	}

	/**
	 * @param tipoAreaContabile
	 *            The tipoAreaContabile to set.
	 */
	public void setTipoAreaContabile(TipoAreaContabile tipoAreaContabile) {
		this.tipoAreaContabile = tipoAreaContabile;
	}

	@Override
	public void setTipoAreaDocumento(ITipoAreaDocumento tipoAreaDocumento) {
		tipoAreaContabile = (TipoAreaContabile) tipoAreaDocumento;
	}

	/**
	 * @param validDataRigheContabili
	 *            The validDataRigheContabili to set.
	 */
	public void setValidDataRigheContabili(Date validDataRigheContabili) {
		this.validDataRigheContabili = validDataRigheContabili;
	}

	/**
	 * @param validRigheContabili
	 *            The validRigheContabili to set.
	 */
	public void setValidRigheContabili(boolean validRigheContabili) {
		// Regola di dominio: se le righe contabili erano valide e
		// successivamente mi vengono invalidate
		// lo stato del documento viene riportato a PROVVISORIO
		if (this.validRigheContabili && !validRigheContabili) {
			this.validDataRigheContabili = null;
			this.validUtenteRigheContabili = null;

			this.statoAreaContabile = StatoAreaContabile.PROVVISORIO;
		}
		this.validRigheContabili = validRigheContabili;
	}

	/**
	 * @param validUtenteRigheContabili
	 *            The validUtenteRigheContabili to set.
	 */
	public void setValidUtenteRigheContabili(String validUtenteRigheContabili) {
		this.validUtenteRigheContabili = validUtenteRigheContabili;
	}

	/**
	 * @param valoreProtocollo
	 *            the valoreProtocollo to set
	 */
	public void setValoreProtocollo(Integer valoreProtocollo) {
		this.valoreProtocollo = valoreProtocollo;
	}

	/**
	 * @param valoreProtocolloCollegato
	 *            the valoreProtocolloCollegato to set
	 */
	public void setValoreProtocolloCollegato(Integer valoreProtocolloCollegato) {
		this.valoreProtocolloCollegato = valoreProtocolloCollegato;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("AreaContabile[");
		buffer.append(super.toString());
		buffer.append("annoIva = ").append(annoIva);
		buffer.append(" annoMovimento = ").append(annoMovimento);
		buffer.append(" cambio = ").append(cambio);
		buffer.append(" dataRegistrazione = ").append(dataRegistrazione);
		buffer.append(" documento = ").append(documento != null ? documento.getId() : null);
		buffer.append(" note = ").append(note);
		buffer.append(" numeroPaginaGiornale = ").append(numeroPaginaGiornale);
		buffer.append(" stampatoSuRegistro = ").append(stampatoSuRegistro);
		buffer.append(" tipoAreaContabile = ").append(tipoAreaContabile);
		buffer.append(" statoAreaContabile = ").append(statoAreaContabile);
		buffer.append("]");
		return buffer.toString();
	}
}
