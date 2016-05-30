package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.domain.Importo;

/**
 * Rappresenta gli sconti che possono essere applicati. Possono essere applicati fino ad un massino di 4 sconti e la
 * classe offre un metodo per calcolare il prezzo scontato in base a tutti gli sconti settati.<br>
 * <br>
 * Lo sconto risulta essere valido solo se tutti gli sconti precedenti all'ultimo settato risultano avvalorati (Esempio:
 * se sconto3 è avvalorato, lo devono essere anche sconto1 e sconto2). Il metodo <code>isValid</code> verifica se questa
 * condizione è rispettata.
 *
 * @author fattazzo
 */
@Entity
@Audited
@Table(name = "maga_sconti", uniqueConstraints = @UniqueConstraint(columnNames = { "codiceAzienda", "sconto1",
        "sconto2", "sconto3", "sconto4" }) )
@NamedQueries({
        @NamedQuery(name = "Sconto.caricaAll", query = "from Sconto s where s.codiceAzienda = :paramCodiceAzienda") })
public class Sconto extends EntityBase {

    private static final long serialVersionUID = -2154963004268051779L;

    /**
     * @uml.property name="codiceAzienda"
     */
    @Column(length = 10, nullable = false)
    @Index(name = "index_codiceAzienda")
    private String codiceAzienda = null;

    /**
     * @uml.property name="codice"
     */
    @Column(length = 10)
    private String codice;

    /**
     * @uml.property name="descrizione"
     */
    @Column(length = 30)
    private String descrizione;

    /**
     * @uml.property name="sconto1"
     */
    @Column(precision = 5, scale = 2)
    private BigDecimal sconto1;

    /**
     * @uml.property name="sconto2"
     */
    @Column(precision = 5, scale = 2)
    private BigDecimal sconto2;

    /**
     * @uml.property name="sconto3"
     */
    @Column(precision = 5, scale = 2)
    private BigDecimal sconto3;

    /**
     * @uml.property name="sconto4"
     */
    @Column(precision = 5, scale = 2)
    private BigDecimal sconto4;

    /**
     * Costruttore.
     *
     */
    public Sconto() {
        sconto1 = BigDecimal.ZERO;
        sconto2 = BigDecimal.ZERO;
        sconto3 = BigDecimal.ZERO;
        sconto4 = BigDecimal.ZERO;
    }

    /**
     *
     * Costruttore.
     *
     * @param sconto1
     *            sconto 1 da applicare
     * @param sconto2
     *            sconto 1 da applicare
     * @param sconto3
     *            sconto 1 da applicare
     * @param sconto4
     *            sconto 1 da applicare
     */
    public Sconto(final BigDecimal sconto1, final BigDecimal sconto2, final BigDecimal sconto3,
            final BigDecimal sconto4) {
        this.sconto1 = sconto1;
        this.sconto2 = sconto2;
        this.sconto3 = sconto3;
        this.sconto4 = sconto4;
    }

    /**
     * aggiunge una variazione in coda alle variazioni già presenti.
     *
     * @param variazione
     *            variazione da aggiungere
     * @param sconto1Bloccato
     *            indica se lo sconto1 è bloccato oppure no
     */
    public void aggiungiInCoda(BigDecimal variazione, Boolean sconto1Bloccato) {
        if (!sconto1Bloccato && BigDecimal.ZERO.compareTo(sconto1) == 0) {
            sconto1 = variazione;
        } else if (BigDecimal.ZERO.compareTo(sconto2) == 0) {
            sconto2 = variazione;
        } else if (BigDecimal.ZERO.compareTo(sconto3) == 0) {
            sconto3 = variazione;
        } else if (BigDecimal.ZERO.compareTo(sconto4) == 0) {
            sconto4 = variazione;
        }
    }

    /**
     * Calcola il prezzo scontato usando tutti gli sconti settati.
     *
     * @param prezzo
     *            Prezzo originale
     * @param numeroDecimali
     *            numero decimali al quale arrotondare
     * @return Prezzo scontato.
     */
    public BigDecimal applica(BigDecimal prezzo, int numeroDecimali) {

        if (prezzo == null) {
            return BigDecimal.ZERO;
        }

        prezzo = prezzo.setScale(numeroDecimali, BigDecimal.ROUND_HALF_UP);

        // sconto1
        if (sconto1 != null && BigDecimal.ZERO.compareTo(sconto1) != 0) {
            prezzo = prezzo.add(sconto1.divide(Importo.HUNDRED).multiply(prezzo));
            prezzo = prezzo.setScale(numeroDecimali, BigDecimal.ROUND_HALF_UP);
        }

        // sconto2
        if (sconto2 != null && BigDecimal.ZERO.compareTo(sconto2) != 0) {
            prezzo = prezzo.add(sconto2.divide(Importo.HUNDRED).multiply(prezzo));
            prezzo = prezzo.setScale(numeroDecimali, BigDecimal.ROUND_HALF_UP);
        }

        // sconto3
        if (sconto3 != null && BigDecimal.ZERO.compareTo(sconto3) != 0) {
            prezzo = prezzo.add(sconto3.divide(Importo.HUNDRED).multiply(prezzo));
            prezzo = prezzo.setScale(numeroDecimali, BigDecimal.ROUND_HALF_UP);
        }

        // sconto4
        if (sconto4 != null && BigDecimal.ZERO.compareTo(sconto4) != 0) {
            prezzo = prezzo.add(sconto4.divide(Importo.HUNDRED).multiply(prezzo));
            prezzo = prezzo.setScale(numeroDecimali, BigDecimal.ROUND_HALF_UP);
        }

        return prezzo;
    }

    /**
     * @return codice
     * @uml.property name="codice"
     */
    public String getCodice() {
        return codice;
    }

    /**
     * @return codiceAzienda
     * @uml.property name="codiceAzienda"
     */
    public String getCodiceAzienda() {
        return codiceAzienda;
    }

    /**
     * @return descrizione
     * @uml.property name="descrizione"
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @return sconto1
     * @uml.property name="sconto1"
     */
    public BigDecimal getSconto1() {
        return sconto1;
    }

    /**
     * @return sconto2
     * @uml.property name="sconto2"
     */
    public BigDecimal getSconto2() {
        return sconto2;
    }

    /**
     * @return sconto3
     * @uml.property name="sconto3"
     */
    public BigDecimal getSconto3() {
        return sconto3;
    }

    /**
     * @return sconto4
     * @uml.property name="sconto4"
     */
    public BigDecimal getSconto4() {
        return sconto4;
    }

    /**
     * Indica se sono presenti degli sconti.
     *
     * @return <code>true</code> se tutti gli sconti sono nulli o uguali a 0
     */
    public boolean isEmpty() {

        BigDecimal sconto1tmp = ObjectUtils.defaultIfNull(sconto1, BigDecimal.ZERO);
        BigDecimal sconto2tmp = ObjectUtils.defaultIfNull(sconto2, BigDecimal.ZERO);
        BigDecimal sconto3tmp = ObjectUtils.defaultIfNull(sconto3, BigDecimal.ZERO);
        BigDecimal sconto4tmp = ObjectUtils.defaultIfNull(sconto4, BigDecimal.ZERO);

        return sconto1tmp.compareTo(BigDecimal.ZERO) == 0 && sconto2tmp.compareTo(BigDecimal.ZERO) == 0
                && sconto3tmp.compareTo(BigDecimal.ZERO) == 0 && sconto4tmp.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Verifica che gli sconti siano stati inseriti in modo corretto e che quindi quelli precedenti all'ultimo
     * avvalorato siano settati.
     *
     * @return <code>true</code> se lo sconto risulta valido, <code>false</code> altrimenti
     */
    public boolean isValid() {

        if ((sconto4 != null && sconto4.compareTo(BigDecimal.ZERO) != 0)
                && (sconto3 == null || sconto3.compareTo(BigDecimal.ZERO) == 0)) {
            return false;
        }

        if ((sconto3 != null && sconto3.compareTo(BigDecimal.ZERO) != 0)
                && (sconto2 == null || sconto2.compareTo(BigDecimal.ZERO) == 0)) {
            return false;
        }

        if ((sconto2 != null && sconto2.compareTo(BigDecimal.ZERO) != 0)
                && (sconto1 == null || sconto1.compareTo(BigDecimal.ZERO) == 0)) {
            return false;
        }

        return true;
    }

    /**
     * @param codice
     *            the codice to set
     * @uml.property name="codice"
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /**
     * @param codiceAzienda
     *            the codiceAzienda to set
     * @uml.property name="codiceAzienda"
     */
    public void setCodiceAzienda(String codiceAzienda) {
        this.codiceAzienda = codiceAzienda;
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
     * @param sconto1
     *            the sconto1 to set
     * @uml.property name="sconto1"
     */
    public void setSconto1(BigDecimal sconto1) {
        this.sconto1 = ObjectUtils.defaultIfNull(sconto1, BigDecimal.ZERO);
    }

    /**
     * @param sconto2
     *            the sconto2 to set
     * @uml.property name="sconto2"
     */
    public void setSconto2(BigDecimal sconto2) {
        this.sconto2 = ObjectUtils.defaultIfNull(sconto2, BigDecimal.ZERO);
    }

    /**
     * @param sconto3
     *            the sconto3 to set
     * @uml.property name="sconto3"
     */
    public void setSconto3(BigDecimal sconto3) {
        this.sconto3 = ObjectUtils.defaultIfNull(sconto3, BigDecimal.ZERO);
    }

    /**
     * @param sconto4
     *            the sconto4 to set
     * @uml.property name="sconto4"
     */
    public void setSconto4(BigDecimal sconto4) {
        this.sconto4 = ObjectUtils.defaultIfNull(sconto4, BigDecimal.ZERO);
    }

    /**
     * Sostituisce la variazione a quelle presenti.
     *
     * @param variazione
     *            variazione da aggiungere
     * @param sconto1Bloccato
     *            indica se lo sconto1 è bloccato oppure no
     */
    public void sostituisciSconti(BigDecimal variazione, Boolean sconto1Bloccato) {

        if (!sconto1Bloccato) {
            sconto1 = BigDecimal.ZERO;
        }
        sconto2 = BigDecimal.ZERO;
        sconto3 = BigDecimal.ZERO;
        sconto4 = BigDecimal.ZERO;

        aggiungiInCoda(variazione, sconto1Bloccato);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Sconto[");
        buffer.append("codice = ").append(codice);
        buffer.append("]");
        return buffer.toString();
    }
}
