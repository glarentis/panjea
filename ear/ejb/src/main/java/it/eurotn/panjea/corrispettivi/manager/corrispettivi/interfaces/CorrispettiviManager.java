package it.eurotn.panjea.corrispettivi.manager.corrispettivi.interfaces;

import java.util.Date;

import javax.ejb.Local;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.corrispettivi.domain.CalendarioCorrispettivo;
import it.eurotn.panjea.corrispettivi.domain.Corrispettivo;
import it.eurotn.panjea.manager.interfaces.CrudManager;

@Local
public interface CorrispettiviManager extends CrudManager<Corrispettivo> {

    /**
     * Aggiorna le righe corrispettivo.
     *
     * @param corrispettivo
     *            corrispettivo
     * @param tipoDocumento
     *            tipo documento
     * @throws CorrispettivoPresenteException
     *             CorrispettivoPresenteException
     */
    void aggiornaCodiceIva(Corrispettivo corrispettivo, TipoDocumento tipoDocumento);

    /**
     * Cancella un {@link CalendarioCorrispettivo}.
     *
     * @param calendarioCorrispettivo
     *            calendario da cancellare
     */
    void cancellaCorrispettivi(CalendarioCorrispettivo calendarioCorrispettivo);

    /**
     * Cancella tutti i {@link Corrispettivo} nella data specificata.
     *
     * @param data
     *            data del corrispettivo
     */
    void cancellaCorrispettivo(Date data);

    /**
     * Carica il corrispettivo relativo alla data. Se nella data specificata non è presente nessun corrispettivo ne
     * viene creato uno nuovo.
     *
     * @param data
     *            data corrispettivo
     * @param tipoDocumento
     *            tipoDocumento
     * @param createNew
     *            se creare e salvare un nuovo corrispettivo nel caso non esista
     * @return corrispettivo creato
     * @throws DAOException
     *             DAOException
     * @throws CorrispettivoPresenteException
     *             CorrispettivoPresenteException
     */
    Corrispettivo caricaCorrispettivo(Date data, TipoDocumento tipoDocumento, boolean createNew);
}