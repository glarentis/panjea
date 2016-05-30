/**
 *
 */
package it.eurotn.panjea.anagrafica.rich.bd;

import java.util.List;
import java.util.Set;

import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.CaricamentoSediEntita;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.Contatto;
import it.eurotn.panjea.anagrafica.domain.ContattoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.Filiale;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioSedeEntita;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.manager.depositi.ParametriRicercaDepositi;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.anagrafica.service.exception.ContattoOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeAnagraficaOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.anagrafica.service.exception.TipoSedeEntitaNonTrovataException;
import it.eurotn.panjea.anagrafica.service.interfaces.AnagraficaService;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.anagrafica.util.RubricaDTO;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;

/**
 * Interfaccia del business delegate di {@link AnagraficaService}.
 *
 * @author adriano
 * @version 1.0, 18/dic/07
 *
 */
public interface IAnagraficaBD {
    final String BEAN_ID = "anagraficaBD";

    /**
     * esegue la variazione della {@link SedeAzienda} principale.
     *
     * @param nuovaSedeAziendaPrincipaleAzienda
     * @param tipoSedeSostitutivaEntita
     */
    @AsyncMethodInvocation
    void cambiaSedePrincipaleAzienda(SedeAzienda nuovaSedeAziendaPrincipaleAzienda,
            TipoSedeEntita tipoSedeSostitutivaEntita);

    /**
     * Esegue la varizione della {@link SedeEntita} principale della sua {@link Entita}.
     *
     * @param sedeEntita
     * @param tipoSedeEntita
     *            {@link TipoSedeEntita} secondaria da assegnare alla precedente {@link SedeEntita} principale
     */
    @AsyncMethodInvocation
    void cambiaSedePrincipaleEntita(SedeEntita sedeEntita, TipoSedeEntita tipoSedeEntita);

    /**
     * esegue la cancellazione di {@link Banca}.
     *
     * @param banca
     * @throws AnagraficaServiceException
     */
    void cancellaBanca(Banca banca);

    void cancellaCategoriaEntita(CategoriaEntita categoriaEntita);

    /**
     * Esegue la cancellazione di tutti i <code>ContattoSedeEntita</code> e di tutti i <code>Contatto</code>.
     *
     * @param idEntita
     */
    void cancellaContattiPerEntita(Entita entita);

    /**
     * Esegue la cancellazione di tutti i <code>ContattoSedeEntita</code> e di tutti i <code>Contatto</code>.
     *
     * @param sedeEntita
     */
    void cancellaContattiPerSedeEntita(SedeEntita sedeEntita);

    /**
     * esegue la cancellazione di <code>Contatto</code>.
     *
     * @param contatto
     */
    void cancellaContatto(Contatto contatto);

    /**
     * Esegue la cancellazione di <code>MansioneSede</code> e verifica in caso di deleteOrphan==true se il contatto
     * risulta essere orfano ed eventualmente rilancia una <code>ContattoOrphanException</code>.
     *
     * @param mansioneSede
     * @throws AnagraficaServiceException
     * @throws ContattoOrphanException
     */
    void cancellaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita);

    /**
     * Cancella un deposito.
     *
     * @param deposito
     *            Value Object del deposito da cancellare.
     * @throws AnagraficaServiceException
     */
    void cancellaDeposito(Deposito deposito);

    /**
     * Esegue la cancellazione di <code>Entita</code>.
     *
     * @param idEntita
     */
    @AsyncMethodInvocation
    void cancellaEntita(Entita entita);

    /**
     * esegue la cancellazione di {@link Filiale}.
     *
     * @param filiale
     * @throws AnagraficaServiceException
     */
    void cancellaFiliale(Filiale filiale);

    /**
     *
     * @param rapportoBancarioAzienda
     * @throws AnagraficaServiceException
     */
    void cancellaRapportoBancario(RapportoBancarioAzienda rapportoBancarioAzienda);

    /**
     * Cancella il rapporto bancario associato alla sede do un entità.
     *
     * @param rapportoBancario
     *            rapporto bancario da cancellare
     */
    void cancellaRapportoBancarioSedeEntita(RapportoBancarioSedeEntita rapportoBancario);

    /**
     * Esegue la cancellazione di {@link SedeAzienda}
     *
     * @param sedeAzienda
     */
    void cancellaSedeAzienda(SedeAzienda sedeAzienda) throws SedeAnagraficaOrphanException;

    /**
     * Esegue la cancellazione di {@link SedeAzienda} ed effettua la cancellazione delle {@link SedeAnagrafica} rimaste
     * senza associazione
     *
     * @param sedeAzienda
     * @param deleteOrphan
     */
    void cancellaSedeAzienda(SedeAzienda sedeAzienda, boolean deleteOrphan);

    /**
     * Esegue la cancellazione di {@link SedeEntita} e esegue la cancellazione di {@link SedeAnagrafica} rimaste senza
     * associazione
     *
     * @param sedeEntita
     * @param deleteOrphan
     * @throws SedeAnagraficaOrphanException
     */
    void cancellaSedeEntita(SedeEntita sedeEntita, boolean deleteOrphan) throws SedeAnagraficaOrphanException;

    /**
     * Carica tutti gli agenti che non sono ancora associati ad un capo area.
     *
     * @return agenti caricati
     */
    @AsyncMethodInvocation
    List<Agente> caricaAgentiSenzaCapoArea();

    /**
     * recupera {@link Anagrafica} identificata dall'argomento idAnagrafica
     *
     * @param anagraficaLite
     * @return
     */
    Anagrafica caricaAnagrafica(Integer idAnagrafica);

    /**
     *
     * @return tutte le anagrafiche aziendali con "l'albero" completo (entità,sedi,contatti)
     */
    @AsyncMethodInvocation
    List<RubricaDTO> caricaAnagraficheFull();

    /**
     * Carica tutte le anagrafiche presenti per l'azienda loggata.
     *
     * @param codice
     *            codice fiscale o partita iva da filtrare
     * @param denominazione
     *            denominazione
     * @return anagrafiche caricate
     */
    @AsyncMethodInvocation
    List<AnagraficaLite> caricaAnagraficheSearchObject(String codice, String denominazione);

    /**
     * Carica l'azienda corrente restituendo una AziendaLite contenente le informazioni principali dell'azienda
     *
     * @return AziendaLite
     */
    AziendaLite caricaAzienda();

    /**
     * Carica l'azienda dal codice
     *
     * @param codiceAzienda
     * @return
     */
    AziendaLite caricaAzienda(String codiceAzienda);

    /**
     * recupera {@link Azienda} e {@link SedeAzienda} principale e li restituisce all'interno di
     * {@link AziendaAnagraficaDTO}
     *
     * @param codice
     * @return
     */
    AziendaAnagraficaDTO caricaAziendaAnagrafica(String codice);

    /**
     * recupera {@link Azienda} per codice
     *
     * @param codice
     * @return
     */
    Azienda caricaAziendaByCodice(String codice);

    /**
     * Restiuisce {@link List} di {@link AziendaLite}
     *
     * @return
     */
    List<AziendaLite> caricaAziende();

    /**
     * carica {@link Banca} identificata da idBanca
     *
     * @param idBanca
     * @return
     * @throws AnagraficaServiceException
     */
    Banca caricaBanca(Integer idBanca);

    /**
     * Carica la {@link List} di {@link Banca}
     *
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return
     * @throws AnagraficaServiceException
     */
    @AsyncMethodInvocation
    List<Banca> caricaBanche(String fieldSearch, String valueSearch);

    /**
     * carica la liste de categorie della entita.
     *
     * @param idEntita
     * @return
     * @throws AnagraficaServiceException
     */
    List<CategoriaEntita> caricaCategoriaEntitaByEntita(Integer idEntita);

    List<CategoriaEntita> caricaCategorieEntita(String fieldSearch, String valueSearch);

    /**
     * Carica tutte le causali per le ritenute acconto.
     *
     * @return causali caricate
     */
    List<CausaleRitenutaAcconto> caricaCausaliRitenuteAcconto();

    /**
     * Carica tutte le classi tipo documento.
     *
     * @return
     */
    List<IClasseTipoDocumento> caricaClassiTipoDocumento();

    List<String> caricaCodiciValuteAzienda();

    /**
     * Carica indistintamente tutti i <code>Contatto</code> di <code>Entita</code>
     *
     * @param idEntita
     * @return
     */
    List<Contatto> caricaContattiPerEntita(Entita entita);

    /**
     * Restituisce tutte le mansioni di una data Entita TODO modificare argomenti id -> Entita
     *
     * @return
     * @throws AnagraficaServiceException
     */
    List<ContattoSedeEntita> caricaContattiSedeEntitaPerEntita(Entita entita);

    /**
     * Restituisce tutte le mansioni di una data SedeEntita
     *
     * @return
     * @throws AnagraficaServiceException
     */
    List<ContattoSedeEntita> caricaContattiSedeEntitaPerSedeEntita(SedeEntita sedeEntita);

    /**
     * restituisce {@link Contatto} identificato da idContatto
     *
     * @param idContatto
     * @return
     * @throws AnagraficaServiceException
     */
    Contatto caricaContatto(java.lang.Integer idContatto);

    ContattoSedeEntita caricaContattoSedeEntita(java.lang.Integer idMansioneSede)
            throws it.eurotn.dao.exception.ObjectNotFoundException, it.eurotn.dao.exception.DAOException;

    /**
     * Carica tutti i contributi previdenziali Enasarco.
     *
     * @return contributi caricati
     */
    List<ContributoPrevidenziale> caricaContributiEnasarco();

    /**
     * Carica tutti i contributi previdenziali INPS.
     *
     * @return contributi caricati
     */
    List<ContributoPrevidenziale> caricaContributiINPS();

    /**
     * Carica i depositi per tutte le sedi dell'azienda.
     *
     * @return lista di Depositi
     */
    @AsyncMethodInvocation
    List<Deposito> caricaDepositi();

    /**
     * Carica i depositi per l'entità.
     *
     * @param entita
     *            entita di riferimento
     * @return lista di Depositi
     */
    List<Deposito> caricaDepositi(EntitaLite entita);

    /**
     * Carica i depositi per la sede.
     *
     * @param sedeAzienda
     *            . L'ID deve essere settato
     * @return lista di Depositi
     * @throws AnagraficaServiceException
     */
    @AsyncMethodInvocation
    List<Deposito> caricaDepositi(SedeAzienda sedeAzienda);

    /**
     * Carica i depositi per tutte le sedi dell'azienda.
     *
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return lista di Depositi
     */
    List<Deposito> caricaDepositi(String fieldSearch, String valueSearch);

    /**
     * Ricerca i depositi dell'azienda.
     *
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @param soloConMezziTrasporto
     *            carica solo i depositi associati ad un mezzo di trasporto
     * @return List<DepositoLite>
     */
    List<DepositoLite> caricaDepositiAzienda(String fieldSearch, String valueSearch, boolean soloConMezziTrasporto);

    /**
     * Recupera {@link Deposito} identificato da idDeposito
     *
     * @param idDeposito
     * @return
     * @throws AnagraficaServiceException
     */
    Deposito caricaDeposito(Integer idDeposito);

    /**
     *
     * @return deposito principale dell'azienda loggata
     */
    Deposito caricaDepositoPrincipale();

    /**
     * Carica tutte le entita associate all'anagrafica specificata.
     *
     * @param anagraficaLite
     *            anagrafica di riferimento
     * @return entita caricate
     */
    @AsyncMethodInvocation
    List<EntitaLite> caricaEntita(AnagraficaLite anagraficaLite);

    /**
     * restituisce Entita
     *
     * @param entita
     * @return
     */
    Entita caricaEntita(Entita entita);

    /**
     * restituisce un {@link Entita} attraverso il corrispondente oggetto lite
     *
     * @param entitaLite
     * @return
     * @throws AnagraficaServiceException
     */
    Entita caricaEntita(EntitaLite entitaLite, boolean caricaLazy);

    /**
     *
     * @param categorie
     *            lista di categorie per le quali caricare i clienti.
     * @return clienti appartenenti a tutte le categorie
     */
    List<SedeEntita> caricaEntitaByCategorie(List<CategoriaEntita> categorie);

    /**
     * recupera {@link EntitaLite}
     *
     * @param entitaLite
     * @return
     */
    EntitaLite caricaEntitaLite(EntitaLite entitaLite);

    /**
     * carica {@link Filiale} identificato da idFiliale
     *
     * @param idFiliale
     * @return
     * @throws AnagraficaServiceException
     */
    Filiale caricaFiliale(Integer idFiliale);

    /**
     * Carica la {@link List} di {@link Filiale}
     *
     * @return
     * @throws AnagraficaServiceException
     */
    @AsyncMethodInvocation
    List<Filiale> caricaFiliali();

    /**
     * Carica {@link List} di {@link Filiale} associate all'argomento {@link Banca}.
     *
     * @param banca
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return
     * @throws AnagraficaServiceException
     */
    List<Filiale> caricaFiliali(Banca banca, String fieldSearch, String valueSearch);

    /**
     * Carica tutte le prestazioni.
     *
     * @return prestazioni caricate
     */
    List<Prestazione> caricaPrestazioni();

    /**
     * Recupera {@link List} di {@link RapportoBancarioAzienda}
     *
     * @param filter
     * @param object
     * @param valueSearch
     * @return
     * @throws AnagraficaServiceException
     */
    List<RapportoBancarioAzienda> caricaRapportiBancariAzienda(String filter, String valueSearch);

    List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita);

    /**
     * Carica i rapporti bancari per l'entità interessata.
     *
     * @param idSedeEntita
     *            id sede
     * @param ignoraEredita
     *            ignora l'ereditarietà dei rapporti bancari
     * @return rapporti bancari sede. rapp sede principale se eredita
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita, boolean ignoraEredita);

    List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntitaPrincipale(String fieldSearch, String valueSearch,
            Integer idEntita);

    /**
     * Recupera {@link RapportoBancarioAzienda} identificato da idRapportoBancario.
     *
     * @param idRapportoBancario
     * @return
     * @throws AnagraficaServiceException
     */
    RapportoBancarioAzienda caricaRapportoBancario(Integer idRapportoBancario, boolean initializeLazy);

    /**
     * Carica il riepilogo dei dati bancari delle entità.
     *
     * @return riepilogo
     */
    List<RapportoBancarioSedeEntita> caricaRiepilogoDatiBancari();

    /**
     * Carica il {@link RiepilogoSedeEntitaDTO} dell'azienda loggata.
     *
     * @return riepilogo caricato
     */
    @AsyncMethodInvocation
    List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntita();

    /**
     * Carica il {@link RiepilogoSedeEntitaDTO} dell'azienda loggata.
     *
     * @param agente
     *            carica le sedi che hanno l'agente richiesto
     * @return riepilogo caricato
     */
    @AsyncMethodInvocation
    List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByAgente(AgenteLite agente);

    /**
     * Carica il {@link RiepilogoSedeEntitaDTO} dell'azienda loggata.
     *
     * @param vettore
     *            carica le sedi che hanno il vettore richiesto
     * @return riepilogo caricato
     */
    @AsyncMethodInvocation
    List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByVettore(VettoreLite vettore);

    /**
     * Recupera {@link SedeAzienda}.
     *
     * @param idSedeAzienda
     * @return
     */
    SedeAzienda caricaSedeAzienda(Integer idSedeAzienda);

    /**
     * restituisce {@link SedeEntita} identificata dall'argomento idSedeEntita.
     *
     * @param idSedeEntita
     * @return
     */
    SedeEntita caricaSedeEntita(Integer idSedeEntita);

    /**
     * restituisce {@link SedeEntita} identificata dall'argomento idSedeEntita.
     *
     * @param idSedeEntita
     *            id della sede da caricare
     * @param caricaLazy
     *            <code>true</code> non inizializza le collezioni lazy della sede
     * @return sede caricata
     */
    SedeEntita caricaSedeEntita(Integer idSedeEntita, boolean caricaLazy);

    /**
     * Carica la sede predefinita per l'entità.
     *
     * @param entita
     *            entità della sede
     * @return sede predefinita dell'entità
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    SedeEntita caricaSedePredefinitaEntita(Entita entita);

    /**
     * Recupera la {@link SedeAzienda} principale di {@link Azienda}.
     *
     * @param azienda
     * @return
     */
    SedeAzienda caricaSedePrincipaleAzienda(Azienda azienda);

    /**
     * restituisce {@link SedeEntita} principale per {@link Entita} identificata da idEntita.
     *
     * @param entita
     * @return
     * @throws AnagraficaServiceException
     */
    SedeEntita caricaSedePrincipaleEntita(Entita entita);

    /**
     * Recupera la {@link List} di {@link SedeAnagrafica} associate ad {@link Anagrafica}.
     *
     * @param idAnagrafica
     * @return
     */
    List<SedeAnagrafica> caricaSediAnagrafica(Anagrafica anagrafica);

    /**
     * Recupera la {@link List} di {@link SedeAnagrafica} associate ad {@link Azienda}.
     *
     * @param azienda
     * @return
     */
    List<SedeAnagrafica> caricaSediAnagraficaAzienda(Azienda azienda);

    Set<SedeEntita> caricaSediAssociate(AgenteLite agente);

    /**
     *
     * @return lista sedi aziendali
     * @throws AnagraficaServiceException
     */
    List<SedeAzienda> caricaSediAzienda();

    /**
     * Recupera la {@link List} di {@link SedeAzienda}.
     *
     * @param azienda
     * @return
     */
    List<SedeAzienda> caricaSediAzienda(Azienda azienda);

    /**
     * Carica tutte le sedi abilitate dell'entita.
     *
     * @param idEntita
     *            id entita
     * @return sedi caricate
     */
    @AsyncMethodInvocation
    List<SedeEntitaLite> caricaSediEntita(Integer idEntita);

    /**
     * Restituisce {@link Set} di {@link SedeEntita} associati ad {@link Entita} identificata dall'argomento di
     * idEntita.
     *
     * @param valueSearch
     *            valore da filtrare
     *
     * @param idEntita
     *            id dell'entità
     * @param caricamentoSediEntita
     *            tipo di caricamento
     * @return sedi caricate
     */
    @AsyncMethodInvocation
    List<SedeEntita> caricaSediEntita(String valueSearch, Integer idEntita, CaricamentoSediEntita caricamentoSediEntita,
            Boolean caricaSedeDisabilitate);

    /**
     * Carica tutte le sedi entità associate al vettore.
     *
     * @param vettore
     *            vettore di riferimento
     * @return sedi entità associate
     */
    @AsyncMethodInvocation
    Set<SedeEntita> caricaSediEntitaAssociate(VettoreLite vettore);

    /**
     * carica le sede anagrafiche che non ereditano dati comerciali per la entita.
     *
     * @param idEntita
     * @return
     */
    @AsyncMethodInvocation
    List<SedeEntita> caricaSediEntitaNonEreditaDatiComerciali(Integer idEntita);

    /**
     * Recupera la {@link List} di {@link SedeAzienda} secondarie.
     *
     * @param azienda
     * @return
     */
    @AsyncMethodInvocation
    List<SedeAzienda> caricaSediSecondarieAzienda(Azienda azienda);

    /**
     * resituisce {@link List} di {@link SedeEntita} secondarie associate ad {@link Entita} identificata da idEntita.
     *
     * @param entita
     * @return @
     */
    @AsyncMethodInvocation
    List<SedeEntita> caricaSediSecondarieEntita(Entita entita);

    Entita confermaClientePotenziale(Integer idEntita);

    /**
     *
     * @param idEntita
     * @return
     * @throws TipoSedeEntitaNonTrovataException
     */
    SedeEntita creaSedeEntitaGenerica(Integer idEntita);

    /**
     * Restituisce tutte le variabili che possono essere utilizzate per la generazione del codice.
     *
     * @return lista di variabili
     */
    String[] getVariabiliPatternCodiceDocumento();

    /**
     * Esegue la ricerca tra tutti i Contatto esistenti filtrando per i valori contenuti nella <code>Map</code>
     * parametri.
     *
     * @param parametri
     * @return List<MansioneSede>
     */
    @AsyncMethodInvocation
    List<Contatto> ricercaContatti(java.util.Map<String, Object> parametri);

    /**
     * Esegue la ricerca tra tutti i Contatto di un <code>Entita</code> filtrando per i valori contenuti nella
     * <code>Map</code> parametri.
     *
     * @param idEntita
     * @param parametri
     * @return List<MansioneSede>
     */
    @AsyncMethodInvocation
    List<Contatto> ricercaContattiPerEntita(Entita entita, java.util.Map<String, Object> parametri);

    /**
     * Ricerca i depositi in base ai parametri di ricerca.
     *
     * @param parametri
     *            parametri di ricerca
     * @return depositi trovati
     */
    List<Deposito> ricercaDepositi(ParametriRicercaDepositi parametri);

    /**
     * restituisce {@link List} di {@link EntitaLite} che rispondono ai criteri presenti in parametri.
     *
     * @param parametriRicercaEntita
     *            parametri di ricerca
     * @return entità trovate
     */
    @AsyncMethodInvocation
    List<EntitaLite> ricercaEntita(ParametriRicercaEntita parametriRicercaEntita);

    /**
     * restituisce {@link List} di {@link EntitaLite} che rispondono ai criteri presenti.
     *
     * @param codiceFiscale
     *            codice fiscale
     * @param partitaIva
     *            partita iva
     * @return entità trovate
     */
    @AsyncMethodInvocation
    List<EntitaLite> ricercaEntita(String codiceFiscale, String partitaIva);

    /**
     * Esegue una ricerca ottimizzata di {@link EntitaLite} che rispondono ai criteri presenti in parametri.
     *
     * @param parametriRicercaEntita
     *            parametri di ricerca
     * @return entità trovate
     */
    List<EntitaLite> ricercaEntitaSearchObject(ParametriRicercaEntita parametriRicercaEntita);

    /**
     * esegue il salvataggio di {@link Azienda}.
     *
     * @param azienda
     * @return
     */
    Azienda salvaAzienda(Azienda azienda) throws AnagraficaServiceException;

    /**
     * Esegue il salvataggio di {@link Azienda} e {@link SedeAzienda}.
     *
     * @param aziendaAnagraficaDTO
     * @return
     */
    AziendaAnagraficaDTO salvaAziendaAnagrafica(AziendaAnagraficaDTO aziendaAnagraficaDTO);

    /**
     * salva {@link Banca}.
     *
     * @return
     */
    Banca salvaBanca(Banca banca);

    /**
     *
     * @param categoriaEntita
     * @return
     */
    CategoriaEntita salvaCategoriaEntita(CategoriaEntita categoriaEntita);

    /**
     * Esegue il salvataggio di un istanza di <code>Contatto</code>.
     *
     * @param contatto
     * @return
     */
    Contatto salvaContatto(Contatto contatto);

    /**
     * Esegue il salvataggio di mansione sede {@link ContattoSedeEntita}.
     *
     * @param contattoSedeEntita
     * @return
     */
    ContattoSedeEntita salvaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita);

    /**
     * Salva un deposito.
     *
     * @param deposito
     *            da salvare
     * @return <code>Deposito</code> salvato
     */
    Deposito salvaDeposito(Deposito deposito);

    /**
     *
     * @param entita
     * @return
     * @throws AnagraficheDuplicateException
     */
    Entita salvaEntita(Entita entita) throws AnagraficheDuplicateException;

    /**
     * esegue il salvataggio di {@link Filiale}.
     *
     * @param filiale
     * @return
     */
    Filiale salvaFiliale(Filiale filiale);

    /**
     *
     * @param rapportoBancarioAzienda
     * @return
     * @throws AnagraficaServiceException
     */
    RapportoBancarioAzienda salvaRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda);

    RapportoBancarioSedeEntita salvaRapportoBancarioSedeEntita(RapportoBancarioSedeEntita rapportoBancarioSedeEntita);

    /**
     * Esegue il salvataggio di {@link SedeAzienda}
     *
     * @param sedeAzienda
     * @return
     * @throws SedeEntitaPrincipaleAlreadyExistException
     */
    SedeAzienda salvaSedeAzienda(SedeAzienda sedeAzienda) throws SedeEntitaPrincipaleAlreadyExistException;

    /**
     *
     * @param sedeEntita
     * @return
     * @throws SedeEntitaPrincipaleAlreadyExistException
     * @throws AnagraficaServiceException
     * @throws SedeEntitaPrincipaleAlreadyExistException
     */
    SedeEntita salvaSedeEntita(SedeEntita sedeEntita) throws SedeEntitaPrincipaleAlreadyExistException;

    /**
     * Sostituisce i dati bancari ai {@link RapportoBancarioSedeEntita}.
     *
     * @param rapporti
     *            rapporti bancarii
     * @param banca
     *            banca di sostituzione
     * @param filiale
     *            filiale di sostituzione
     */
    void sostituisciDatiBancari(List<RapportoBancarioSedeEntita> rapporti, Banca banca, Filiale filiale);
}
