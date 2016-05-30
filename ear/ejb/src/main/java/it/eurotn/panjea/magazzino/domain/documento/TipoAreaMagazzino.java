package it.eurotn.panjea.magazzino.domain.documento;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.ITipoAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.manager.documento.exporter.CP110WExporter;
import it.eurotn.panjea.magazzino.manager.documento.exporter.CassaOlivettiExporter;
import it.eurotn.panjea.magazzino.manager.documento.exporter.SwedaLaborExporter;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Definisce le proprietà per l'area magazzino.
 *
 * @author giangi
 */
@Entity
@Audited
@Table(name = "maga_tipi_area_magazzino")
@NamedQueries({
        @NamedQuery(name = "TipoAreaMagazzino.caricaByTipoDocumento", query = "select tam from TipoAreaMagazzino tam inner join tam.tipoDocumento td where td.id = :paramId "),
        @NamedQuery(name = "TipoAreaMagazzino.caricaByAnagraficaFatturazione", query = "select ta.tipoDocumento from TipoAreaMagazzino ta where ta.tipoDocumento.codiceAzienda = :paramCodiceAzienda and ta.tipoMovimento = :paramTipoMovimento "),
        @NamedQuery(name = "TipoAreaMagazzino.caricaTipiDocumentoDestinazioneFatturazione", query = "select distinct ta.tipoDocumentoPerFatturazione from TipoAreaMagazzino ta where ta.tipoDocumentoPerFatturazione.classeTipoDocumento = 'it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura' and ta.tipoDocumentoPerFatturazione.codiceAzienda = :paramCodiceAzienda  and ta.tipoDocumentoPerFatturazione.tipoEntita = 0 "),
        @NamedQuery(name = "TipoAreaMagazzino.caricaByTipoDocumentoFatturazione", query = "select ta.tipoDocumento  from TipoAreaMagazzino ta where ta.tipoDocumentoPerFatturazione.id = :paramIdTipoDoc "),
        @NamedQuery(name = "TipoAreaMagazzino.caricaTipiDocumentiMagazzino", query = " select ta.tipoDocumento from TipoAreaMagazzino ta where ta.tipoDocumento.codiceAzienda = :paramCodiceAzienda "),
        @NamedQuery(name = "TipoAreaMagazzino.caricaByGestioneConaiAttiva", query = " select ta from TipoAreaMagazzino ta where ta.tipoDocumento.codiceAzienda = :paramCodiceAzienda and ta.gestioneConai = true ") })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "tipoAreaMagazzino")
public class TipoAreaMagazzino extends EntityBase implements java.io.Serializable, Cloneable, ITipoAreaDocumento {

    /**
     * @author giangi
     * @version 1.0, 10/nov/2010
     */
    public enum ESezioneTipoMovimento {
        ACQUISTATOVENDUTO, GENERICO
    }

    /**
     * Opzione per decidere l'azione se non trovo l'area contabile. <br>
     * <LI>non avvisare (ed inserire un flag in area magazzino che indica che l'area magazzino è
     * stata inserita in questa modalità)</LI>
     * <LI>avvisare (indica che ho accettato di fare l'area magazzino senza area contabile)</LI>
     * <LI>blocca.( Non permette il salvataggio della testata)</LI>tipoDocumento
     *
     * @author giangi
     */
    public enum OperazioneAreaContabileNonTrovata {
        NONAVVISARE, AVVISA, BLOCCA
    }

    /**
     * Provenienza del prezzo per gli articoli inseriti nell'area magazzino.<br>
     * Su AreaMagazzino non è sovrescrivibile, viene decisa direttamente nel TipoAreaMagazzino.
     *
     * @author giangi
     */
    public enum ProvenienzaPrezzo {

        LISTINO, ULTIMO_COSTO
    }

    /**
     * Determina la strategia da utilizzare per l'esportazione del documento.<br/>
     * I manager degli esporter devono essere dei bean pubblicati nel jndi.
     *
     * @author fattazzo
     */
    public enum StrategiaEsportazioneFlusso {
        NESSUNO(""), CASSA_OLIVETTI(CassaOlivettiExporter.JNDI_NAME), CP110W(CP110WExporter.JNDI_NAME), ESWEDA(
                SwedaLaborExporter.JNDI_NAME), GULLIVER("Panjea.GulliverDocumentoCaricoExporter", true);

        private final boolean richiestaConferma;
        private final String jndiNameExporter;

        /**
         * Costruttore. La richiesta della conferma è settata a false.
         *
         * @param jndiNameExporter
         *            nome jndi dell'esporter.
         */
        StrategiaEsportazioneFlusso(final String jndiNameExporter) {
            this(jndiNameExporter, false);
        }

        /**
         * Costruttore.
         *
         * @param richiestaConferma
         *            indica se è richiesta una conferma per esportare il flusso.
         * @param jndiNameExporter
         *            nome jndi dell'esporter.
         */
        StrategiaEsportazioneFlusso(final String jndiNameExporter, final boolean richiestaConferma) {
            this.jndiNameExporter = jndiNameExporter;
            this.richiestaConferma = richiestaConferma;
        }

        /**
         * @return Returns the jndiNameExporter.
         */
        public String getJndiNameExporter() {
            return jndiNameExporter;
        }

        /**
         * @return Returns the richiestaConferma.
         */
        public boolean isRichiestaConferma() {
            return richiestaConferma;
        }
    }

    /**
     * Strategia per il calcolo del totale documento.
     * <ul>
     * <li>NORMALE</li>
     * <li>SCONTRINO</li>
     * </ul>
     *
     * @author Leonardo
     */
    public enum StrategiaTotalizzazioneDocumento {
        NORMALE, SCONTRINO
    }

    /**
     * Indica come il tipo documento movimenta la merce <br>
     * <b>NB.</b>Trasferimento può essere applicato solamente se entità del tipoDocumento=Azienda
     *
     * @author giangi
     */
    public enum TipoMovimento {
        NESSUNO(false), CARICO(false), SCARICO(false), TRASFERIMENTO(true), INVENTARIO(false), CARICO_VALORE(
                false), SCARICO_VALORE(false), CARICO_PRODUZIONE(true);

        private final boolean enableDepositoDestinazione;

        /**
         * Costruttore.
         *
         * @param enableDepositoDestinazione
         *            abilita o meno il deposito di destinazione
         */
        private TipoMovimento(final boolean enableDepositoDestinazione) {
            this.enableDepositoDestinazione = enableDepositoDestinazione;
        }

        /**
         * @return Returns the enableDepositoDestinazione.
         */
        public boolean isEnableDepositoDestinazione() {
            return enableDepositoDestinazione;
        }

    }

    public static final String REPORT_PATH = "Magazzino/Documenti";

    private static final long serialVersionUID = 6689138018986201966L;

    /**
     * Indica il tipo documento che verrà utilizzato per la fatturazione PA.
     */
    @ManyToOne
    private TipoDocumento tipoDocumentoPerFatturazionePA;

    /**
     * Indica il tipo documento che verrà utilizzato per la fatturazione.
     */
    @ManyToOne
    private TipoDocumento tipoDocumentoPerFatturazione;

    /**
     * Contiene la maschera necessaria per la generazione della descrizione della riga testata sul
     * documento di fatturazione.
     */
    @Column(length = 100)
    private String tipoDocumentoPerFatturazioneDescrizioneMaschera;

    @OneToOne
    private TipoDocumento tipoDocumento;

    private boolean riepilogoIva;

    private TipoMovimento tipoMovimento;

    private ESezioneTipoMovimento sezioneTipoMovimento;

    private boolean dataDocLikeDataReg;

    private ProvenienzaPrezzo provenienzaPrezzo;

    private OperazioneAreaContabileNonTrovata operazioneAreaContabileNonTrovata;

    private boolean depositoOrigineBloccato;

    private boolean depositoDestinazioneBloccato;

    private boolean qtaZeroPermessa;

    @ManyToOne
    private EntitaLite entitaPredefinita;

    @ManyToOne
    private DepositoLite depositoOrigine;

    @ManyToOne
    private DepositoLite depositoDestinazione;

    @ManyToOne
    private Listino listino;

    @Column(length = 100)
    private String descrizionePerStampa;

    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoAreaMagazzino", cascade = { CascadeType.ALL })
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Fetch(FetchMode.JOIN)
    @OrderBy(value = "ordinamento")
    private Set<DatoAccompagnatorioMagazzinoMetaData> datiAccompagnatoriMetaData;

    private StrategiaTotalizzazioneDocumento strategiaTotalizzazioneDocumento;

    /**
     * Indica se nel datawarehouse i valori di quantità e importo del documento dovranno essere
     * inseriti come valori di fatturato.
     */
    private boolean valoriFatturato;

    /**
     * Indica se il documento ha il mezzo di trasporto
     */
    private boolean richiestaMezzoTrasporto;

    /**
     * Indica se il documento prevede l'obbligatorieta' di inserimento di un mezzo di trasporto.
     */
    private boolean richiestaMezzoTrasportoObbligatorio;

    private boolean aggiornaCostoUltimo;

    /**
     * indica se nel tipoAreaMagazzino posso gestire gli agenti sulle righe.
     */
    private boolean gestioneAgenti;

    private StrategiaEsportazioneFlusso strategiaEsportazioneFlusso;

    private Integer numeroCopiePerStampa;

    /**
     * Indica se le note della riga dovranno essere riportate sul documento di destinazione.
     */
    private boolean noteSuDestinazione;

    /**
     * Indica se durante la contabilizzazione dell'area magazzino deve essere creata l'area
     * contabile.
     */
    private boolean generaAreaContabile;

    private boolean gestioneConai;

    private boolean gestioneVending;

    /**
     * Costruttore di default.
     */
    public TipoAreaMagazzino() {
        this.tipoDocumento = new TipoDocumento();
        this.qtaZeroPermessa = false;
        this.provenienzaPrezzo = ProvenienzaPrezzo.LISTINO;
        this.operazioneAreaContabileNonTrovata = OperazioneAreaContabileNonTrovata.NONAVVISARE;
        this.tipoMovimento = TipoMovimento.NESSUNO;
        this.aggiornaCostoUltimo = false;
        this.noteSuDestinazione = true;
        this.strategiaTotalizzazioneDocumento = StrategiaTotalizzazioneDocumento.NORMALE;
        this.strategiaEsportazioneFlusso = StrategiaEsportazioneFlusso.NESSUNO;
        this.tipoDocumentoPerFatturazioneDescrizioneMaschera = "";
        this.gestioneAgenti = false;
        this.gestioneConai = false;
        this.numeroCopiePerStampa = 0;
        this.datiAccompagnatoriMetaData = new TreeSet<DatoAccompagnatorioMagazzinoMetaData>();
        this.riepilogoIva = false;
        this.gestioneVending = false;
    }

    @Override
    public Object clone() {
        TipoAreaMagazzino tipoAreaMagazzino = PanjeaEJBUtil.cloneObject(this);
        tipoAreaMagazzino.setId(null);
        tipoAreaMagazzino.setDatiAccompagnatoriMetaData(new TreeSet<DatoAccompagnatorioMagazzinoMetaData>());
        if (this.datiAccompagnatoriMetaData != null) {
            for (DatoAccompagnatorioMagazzinoMetaData datoAccompagnatorioMagazzinoMetaData : this.datiAccompagnatoriMetaData) {
                DatoAccompagnatorioMagazzinoMetaData newDato = (DatoAccompagnatorioMagazzinoMetaData) datoAccompagnatorioMagazzinoMetaData
                        .clone();
                newDato.setTipoAreaMagazzino(tipoAreaMagazzino);
                tipoAreaMagazzino.getDatiAccompagnatoriMetaData().add(newDato);
            }
        }

        return tipoAreaMagazzino;
    }

    /**
     * @return the datiAccompagnatoriMetaData
     */
    public Set<DatoAccompagnatorioMagazzinoMetaData> getDatiAccompagnatoriMetaData() {
        return datiAccompagnatoriMetaData;
    }

    /**
     * @return the depositoDestinazione
     */
    public DepositoLite getDepositoDestinazione() {
        return depositoDestinazione;
    }

    /**
     * @return the depositoOrigine
     */
    public DepositoLite getDepositoOrigine() {
        return depositoOrigine;
    }

    /**
     * @return the descrizionePerStampa
     */
    @Override
    public String getDescrizionePerStampa() {
        return descrizionePerStampa;
    }

    /**
     * @return the entitaPredefinita
     */
    public EntitaLite getEntitaPredefinita() {
        return entitaPredefinita;
    }

    @Override
    public String getFormulaStandardNumeroCopie() {
        return "$Copie tipo documento$ + $Copie vettore$";
    }

    /**
     * @return Returns the listino.
     */
    public Listino getListino() {
        return listino;
    }

    /**
     * @return the numeroCopiePerStampa
     */
    public Integer getNumeroCopiePerStampa() {
        return numeroCopiePerStampa;
    }

    /**
     * @return tipo di operazione da eseguire se l'are acontabile non è stata trovata;<br/>
     *         <ul>
     *         <li>NONAVVISARE: crea l'area magazzino senza nessun messaggio
     *         <li>AVVISARE: avvisa che l'area contabile non esiste
     *         <li>BLOCCA: blocca la creazione dell'area di magazzino se l'area contabile non è
     *         trovata
     *         </ul>
     */
    public OperazioneAreaContabileNonTrovata getOperazioneAreaContabileNonTrovata() {
        return operazioneAreaContabileNonTrovata;
    }

    /**
     * @return ritorna la provenienza del prezzo per il calcolo delle righe articolo dell'area
     *         magazzino
     *         <ul>
     *         <li>LISTINO: calcola il prezzo in base alla configurazione Listini/contratti
     *         <li>ULTIMO COSTO:calcola il prezzo in base alll'utlimo costo dell'articolo nella data
     *         del documento.
     *         </ul>
     */
    public ProvenienzaPrezzo getProvenienzaPrezzo() {
        return provenienzaPrezzo;
    }

    @Override
    public String getReportPath() {
        return REPORT_PATH;
    }

    /**
     * @return sezione del tipo movimento per la quale viene movimentata la merge (aquistato/venduto
     *         o generico). Utile nelle analisi.
     */
    public ESezioneTipoMovimento getSezioneTipoMovimento() {
        return sezioneTipoMovimento;
    }

    /**
     * @return the strategiaEsportazioneFlusso
     */
    public StrategiaEsportazioneFlusso getStrategiaEsportazioneFlusso() {
        return strategiaEsportazioneFlusso;
    }

    /**
     * @return the strategiaCalcoloTotaleDocumento
     */
    public StrategiaTotalizzazioneDocumento getStrategiaTotalizzazioneDocumento() {
        return strategiaTotalizzazioneDocumento;
    }

    /**
     * @return tipoDocumento associato
     */
    @Override
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @return tipo documento di destinazione per lo use case fatturazione.
     */
    public TipoDocumento getTipoDocumentoPerFatturazione() {
        return tipoDocumentoPerFatturazione;
    }

    /**
     * @return tipoDocumentoPerFatturazioneDescrizioneMaschera
     */
    public String getTipoDocumentoPerFatturazioneDescrizioneMaschera() {
        if (tipoDocumentoPerFatturazioneDescrizioneMaschera == null) {
            tipoDocumentoPerFatturazioneDescrizioneMaschera = "$descrizioneTipoDocumento$ numero  $numeroDocumento$ del $dataDocumento$";
        }
        return tipoDocumentoPerFatturazioneDescrizioneMaschera;
    }

    /**
     * @return the tipoDocumentoPerFatturazionePA
     */
    public TipoDocumento getTipoDocumentoPerFatturazionePA() {
        return tipoDocumentoPerFatturazionePA;
    }

    /**
     * @return tipoMovimento
     */
    public TipoMovimento getTipoMovimento() {
        return tipoMovimento;
    }

    /**
     * @return the aggiornaCostoUltimo
     */
    public boolean isAggiornaCostoUltimo() {
        return aggiornaCostoUltimo;
    }

    /**
     * @return dataDocLikeDataReg
     */
    public boolean isDataDocLikeDataReg() {
        return dataDocLikeDataReg;
    }

    /**
     * @return the depositoDestinazioneBloccato
     */
    public boolean isDepositoDestinazioneBloccato() {
        return depositoDestinazioneBloccato;
    }

    /**
     * @return Returns the depositoOrigineBloccato.
     */
    public boolean isDepositoOrigineBloccato() {
        return depositoOrigineBloccato;
    }

    /**
     * @return the generaAreaContabile
     */
    public boolean isGeneraAreaContabile() {
        return generaAreaContabile;
    }

    /**
     * @return Returns the gestioneAgenti.
     */
    public boolean isGestioneAgenti() {
        return gestioneAgenti;
    }

    /**
     * @return the gestioneConai
     */
    public boolean isGestioneConai() {
        return gestioneConai;
    }

    /**
     * @return the gestioneVending
     */
    public final boolean isGestioneVending() {
        return gestioneVending;
    }

    /**
     * @return <code>true</code> se viene inserito nel datawarehouse
     */
    public boolean isMovimentaDatawarehouse() {
        return !((tipoMovimento == TipoMovimento.NESSUNO || tipoMovimento == TipoMovimento.INVENTARIO)
                && !isValoriFatturato());
    }

    /**
     * @return <code>true</code> se movimenta il magazzino
     */
    public boolean isMovimentaMagazzino() {
        return tipoMovimento == TipoMovimento.CARICO || tipoMovimento == TipoMovimento.SCARICO
                || tipoMovimento == TipoMovimento.INVENTARIO || tipoMovimento == TipoMovimento.TRASFERIMENTO
                || tipoMovimento == TipoMovimento.CARICO_PRODUZIONE;
    }

    /**
     * @return noteSuDestinazione
     */
    public boolean isNoteSuDestinazione() {
        return noteSuDestinazione;
    }

    /**
     * @return Returns the qtaZeroPermessa.
     */
    public boolean isQtaZeroPermessa() {
        return qtaZeroPermessa;
    }

    /**
     * @return the richiestaMezzo
     */
    public boolean isRichiestaMezzoTrasporto() {
        return richiestaMezzoTrasporto;
    }

    /**
     * @return Returns the richiestaMezzoTrasportoObbligatorio.
     */
    public boolean isRichiestaMezzoTrasportoObbligatorio() {
        return richiestaMezzoTrasportoObbligatorio;
    }

    /**
     *
     * @return true se devo visualizzare un riepilogoIva sull'editor
     */
    public boolean isRiepilogoIva() {
        return riepilogoIva;
    }

    /**
     * @return valoriFatturato
     */
    public boolean isValoriFatturato() {
        return valoriFatturato;
    }

    /**
     * @param aggiornaCostoUltimo
     *            the aggiornaCostoUltimo to set
     */
    public void setAggiornaCostoUltimo(boolean aggiornaCostoUltimo) {
        this.aggiornaCostoUltimo = aggiornaCostoUltimo;
    }

    /**
     * @param dataDocLikeDataReg
     *            the dataDocLikeDataReg to set
     */
    public void setDataDocLikeDataReg(boolean dataDocLikeDataReg) {
        this.dataDocLikeDataReg = dataDocLikeDataReg;
    }

    /**
     * @param datiAccompagnatoriMetaData
     *            the datiAccompagnatoriMetaData to set
     */
    public void setDatiAccompagnatoriMetaData(Set<DatoAccompagnatorioMagazzinoMetaData> datiAccompagnatoriMetaData) {
        this.datiAccompagnatoriMetaData = datiAccompagnatoriMetaData;
    }

    /**
     * @param depositoDestinazione
     *            the depositoDestinazione to set
     */
    public void setDepositoDestinazione(DepositoLite depositoDestinazione) {
        this.depositoDestinazione = depositoDestinazione;
    }

    /**
     * @param depositoDestinazioneBloccato
     *            the depositoDestinazioneBloccato to set
     */
    public void setDepositoDestinazioneBloccato(boolean depositoDestinazioneBloccato) {
        this.depositoDestinazioneBloccato = depositoDestinazioneBloccato;
    }

    /**
     * @param depositoOrigine
     *            the depositoOrigine to set
     */
    public void setDepositoOrigine(DepositoLite depositoOrigine) {
        this.depositoOrigine = depositoOrigine;
    }

    /**
     * @param depositoOrigineBloccato
     *            the depositoOrigineBloccato to set
     */
    public void setDepositoOrigineBloccato(boolean depositoOrigineBloccato) {
        this.depositoOrigineBloccato = depositoOrigineBloccato;
    }

    /**
     * @param descrizionePerStampa
     *            the descrizionePerStampa to set
     */
    public void setDescrizionePerStampa(String descrizionePerStampa) {
        this.descrizionePerStampa = descrizionePerStampa;
    }

    /**
     * @param entitaPredefinita
     *            the entitaPredefinita to set
     */
    public void setEntitaPredefinita(EntitaLite entitaPredefinita) {
        this.entitaPredefinita = entitaPredefinita;
    }

    /**
     * @param generaAreaContabile
     *            the generaAreaContabile to set
     */
    public void setGeneraAreaContabile(boolean generaAreaContabile) {
        this.generaAreaContabile = generaAreaContabile;
    }

    /**
     * @param gestioneAgenti
     *            The gestioneAgenti to set.
     */
    public void setGestioneAgenti(boolean gestioneAgenti) {
        this.gestioneAgenti = gestioneAgenti;
    }

    /**
     * @param gestioneConai
     *            the gestioneConai to set
     */
    public void setGestioneConai(boolean gestioneConai) {
        this.gestioneConai = gestioneConai;
    }

    /**
     * @param gestioneVending
     *            the gestioneVending to set
     */
    public final void setGestioneVending(boolean gestioneVending) {
        this.gestioneVending = gestioneVending;
    }

    /**
     * @param listino
     *            The listino to set.
     */
    public void setListino(Listino listino) {
        this.listino = listino;
    }

    /**
     * @param noteSuDestinazione
     *            the noteSuDestinazione to set
     */
    public void setNoteSuDestinazione(boolean noteSuDestinazione) {
        this.noteSuDestinazione = noteSuDestinazione;
    }

    /**
     * @param numeroCopiePerStampa
     *            the numeroCopiePerStampa to set
     */
    public void setNumeroCopiePerStampa(Integer numeroCopiePerStampa) {
        this.numeroCopiePerStampa = numeroCopiePerStampa;
    }

    /**
     * @param operazioneAreaContabileNonTrovata
     *            the operazioneAreaContabileNonTrovata to set
     */
    public void setOperazioneAreaContabileNonTrovata(
            OperazioneAreaContabileNonTrovata operazioneAreaContabileNonTrovata) {
        this.operazioneAreaContabileNonTrovata = operazioneAreaContabileNonTrovata;
    }

    /**
     * @param provenienzaPrezzo
     *            the provenienzaPrezzo to set
     */
    public void setProvenienzaPrezzo(ProvenienzaPrezzo provenienzaPrezzo) {
        this.provenienzaPrezzo = provenienzaPrezzo;
    }

    /**
     * @param qtaZeroPermessa
     *            The qtaZeroPermessa to set.
     */
    public void setQtaZeroPermessa(boolean qtaZeroPermessa) {
        this.qtaZeroPermessa = qtaZeroPermessa;
    }

    /**
     * @param richiestaMezzoTrasporto
     *            the richiestaMezzo to set
     */
    public void setRichiestaMezzoTrasporto(boolean richiestaMezzoTrasporto) {
        this.richiestaMezzoTrasporto = richiestaMezzoTrasporto;
        if (!richiestaMezzoTrasporto) {
            richiestaMezzoTrasportoObbligatorio = false;
        }
    }

    /**
     * @param richiestaMezzoTrasportoObbligatorio
     *            The richiestaMezzoTrasportoObbligatorio to set.
     */
    public void setRichiestaMezzoTrasportoObbligatorio(boolean richiestaMezzoTrasportoObbligatorio) {
        this.richiestaMezzoTrasportoObbligatorio = richiestaMezzoTrasportoObbligatorio;
    }

    /**
     *
     * @param riepilogoIva
     *            set riepilogoIva
     */
    public void setRiepilogoIva(boolean riepilogoIva) {
        this.riepilogoIva = riepilogoIva;
    }

    /**
     * @param sezioneTipoMovimento
     *            the sezioneTipoMovimento to set
     */
    public void setSezioneTipoMovimento(ESezioneTipoMovimento sezioneTipoMovimento) {
        this.sezioneTipoMovimento = sezioneTipoMovimento;
    }

    /**
     * @param strategiaEsportazioneFlusso
     *            the strategiaEsportazioneFlusso to set
     */
    public void setStrategiaEsportazioneFlusso(StrategiaEsportazioneFlusso strategiaEsportazioneFlusso) {
        this.strategiaEsportazioneFlusso = strategiaEsportazioneFlusso;
    }

    /**
     * @param strategiaTotalizzazioneDocumento
     *            the strategiaCalcoloTotaleDocumento to set
     */
    public void setStrategiaTotalizzazioneDocumento(StrategiaTotalizzazioneDocumento strategiaTotalizzazioneDocumento) {
        this.strategiaTotalizzazioneDocumento = strategiaTotalizzazioneDocumento;
    }

    /**
     * @param tipoDocumento
     *            the tipoDocumento to set
     */
    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @param tipoDocumentoPerFatturazione
     *            the tipoDocumentoPerFatturazione to set
     */
    public void setTipoDocumentoPerFatturazione(TipoDocumento tipoDocumentoPerFatturazione) {
        this.tipoDocumentoPerFatturazione = tipoDocumentoPerFatturazione;
    }

    /**
     * @param tipoDocumentoPerFatturazioneDescrizioneMaschera
     *            the tipoDocumentoPerFatturazioneDescrizioneMaschera to set
     */
    public void setTipoDocumentoPerFatturazioneDescrizioneMaschera(
            String tipoDocumentoPerFatturazioneDescrizioneMaschera) {
        this.tipoDocumentoPerFatturazioneDescrizioneMaschera = tipoDocumentoPerFatturazioneDescrizioneMaschera;
    }

    /**
     * @param tipoDocumentoPerFatturazionePA
     *            the tipoDocumentoPerFatturazionePA to set
     */
    public void setTipoDocumentoPerFatturazionePA(TipoDocumento tipoDocumentoPerFatturazionePA) {
        this.tipoDocumentoPerFatturazionePA = tipoDocumentoPerFatturazionePA;
    }

    /**
     * @param tipoMovimento
     *            the tipoMovimento to set
     */
    public void setTipoMovimento(TipoMovimento tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    /**
     * @param valoriFatturato
     *            the valoriFatturato to set
     */
    public void setValoriFatturato(boolean valoriFatturato) {
        this.valoriFatturato = valoriFatturato;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(super.toString());
        if (tipoDocumento != null) {
            buffer.append(" TipoDocumento = ").append(tipoDocumento);
        } else {
            buffer.append(" TipoDocumento = null");
        }
        if (getDepositoOrigine() != null) {
            buffer.append(" Deposito id = " + getDepositoOrigine().getId());
        } else {
            buffer.append(" Deposito null");
        }
        return buffer.toString();
    }
}