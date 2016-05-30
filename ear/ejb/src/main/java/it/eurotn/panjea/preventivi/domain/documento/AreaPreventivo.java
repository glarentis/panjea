package it.eurotn.panjea.preventivi.domain.documento;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.DatiValidazione;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IStatoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.TotaliArea;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoNote;
import it.eurotn.panjea.preventivi.domain.documento.interfaces.IAreaDocumentoTestata;
import it.eurotn.util.PanjeaEJBUtil;

import java.util.Calendar;
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
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Audited
@Table(name = "prev_area_preventivi")
@Entity
@NamedQueries({ @NamedQuery(name = "AreaPreventivo.countAree", query = "select count(ap.id) from AreaPreventivo ap where ap.documento.codiceAzienda = :paramCodiceAzienda") })
public class AreaPreventivo extends EntityBase implements IAreaDocumentoTestata, Cloneable {

	public enum StatoAreaPreventivo implements IStatoDocumento {
		PROVVISORIO(true), IN_ATTESA(false), ACCETTATO(false), RIFIUTATO(false);

		private boolean provvisorio;

		private StatoAreaPreventivo(boolean provvisorio) {
			this.provvisorio = provvisorio;
		}

		@Override
		public boolean isProvvisorio() {
			return provvisorio;
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Long dataCreazioneTimeStamp;

	@Transient
	private boolean inserimentoBloccato;

	@ManyToOne
	private TipoAreaPreventivo tipoAreaPreventivo;

	@ManyToOne
	private Listino listinoAlternativo;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "areaPreventivo", cascade = CascadeType.REMOVE)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<RigaPreventivo> righe;

	@ManyToOne
	private Listino listino;

	@OneToOne
	private Documento documento;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "valid", column = @Column(name = "validRighePreventivo")),
			@AttributeOverride(name = "validData", column = @Column(name = "validDataRighePreventivo")),
			@AttributeOverride(name = "validUtente", column = @Column(name = "validUtenteRighePreventivo")) })
	private DatiValidazione datiValidazioneRighe;

	@Index(name = "dataRegistrazione")
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataRegistrazione;

	@Index(name = "dataScadenza")
	@Temporal(TemporalType.DATE)
	private Date dataScadenza;

	@Temporal(TemporalType.DATE)
	private Date dataConsegna;

	private boolean addebitoSpeseIncasso;

	@Index(name = "annoMovimento")
	private Integer annoMovimento;

	@Embedded
	private TotaliArea totaliArea;

	@OneToOne(cascade = CascadeType.ALL)
	@Fetch(FetchMode.JOIN)
	private AreaPreventivoNote areaPreventivoNote;

	@ManyToOne
	private CodiceIva codiceIvaAlternativo;

	@Formula("(select count(ro.id) from prev_righe_preventivo ro  where ro.areaPreventivo_id=id)")
	@NotAudited
	private int numeroRighe;

	private ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo;

	private StatoAreaPreventivo statoAreaPreventivo;

	@Temporal(TemporalType.DATE)
	private Date dataAccettazione;

	@Formula("( select if(sum(coalesce(rigaOrd.qta,0))>0,1,0) from ordi_righe_ordine rigaOrd inner join prev_righe_preventivo rigaPrev on rigaOrd.rigaPreventivoCollegata_id = rigaPrev.id where rigaPrev.areaPreventivo_id = id)")
	@NotAudited
	private boolean processato;

	private StatoSpedizione statoSpedizione;

	/**
	 * init dei valori.
	 */
	{
		this.statoAreaPreventivo = StatoAreaPreventivo.PROVVISORIO;
		this.totaliArea = new TotaliArea();
		this.datiValidazioneRighe = new DatiValidazione();
		this.annoMovimento = -1;
		this.tipologiaCodiceIvaAlternativo = ETipologiaCodiceIvaAlternativo.NESSUNO;
		this.documento = new Documento();
		this.areaPreventivoNote = new AreaPreventivoNote();
		this.statoSpedizione = StatoSpedizione.NON_SPEDITO;
	}

	/**
	 * @param blocca
	 *            true per bloccare il preventivo,, false per sbloccarlo.
	 */
	public void bloccaPreventivo(boolean blocca) {
		// if (statoAreaPreventivo == StatoAreaPreventivo.PROVVISORIO) {
		// throw new IllegalStateException("Impossibile bloccare un preventivo in stato provvisorio");
		// }
		// this.statoAreaPreventivo = blocca ? StatoAreaPreventivo.BLOCCATO : StatoAreaPreventivo.CONFERMATO;
	}

	@Override
	public Object clone() {
		AreaPreventivo areaClone = PanjeaEJBUtil.cloneObject(this);
		areaClone.setId(null);
		areaClone.setVersion(null);

		areaClone.getDocumento().setId(null);
		areaClone.getDocumento().setVersion(null);
		areaClone.getDocumento().getCodice().setCodice(null);
		areaClone.setStatoAreaPreventivo(StatoAreaPreventivo.PROVVISORIO);

		areaClone.getAreaPreventivoNote().setId(null);
		areaClone.getAreaPreventivoNote().setVersion(null);

		areaClone.getDatiValidazioneRighe().invalida();
		areaClone.setDataAccettazione(null);

		for (RigaPreventivo rigaPreventivo : areaClone.getRighe()) {
			rigaPreventivo.setId(null);
			rigaPreventivo.setVersion(null);
			rigaPreventivo.setAreaPreventivo(areaClone);
		}

		return areaClone;
	}

	@Override
	public Map<String, Object> fillVariables() {
		return new HashMap<String, Object>();
	}

	/**
	 * @return the annoMovimento
	 */
	public Integer getAnnoMovimento() {
		return annoMovimento;
	}

	@Override
	public IAreaDocumentoNote getAreaDocumentoNote() {
		return getAreaPreventivoNote();
	}

	/**
	 * @return the AreaPreventivoNote
	 */
	public AreaPreventivoNote getAreaPreventivoNote() {
		if (areaPreventivoNote == null) {
			areaPreventivoNote = new AreaPreventivoNote();
		}
		return areaPreventivoNote;
	}

	/**
	 * @return the codiceIvaAlternativo
	 */
	public CodiceIva getCodiceIvaAlternativo() {
		return codiceIvaAlternativo;
	}

	/**
	 * @return the dataAccettazione
	 */
	public Date getDataAccettazione() {
		return dataAccettazione;
	}

	/**
	 * @return the dataConsegna
	 */
	public Date getDataConsegna() {
		return dataConsegna;
	}

	/**
	 * @return Returns the dataCreazioneTimeStamp.
	 */
	public Long getDataCreazioneTimeStamp() {
		return dataCreazioneTimeStamp;
	}

	/**
	 * @return the dataRegistrazione
	 */
	@Override
	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	/**
	 * @return the dataConsegna
	 */
	public Date getDataScadenza() {
		return dataScadenza;
	}

	/**
	 * @return the datiValidazioneRighe
	 */
	public DatiValidazione getDatiValidazioneRighe() {
		return datiValidazioneRighe;
	}

	/**
	 * @return the documento
	 */
	@Override
	public Documento getDocumento() {
		return documento;
	}

	/**
	 * @return the listino
	 */
	public Listino getListino() {
		return listino;
	}

	/**
	 * @return the listinoAlternativo
	 */
	public Listino getListinoAlternativo() {
		return listinoAlternativo;
	}

	/**
	 * @return the numeroRighe
	 */
	public int getNumeroRighe() {
		return numeroRighe;
	}

	/**
	 * @return Returns the righe.
	 */
	public Set<RigaPreventivo> getRighe() {
		return righe;
	}

	@Override
	public IStatoDocumento getStato() {
		return getStatoAreaPreventivo();
	}

	/**
	 * @return the statoAreaPreventivo
	 */
	public StatoAreaPreventivo getStatoAreaPreventivo() {
		return statoAreaPreventivo;
	}

	/**
	 * @return the statoSpedizione
	 */
	public StatoSpedizione getStatoSpedizione() {
		return statoSpedizione;
	}

	@Override
	public ITipoAreaDocumento getTipoArea() {
		return getTipoAreaPreventivo();
	}

	@Override
	public ITipoAreaDocumento getTipoAreaDocumento() {
		return tipoAreaPreventivo;
	}

	/**
	 * @return the tipoAreaPreventivo
	 */
	public TipoAreaPreventivo getTipoAreaPreventivo() {
		return tipoAreaPreventivo;
	}

	/**
	 * @return the tipologiaCodiceIvaAlternativo
	 */
	public ETipologiaCodiceIvaAlternativo getTipologiaCodiceIvaAlternativo() {
		return tipologiaCodiceIvaAlternativo;
	}

	/**
	 * @return the totaliArea
	 */
	public TotaliArea getTotaliArea() {
		return totaliArea;
	}

	/**
	 *
	 * @return true se la data accettazione è settata.
	 */
	public boolean isAccettato() {
		return dataAccettazione != null;
	}

	/**
	 * @return the addebitoSpeseIncasso
	 */
	public boolean isAddebitoSpeseIncasso() {
		return addebitoSpeseIncasso;
	}

	/**
	 * @return Returns the inserimentoBloccato.
	 */
	public boolean isInserimentoBloccato() {
		return inserimentoBloccato;
	}

	/**
	 * @return the processato
	 */
	public boolean isProcessato() {
		return processato;
	}

	/**
	 *
	 * @return true se non accettato e data scadenza superata.
	 */
	public boolean isScaduto() {
		return !isAccettato() && dataScadenza.before(Calendar.getInstance().getTime());
	}

	/**
	 * @param addebitoSpeseIncasso
	 *            the addebitoSpeseIncasso to set
	 */
	public void setAddebitoSpeseIncasso(boolean addebitoSpeseIncasso) {
		this.addebitoSpeseIncasso = addebitoSpeseIncasso;
	}

	/**
	 * @param annoMovimento
	 *            the annoMovimento to set
	 */
	public void setAnnoMovimento(Integer annoMovimento) {
		this.annoMovimento = annoMovimento;
	}

	/**
	 * @param areaPreventivoNote
	 *            the areaPreventivoNote to set
	 */
	public void setAreaPreventivoNote(AreaPreventivoNote areaPreventivoNote) {
		this.areaPreventivoNote = areaPreventivoNote;
	}

	/**
	 * @param codiceIvaAlternativo
	 *            the codiceIvaAlternativo to set
	 */
	public void setCodiceIvaAlternativo(CodiceIva codiceIvaAlternativo) {
		this.codiceIvaAlternativo = codiceIvaAlternativo;
	}

	/**
	 * @param dataAccettazione
	 *            the dataAccettazione to set
	 */
	public void setDataAccettazione(Date dataAccettazione) {
		this.dataAccettazione = dataAccettazione;
	}

	/**
	 * @param dataConsegna
	 *            the dataConsegna to set
	 */
	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	/**
	 * @param dataCreazioneTimeStamp
	 *            The dataCreazioneTimeStamp to set.
	 */
	public void setDataCreazioneTimeStamp(Long dataCreazioneTimeStamp) {
		this.dataCreazioneTimeStamp = dataCreazioneTimeStamp;
	}

	/**
	 * @param dataRegistrazione
	 *            the dataRegistrazione to set
	 */
	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	/**
	 * @param dataScadenza
	 *            the dataConsegna to set
	 */
	public void setDataScadenza(Date dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

	/**
	 * @param datiValidazioneRighe
	 *            the datiValidazioneRighe to set
	 */
	public void setDatiValidazioneRighe(DatiValidazione datiValidazioneRighe) {
		this.datiValidazioneRighe = datiValidazioneRighe;
	}

	/**
	 * @param documento
	 *            the documento to set
	 */
	@Override
	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	/**
	 * @param inserimentoBloccato
	 *            The inserimentoBloccato to set.
	 */
	public void setInserimentoBloccato(boolean inserimentoBloccato) {
		this.inserimentoBloccato = inserimentoBloccato;
	}

	/**
	 * @param listino
	 *            the listino to set
	 */
	public void setListino(Listino listino) {
		this.listino = listino;
	}

	/**
	 * @param listinoAlternativo
	 *            the listinoAlternativo to set
	 */
	public void setListinoAlternativo(Listino listinoAlternativo) {
		this.listinoAlternativo = listinoAlternativo;
	}

	/**
	 * @param processato
	 *            the processato to set
	 */
	public void setProcessato(boolean processato) {
		this.processato = processato;
	}

	/**
	 * @param righe
	 *            the righe to set
	 */
	public void setRighe(Set<RigaPreventivo> righe) {
		this.righe = righe;
	}

	/**
	 * @param statoAreaPreventivo
	 *            the statoAreaPreventivo to set
	 */
	public void setStatoAreaPreventivo(StatoAreaPreventivo statoAreaPreventivo) {
		this.statoAreaPreventivo = statoAreaPreventivo;
	}

	/**
	 * @param statoSpedizione
	 *            the statoSpedizione to set
	 */
	public void setStatoSpedizione(StatoSpedizione statoSpedizione) {
		this.statoSpedizione = statoSpedizione;
	}

	@Override
	public void setTipoArea(ITipoAreaDocumento tipoArea) {
		setTipoAreaPreventivo(tipoAreaPreventivo);
	}

	@Override
	public void setTipoAreaDocumento(ITipoAreaDocumento tipoAreaDocumento) {
		tipoAreaPreventivo = (TipoAreaPreventivo) tipoAreaDocumento;
	}

	/**
	 * @param tipoAreaPreventivo
	 *            the tipoAreaPreventivo to set
	 */
	public void setTipoAreaPreventivo(TipoAreaPreventivo tipoAreaPreventivo) {
		this.tipoAreaPreventivo = tipoAreaPreventivo;
	}

	/**
	 * @param tipologiaCodiceIvaAlternativo
	 *            the tipologiaCodiceIvaAlternativo to set
	 */
	public void setTipologiaCodiceIvaAlternativo(ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo) {
		this.tipologiaCodiceIvaAlternativo = tipologiaCodiceIvaAlternativo;
	}

	/**
	 * @param totaliArea
	 *            the totaliArea to set
	 */
	public void setTotaliArea(TotaliArea totaliArea) {
		this.totaliArea = totaliArea;
	}

	@Override
	public String toString() {
		StringBuffer retValue = new StringBuffer();
		retValue.append("AreaPreventivo[ ").append(super.toString()).append(" documento = ").append(this.documento)
				.append(" dataRegistrazione = ").append(this.dataRegistrazione);
		return retValue.toString();
	}
}
