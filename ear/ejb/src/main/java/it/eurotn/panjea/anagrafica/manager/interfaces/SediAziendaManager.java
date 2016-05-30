package it.eurotn.panjea.anagrafica.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.SedeAnagraficaOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;

@Local
public interface SediAziendaManager {

    /**
     * Esegue la cancellazione di {@link SedeAzienda}.
     *
     * @param sedeAzienda
     *            sede da cancellare
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws SedeAnagraficaOrphanException
     *             sollevata se la sede anagrafica rimane orfana
     */
    void cancellaSedeAzienda(SedeAzienda sedeAzienda) throws AnagraficaServiceException, SedeAnagraficaOrphanException;

    /**
     * Esegue la cancellazione di {@link SedeAzienda} ed effettua la cancellazione delle
     * {@link SedeAnagrafica} rimaste senza associazione.
     *
     * @param sedeAzienda
     *            sede da cancellare
     * @param deleteOrphan
     *            <code>true</code> per la cancellazione delle sedi orfane
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws SedeAnagraficaOrphanException
     *             sollevata se la sede anagrafica rimane orfana
     */
    void cancellaSedeAzienda(SedeAzienda sedeAzienda, boolean deleteOrphan)
            throws AnagraficaServiceException, SedeAnagraficaOrphanException;

    /**
     * Recupera {@link SedeAzienda} .
     *
     * @param idSedeAzienda
     *            id della sede da caricare
     * @return sede caricata
     * @throws AnagraficaServiceException
     *             eccezione generale
     * @throws ObjectNotFoundException
     */
    SedeAzienda caricaSedeAzienda(Integer idSedeAzienda) throws AnagraficaServiceException;

    /**
     * Recupera la {@link SedeAzienda} principale di {@link Azienda}.
     *
     * @param azienda
     *            azienda di riferimento
     * @return sede caricata
     * @throws AnagraficaServiceException
     *             eccezione generale
     */
    SedeAzienda caricaSedePrincipaleAzienda(Azienda azienda) throws AnagraficaServiceException;

    /**
     * Recupera la {@link List} di {@link SedeAnagrafica} associate ad {@link Azienda}.
     *
     * @param azienda
     *            azienda di riferimento
     * @return sedi caricate
     * @throws AnagraficaServiceException
     *             eccezione generale
     */
    List<SedeAnagrafica> caricaSediAnagraficaAzienda(Azienda azienda) throws AnagraficaServiceException;

    /**
     *
     * @return lista sedi aziendali
     * @throws AnagraficaServiceException
     */
    List<SedeAzienda> caricaSediAzienda() throws AnagraficaServiceException;

    /**
     * Recupera la {@link List} di {@link SedeAzienda}.
     *
     * @param azienda
     *            azienda di riferimento
     * @return sedi caricate
     * @throws AnagraficaServiceException
     *             eccezione generale
     */
    List<SedeAzienda> caricaSediAzienda(Azienda azienda) throws AnagraficaServiceException;

    /**
     * Recupera la {@link List} di {@link SedeAzienda} secondarie.
     *
     * @param azienda
     *            azienda di riferimento
     * @return sedi caricate
     * @throws AnagraficaServiceException
     *             eccezione generale
     */
    List<SedeAzienda> caricaSediSecondarieAzienda(Azienda azienda) throws AnagraficaServiceException;

    /**
     * Esegue il salvataggio di {@link SedeAzienda}.
     *
     * @param sedeAzienda
     *            sede da salvare
     * @return sede salvata
     * @throws AnagraficaServiceException
     *             eccezione generale
     * @throws SedeEntitaPrincipaleAlreadyExistException
     *             sollevata se la sede che si sta cercando di salvare è una sede principale e ne
     *             esiste già una per l'azienda
     */
    SedeAzienda salvaSedeAzienda(SedeAzienda sedeAzienda)
            throws AnagraficaServiceException, SedeEntitaPrincipaleAlreadyExistException;

}
