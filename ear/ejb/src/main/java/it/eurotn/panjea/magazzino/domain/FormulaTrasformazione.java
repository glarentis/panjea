package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.mrp.domain.Moltiplicatore;

/**
 * Contiene una stringa con la formula in JavaScript per trasformare il valore degli attributi di un articolo in una
 * quantità<BR>
 * Gli attributi nella formula devono essere inseriti con il loro codice attributo e delimitati con $.<br>
 * <b>Es</b>: $Peso$ <BR>
 * <br>
 * <b>NB.</b>Il tipo attributo può anche non essere associato ad un attributo sull'articolo. In questo caso alla riga
 * verrà aggiunto <br>
 * un attributo in scrittura
 */
@Entity
@Audited
@Table(name = "maga_formula_trasformazione")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "formulaTrasformazione")
public class FormulaTrasformazione extends EntityBase {

    private static final Logger LOGGER = Logger.getLogger(FormulaTrasformazione.class);

    private static final long serialVersionUID = -5701917827647895947L;

    @Column(nullable = false, unique = true)
    private String codice;

    @Column(nullable = false)
    private String formula;

    @Column(nullable = false, length = 10)
    private String codiceAzienda;

    /**
     * Calcola il valori della formula di trasformazione.
     *
     * @param moltiplicatore
     *            moltiplicatore esegue la formula
     * @param map
     *            definizione delle variabili
     * @param numeroDecimali
     *            decimali di calcolo
     * @param qta
     *            quantità base
     * @return risultato
     */
    public Object calcola(Moltiplicatore moltiplicatore, Map<String, Object> map, int numeroDecimali, Double qta) {
        BigDecimal result = null;
        try {
            LOGGER.debug("--> Enter calcola");
            result = moltiplicatore.calcola(formula, qta != null ? BigDecimal.valueOf(qta) : null, numeroDecimali, map);
        } catch (RuntimeException re) {
            throw re;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Exit calcola con risultato " + result);
        }
        return result;
    }

    /**
     * @return codice
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return codiceAzienda
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * Restituisce una lista di codici di {@link TipoAttributo} ricavati dalla Formula.
     *
     * @return CodiceTipiAttributi
     */
    public Set<String> getCodiceTipiAttributi() {
        return getCodiceTipiAttributi(true);
    }

    /**
     * Restituisce una lista di codici di {@link TipoAttributo} ricavati dalla Formula.
     *
     * @param includiCalcolati
     *            indica se includere o meno anche gli attributi che verranno calcolati e non sono quindi dell'articolo
     * @return CodiceTipiAttributi
     */
    public Set<String> getCodiceTipiAttributi(boolean includiCalcolati) {
        Set<String> tipoAttributi = new HashSet<String>();
        Pattern pattern = Pattern.compile(TipoAttributo.PATTERN_SEARCH);
        Matcher matcher = pattern.matcher(formula);
        String valoreSegnaposto = "";
        while (matcher.find()) {
            valoreSegnaposto = matcher.group();
            // Inserisco la chiave senza i delimitatori
            String codiceTipoAttributo = valoreSegnaposto.substring(1).substring(0, valoreSegnaposto.length() - 2);
            if (includiCalcolati) {
                tipoAttributi.add(codiceTipoAttributo);
            } else {
                if (!Arrays.asList(TipoAttributo.TIPIATTRIBUTOCALCOLATI).contains(codiceTipoAttributo)) {
                    tipoAttributi.add(codiceTipoAttributo);
                }
            }
        }
        return tipoAttributi;
    }

    /**
     * @return formula
     */
    public String getFormula() {
        return formula;
    }

    /**
     * @param codice
     *            tghe codice to set
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
    }

    /**
     * @param formula
     *            the formula to set
     */
    public void setFormula(String formula) {
        // se ricalcolo la formula devo ricrearmi l'engine script
        // per risettargli la nuova formula
        this.formula = formula;
    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome attributo = valore.
     *
     * @return a <code>String</code> come risultato di questo oggetto
     */
    @Override
    public String toString() {

        StringBuilder retValue = new StringBuilder();

        retValue.append("FormulaTrasformazione[ ").append(super.toString()).append(" codice = ").append(this.codice)
                .append(" formula = ").append(this.formula).append(" engine = ").append(" variabiliFormula = ")
                .append(" codiceAzienda = ").append(this.codiceAzienda).append(" ]");

        return retValue.toString();
    }

}
