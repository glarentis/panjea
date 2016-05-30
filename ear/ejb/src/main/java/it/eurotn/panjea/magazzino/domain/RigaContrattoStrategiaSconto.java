package it.eurotn.panjea.magazzino.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import it.eurotn.panjea.magazzino.domain.RigaContratto.Azione;

/**
 * Contiene i parametri utilizzati per il calcolo dello sconto su una riga contratto.
 *
 * @author giangi
 */
@Embeddable
public class RigaContrattoStrategiaSconto implements Serializable {
    private static final long serialVersionUID = -6810022230066673792L;
    /**
     * @uml.property name="azioneSconto"
     * @uml.associationEnd
     */
    private Azione azioneSconto;

    /**
     * @uml.property name="bloccoSconto"
     */
    private boolean bloccoSconto;

    /**
     * @uml.property name="ignoraBloccoScontoPrecedente"
     */
    private boolean ignoraBloccoScontoPrecedente;

    /**
     * indica se il contratto ha una soglia di qta per attivare la stategia.
     *
     * @uml.property name="quantitaSogliaSconto"
     */
    private Double quantitaSogliaSconto;

    /**
     * @uml.property name="sconto"
     * @uml.associationEnd
     */
    @ManyToOne
    private Sconto sconto;

    /**
     * Costruttore.
     *
     */
    public RigaContrattoStrategiaSconto() {
        initialize();
    }

    /**
     * @return azioneSconto
     * @uml.property name="azioneSconto"
     */
    public Azione getAzioneSconto() {
        return azioneSconto;
    }

    /**
     * @return quantitaSogliaSconto
     * @uml.property name="quantitaSogliaSconto"
     */
    public Double getQuantitaSogliaSconto() {
        return quantitaSogliaSconto;
    }

    /**
     * @return sconto
     * @uml.property name="sconto"
     */
    public Sconto getSconto() {
        return sconto;
    }

    /**
     * Inizializza i valori di default.
     */
    private void initialize() {
        azioneSconto = Azione.VARIAZIONE;
        bloccoSconto = false;
        ignoraBloccoScontoPrecedente = false;
        quantitaSogliaSconto = 0.0;
    }

    /**
     * @return bloccoSconto
     * @uml.property name="bloccoSconto"
     */
    public boolean isBloccoSconto() {
        return bloccoSconto;
    }

    /**
     * @return ignoraBloccoScontoPrecedente
     * @uml.property name="ignoraBloccoScontoPrecedente"
     */
    public boolean isIgnoraBloccoScontoPrecedente() {
        return ignoraBloccoScontoPrecedente;
    }

    /**
     * @param azioneSconto
     *            the azioneSconto to set
     * @uml.property name="azioneSconto"
     */
    public void setAzioneSconto(Azione azioneSconto) {
        this.azioneSconto = azioneSconto;
    }

    /**
     * @param bloccoSconto
     *            the bloccoSconto to set
     * @uml.property name="bloccoSconto"
     */
    public void setBloccoSconto(boolean bloccoSconto) {
        this.bloccoSconto = bloccoSconto;
    }

    /**
     * @param ignoraBloccoScontoPrecedente
     *            the ignoraBloccoScontoPrecedente to set
     * @uml.property name="ignoraBloccoScontoPrecedente"
     */
    public void setIgnoraBloccoScontoPrecedente(boolean ignoraBloccoScontoPrecedente) {
        this.ignoraBloccoScontoPrecedente = ignoraBloccoScontoPrecedente;
    }

    /**
     * @param quantitaSogliaSconto
     *            the quantitaSogliaSconto to set
     * @uml.property name="quantitaSogliaSconto"
     */
    public void setQuantitaSogliaSconto(Double quantitaSogliaSconto) {
        this.quantitaSogliaSconto = quantitaSogliaSconto;
    }

    /**
     * @param sconto
     *            the sconto to set
     * @uml.property name="sconto"
     */
    public void setSconto(Sconto sconto) {
        this.sconto = sconto;
    }
}
