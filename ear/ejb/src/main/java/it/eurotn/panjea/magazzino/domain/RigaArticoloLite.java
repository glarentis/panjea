package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.rulesvalidation.AbstractRigaArticoloRulesValidation;

/**
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_righe_magazzino")
@Where(clause = "TIPO_RIGA='A'")
public class RigaArticoloLite extends EntityBase {

    private static final long serialVersionUID = -3529435578873734648L;

    /**
     * @uml.property name="areaMagazzino"
     * @uml.associationEnd
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private AreaMagazzinoLite areaMagazzino;

    /**
     * @uml.property name="tipoRiga"
     */
    @Column(name = "TIPO_RIGA", length = 2, nullable = false)
    private String tipoRiga;

    /**
     * @uml.property name="qta"
     */
    @Column(precision = 10, scale = 6, nullable = true)
    private Double qta;

    /**
     * @uml.property name="qtaMagazzino"
     */
    @Column(nullable = true)
    private Double qtaMagazzino;

    /**
     * @uml.property name="articolo"
     * @uml.associationEnd
     */
    @ManyToOne
    private ArticoloLite articolo;

    /**
     * @uml.property name="descrizione"
     */
    // In lingua aziendale. Copiata dall'articolo
    private String descrizione;

    /**
     * @uml.property name="prezzoUnitario"
     * @uml.associationEnd
     */
    @Embedded
    private Importo prezzoUnitario;

    /**
     * @uml.property name="prezzoNetto"
     * @uml.associationEnd
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaNetto", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaNetto", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioNetto", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaNetto", length = 3) ) })
    private Importo prezzoNetto;

    /**
     * @uml.property name="prezzoTotale"
     * @uml.associationEnd
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaTotale", precision = 19, scale = 6) ),
            @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaAziendaTotale", precision = 19, scale = 6) ),
            @AttributeOverride(name = "tassoDiCambio", column = @Column(name = "tassoDiCambioTotale", precision = 12, scale = 6) ),
            @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaTotale", length = 3) ) })
    private Importo prezzoTotale;

    /**
     * @uml.property name="prezzoDeterminato"
     */
    @Column(precision = 19, scale = 6)
    private BigDecimal prezzoDeterminato;

    @ManyToOne
    private AgenteLite agente;

    /**
     * Contiene tutti gli eventuali errori di validazione dell'area magazzino.
     *
     * @uml.property name="rulesValidationError"
     * @uml.associationEnd
     */
    @Transient
    private RulesValidationErrors rulesValidationError;

    /**
     * Costruttore di default.
     */
    public RigaArticoloLite() {
        super();
        initialize();
    }

    /**
     * Aggiunge un errore di validazione.
     *
     * @param rule
     *            errore di validazione
     */
    public void addToRulesValidationError(AbstractRigaArticoloRulesValidation rule) {
        rulesValidationError.addToRules(rule);
    }

    /**
     * @return Returns the agente.
     */
    public AgenteLite getAgente() {
        return agente;
    }

    /**
     * @return areaMagazzino
     * @uml.property name="areaMagazzino"
     */
    public AreaMagazzinoLite getAreaMagazzino() {
        return areaMagazzino;
    }

    /**
     * @return articolo
     * @uml.property name="articolo"
     */
    public ArticoloLite getArticolo() {
        return articolo;
    }

    /**
     * @return the descrizione
     * @uml.property name="descrizione"
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return the prezzoDeterminato
     * @uml.property name="prezzoDeterminato"
     */
    public BigDecimal getPrezzoDeterminato() {
        return prezzoDeterminato;
    }

    /**
     * @return prezzoNetto
     * @uml.property name="prezzoNetto"
     */
    public Importo getPrezzoNetto() {
        return prezzoNetto;
    }

    /**
     * @return prezzoTotale
     * @uml.property name="prezzoTotale"
     */
    public Importo getPrezzoTotale() {
        return prezzoTotale;
    }

    /**
     * @return prezzoUnitario
     * @uml.property name="prezzoUnitario"
     */
    public Importo getPrezzoUnitario() {
        return prezzoUnitario;
    }

    /**
     * @return the qta
     * @uml.property name="qta"
     */
    public Double getQta() {
        return qta;
    }

    /**
     * @return the qtaMagazzino
     * @uml.property name="qtaMagazzino"
     */
    public Double getQtaMagazzino() {
        return qtaMagazzino;
    }

    /**
     * @return rulesValidationError
     * @uml.property name="rulesValidationError"
     */
    public RulesValidationErrors getRulesValidationError() {
        return rulesValidationError;
    }

    /**
     * @return tipoRiga
     * @uml.property name="tipoRiga"
     */
    public String getTipoRiga() {
        return tipoRiga;
    }

    /**
     * Inizializza i valori di default.
     */
    private void initialize() {
        this.rulesValidationError = new RulesValidationErrors();
    }

    /**
     * @param agente
     *            The agente to set.
     */
    public void setAgente(AgenteLite agente) {
        this.agente = agente;
    }

    /**
     * @param areaMagazzino
     *            the areaMagazzino to set
     * @uml.property name="areaMagazzino"
     */
    public void setAreaMagazzino(AreaMagazzinoLite areaMagazzino) {
        this.areaMagazzino = areaMagazzino;
    }

    /**
     * @param articolo
     *            the articolo to set
     * @uml.property name="articolo"
     */
    public void setArticolo(ArticoloLite articolo) {
        this.articolo = articolo;
    }

    /**
     * @param descrizione
     *            the descrizione to set
     * @uml.property name="descrizione"
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @param prezzoDeterminato
     *            the prezzoDeterminato to set
     * @uml.property name="prezzoDeterminato"
     */
    public void setPrezzoDeterminato(BigDecimal prezzoDeterminato) {
        this.prezzoDeterminato = prezzoDeterminato;
    }

    /**
     * @param prezzoNetto
     *            the prezzoNetto to set
     * @uml.property name="prezzoNetto"
     */
    public void setPrezzoNetto(Importo prezzoNetto) {
        this.prezzoNetto = prezzoNetto;
    }

    /**
     * @param prezzoTotale
     *            the prezzoTotale to set
     * @uml.property name="prezzoTotale"
     */
    public void setPrezzoTotale(Importo prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    /**
     * @param prezzoUnitario
     *            the prezzoUnitario to set
     * @uml.property name="prezzoUnitario"
     */
    public void setPrezzoUnitario(Importo prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    /**
     * @param qta
     *            the qta to set
     */
    public void setQta(double qta) {
        this.qta = qta;
    }

    /**
     * @param qtaMagazzino
     *            the qtaMagazzino to set
     * @uml.property name="qtaMagazzino"
     */
    public void setQtaMagazzino(Double qtaMagazzino) {
        this.qtaMagazzino = qtaMagazzino;
    }

    /**
     * @param tipoRiga
     *            the tipoRiga to set
     * @uml.property name="tipoRiga"
     */
    public void setTipoRiga(String tipoRiga) {
        this.tipoRiga = tipoRiga;
    }

}
