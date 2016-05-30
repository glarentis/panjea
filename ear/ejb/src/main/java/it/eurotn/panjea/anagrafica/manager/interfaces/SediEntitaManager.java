package it.eurotn.panjea.anagrafica.manager.interfaces;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.CaricamentoSediEntita;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.SedeAnagraficaOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.anagrafica.service.exception.TipoSedeEntitaNonTrovataException;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;

/**
 *
 * Interfaccia Local per SessionBean manager di {@link SedeEntita}.
 *
 * @author adriano
 * @version 1.0, 17/dic/07
 *
 */
@Local
public interface SediEntitaManager {

    /**
     * Esegue la cancellazione di {@link SedeEntita} e esegue la cancellazione di {@link SedeAnagrafica} rimaste senza
     * associazione.
     *
     * @param sedeEntita
     *            sede da cancellare
     * @param deleteOrphan
     *            <code>true</code> per la cancellazione delle sedi orfane
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws SedeAnagraficaOrphanException
     *             sollevata in caso esista una sede orfana e deleteOrphan sia <code>false</code>
     */
    void cancellaSedeEntita(SedeEntita sedeEntita, boolean deleteOrphan)
            throws AnagraficaServiceException, SedeAnagraficaOrphanException;

    /**
     * Carica le note di stampa della sede entità, utilizzo un metodo a parte per caricarle solo quando necessario senza
     * dover caricare l'intera Entità.
     *
     * @param idSedeEntita
     *            l'id della sede entità da caricare
     * @return le note stampa o null se non sono definite delle note
     */
    String caricaNoteStampa(Integer idSedeEntita);

    /**
     * Carica il {@link RiepilogoSedeEntitaDTO} dell'azienda loggata.
     *
     * @return riepilogo caricato
     */
    List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntita();

    /**
     * Carica il {@link RiepilogoSedeEntitaDTO} dell'azienda loggata.
     *
     * @param agente
     *            carica le sedi che hanno l'agente richiesto
     * @return riepilogo caricato
     */
    List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByAgente(AgenteLite agente);

    /**
     * Carica il {@link RiepilogoSedeEntitaDTO} dell'azienda loggata.
     *
     * @param idListino
     *            carica le sedi che hanno il listino o il listino arichiesto
     * @return riepilogo caricato
     */
    List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByListino(Integer idListino);

    /**
     * Carica il {@link RiepilogoSedeEntitaDTO} dell'azienda loggata.
     *
     * @param vettore
     *            carica le sedi che hanno il vettore richiesto
     * @return riepilogo caricato
     */
    List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByVettore(VettoreLite vettore);

    /**
     * restituisce una {@link SedeEntita} con il codice richiesto.
     *
     * @param entita
     *            entita di riferimento
     * @param codiceSedeEntita
     *            codice della sede da caricare
     * @return sede caricata, <code>null</code> se non esiste
     */
    SedeEntita caricaSedeEntita(Entita entita, String codiceSedeEntita);

    /**
     * restituisce {@link SedeEntita} identificata dall'argomento idSedeEntita.
     *
     * @param idSedeEntita
     *            id della sede da caricare
     * @return sede caricata
     * @throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException
     *             eccezione generica
     */
    SedeEntita caricaSedeEntita(Integer idSedeEntita)
            throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

    /**
     * restituisce {@link SedeEntita} identificata dall'argomento idSedeEntita.
     *
     * @param idSedeEntita
     *            id della sede da caricare
     * @param caricaLazy
     *            <code>true</code> non inizializza le collezioni lazy della sede
     * @return sede caricata
     * @throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException
     *             eccezione generica
     */
    SedeEntita caricaSedeEntita(Integer idSedeEntita, boolean caricaLazy)
            throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

    /**
     * Carica la sede predefinita per l'entità.
     *
     * @param entita
     *            entità della sede
     * @return sede predefinita dell'entità
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    SedeEntita caricaSedePredefinitaEntita(Entita entita) throws AnagraficaServiceException;

    /**
     * restituisce {@link SedeEntita} principale per {@link Entita} identificata da entita.
     *
     * @param entita
     *            entita di riferimento
     * @return sede principale caricata
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    SedeEntita caricaSedePrincipaleEntita(Entita entita) throws AnagraficaServiceException;

    /**
     * restituisce {@link SedeEntita} principale per {@link Entita} identificata da idEntita.
     *
     * @param idEntita
     *            id entita di riferimento
     * @return sede principale caricata
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    SedeEntita caricaSedePrincipaleEntita(Integer idEntita) throws AnagraficaServiceException;

    /**
     * Carica tutte le sedi abilitate dell'entita.
     *
     * @param idEntita
     *            id entita
     * @return sedi caricate
     */
    List<SedeEntitaLite> caricaSediEntita(Integer idEntita);

    /**
     * Restituisce {@link Set} di {@link SedeEntita} associati ad {@link Entita} identificata dall'argomento di
     * idEntita.
     *
     * @param filtro
     *            fitrlo
     *
     * @param idEntita
     *            id dell'entità
     * @param caricamentoSediEntita
     *            tipo di caricamento
     * @param caricaSedeDisabilitate
     *            carica anche le sedi disabilitate
     * @return sedi caricate
     * @throws AnagraficaServiceException
     *             AnagraficaServiceException
     */
    List<SedeEntita> caricaSediEntita(String filtro, Integer idEntita, CaricamentoSediEntita caricamentoSediEntita,
            boolean caricaSedeDisabilitate) throws AnagraficaServiceException;

    /**
     * Carica tutte le sedi entità associate al vettore.
     *
     * @param vettore
     *            vettore di riferimento
     * @return sedi entità associate
     */
    Set<SedeEntita> caricaSediEntitaAssociate(VettoreLite vettore);

    /**
     * Carica tutte le sedi dell'entità specificata che non ereditano i dati commerciali.
     *
     * @param idEntita
     *            id entità di riferimento
     * @return sedi caricate
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<SedeEntita> caricaSediEntitaNonEreditaDatiComerciali(Integer idEntita) throws AnagraficaServiceException;

    /**
     * resituisce {@link List} di {@link SedeEntita} secondarie associate ad {@link Entita} identificata da idEntita.
     *
     * @param entita
     *            entita di riferimento
     * @return sedi caricate
     * @throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException
     *             eccezione generica
     */
    List<SedeEntita> caricaSediSecondarieEntita(Entita entita)
            throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

    /**
     * Crea soltanto una sede entita' generica con dati geografici di default, flag ereditaDatiCommercaili e
     * ereditaRapportiBancari a true, senza pero' descrizione e indirizzo.
     *
     * @param idEntita
     *            entita' a cui legare la sede
     * @return SedeEntita' con dati di default
     * @throws TipoSedeEntitaNonTrovataException
     *             se non viene trovato il tipoSedeEntita per la sedeEntita generata da documento
     */
    SedeEntita creaSedeEntitaGenerica(Integer idEntita) throws TipoSedeEntitaNonTrovataException;

    /**
     * Genera la {@link SedeEntita} principale.
     *
     * @param entitaRiferimento
     *            entita di cui generare la sede principale
     */
    void generaSedePrincipaleInEntita(Entita entitaRiferimento);

    /**
     * Salva una SedeEntita.
     *
     * @param sedeEntita
     *            sede da salvare
     * @return sede salvata
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws SedeEntitaPrincipaleAlreadyExistException
     *             sollevata se la sede che si sta cercando di salvare è una sede principale e ne esiste già una per
     *             l'entità
     */
    SedeEntita salvaSedeEntita(SedeEntita sedeEntita)
            throws AnagraficaServiceException, SedeEntitaPrincipaleAlreadyExistException;
}
