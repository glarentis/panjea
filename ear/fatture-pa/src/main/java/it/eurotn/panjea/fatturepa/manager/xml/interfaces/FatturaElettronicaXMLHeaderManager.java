package it.eurotn.panjea.fatturepa.manager.xml.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;
import it.eurotn.panjea.fatturepa.domain.Trasmissione;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.gov.fatturapa.sdi.fatturapa.FormatoTrasmissioneType;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaHeaderType;

@Local
public interface FatturaElettronicaXMLHeaderManager {

    /**
     * @return formato di trasmissione gestito
     */
    FormatoTrasmissioneType getFormatoTrasmissione();

    /**
     *
     * @param trasmissione
     *            trasmissione di riferimento
     * @param aziendaPA
     *            dati azienda PA
     * @param areaMagazzino
     *            area magazzino di riferimento
     * @return header
     */
    IFatturaElettronicaHeaderType getHeader(Trasmissione trasmissione, AziendaFatturaPA aziendaPA,
            AreaMagazzino areaMagazzino);
}
