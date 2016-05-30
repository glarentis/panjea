package it.gov.fatturapa.sdi.fatturapa;

import org.w3._2000._09.xmldsig_.SignatureType;

import it.gov.fatturapa.sdi.fatturapa.v1.DatiTrasmissioneType;

public interface IFatturaElettronicaType {

    /**
     * Ripulisce tutti i valori che non sono avvalorati per generare correttamente l'xml.
     */
    void cleanEmptyValues();

    /**
     * Copia tutte le proprietà non richieste che sono avvalorate dalla fattura elettronica passata come parametro.
     *
     * @param otherFatturaElettronicaType
     *            fattura elettronica
     */
    void copyNotRequiredProperty(IFatturaElettronicaType otherFatturaElettronicaType);

    /**
     * @return dati di trasmissione
     */
    DatiTrasmissioneType getDatiTrasmissione();

    /**
     * Gets the value of the signature property.
     *
     * @return possible object is {@link SignatureType }
     *
     */
    SignatureType getSignature();

    /**
     * Gets the value of the versione property.
     *
     * @return possible object is {@link String }
     *
     */
    String getVersione();
}
