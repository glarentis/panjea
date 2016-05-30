package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
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
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.conai.domain.RigaConaiComponente;
import it.eurotn.panjea.lotti.domain.RigaLotto;
import it.eurotn.panjea.magazzino.domain.Articolo.TipoLotto;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.RisultatoPrezzo;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.util.RigaArticoloDTO;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * Riga contenuta in un area di magazzino. <br>
 * La riga è associata ad un articolo ed ha i valori per poter definire un prezzo finale.
 *
 * @author giangi
 */
@Entity
@Audited
@DiscriminatorValue("A")
@NamedQueries({
        @NamedQuery(name = "RigaArticolo.caricaImponibiliIva", query = " select sum(riga.prezzoTotale.importoInValuta), sum(riga.prezzoTotale.importoInValutaAzienda), riga.codiceIva from RigaArticolo riga where riga.areaMagazzino = :paramArea group by riga.codiceIva having sum(riga.prezzoTotale.importoInValutaAzienda) <> 0"),
        @NamedQuery(name = "RigaArticolo.caricaByTipo", query = " select sum(riga.prezzoTotale.importoInValuta), sum(riga.prezzoTotale.importoInValutaAzienda), riga.articolo.tipoArticolo from RigaArticolo riga where riga.areaMagazzino = :paramArea group by riga.articolo.tipoArticolo"),
        @NamedQuery(name = "RigaArticolo.caricaImportiByCategoriaContabileArticolo", query = "select cat ,sum(riga.prezzoTotale.importoInValutaAzienda) from RigaArticolo riga left join riga.categoriaContabileArticolo cat where riga.areaMagazzino.id = :paramIdAreaMagazzino group by cat") })
public class RigaArticolo extends RigaMagazzino implements IRigaArticoloDocumento {

    private static final Logger LOGGER = Logger.getLogger(RigaArticolo.class);

    private static final long serialVersionUID = -1102284459781308936L;
    public static final int SCALE_IMPORTO_TOTALE = 2;

    private Integer numeroDecimaliPrezzo;

    private Integer idConfigurazioneDistinta;

    private Double resa;

    private Double battute;

    @Transient
    private String codiceEntita;

    @Transient
    private String noteOmaggiPerStampa;

    /**
     * Quantità di vendita/acquisto. Su questa quantità si basa il prezzo totale
     */
    @Column(precision = 10, scale = 6, nullable = true)
    private Double qta;

    /**
     * Quantità che movimenta il magazzino. Tramite la formulaTrasformazioneQtaMagazzino viene trasformata la qta in
     * qtaMagazzino
     */
    @Column(nullable = true)
    private Double qtaMagazzino;

    @Transient
    private double qtaAttrezzaggio;

    @Transient
    private double qtaMagazzinoChiusa;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private CategoriaContabileArticolo categoriaContabileArticolo;

    @Column(length = 255)
    private String descrizione; // In lingua aziendale. Copiata dall'articolo

    @Column(length = 255)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    private FormulaTrasformazione formulaTrasformazioneQtaMagazzino;

    @ManyToOne
    private ArticoloLite articolo;

    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private CodiceIva codiceIva;

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "rigaArticolo")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("riga,ordine,tipoAttributo")
    private List<AttributoRiga> attributi;

    @Embedded
    private Importo prezzoUnitario;

    @Transient
    private Importo prezzoUnitarioReale;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaUnitarioImposta", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaUnitarioImposta", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioUnitarioImposta", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaUnitarioImposta", length = 3) ) })
    private Importo prezzoUnitarioImposta;

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

    /**
     * Rappresenta il prezzo determinato con la politica calcolo prezzo in valuta azienda.
     */
    @Column(precision = 19, scale = 6)
    private BigDecimal prezzoDeterminato;

    @Column(precision = 19, scale = 6)
    private BigDecimal prezzoUnitarioBaseProvvigionale;

    @Column(precision = 19, scale = 6)
    private BigDecimal prezzoNettoBaseProvvigionale;

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

    @Column(precision = 5, scale = 2)
    private BigDecimal percProvvigione;

    private Integer numeroDecimaliQta;

    @Transient
    private PoliticaPrezzo politicaPrezzo;

    /**
     * Indica il moltiplicatore della quantità impostato da una evbasione ordine.
     */
    private double moltQtaOrdine;

    /**
     * Indica la quantità della riga che è stata inserita nei documenti di chiusura.chr.
     */
    @Transient
    private double qtaChiusa;

    @ManyToOne
    private AgenteLite agente;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "rigaArticolo")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<RigaLotto> righeLotto;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "rigaArticolo")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy(value = "materiale tipoImballo")
    private Set<RigaConaiComponente> righeConaiComponente;

    @Formula("(select count(rcc.id) from maga_conai_righe_componente rcc where rcc.rigaArticolo_id=id)")
    @NotAudited
    private int numeroRigheConaiComponente;

    @Enumerated
    private TipoOmaggio tipoOmaggio;

    private boolean sconto1Bloccato;

    private boolean ivata;

    private StrategiaTotalizzazioneDocumento strategiaTotalizzazioneDocumento;

    @Transient
    private Giacenza giacenza;

    {
        this.articoloLibero = false;
        variazione1 = BigDecimal.ZERO;
        variazione2 = BigDecimal.ZERO;
        variazione3 = BigDecimal.ZERO;
        variazione4 = BigDecimal.ZERO;
        prezzoUnitario = new Importo();
        prezzoUnitarioImposta = new Importo();
        prezzoNetto = new Importo();
        prezzoTotale = new Importo();
        prezzoDeterminato = BigDecimal.ZERO;
        numeroDecimaliPrezzo = 6;
        qta = 0.0;
        qtaAttrezzaggio = 0.0;
        qtaMagazzino = 0.0;
        moltQtaOrdine = 1;
        righeLotto = new HashSet<RigaLotto>();
        righeConaiComponente = new HashSet<RigaConaiComponente>();
        sconto1Bloccato = Boolean.FALSE;
        tipoOmaggio = TipoOmaggio.NESSUNO;
        ivata = Boolean.FALSE;

        prezzoUnitarioReale = new Importo();

        strategiaTotalizzazioneDocumento = StrategiaTotalizzazioneDocumento.NORMALE;
    }

    /**
     * Prima di salvare una riga nuova associa la riga agli attributi.<br>
     * Nota: @PrePersist che era applicato in precedenza a questo metodo, veniva chiamato, ma le collection su cui
     * voglio lavorare risultano essere vuote, chiamo quindi questo metodo prima di salvare la riga magazzino
     */
    public void aggiornaCollections() {
        for (AttributoRiga attributoRiga : getAttributi()) {
            attributoRiga.setRigaArticolo(this);
        }
        if (righeConaiComponente != null) {
            for (RigaConaiComponente rigaConaiComponente : righeConaiComponente) {
                rigaConaiComponente.setRigaArticolo(this);
                rigaConaiComponente.calcolaEsenzione();
            }
        }
    }

    /**
     * @param importoSconto
     *            sconto da aggiornare
     */
    public void aggiornaScontoCommerciale(BigDecimal importoSconto) {

        // in caso di riga omaggio non applico un eventuale sconto commerciale
        if (isOmaggio()) {
            return;
        }

        setSconto1Bloccato(Boolean.FALSE);
        if (importoSconto == null || BigDecimal.ZERO.compareTo(importoSconto) == 0) {
            setVariazione1(getVariazione2());
            setVariazione2(getVariazione3());
            setVariazione3(getVariazione4());
            setVariazione4(BigDecimal.ZERO);
        } else {
            setVariazione4(getVariazione3());
            setVariazione3(getVariazione2());
            setVariazione2(getVariazione1());
            setVariazione1(importoSconto);
            setSconto1Bloccato(Boolean.TRUE);
        }
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

        setIvata(politicaPrezzo.isPrezzoIvato());
        if (!politicaPrezzo.getPrezzi().isEmpty()) {
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
        if (!politicaPrezzo.getSconti().isEmpty()) {
            RisultatoPrezzo<Sconto> risultatoSconto = politicaPrezzo.getSconti().getRisultatoPrezzo(qtaRiga);
            if (risultatoSconto != null) {
                setVariazione1(risultatoSconto.getValue().getSconto1());
                setVariazione2(risultatoSconto.getValue().getSconto2());
                setVariazione3(risultatoSconto.getValue().getSconto3());
                setVariazione4(risultatoSconto.getValue().getSconto4());
                setSconto1Bloccato(politicaPrezzo.isSconto1Bloccato());
            } else {
                setVariazione1(BigDecimal.ZERO);
                setVariazione2(BigDecimal.ZERO);
                setVariazione3(BigDecimal.ZERO);
                setVariazione4(BigDecimal.ZERO);
                setSconto1Bloccato(politicaPrezzo.isSconto1Bloccato());
            }
        }
        if (!politicaPrezzo.getProvvigioni().isEmpty()) {
            RisultatoPrezzo<BigDecimal> risultatoProvvigioni = politicaPrezzo.getProvvigioni()
                    .getRisultatoPrezzo(qtaRiga);
            if (risultatoProvvigioni != null) {
                setPercProvvigione(risultatoProvvigioni.getValue());
            }
        }

        // Se ho il totalizzatore che forza il numero decimali uso quello
        updateValoriImporto();
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
                attributoRigaClone.setRigaArticolo(rigaArticoloClone);
                listAttributiClone.add(attributoRigaClone);

            }
            rigaArticoloClone.setAttributi(listAttributiClone);
        } catch (CloneNotSupportedException e) {
            LOGGER.debug("--> Eccezione non considerata.");
        }
        return rigaArticoloClone;
    }

    @Override
    public AttributoRigaArticolo creaAttributoRiga(AttributoArticolo attributoArticolo) {
        AttributoRiga attributoRiga = new AttributoRiga();
        attributoRiga.setUpdatable(attributoArticolo.getUpdatable());
        attributoRiga.setStampa(false);
        attributoRiga.setTipoAttributo(attributoArticolo.getTipoAttributo());
        attributoRiga.setValore(attributoArticolo.getValore());
        attributoRiga.setFormula(attributoArticolo.getFormula());
        attributoRiga.setRiga(attributoArticolo.getRiga());
        attributoRiga.setOrdine(attributoArticolo.getOrdine());
        attributoRiga.setStampa(attributoArticolo.getStampa());
        attributoRiga.setObbligatorio(attributoArticolo.getObbligatorio());
        attributoRiga.setRicalcolaInEvasione(attributoArticolo.getRicalcolaInEvasione());
        attributoRiga.setRigaArticolo(this);
        return attributoRiga;
    }

    @Override
    protected RigaMagazzinoDTO creaIstanzaRigaMagazzinoDTO() {
        RigaArticoloDTO rigaArticoloDTO = new RigaArticoloDTO();
        if (getArticolo() != null) {
            rigaArticoloDTO.setCodice(getArticolo().getCodice());
            rigaArticoloDTO.setIdArticolo(getArticolo().getId());
            rigaArticoloDTO.setDescrizioneArticolo(getArticolo().getDescrizione());
            rigaArticoloDTO.setDescrizione(getArticolo().getDescrizione());
            rigaArticoloDTO.setCodiceEntita(getArticolo().getCodiceEntita());
        }
        Sconto sconto = new Sconto(getVariazione1(), getVariazione2(), getVariazione3(), getVariazione4());
        rigaArticoloDTO.setSconto(sconto);
        return rigaArticoloDTO;
    }

    @Override
    public RigaMagazzino creaRigaCollegata(AreaMagazzino areaMagazzino, double ordinamentoRigaCollagata) {
        LOGGER.debug("--> Enter creaRigaCollegata");

        RigaArticolo rigaArticoloCollegata = this.clone();
        rigaArticoloCollegata.setAreaOrdineCollegata(null);
        rigaArticoloCollegata.setAreaMagazzino(areaMagazzino);
        rigaArticoloCollegata.setAreaMagazzinoCollegata(this.getAreaMagazzino());
        rigaArticoloCollegata.setLivello(this.getLivello() + 1);
        rigaArticoloCollegata.setOrdinamento(ordinamentoRigaCollagata);
        rigaArticoloCollegata.setRigaMagazzinoCollegata(this);
        rigaArticoloCollegata.setRigaAutomatica(false);

        // metto la riga ordine collegata a null altrimenti verrebbe collegata
        // anche al documento di destinazione
        rigaArticoloCollegata.setRigaOrdineCollegata(null);

        // setto alla riga collegata solo la quantità della riga originale che
        // deve ancora essere chiusa.
        rigaArticoloCollegata.setQta(this.getQta() - this.getQtaChiusa());
        rigaArticoloCollegata.setQtaMagazzino(this.getQtaMagazzino() - this.getQtaMagazzinoChiusa());

        if (getNumeroRigheConaiComponente() > 0 && this.righeConaiComponente != null
                && areaMagazzino.getTipoAreaMagazzino().isGestioneConai()) {
            Set<RigaConaiComponente> righe = new HashSet<RigaConaiComponente>();
            for (RigaConaiComponente rigaConaiComponente : righeConaiComponente) {
                RigaConaiComponente rigaClonata = (RigaConaiComponente) rigaConaiComponente.clone();
                rigaClonata.setRigaArticolo(rigaArticoloCollegata);
                righe.add(rigaClonata);
            }
            rigaArticoloCollegata.setRigheConaiComponente(righe);
        }

        LOGGER.debug("--> Exit creaRigaCollegata");

        return rigaArticoloCollegata;
    }

    /**
     * @return the agente
     */
    public AgenteLite getAgente() {
        return agente;
    }

    @Override
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return attributi dell'articolo
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
        StringBuilder sb = new StringBuilder();
        String separator = "";
        for (AttributoRiga attributoRiga : getAttributi()) {
            if (attributoRiga.visualizzaInStampa()) {
                sb.append(separator);
                sb.append(attributoRiga.getDescrizione(lingua));
                separator = " - ";
            }
        }
        return sb.toString();
    }

    /**
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
     * @return Returns the battute.
     */
    public Double getBattute() {
        return battute;
    }

    /**
     * @return the categoriaContabileArticolo
     */
    @Override
    public CategoriaContabileArticolo getCategoriaContabileArticolo() {
        return categoriaContabileArticolo;
    }

    /**
     * @return Returns the codiceEntita.
     */
    public String getCodiceEntita() {
        return codiceEntita;
    }

    /**
     * @return the codiceIva
     */
    @Override
    public CodiceIva getCodiceIva() {
        return codiceIva;
    }

    @Override
    public Set<IRigaArticoloDocumento> getComponenti() {
        return null;
    }

    /**
     * Restituisce la descrizione delle righe conai legate alla riga articolo.
     *
     * @param lingua
     *            lingua da utilizzare per generare la descrizione
     * @return descrizione generata
     */
    private String getConaiDescrizione(String lingua) {

        DecimalFormat format = new DecimalFormat("###,###,###,##0.000");
        StringBuilder sb = new StringBuilder();

        List<RigaConaiComponente> righeConai = new ArrayList<RigaConaiComponente>();
        if (righeConaiComponente != null) {
            righeConai.addAll(righeConaiComponente);
        }
        for (RigaConaiComponente rigaConai : righeConai) {
            if (sb.length() > 0) {
                sb.append("<br>");
            }
            sb.append("<i>");
            sb.append("&nbsp&nbsp&nbsp Contrib. CONAI ");
            sb.append(rigaConai.getMateriale().getDescrizione());
            sb.append(" ");
            sb.append(format.format(rigaConai.getPesoTotale()));
            sb.append("t, Es.:");
            sb.append(rigaConai.getPercentualeEsenzione());
            sb.append("% ");
            sb.append("</i>");
        }
        return sb.toString();
    }

    /**
     * @return the descrizione
     */
    @Override
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the descrizioneLingua
     */
    @Override
    public String getDescrizioneLingua() {
        return descrizioneLingua;
    }

    /**
     * @return the descrizionePoliticaPrezzo
     */
    @Override
    public DescrizionePoliticaPrezzo getDescrizionePoliticaPrezzo() {
        return descrizionePoliticaPrezzo;
    }

    @Override
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua,
            boolean escludiTagHtml) {

        String linguaEntita = (areaMagazzino.getDocumento().getSedeEntita() != null)
                ? areaMagazzino.getDocumento().getSedeEntita().getLingua() : null;

        StringBuilder stringBuffer = new StringBuilder();
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
        if (stampaAttributi && attributi != null && !attributi.isEmpty()) {
            String attrs = getAttributiDescrizione(lingua);
            if (attrs.length() > 0) {
                stringBuffer.append("<br>").append(attrs);
            }
        }
        if (noteOmaggiPerStampa != null) {
            stringBuffer.append("<br>").append(noteOmaggiPerStampa);
        }
        stringBuffer.append("</html>");

        String descrizioneRiga = stringBuffer.toString();
        if (escludiTagHtml) {
            descrizioneRiga = PanjeaEJBUtil.removeHtml(descrizioneRiga);
        }

        return descrizioneRiga;
    }

    @Override
    public String getDescrizioneRiga(boolean stampaAttributi, boolean stampaNote, String lingua, boolean escludiTagHtml,
            boolean stampaConai) {
        // Genero la descrizione della riga
        String descRiga = getDescrizioneRiga(stampaAttributi, stampaNote, lingua, escludiTagHtml);

        // tolgo il tag di chiusura html se esiste
        descRiga = descRiga.substring(0, descRiga.length() - 7);

        StringBuilder sb = new StringBuilder();
        sb.append(descRiga);

        // aggiungo il dettaglio del conai
        if (stampaConai && righeConaiComponente != null && !righeConaiComponente.isEmpty()) {
            String descConai = getConaiDescrizione(lingua);
            if (!descConai.isEmpty()) {
                sb.append("<br>").append(descConai);
            }
        }

        sb.append("</html>");

        // rimuovo i tag html se devo
        String descrizioneRiga = sb.toString();
        if (escludiTagHtml) {
            descrizioneRiga = PanjeaEJBUtil.removeHtml(descrizioneRiga);
        }

        return descrizioneRiga;
    }

    /**
     * @return the formulaConversioneUnitaMisura
     */
    @Override
    public String getFormulaConversioneUnitaMisura() {
        return formulaConversioneUnitaMisura;
    }

    /**
     * @return the formulaTrasformazione
     */
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

    @Override
    public Giacenza getGiacenza() {
        if (giacenza == null) {
            giacenza = new Giacenza(0.0, 0.0);
        }
        return giacenza;
    }

    /**
     * @return Returns the idConfigurazioneDistinta.
     */
    public Integer getIdConfigurazioneDistinta() {
        return idConfigurazioneDistinta;
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

    /**
     * @return the moltQtaOrdine
     */
    public double getMoltQtaOrdine() {
        return moltQtaOrdine;
    }

    /**
     * @return the noteOmaggiPerStampa
     */
    public String getNoteOmaggiPerStampa() {
        return noteOmaggiPerStampa;
    }

    @Override
    public String getNotePerStampa(String lingua) {
        String linguaEntita = (areaMagazzino.getDocumento().getSedeEntita() != null)
                ? areaMagazzino.getDocumento().getSedeEntita().getLingua() : null;

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

    /**
     * @return the numeroDecimaliPrezzo
     */
    @Override
    public Integer getNumeroDecimaliPrezzo() {
        return numeroDecimaliPrezzo;
    }

    /**
     * @return the numeroDecimaliQta
     */
    @Override
    public Integer getNumeroDecimaliQta() {
        return numeroDecimaliQta;
    }

    /**
     * @return the numeroRigheConaiComponente
     */
    public int getNumeroRigheConaiComponente() {
        return numeroRigheConaiComponente;
    }

    /**
     * @return Returns the percProvvigione.
     */
    @Override
    public BigDecimal getPercProvvigione() {
        return percProvvigione;
    }

    /**
     * @return the politicaPrezzo
     */
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

    /**
     * Calcola il prezzo netto ivato.
     *
     * @return prezzo ivato
     */
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

    /**
     * Calcola il prezzo netto ivato in valuta azienda.
     *
     * @return prezzo ivato
     */
    @Override
    public BigDecimal getPrezzoIvatoInValutaAzienda() {
        // Devo ricalcolare i valori con il numero decimale a 2
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

    /**
     * @return the prezzoNetto
     */
    @Override
    public Importo getPrezzoNetto() {
        return prezzoNetto;
    }

    /**
     * @return the prezzoNettoBaseProvvigionale
     */
    public BigDecimal getPrezzoNettoBaseProvvigionale() {
        return prezzoNettoBaseProvvigionale;
    }

    /**
     * @return the prezzoTotale
     */
    @Override
    public Importo getPrezzoTotale() {
        return prezzoTotale;
    }

    /**
     * @return the prezzoUnitario
     */
    @Override
    public Importo getPrezzoUnitario() {
        return prezzoUnitario;
    }

    /**
     * @return the prezzoUnitarioBaseProvvigionale
     */
    public BigDecimal getPrezzoUnitarioBaseProvvigionale() {
        return prezzoUnitarioBaseProvvigionale;
    }

    /**
     * @return the prezzoUnitarioImposta
     */
    public Importo getPrezzoUnitarioImposta() {
        return prezzoUnitarioImposta;
    }

    /**
     * @return the prezzoUnitarioReale
     */
    public Importo getPrezzoUnitarioReale() {
        updatePrezzoUnitarioReale();

        return prezzoUnitarioReale;
    }

    /**
     * @return the qta
     */
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
     * @return the qtaChiusa
     */
    public double getQtaChiusa() {
        return qtaChiusa;
    }

    /**
     * @return the qtaMagazzino
     */
    @Override
    public Double getQtaMagazzino() {
        return qtaMagazzino;
    }

    /**
     * @return the qtaagazzinoChiusa
     */
    public double getQtaMagazzinoChiusa() {
        return qtaMagazzinoChiusa;
    }

    /**
     * @return quantità sulle righe lotto
     */
    public double getQtaRigheLotto() {
        Integer numDecQta = 6;
        if (getNumeroDecimaliQta() != null) {
            numDecQta = getNumeroDecimaliQta();
        }

        BigDecimal qtaLotti = BigDecimal.ZERO;
        if (areaMagazzino.getDocumento().getTipoDocumento().isGestioneLotti()) {
            for (RigaLotto rigaLotto : righeLotto) {
                qtaLotti = qtaLotti
                        .add(BigDecimal.valueOf(rigaLotto.getQuantita()).setScale(numDecQta, RoundingMode.HALF_UP));
            }
        }

        return qtaLotti.doubleValue();
    }

    /**
     * @return Returns the resa.
     */
    public Double getResa() {
        return resa;
    }

    /**
     *
     * @param codiceLotto
     *            codice del lotto
     * @param dataScadenza
     *            dataScadenza
     * @return riga lotto con il lotto ricercato. Null se non trovo nulla
     */
    public RigaLotto getRigaLotto(String codiceLotto, Date dataScadenza) {
        for (RigaLotto rigaLotto : righeLotto) {
            if (rigaLotto.getLotto().getCodice().equals(codiceLotto)
                    && rigaLotto.getLotto().getDataScadenza().equals(dataScadenza)) {
                return rigaLotto;
            }
        }
        return null;
    }

    /**
     * @return the righeConaiArticolo
     */
    public Set<RigaConaiComponente> getRigheConaiComponente() {
        return righeConaiComponente;
    }

    /**
     * @return the righeLotto
     */
    public Set<RigaLotto> getRigheLotto() {
        return righeLotto;
    }

    /**
     * @return the strategiaTotalizzazioneDocumento
     */
    public StrategiaTotalizzazioneDocumento getStrategiaTotalizzazioneDocumento() {
        return strategiaTotalizzazioneDocumento;
    }

    /**
     * @return the tipoOmaggio
     */
    @Override
    public TipoOmaggio getTipoOmaggio() {
        return tipoOmaggio;
    }

    /**
     * @return the unitaMisura
     */
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

    /**
     *
     * @param codiceAttributo
     *            codice dell.attributo.
     * @return valore dell'attributo
     */
    @Override
    public String getValoreAttributo(String codiceAttributo) {
        return getValoreAttributoTipizzato(codiceAttributo, String.class);
    }

    /**
     * @param codiceAttributo
     *            il codice dell'attributo da recuperare
     * @param returnType
     *            tipo richiesto
     * @param <T>
     *            tipo
     * @return the valore tipizzato
     */
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

    /**
     * @return the variazione1
     */
    @Override
    public BigDecimal getVariazione1() {
        return variazione1;
    }

    /**
     * @return the variazione2
     */
    @Override
    public BigDecimal getVariazione2() {
        return variazione2;
    }

    /**
     * @return the variazione3
     */
    @Override
    public BigDecimal getVariazione3() {
        return variazione3;
    }

    /**
     * @return the variazione4
     */
    @Override
    public BigDecimal getVariazione4() {
        return variazione4;
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
     * @return the articoloLibero
     */
    @Override
    public boolean isArticoloLibero() {
        return articoloLibero;
    }

    /**
     * Indica se per la riga articolo è prevista l'assegnazione automatica delle righe lotto.
     *
     * @param gestioneLottiAutomatici
     *            flag di gestione lotti automatici
     * @return <code>true</code> se l'assegnazione lotti deve essere automatica
     */
    public boolean isAssegnazioneLottiAutomaticaAbilitata(boolean gestioneLottiAutomatici) {

        TipoLotto tipoLottoArticolo = TipoLotto.NESSUNO;
        if (this.articolo != null && this.articolo.getId() != null) {
            tipoLottoArticolo = this.articolo.getTipoLotto();
        }

        boolean gestioneLottiTipoDocumento = false;
        TipoMovimento tipoMovimento = null;
        if (this.areaMagazzino != null && this.areaMagazzino.getTipoAreaMagazzino() != null
                && this.areaMagazzino.getTipoAreaMagazzino().getTipoMovimento() != null) {
            tipoMovimento = this.areaMagazzino.getTipoAreaMagazzino().getTipoMovimento();
            gestioneLottiTipoDocumento = this.areaMagazzino.getDocumento().getTipoDocumento().isGestioneLotti();
        }

        return gestioneLottiAutomatici && tipoLottoArticolo != TipoLotto.NESSUNO && gestioneLottiTipoDocumento
                && tipoMovimento != null && tipoMovimento == TipoMovimento.SCARICO;
    }

    /**
     * Indica se la somma delle quantità delle righe collegate è uguale alla quantità di riga.
     *
     * @return <code>true</code> se la somma delle righe collegate è uguale alla quantità di riga, <code>false</code> se
     *         la somma è minore.
     */
    public boolean isChiusa() {
        boolean chiusa = false;
        if (qta != null) {
            chiusa = qta > 0.0 && (qta - qtaChiusa == 0.0);
        }
        return chiusa;
    }

    /**
     * @return the ivata
     */
    public boolean isIvata() {
        return ivata;
    }

    /**
     * Verifica che la somma delle quantità delle {@link RigaLotto} sia uguale alla quantità presente sulla
     * {@link RigaArticolo}.
     *
     * @return <code>true</code> se le quantità sono uguali, <code>false</code> altrimenti
     */
    public boolean isLottiValid() {

        Integer numDecQta = 6;

        // check di inizio metodo
        if (articolo == null) {
            return true;
        }

        if (articolo.getTipoLotto() == null) {
            return true;
        }

        if (articolo.getTipoLotto() == TipoLotto.NESSUNO || this.qta == null) {
            return true;
        }

        if (!areaMagazzino.getDocumento().getTipoDocumento().isGestioneLotti()) {
            return true;
        }

        if (areaMagazzino.getTipoAreaMagazzino().getTipoMovimento() == TipoMovimento.NESSUNO) {
            return true;
        }

        if (articolo.isLottoFacoltativo() && righeLotto.isEmpty()
                && areaMagazzino.getTipoAreaMagazzino().getTipoMovimento() == TipoMovimento.SCARICO) {
            return true;
        }

        BigDecimal qtaLotti = BigDecimal.ZERO;
        BigDecimal qtaRiga = BigDecimal.valueOf(this.qta);
        if (getNumeroDecimaliQta() != null) {
            numDecQta = getNumeroDecimaliQta();
        }

        qtaRiga = qtaRiga.setScale(numDecQta, RoundingMode.HALF_UP);

        for (RigaLotto rigaLotto : righeLotto) {
            qtaLotti = qtaLotti
                    .add(BigDecimal.valueOf(rigaLotto.getQuantita()).setScale(numDecQta, RoundingMode.HALF_UP));
        }

        return (qtaRiga.compareTo(qtaLotti) == 0);
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
     * Lo stesso controllo è riproposto per necessità di creazione delle rules del client nella seguente class:<br>
     * it.eurotn.panjea.magazzino.rich.rules. ClasseTipoDocumentoQtaRigaArticoloConstraint<br>
     * aggiornare il check in caso di modifiche.
     *
     * @return false se classeTipoDocumento diversa da ddt e qta = 0, true altrimenti
     */
    public boolean isQtaValid() {
        boolean isQtaZeroPermessa = getAreaMagazzino() != null && getAreaMagazzino().getTipoAreaMagazzino() != null
                && getAreaMagazzino().getTipoAreaMagazzino().isQtaZeroPermessa();

        boolean isQtaZeroArticoloPermessa = articolo != null && articolo.isGestioneQuantitaZero();

        return !(getQta() != null && getQta() == 0 && !isQtaZeroArticoloPermessa && !isQtaZeroPermessa);
    }

    /**
     * @return the sconto1Bloccato
     */
    @Override
    public boolean isSconto1Bloccato() {
        return sconto1Bloccato;
    }

    /**
     * @param agente
     *            the agente to set
     */
    public void setAgente(AgenteLite agente) {
        this.agente = agente;
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
     * @param attributi
     *            the attributi to set
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setAttributi(List<? extends AttributoRigaArticolo> attributi) {
        this.attributi = (List<AttributoRiga>) attributi;
    }

    /**
     * @param battute
     *            The battute to set.
     */
    public void setBattute(Double battute) {
        this.battute = battute;
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
     * @param codiceEntita
     *            The codiceEntita to set.
     */
    public void setCodiceEntita(String codiceEntita) {
        this.codiceEntita = codiceEntita;
    }

    /**
     * @param codiceIva
     *            the codiceIva to set
     */
    @Override
    public void setCodiceIva(CodiceIva codiceIva) {
        this.codiceIva = codiceIva;
        updateImposta();
    }

    @Override
    public void setComponenti(Set<IRigaArticoloDocumento> componenti) {
        // non gestisco componenti
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

    @Override
    public void setGiacenza(Giacenza giacenza) {
        this.giacenza = giacenza;
    }

    /**
     * @param idConfigurazioneDistinta
     *            The idConfigurazioneDistinta to set.
     */
    public void setIdConfigurazioneDistinta(Integer idConfigurazioneDistinta) {
        this.idConfigurazioneDistinta = idConfigurazioneDistinta;
    }

    /**
     * @param ivata
     *            the ivata to set
     */
    public void setIvata(boolean ivata) {
        this.ivata = ivata;
    }

    /**
     * @param moltQtaOrdine
     *            the moltQtaOrdine to set
     */
    public void setMoltQtaOrdine(double moltQtaOrdine) {
        this.moltQtaOrdine = moltQtaOrdine;
    }

    /**
     * @param noteOmaggiPerStampa
     *            the noteOmaggiPerStampa to set
     */
    public void setNoteOmaggiPerStampa(String noteOmaggiPerStampa) {
        this.noteOmaggiPerStampa = noteOmaggiPerStampa;
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
     * @param numeroRigheConaiComponente
     *            the numeroRigheConaiComponente to set
     */
    public void setNumeroRigheConaiComponente(int numeroRigheConaiComponente) {
        this.numeroRigheConaiComponente = numeroRigheConaiComponente;
    }

    /**
     * @param percProvvigione
     *            The percProvvigione to set.
     */
    @Override
    public void setPercProvvigione(BigDecimal percProvvigione) {

        BigDecimal percTmp = percProvvigione;
        if (percProvvigione != null && BigDecimal.ZERO.compareTo(percProvvigione) > 0) {
            percTmp = BigDecimal.ZERO;
        }
        this.percProvvigione = percTmp;
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
    }

    /**
     * @param prezzoNettoBaseProvvigionale
     *            the prezzoNettoBaseProvvigionale to set
     */
    public void setPrezzoNettoBaseProvvigionale(BigDecimal prezzoNettoBaseProvvigionale) {
        this.prezzoNettoBaseProvvigionale = prezzoNettoBaseProvvigionale;
    }

    /**
     * @param prezzoTotale
     *            the prezzoTotale to set
     */
    public void setPrezzoTotale(Importo prezzoTotale) {
        // viene aggiornato con le set di prezzo unitario e variazioni
    }

    /**
     * @param prezzoUnitarioNew
     *            the prezzoUnitario to set
     */
    @Override
    public void setPrezzoUnitario(Importo prezzoUnitarioNew) {
        LOGGER.debug("--> Enter setPrezzoUnitario");
        this.prezzoUnitario = prezzoUnitarioNew;
        updateValoriImporto();
        LOGGER.debug("--> Exit setPrezzoUnitario");
    }

    /**
     * @param prezzoUnitarioBaseProvvigionale
     *            the prezzoUnitarioBaseProvvigionale to set
     */
    public void setPrezzoUnitarioBaseProvvigionale(BigDecimal prezzoUnitarioBaseProvvigionale) {
        this.prezzoUnitarioBaseProvvigionale = prezzoUnitarioBaseProvvigionale;
    }

    /**
     * @param prezzoUnitarioImposta
     *            the prezzoUnitarioImposta to set
     */
    public void setPrezzoUnitarioImposta(Importo prezzoUnitarioImposta) {
        this.prezzoUnitarioImposta = prezzoUnitarioImposta;
    }

    /**
     * @param prezzoUnitarioReale
     *            the prezzoUnitarioReale to set
     */
    public void setPrezzoUnitarioReale(Importo prezzoUnitarioReale) {
        this.prezzoUnitarioReale = prezzoUnitarioReale;
        if (!isIvata() || getStrategiaTotalizzazioneDocumento() == StrategiaTotalizzazioneDocumento.NORMALE) {
            setPrezzoUnitario(prezzoUnitarioReale.clone());
            updateImposta();
        } else {
            initPrezzoUnitarioReale(prezzoUnitarioReale);
            updatePrezzoUnitarioReale();
        }
    }

    /**
     * @param qta
     *            the Qta to set
     */
    @Override
    public void setQta(Double qta) {
        // se sono su un carico valore la quantità deve rimanere a null quindi
        // non devo cambiarla
        if (this.qta == null || this.qta.equals(qta)) {
            return;
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
     * @param qtaChiusa
     *            the qtaChiusa to set
     */
    public void setQtaChiusa(double qtaChiusa) {
        this.qtaChiusa = qtaChiusa;
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
     * @param qtaMagazzinoChiusa
     *            the qtaMagazzinoChiusa to set
     */
    public void setQtaMagazzinoChiusa(double qtaMagazzinoChiusa) {
        this.qtaMagazzinoChiusa = qtaMagazzinoChiusa;
    }

    /**
     * @param resa
     *            The resa to set.
     */
    public void setResa(Double resa) {
        this.resa = resa;
    }

    /**
     * @param righeConaiComponente
     *            the righeConaiArticolo to set
     */
    public void setRigheConaiComponente(Set<RigaConaiComponente> righeConaiComponente) {
        this.righeConaiComponente = righeConaiComponente;
    }

    /**
     * @param righeLotto
     *            the righeLotto to set
     */
    public void setRigheLotto(Set<RigaLotto> righeLotto) {
        this.righeLotto = righeLotto;
    }

    /**
     * @param sconto1Bloccato
     *            the sconto1Bloccato to set
     */
    public void setSconto1Bloccato(boolean sconto1Bloccato) {
        this.sconto1Bloccato = sconto1Bloccato;
    }

    /**
     * @param strategiaTotalizzazioneDocumento
     *            the strategiaTotalizzazioneDocumento to set
     */
    @Override
    public void setStrategiaTotalizzazioneDocumento(StrategiaTotalizzazioneDocumento strategiaTotalizzazioneDocumento) {
        this.strategiaTotalizzazioneDocumento = strategiaTotalizzazioneDocumento;
    }

    /**
     * @param tipoOmaggio
     *            the tipoOmaggio to set
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

        BigDecimal var = ObjectUtils.defaultIfNull(variazione1, BigDecimal.ZERO);
        if (var.compareTo(this.variazione1) == 0) {
            return;
        }
        this.variazione1 = var;
        updateValoriImporto();
    }

    /**
     * @param variazione2
     *            the variazione2 to set
     */
    @Override
    public void setVariazione2(BigDecimal variazione2) {

        BigDecimal var = ObjectUtils.defaultIfNull(variazione2, BigDecimal.ZERO);
        if (var.compareTo(this.variazione2) == 0) {
            return;
        }
        this.variazione2 = var;
        updateValoriImporto();
    }

    /**
     * @param variazione3
     *            the variazione3 to set
     */
    @Override
    public void setVariazione3(BigDecimal variazione3) {

        BigDecimal var = ObjectUtils.defaultIfNull(variazione3, BigDecimal.ZERO);
        if (var.compareTo(this.variazione3) == 0) {
            return;
        }
        this.variazione3 = var;
        updateValoriImporto();
    }

    /**
     * @param variazione4
     *            the variazione4 to set
     */
    @Override
    public void setVariazione4(BigDecimal variazione4) {

        BigDecimal var = ObjectUtils.defaultIfNull(variazione4, BigDecimal.ZERO);
        if (var.compareTo(this.variazione4) == 0) {
            return;
        }
        this.variazione4 = var;
        updateValoriImporto();
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     *
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {
        StringBuilder retValue = new StringBuilder();
        retValue.append("RigaArticolo[ ").append(super.toString()).append("articolo=")
                .append(articolo != null ? articolo.getCodice() : "").append(" qta = ").append(this.qta)
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

    /**
     * Aggiorna il valore delle battute in base alla resa e quantità della riga
     */
    public void updateBattute() {
        BigDecimal qtaBigDecimal = BigDecimal.valueOf(ObjectUtils.defaultIfNull(getQta(), 0.0));
        BigDecimal resaBigDecimal = BigDecimal.valueOf(ObjectUtils.defaultIfNull(getResa(), 0.0));
        battute = qtaBigDecimal.multiply(resaBigDecimal).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
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
        if (isIvata() && getStrategiaTotalizzazioneDocumento() == StrategiaTotalizzazioneDocumento.SCONTRINO) {
            prezzoUnitarioReale.setImportoInValuta(
                    prezzoUnitarioReale.getImportoInValuta().add(prezzoUnitarioImposta.getImportoInValuta()));
        }
        prezzoUnitarioReale.calcolaImportoValutaAzienda(numeroDecimaliPrezzo);
    }

    /**
     * @see RigaArticolo#updateValoriImporto(int) numeroDecimali vengono presi dalla variabile numeroDecimali
     */
    protected void updateValoriImporto() {
        updateValoriImporto(numeroDecimaliPrezzo);
    }

    /**
     * Sincronizza i valori di prezzoTotale e PrezzoNetto. Deve essere chiamato ogni volta che modifico un valore che
     * cambia il prezzoTotale e Netto (sconti o prezzo unitario).
     *
     * @param numeroDecimaliImporti
     *            numero decimali ai quali arrotondare gli importi.
     */
    protected void updateValoriImporto(int numeroDecimaliImporti) {
        LOGGER.debug("--> Enter updateValoriImporto " + prezzoUnitario);

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

        // Creo uno sconto "al volo" ed utilizzo il suo metodo per applicarlo
        Sconto sconto = new Sconto();
        sconto.setSconto1(variazione1);
        sconto.setSconto2(variazione2);
        sconto.setSconto3(variazione3);
        sconto.setSconto4(variazione4);

        BigDecimal importoNettoValuta = getPrezzoUnitarioReale().getImportoInValuta();
        importoNettoValuta = sconto.applica(importoNettoValuta, numeroDecimaliImporti);

        prezzoNetto.setImportoInValuta(importoNettoValuta);
        prezzoNetto.calcolaImportoValutaAzienda(numeroDecimaliImporti);
        LOGGER.debug("--> nuovo prezzoNetto " + prezzoNetto);

        Double qt = getQta();
        if (qt == null) {
            prezzoTotale = prezzoNetto.clone();
        } else {
            prezzoTotale.setImportoInValuta(prezzoNetto.getImportoInValuta().multiply(BigDecimal.valueOf(qt)));
            prezzoTotale.setImportoInValuta(
                    prezzoTotale.getImportoInValuta().setScale(SCALE_IMPORTO_TOTALE, BigDecimal.ROUND_HALF_UP));
            prezzoTotale.calcolaImportoValutaAzienda(SCALE_IMPORTO_TOTALE);
        }

        updateBattute();

        LOGGER.debug("--> nuovo prezzoTotale " + prezzoTotale);
        LOGGER.debug("--> Exit updateValoriImporto");
    }
}