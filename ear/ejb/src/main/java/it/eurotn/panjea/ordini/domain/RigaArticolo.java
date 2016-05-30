package it.eurotn.panjea.ordini.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistintaBase;
import it.eurotn.panjea.magazzino.domain.DescrizionePoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.descrizionilingua.DescrizioneLinguaTipoAttributo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.util.RigaArticoloDTO;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.panjea.pagamenti.domain.AzioneScontoCommerciale;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Riga contenuta in un area di ordine. <br>
 * La riga è associata ad un articolo ed ha i valori per poter definire un prezzo finale.
 *
 * @author giangi
 */
@Entity(name = "RigaArticoloOrdine")
@DiscriminatorValue("A")
@Audited
@NamedQueries({
        @NamedQuery(name = "RigaArticoloOrdine.caricaImponibiliIva", query = " select sum(riga.prezzoTotale.importoInValuta), sum(riga.prezzoTotale.importoInValutaAzienda), riga.codiceIva from RigaArticoloOrdine riga where riga.areaOrdine = :paramArea group by riga.codiceIva having sum(riga.prezzoTotale.importoInValutaAzienda) <> 0"),
        @NamedQuery(name = "RigaArticoloOrdine.caricaByTipo", query = " select sum(riga.prezzoTotale.importoInValuta), sum(riga.prezzoTotale.importoInValutaAzienda), riga.articolo.tipoArticolo from RigaArticoloOrdine riga where riga.areaOrdine = :paramArea group by riga.articolo.tipoArticolo") })
public class RigaArticolo extends RigaOrdine implements IRigaArticoloDocumento {

    private static Logger logger = Logger.getLogger(RigaArticolo.class);

    private static final long serialVersionUID = -1102284459781308936L;

    public static final int SCALE_IMPORTO_TOTALE = 2;

    private Integer numeroDecimaliPrezzo;
    /**
     * Giacenza calcolata prima della stampa dell'ordine.
     */
    @Embedded
    private Giacenza giacenza;

    /**
     * Indica se la riga è un omaggio.
     */
    private TipoOmaggio tipoOmaggio;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private FormulaTrasformazione formulaTrasformazioneQtaMagazzino;

    @Column(precision = 5, scale = 2)
    private BigDecimal percProvvigione;

    @ManyToOne(fetch = FetchType.LAZY)
    private ConfigurazioneDistinta configurazioneDistinta;

    /**
     * Quantità che movimenta il magazzino. Tramite la formulaTrasformazioneQtaMagazzino viene trasformata la qta in
     * qtaMagazzino
     */
    @Column(nullable = true)
    private Double qtaMagazzino;

    @Column(nullable = true)
    private Double qtaMagazzinoTolleranza;

    /**
     * Quantità di vendita/acquisto. Su questa quantità si basa il prezzo totale
     */
    @Column(precision = 10, scale = 6, nullable = true)
    private Double qta;

    /**
     * Qta richiesta in fase di calcolo Mrp.
     */
    @Column(precision = 10, scale = 6, nullable = true)
    private Double qtaR;

    @Transient
    private double qtaEvasa;

    @Transient
    private double qtaAttrezzaggio;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private CategoriaContabileArticolo categoriaContabileArticolo;

    private String descrizione; // In lingua aziendale. Copiata dall'articolo

    private String descrizioneLingua; // In lingua entità. Copiata dall'articolo

    private boolean articoloLibero;

    /**
     * Memorizzo la stringa dell'unita di misura perchè non deve mai cambiare una volta inserita in riga.
     */
    @Column(length = 3)
    private String unitaMisura;

    /**
     * Memorizzo la stringa dell'unita di misura perchè non deve mai cambiare.
     */
    @Column(length = 3)
    private String unitaMisuraQtaMagazzino;

    private String formulaConversioneUnitaMisura;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private FormulaTrasformazione formulaTrasformazione;

    @ManyToOne
    private ArticoloLite articolo;

    @ManyToOne
    private ArticoloLite articoloPadre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rigaArticolo")
    private List<RigaDistintaCarico> righeDistintaCarico;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private CodiceIva codiceIva;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "rigaArticolo")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("riga,ordine")
    @Fetch(FetchMode.JOIN)
    private List<AttributoRiga> attributi;

    /**
     * Rappresenta il prezzo determinato con la politica calcolo prezzo in valuta azienda.
     */
    private BigDecimal prezzoDeterminato;

    /**
     * Prezzo totale netto generato durante l'importazione degli ordini da Aton.
     */
    private BigDecimal prezzoNettoImportazione;

    /**
     * Prezzo totale unitario generato durante l'importazione degli ordini da Aton.
     */
    private BigDecimal prezzoUnitarioImportazione;

    @Column(precision = 5, scale = 2)
    private BigDecimal variazione1Importazione;

    @Column(precision = 5, scale = 2)
    private BigDecimal variazione2Importazione;

    @Column(precision = 5, scale = 2)
    private BigDecimal variazione3Importazione;

    @Column(precision = 5, scale = 2)
    private BigDecimal variazione4Importazione;

    @Embedded
    private Importo prezzoUnitario;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaNetto", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaNetto", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioNetto", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaNetto", length = 3) ) })
    private Importo prezzoNetto;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaTotale", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaTotale", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioTotale", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaTotale", length = 3) ) })
    private Importo prezzoTotale;

    @Embedded
    private DescrizionePoliticaPrezzo descrizionePoliticaPrezzo;

    @Column(precision = 5, scale = 2)
    private BigDecimal variazione1;

    @Column(precision = 5, scale = 2)
    private BigDecimal variazione2;

    @Column(precision = 5, scale = 2)
    private BigDecimal variazione3;

    @Column(precision = 5, scale = 2)
    private BigDecimal variazione4;

    private Integer numeroDecimaliQta;

    @Transient
    private PoliticaPrezzo politicaPrezzo;

    private Date dataConsegna;

    private Date dataProduzione;

    private Date dataFineProduzione;

    private boolean bloccata;

    private boolean sconto1Bloccato;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "rigaOrdineCollegata")
    private List<RigaMagazzino> righeMagazzinoCollegate;

    @Transient
    private String codiceArticoloEntita;

    @Transient
    private String descrizioneArticoloEntita;

    @Transient
    private String barCodeEntita;

    private boolean ivata;

    @Transient
    private Importo prezzoUnitarioReale;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaUnitarioImposta", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaUnitarioImposta", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioUnitarioImposta", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaUnitarioImposta", length = 3) ) })
    private Importo prezzoUnitarioImposta;

    {
        this.articoloLibero = false;
        variazione1 = BigDecimal.ZERO;
        variazione2 = BigDecimal.ZERO;
        variazione3 = BigDecimal.ZERO;
        variazione4 = BigDecimal.ZERO;
        prezzoUnitario = new Importo();
        prezzoNetto = new Importo();
        prezzoTotale = new Importo();
        numeroDecimaliPrezzo = 4;
        qta = 0.0;
        qtaAttrezzaggio = 0.0;
        qtaMagazzino = 0.0;
        this.tipoOmaggio = TipoOmaggio.NESSUNO;
        this.sconto1Bloccato = Boolean.FALSE;
        barCodeEntita = "";
        ivata = Boolean.FALSE;
        prezzoUnitarioImposta = new Importo();
        prezzoUnitarioReale = new Importo();
    }

    /**
     * Aggiunge una variazione in coda alle altre.
     *
     * @param variazione
     *            variazione da aggiungere
     * @param percProvv
     *            percentuale provvigione
     * @param forzaVariazione
     *            forza l'applicazione delle variazioni su sconto e provvigione
     */
    public void aggiungiVariazione(BigDecimal variazione, BigDecimal percProvv, boolean forzaVariazione) {

        variazione = variazione == null ? BigDecimal.ZERO : variazione;

        boolean variazioniPresenti = politicaPrezzo != null && !politicaPrezzo.isPoliticaScontiPresenti()
                && variazione.compareTo(BigDecimal.ZERO) != 0;

        // aggiungo la variazione solo se non ci sono sconti determinati nella
        // politica prezzo
        if (variazioniPresenti || forzaVariazione) {
            Sconto sconto = new Sconto(variazione1, variazione2, variazione3, variazione4);
            sconto.aggiungiInCoda(variazione, politicaPrezzo != null && politicaPrezzo.isSconto1Bloccato());
            variazione1 = sconto.getSconto1();
            variazione2 = sconto.getSconto2();
            variazione3 = sconto.getSconto3();
            variazione4 = sconto.getSconto4();
        }

        boolean provvigionePresente = percProvv != null;
        if (provvigionePresente || forzaVariazione) {
            setPercProvvigione(percProvv);
        }

        updateValoriImporto();
    }

    @Override
    public void applicaPoliticaPrezzo() {
        Double qtaRiga = getQta();
        if (qtaRiga == null) {
            qtaRiga = new Double(0.0);
        }
        applicaPoliticaPrezzo(qtaRiga);
    }

    @Override
    public void applicaPoliticaPrezzo(double qtaRiga) {
        setNumeroDecimaliPrezzo(articolo.getNumeroDecimaliPrezzo());
        if (prezzoUnitario.getCodiceValuta() == null || prezzoUnitario.getTassoDiCambio() == null) {
            throw new IllegalStateException("impostare codice valuta e tasso di cambio al prezzo unitario");
        }

        if (politicaPrezzo.getPrezzi().size() > 0) {

            setIvata(politicaPrezzo.isPrezzoIvato());
            // Dalla politica prezzi prendolo scaglione per la qta e setto il
            // numero decimali del prezzo
            RisultatoPrezzo<BigDecimal> risultatoPrezzo = politicaPrezzo.getPrezzi().getRisultatoPrezzo(qtaRiga);
            if (risultatoPrezzo != null) {
                numeroDecimaliPrezzo = risultatoPrezzo.getNumeroDecimali();
                if (numeroDecimaliPrezzo != null) {
                    setNumeroDecimaliPrezzo(numeroDecimaliPrezzo);
                }

                Importo tmp = prezzoUnitario.clone();
                if (tmp != null) {
                    tmp.setImportoInValuta(risultatoPrezzo.getValue());
                    tmp.calcolaImportoValutaAzienda(numeroDecimaliPrezzo);
                    initPrezzoUnitarioReale(tmp);
                }
                setPrezzoDeterminato(prezzoUnitario.getImportoInValuta());
            }
        }
        if (politicaPrezzo.getSconti().size() > 0) {
            RisultatoPrezzo<Sconto> risultatoSconto = politicaPrezzo.getSconti().getRisultatoPrezzo(qtaRiga);
            if (risultatoSconto != null) {
                setSconto1Bloccato(politicaPrezzo.isSconto1Bloccato());
                setVariazione1(risultatoSconto.getValue().getSconto1());
                setVariazione2(risultatoSconto.getValue().getSconto2());
                setVariazione3(risultatoSconto.getValue().getSconto3());
                setVariazione4(risultatoSconto.getValue().getSconto4());
            }
        }

        if (politicaPrezzo.getProvvigioni().size() > 0) {
            RisultatoPrezzo<BigDecimal> risultatoProvvigioni = politicaPrezzo.getProvvigioni()
                    .getRisultatoPrezzo(qtaRiga);
            if (risultatoProvvigioni != null) {
                setPercProvvigione(risultatoProvvigioni.getValue());
            }
        }
        updateValoriImporto();
    }

    /**
     * Applica lo sconto commerciale.
     *
     * @param azioneScontoCommerciale
     *            inserisci o rimuovi sconto
     * @param importoSconto
     *            importo dello sconto
     */
    public void applicaScontoCommerciale(AzioneScontoCommerciale azioneScontoCommerciale, BigDecimal importoSconto) {

        // non tocco le righe omaggio, evase o parzialmente evase
        if (isOmaggio() || isEvasa() || qtaEvasa > 0) {
            return;
        }

        setSconto1Bloccato(Boolean.FALSE);
        switch (azioneScontoCommerciale) {
        case RIMUOVI:
            setVariazione1(getVariazione2());
            setVariazione2(getVariazione3());
            setVariazione3(getVariazione4());
            setVariazione4(BigDecimal.ZERO);
            break;
        case INSERISCI:
            setVariazione4(getVariazione3());
            setVariazione3(getVariazione2());
            setVariazione2(getVariazione1());
            setVariazione1(importoSconto);
            setSconto1Bloccato(Boolean.TRUE);
            break;
        default:
            throw new UnsupportedOperationException("Azione sconto commerciale non supportata.");
        }

    }

    @Override
    protected RigaArticolo clone() {

        RigaArticolo rigaArticoloClone = null;
        try {
            rigaArticoloClone = (RigaArticolo) super.clone();
            rigaArticoloClone.setId(null);

            List<AttributoRiga> listAttributiClone = new ArrayList<AttributoRiga>();
            for (AttributoRiga attributoRiga : this.attributi) {
                AttributoRiga attributoRigaClone = (AttributoRiga) attributoRiga.clone();
                attributoRigaClone.setId(null);
                listAttributiClone.add(attributoRigaClone);
            }
            rigaArticoloClone.setAttributi(listAttributiClone);
        } catch (CloneNotSupportedException e) {
            logger.debug("--> Eccezione non considerata.");
        }
        return rigaArticoloClone;
    }

    @Override
    public AttributoRigaArticolo creaAttributoRiga(AttributoArticolo attributoArticolo) {
        AttributoRiga attributoRiga = new AttributoRiga();
        attributoRiga.setTipoAttributo(attributoArticolo.getTipoAttributo());
        attributoRiga.setUpdatable(attributoArticolo.getUpdatable());
        attributoRiga.setValore(attributoArticolo.getValore());
        attributoRiga.setFormula(attributoArticolo.getFormula());
        attributoRiga.setRiga(attributoArticolo.getRiga());
        attributoRiga.setOrdine(attributoArticolo.getOrdine());
        attributoRiga.setStampa(attributoArticolo.getStampa());
        attributoRiga.setObbligatorio(attributoArticolo.getObbligatorio());
        attributoRiga.setRigaArticolo(this);
        attributoRiga.setRicalcolaInEvasione(attributoArticolo.getRicalcolaInEvasione());
        return attributoRiga;
    }

    /**
     *
     * @return istanza di riga di magazzino coerente con la rigaArticolo
     */
    protected it.eurotn.panjea.magazzino.domain.RigaArticolo creaIstanzaRigaMagazzino() {
        return new it.eurotn.panjea.magazzino.domain.RigaArticolo();
    }

    @Override
    protected RigaOrdineDTO creaIstanzaRigaOrdineDTO() {
        RigaArticoloDTO rigaArticoloDTO = new RigaArticoloDTO();
        if (!isNew()) {
            rigaArticoloDTO.setCodice(getArticolo().getCodice());
            rigaArticoloDTO.setIdArticolo(getArticolo().getId());
            rigaArticoloDTO.setDescrizioneArticolo(getArticolo().getDescrizione());
            rigaArticoloDTO.setDescrizione(getArticolo().getDescrizione());
            rigaArticoloDTO.setCodiceEntita(getArticolo().getCodiceEntita());
            Sconto sconto = new Sconto(getVariazione1(), getVariazione2(), getVariazione3(), getVariazione4());
            rigaArticoloDTO.setSconto(sconto);
            // la riga ordine non gestisce la qtaChiusa quindi la imposto a 0
            rigaArticoloDTO.setQtaChiusa(0.00);
        }
        return rigaArticoloDTO;
    }

    /**
     *
     * @return rigaArticolo di magazzino con gli stessi dati della riga ordine.<br/>
     *         Utilizzata per l'evasione di una riga ordine su una riga di magazzino
     */
    public it.eurotn.panjea.magazzino.domain.RigaArticolo creaRigaArticoloMagazzino() {
        it.eurotn.panjea.magazzino.domain.RigaArticolo rigaArticoloMagazzino = creaIstanzaRigaMagazzino();
        rigaArticoloMagazzino = initRigaArticoloMagazzino(rigaArticoloMagazzino);
        return rigaArticoloMagazzino;
    }

    @Override
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the articoloPadre
     */
    public ArticoloLite getArticoloPadre() {
        return articoloPadre;
    }

    /**
     * @return attributi
     */
    @Override
    public List<AttributoRiga> getAttributi() {
        if (attributi == null) {
            attributi = new ArrayList<AttributoRiga>();
        }
        return attributi;
    }

    @Override
    public String getAttributiDescrizione(String lingua) {
        StringBuffer sb = new StringBuffer();
        for (AttributoRiga attributo : getAttributi()) {
            if (attributo.isStampa()) {

                if (sb.length() > 0) {
                    sb.append(" - ");
                }

                DescrizioneLinguaTipoAttributo descrizioneInLingua = null;

                if (lingua != null) {
                    descrizioneInLingua = attributo.getTipoAttributo().getNomiLingua().get(lingua);
                }

                // descrizione tipo attributo
                if (descrizioneInLingua != null) {
                    sb.append(descrizioneInLingua.getDescrizione());
                } else {
                    sb.append(attributo.getTipoAttributo().getNome());
                }

                // unità di minura
                if (attributo.getTipoAttributo().getUnitaMisura() != null) {
                    sb.append(" [" + attributo.getTipoAttributo().getUnitaMisura().getCodice() + "]");
                }

                sb.append(":");

                sb.append(attributo.getValore());

                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param codiceAttributo
     *            codiceAttributo
     * @return attributo corrispondente al codice. Null se non trovato.
     */
    public AttributoRiga getAttributo(String codiceAttributo) {
        for (AttributoRiga attributoRiga : attributi) {
            if (codiceAttributo.equals(attributoRiga.getTipoAttributo().getCodice())) {
                return attributoRiga;
            }
        }
        return null;
    }

    /**
     * @return Returns the barCodeEntita.
     */
    public String getBarCodeEntita() {
        return barCodeEntita;
    }

    @Override
    public CategoriaContabileArticolo getCategoriaContabileArticolo() {
        return categoriaContabileArticolo;
    }

    /**
     * @return the codiceArticoloEntita
     */
    public String getCodiceArticoloEntita() {
        return codiceArticoloEntita;
    }

    @Override
    public CodiceIva getCodiceIva() {
        return codiceIva;
    }

    @Override
    public Set<IRigaArticoloDocumento> getComponenti() {
        return null;
    }

    /**
     * @return Returns the configurazioneDistinta.
     */
    public ConfigurazioneDistinta getConfigurazioneDistinta() {
        return configurazioneDistinta;
    }

    /**
     * @return the dataConsegna
     */
    public Date getDataConsegna() {
        return dataConsegna;
    }

    /**
     *
     * @return data di fine produzione
     */
    public Date getDataFineProduzione() {
        return dataFineProduzione;
    }

    /**
     * @return the dataProduzione
     */
    public Date getDataProduzione() {
        return dataProduzione;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the descrizioneArticoloEntita
     */
    public String getDescrizioneArticoloEntita() {
        return descrizioneArticoloEntita;
    }

    @Override
    public String getDescrizioneLingua() {
        return descrizioneLingua;
    }

    @Override
    public DescrizionePoliticaPrezzo getDescrizionePoliticaPrezzo() {
        return descrizionePoliticaPrezzo;
    }

    @Override
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua) {
        String linguaEntita = (areaOrdine.getDocumento().getSedeEntita() != null)
                ? areaOrdine.getDocumento().getSedeEntita().getLingua() : null;

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html>");
        if (linguaEntita != null && linguaEntita.equals(lingua) && getDescrizioneLingua() != null) {
            stringBuffer.append(getDescrizioneLingua());
        } else {
            stringBuffer.append(getDescrizione());
        }

        if (stampaNote && getNoteRiga() != null) {
            String noteRiga = getNotePerStampa(lingua);
            if (noteRiga != null) {
                stringBuffer.append(noteRiga);
            }
        }
        if (stampaAttributi && attributi != null && attributi.size() > 0) {
            String attrs = getAttributiDescrizione(lingua);
            if (attrs.length() > 0) {
                stringBuffer.append("<br>").append(attrs);
            }
        }
        stringBuffer.append("</html>");
        return stringBuffer.toString();
    }

    /**
     * @return the formulaConversioneUnitaMisura
     */
    @Override
    public String getFormulaConversioneUnitaMisura() {
        return formulaConversioneUnitaMisura;
    }

    @Override
    public FormulaTrasformazione getFormulaTrasformazione() {
        return formulaTrasformazione;
    }

    /**
     * @return the formulaTrasformazioneQtaMagazzino
     */
    @Override
    public FormulaTrasformazione getFormulaTrasformazioneQtaMagazzino() {
        return formulaTrasformazioneQtaMagazzino;
    }

    /**
     * @return Returns the giacenza.
     */
    @Override
    public Giacenza getGiacenza() {
        if (giacenza == null) {
            giacenza = new Giacenza(0.0, 0.0);
        }
        return giacenza;
    }

    /**
     * Calcola il prezzo netto ivato.
     *
     * @return prezzo ivato
     */
    public BigDecimal getImpostaIvata() {

        BigDecimal impostaTmp = getPrezzoUnitarioImposta().getImportoInValuta();

        if (variazione1 != null && variazione1.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal prezzoIvato = getPrezzoIvato();
            // scorporo il prezzo
            BigDecimal imponibileUnitario = prezzoIvato.divide(
                    BigDecimal.ONE.add(getCodiceIva().getPercApplicazione().divide(new BigDecimal(100))),
                    numeroDecimaliPrezzo, RoundingMode.HALF_UP);

            // l'imposta va sempre arrotondata a 2 decimali
            impostaTmp = imponibileUnitario.multiply(getCodiceIva().getPercApplicazione().divide(new BigDecimal(100)))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return impostaTmp;
    }

    @Override
    public String getNotePerStampa(String lingua) {
        String linguaEntita = (areaOrdine.getDocumento().getSedeEntita() != null)
                ? areaOrdine.getDocumento().getSedeEntita().getLingua() : null;

        String noteRiga = null;
        if (linguaEntita != null && linguaEntita.equals(lingua)) {
            noteRiga = getNoteLinguaRiga();
        } else {
            noteRiga = getNoteRiga();
        }
        String separatoreRiga = "<br>";
        if (noteRiga != null && noteRiga.indexOf("<html>") != -1) {
            String head = StringUtils.substringBetween(noteRiga, "<head>", "</head>");
            if (head != null) {
                noteRiga = noteRiga.replace(head, "");
            }
            noteRiga = noteRiga.replaceAll("<html>", "");
            noteRiga = noteRiga.replaceAll("</html>", "");
            noteRiga = noteRiga.replaceAll("<head>", "");
            noteRiga = noteRiga.replaceAll("</head>", "");
            noteRiga = noteRiga.replaceAll("<body>", "");
            noteRiga = noteRiga.replaceAll("</body>", "");
            noteRiga = noteRiga.replaceAll("<p style=\"margin-top: 0\">", "<div>");
            noteRiga = noteRiga.replaceAll("</p>", "</div>");
            noteRiga = noteRiga.replaceAll("<div>", separatoreRiga);
            noteRiga = noteRiga.replaceAll("</div>", "");
            noteRiga = noteRiga.trim();
            if (!noteRiga.toLowerCase().startsWith(separatoreRiga)) {
                noteRiga = separatoreRiga + noteRiga;
            }
        }
        if (noteRiga != null && noteRiga.equals(separatoreRiga)) {
            noteRiga = null;
        }
        if (noteRiga != null) {
            noteRiga = "<i>" + noteRiga + "</i>";
        }
        return noteRiga;
    }

    @Override
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    @Override
    public Integer getNumeroDecimaliQta() {
        return numeroDecimaliQta;
    }

    /**
     * @return Returns the percProvvigione.
     */
    @Override
    public BigDecimal getPercProvvigione() {
        return percProvvigione;
    }

    @Override
    public PoliticaPrezzo getPoliticaPrezzo() {
        return politicaPrezzo;
    }

    /**
     * @return the prezzoDeterminato
     */
    @Override
    public BigDecimal getPrezzoDeterminato() {
        return prezzoDeterminato;
    }

    @Override
    public BigDecimal getPrezzoIvato() {
        updateValoriImporto(2);
        BigDecimal prezzoUnitarioTmp = getPrezzoUnitarioReale().getImportoInValuta();

        try {
            Sconto sconto = new Sconto();
            sconto.setSconto1(variazione1);
            sconto.setSconto2(variazione2);
            sconto.setSconto3(variazione3);
            sconto.setSconto4(variazione4);
            prezzoUnitarioTmp = sconto.applica(prezzoUnitarioTmp, 2);
            if (!isIvata()) {
                BigDecimal percApplicazioneIva = getCodiceIva().getPercApplicazione();
                BigDecimal impostaCalcolata = prezzoUnitarioTmp.multiply(percApplicazioneIva).divide(Importo.HUNDRED);
                prezzoUnitarioTmp = prezzoUnitarioTmp.add(impostaCalcolata).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
        } finally {
            updateValoriImporto();
        }
        return prezzoUnitarioTmp;
    }

    @Override
    public BigDecimal getPrezzoIvatoInValutaAzienda() {
        updateValoriImporto(2);

        BigDecimal prezzoUnitarioTmp = getPrezzoUnitarioReale().getImportoInValutaAzienda();
        BigDecimal impostaCalcolata = BigDecimal.ZERO;
        try {
            Sconto sconto = new Sconto();
            sconto.setSconto1(variazione1);
            sconto.setSconto2(variazione2);
            sconto.setSconto3(variazione3);
            sconto.setSconto4(variazione4);
            prezzoUnitarioTmp = sconto.applica(prezzoUnitarioTmp, 2);
            if (!isIvata()) {
                BigDecimal percApplicazioneIva = getCodiceIva().getPercApplicazione();
                impostaCalcolata = prezzoUnitarioTmp.multiply(percApplicazioneIva).divide(Importo.HUNDRED, 2,
                        BigDecimal.ROUND_HALF_UP);
            }
        } finally {
            // ripristino i valori con i decimali corretti
            updateValoriImporto(numeroDecimaliPrezzo);
        }

        return prezzoUnitarioTmp.add(impostaCalcolata);
    }

    @Override
    public Importo getPrezzoNetto() {
        return prezzoNetto;
    }

    /**
     * @return the prezzoNettoImportazione
     */
    public BigDecimal getPrezzoNettoImportazione() {
        return prezzoNettoImportazione;
    }

    @Override
    public Importo getPrezzoTotale() {
        return prezzoTotale;
    }

    @Override
    public Importo getPrezzoUnitario() {
        return prezzoUnitario;
    }

    /**
     * @return the prezzoUnitarioImportazione
     */
    public BigDecimal getPrezzoUnitarioImportazione() {
        return prezzoUnitarioImportazione;
    }

    /**
     * @return the prezzoUnitarioImposta
     */
    public Importo getPrezzoUnitarioImposta() {
        return prezzoUnitarioImposta;
    }

    /**
     *
     * @return prezzo unitario reale
     */
    public Importo getPrezzoUnitarioReale() {
        updatePrezzoUnitarioReale();

        return prezzoUnitarioReale;
    }

    @Override
    public Double getQta() {
        return qta;
    }

    /**
     * @return the qtaAttrezzaggio
     */
    public double getQtaAttrezzaggio() {
        return qtaAttrezzaggio;
    }

    /**
     * @return the qtaEvasa
     */
    public double getQtaEvasa() {
        return qtaEvasa;
    }

    /**
     * @return the qtaMagazzino
     */
    @Override
    public Double getQtaMagazzino() {
        return qtaMagazzino;
    }

    /**
     * @return Returns the qtaMagazzinoTolleranza.
     */
    public Double getQtaMagazzinoTolleranza() {
        return qtaMagazzinoTolleranza;
    }

    /**
     * @return Returns the qtaR.
     */
    public Double getQtaR() {
        return qtaR;
    }

    /**
     * @return Returns the righeDistintaCarico.
     */
    public List<RigaDistintaCarico> getRigaDistintaCarico() {
        return righeDistintaCarico;
    }

    /**
     * @return righe magazzino collegate alla riga ordine.
     */
    public List<RigaMagazzino> getRigheMagazzinoCollegate() {
        return righeMagazzinoCollegate;
    }

    /**
     * @return Returns the tipoOmaggio.
     */
    @Override
    public TipoOmaggio getTipoOmaggio() {
        return tipoOmaggio;
    }

    @Override
    public String getUnitaMisura() {
        return unitaMisura;
    }

    /**
     * @return the unitaMisuraQtaMagazzino
     */
    @Override
    public String getUnitaMisuraQtaMagazzino() {
        return unitaMisuraQtaMagazzino;
    }

    @Override
    public String getValoreAttributo(String codiceAttributo) {
        return getValoreAttributoTipizzato(codiceAttributo, String.class);
    }

    @Override
    public <T> T getValoreAttributoTipizzato(String codiceAttributo, Class<T> returnType) {
        T valoreAttr = null;
        for (AttributoRiga attributoRiga : attributi) {
            if (codiceAttributo.equals(attributoRiga.getTipoAttributo().getCodice())) {
                valoreAttr = attributoRiga.getValoreTipizzato(returnType);
                break;
            }
        }
        return valoreAttr;
    }

    @Override
    public BigDecimal getVariazione1() {
        return variazione1;
    }

    /**
     * @return the variazione1Importazione
     */
    public BigDecimal getVariazione1Importazione() {
        return variazione1Importazione;
    }

    @Override
    public BigDecimal getVariazione2() {
        return variazione2;
    }

    /**
     * @return the variazione2Importazione
     */
    public BigDecimal getVariazione2Importazione() {
        return variazione2Importazione;
    }

    @Override
    public BigDecimal getVariazione3() {
        return variazione3;
    }

    /**
     * @return the variazione3Importazione
     */
    public BigDecimal getVariazione3Importazione() {
        return variazione3Importazione;
    }

    @Override
    public BigDecimal getVariazione4() {
        return variazione4;
    }

    /**
     * @return the variazione4Importazione
     */
    public BigDecimal getVariazione4Importazione() {
        return variazione4Importazione;
    }

    /**
     * @param prezzoUnitarioRealeParam
     *            the prezzoUnitarioReale to set
     */
    public void initPrezzoUnitarioReale(Importo prezzoUnitarioRealeParam) {
        this.prezzoUnitario = prezzoUnitarioRealeParam.clone();

        BigDecimal imponibileUnitario = prezzoUnitarioRealeParam.getImportoInValuta();
        BigDecimal impostaUnitario = BigDecimal.ZERO;

        if (isIvata()) {
            // scorporo il prezzo e "aggiusto se serve l'imponibile"
            imponibileUnitario = prezzoUnitarioRealeParam.getImportoInValuta().divide(
                    BigDecimal.ONE.add(getCodiceIva().getPercApplicazione().divide(new BigDecimal(100))),
                    numeroDecimaliPrezzo, RoundingMode.HALF_UP);

            // l'imposta va sempre arrotondata a 2 decimali
            impostaUnitario = imponibileUnitario
                    .multiply(getCodiceIva().getPercApplicazione().divide(new BigDecimal(100)))
                    .setScale(2, RoundingMode.HALF_UP);

            if (imponibileUnitario.add(impostaUnitario).compareTo(prezzoUnitarioRealeParam.getImportoInValuta()) != 0) {
                imponibileUnitario = prezzoUnitarioRealeParam.getImportoInValuta().subtract(impostaUnitario);
            }
        }

        prezzoUnitario.setImportoInValuta(imponibileUnitario);
        prezzoUnitario.calcolaImportoValutaAzienda(numeroDecimaliPrezzo);

        prezzoUnitarioImposta = prezzoUnitario.clone();
        prezzoUnitarioImposta.setImportoInValuta(impostaUnitario);
        prezzoUnitarioImposta.calcolaImportoValutaAzienda(numeroDecimaliPrezzo);

        updateValoriImporto();
    }

    /**
     * Inizializza tutte le proprietà della riga articolo magazzino utilizzando quelle della riga ordine.
     *
     * @param rigaArticolo
     *            rita articolo da inizializzare
     * @return riga articolo avvalorata
     */
    public it.eurotn.panjea.magazzino.domain.RigaArticolo initRigaArticoloMagazzino(
            it.eurotn.panjea.magazzino.domain.RigaArticolo rigaArticolo) {
        PanjeaEJBUtil.copyProperties(rigaArticolo, this);
        rigaArticolo.setId(null);
        rigaArticolo.setVersion(null);

        rigaArticolo.setResa(this.getArticolo().getResa());

        List<it.eurotn.panjea.magazzino.domain.AttributoRiga> attributiMagazzino = new ArrayList<it.eurotn.panjea.magazzino.domain.AttributoRiga>();
        for (AttributoRiga attributoOrdine : getAttributi()) {
            it.eurotn.panjea.magazzino.domain.AttributoRiga attributoMagazzino = attributoOrdine
                    .creaAttributoRigaMagazzino();
            attributoMagazzino.setRigaArticolo(rigaArticolo);
            attributiMagazzino.add(attributoMagazzino);
        }
        rigaArticolo.setAttributi(attributiMagazzino);
        rigaArticolo.setRigaOrdineCollegata(this);
        rigaArticolo.setAreaOrdineCollegata(getAreaOrdine());
        rigaArticolo.setAgente(getAreaOrdine().getAgente());

        return rigaArticolo;
    }

    @Override
    public boolean isArticoloLibero() {
        return articoloLibero;
    }

    /**
     *
     * @return true se la riga è bloccata e non può essere evasa
     */
    public boolean isBloccata() {
        return bloccata;
    }

    /**
     * Indica se la somma delle quantità delle righe collegate (qtaEvasa) è uguale alla quantità di riga.
     *
     * @return <code>true</code> se la somma delle righe collegate è uguale alla quantità di riga, <code>false</code> se
     *         la somma è minore.
     */
    public boolean isChiusa() {
        return this.qta > 0.0 && (this.qta - this.qtaEvasa <= 0.0);
    }

    /**
     *
     * @return true se i dati di importazione corrispondono ai dati della riga.
     */
    public boolean isDatiImportazioneCoerenti() {
        BigDecimal prezzoNettoImportazioneTest = prezzoNettoImportazione == null
                ? prezzoNetto.getImportoInValutaAzienda() : prezzoNettoImportazione;
        BigDecimal prezzoUnitarioImportazioneTest = prezzoUnitarioImportazione == null
                ? prezzoUnitario.getImportoInValutaAzienda() : prezzoUnitarioImportazione;

        boolean result = prezzoNetto.getImportoInValutaAzienda().compareTo(prezzoNettoImportazioneTest) == 0;
        result = result && prezzoUnitario.getImportoInValutaAzienda().compareTo(prezzoUnitarioImportazioneTest) == 0;
        BigDecimal variazione1Test = variazione1Importazione == null ? variazione1 : variazione1Importazione;
        result = result && variazione1.compareTo(variazione1Test) == 0;

        BigDecimal variazione2Test = variazione2Importazione == null ? variazione2 : variazione2Importazione;
        result = result && variazione2.compareTo(variazione2Test) == 0;

        BigDecimal variazione3Test = variazione3Importazione == null ? variazione3 : variazione3Importazione;
        result = result && variazione3.compareTo(variazione3Test) == 0;

        BigDecimal variazione4Test = variazione4Importazione == null ? variazione4 : variazione4Importazione;
        result = result && variazione4.compareTo(variazione4Test) == 0;
        return result;
    }

    /**
     * Indica se forzata o se la qta della riga coincide con la somma delle righe magazzino collegate.
     *
     * @return true se evasa o forzata, false altrimenti
     */
    public boolean isEvasa() {
        boolean evasa = false;

        if (isEvasioneForzata()) {
            evasa = true;
        } else {
            evasa = isChiusa();
        }
        return evasa;
    }

    /**
     * @return the ivata
     */
    public boolean isIvata() {
        return ivata;
    }

    /**
     * Indica se la riga è omaggio o meno controllando il valore di tipoOmaggio.
     *
     * @return true se tipoOmaggio diverso da NESSUNO, false altrimenti
     */
    public boolean isOmaggio() {
        return tipoOmaggio != null && !tipoOmaggio.equals(TipoOmaggio.NESSUNO);
    }

    /**
     * @return the sconto1Bloccato
     */
    @Override
    public boolean isSconto1Bloccato() {
        return sconto1Bloccato;
    }

    /**
     * @param articolo
     *            the articolo to set
     */
    @Override
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param articoloLibero
     *            the articoloLibero to set
     */
    @Override
    public void setArticoloLibero(boolean articoloLibero) {
        this.articoloLibero = articoloLibero;
    }

    /**
     * @param articoloPadre
     *            the articoloPadre to set
     */
    public void setArticoloPadre(ArticoloLite articoloPadre) {
        this.articoloPadre = articoloPadre;
    }

    /**
     * @param attributi
     *            the attributi to set
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setAttributi(List<? extends AttributoRigaArticolo> attributi) {
        this.attributi = (List<AttributoRiga>) attributi;
    }

    /**
     *
     * @param barCodeEntita
     *            bar code del fornitore/cliente
     */
    public void setBarCodeEntita(String barCodeEntita) {
        this.barCodeEntita = barCodeEntita;

    }

    /**
     * @param bloccata
     *            the bloccata to set
     */
    public void setBloccata(boolean bloccata) {
        this.bloccata = bloccata;
    }

    /**
     * @param categoriaContabileArticolo
     *            the categoriaContabileArticolo to set
     */
    @Override
    public void setCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo) {
        this.categoriaContabileArticolo = categoriaContabileArticolo;
    }

    /**
     * @param codiceArticoloEntita
     *            the codiceArticoloEntita to set
     */
    public void setCodiceArticoloEntita(String codiceArticoloEntita) {
        this.codiceArticoloEntita = codiceArticoloEntita;
    }

    /**
     * @param codiceIva
     *            the codiceIva to set
     */
    @Override
    public void setCodiceIva(CodiceIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    @Override
    public void setComponenti(Set<IRigaArticoloDocumento> componenti) {
        // non gestisco componenti
    }

    /**
     * @param configurazioneDistinta
     *            The configurazioneDistinta to set.
     */
    public void setConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta) {
        if (configurazioneDistinta instanceof ConfigurazioneDistintaBase) {
            this.configurazioneDistinta = null;
        } else {
            this.configurazioneDistinta = configurazioneDistinta;
        }
    }

    /**
     * @param dataConsegna
     *            the dataConsegna to set
     */
    public void setDataConsegna(Date dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    /**
     *
     * @param dataFineProduzione
     *            dataFineProduzione
     */
    public void setDataFineProduzione(Date dataFineProduzione) {
        this.dataFineProduzione = dataFineProduzione;
    }

    /**
     * @param dataProduzione
     *            the dataProduzione to set
     */
    public void setDataProduzione(Date dataProduzione) {
        this.dataProduzione = dataProduzione;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     */
    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param descrizioneArticoloEntita
     *            the descrizioneArticoloEntita to set
     */
    public void setDescrizioneArticoloEntita(String descrizioneArticoloEntita) {
        this.descrizioneArticoloEntita = descrizioneArticoloEntita;
    }

    /**
     * @param descrizioneLingua
     *            the descrizioneLingua to set
     */
    @Override
    public void setDescrizioneLingua(String descrizioneLingua) {
        this.descrizioneLingua = descrizioneLingua;
    }

    /**
     * @param descrizionePoliticaPrezzo
     *            the descrizionePoliticaPrezzo to set
     */
    public void setDescrizionePoliticaPrezzo(DescrizionePoliticaPrezzo descrizionePoliticaPrezzo) {
        this.descrizionePoliticaPrezzo = descrizionePoliticaPrezzo;
    }

    /**
     * @param formulaConversioneUnitaMisura
     *            the formulaConversioneUnitaMisura to set
     */
    @Override
    public void setFormulaConversioneUnitaMisura(String formulaConversioneUnitaMisura) {
        this.formulaConversioneUnitaMisura = formulaConversioneUnitaMisura;
    }

    /**
     * @param formulaTrasformazione
     *            the formulaTrasformazione to set
     */
    @Override
    public void setFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione) {
        this.formulaTrasformazione = formulaTrasformazione;
    }

    /**
     * @param formulaTrasformazioneQtaMagazzino
     *            the formulaTrasformazioneQtaMagazzino to set
     */
    @Override
    public void setFormulaTrasformazioneQtaMagazzino(FormulaTrasformazione formulaTrasformazioneQtaMagazzino) {
        this.formulaTrasformazioneQtaMagazzino = formulaTrasformazioneQtaMagazzino;
    }

    /**
     * @param giacenza
     *            The giacenza to set.
     */
    @Override
    public void setGiacenza(Giacenza giacenza) {
        this.giacenza = giacenza;
    }

    /**
     * @param ivata
     *            the ivata to set
     */
    public void setIvata(boolean ivata) {
        this.ivata = ivata;
    }

    /**
     * @param numeroDecimaliPrezzo
     *            the numeroDecimaliPrezzo to set
     */
    @Override
    public void setNumeroDecimaliPrezzo(Integer numeroDecimaliPrezzo) {
        this.numeroDecimaliPrezzo = numeroDecimaliPrezzo;
    }

    /**
     * @param numeroDecimaliQta
     *            the numeroDecimaliQta to set
     */
    @Override
    public void setNumeroDecimaliQta(Integer numeroDecimaliQta) {
        this.numeroDecimaliQta = numeroDecimaliQta;
    }

    /**
     * @param percProvvigione
     *            The percProvvigione to set.
     */
    public void setPercProvvigione(BigDecimal percProvvigione) {
        this.percProvvigione = percProvvigione;
    }

    /**
     * @param politicaPrezzo
     *            the politicaPrezzo to set
     */
    @Override
    public void setPoliticaPrezzo(PoliticaPrezzo politicaPrezzo) {
        this.politicaPrezzo = politicaPrezzo;
    }

    /**
     * @param prezzoDeterminato
     *            the prezzoDeterminato to set
     */
    @Override
    public void setPrezzoDeterminato(BigDecimal prezzoDeterminato) {
        this.prezzoDeterminato = prezzoDeterminato;
    }

    /**
     * @param prezzoNetto
     *            the prezzoNetto to set
     */
    public void setPrezzoNetto(Importo prezzoNetto) {
        // viene aggiornato con le set di prezzo unitario e variazioni
        // this.prezzoNetto = prezzoNetto;
    }

    /**
     * @param prezzoNettoImportazione
     *            the prezzoNettoImportazione to set
     */
    public void setPrezzoNettoImportazione(BigDecimal prezzoNettoImportazione) {
        this.prezzoNettoImportazione = prezzoNettoImportazione;
    }

    /**
     * @param prezzoTotale
     *            the prezzoTotale to set
     */
    public void setPrezzoTotale(Importo prezzoTotale) {
        // viene aggiornato con le set di prezzo unitario e variazioni
        // throw new UnsupportedOperationException();
    }

    /**
     * @param prezzoUnitarioNew
     *            the prezzoUnitario to set
     */
    @Override
    public void setPrezzoUnitario(Importo prezzoUnitarioNew) {
        logger.debug("--> Enter setPrezzoUnitario");
        this.prezzoUnitario = prezzoUnitarioNew;
        updateValoriImporto();
        logger.debug("--> Exit setPrezzoUnitario");
    }

    /**
     * @param prezzoUnitarioImportazione
     *            the prezzoUnitarioImportazione to set
     */
    public void setPrezzoUnitarioImportazione(BigDecimal prezzoUnitarioImportazione) {
        this.prezzoUnitarioImportazione = prezzoUnitarioImportazione;
    }

    /**
     * @param prezzoUnitarioImposta
     *            the prezzoUnitarioImposta to set
     */
    public void setPrezzoUnitarioImposta(Importo prezzoUnitarioImposta) {
        this.prezzoUnitarioImposta = prezzoUnitarioImposta;
    }

    /**
     *
     * @param prezzoUnitarioReale
     *            unitario reale
     */
    public void setPrezzoUnitarioReale(Importo prezzoUnitarioReale) {
        this.prezzoUnitarioReale = prezzoUnitarioReale;
        // if (!isIvata()) {
        setPrezzoUnitario(prezzoUnitarioReale.clone());
        updateImposta();
        // } else {
        // initPrezzoUnitarioReale(prezzoUnitarioReale);
        // updatePrezzoUnitarioReale();
        // }
    }

    /**
     * @param qta
     *            the Qta to set
     */
    @Override
    public void setQta(Double qta) {
        if (ObjectUtils.compare(this.qta, qta) == 0) {
            return;
        }
        if (qta == null) {
            qta = 0.0;
        }
        this.qta = qta;
        updateValoriImporto();
    }

    /**
     * @param qtaAttrezzaggio
     *            the qtaAttrezzaggio to set
     */
    public void setQtaAttrezzaggio(double qtaAttrezzaggio) {
        this.qtaAttrezzaggio = qtaAttrezzaggio;
    }

    /**
     * @param qtaEvasa
     *            the qtaEvasa to set
     */
    public void setQtaEvasa(double qtaEvasa) {
        this.qtaEvasa = qtaEvasa;
    }

    /**
     * @param qtaMagazzino
     *            the qtaMagazzino to set
     */
    @Override
    public void setQtaMagazzino(Double qtaMagazzino) {
        this.qtaMagazzino = qtaMagazzino;
    }

    /**
     * @param qtaMagazzinoTolleranza
     *            The qtaMagazzinoTolleranza to set.
     */
    public void setQtaMagazzinoTolleranza(Double qtaMagazzinoTolleranza) {
        this.qtaMagazzinoTolleranza = qtaMagazzinoTolleranza;
    }

    /**
     * @param qtaR
     *            The qtaR to set.
     */
    public void setQtaR(Double qtaR) {
        this.qtaR = qtaR;
    }

    @Override
    public void setResa(Double resa) {
        // Non utilizzato
    }

    /**
     * @param righeMagazzinoCollegate
     *            the righeMagazzinoCollegate to set
     */
    public void setRigheMagazzinoCollegate(List<RigaMagazzino> righeMagazzinoCollegate) {
        this.righeMagazzinoCollegate = righeMagazzinoCollegate;
    }

    /**
     * Prende le variazioni sullo sconto e le imposta sulle righe.
     *
     * @param sconto
     *            sconto da settare
     */
    public void setSconto(Sconto sconto) {
        if (sconto.getSconto1() != null) {
            setVariazione1(sconto.getSconto1());
        }
        if (sconto.getSconto2() != null) {
            setVariazione2(sconto.getSconto2());
        }
        if (sconto.getSconto3() != null) {
            setVariazione3(sconto.getSconto3());
        }
        if (sconto.getSconto4() != null) {
            setVariazione4(sconto.getSconto4());
        }
    }

    /**
     * @param sconto1Bloccato
     *            the sconto1Bloccato to set
     */
    public void setSconto1Bloccato(boolean sconto1Bloccato) {
        this.sconto1Bloccato = sconto1Bloccato;
    }

    @Override
    public void setSomministrazione(boolean somministrazione) {
        // Non utilizzato
    }

    @Override
    public void setStrategiaTotalizzazioneDocumento(StrategiaTotalizzazioneDocumento strategiaTotalizzazioneDocumento) {
        // non usato sulle righe ordine
    }

    /**
     * @param tipoOmaggio
     *            The tipoOmaggio to set.
     */
    @Override
    public void setTipoOmaggio(TipoOmaggio tipoOmaggio) {
        this.tipoOmaggio = tipoOmaggio;
        updateValoriImporto();
    }

    /**
     * @param unitaMisura
     *            the unitaMisura to set
     */
    @Override
    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    /**
     * @param unitaMisuraQtaMagazzino
     *            the unitaMisuraQtaMagazzino to set
     */
    @Override
    public void setUnitaMisuraQtaMagazzino(String unitaMisuraQtaMagazzino) {
        this.unitaMisuraQtaMagazzino = unitaMisuraQtaMagazzino;
    }

    /**
     * @param variazione1
     *            the variazione to set
     */
    @Override
    public void setVariazione1(BigDecimal variazione1) {
        if (variazione1 == null) {
            variazione1 = BigDecimal.ZERO;
        }
        if (variazione1.compareTo(this.variazione1) == 0) {
            return;
        }
        this.variazione1 = variazione1;
        updateValoriImporto();
    }

    /**
     * @param variazione1Importazione
     *            the variazione1Importazione to set
     */
    public void setVariazione1Importazione(BigDecimal variazione1Importazione) {
        this.variazione1Importazione = variazione1Importazione;
    }

    /**
     * @param variazione2
     *            the variazione2 to set
     */
    @Override
    public void setVariazione2(BigDecimal variazione2) {
        if (variazione2 == null) {
            variazione2 = BigDecimal.ZERO;
        }
        if (variazione2.compareTo(this.variazione2) == 0) {
            return;
        }
        this.variazione2 = variazione2;
        updateValoriImporto();
    }

    /**
     * @param variazione2Importazione
     *            the variazione2Importazione to set
     */
    public void setVariazione2Importazione(BigDecimal variazione2Importazione) {
        this.variazione2Importazione = variazione2Importazione;
    }

    /**
     * @param variazione3
     *            the variazione3 to set
     */
    @Override
    public void setVariazione3(BigDecimal variazione3) {
        if (variazione3 == null) {
            variazione3 = BigDecimal.ZERO;
        }
        if (variazione3.compareTo(this.variazione3) == 0) {
            return;
        }
        this.variazione3 = variazione3;
        updateValoriImporto();
    }

    /**
     * @param variazione3Importazione
     *            the variazione3Importazione to set
     */
    public void setVariazione3Importazione(BigDecimal variazione3Importazione) {
        this.variazione3Importazione = variazione3Importazione;
    }

    /**
     * @param variazione4
     *            the variazione4 to set
     */
    @Override
    public void setVariazione4(BigDecimal variazione4) {
        if (variazione4 == null) {
            variazione4 = BigDecimal.ZERO;
        }
        if (variazione4.compareTo(this.variazione4) == 0) {
            return;
        }
        this.variazione4 = variazione4;
        updateValoriImporto();
    }

    /**
     * @param variazione4Importazione
     *            the variazione4Importazione to set
     */
    public void setVariazione4Importazione(BigDecimal variazione4Importazione) {
        this.variazione4Importazione = variazione4Importazione;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     *
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuffer retValue = new StringBuffer();

        retValue.append("RigaArticolo[ ").append(super.toString()).append(" qta = ").append(this.qta)
                .append(" descrizione = ").append(this.descrizione).append(" descrizioneLingua = ")
                .append(this.descrizioneLingua).append(" formulaTrasformazione = ")
                .append(this.formulaTrasformazione != null ? this.formulaTrasformazione.getId() : null)
                .append(" prezzoUnitario = ").append(this.prezzoUnitario).append(" prezzoNetto = ")
                .append(this.prezzoNetto).append(" prezzoTotale = ").append(this.prezzoTotale).append(" variazione1 = ")
                .append(this.variazione1).append(" variazione2 = ").append(this.variazione2).append(" variazione3 = ")
                .append(this.variazione3).append(" variazione4 = ").append(this.variazione4)
                .append(" politicaPrezzo = ").append(this.politicaPrezzo != null ? this.politicaPrezzo : null)
                .append(" ]");

        return retValue.toString();
    }

    private void updateImposta() {
        BigDecimal imposta = BigDecimal.ZERO;

        if (getCodiceIva() != null) {
            imposta = prezzoUnitario.getImportoInValuta()
                    .multiply(getCodiceIva().getPercApplicazione().divide(new BigDecimal(100)))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        prezzoUnitarioImposta.setImportoInValuta(imposta);
        prezzoUnitarioImposta.calcolaImportoValutaAzienda(numeroDecimaliPrezzo);

        updatePrezzoUnitarioReale();
    }

    /**
     * aggiorna il prezzo unitario reale.
     */
    public void updatePrezzoUnitarioReale() {
        prezzoUnitarioReale.setImportoInValuta(prezzoUnitario.getImportoInValuta());
        prezzoUnitarioReale.setCodiceValuta(prezzoUnitario.getCodiceValuta());
        prezzoUnitarioReale.setTassoDiCambio(prezzoUnitario.getTassoDiCambio());
        // if (isIvata()) {
        // prezzoUnitarioReale.setImportoInValuta(prezzoUnitarioReale.getImportoInValuta().add(
        // prezzoUnitarioImposta.getImportoInValuta()));
        // }
        prezzoUnitarioReale.calcolaImportoValutaAzienda(numeroDecimaliPrezzo);
    }

    protected void updateValoriImporto() {
        updateValoriImporto(numeroDecimaliPrezzo);
    }

    /**
     * Sincronizza i valori di prezzoTotale e PrezzoNetto. Deve essere chiamato ogni volta che modifico un valore che
     * cambia il prezzoTotale e Netto (sconti o prezzo unitario).
     */
    private void updateValoriImporto(int numeroDecimaliImporti) {
        logger.debug("--> Enter updateValoriImporto " + prezzoUnitario);

        if (isOmaggio()) {
            // se il tipoOmaggio è altro omaggio, non devo impostare lo sconto a
            // 0
            variazione1 = !tipoOmaggio.equals(TipoOmaggio.ALTRO_OMAGGIO) ? Importo.HUNDRED.negate() : BigDecimal.ZERO;
            variazione2 = BigDecimal.ZERO;
            variazione3 = BigDecimal.ZERO;
            variazione4 = BigDecimal.ZERO;
        }

        // Copio valuta e cambio
        prezzoTotale.setCodiceValuta(prezzoUnitario.getCodiceValuta());
        prezzoTotale.setTassoDiCambio(prezzoUnitario.getTassoDiCambio());
        prezzoNetto.setCodiceValuta(prezzoUnitario.getCodiceValuta());
        prezzoNetto.setTassoDiCambio(prezzoUnitario.getTassoDiCambio());

        BigDecimal importoNettoValuta = getPrezzoUnitarioReale().getImportoInValuta();

        // Creo uno sconto "al volo" ed utilizzo il suo metodo per applicarlo
        Sconto sconto = new Sconto();
        sconto.setSconto1(variazione1);
        sconto.setSconto2(variazione2);
        sconto.setSconto3(variazione3);
        sconto.setSconto4(variazione4);
        importoNettoValuta = sconto.applica(importoNettoValuta, numeroDecimaliImporti);

        prezzoNetto.setImportoInValuta(importoNettoValuta);
        prezzoNetto.calcolaImportoValutaAzienda(numeroDecimaliImporti);

        Double qt = getQta();
        if (qt == null) {
            prezzoTotale = prezzoNetto.clone();
        } else {
            prezzoTotale.setImportoInValuta(prezzoNetto.getImportoInValuta().multiply(BigDecimal.valueOf(qt)));
            prezzoTotale.setImportoInValuta(
                    prezzoTotale.getImportoInValuta().setScale(SCALE_IMPORTO_TOTALE, BigDecimal.ROUND_HALF_UP));
            prezzoTotale.calcolaImportoValutaAzienda(SCALE_IMPORTO_TOTALE);
        }
        logger.debug("--> Exit updateValoriImporto");
    }

}