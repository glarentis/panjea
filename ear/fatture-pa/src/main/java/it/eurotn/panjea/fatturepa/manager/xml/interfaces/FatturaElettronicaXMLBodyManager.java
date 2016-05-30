package it.eurotn.panjea.fatturepa.manager.xml.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.gov.fatturapa.sdi.fatturapa.IFatturaElettronicaBodyType;

@Local
public interface FatturaElettronicaXMLBodyManager {

    /**
     * @param areaMagazzino
     *            area magazzino di riferimento
     * @return body dreato
     */
    IFatturaElettronicaBodyType getBody(AreaMagazzino areaMagazzino);

}
