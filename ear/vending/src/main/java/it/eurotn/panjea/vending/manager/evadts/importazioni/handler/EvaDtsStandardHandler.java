package it.eurotn.panjea.vending.manager.evadts.importazioni.handler;

import org.apache.log4j.Logger;

import it.eurotn.panjea.vending.manager.evadts.exception.ImportazioneEvaDtsException;
import it.eurotn.panjea.vending.manager.evadts.importazioni.parser.LetturaEvaDtsParser;

public abstract class EvaDtsStandardHandler {

    private static final Logger LOGGER = Logger.getLogger(EvaDtsStandardHandler.class);

    private EvaDtsStandardHandler successor;

    /**
     * Costruttore.
     *
     * @param successor
     *            successor
     */
    public EvaDtsStandardHandler(final EvaDtsStandardHandler successor) {
        super();
        this.successor = successor;
    }

    /**
     * @param riga
     *            riga del file
     * @return <code>true</code> se la riga corrisponde all'inizio dello standard gestito
     */
    protected abstract boolean canHandle(String riga);

    protected abstract LetturaEvaDtsParser creaParser(String[] righe, int rigaInizio);

    /**
     * Gestisce le righe passate come parametro creando il relativo parser.
     *
     * @param righe
     *            righe da gestire
     * @param rigaInizio
     *            riga di partenza
     * @return parser creato
     * @throws ImportazioneEvaDtsException
     *             sollevata in caso di errore di lettura
     */
    public LetturaEvaDtsParser handleRighe(String[] righe, int rigaInizio) throws ImportazioneEvaDtsException {
        if (canHandle(righe[rigaInizio])) {
            return creaParser(righe, rigaInizio);
        }

        if (successor != null) {
            return successor.creaParser(righe, rigaInizio);
        }

        LOGGER.error("--> Tipo di riga lettura EVA DTS non gestito " + righe[rigaInizio]);
        throw new ImportazioneEvaDtsException("Tipo di riga lettura EVA DTS non gestito " + righe[rigaInizio]);
    }
}
