package it.eurotn.panjea.fatturepa.manager.xml.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.Trasmissione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaType;

@Local
public interface FatturaElettronicaXMLManager {

    /**
     * Dall'XML passato ocme parametro viene fatto l'unmarshalling e creata la fattura elettronica type.
     *
     * @param xmlContent
     *            contenuto dell'xml
     * @return fattura creata
     */
    IFatturaElettronicaType caricaFatturaElettronicaType(String xmlContent);

    /**
     * Restituisce l'xml generato dalla {@link IFatturaElettronicaType}.
     *
     * @param fatturaElettronicaType
     *            fattura elettronica type
     * @return xml creato
     */
    String getXmlData(IFatturaElettronicaType fatturaElettronicaType);

    /**
     * Restituisce l'xml generato dei documenti come array di byte.
     *
     * @param trasmissione
     *            trasmissione
     * @param aziendaPA
     *            dati azienda PA
     * @param areaMagazzino
     *            area magazzino di riferimento
     * @return xml creato
     */
    String getXmlData(Trasmissione trasmissione, AziendaFatturaPA aziendaPA, AreaMagazzino areaMagazzino);
}
