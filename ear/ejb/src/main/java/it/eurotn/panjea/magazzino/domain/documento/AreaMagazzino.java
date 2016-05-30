package it.eurotn.panjea.magazzino.domain.documento;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import it.eurotn.entity.EntityBase;
import it.eurotn.entity.annotation.EntityConverter;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura;
import it.eurotn.panjea.anagrafica.documenti.domain.DatiValidazione;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.IStatoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.Vettore;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.audit.envers.AuditableProperties;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;
import it.eurotn.panjea.magazzino.domain.TotaliArea;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.OperazioneAreaContabileNonTrovata;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.spedizioni.domain.DatiSpedizioniDocumento;
import it.eurotn.panjea.spedizioni.domain.Segnacollo;
import it.eurotn.panjea.spedizioni.util.ParametriCreazioneEtichette;

/**
 * Definisce gli attributi per l'area magazzino.
 *
 * @author adriano
 * @version 1.0, 27/ago/2008
 */
@Entity
@Audited
@Table(name = "maga_area_magazzino")
@NamedQueries({
        @NamedQuery(name = "AreaMagazzino.caricaInventarioUtile", query = "select am from AreaMagazzino am where am.tipoOperazione = 0 and am.documento.codiceAzienda = :paramCodiceAzienda and am.dataRegistrazione <= :paramData and am.depositoOrigine.id = :paramDeposito order by am.dataRegistrazione desc"),
        @NamedQuery(name = "AreaMagazzino.caricaInventari", query = "select am from AreaMagazzino am where am.tipoOperazione = 0 and am.documento.codiceAzienda = :paramCodiceAzienda and am.dataRegistrazione >= :paramDataInizio and am.dataRegistrazione <= :paramDataFine and am.depositoOrigine.id = :paramDeposito order by am.dataRegistrazione asc"),
        @NamedQuery(name = "AreaMagazzino.caricaInFatturazione", query = "select am from AreaMagazzino am where am.statoAreaMagazzino = 3 and am.documento.codiceAzienda = :paramCodiceAzienda"),
        @NamedQuery(name = "AreaMagazzino.caricaBYDatiGenerazioneFatturazione", query = "select new it.eurotn.panjea.magazzino.domain.DatiGenerazione(am.datiGenerazione.utente,am.datiGenerazione.dataGenerazione,am.datiGenerazione.dataCreazione,am.datiGenerazione.note,am.datiGenerazione.esportato,datiGenerazione.tipoGenerazione) from AreaMagazzinoLite am where am.datiGenerazione.tipoGenerazione=0 and am.datiGenerazione.dataCreazione is not null and year(am.datiGenerazione.dataGenerazione) = :paramAnnoFatturazione and am.documento.codiceAzienda = :paramCodiceAzienda group by am.datiGenerazione.dataGenerazione, am.datiGenerazione.dataCreazione order by am.datiGenerazione.dataGenerazione, am.datiGenerazione.dataCreazione"),
        @NamedQuery(name = "AreaMagazzino.caricaBYDatiGenerazioneFatturazioneTemporanea", query = "select new it.eurotn.panjea.magazzino.domain.DatiGenerazione(am.datiGenerazione.utente,am.datiGenerazione.dataGenerazione,am.datiGenerazione.dataCreazione,am.datiGenerazione.note,am.datiGenerazione.esportato,datiGenerazione.tipoGenerazione) from AreaMagazzinoLite am where am.statoAreaMagazzino = 3 and am.documento.codiceAzienda = :paramCodiceAzienda group by am.datiGenerazione.utente,am.datiGenerazione.dataGenerazione, am.datiGenerazione.dataCreazione order by am.datiGenerazione.dataGenerazione, am.datiGenerazione.dataCreazione"),
        @NamedQuery(name = "AreaMagazzino.impostaComeEsportato", query = "update AreaMagazzino am set am.datiGenerazione.esportato=true where am.datiGenerazione.dataGenerazione=:paramDataGenerazione and am.datiGenerazione.dataCreazione=:paramDataCreazione"),
        @NamedQuery(name = "AreaMagazzino.caricaBYFatturazione", query = "select am from AreaMagazzinoLite am where am.documento.codiceAzienda = :paramCodiceAzienda and am.datiGenerazione.dataCreazione = :paramDataCreazione order by am.documento.codice.codiceOrder "),
        @NamedQuery(name = "AreaMagazzino.caricaDataFatturazioneMovimenti", query = "select distinct am.datiGenerazione.dataGenerazione from AreaMagazzino am where am.statoAreaMagazzino = 3 and am.documento.codiceAzienda = :paramCodiceAzienda and am.datiGenerazione.utente = :paramUtente "),
        @NamedQuery(name = "AreaMagazzino.ricercaByDocumento", query = "select a from AreaMagazzino a inner join a.documento d where d.id = :paramIdDocumento"),
        @NamedQuery(name = "AreaMagazzino.checkByDocumento", query = "select am.id from AreaMagazzino am where am.documento.id  = :paramIdDocumento"),
        @NamedQuery(name = "AreaMagazzino.caricaUltimoInventario", query = "select am from AreaMagazzinoLite am where am.tipoAreaMagazzino.tipoMovimento=4 and am.depositoOrigine.id = :paramIdDeposito order by am.documento.dataDocumento desc"),
        @NamedQuery(name = "AreaMagazzino.countAree", query = "select count(am.id) from AreaMagazzino am where am.documento.codiceAzienda = :paramCodiceAzienda") })
@AuditableProperties(properties = { "documento" })
@EntityConverter(properties = "documento")
public class AreaMagazzino extends EntityBase implements IAreaDocumento {

    /**
     *
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
    public enum StatoAreaMagazzino implements IStatoDocumento {
        CONFERMATO(false), PROVVISORIO(true), FORZATO(false), INFATTURAZIONE(false);

        private boolean provvisorio;

        /**
         * Costruttore.
         *
         * @param provvisorio
         *            indica se è uno stato provvisorio
         */
        private StatoAreaMagazzino(final boolean provvisorio) {
            this.provvisorio = provvisorio;
        }

        @Override
        public boolean isProvvisorio() {
            return provvisorio;
        }
    }

    public static final String FORMULA_VAR_N_COPIE_TIPO_DOCUMENTO = "$Copie tipo documento$";
    public static final String FORMULA_VAR_N_COPIE_VETTORE = "$Copie vettore$";
    public static final String FORMULA_VAR_COLLI = "$Colli$";

    private static final long serialVersionUID = -13810142713659461L;

    /**
     * Salva il tipoOperazione del tipoAreaMagazzino. Utilizzato per indicare se il movimento è un inventario.
     */
    private Integer tipoOperazione;

    @ManyToOne
    private TipoAreaMagazzino tipoAreaMagazzino;

    @Formula("(select if(sum(if(rigaMag.qta-coalesce((select sum(rm.qta) from maga_righe_magazzino rm where rm.rigaMagazzinoCollegata_id = rigaMag.id),0)>0,1,0))> 0,0,1) from maga_righe_magazzino rigaMag where rigaMag.areaMagazzino_id = id)")
    @NotAudited
    private boolean chiuso;

    @ManyToOne
    private Listino listinoAlternativo;

    @ManyToOne
    private Listino listino;

    @OneToOne
    private Documento documento;

    @Formula("(select count(rm.id) from maga_righe_magazzino rm  where rm.areaMagazzino_id=id)")
    @NotAudited
    private int numeroRighe;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "valid", column = @Column(name = "validRigheMagazzino") ),
            @AttributeOverride(name = "validData", column = @Column(name = "validDataRigheMagazzino") ),
            @AttributeOverride(name = "validUtente", column = @Column(name = "validUtenteRigheMagazzino") ) })
    private DatiValidazione datiValidazioneRighe;

    @Index(name = "dataRegistrazione")
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataRegistrazione;

    /**
     * Indica che il salvataggio di {@link AreaMagazzino} ha creato il documento perche' l'attributo
     * {@link OperazioneAreaContabileNonTrovata} e' valorizzato con NONAVVISARE.
     */
    private boolean documentoCreatoDaAreaMagazzino;

    /**
     * Indica che il salvataggio di {@link AreaMagazzino} ha forzato la creazione di documento perchè l'attributo
     * {@link OperazioneAreaContabileNonTrovata} e' valorizzato con AVVISARE.
     */
    private boolean documentoForzatoDaAreaMagazzino;

    /**
     * Proprieta' copiata da tipoAreaMagazzino che indica se l'area magazzino viene considerata tra le aree per
     * recuperare l'ultimo costo.
     */
    private boolean aggiornaCostoUltimo;

    @ManyToOne
    private DepositoLite depositoDestinazione;

    @ManyToOne
    private DepositoLite depositoOrigine;

    private StatoAreaMagazzino statoAreaMagazzino;

    @Column(length = 40)
    private String causaleTrasporto;

    @Column(length = 45)
    private String trasportoCura;

    @Column(length = 20)
    private String responsabileRitiroMerce;

    @Column(length = 30)
    private String tipoPorto;
    @ManyToOne
    @JoinColumn(name = "vettore_id")
    private VettoreLite vettore;
    @ManyToOne
    @JoinColumn(name = "mezzoTrasporto_id")
    private MezzoTrasporto mezzoTrasporto;

    @ManyToOne
    private SedeEntita sedeVettore;

    private boolean addebitoSpeseIncasso;

    private boolean raggruppamentoBolle;

    @Column(length = 40)
    private String aspettoEsteriore;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataInizioTrasporto;

    private Integer numeroColli;

    @Column(precision = 8, scale = 2)
    private BigDecimal pesoNetto;

    @Column(precision = 8, scale = 2)
    private BigDecimal pesoLordo;

    @Column(precision = 8, scale = 3)
    private BigDecimal volume;

    @Index(name = "annoMovimento")
    private Integer annoMovimento;

    @Embedded
    private DatiGenerazione datiGenerazione;

    @Embedded
    private TotaliArea totaliArea;

    @Embedded
    private AreaMagazzinoNote areaMagazzinoNote;

    private boolean stampaPrezzi;

    @ManyToOne
    private CodiceIva codiceIvaAlternativo;

    private ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo;

    private Integer idZonaGeografica;

    /**
     * Flag per indicare se è possibile concludere l'inserimento del documento di magazzino e quindi salvarlo; è stato
     * aggiunto questo attributo transiente per scegliere a runtime se ci sono tutte le condizioni necessarie al
     * completamento dell'operazione.<br>
     * Ad esempio sulla sede magazzino abbiamo un flag che indica se la sede è stata bloccata e quindi l'inserimento di
     * un documento risulta impossibile; al momento della scelta della sede viene valorizzato questo attributo.
     */
    @Transient
    private boolean inserimentoBloccato;

    @OneToMany(mappedBy = "areaMagazzino", fetch = FetchType.LAZY)
    private List<RigaMagazzino> righeMagazzino;

    private String uUIDContabilizzazione;

    @Embedded
    private DatiSpedizioniDocumento datiSpedizioniDocumento;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @OrderBy("numeroSegnacollo")
    private Set<Segnacollo> segnacolli;

    private boolean contoTerzi;

    private Integer pallet;

    private StatoSpedizione statoSpedizione;

    private boolean bloccata;

    {
        this.statoAreaMagazzino = StatoAreaMagazzino.PROVVISORIO;
        this.totaliArea = new TotaliArea();
        this.datiSpedizioniDocumento = new DatiSpedizioniDocumento();
        this.datiGenerazione = new DatiGenerazione();
        this.datiValidazioneRighe = new DatiValidazione();

        addebitoSpeseIncasso = false;
        aggiornaCostoUltimo = false;
        inserimentoBloccato = false;
        tipoOperazione = -1; // Inizialmente l'area Documento non è riferita a nessun tipoDocumentoBase
        this.annoMovimento = -1;
        dataInizioTrasporto = null;
        stampaPrezzi = true;
        this.tipologiaCodiceIvaAlternativo = ETipologiaCodiceIvaAlternativo.NESSUNO;
        this.documento = new Documento();
        this.areaMagazzinoNote = new AreaMagazzinoNote();

        this.statoSpedizione = StatoSpedizione.DA_VERIFICARE;

        this.bloccata = false;
    }

    /**
     * Costruttore.
     */
    public AreaMagazzino() {
    }

    /**
     * Applica i parametri di generazione delle etichette all'area magazzino.
     *
     * @param parametri
     *            parametri da applicare
     */
    public void applyParametriCreazioneEtichette(ParametriCreazioneEtichette parametri) {
        this.dataInizioTrasporto = parametri.getDataSpedizione();
        this.getDatiSpedizioniDocumento().setConsegna(parametri.getConsegna());
        this.numeroColli = parametri.getNumeroColli();
        this.pesoLordo = parametri.getPeso();
        this.volume = parametri.getVolume();
        this.getDatiSpedizioniDocumento().setNoteSpedizione(parametri.getNote());
        this.getDatiSpedizioniDocumento().setImportoContrassegnoSpedizione(parametri.getImportoContrassegno());
        this.getDatiSpedizioniDocumento().setModalitaIncasso(parametri.getModalitaIncasso());
        this.getDatiSpedizioniDocumento().setDataConsegna(parametri.getDataConsegna());
    }

    /**
     * Assegna un'eventuale iva laternativa.
     *
     * @param sedeMagazzino
     *            sedeMagazzino dalla quale recuperare i dati di iva alternativa
     */
    public void assegnaCodiceIvaAlternativo(SedeMagazzino sedeMagazzino) {
        tipologiaCodiceIvaAlternativo = sedeMagazzino.getTipologiaCodiceIvaAlternativo();
        if (sedeMagazzino.getCodiceIvaAlternativo() != null) {
            codiceIvaAlternativo = sedeMagazzino.getCodiceIvaAlternativo();
        }
        // se la tipologia del codice iva alternativo è ESENZIONE_DICHIARAZIONE_INTENTO controllo
        // che la
        // data del documento rientri nella scadenza altrimenti imposto come tipologia NESSUNA e
        // rimuovo il
        // codice iv alternativo
        if (sedeMagazzino
                .getTipologiaCodiceIvaAlternativo() == ETipologiaCodiceIvaAlternativo.ESENZIONE_DICHIARAZIONE_INTENTO) {
            Date dataScadenza = sedeMagazzino.getDichiarazioneIntento().getDataScadenza();
            Date dataDocumento = getDocumento().getDataDocumento();

            if (dataScadenza == null || dataDocumento.after(dataScadenza)) {
                tipologiaCodiceIvaAlternativo = ETipologiaCodiceIvaAlternativo.NESSUNO;
                codiceIvaAlternativo = null;
            }
        }
    }

    @Override
    public Map<String, Object> fillVariables() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(FORMULA_VAR_N_COPIE_TIPO_DOCUMENTO, getTipoAreaMagazzino().getNumeroCopiePerStampa());

        Integer numCopiePerStampaVettore = 0;
        if (sedeVettore != null) {
            numCopiePerStampaVettore = ObjectUtils
                    .defaultIfNull(((Vettore) sedeVettore.getEntita()).getNumeroCopiePerStampa(), 0);
        }
        variables.put(FORMULA_VAR_N_COPIE_VETTORE, numCopiePerStampaVettore);
        variables.put(FORMULA_VAR_COLLI, ObjectUtils.defaultIfNull(getNumeroColli(), 0));
        return variables;
    }

    /**
     * @return the altreSpese
     */
    public Importo getAltreSpese() {
        return totaliArea.getAltreSpese();
    }

    /**
     * @return the annoMovimento
     */
    public Integer getAnnoMovimento() {
        return annoMovimento;
    }

    /**
     * @return AreaMagazzinoNote.
     */
    public AreaMagazzinoNote getAreaMagazzinoNote() {
        if (areaMagazzinoNote == null) {
            this.areaMagazzinoNote = new AreaMagazzinoNote();
        }
        return areaMagazzinoNote;
    }

    /**
     * @return String AspettoEsterior.
     */
    public String getAspettoEsteriore() {
        return aspettoEsteriore;
    }

    /**
     * @return String causaleTrasporto.
     */
    public String getCausaleTrasporto() {
        return causaleTrasporto;
    }

    /**
     * @return the codiceIvaAlternativo.
     */
    public CodiceIva getCodiceIvaAlternativo() {
        return codiceIvaAlternativo;
    }

    /**
     * @return Date dataInizioTrasporto.
     */
    public Date getDataInizioTrasporto() {
        return dataInizioTrasporto;
    }

    /**
     * @return Returns the dataRegistrazione.
     */
    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    /**
     * @return datiGenerazione.
     */
    public DatiGenerazione getDatiGenerazione() {
        if (datiGenerazione == null) {
            datiGenerazione = new DatiGenerazione();
        }
        return datiGenerazione;
    }

    /**
     * @return the datiSpedizioniDocumento
     */
    public DatiSpedizioniDocumento getDatiSpedizioniDocumento() {
        if (datiSpedizioniDocumento == null) {
            datiSpedizioniDocumento = new DatiSpedizioniDocumento();
        }
        return datiSpedizioniDocumento;
    }

    /**
     * @return the datiValidazioneRighe
     */
    public DatiValidazione getDatiValidazioneRighe() {
        if (datiValidazioneRighe == null) {
            this.datiValidazioneRighe = new DatiValidazione();
        }
        return datiValidazioneRighe;
    }

    /**
     * @return Returns the depositoDestinazione.
     */
    public DepositoLite getDepositoDestinazione() {
        return depositoDestinazione;
    }

    /**
     * @return Returns the depositoOrigine.
     */
    public DepositoLite getDepositoOrigine() {
        return depositoOrigine;
    }

    /**
     * @return Returns the documento.
     */
    @Override
    public Documento getDocumento() {
        return documento;
    }

    /**
     * @return the idZonaGeografica
     */
    public Integer getIdZonaGeografica() {
        return idZonaGeografica;
    }

    /**
     * @return listino.
     */
    public Listino getListino() {
        return listino;
    }

    /**
     * @return listinoAlternativo.
     */
    public Listino getListinoAlternativo() {
        return listinoAlternativo;
    }

    /**
     * @return the mezzoTrasporto
     */
    public MezzoTrasporto getMezzoTrasporto() {
        return mezzoTrasporto;
    }

    /**
     * @return Integer numeroColli.
     */
    public Integer getNumeroColli() {
        return numeroColli;
    }

    /**
     * @return Returns the numeroRighe.
     */
    public int getNumeroRighe() {
        return numeroRighe;
    }

    /**
     *
     * @return pallet
     */
    public Integer getPallet() {
        return pallet;
    }

    /**
     * @return BigDecimal pesoLordo.
     */
    public BigDecimal getPesoLordo() {
        return pesoLordo;
    }

    /**
     * @return BigDecimal pesoNetto.
     */
    public BigDecimal getPesoNetto() {
        return pesoNetto;
    }

    /**
     * @return Returns the responsabileRitiroMerce.
     */
    public String getResponsabileRitiroMerce() {
        return responsabileRitiroMerce;
    }

    /**
     * @return the righeMagazzino
     */
    public List<RigaMagazzino> getRigheMagazzino() {
        return righeMagazzino;
    }

    /**
     * @return sedeVettore.
     */
    public SedeEntita getSedeVettore() {
        return sedeVettore;
    }

    /**
     * @return the segnacolli
     */
    public Set<Segnacollo> getSegnacolli() {
        return segnacolli;
    }

    /**
     * @return Importo Spese di trasporto.
     */
    public Importo getSpeseTrasporto() {
        return totaliArea.getSpeseTrasporto();
    }

    @Override
    public IStatoDocumento getStato() {
        return statoAreaMagazzino;
    }

    /**
     * @return Returns the statoAreaMagazzino.
     */
    public StatoAreaMagazzino getStatoAreaMagazzino() {
        return statoAreaMagazzino;
    }

    /**
     * @return the statoSpedizione
     */
    public StatoSpedizione getStatoSpedizione() {
        return statoSpedizione;
    }

    @Override
    public ITipoAreaDocumento getTipoAreaDocumento() {
        return getTipoAreaMagazzino();
    }

    /**
     * @return Returns the tipoAreaMagazzino.
     */
    public TipoAreaMagazzino getTipoAreaMagazzino() {
        return tipoAreaMagazzino;
    }

    /**
     *
     * @return {@link ETipoEconomico}.
     */
    public ETipoEconomico getTipoEconomico() {
        switch (tipoAreaMagazzino.getTipoDocumento().getTipoEntita()) {
        case CLIENTE:
            return ETipoEconomico.RICAVO;

        case FORNITORE:
            return ETipoEconomico.COSTO;
        default:
            throw new UnsupportedOperationException(
                    "Richiesto un tipo economico per un documento associato ad un tipEntità non corretta.id Documento "
                            + getId());
        }
    }

    /**
     * @return the tipologiaCodiceIvaAlternativo
     */
    public ETipologiaCodiceIvaAlternativo getTipologiaCodiceIvaAlternativo() {
        return tipologiaCodiceIvaAlternativo;
    }

    /**
     * @return Integer tipoOperazione
     */
    public Integer getTipoOperazione() {
        return tipoOperazione;
    }

    /**
     * @return String tipoPorto.
     */
    public String getTipoPorto() {
        return tipoPorto;
    }

    /**
     * @return Importo totaleMerce·
     */
    public Importo getTotaleMerce() {
        return totaliArea.getTotaleMerce();
    }

    /**
     * @return the totaliArea
     */
    public TotaliArea getTotaliArea() {
        if (totaliArea == null) {
            totaliArea = new TotaliArea();
        }
        return totaliArea;
    }

    /**
     * @return {@link TrasportoCura}.
     */
    public String getTrasportoCura() {
        return trasportoCura;
    }

    /**
     * @return the uUIDContabilizzazione
     */
    public String getUUIDContabilizzazione() {
        return uUIDContabilizzazione;
    }

    /**
     * @return {@link VettoreLite}.
     */
    public VettoreLite getVettore() {
        return vettore;
    }

    /**
     * @return BigDecimal volume.
     */
    public BigDecimal getVolume() {
        return volume;
    }

    /**
     * @return boolean addebitoSpeseIncasso.
     */
    public boolean isAddebitoSpeseIncasso() {
        return addebitoSpeseIncasso;
    }

    /**
     * @return the aggiornaCostoUltimo
     */
    public boolean isAggiornaCostoUltimo() {
        return aggiornaCostoUltimo;
    }

    /**
     * @return the bloccata
     */
    public final boolean isBloccata() {
        return bloccata;
    }

    /**
     * True se il documento è considerato un documento di carico. <br/>
     * Un documento di carico ha la possibilità di modificare iva/totale documento e altri dati calcolati
     * automaticamente
     *
     * @return boolean true nel caso sia un carico.
     */
    public boolean isCarico() {
        return getTipoAreaMagazzino().getTipoMovimento() == TipoMovimento.CARICO;
    }

    /**
     * @return Returns the chiuso.
     */
    public boolean isChiuso() {
        return chiuso;
    }

    /**
     * @return Returns the contoTerzi.
     */
    public boolean isContoTerzi() {
        return contoTerzi;
    }

    /**
     * @return dichiarazione intento
     */
    public boolean isDichiarazioneIntento() {
        return getTipoAreaMagazzino() != null && getTipoAreaMagazzino().isValoriFatturato()
                && getTipologiaCodiceIvaAlternativo() == ETipologiaCodiceIvaAlternativo.ESENZIONE_DICHIARAZIONE_INTENTO;
    }

    /**
     * @return Returns the documentoCreatoDaAreaMagazzino.
     */
    public boolean isDocumentoCreatoDaAreaMagazzino() {
        return documentoCreatoDaAreaMagazzino;
    }

    /**
     * @return Returns the documentoForzatoDaAreaMagazzino.
     */
    public boolean isDocumentoForzatoDaAreaMagazzino() {
        return documentoForzatoDaAreaMagazzino;
    }

    /**
     * @return <code>true</code> se si tratta di una fattura a PA.
     */
    public boolean isFatturaPA() {
        return getDocumento() != null
                && getDocumento().getTipoDocumento().getClasseTipoDocumentoInstance() instanceof ClasseFattura
                && getDocumento().getEntita() != null && getDocumento().getEntita().isFatturazionePA()
                && (getStatoAreaMagazzino() == StatoAreaMagazzino.CONFERMATO
                        || getStatoAreaMagazzino() == StatoAreaMagazzino.FORZATO);
    }

    /**
     * @return boolean true nel caso il inserimento sia blocato.
     */
    public boolean isInserimentoBloccato() {
        return inserimentoBloccato;
    }

    /**
     * @return Returns the modificabile.
     */
    public boolean isModificabile() {
        return numeroRighe == 0 || !chiuso;
    }

    /**
     * @return boolean true se is ragruppamentoBolle.
     */
    public boolean isRaggruppamentoBolle() {
        return raggruppamentoBolle;
    }

    /**
     * @return the stampaPrezzi
     */
    public boolean isStampaPrezzi() {
        return stampaPrezzi;
    }

    /**
     * @param addebitoSpeseIncasso
     *            the addebitoSpeseIncasso to set
     */
    public void setAddebitoSpeseIncasso(boolean addebitoSpeseIncasso) {
        this.addebitoSpeseIncasso = addebitoSpeseIncasso;
    }

    /**
     * @param aggiornaCostoUltimo
     *            the aggiornaCostoUltimo to set
     */
    public void setAggiornaCostoUltimo(boolean aggiornaCostoUltimo) {
        this.aggiornaCostoUltimo = aggiornaCostoUltimo;
    }

    /**
     * @param altreSpese
     *            the altreSpese to set
     */
    public void setAltreSpese(Importo altreSpese) {
        this.totaliArea.setAltreSpese(altreSpese);
    }

    /**
     * @param annoMovimento
     *            the annoMovimento to set
     */
    public void setAnnoMovimento(Integer annoMovimento) {
        this.annoMovimento = annoMovimento;
    }

    /**
     * @param areaMagazzinoNote
     *            the areaMagazzinoNote to set
     */
    public void setAreaMagazzinoNote(AreaMagazzinoNote areaMagazzinoNote) {
        this.areaMagazzinoNote = areaMagazzinoNote;
    }

    /**
     * @param aspettoEsteriore
     *            the aspettoEsteriore to set
     */
    public void setAspettoEsteriore(String aspettoEsteriore) {
        this.aspettoEsteriore = aspettoEsteriore;
    }

    /**
     * @param bloccata
     *            the bloccata to set
     */
    public final void setBloccata(boolean bloccata) {
        this.bloccata = bloccata;
    }

    /**
     * @param causaleTrasporto
     *            the causaleTrasporto to set
     */
    public void setCausaleTrasporto(String causaleTrasporto) {
        this.causaleTrasporto = causaleTrasporto;
    }

    /**
     * @param chiuso
     *            The chiuso to set.
     */
    public void setChiuso(boolean chiuso) {
        this.chiuso = chiuso;
    }

    /**
     * @param codiceIvaAlternativo
     *            the codiceIvaAlternativo to set
     */
    public void setCodiceIvaAlternativo(CodiceIva codiceIvaAlternativo) {
        this.codiceIvaAlternativo = codiceIvaAlternativo;
    }

    /**
     *
     * @param contoTerzi
     *            contoTerziToSet
     */
    public void setContoTerzi(boolean contoTerzi) {
        this.contoTerzi = contoTerzi;

    }

    /**
     * @param dataInizioTrasporto
     *            the dataInizioTrasporto to set
     */
    public void setDataInizioTrasporto(Date dataInizioTrasporto) {
        this.dataInizioTrasporto = dataInizioTrasporto;
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
     * @param datiSpedizioniDocumento
     *            the datiSpedizioniDocumento to set
     */
    public void setDatiSpedizioniDocumento(DatiSpedizioniDocumento datiSpedizioniDocumento) {
        this.datiSpedizioniDocumento = datiSpedizioniDocumento;
    }

    /**
     * @param datiValidazioneRighe
     *            the datiValidazioneRighe to set
     */
    public void setDatiValidazioneRighe(DatiValidazione datiValidazioneRighe) {
        this.datiValidazioneRighe = datiValidazioneRighe;
    }

    /**
     * @param depositoDestinazione
     *            The depositoDestinazione to set.
     */
    public void setDepositoDestinazione(DepositoLite depositoDestinazione) {
        this.depositoDestinazione = depositoDestinazione;
    }

    /**
     * @param depositoOrigine
     *            The depositoOrigine to set.
     */
    public void setDepositoOrigine(DepositoLite depositoOrigine) {
        this.depositoOrigine = depositoOrigine;
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
     * @param documentoCreatoDaAreaMagazzino
     *            The documentoCreatoDaAreaMagazzino to set.
     */
    public void setDocumentoCreatoDaAreaMagazzino(boolean documentoCreatoDaAreaMagazzino) {
        this.documentoCreatoDaAreaMagazzino = documentoCreatoDaAreaMagazzino;
    }

    /**
     * @param documentoForzatoDaAreaMagazzino
     *            The documentoForzatoDaAreaMagazzino to set.
     */
    public void setDocumentoForzatoDaAreaMagazzino(boolean documentoForzatoDaAreaMagazzino) {
        this.documentoForzatoDaAreaMagazzino = documentoForzatoDaAreaMagazzino;
    }

    /**
     * @param idZonaGeografica
     *            the idZonaGeografica to set
     */
    public void setIdZonaGeografica(Integer idZonaGeografica) {
        this.idZonaGeografica = idZonaGeografica;
    }

    /**
     * @param inserimentoBloccato
     *            the inserimentoBloccato to set
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
     * @param mezzoTrasporto
     *            the mezzoTrasporto to set
     */
    public void setMezzoTrasporto(MezzoTrasporto mezzoTrasporto) {
        this.mezzoTrasporto = mezzoTrasporto;
    }

    /**
     * @param numeroColli
     *            the numeroColli to set
     */
    public void setNumeroColli(Integer numeroColli) {
        this.numeroColli = numeroColli;
    }

    /**
     *
     * @param pallet
     *            pallet
     */
    public void setPallet(Integer pallet) {
        this.pallet = pallet;
    }

    /**
     * @param pesoLordo
     *            the pesoLordo to set
     */
    public void setPesoLordo(BigDecimal pesoLordo) {
        this.pesoLordo = pesoLordo;
    }

    /**
     * @param pesoNetto
     *            the pesoNetto to set
     */
    public void setPesoNetto(BigDecimal pesoNetto) {
        this.pesoNetto = pesoNetto;
    }

    /**
     * @param raggruppamentoBolle
     *            the raggruppamentoBolle to set
     */
    public void setRaggruppamentoBolle(boolean raggruppamentoBolle) {
        this.raggruppamentoBolle = raggruppamentoBolle;
    }

    /**
     * @param responsabileRitiroMerce
     *            The responsabileRitiroMerce to set.
     */
    public void setResponsabileRitiroMerce(String responsabileRitiroMerce) {
        this.responsabileRitiroMerce = responsabileRitiroMerce;
    }

    /**
     * @param righeMagazzino
     *            the righeMagazzino to set
     */
    public void setRigheMagazzino(List<RigaMagazzino> righeMagazzino) {
        this.righeMagazzino = righeMagazzino;
    }

    /**
     * @param sedeVettore
     *            the sedeVettore to set
     */
    public void setSedeVettore(SedeEntita sedeVettore) {
        this.sedeVettore = sedeVettore;
    }

    /**
     * @param segnacolli
     *            the segnacolli to set
     */
    public void setSegnacolli(Set<Segnacollo> segnacolli) {
        this.segnacolli = segnacolli;
    }

    /**
     * @param speseTrasporto
     *            the speseTrasporto to set
     */
    public void setSpeseTrasporto(Importo speseTrasporto) {
        this.totaliArea.setSpeseTrasporto(speseTrasporto);
    }

    /**
     * @param stampaPrezzi
     *            the stampaPrezzi to set
     */
    public void setStampaPrezzi(boolean stampaPrezzi) {
        this.stampaPrezzi = stampaPrezzi;
    }

    /**
     * @param statoAreaMagazzino
     *            The statoAreaMagazzino to set.
     */
    public void setStatoAreaMagazzino(StatoAreaMagazzino statoAreaMagazzino) {
        this.statoAreaMagazzino = statoAreaMagazzino;
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
        tipoAreaMagazzino = (TipoAreaMagazzino) tipoAreaDocumento;
    }

    /**
     * @param tipoAreaMagazzino
     *            The tipoAreaMagazzino to set.
     */
    public void setTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        this.tipoAreaMagazzino = tipoAreaMagazzino;
    }

    /**
     * @param tipologiaCodiceIvaAlternativo
     *            the tipologiaCodiceIvaAlternativo to set
     */
    public void setTipologiaCodiceIvaAlternativo(ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo) {
        this.tipologiaCodiceIvaAlternativo = tipologiaCodiceIvaAlternativo;
    }

    /**
     * @param tipoOperazione
     *            the tipoOperazione to set
     */
    public void setTipoOperazione(Integer tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }

    /**
     * @param tipoPorto
     *            the tipoPorto to set
     */
    public void setTipoPorto(String tipoPorto) {
        this.tipoPorto = tipoPorto;
    }

    /**
     * @param totaleMerce
     *            the totaleMerce to set
     */
    public void setTotaleMerce(Importo totaleMerce) {
        this.totaliArea.setTotaleMerce(totaleMerce);
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
     * @param uUIDContabilizzazioneParam
     *            the uUIDContabilizzazione to set
     */
    public void setUUIDContabilizzazione(String uUIDContabilizzazioneParam) {
        this.uUIDContabilizzazione = uUIDContabilizzazioneParam;
    }

    /**
     * @param vettore
     *            the vettore to set
     */
    public void setVettore(VettoreLite vettore) {
        this.vettore = vettore;
    }

    /**
     * @param volume
     *            the volume to set
     */
    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     *
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {
        StringBuilder retValue = new StringBuilder();
        retValue.append("AreaMagazzino[ ").append(super.toString()).append(" documento = ").append(this.documento)
                .append(" dataRegistrazione = ").append(this.dataRegistrazione).append(" depositoDestinazione = ")
                .append(this.depositoDestinazione).append(" depositoOrigine = ").append(this.depositoOrigine)
                .append(" ]");
        return retValue.toString();
    }
}
