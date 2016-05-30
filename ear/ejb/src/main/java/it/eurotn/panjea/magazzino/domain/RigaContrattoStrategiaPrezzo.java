package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import it.eurotn.panjea.magazzino.domain.RigaContratto.Azione;

/**
 * Contiene i parametri utilizzati per il calcolo del prezzo su una riga contratto.
 *
 * @author giangi
 */
@Embeddable
public class RigaContrattoStrategiaPrezzo implements Serializable {

    /**
     * Indica se il valore sulla riga è un importo oppure uno sconto.
     */
    public enum TipoValore {
        /**
         * @uml.property name="iMPORTO"
         * @uml.associationEnd
         */
        IMPORTO, /**
                  * @uml.property name="pERCENTUALE"
                  * @uml.associationEnd
                  */
        PERCENTUALE
    }

    private static final long serialVersionUID = -8726191752407653419L;

    /**
     * @uml.property name="azionePrezzo"
     * @uml.associationEnd
     */
    private Azione azionePrezzo;

    /**
     * @uml.property name="bloccoPrezzo"
     */
    private boolean bloccoPrezzo;
    /**
     * @uml.property name="ignoraBloccoPrezzoPrecedente"
     */
    private boolean ignoraBloccoPrezzoPrecedente;
    /**
     * indica se il contratto ha una soglia di qta per attivare la stategia.
     * 
     */
    private Double quantitaSogliaPrezzo;

    /**
     * @uml.property name="valorePrezzo"
     */
    @Column(precision = 19, scale = 6)
    private BigDecimal valorePrezzo;

    /**
     * Valore può essere uno sconto o un importo applicato al prezzo.
     * 
     * @uml.property name="tipoValorePrezzo"
     * @uml.associationEnd
     */
    private TipoValore tipoValorePrezzo;

    /**
     * costruttore di default.
     */
    public RigaContrattoStrategiaPrezzo() {
        tipoValorePrezzo = TipoValore.IMPORTO;
        azionePrezzo = Azione.VARIAZIONE;
        bloccoPrezzo = false;
        ignoraBloccoPrezzoPrecedente = false;
        quantitaSogliaPrezzo = 0.0;
    }

    /**
     * @return the azionePrezzo
     * @uml.property name="azionePrezzo"
     */
    public Azione getAzionePrezzo() {
        return azionePrezzo;
    }

    /**
     * @return the quantitaSogliaPrezzo
     * @uml.property name="quantitaSogliaPrezzo"
     */
    public Double getQuantitaSogliaPrezzo() {
        return quantitaSogliaPrezzo;
    }

    /**
     * @return the tipoValorePrezzo
     * @uml.property name="tipoValorePrezzo"
     */
    public TipoValore getTipoValorePrezzo() {
        return tipoValorePrezzo;
    }

    /**
     * @return the valorePrezzo
     * @uml.property name="valorePrezzo"
     */
    public BigDecimal getValorePrezzo() {
        return valorePrezzo;
    }

    /**
     * @return the bloccoPrezzo
     * @uml.property name="bloccoPrezzo"
     */
    public boolean isBloccoPrezzo() {
        return bloccoPrezzo;
    }

    /**
     * @return the ignoraBloccoPrezzoPrecedente
     * @uml.property name="ignoraBloccoPrezzoPrecedente"
     */
    public boolean isIgnoraBloccoPrezzoPrecedente() {
        return ignoraBloccoPrezzoPrecedente;
    }

    /**
     * @param azionePrezzo
     *            the azionePrezzo to set
     * @uml.property name="azionePrezzo"
     */
    public void setAzionePrezzo(Azione azionePrezzo) {
        this.azionePrezzo = azionePrezzo;
    }

    /**
     * @param bloccoPrezzo
     *            the bloccoPrezzo to set
     * @uml.property name="bloccoPrezzo"
     */
    public void setBloccoPrezzo(boolean bloccoPrezzo) {
        this.bloccoPrezzo = bloccoPrezzo;
    }

    /**
     * @param ignoraBloccoPrezzoPrecedente
     *            the ignoraBloccoPrezzoPrecedente to set
     * @uml.property name="ignoraBloccoPrezzoPrecedente"
     */
    public void setIgnoraBloccoPrezzoPrecedente(boolean ignoraBloccoPrezzoPrecedente) {
        this.ignoraBloccoPrezzoPrecedente = ignoraBloccoPrezzoPrecedente;
    }

    /**
     * @param quantitaSogliaPrezzo
     *            the quantitaSogliaPrezzo to set
     * @uml.property name="quantitaSogliaPrezzo"
     */
    public void setQuantitaSogliaPrezzo(Double quantitaSogliaPrezzo) {
        this.quantitaSogliaPrezzo = quantitaSogliaPrezzo;
    }

    /**
     * @param tipoValorePrezzo
     *            the tipoValorePrezzo to set
     * @uml.property name="tipoValorePrezzo"
     */
    public void setTipoValorePrezzo(TipoValore tipoValorePrezzo) {
        this.tipoValorePrezzo = tipoValorePrezzo;
    }

    /**
     * @param valorePrezzo
     *            the valorePrezzo to set
     * @uml.property name="valorePrezzo"
     */
    public void setValorePrezzo(BigDecimal valorePrezzo) {
        this.valorePrezzo = valorePrezzo;
    }
}
