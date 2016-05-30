package it.eurotn.panjea.fatturepa.manager.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.fatturepa.domain.AziendaFatturaPA;

/**
 * @author fattazzo
 *
 */
@Local
public interface AziendaFatturaPAManager {

    /**
     * Carica la {@link AziendaFatturaPA} relativa all'azienda loggata.
     *
     * @return {@link AziendaFatturaPA} caricata
     */
    AziendaFatturaPA caricaAziendaFatturaPA();

    /**
     * Salva una {@link AziendaFatturaPA}.
     *
     * @param aziendaFatturaPA
     *            azienda da salvare
     * @return azienda salvata
     */
    AziendaFatturaPA salvaAziendaFatturaPA(AziendaFatturaPA aziendaFatturaPA);
}
