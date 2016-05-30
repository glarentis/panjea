/**
 *
 */
package it.eurotn.panjea.anagrafica.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.log4j.Logger;

/**
 * Classe @Embeddable per la rappresentazione degli Importi. La classe che la utilizza dovrà essere
 * mappata come segue:
 *
 * @Embedded private Importo importo Se questa classe viene ripetuta più volte all'interno della
 *           stessa classe è necessario eseguire il mapping come segue:
 * @Embedded
 * @AttributeOverride(name = "importoInValuta", column = @Column(name = "importoInValutaUno",
 *                         precision=19, scale=6)),
 * @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name =
 *                         "importoInValutaAziendaUno", precision=19, scale=6)),
 * @AttributeOverride(name = "tassoDiCambio", column = @Column(name =
 *                         "tassoDiCambioUno",precision=12,scale=6)),
 * @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaUno", length=3))})
 *                         private Importo importoUno;
 * @Embedded
 * @AttributeOverrides({ @AttributeOverride(name = "importoInValuta", column = @Column(name =
 *                       "importoInValutaDue", precision=19, scale=6)),
 * @AttributeOverride(name = "importoInValutaAzienda", column = @Column(name = "importoInValutaDue",
 *                         precision=19, scale=6)),
 * @AttributeOverride(name = "tassoDiCambio", column = @Column(name =
 *                         "tassoDiCambioDue",precision=12,scale=6)),
 * @AttributeOverride(name = "codiceValuta", column = @Column(name = "codiceValutaDue", length=3))})
 *                         private Importo importoDue; E' necessario riportare all'interno degli
 *                         AttributeOverrides oltre al nome della colonna anche le sue specifiche
 *                         (scale per BigDecimal e length per String ) La mancata definizione di
 *                         questi attributi comporterà l'assegnazione dei valori di default: scale 2
 *                         e length 255
 * @author adriano
 * @version 1.0, 30/mag/08
 */
@Embeddable
public class Importo implements Serializable, Comparable<Importo>, Cloneable {

    /**
     * Indica se le operazioni sugli importi sono in valuta o valutaAzienda.
     *
     * @author giangi
     * @version 1.0, 22/ago/2011
     *
     */
    public enum TIPO_OPERAZIONE_VALUTA {
        VALUTA, AZIENDA
    }

    private static final long serialVersionUID = 6000769554676230891L;
    public static final String KEY_MSG_EUR_SYMBOL = "eurSymbol";

    public static final BigDecimal HUNDRED = new BigDecimal("100");

    private static Logger logger = Logger.getLogger(Importo.class);

    @Column(name = "importoInValuta", precision = 19, scale = 6)
    private BigDecimal importoInValuta;

    @Column(name = "importoInValutaAzienda", precision = 19, scale = 6)
    private BigDecimal importoInValutaAzienda;

    @Column(name = "tassoDiCambio", precision = 12, scale = 6)
    private BigDecimal tassoDiCambio;

    @Column(name = "codiceValuta", length = 3)
    private String codiceValuta;

    /**
     * Constructor.
     */
    public Importo() {
        super();
        this.initialize();
    }

    /**
     * costruttore che crea una nuova istanza di Importo inizializzandone gli attributi con
     * l'argomento importo.
     *
     * @param importo
     *            importo utilizzato per inizializzare i valori
     */
    public Importo(final Importo importo) {
        this();
        this.codiceValuta = importo.getCodiceValuta();
        this.tassoDiCambio = importo.getTassoDiCambio();
        this.importoInValuta = importo.getImportoInValuta();
        this.importoInValutaAzienda = importo.getImportoInValutaAzienda();
    }

    /**
     * @param codiceValuta
     *            codice valuta
     * @param tassoDiCambio
     *            tasso di cambio
     */
    public Importo(final String codiceValuta, final BigDecimal tassoDiCambio) {
        this();
        this.codiceValuta = codiceValuta;
        this.tassoDiCambio = tassoDiCambio;
    }

    /**
     * @param codiceValuta
     *            codice valuta
     * @param tassoDiCambio
     *            tasso di cambio
     * @param valore
     *            valore da settare come importo in valuta. Viene trasformato e settato subito il
     *            valore in valuta azienda
     */
    public Importo(final String codiceValuta, final BigDecimal tassoDiCambio, BigDecimal valore,
            int scale) {
        this();
        this.codiceValuta = codiceValuta;
        this.tassoDiCambio = tassoDiCambio;
        setImportoInValuta(valore);
        calcolaImportoValutaAzienda(scale);
    }

    /**
     * @return nuova istanza di importo dove il valore dei nuovi attributi è uguale al abs del
     *         valore degli attributi attuali
     */
    public Importo abs() {
        Importo importoResult = new Importo(this.codiceValuta, this.tassoDiCambio);
        importoResult.setImportoInValuta(getImportoInValuta().abs());
        importoResult.setImportoInValutaAzienda(getImportoInValutaAzienda().abs());
        return importoResult;
    }

    /**
     * restituisce un nuovo oggetto Importo come somma di this e dell'oggetto importo passato come
     * argomento <br>
     * viene effettuato un controllo di integrita' tra le due classi per i valori di tassoDiCambio e
     * codice valuta.
     *
     * @param importo
     *            importo da aggiungere
     * @param scale
     *            scale da utilizzare per il ricalcolo di importoInValutaAzienda
     * @return nuova istanza di Importo con il valore aggiunto
     */
    public Importo add(Importo importo, int scale) {
        if (importo == null) {
            return this;
        }
        if (!getCodiceValuta().equals(importo.getCodiceValuta())) {
            throw new UnsupportedOperationException("il codice valuta non corrisponde ");
        }

        if (getTassoDiCambio().compareTo(importo.getTassoDiCambio()) != 0) {
            throw new UnsupportedOperationException("il tasso di cambio non corrisponde ");
        }
        Importo importoResult = new Importo(this.codiceValuta, this.tassoDiCambio);
        importoResult.setImportoInValuta(getImportoInValuta().add(importo.getImportoInValuta()));
        importoResult.calcolaImportoValutaAzienda(scale);
        return importoResult;
    }

    /**
     * Prende il valore dell'importo in valuta o valutaAzienda e ritorna la somma.
     *
     * @param valore
     *            valore da aggiungere
     *
     * @param tipoOperazioneValuta
     *            tipo di valuta da utilizzare (azienda o valuta)
     *
     * @return risultato della somma
     */
    public BigDecimal addValore(BigDecimal valore, TIPO_OPERAZIONE_VALUTA tipoOperazioneValuta) {
        switch (tipoOperazioneValuta) {
        case AZIENDA:
            return valore.add(getImportoInValutaAzienda());
        default:
            return valore.add(getImportoInValuta());
        }
    }

    /**
     * Calcola l'importo per la valuta aziendale.<br>
     * Devono essere settati il tassoDiCambio e l'importo in valuta
     *
     * @param scale
     *            decimali da utilizzare nel calcolo
     */
    public void calcolaImportoValutaAzienda(int scale) {
        if (BigDecimal.ZERO.compareTo(tassoDiCambio) == 0) {
            logger.error("--> errore nal calcolare l'importo in valuta azienda. tassoDicambio"
                    + tassoDiCambio + " importo in valuta " + importoInValuta);
            throw new RuntimeException(
                    "Impossibile calcolare l'importo. Tasso di cambio o valuta non validi");
        }
        if (importoInValuta != null) {
            importoInValutaAzienda = importoInValuta.divide(tassoDiCambio, scale,
                    BigDecimal.ROUND_HALF_UP);
            importoInValutaAzienda = importoInValutaAzienda.setScale(scale,
                    BigDecimal.ROUND_HALF_UP);
        } else {
            importoInValutaAzienda = null;
        }
    }

    @Override
    public Importo clone() {
        Importo importo = null;
        try {
            importo = (Importo) super.clone();
        } catch (CloneNotSupportedException e) {
            logger.error("-->errore Clone non supportato", e);
        }
        return importo;
    }

    @Override
    public int compareTo(Importo importo2) {
        return importoInValuta.compareTo(importo2.getImportoInValuta());

    }

    /**
     * restituisce un nuovo oggetto Importo come divisione degli attributi importoInValuta e
     * importoInValutaAzienda con l'oggetto divisor passato come argomento .<br>
     *
     * @param divisor
     *            divisore
     * @param scale
     *            scala per il risultato
     * @param roundingMode
     *            metodo di arrotondamento
     * @return nuova istanza di Importo.
     */
    public Importo divide(BigDecimal divisor, int scale, int roundingMode) {
        Importo importoResult = new Importo(getCodiceValuta(), getTassoDiCambio());
        importoResult.setImportoInValuta(getImportoInValuta().divide(divisor, scale, roundingMode));
        importoResult.calcolaImportoValutaAzienda(scale);
        return importoResult;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Importo other = (Importo) obj;
        if (codiceValuta == null) {
            if (other.codiceValuta != null) {
                return false;
            }
        } else if (!codiceValuta.equals(other.codiceValuta)) {
            return false;
        }
        if (importoInValuta == null) {
            if (other.importoInValuta != null) {
                return false;
            }
        } else if (importoInValuta.compareTo(other.importoInValuta) != 0) {
            return false;
        }
        if (importoInValutaAzienda == null) {
            if (other.importoInValutaAzienda != null) {
                return false;
            }
        } else if (importoInValutaAzienda.compareTo(other.importoInValutaAzienda) != 0) {
            return false;
        }
        if (tassoDiCambio == null) {
            if (other.tassoDiCambio != null) {
                return false;
            }
        } else if (tassoDiCambio.compareTo(other.tassoDiCambio) != 0) {
            return false;
        }
        return true;
    }

    /**
     * @return Returns the codiceValuta.
     */
    public String getCodiceValuta() {
        return codiceValuta;
    }

    /**
     * @return Returns the importoInValuta.
     */
    public BigDecimal getImportoInValuta() {
        return importoInValuta;
    }

    /**
     * @return Returns the importoInValutaAzienda.
     */
    public BigDecimal getImportoInValutaAzienda() {
        return importoInValutaAzienda;
    }

    /**
     * @return Returns the tassoDiCambio.
     */
    public BigDecimal getTassoDiCambio() {
        return tassoDiCambio;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((codiceValuta == null) ? 0 : codiceValuta.hashCode());
        result = prime * result + ((importoInValuta == null) ? 0 : importoInValuta.hashCode());
        result = prime * result
                + ((importoInValutaAzienda == null) ? 0 : importoInValutaAzienda.hashCode());
        result = prime * result + ((tassoDiCambio == null) ? 0 : tassoDiCambio.hashCode());
        return result;
    }

    /**
     * metodo per l'inizializzazione degli attributi dell'object corrente.
     */
    protected void initialize() {
        this.importoInValuta = BigDecimal.ZERO;
        this.importoInValutaAzienda = BigDecimal.ZERO;
        this.tassoDiCambio = BigDecimal.ONE;
    }

    /**
     * restituisce un nuovo oggetto Importo come multiplicazione degli attributi importoInValuta e
     * importoInValutaAzienda con l'oggetto multiplicand dell'argomento.<br>
     *
     * @param multiplicand
     *            moltiplicando
     * @param scale
     *            utilizzata solamente per l'arrotondamento nella conversione di importoInValuta in
     *            importoInValutaAzienda
     * @return nuoca classe Importo con il risultato
     */
    public Importo multiply(BigDecimal multiplicand, int scale) {
        Importo importoResult = new Importo(this.codiceValuta, this.tassoDiCambio);
        int scaleImportoInValuta = getImportoInValuta().scale();
        importoResult.setImportoInValuta(getImportoInValuta().multiply(multiplicand));
        importoResult.setImportoInValuta(importoResult.getImportoInValuta()
                .setScale(scaleImportoInValuta, RoundingMode.HALF_UP));
        importoResult.calcolaImportoValutaAzienda(scale);
        return importoResult;
    }

    /**
     * restituisce un nuovo oggetto Importo come multiplicazione degli attributi importoInValuta e
     * importoInValutaAzienda con l'oggetto multiplicand dell'argomento.<br>
     *
     * @param multiplicand
     *            moltiplicando
     * @param scale
     *            utilizzata solamente per l'arrotondamento nella conversione di importoInValuta in
     *            importoInValutaAzienda
     * @param mc
     *            MathContext
     * @return nuova istanza di Importo con il risultato
     */
    public Importo multiply(BigDecimal multiplicand, int scale, MathContext mc) {
        Importo importoResult = new Importo(this.codiceValuta, this.tassoDiCambio);
        importoResult.setImportoInValuta(getImportoInValuta().multiply(multiplicand, mc));
        importoResult.calcolaImportoValutaAzienda(scale);
        return importoResult;
    }

    /**
     * @return nuova istanza di importo con valore degli attributi importoInValuta e
     *         importoInValutaAzienda negativi.
     */
    public Importo negate() {
        Importo importoResult = new Importo(this.codiceValuta, this.tassoDiCambio);
        importoResult.setImportoInValuta(getImportoInValuta().negate());
        importoResult.setImportoInValutaAzienda(getImportoInValutaAzienda().negate());
        return importoResult;

    }

    /**
     * @param codiceValuta
     *            The codiceValuta to set.
     */
    public void setCodiceValuta(String codiceValuta) {
        this.codiceValuta = codiceValuta;
    }

    /**
     * @param importoInValuta
     *            The importoInValuta to set.
     */
    public void setImportoInValuta(BigDecimal importoInValuta) {
        this.importoInValuta = importoInValuta;
    }

    /**
     * @param importoInValutaAzienda
     *            The importoInValutaAzienda to set.
     */
    public void setImportoInValutaAzienda(BigDecimal importoInValutaAzienda) {
        this.importoInValutaAzienda = importoInValutaAzienda;
    }

    /**
     * @param tassoDiCambio
     *            The tassoDiCambio to set.
     */
    public void setTassoDiCambio(BigDecimal tassoDiCambio) {
        this.tassoDiCambio = tassoDiCambio;
    }

    /**
     * restituisce un nuovo oggetto Importo come sottrazione di this e dell'oggetto importo passato
     * come argomento <br>
     * viene effettuato un controllo di integrita' tra le due classi per i valori di tassoDiCambio e
     * codice valuta.
     *
     * @param subtrahend
     *            sottraendo
     * @param scale
     *            utilizzata solamente per l'arrotondamento nella conversione di importoInValuta in
     *            importoInValutaAzienda
     * @return nuova istanza di Importo con il risultato
     */
    public Importo subtract(Importo subtrahend, int scale) {
        if (!getCodiceValuta().equals(subtrahend.getCodiceValuta())) {
            throw new UnsupportedOperationException("il codice valuta non corrisponde ");
        }
        Importo importoResult = new Importo(this.codiceValuta, this.tassoDiCambio);
        importoResult
                .setImportoInValuta(getImportoInValuta().subtract(subtrahend.getImportoInValuta()));
        importoResult.calcolaImportoValutaAzienda(scale);
        return importoResult;

    }

    /**
     * Costruisce una <code>String</code> con tutti gli attributi accodati con questo formato nome
     * attributo = valore.
     *
     * @return toString
     */
    @Override
    public String toString() {
        final String spazio = " ";

        StringBuffer retValue = new StringBuffer();

        retValue.append("Importo[ ").append(spazio).append("importoInValuta = ")
                .append(this.importoInValuta).append(spazio).append("importoInValutaAzienda = ")
                .append(this.importoInValutaAzienda).append(spazio).append("tassoDiCambio = ")
                .append(this.tassoDiCambio).append(spazio).append("codiceValuta = ")
                .append(this.codiceValuta).append(spazio).append("]");

        return retValue.toString();
    }
}
