package it.eurotn.panjea.ordini.domain.documento;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.documenti.domain.DatiValidazione;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.IStatoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.TotaliArea;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.RiferimentiOrdine;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Definisce gli attributi per l'area ordine.
 *
 * @author adriano
 * @version 1.0, 27/ago/2008
 */
@Entity
@Table(name = "ordi_area_ordine")
@NamedQueries({
        @NamedQuery(name = "AreaOrdine.ricercaByDocumento", query = "select a from AreaOrdine a inner join a.documento d where d.id = :paramIdDocumento"),
        @NamedQuery(name = "AreaOrdine.countAree", query = "select count(ao.id) from AreaOrdine ao where ao.documento.codiceAzienda = :paramCodiceAzienda") })
@Audited
public class AreaOrdine extends EntityBase implements IAreaDocumento, Cloneable {
    /**
     * @author fattazzo
     */
    public enum StatoAreaOrdine implements IStatoDocumento {
        PROVVISORIO(true), CONFERMATO(false), BLOCCATO(false);

        private boolean provvisorio;

        private StatoAreaOrdine(final boolean provvisorio) {
            this.provvisorio = provvisorio;
        }

        @Override
        public boolean isProvvisorio() {
            return provvisorio;
        }
    }

    private static final long serialVersionUID = 7505184938379559032L;

    private Long dataCreazioneTimeStamp;

    @Column(length = 200)
    @NotAudited
    private String noteImportazione;

    @Transient
    private boolean inserimentoBloccato;

    @ManyToOne
    private TipoAreaOrdine tipoAreaOrdine;

    @ManyToOne
    private Listino listinoAlternativo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "areaOrdine", cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<RigaOrdine> righe;

    @ManyToOne
    private AgenteLite agente;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataInizioTrasporto;

    @ManyToOne
    private Listino listino;

    @OneToOne
    private Documento documento;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "valid", column = @Column(name = "validRigheOrdine") ),
            @AttributeOverride(name = "validData", column = @Column(name = "validDataRigheOrdine") ),
            @AttributeOverride(name = "validUtente", column = @Column(name = "validUtenteRigheOrdine") ) })
    private DatiValidazione datiValidazioneRighe;

    @Index(name = "dataRegistrazione")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataRegistrazione;

    @Index(name = "dataConsegna")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataConsegna;

    @ManyToOne
    private DepositoLite depositoOrigine;

    @Column(length = 40)
    private String causaleTrasporto;

    @Column(length = 45)
    private String trasportoCura;

    @Column(length = 35)
    private String tipoPorto;

    @ManyToOne
    @JoinColumn(name = "vettore_id")
    private VettoreLite vettore;

    @ManyToOne
    private SedeEntita sedeVettore;

    private boolean addebitoSpeseIncasso;

    private boolean raggruppamentoBolle;

    private Integer numeroColli;

    @Index(name = "annoMovimento")
    private Integer annoMovimento;

    @Embedded
    private TotaliArea totaliArea;

    @OneToOne(cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private AreaOrdineNote areaOrdineNote;

    @ManyToOne
    private CodiceIva codiceIvaAlternativo;

    @Formula("( select if(sum(if(rigaOrd.qta-coalesce((select sum(rm.qta*rm.moltQtaOrdine) from maga_righe_magazzino rm where rm.rigaOrdineCollegata_Id = rigaOrd.id),0)>0,1,0))> 0,0,1) from ordi_righe_ordine rigaOrd where rigaOrd.areaOrdine_id = id and rigaOrd.evasioneForzata = 0)")
    @NotAudited
    private boolean evaso;

    @Formula("(select count(ro.id) from ordi_righe_ordine ro  where ro.areaOrdine_id=id)")
    @NotAudited
    private int numeroRighe;

    private ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo;

    private StatoAreaOrdine statoAreaOrdine;

    @Embedded
    private RiferimentiOrdine riferimentiOrdine;

    private StatoSpedizione statoSpedizione;

    /**
     * init dei valori.
     */
    {
        this.statoAreaOrdine = StatoAreaOrdine.PROVVISORIO;
        this.totaliArea = new TotaliArea();
        this.datiValidazioneRighe = new DatiValidazione();
        this.annoMovimento = -1;
        this.tipologiaCodiceIvaAlternativo = ETipologiaCodiceIvaAlternativo.NESSUNO;
        this.raggruppamentoBolle = false;
        this.documento = new Documento();
        this.areaOrdineNote = new AreaOrdineNote();

        this.statoSpedizione = StatoSpedizione.NON_SPEDITO;
    }

    /**
     * @param bloccaOrdine
     *            true per bloccare l'ordine, false per sbloccarlo.
     */
    public void bloccaOrdine(boolean bloccaOrdine) {
        if (statoAreaOrdine == StatoAreaOrdine.PROVVISORIO) {
            throw new IllegalStateException("Impossibile bloccare un ordine in stato provvisorio");
        }
        this.statoAreaOrdine = bloccaOrdine ? StatoAreaOrdine.BLOCCATO : StatoAreaOrdine.CONFERMATO;
    }

    @Override
    public Object clone() {
        AreaOrdine areaOrdineClone = PanjeaEJBUtil.cloneObject(this);
        areaOrdineClone.setId(null);
        areaOrdineClone.setVersion(null);

        areaOrdineClone.getDocumento().setId(null);
        areaOrdineClone.getDocumento().setVersion(null);
        areaOrdineClone.getDocumento().getCodice().setCodice(null);
        areaOrdineClone.setStatoAreaOrdine(StatoAreaOrdine.PROVVISORIO);

        if (areaOrdineClone.getAreaOrdineNote() != null) {
            areaOrdineClone.getAreaOrdineNote().setId(null);
            areaOrdineClone.getAreaOrdineNote().setVersion(null);
        }

        areaOrdineClone.getDatiValidazioneRighe().invalida();

        Set<RigaOrdine> righeOrdini = new HashSet<>();
        for (RigaOrdine rigaOrdine : areaOrdineClone.getRighe()) {
            rigaOrdine.setId(null);
            rigaOrdine.setVersion(null);
            rigaOrdine.setAreaOrdine(areaOrdineClone);
            rigaOrdine.setRigaTestataCollegata(null);
            rigaOrdine.setRigaPreventivoCollegata(null);
            rigaOrdine.setRigheOrdineCollegate(null);
            rigaOrdine.setAreaPreventivoCollegata(null);
            rigaOrdine.setLivello(0);

            if (rigaOrdine instanceof RigaArticolo) {
                righeOrdini.add(rigaOrdine);
            }
        }
        areaOrdineClone.setRighe(righeOrdini);

        return areaOrdineClone;
    }

    @Override
    public Map<String, Object> fillVariables() {
        return new HashMap<String, Object>();
    }

    /**
     * @return Returns the agente.
     */
    public AgenteLite getAgente() {
        return agente;
    }

    /**
     * @return the annoMovimento
     */
    public Integer getAnnoMovimento() {
        return annoMovimento;
    }

    /**
     * @return the areaOrdineNote
     */
    public AreaOrdineNote getAreaOrdineNote() {
        if (areaOrdineNote == null) {
            areaOrdineNote = new AreaOrdineNote();
        }
        return areaOrdineNote;
    }

    /**
     * @return the causaleTrasporto
     */
    public String getCausaleTrasporto() {
        return causaleTrasporto;
    }

    /**
     * @return the codiceIvaAlternativo
     */
    public CodiceIva getCodiceIvaAlternativo() {
        return codiceIvaAlternativo;
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
     * @return Returns the dataInizioTrasporto.
     */
    public Date getDataInizioTrasporto() {
        return dataInizioTrasporto;
    }

    /**
     * @return the dataRegistrazione
     */
    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * @return the datiValidazioneRighe
     */
    public DatiValidazione getDatiValidazioneRighe() {
        return datiValidazioneRighe;
    }

    /**
     * @return the depositoOrigine
     */
    public DepositoLite getDepositoOrigine() {
        return depositoOrigine;
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
     * @return Returns the noteImportazione.
     */
    public String getNoteImportazione() {
        return noteImportazione;
    }

    /**
     * @return the numeroColli
     */
    public Integer getNumeroColli() {
        return numeroColli;
    }

    /**
     * @return the numeroRighe
     */
    public int getNumeroRighe() {
        return numeroRighe;
    }

    /**
     * @return the riferimentiOrdine
     */
    public RiferimentiOrdine getRiferimentiOrdine() {
        if (riferimentiOrdine == null) {
            riferimentiOrdine = new RiferimentiOrdine();
        }
        return riferimentiOrdine;
    }

    /**
     * @return Returns the righe.
     */
    public Set<RigaOrdine> getRighe() {
        return righe;
    }

    /**
     * @return the sedeVettore
     */
    public SedeEntita getSedeVettore() {
        return sedeVettore;
    }

    @Override
    public IStatoDocumento getStato() {
        return statoAreaOrdine;
    }

    /**
     * @return the statoAreaOrdine
     */
    public StatoAreaOrdine getStatoAreaOrdine() {
        return statoAreaOrdine;
    }

    /**
     * @return the statoSpedizione
     */
    public StatoSpedizione getStatoSpedizione() {
        return statoSpedizione;
    }

    @Override
    public ITipoAreaDocumento getTipoAreaDocumento() {
        return tipoAreaOrdine;
    }

    /**
     * @return the tipoAreaOrdine
     */
    public TipoAreaOrdine getTipoAreaOrdine() {
        return tipoAreaOrdine;
    }

    /**
     * @return the tipologiaCodiceIvaAlternativo
     */
    public ETipologiaCodiceIvaAlternativo getTipologiaCodiceIvaAlternativo() {
        return tipologiaCodiceIvaAlternativo;
    }

    /**
     * @return the tipoPorto
     */
    public String getTipoPorto() {
        return tipoPorto;
    }

    /**
     * @return the totaliArea
     */
    public TotaliArea getTotaliArea() {
        return totaliArea;
    }

    /**
     * @return the trasportoCura
     */
    public String getTrasportoCura() {
        return trasportoCura;
    }

    /**
     * @return the vettore
     */
    public VettoreLite getVettore() {
        return vettore;
    }

    /**
     * @return the addebitoSpeseIncasso
     */
    public boolean isAddebitoSpeseIncasso() {
        return addebitoSpeseIncasso;
    }

    /**
     *
     * @return true se i dati di importazione corrispondono ai dati della riga.
     */
    public boolean isDatiImportazioneCoerenti() {
        if (noteImportazione == null) {
            return true;
        }
        return noteImportazione.isEmpty();
    }

    /**
     * @return the evaso
     */
    public boolean isEvaso() {
        return evaso;
    }

    /**
     * @return Returns the inserimentoBloccato.
     */
    public boolean isInserimentoBloccato() {
        return inserimentoBloccato;
    }

    /**
     * @return the raggruppamentoBolle
     */
    public boolean isRaggruppamentoBolle() {
        return raggruppamentoBolle;
    }

    /**
     * @param addebitoSpeseIncasso
     *            the addebitoSpeseIncasso to set
     */
    public void setAddebitoSpeseIncasso(boolean addebitoSpeseIncasso) {
        this.addebitoSpeseIncasso = addebitoSpeseIncasso;
    }

    /**
     * @param agente
     *            The agente to set.
     */
    public void setAgente(AgenteLite agente) {
        this.agente = agente;
    }

    /**
     * @param annoMovimento
     *            the annoMovimento to set
     */
    public void setAnnoMovimento(Integer annoMovimento) {
        this.annoMovimento = annoMovimento;
    }

    /**
     * @param areaOrdineNote
     *            the areaOrdineNote to set
     */
    public void setAreaOrdineNote(AreaOrdineNote areaOrdineNote) {
        this.areaOrdineNote = areaOrdineNote;
    }

    /**
     * @param causaleTrasporto
     *            the causaleTrasporto to set
     */
    public void setCausaleTrasporto(String causaleTrasporto) {
        this.causaleTrasporto = causaleTrasporto;
    }

    /**
     * @param codiceIvaAlternativo
     *            the codiceIvaAlternativo to set
     */
    public void setCodiceIvaAlternativo(CodiceIva codiceIvaAlternativo) {
        this.codiceIvaAlternativo = codiceIvaAlternativo;
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
     * @param dataInizioTrasporto
     *            The dataInizioTrasporto to set.
     */
    public void setDataInizioTrasporto(Date dataInizioTrasporto) {
        this.dataInizioTrasporto = dataInizioTrasporto;
    }

    /**
     * @param dataRegistrazione
     *            the dataRegistrazione to set
     */
    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * @param datiValidazioneRighe
     *            the datiValidazioneRighe to set
     */
    public void setDatiValidazioneRighe(DatiValidazione datiValidazioneRighe) {
        this.datiValidazioneRighe = datiValidazioneRighe;
    }

    /**
     * @param depositoOrigine
     *            the depositoOrigine to set
     */
    public void setDepositoOrigine(DepositoLite depositoOrigine) {
        this.depositoOrigine = depositoOrigine;
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
     * @param evaso
     *            the evaso to set
     */
    public void setEvaso(boolean evaso) {
        this.evaso = evaso;
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
     * @param noteImportazione
     *            The noteImportazione to set.
     */
    public void setNoteImportazione(String noteImportazione) {
        this.noteImportazione = noteImportazione;
    }

    /**
     * @param numeroColli
     *            the numeroColli to set
     */
    public void setNumeroColli(Integer numeroColli) {
        this.numeroColli = numeroColli;
    }

    /**
     * @param raggruppamentoBolle
     *            the raggruppamentoBolle to set
     */
    public void setRaggruppamentoBolle(boolean raggruppamentoBolle) {
        this.raggruppamentoBolle = raggruppamentoBolle;
    }

    /**
     * @param riferimentiOrdine
     *            the riferimentiOrdine to set
     */
    public void setRiferimentiOrdine(RiferimentiOrdine riferimentiOrdine) {
        this.riferimentiOrdine = riferimentiOrdine;
    }

    /**
     * @param righe
     *            the righe to set
     */
    public void setRighe(Set<RigaOrdine> righe) {
        this.righe = righe;
    }

    /**
     * @param sedeVettore
     *            the sedeVettore to set
     */
    public void setSedeVettore(SedeEntita sedeVettore) {
        this.sedeVettore = sedeVettore;
    }

    /**
     * @param statoAreaOrdine
     *            the statoAreaOrdine to set
     */
    public void setStatoAreaOrdine(StatoAreaOrdine statoAreaOrdine) {
        this.statoAreaOrdine = statoAreaOrdine;
    }

    /**
     * @param statoSpedizione
     *            the statoSpedizione to set
     */
    public void setStatoSpedizione(StatoSpedizione statoSpedizione) {
        this.statoSpedizione = statoSpedizione;
    }

    @Override
    public void setTipoAreaDocumento(ITipoAreaDocumento tipoAreaDocumento) {
        tipoAreaOrdine = (TipoAreaOrdine) tipoAreaDocumento;
    }

    /**
     * @param tipoAreaOrdine
     *            the tipoAreaOrdine to set
     */
    public void setTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine) {
        this.tipoAreaOrdine = tipoAreaOrdine;
    }

    /**
     * @param tipologiaCodiceIvaAlternativo
     *            the tipologiaCodiceIvaAlternativo to set
     */
    public void setTipologiaCodiceIvaAlternativo(ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo) {
        this.tipologiaCodiceIvaAlternativo = tipologiaCodiceIvaAlternativo;
    }

    /**
     * @param tipoPorto
     *            the tipoPorto to set
     */
    public void setTipoPorto(String tipoPorto) {
        this.tipoPorto = tipoPorto;
    }

    /**
     * @param totaliArea
     *            the totaliArea to set
     */
    public void setTotaliArea(TotaliArea totaliArea) {
        this.totaliArea = totaliArea;
    }

    /**
     * @param trasportoCura
     *            the trasportoCura to set
     */
    public void setTrasportoCura(String trasportoCura) {
        this.trasportoCura = trasportoCura;
    }

    /**
     * @param vettore
     *            the vettore to set
     */
    public void setVettore(VettoreLite vettore) {
        this.vettore = vettore;
    }

    @Override
    public String toString() {
        StringBuffer retValue = new StringBuffer();
        retValue.append("AreaOrdine[ ").append(super.toString()).append(" documento = ").append(this.documento)
                .append(" dataRegistrazione = ").append(this.dataRegistrazione).append(" depositoOrigine = ")
                .append(this.depositoOrigine).append(" ]");
        return retValue.toString();
    }
}