package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.text.ParseException;

import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;

/**
 * Contiene le proprietà per un attributo. E' legato a {@link TipoAttributo} e le classi ereditate legano questo
 * attributo <br>
 * a diverse altre classi, ad esempio {@link Articolo} o {@link RigaMagazzino}
 *
 * @see AttributoArticolo
 * @author giangi
 */
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
@Audited
public abstract class AttributoMagazzino extends EntityBase {

    private static final Logger LOGGER = Logger.getLogger(AttributoMagazzino.class);

    private static final long serialVersionUID = 3216917897170995274L;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TipoAttributo tipoAttributo;

    private Boolean ricalcolaInEvasione;

    private String valore;

    @ManyToOne(fetch = FetchType.EAGER)
    private FormulaTrasformazione formula;

    private Boolean inserimentoInRiga;

    private Integer riga;

    private Integer ordine;

    private Boolean stampa;

    private Boolean obbligatorio;

    private Boolean updatable;

    {
        this.updatable = true;
        this.ricalcolaInEvasione = true;
        this.obbligatorio = true;
        this.stampa = false;
        this.inserimentoInRiga = false;
    }

    /**
     * Costruttore.
     */
    public AttributoMagazzino() {
        super();
    }

    /**
     * @return the formula
     */
    public FormulaTrasformazione getFormula() {
        return formula;
    }

    /**
     * @return inserimento in riga
     */
    public Boolean getInserimentoInRiga() {
        return inserimentoInRiga;
    }

    /**
     * @return Returns the obbligatorio.
     */
    public Boolean getObbligatorio() {
        return obbligatorio;
    }

    /**
     * @return the ordine
     */
    public Integer getOrdine() {
        return ordine;
    }

    /**
     * @return ricalcolaInEvasione
     */
    public Boolean getRicalcolaInEvasione() {
        return ricalcolaInEvasione;
    }

    /**
     * @return the riga
     */
    public Integer getRiga() {
        return riga;
    }

    /**
     * @return stampa
     */
    public Boolean getStampa() {
        return stampa;
    }

    /**
     * @return tipoAttributo
     */
    public TipoAttributo getTipoAttributo() {
        return tipoAttributo;
    }

    /**
     * @return Returns the updatable.
     */
    public Boolean getUpdatable() {
        return ObjectUtils.defaultIfNull(updatable, true);
    }

    /**
     * @return the valore
     */
    public String getValore() {
        return valore;
    }

    /**
     * Restituisce il valore tipizzato dell'attributo.
     *
     * @param returnType
     *            tipo richiesto
     * @param <T>
     *            T
     * @return valore tipizzato
     */
    @SuppressWarnings("unchecked")
    public <T> T getValoreTipizzato(Class<T> returnType) {

        if (getValore() == null) {
            return null;
        }

        T result = null;

        if (String.class.equals(returnType)) {
            result = returnType.cast(getValore());
        } else if (Boolean.class.equals(returnType)) {
            result = (T) new Boolean(getValore());
        } else {
            DefaultNumberFormatterFactory formatter = new DefaultNumberFormatterFactory("#,##0",
                    getTipoAttributo().getNumeroDecimali(), BigDecimal.class, true);
            try {
                BigDecimal bigDecimalVal = (BigDecimal) formatter.getDefaultFormatter().stringToValue(getValore());

                if (BigDecimal.class.equals(returnType)) {
                    result = returnType.cast(bigDecimalVal);
                } else if (Double.class.equals(returnType)) {
                    result = (T) new Double(bigDecimalVal.doubleValue());
                } else if (Integer.class.equals(returnType)) {
                    result = (T) new Integer(bigDecimalVal.intValue());
                } else {
                    throw new IllegalArgumentException("Tipo dato " + returnType.getName() + " non gestito.");
                }
            } catch (ParseException e) {
                LOGGER.error("-->errore nel parser del valore " + getValore() + " per attributo "
                        + getTipoAttributo().getCodice(), e);
            }
        }
        return result;
    }

    /**
     * @param formula
     *            the formula to set
     */
    public void setFormula(FormulaTrasformazione formula) {
        this.formula = formula;
    }

    /**
     * @param inserimentoInRiga
     *            the inserimentoInRiga to set
     */
    public void setInserimentoInRiga(Boolean inserimentoInRiga) {
        this.inserimentoInRiga = inserimentoInRiga;
    }

    /**
     * @param obbligatorio
     *            The obbligatorio to set.
     */
    public void setObbligatorio(Boolean obbligatorio) {
        this.obbligatorio = obbligatorio;
    }

    /**
     * @param ordine
     *            the ordine to set
     */
    public void setOrdine(Integer ordine) {
        this.ordine = ordine;
    }

    /**
     * @param ricalcolaInEvasione
     *            the inserimentoInRiga to set
     */
    public void setRicalcolaInEvasione(Boolean ricalcolaInEvasione) {
        this.ricalcolaInEvasione = ricalcolaInEvasione;
    }

    /**
     * @param riga
     *            the riga to set
     */
    public void setRiga(Integer riga) {
        this.riga = riga;
    }

    /**
     * @param stampa
     *            the stampa to set
     */
    public void setStampa(Boolean stampa) {
        this.stampa = stampa;
    }

    /**
     * @param tipoAttributo
     *            the tipoAttributo to set
     * @uml.property name="tipoAttributo"
     */
    public void setTipoAttributo(TipoAttributo tipoAttributo) {
        this.tipoAttributo = tipoAttributo;
    }

    /**
     * @param updatable
     *            The updatable to set.
     */
    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    /**
     * @param valore
     *            the decimal valore to set
     */
    public void setValore(BigDecimal valore) {
        DefaultNumberFormatterFactory formatter = new DefaultNumberFormatterFactory("#,##0",
                getTipoAttributo().getNumeroDecimali(), BigDecimal.class, true);
        try {
            this.valore = formatter.getDisplayFormatter().valueToString(valore);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param valore
     *            the boolean valore to set
     */
    public void setValore(Boolean valore) {
        this.valore = Boolean.toString(valore);
    }

    /**
     * @param valore
     *            the string valore to set
     */
    public void setValore(String valore) {
        this.valore = valore;
    }
}