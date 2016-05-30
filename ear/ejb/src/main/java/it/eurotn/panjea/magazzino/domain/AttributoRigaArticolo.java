package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;

@Audited
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AttributoRigaArticolo extends EntityBase {

    private static final Logger LOGGER = Logger.getLogger(AttributoRigaArticolo.class);

    private static final long serialVersionUID = -1679819203700017924L;

    private String valore;

    private boolean ricalcolaInEvasione;

    private boolean updatable;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TipoAttributo tipoAttributo;

    private Boolean obbligatorio;

    @ManyToOne(fetch = FetchType.EAGER)
    private FormulaTrasformazione formula;

    private Integer riga;

    private Integer ordine;

    private boolean stampa;

    {
        ricalcolaInEvasione = false;
    }

    /**
     * Costruttore.
     */
    public AttributoRigaArticolo() {
        super();
    }

    /**
     * @param lingua
     *            lingua per la quale creare la descrizione.
     * @return descrizione in linuga dell'attributo della riga
     */
    public String getDescrizione(String lingua) {
        StringBuilder sb = new StringBuilder();
        sb.append(tipoAttributo.getDescrizione(lingua));

        switch (tipoAttributo.getTipoDato()) {
        case STRINGA:
            sb.append(":");
            sb.append(valore);
            break;
        case BOOLEANO:
            // non faccio nulla perchè per il booleano se è false non stampo l'attributo
            // altrimenti stampo solamente la descrizione del tipo attributo
            break;
        case NUMERICO:
            sb.append(":");
            DefaultNumberFormatterFactory formatter = new DefaultNumberFormatterFactory("#,##0",
                    tipoAttributo.getNumeroDecimali(), Double.class, true);
            try {
                if (!StringUtils.isEmpty(valore)) {
                    Number valoreDecimale = new DecimalFormat().parse(valore);
                    sb.append(formatter.getDefaultFormatter().valueToString(valoreDecimale));
                }
            } catch (ParseException e) {
                LOGGER.error("-->errore nel formattare l'attributo " + this, e);
                sb.append("Errore nel formattare l'attributo");
            }
            break;
        default:
            throw new UnsupportedOperationException("tipoDato non supportato" + tipoAttributo.getTipoDato());
        }
        return sb.toString();
    }

    /**
     * @return the formula
     */
    public FormulaTrasformazione getFormula() {
        return formula;
    }

    /**
     * @return Returns the obbligatorio.
     */
    public Boolean getObbligatorio() {
        if (obbligatorio == null) {
            return false;
        }
        return obbligatorio;
    }

    /**
     * @return the ordine
     */
    public Integer getOrdine() {
        return ordine;
    }

    /**
     * @return the riga
     */
    public Integer getRiga() {
        return riga;
    }

    /**
     * @return the tipoAttributo
     */
    public TipoAttributo getTipoAttributo() {
        return tipoAttributo;
    }

    /**
     * @return the valore
     */
    public String getValore() {
        return valore;
    }

    /**
     * Restituisce il valore tipizzato dell'attributo in base al suo tipo.
     * 
     * @return valore tipizzato
     */
    public Object getValoreTipizzato() {
        return getValoreTipizzato(tipoAttributo.getTipoDato().getJavaType());
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

        if (getValore() == null || getValore().isEmpty()) {
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
                result = null;
            }
        }
        return result;
    }

    /**
     * @return Returns the ricalcolaInEvasione.
     */
    public boolean isRicalcolaInEvasione() {
        return ricalcolaInEvasione;
    }

    /**
     * @return the stampa
     */
    public boolean isStampa() {
        return stampa;
    }

    /**
     * @return the updatable
     */
    public boolean isUpdatable() {
        return updatable && formula == null;
    }

    /**
     * @param formula
     *            the formula to set
     */
    public void setFormula(FormulaTrasformazione formula) {
        this.formula = formula;
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
     *            The ricalcolaInEvasione to set.
     */
    public void setRicalcolaInEvasione(boolean ricalcolaInEvasione) {
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
    public void setStampa(boolean stampa) {
        this.stampa = stampa;
    }

    /**
     * @param tipoAttributo
     *            the tipoAttributo to set
     */
    public void setTipoAttributo(TipoAttributo tipoAttributo) {
        this.tipoAttributo = tipoAttributo;
    }

    /**
     * @param updatable
     *            the updatable to set
     */
    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    /**
     * @param valore
     *            the valore to set
     */
    public void setValore(String valore) {
        this.valore = valore;
    }

    /**
     * 
     * @return indica se un attributo va visualizzato in stampa.<br/>
     *         L'attributo può essere stampabile però se è un booleano a false allora non va visualizzato.
     */
    public boolean visualizzaInStampa() {
        boolean result = isStampa();
        if (tipoAttributo.getTipoDato() == ETipoDatoTipoAttributo.BOOLEANO) {
            Boolean valoreBoolean = new Boolean(valore);
            result = result && valoreBoolean;
        }
        return result;
    }
}
