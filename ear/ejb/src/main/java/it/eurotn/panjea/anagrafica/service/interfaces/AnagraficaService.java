package it.eurotn.panjea.anagrafica.service.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.Remote;

import it.eurotn.dao.exception.DuplicateKeyObjectException;
import it.eurotn.dao.exception.StaleObjectStateException;
import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.agenti.domain.Agente;
import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.CambioValuta;
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
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.VettoreLite;
import it.eurotn.panjea.anagrafica.manager.depositi.ParametriRicercaDepositi;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficheDuplicateException;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;
import it.eurotn.panjea.anagrafica.service.exception.ContattoOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeAnagraficaOrphanException;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.anagrafica.service.exception.TipoSedeEntitaNonTrovataException;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.anagrafica.util.RubricaDTO;
import it.eurotn.panjea.anagrafica.util.parametriricerca.ParametriRicercaEntita;
import it.eurotn.panjea.contabilita.domain.CausaleRitenutaAcconto;
import it.eurotn.panjea.contabilita.domain.ContributoPrevidenziale;
import it.eurotn.panjea.contabilita.domain.Prestazione;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;

@Remote
public interface AnagraficaService {

    /**
     * Aggiorna tutti i tassi di cambio per le valute aziendali..
     *
     * @param byteArray
     *            array contenente il file per l'aggiornamento delle valute
     */
    void aggiornaTassi(byte[] byteArray);

    /**
     * Esegue la variazione della {@link SedeAzienda} principale.
     *
     * @param nuovaSedeAziendaPrincipaleAzienda
     * @param tipoSedeSostitutivaEntita
     * @throws AnagraficaServiceException
     */
    void cambiaSedePrincipaleAzienda(SedeAzienda nuovaSedeAziendaPrincipaleAzienda,
            TipoSedeEntita tipoSedeSostitutivaEntita) throws AnagraficaServiceException;

    /**
     * Esegue la varizione della {@link SedeEntita} principale della sua {@link Entita}.
     *
     * @param sedeEntita
     * @param tipoSedeEntita
     *            {@link TipoSedeEntita} secondaria da assegnare alla precedente {@link SedeEntita} principale
     */
    void cambiaSedePrincipaleEntita(SedeEntita sedeEntita, TipoSedeEntita tipoSedeEntita);

    /**
     * Esegue la cancellazione di {@link Banca}.
     *
     * @param banca
     * @throws AnagraficaServiceException
     */
    void cancellaBanca(Banca banca) throws AnagraficaServiceException;

    /**
     * Cancella un cambio valuta. <br/>
     * <B>Non è possibile cancellare tutti i cambi di una valuta. Si deve cancellare la valuta</B>
     *
     * @param cambioValuta
     *            cambio da cancellare
     */
    void cancellaCambioValuta(CambioValuta cambioValuta);

    /**
     *
     * @param categoriaEntita
     */
    void cancellaCategoriaEntita(CategoriaEntita categoriaEntita);

    /**
     * Esegue la cancellazione di tutti i <code>ContattoSedeEntita</code> e di tutti i <code>Contatto</code>
     *
     * @param idEntita
     */
    void cancellaContattiPerEntita(Entita entita);

    /**
     * Esegue la cancellazione di tutti i <code>ContattoSedeEntita</code> e di tutti i <code>Contatto</code>
     *
     * @param idSedeEntita
     */
    void cancellaContattiPerSedeEntita(SedeEntita sedeEntita);

    /**
     * esegue la cancellazione di <code>Contatto</code>
     *
     * @param contatto
     */
    void cancellaContatto(Contatto contatto);

    /**
     * Esegue la cancellazione di <code>MansioneSede</code> e verifica in caso di deleteOrphan==true se il contatto
     * risulta essere orfano ed eventualmente rilancia una <code>ContattoOrphanException</code>
     *
     * @param mansioneSede
     * @throws AnagraficaServiceException
     * @throws ContattoOrphanException
     */
    void cancellaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita)
            throws AnagraficaServiceException, ContattoOrphanException;

    /**
     * Cancella un deposito
     *
     * @param depositoVO
     *            Value Object del deposito da cancellare.
     * @throws AnagraficaServiceException
     */
    void cancellaDeposito(Deposito deposito);

    /**
     * Esegue la cancellazione di <code>Entita</code>
     *
     * @param idEntita
     * @throws AnagraficaServiceException
     * @throws ObjectNotFoundException
     */
    void cancellaEntita(Entita entita)
            throws AnagraficaServiceException, it.eurotn.dao.exception.ObjectNotFoundException;

    /**
     * esegue la cancellazione di {@link Filiale}
     *
     * @param filiale
     * @throws AnagraficaServiceException
     */
    void cancellaFiliale(Filiale filiale) throws AnagraficaServiceException;

    /**
     *
     * @param rapportoBancario
     *            rapporto bancario azienda da cancellare
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    void cancellaRapportoBancario(RapportoBancarioAzienda rapportoBancario) throws AnagraficaServiceException;

    /**
     * Cancella il rapporto bancario associato alla sede do un entità.
     *
     * @param rapportoBancario
     *            rapporto bancario da cancellare
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    void cancellaRapportoBancarioSedeEntita(RapportoBancarioSedeEntita rapportoBancario)
            throws AnagraficaServiceException;

    /**
     * Esegue la cancellazione di {@link SedeAzienda}
     *
     * @param sedeAzienda
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws SedeAnagraficaOrphanException
     * @throws ObjectNotFoundException
     */
    void cancellaSedeAzienda(SedeAzienda sedeAzienda) throws AnagraficaServiceException, SedeAnagraficaOrphanException,
            it.eurotn.dao.exception.ObjectNotFoundException;

    /**
     * Esegue la cancellazione di {@link SedeAzienda} ed effettua la cancellazione delle {@link SedeAnagrafica} rimaste
     * senza associazione
     *
     * @param sedeAziendaVO
     * @param deleteOrphan
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws SedeAnagraficaOrphanException
     * @throws ObjectNotFoundException
     */
    void cancellaSedeAzienda(SedeAzienda sedeAzienda, boolean deleteOrphan) throws AnagraficaServiceException,
            SedeAnagraficaOrphanException, it.eurotn.dao.exception.ObjectNotFoundException;

    /**
     * Esegue la cancellazione di {@link SedeEntita} e esegue la cancellazione di {@link SedeAnagrafica} rimaste senza
     * associazione
     *
     * @param sedeEntita
     * @param deleteOrphan
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws SedeAnagraficaOrphanException
     */
    void cancellaSedeEntita(SedeEntita sedeEntita, boolean deleteOrphan)
            throws AnagraficaServiceException, SedeAnagraficaOrphanException;

    /**
     * Cancella una valuta azienda e lo storico dei cambi.
     *
     * @param valutaAzienda
     *            valuta da cancellare.
     */
    void cancellaValutaAzienda(ValutaAzienda valutaAzienda);

    /**
     * Carica tutti gli agenti che non sono ancora associati ad un capo area.
     *
     * @return agenti caricati
     */
    List<Agente> caricaAgentiSenzaCapoArea();

    /**
     * recupera {@link Anagrafica} identificata dall'argomento idAnagrafica
     *
     * @param anagraficaLite
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    Anagrafica caricaAnagrafica(Integer idAnagrafica) throws AnagraficaServiceException;

    /**
     *
     * @return tutte le anagrafiche aziendali con "l'albero" completo (entità,sedi,contatti)
     */
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
    List<AnagraficaLite> caricaAnagraficheSearchObject(String codice, String denominazione);

    /**
     * Carica l'azienda corrente restituendo una AziendaLite contenente le informazioni principali dell'azienda.
     *
     * @return AziendaLite
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    AziendaLite caricaAzienda() throws AnagraficaServiceException;

    /**
     * Metodo usato nei reports.
     *
     * @param parametri
     * @return
     */
    AziendaLite caricaAzienda(Map<Object, Object> parametri);

    /**
     * Carica l'azienda dal codice. Metodo usato per l'importazione.
     *
     * @param codiceAzienda
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    AziendaLite caricaAzienda(String codiceAzienda) throws AnagraficaServiceException;

    /**
     * Carica l'azienda dal codice, il parametro loadFull stabilisce se eseguire il load delle classi associate ad
     * {@link AziendaLite}
     *
     * @param codiceAzienda
     * @param loadSede
     * @param loadValuta
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    AziendaLite caricaAzienda(String codiceAzienda, boolean loadSede) throws AnagraficaServiceException;

    /**
     * recupera {@link Azienda} e {@link SedeAzienda} principale e li restituisce all'interno di
     * {@link AziendaAnagraficaDTO}
     *
     * @param codice
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws ObjectNotFoundException
     */
    AziendaAnagraficaDTO caricaAziendaAnagrafica(String codice)
            throws AnagraficaServiceException, it.eurotn.dao.exception.ObjectNotFoundException;

    AziendaAnagraficaDTO caricaAziendaAnagraficaCorrente() throws AnagraficaServiceException;

    /**
     * recupera {@link Azienda} per codice
     *
     * @param codice
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    Azienda caricaAziendaByCodice(String codice) throws AnagraficaServiceException;

    /**
     * Restiuisce {@link List} di {@link AziendaLite}
     *
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<AziendaLite> caricaAziende() throws AnagraficaServiceException;

    /**
     * carica {@link Banca} identificata da idBanca
     *
     * @param idBanca
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    Banca caricaBanca(Integer idBanca) throws AnagraficaServiceException;

    /**
     * Carica la {@link List} di {@link Banca}
     *
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<Banca> caricaBanche(String fieldSearch, String valueSearch) throws AnagraficaServiceException;

    /**
     * Carica una valuta con il cambio valido per la data di riferimento.<br/>
     * Se a quella data non esiste il cambio viene presa la più vicina
     *
     * @param codiceValuta
     *            codice valuta richiesta
     * @param date
     *            data di riferimento
     * @return cambio della valuta alla data richiesta.
     * @throws CambioNonPresenteException
     *             lanciata quando non è presente il cambio.
     */
    CambioValuta caricaCambioValuta(String codiceValuta, Date date) throws CambioNonPresenteException;

    /**
     * Carica le valute con il cambio valido per la data di riferimento.<br/>
     * Se a quella data non esiste il cambio viene presa la più vicina
     *
     * @param date
     *            data di riferimento
     * @return cambiValuta alla data richiesta per tutte le valute aziendali
     */
    List<CambioValuta> caricaCambiValute(Date date);

    /**
     * Carica tutti i cambi della valuta per l'anno di riferimento.
     *
     * @param codiceValuta
     *            codice valuta
     * @param anno
     *            anno di riferimento
     * @return storico dei cambi per l'anno e la valuta voluti.
     */
    List<CambioValuta> caricaCambiValute(String codiceValuta, int anno);

    /**
     * carica la lista di categorie per la entita.
     *
     * @param idEntita
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<CategoriaEntita> caricaCategoriaEntitaByEntita(Integer idEntita) throws AnagraficaServiceException;

    /**
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<CategoriaEntita> caricaCategorieEntita(String fieldSearch, String valueSearch)
            throws AnagraficaServiceException;

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

    /**
     *
     * @return codici delle valute abilitate per l'azienda
     */
    List<String> caricaCodiciValuteAzienda();

    /**
     * Carica indistintamente tutti i <code>Contatto</code> di <code>Entita</code>
     *
     * @param idEntita
     * @return
     */
    List<Contatto> caricaContattiPerEntita(Entita entita);

    /**
     * Restituisce tutte le mansioni di una data Entita
     *
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<ContattoSedeEntita> caricaContattiSedeEntitaPerEntita(Entita entita) throws AnagraficaServiceException;

    /**
     * Restituisce tutte le mansioni di una data SedeEntita
     *
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<ContattoSedeEntita> caricaContattiSedeEntitaPerSedeEntita(SedeEntita sedeEntita)
            throws AnagraficaServiceException;

    /**
     * restituisce {@link Contatto} identificato da idContatto
     *
     * @param idContatto
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    Contatto caricaContatto(java.lang.Integer idContatto) throws AnagraficaServiceException;

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
     * @throws AnagraficaServiceException
     *             eccezione generica eccezione generica.
     */
    List<Deposito> caricaDepositi() throws AnagraficaServiceException;

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
     *             eccezione generica
     */
    List<Deposito> caricaDepositi(SedeAzienda sedeAzienda) throws AnagraficaServiceException;

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
     *             eccezione generica
     */
    Deposito caricaDeposito(Integer idDeposito) throws AnagraficaServiceException;

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
    List<EntitaLite> caricaEntita(AnagraficaLite anagraficaLite);

    /**
     * restituisce Entita
     *
     * @param entita
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    Entita caricaEntita(Entita entita, Boolean caricaLazy) throws AnagraficaServiceException;

    /**
     * restituisce un {@link Entita} attraverso il corrispondente oggetto lite
     *
     * @param entitaLite
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    Entita caricaEntita(EntitaLite entitaLite, Boolean caricaLazy) throws AnagraficaServiceException;

    /**
     * carica la liste de categorie della entita.
     *
     * @param idEntita
     * @return
     * @throws AnagraficaServiceException
     */
    List<SedeEntita> caricaEntitaByCategorie(List<CategoriaEntita> categorie);

    /**
     * recupera {@link EntitaLite}
     *
     * @param entitaLite
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    EntitaLite caricaEntitaLite(EntitaLite entitaLite) throws AnagraficaServiceException;

    /**
     * carica {@link Filiale} identificato da idFiliale
     *
     * @param idFiliale
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    Filiale caricaFiliale(Integer idFiliale) throws AnagraficaServiceException;

    /**
     * Carica la {@link List} di {@link Filiale}
     *
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<Filiale> caricaFiliali() throws AnagraficaServiceException;

    /**
     * Carica {@link List} di {@link Filiale} associate all'argomento {@link Banca}
     *
     * @param banca
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<Filiale> caricaFiliali(Banca banca, String fieldSearch, String valueSearch) throws AnagraficaServiceException;

    /**
     * Carica tutte le prestazioni.
     *
     * @return prestazioni caricate
     */
    List<Prestazione> caricaPrestazioni();

    /**
     * Recupera {@link List} di {@link RapportoBancarioAzienda}.
     *
     * @param filter
     * @param valueSearch
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<RapportoBancarioAzienda> caricaRapportiBancariAzienda(String fieldSearch, String valueSearch)
            throws AnagraficaServiceException;

    /**
     * Carica i rapporti bancari per l'entità interessata.
     *
     * @param idSedeEntita
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita)
            throws AnagraficaServiceException;

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
    List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntita(Integer idSedeEntita, boolean ignoraEredita)
            throws AnagraficaServiceException;

    /**
     * Carica i rapporti bancari per la sede principale dell'entità.
     *
     * @param valueSearch
     *            .
     * @param fieldSearch
     *            .
     *
     * @param idSedeEntita
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<RapportoBancarioSedeEntita> caricaRapportiBancariSedeEntitaPrincipale(String fieldSearch, String valueSearch,
            Integer idEntita) throws AnagraficaServiceException;

    /**
     * Recupera {@link RapportoBancarioAzienda} identificato da idRapportoBancario
     *
     * @param idRapportoBancario
     * @return
     */
    RapportoBancarioAzienda caricaRapportoBancario(Integer idRapportoBancario, boolean initializeLazy);

    RapportoBancarioSedeEntita caricaRapportoBancarioSedeEntita(Integer idRapportoBancario)
            throws AnagraficaServiceException;

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
     * @param vettore
     *            carica le sedi che hanno il vettore richiesto
     * @return riepilogo caricato
     */
    List<RiepilogoSedeEntitaDTO> caricaRiepilogoSediEntitaByVettore(VettoreLite vettore);

    /**
     * Recupera {@link SedeAzienda}
     *
     * @param idSedeAzienda
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws ObjectNotFoundException
     */
    SedeAzienda caricaSedeAzienda(Integer idSedeAzienda) throws AnagraficaServiceException;

    /**
     * restituisce {@link SedeEntita} identificata dall'argomento idSedeEntita
     *
     * @param idSedeEntita
     * @return
     * @throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException
     * @throws ObjectNotFoundException
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
     * Carica la sede predefinita per l'entità
     *
     * @param entita
     *            entità della sede
     * @return sede predefinita dell'entità
     * @throws AnagraficaServiceException
     */
    SedeEntita caricaSedePredefinitaEntita(Entita entita) throws AnagraficaServiceException;

    /**
     * Recupera la {@link SedeAzienda} principale di {@link Azienda}
     *
     * @param azienda
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws ObjectNotFoundException
     */
    SedeAzienda caricaSedePrincipaleAzienda(Azienda azienda) throws AnagraficaServiceException;

    /**
     * restituisce {@link SedeEntita} principale per {@link Entita} identificata da idEntita
     *
     * @param entita
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    SedeEntita caricaSedePrincipaleEntita(Entita entita) throws AnagraficaServiceException;

    SedeEntita caricaSedePrincipaleEntita(Integer idEntita) throws AnagraficaServiceException;

    /**
     * Recupera la {@link List} di {@link SedeAnagrafica} associate ad {@link Anagrafica}
     *
     * @param idAnagrafica
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<SedeAnagrafica> caricaSediAnagrafica(Anagrafica anagrafica) throws AnagraficaServiceException;

    /**
     * Recupera la {@link List} di {@link SedeAnagrafica} associate ad {@link Azienda}
     *
     * @param azienda
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<SedeAnagrafica> caricaSediAnagraficaAzienda(Azienda azienda) throws AnagraficaServiceException;

    Set<SedeEntita> caricaSediAssociate(AgenteLite agente);

    /**
     *
     * @return lista sedi aziendali
     * @throws AnagraficaServiceException
     */
    List<SedeAzienda> caricaSediAzienda() throws AnagraficaServiceException;

    /**
     * Recupera la {@link List} di {@link SedeAzienda}
     *
     * @param azienda
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<SedeAzienda> caricaSediAzienda(Azienda azienda) throws AnagraficaServiceException;

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
     *            filtro
     * @param idEntita
     *            id dell'entità
     * @param caricamentoSediEntita
     *            tipo di caricamento
     * @return sedi caricate
     * @throws AnagraficaServiceException
     *             eccezione generica AnagraficaServiceException
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
     *
     * @param idEntita
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<SedeEntita> caricaSediEntitaNonEreditaDatiComerciali(Integer idEntita) throws AnagraficaServiceException;

    /**
     * Recupera la {@link List} di {@link SedeAzienda} secondarie
     *
     * @param azienda
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<SedeAzienda> caricaSediSecondarieAzienda(Azienda azienda) throws AnagraficaServiceException;

    /**
     * resituisce {@link List} di {@link SedeEntita} secondarie associate ad {@link Entita} identificata da idEntita
     *
     * @param entita
     * @return
     * @throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException
     */
    List<SedeEntita> caricaSediSecondarieEntita(Entita entita)
            throws it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;

    /**
     * Carica {@link ValutaAzienda} attraverso il suo codice valuta.
     *
     * @param codiceValuta
     *            codice della valuta da caricare
     * @return {@link ValutaAzienda} identificata da codiceValuta
     */
    ValutaAzienda caricaValutaAzienda(String codiceValuta);

    /**
     *
     * @return valutaAzienda
     */
    ValutaAzienda caricaValutaAziendaCorrente();

    /**
     *
     * @return carica le valute dell'azienda.
     */
    List<ValutaAzienda> caricaValuteAzienda();

    /**
     * Conferma un cliente potenziale convertendolo in cliente, nel processo devo risalvare il codice entita' e
     * associare il conto
     *
     * @param idEntita
     * @return
     */
    Entita confermaClientePotenziale(Integer idEntita);

    /**
     *
     * @param idEntita
     * @return
     * @throws TipoSedeEntitaNonTrovataException
     */
    SedeEntita creaSedeEntitaGenerica(Integer idEntita) throws TipoSedeEntitaNonTrovataException;

    /**
     * Restituisce le variabili valorizzate in base all'entity di riferimento per la generazione del codice.
     *
     * @param entity
     *            entity
     * @return variabili
     */
    Map<String, String> creaVariabiliCodice(EntityBase entity);

    /**
     * Genera il codice in base al pattern specificato e usando i valori delle variabili contenute nella mappa.
     *
     * @param pattern
     *            pattern di generazione
     * @param mapVariabili
     *            mappa delle variabili
     * @return codice generato
     */
    String generaCodice(String pattern, Map<String, String> mapVariabili);

    /**
     * Restituisce tutte le variabili che possono essere utilizzate per la generazione del codice.
     *
     * @return lista di variabili
     */
    String[] getVariabiliPatternCodiceDocumento();

    /**
     * Esegue la ricerca tra tutti i Contatto esistenti filtrando per i valori contenuti nella <code>Map</code>
     * parametri
     *
     * @param parametri
     * @return List<MansioneSede>
     */
    List<Contatto> ricercaContatti(java.util.Map<String, Object> parametri);

    /**
     * Esegue la ricerca tra tutti i Contatto di un <code>Entita</code> filtrando per i valori contenuti nella
     * <code>Map</code> parametri
     *
     * @param entita
     * @param parametri
     * @return List<MansioneSede>
     */
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
     * esegue il salvataggio di {@link Azienda}
     *
     * @param azienda
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    Azienda salvaAzienda(Azienda azienda) throws AnagraficaServiceException;

    /**
     * Esegue il salvataggio di {@link Azienda} e {@link SedeAzienda}
     *
     * @param aziendaAnagraficaDTO
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws DuplicateKeyObjectException
     * @throws StaleObjectStateException
     * @throws SedeEntitaPrincipaleAlreadyExistException
     */
    AziendaAnagraficaDTO salvaAziendaAnagrafica(AziendaAnagraficaDTO aziendaAnagraficaDTO)
            throws AnagraficaServiceException, DuplicateKeyObjectException, StaleObjectStateException,
            SedeEntitaPrincipaleAlreadyExistException;

    /**
     * salva {@link Banca}
     *
     * @return
     */
    Banca salvaBanca(Banca banca);

    /**
     * Salva un cambio valuta.
     *
     * @param cambioValuta
     *            cambio da salvare
     * @return cambio salvato
     */
    CambioValuta salvaCambioValuta(CambioValuta cambioValuta);

    /**
     *
     * @param categoriaEntita
     * @return
     */
    CategoriaEntita salvaCategoriaEntita(CategoriaEntita categoriaEntita);

    /**
     * Esegue il salvataggio di un istanza di <code>Contatto</code>
     *
     * @param contatto
     * @return
     */
    Contatto salvaContatto(Contatto contatto);

    /**
     * Esegue il salvataggio di mansione sede {@link ContattoSedeEntita}
     *
     * @param contattoSedeEntita
     * @return
     */
    ContattoSedeEntita salvaContattoSedeEntita(ContattoSedeEntita contattoSedeEntita);

    /**
     * Salva un deposito
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
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws AnagraficaServiceException
     * @throws AnagraficheDuplicateException
     *             eccezione generica
     */
    Entita salvaEntita(Entita entita) throws AnagraficaServiceException, AnagraficheDuplicateException;

    /**
     * esegue il salvataggio di {@link Filiale}
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
     *             eccezione generica
     */
    RapportoBancarioAzienda salvaRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda)
            throws AnagraficaServiceException;

    RapportoBancarioSedeEntita salvaRapportoBancarioSedeEntita(RapportoBancarioSedeEntita rapportoBancarioSedeEntita)
            throws AnagraficaServiceException;

    /**
     * Esegue il salvataggio di {@link SedeAzienda}
     *
     * @param sedeAzienda
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws SedeEntitaPrincipaleAlreadyExistException
     */
    SedeAzienda salvaSedeAzienda(SedeAzienda sedeAzienda)
            throws AnagraficaServiceException, SedeEntitaPrincipaleAlreadyExistException;

    /**
     *
     * @param sedeEntita
     * @return
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws SedeEntitaPrincipaleAlreadyExistException
     */
    SedeEntita salvaSedeEntita(SedeEntita sedeEntita)
            throws AnagraficaServiceException, SedeEntitaPrincipaleAlreadyExistException;

    /**
     * Salva una valuta azienda.
     *
     * @param valutaAzienda
     *            valuta azienda da salvare
     * @return valutaazienda salvata
     */
    ValutaAzienda salvaValutaAzienda(ValutaAzienda valutaAzienda);

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

    List<?> test();

}
