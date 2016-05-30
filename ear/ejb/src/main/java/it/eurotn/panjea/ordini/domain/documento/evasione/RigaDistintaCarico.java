package it.eurotn.panjea.ordini.domain.documento.evasione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaTestata;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.util.CRC;

/**
 * Gestisce una riga ordine nel processo di evasione.<br>
 * Viene utilizzata per riempire il carrello della distinta di carico, salvata sulla distinta di carico ed infine evasa.
 * Se la distintaDiCarico==null allora è una riga evasione altrimenti è una riga di una distinta di carico pronta per
 * essere evasa.
 *
 * @author fattazzo
 */
@Entity
@Table(name = "ordi_riga_distinta_carico")
@Audited
@NamedQueries({
        @NamedQuery(name = "RigaDistintaCarico.caricaEntitaSenzaTipoDocumentoEvasione", query = "select rigaOrd.areaOrdine.documento.entita  from RigaDistintaCarico rigaEv, RigaOrdine rigaOrd  where rigaEv.rigaArticolo.id = rigaOrd.id and rigaEv.datiEvasioneDocumento.tipoAreaEvasione is null") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING, length = 2)
@DiscriminatorValue("A")
public class RigaDistintaCarico extends EntityBase {

    public enum GestioneGiacenza {
        DAEVADERE_CONSIDERA_GIACENZA, DAEVADERE_EQ_INEVASA, NESSUNO
    }

    /**
     * @author fattazzo
     */
    public enum StatoRiga {
        SELEZIONABILE, SELEZIONATA, NEL_CARRELLO
    }

    private static final long serialVersionUID = 1002341854604881397L;

    private static final Double DOUBLE_ZERO = new Double(0);

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "rigaDistintaCarico")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<RigaDistintaCaricoLotto> righeDistintaLotto;

    @Transient
    private int livello;

    @Transient
    private Integer idRigaTestata;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private DistintaCarico distintaCarico;

    private Integer idConfigurazioneDistinta;

    @Transient
    private Double qtaGiacenza;

    /**
     * Utilizzata in gulliver al ritorno dell'ordine.
     */
    private String areaSpedizione;

    @Transient
    private Double qtaOrdinata;

    @Transient
    private String trasportoCura;

    private Double qtaEvasa;

    private Double qtaDaEvadere;

    @Transient
    private Double qtaGiacenzaAttuale;

    private double moltiplicatoreQta;

    @Transient
    protected StatoRiga stato = StatoRiga.SELEZIONABILE;

    private Boolean forzata;

    /**
     * Dati da utilizzare durante l'evasione del documento. Vengono usati solamente per velocizzare il processo di
     * evasione.
     */
    @Embedded
    private DatiEvasioneDocumento datiEvasioneDocumento;

    @Column(precision = 19, scale = 6)
    private BigDecimal prezzoUnitario;

    @Column(precision = 19, scale = 6)
    private BigDecimal prezzoNetto;

    @Column(precision = 19, scale = 6)
    private BigDecimal prezzoTotale;

    @Column(length = 3)
    private String codiceValuta;

    private Integer numeroDecimaliPrezzo;

    private BigDecimal variazione1;

    private BigDecimal variazione2;

    private BigDecimal variazione3;

    private BigDecimal variazione4;

    private TipoOmaggio tipoOmaggio;

    @Transient
    private GestioneGiacenza gestioneGiacenza;

    @Transient
    private List<RigaDistintaCarico> righeSostituzione;

    @ManyToOne(fetch = FetchType.LAZY)
    private RigaArticolo rigaArticolo;

    @ManyToOne
    private ArticoloLite articolo;

    @Transient
    private CRC checksum = null;

    @Transient
    private SedeEntita sedeEntitaEvasione;

    {
        this.prezzoUnitario = BigDecimal.ZERO;
        this.qtaDaEvadere = 0.0;
        this.numeroDecimaliPrezzo = 0;
        this.tipoOmaggio = TipoOmaggio.NESSUNO;
        this.setId(new Long(Calendar.getInstance().getTimeInMillis()).intValue());
        this.stato = StatoRiga.SELEZIONABILE;
        this.moltiplicatoreQta = 1.0;
        this.qtaGiacenza = 0.0;
        this.qtaEvasa = 0.0;
        this.righeSostituzione = new ArrayList<RigaDistintaCarico>();
        this.forzata = Boolean.FALSE;
        this.datiEvasioneDocumento = new DatiEvasioneDocumento();
        this.gestioneGiacenza = GestioneGiacenza.DAEVADERE_CONSIDERA_GIACENZA;
        this.qtaOrdinata = 0.0;
        this.rigaArticolo = new RigaArticolo();
        this.rigaArticolo.getAreaOrdine().setTipoAreaOrdine(new TipoAreaOrdine());
        this.rigaArticolo.getAreaOrdine().getDocumento().setEntita(new EntitaLite());
        this.rigaArticolo.getAreaOrdine().getDocumento().setSedeEntita(new SedeEntita());
        this.rigaArticolo.getAreaOrdine().setDepositoOrigine(new DepositoLite());
        this.rigaArticolo.setArticolo(new ArticoloLite());
        this.articolo = new ArticoloLite();
    }

    /**
     * Costruttore.
     */
    public RigaDistintaCarico() {
        super();
    }

    /**
     * Aggiorna il valore del prezzo netto e totale in base a quello unitario e sconti.
     */
    private void aggiornaPrezzi() {
        // Creo uno sconto "al volo" ed utilizzo il suo metodo per applicarlo
        Sconto sconto = new Sconto();
        sconto.setSconto1(variazione1);
        sconto.setSconto2(variazione2);
        sconto.setSconto3(variazione3);
        sconto.setSconto4(variazione4);
        prezzoNetto = sconto.applica(prezzoUnitario, numeroDecimaliPrezzo);

        double qta = qtaDaEvadere != null ? qtaDaEvadere : 0;

        prezzoTotale = prezzoNetto.multiply(BigDecimal.valueOf(qta)).setScale(
                it.eurotn.panjea.magazzino.domain.RigaArticolo.SCALE_IMPORTO_TOTALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Aggiorna il valore della quantità da evadere in base alla giacenza, alla quantità ordinata e alla quantità evasa.
     */
    private void aggiornaQtaDaEvadere() {
        switch (gestioneGiacenza) {
        case DAEVADERE_EQ_INEVASA:
            qtaDaEvadere = qtaOrdinata - qtaEvasa;
            break;
        case DAEVADERE_CONSIDERA_GIACENZA:
            if (qtaGiacenza >= (qtaOrdinata - qtaEvasa)) {
                qtaDaEvadere = qtaOrdinata - qtaEvasa;
            } else {
                qtaDaEvadere = 0.0;
            }
            break;
        default:
            break;
        }
    }

    /**
     * Aggiorna lo stato della riga in base alle qta assegnate.
     */
    protected void aggiornaStato() {
        stato = StatoRiga.SELEZIONABILE;
        if (forzata) {
            stato = StatoRiga.SELEZIONATA;
        }

        // Se ho una distinta collegata lo stato deve tener conto della qtaEvasa
        if (distintaCarico != null && !DOUBLE_ZERO.equals(qtaEvasa)) {
            stato = StatoRiga.SELEZIONATA;
        }

        // Se non ho una distinta collegata lo stato deve tener conto della
        // qtaDaEvadere. Non passo da una distinta per
        // evadere
        if (distintaCarico == null && !DOUBLE_ZERO.equals(qtaDaEvadere)) {
            stato = StatoRiga.SELEZIONATA;
        }

    }

    /**
     * Aggiunge una riga di sostituzione articolo.
     *
     * @param rigaEvasioneSost
     *            {@link ArticoloLite}
     *
     * @return true se la riga è stata aggiunta. false se la riga non è stata aggiunta perchè non valida. <br/>
     *         La riga è valida se ha articolo,qta e molt. avvalorati.
     */
    public boolean aggiungiRigaSostituzione(RigaDistintaCarico rigaEvasioneSost) {
        if (rigaEvasioneSost.getArticolo() == null) {
            return false;
        }
        if (rigaEvasioneSost.getQtaDaEvadere() == null
                || rigaEvasioneSost.getQtaDaEvadere().compareTo(new Double(0.0)) <= 0) {
            return false;
        }

        rigaEvasioneSost.setRigaArticolo(this.getRigaArticolo());

        // setto il deposito per poi trovare la giacenza articolo-deposito
        rigaEvasioneSost.setIdDeposito(this.getDeposito().getId());
        rigaEvasioneSost.setCodiceDeposito(this.getDeposito().getCodice());
        rigaEvasioneSost.setDescrizioneDeposito(this.getDeposito().getDescrizione());

        // setto l'id della riga perchè è la proprietà sulla quale si basa
        // l'equals
        // rigaEvasioneSost.setId(new
        // Long(Calendar.getInstance().getTimeInMillis()).intValue());

        rigaEvasioneSost.setGestioneGiacenza(GestioneGiacenza.NESSUNO);
        this.righeSostituzione.add(rigaEvasioneSost);
        return true;
    }

    /**
     * Svuota le righe di sostituzione.
     */
    public void clearRigheSostituzione() {
        righeSostituzione = new ArrayList<RigaDistintaCarico>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RigaDistintaCarico other = (RigaDistintaCarico) obj;
        if (rigaArticolo == null) {
            if (other.rigaArticolo != null) {
                return false;
            }
        } else if (!rigaArticolo.equals(other.rigaArticolo)) {
            return false;
        }
        return true;
    }

    /**
     * @return araeSpedizione delle rigaDistinta evasa.
     */
    public String getAreaSpedizione() {
        return areaSpedizione;
    }

    /**
     * @return the articolo
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return checksum delle riga evasione
     */
    public int getChecksum() {
        if (checksum == null) {
            checksum = new CRC();
            checksum.update(this.getDatiEvasioneDocumento().getTipoAreaEvasione());
            checksum.update(this.getDatiEvasioneDocumento().getCodicePagamento());
            checksum.update(this.idConfigurazioneDistinta);
            AreaOrdine area = this.rigaArticolo.getAreaOrdine();
            checksum.update(area.getDepositoOrigine());
            checksum.update(area.getDocumento().getSedeEntita());
            checksum.update(area.getListinoAlternativo());
            checksum.update(area.getListino());
            checksum.update(area.isAddebitoSpeseIncasso());
            checksum.update(area.isRaggruppamentoBolle());
            checksum.update(area.getSedeVettore());
            checksum.update(area.getCausaleTrasporto());
            checksum.update(area.getTipoPorto());
        }
        return checksum.getInt31();
    }

    /**
     * @return Returns the codiceValuta.
     */
    public String getCodiceValuta() {
        return codiceValuta;
    }

    /**
     * @return the dataConsegna
     */
    public Date getDataConsegna() {
        return rigaArticolo.getDataConsegna();
    }

    /**
     * @return the dataInizioTrasporto
     */
    public Date getDataInizioTrasporto() {
        return rigaArticolo.getAreaOrdine().getDataInizioTrasporto();
    }

    /**
     * @return the dataRegistrazione
     */
    public Date getDataRegistrazione() {
        return rigaArticolo.getAreaOrdine().getDataRegistrazione();
    }

    /**
     * @return the datiEvasioneDocumento
     */
    public DatiEvasioneDocumento getDatiEvasioneDocumento() {
        return datiEvasioneDocumento;
    }

    /**
     * @return the deposito
     */
    public DepositoLite getDeposito() {
        return rigaArticolo.getAreaOrdine().getDepositoOrigine();
    }

    /**
     * @return Returns the descrizioneTestata.
     */
    public String getDescrizioneTestata() {
        return rigaArticolo.getRigaTestataCollegata() != null ? rigaArticolo.getRigaTestataCollegata().getDescrizione()
                : "";
    }

    /**
     * @return Returns the distintaCarico.
     */
    public DistintaCarico getDistintaCarico() {
        return distintaCarico;
    }

    /**
     * @return the Entità
     */
    public EntitaDocumento getEntita() {
        return rigaArticolo.getAreaOrdine().getDocumento().getEntitaDocumento();
    }

    /**
     * @return the forzata
     */
    public Boolean getForzata() {
        return forzata;
    }

    /**
     * @return the gestioneGiacenza
     */
    public GestioneGiacenza getGestioneGiacenza() {
        return gestioneGiacenza;
    }

    /**
     * @return the idAreaOrdine
     */
    public Integer getIdAreaOrdine() {
        return rigaArticolo.getAreaOrdine().getId();
    }

    /**
     * @return Returns the idConfigurazioneDistinta.
     */
    public Integer getIdConfigurazioneDistinta() {
        return idConfigurazioneDistinta;
    }

    /**
     * @return Returns the idRigaTestata.
     */
    public Integer getIdRigaTestata() {
        return rigaArticolo.getRigaTestataCollegata() != null ? rigaArticolo.getRigaTestataCollegata().getId() : null;
    }

    /**
     * @return Returns the livello.
     */
    public int getLivello() {
        return livello;
    }

    /**
     * @return the moltiplicatoreQta
     */
    public double getMoltiplicatoreQta() {
        return moltiplicatoreQta;
    }

    /**
     * @return Returns the numeroDecimaliPrezzo.
     */
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the numeroDocumento
     */
    public CodiceDocumento getNumeroDocumento() {
        return rigaArticolo.getAreaOrdine().getDocumento().getCodice();
    }

    /**
     * @return Returns the prezzoNetto.
     */
    public BigDecimal getPrezzoNetto() {
        return prezzoNetto;
    }

    /**
     * @return Returns the prezzoTotale.
     */
    public BigDecimal getPrezzoTotale() {
        return prezzoTotale;
    }

    /**
     * @return Returns the prezzoUnitario.
     */
    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    /**
     * @return the qtaDaEvadere
     */
    public Double getQtaDaEvadere() {
        return qtaDaEvadere;
    }

    /**
     * @return the qtaDaEvadereSostituzione
     */
    public Double getQtaDaEvadereSostituzione() {
        Double qtaDaEvadereSost = 0.0;

        for (RigaDistintaCarico rigaEvasione : righeSostituzione) {
            qtaDaEvadereSost = qtaDaEvadereSost
                    + (rigaEvasione.getQtaDaEvadere() * rigaEvasione.getMoltiplicatoreQta());
        }
        return qtaDaEvadereSost;
    }

    /**
     * @return the qtaEvasa
     */
    public Double getQtaEvasa() {
        return qtaEvasa;
    }

    /**
     * @return the qtaGiacenza
     */
    public Double getQtaGiacenza() {
        return qtaGiacenza;
    }

    /**
     * @return the qtaGiacenzaAttuale
     */
    public Double getQtaGiacenzaAttuale() {
        return qtaGiacenzaAttuale;
    }

    /**
     * @return the qtaOrdinata
     */
    public Double getQtaOrdinata() {
        return qtaOrdinata;
    }

    /**
     * @return the qtaResidua
     */
    public Double getQtaResidua() {
        Double qtaResidua = qtaOrdinata - getQtaDaEvadereSostituzione();
        if (qtaDaEvadere != null) {
            qtaResidua = qtaResidua - qtaDaEvadere - qtaEvasa;
        }
        return qtaResidua;
    }

    /**
     * @return Returns the raggrupamento1.
     */
    public String getRaggrupamento1() {
        return variazione1.toString();
    }

    /**
     * @return Returns the raggrupamento2.
     */
    public String getRaggrupamento2() {
        return variazione2.toString();
    }

    /**
     * @return the rigaArticolo
     */
    public RigaArticolo getRigaArticolo() {
        return rigaArticolo;
    }

    /**
     * @return Returns the righeDistintaLotto.
     */
    public List<RigaDistintaCaricoLotto> getRigheDistintaLotto() {
        return righeDistintaLotto;
    }

    /**
     * @return the righeSostituzione
     */
    public List<RigaDistintaCarico> getRigheSostituzione() {
        return Collections.unmodifiableList(righeSostituzione);
    }

    /**
     * @return the sedeEntita
     */
    public SedeEntita getSedeEntita() {
        return rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita();
    }

    /**
     * @return Returns the sedeEntitaEvasione.
     */
    public SedeEntita getSedeEntitaEvasione() {
        if (sedeEntitaEvasione == null) {
            return getRigaArticolo().getAreaOrdine().getDocumento().getSedeEntita();
        }
        return sedeEntitaEvasione;
    }

    /**
     * @return the stato
     */
    public StatoRiga getStato() {
        return stato;
    }

    /**
     * @return Returns the tipoOmaggio.
     */
    public TipoOmaggio getTipoOmaggio() {
        return tipoOmaggio;
    }

    /**
     * @return Returns the tipoPorto.
     */
    public String getTrasportoCura() {
        return trasportoCura;
    }

    /**
     * @return Returns the variazione1.
     */
    public BigDecimal getVariazione1() {
        return variazione1;
    }

    /**
     * @return Returns the variazione2.
     */
    public BigDecimal getVariazione2() {
        return variazione2;
    }

    /**
     * @return Returns the variazione3.
     */
    public BigDecimal getVariazione3() {
        return variazione3;
    }

    /**
     * @return Returns the variazione4.
     */
    public BigDecimal getVariazione4() {
        return variazione4;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((rigaArticolo == null) ? 0 : rigaArticolo.hashCode());
        return result;
    }

    /**
     * @return true se la riga può essere inserita in una missione
     */
    public boolean isEvasioneAllowed() {
        return qtaEvasa.compareTo(new Double(0.0)) != 0 || getArticolo().isGestioneQuantitaZero();
    }

    /**
     * @return true se la riga può essere inserita in una missione
     */
    public boolean isMissioneAllowed() {
        return qtaDaEvadere.compareTo(new Double(0.0)) != 0 || getArticolo().isGestioneQuantitaZero();
    }

    /**
     * @return true se è una riga omaggio
     */
    public boolean isOmaggio() {
        if (this.tipoOmaggio != null) {
            return this.tipoOmaggio.isOmaggio();
        }
        return false;
    }

    /**
     * @return the sostituzione
     */
    public boolean isSostituita() {
        return !righeSostituzione.isEmpty();
    }

    /**
     * Metodo callback.
     */
    @PostLoad
    private void postLoad() {
        if (qtaEvasa == null) {
            qtaEvasa = 0.0;
        }
        aggiornaStato();
    }

    /**
     * Rimuove le righe di sostituzione articolo specificate. Se la riga evasione non contiene più righe sostituzione
     * viene aggiornato anche il suo stato "sotituzione".
     *
     * @param righeEvasione
     *            righe da rimuovere
     */
    public void rimuoviRigheSostituzione(Collection<RigaDistintaCarico> righeEvasione) {
        this.righeSostituzione.removeAll(righeEvasione);
    }

    /**
     * @param areaSpedizione
     *            areaSpedizione dove è stata evasa la riga.
     */
    public void setAreaSpedizione(String areaSpedizione) {
        this.areaSpedizione = areaSpedizione;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param codiceArticolo
     *            the codiceArticolo to set
     */
    public void setCodiceArticolo(String codiceArticolo) {
        rigaArticolo.getArticolo().setCodice(codiceArticolo);
    }

    /**
     * @param codiceDeposito
     *            the codiceDeposito to set
     */
    public void setCodiceDeposito(String codiceDeposito) {
        rigaArticolo.getAreaOrdine().getDepositoOrigine().setCodice(codiceDeposito);
    }

    /**
     * @param codiceEntita
     *            the codiceEntita to set
     */
    public void setCodiceEntita(Integer codiceEntita) {
        rigaArticolo.getAreaOrdine().getDocumento().getEntita().setCodice(codiceEntita);
    }

    /**
     * @param codiceTipoDocumento
     *            the codiceTipoDocumento to set
     */
    public void setCodiceTipoDocumento(String codiceTipoDocumento) {
        rigaArticolo.getAreaOrdine().getDocumento().getTipoDocumento().setCodice(codiceTipoDocumento);
        rigaArticolo.getAreaOrdine().getTipoAreaOrdine().getTipoDocumento().setCodice(codiceTipoDocumento);
    }

    /**
     * @param codiceValuta
     *            The codiceValuta to set.
     */
    public void setCodiceValuta(String codiceValuta) {
        this.codiceValuta = codiceValuta;
    }

    /**
     * @param dataConsegna
     *            the dataConsegna to set
     */
    public void setDataConsegna(Date dataConsegna) {
        rigaArticolo.setDataConsegna(dataConsegna);
    }

    /**
     * @param dataDocumento
     *            the dataDocumento to set
     */
    public void setDataDocumento(Date dataDocumento) {
        rigaArticolo.getAreaOrdine().getDocumento().setDataDocumento(dataDocumento);
    }

    /**
     * @param dataRegistrazione
     *            the dataRegistrazione to set
     */
    public void setDataRegistrazione(Date dataRegistrazione) {
        rigaArticolo.getAreaOrdine().setDataRegistrazione(dataRegistrazione);
    }

    /**
     * @param datiEvasioneDocumento
     *            the datiEvasioneDocumento to set
     */
    public void setDatiEvasioneDocumento(DatiEvasioneDocumento datiEvasioneDocumento) {
        this.datiEvasioneDocumento = datiEvasioneDocumento;
    }

    /**
     * @param descrizioneDeposito
     *            the descrizioneDeposito to set
     */
    public void setDescrizioneDeposito(String descrizioneDeposito) {
        rigaArticolo.getAreaOrdine().getDepositoOrigine().setDescrizione(descrizioneDeposito);
    }

    /**
     * @param descrizioneEntita
     *            the descrizioneEntita to set
     */
    public void setDescrizioneEntita(String descrizioneEntita) {
        rigaArticolo.getAreaOrdine().getDocumento().getEntita().getAnagrafica().setDenominazione(descrizioneEntita);
    }

    /**
     * @param descrizioneRiga
     *            the descrizioneRiga to set
     */
    public void setDescrizioneRiga(String descrizioneRiga) {
        rigaArticolo.getArticolo().setDescrizione(descrizioneRiga);
    }

    /**
     * @param descrizioneSedeEntita
     *            the descrizioneSedeEntita to set
     */
    public void setDescrizioneSedeEntita(String descrizioneSedeEntita) {
        rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().getSede().setDescrizione(descrizioneSedeEntita);
    }

    /**
     * @param descrizioneTestata
     *            The descrizioneTestata to set.
     */
    public void setDescrizioneTestata(String descrizioneTestata) {
        rigaArticolo.getRigaTestataCollegata().setDescrizione(descrizioneTestata);
    }

    /**
     * @param descrizioneTipoDocumento
     *            the descrizioneTipoDocumento to set
     */
    public void setDescrizioneTipoDocumento(String descrizioneTipoDocumento) {
        rigaArticolo.getAreaOrdine().getDocumento().getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
        rigaArticolo.getAreaOrdine().getTipoAreaOrdine().getTipoDocumento().setDescrizione(descrizioneTipoDocumento);
    }

    /**
     * @param distintaCarico
     *            The distintaCarico to set.
     */
    public void setDistintaCarico(DistintaCarico distintaCarico) {
        this.distintaCarico = distintaCarico;
    }

    /**
     * @param forzata
     *            the forzata to set
     */
    public void setForzata(Boolean forzata) {
        this.forzata = forzata;
        aggiornaStato();
        aggiornaPrezzi();
    }

    /**
     * @param gestioneGiacenza
     *            the gestioneGiacenzato set
     */
    public void setGestioneGiacenza(GestioneGiacenza gestioneGiacenza) {
        this.gestioneGiacenza = gestioneGiacenza;
        aggiornaQtaDaEvadere();
    }

    /**
     * @param idAreaOrdine
     *            the idAreaOrdine to set
     */
    public void setIdAreaOrdine(Integer idAreaOrdine) {
        rigaArticolo.getAreaOrdine().setId(idAreaOrdine);
    }

    /**
     * @param idArticolo
     *            the idArticolo to set
     */
    public void setIdArticolo(Integer idArticolo) {
        rigaArticolo.getArticolo().setId(idArticolo);
    }

    /**
     * @param idConfigurazioneDistinta
     *            The idConfigurazioneDistinta to set.
     */
    public void setIdConfigurazioneDistinta(Integer idConfigurazioneDistinta) {
        this.idConfigurazioneDistinta = idConfigurazioneDistinta;
    }

    /**
     * @param idDeposito
     *            the idDeposito to set
     */
    public void setIdDeposito(Integer idDeposito) {
        rigaArticolo.getAreaOrdine().getDepositoOrigine().setId(idDeposito);
    }

    /**
     * @param idDocumento
     *            the idDocumento to set
     */
    public void setIdDocumento(Integer idDocumento) {
        rigaArticolo.getAreaOrdine().getDocumento().setId(idDocumento);
    }

    /**
     * @param idEntita
     *            the idEntita to set
     */
    public void setIdEntita(Integer idEntita) {
        rigaArticolo.getAreaOrdine().getDocumento().getEntita().setId(idEntita);
    }

    /**
     * @param id
     *            id Articolo
     */
    public void setIdRigaArticolo(Integer id) {
        rigaArticolo.setId(id);
    }

    /**
     * @param idRigaTestata
     *            The idRigaTestata to set.
     */
    public void setIdRigaTestata(Integer idRigaTestata) {
        RigaTestata rt = new RigaTestata();
        rt.setId(idRigaTestata);
        rigaArticolo.setRigaTestataCollegata(rt);
    }

    /**
     * @param idSedeEntita
     *            the idSedeEntita to set
     */
    public void setIdSedeEntita(Integer idSedeEntita) {
        rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().setId(idSedeEntita);
    }

    /**
     * @param idTipoDocumento
     *            the idTipoDocumento to set
     */
    public void setIdTipoDocumento(Integer idTipoDocumento) {
        rigaArticolo.getAreaOrdine().getDocumento().getTipoDocumento().setId(idTipoDocumento);
        rigaArticolo.getAreaOrdine().getTipoAreaOrdine().getTipoDocumento().setId(idTipoDocumento);
    }

    /**
     * @param livello
     *            The livello to set.
     */
    public void setLivello(int livello) {
        this.livello = livello;
    }

    /**
     * @param livelloAmministrativo1
     *            The livelloAmministrativo1 to set.
     */
    public void setLivelloAmministrativo1(String livelloAmministrativo1) {
        if (rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                .getLivelloAmministrativo1() == null) {
            rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                    .setLivelloAmministrativo1(new LivelloAmministrativo1());
        }
        rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                .getLivelloAmministrativo1().setNome(livelloAmministrativo1);
    }

    /**
     * @param livelloAmministrativo2
     *            The livelloAmministrativo2 to set.
     */
    public void setLivelloAmministrativo2(String livelloAmministrativo2) {
        if (rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                .getLivelloAmministrativo2() == null) {
            rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                    .setLivelloAmministrativo2(new LivelloAmministrativo2());
        }
        rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                .getLivelloAmministrativo2().setNome(livelloAmministrativo2);
    }

    /**
     * @param livelloAmministrativo3
     *            The livelloAmministrativo3 to set.
     */
    public void setLivelloAmministrativo3(String livelloAmministrativo3) {
        if (rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                .getLivelloAmministrativo3() == null) {
            rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                    .setLivelloAmministrativo3(new LivelloAmministrativo3());
        }
        rigaArticolo.getAreaOrdine().getDocumento().getSedeEntita().getSede().getDatiGeografici()
                .getLivelloAmministrativo3().setNome(livelloAmministrativo3);
    }

    /**
     * @param moltiplicatoreQta
     *            the moltiplicatoreQta to set
     */
    public void setMoltiplicatoreQta(double moltiplicatoreQta) {
        this.moltiplicatoreQta = moltiplicatoreQta;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            The numeroDecimaliPrezzo to set.
     */
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
        aggiornaPrezzi();
    }

    /**
     * @param numeroDocumento
     *            the numeroDocumento to set
     */
    public void setNumeroDocumento(CodiceDocumento numeroDocumento) {
        rigaArticolo.getAreaOrdine().getDocumento().setCodice(numeroDocumento);
    }

    /**
     * @param prezzoUnitario
     *            The prezzoUnitario to set.
     */
    public void setPrezzoUnitario(BigDecimal prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
        aggiornaPrezzi();
    }

    /**
     * @param qtaDaEvadere
     *            the qtaDaEvadere to set
     */
    public void setQtaDaEvadere(Double qtaDaEvadere) {
        this.qtaDaEvadere = qtaDaEvadere;
        aggiornaStato();
        aggiornaPrezzi();
    }

    /**
     * @param qtaEvasa
     *            the qtaEvasa to set
     */
    public void setQtaEvasa(Double qtaEvasa) {
        if (qtaEvasa == null) {
            qtaEvasa = 0.0;
        }
        this.qtaEvasa = qtaEvasa;
        aggiornaStato();
    }

    /**
     * @param qtaGiacenza
     *            the qtaGiacenza to set
     */
    public void setQtaGiacenza(Double qtaGiacenza) {
        this.qtaGiacenza = qtaGiacenza;
        aggiornaQtaDaEvadere();
    }

    /**
     * @param qtaGiacenzaAttuale
     *            the qtaGiacenzaAttuale to set
     */
    public void setQtaGiacenzaAttuale(Double qtaGiacenzaAttuale) {
        this.qtaGiacenzaAttuale = qtaGiacenzaAttuale;
    }

    /**
     * @param qtaOrdinata
     *            the qtaOrdinata to set
     */
    protected void setQtaOrdinata(Double qtaOrdinata) {
        this.qtaOrdinata = qtaOrdinata;
        aggiornaQtaDaEvadere();
    }

    /**
     * @param rigaArticolo
     *            the rigaArticolo to set
     */
    public void setRigaArticolo(RigaArticolo rigaArticolo) {
        this.rigaArticolo = rigaArticolo;
    }

    /**
     * @param righeDistintaLotto
     *            The righeDistintaLotto to set.
     */
    public void setRigheDistintaLotto(List<RigaDistintaCaricoLotto> righeDistintaLotto) {
        this.righeDistintaLotto = righeDistintaLotto;
    }

    /**
     * @param righeSostituzione
     *            the righeSostituzione to set
     */
    public void setRigheSostituzione(List<RigaDistintaCarico> righeSostituzione) {
        this.righeSostituzione = righeSostituzione;
    }

    /**
     * @param sedeEntitaEvasione
     *            The sedeEntitaEvasione to set.
     */
    public void setSedeEntitaEvasione(SedeEntita sedeEntitaEvasione) {
        this.sedeEntitaEvasione = sedeEntitaEvasione;
    }

    /**
     * @param stato
     *            the stato to set
     */
    public void setStato(StatoRiga stato) {
        this.stato = stato;
        if (forzata) {
            return;
        }
        if (distintaCarico != null && stato == StatoRiga.SELEZIONATA) {
            qtaEvasa = qtaDaEvadere;
        }
        if (distintaCarico != null && stato == StatoRiga.SELEZIONABILE) {
            qtaEvasa = 0.0;
        }
    }

    /**
     * @param tipoEntita
     *            the tipoEntita to set
     */
    public void setTipoEntita(TipoEntita tipoEntita) {
        rigaArticolo.getAreaOrdine().getDocumento().getTipoDocumento().setTipoEntita(tipoEntita);
        rigaArticolo.getAreaOrdine().getTipoAreaOrdine().getTipoDocumento().setTipoEntita(tipoEntita);
    }

    /**
     * @param tipoOmaggio
     *            The tipoOmaggio to set.
     */
    public void setTipoOmaggio(TipoOmaggio tipoOmaggio) {
        this.tipoOmaggio = tipoOmaggio;
    }

    /**
     * @param trasportoCura
     *            The tipoPorto to set.
     */
    public void setTrasportoCura(String trasportoCura) {
        this.trasportoCura = trasportoCura;
    }

    /**
     * @param variazione1
     *            The variazione1 to set.
     */
    public void setVariazione1(BigDecimal variazione1) {
        this.variazione1 = variazione1;
        aggiornaPrezzi();
    }

    /**
     * @param variazione2
     *            The variazione2 to set.
     */
    public void setVariazione2(BigDecimal variazione2) {
        this.variazione2 = variazione2;
        aggiornaPrezzi();
    }

    /**
     * @param variazione3
     *            The variazione3 to set.
     */
    public void setVariazione3(BigDecimal variazione3) {
        this.variazione3 = variazione3;
        aggiornaPrezzi();
    }

    /**
     * @param variazione4
     *            The variazione4 to set.
     */
    public void setVariazione4(BigDecimal variazione4) {
        this.variazione4 = variazione4;
        aggiornaPrezzi();
    }

    /**
     * @param versionAreaOrdine
     *            the versionAreaOrdine to set
     */
    public void setVersionAreaOrdine(Integer versionAreaOrdine) {
        rigaArticolo.getAreaOrdine().setVersion(versionAreaOrdine);
    }

    /**
     * @param versionDeposito
     *            the versionDeposito to set
     */
    public void setVersionDeposito(Integer versionDeposito) {
        rigaArticolo.getAreaOrdine().getDepositoOrigine().setVersion(versionDeposito);
    }

    /**
     * @param versionTipoDocumento
     *            the versionTipoDocumento to set
     */
    public void setVersionTipoDocumento(Integer versionTipoDocumento) {
        rigaArticolo.getAreaOrdine().getDocumento().getTipoDocumento().setVersion(versionTipoDocumento);
        rigaArticolo.getAreaOrdine().getTipoAreaOrdine().getTipoDocumento().setVersion(versionTipoDocumento);
    }

    @Override
    public String toString() {
        return "RigaDistintaCarico [id=" + getId() + " + distintaCarico=" + distintaCarico + ", qtaOrdinata="
                + qtaOrdinata + ", qtaEvasa=" + qtaEvasa + ", qtaDaEvadere=" + qtaDaEvadere + ", stato=" + stato
                + ", forzata=" + forzata + ", tipoOmaggio=" + tipoOmaggio + "]";
    }

}
