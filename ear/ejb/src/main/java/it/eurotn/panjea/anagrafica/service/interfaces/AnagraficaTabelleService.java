package it.eurotn.panjea.anagrafica.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.CodiceIvaRicorsivoException;
import it.eurotn.panjea.anagrafica.domain.Carica;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.ContrattoSpesometro;
import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.anagrafica.domain.FormaGiuridica;
import it.eurotn.panjea.anagrafica.domain.Lingua;
import it.eurotn.panjea.anagrafica.domain.Mansione;
import it.eurotn.panjea.anagrafica.domain.NotaAnagrafica;
import it.eurotn.panjea.anagrafica.domain.TipoDeposito;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.LinguaAziendaleDeleteException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;

@Remote
public interface AnagraficaTabelleService {

    /**
     * Aggiunge il contratto al documento.
     *
     * @param contratto
     *            il contratto da aggiungere
     * @param documento
     *            il documento a cui aggiungere il contratto
     * @return il documento salvato con associato il contratto
     */
    Documento aggiungiContrattoADocumento(ContrattoSpesometro contratto, Documento documento);

    /**
     * Cancella una carica.
     *
     * @param carica
     *            carica da cancellare
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    void cancellaCarica(Carica carica) throws AnagraficaServiceException;

    /**
     * cancella un {@link CodiceIva}.
     *
     * @param codiceIva
     *            codice iva da cancellare
     */
    void cancellaCodiceIva(CodiceIva codiceIva);

    /**
     * Esegue la cancellazione di {@link ContrattoSpesometro}.
     *
     * @param contratto
     *            contratto da cancellare
     */
    void cancellaContratto(ContrattoSpesometro contratto);

    /**
     * Cancella una conversione unità di musura.
     *
     * @param conversioneUnitaMisura
     *            <code>ConversioneUnitaMisura</code> da cancellare
     */
    void cancellaConversioneUnitaMisura(ConversioneUnitaMisura conversioneUnitaMisura);

    /**
     * Cancella una forma giuridica.
     *
     * @param formaGiuridica
     *            Value Object della forma giuridica da eliminare
     * @throws AnagraficaServiceException
     *             eccezione generica
     *
     */
    void cancellaFormaGiuridica(FormaGiuridica formaGiuridica) throws AnagraficaServiceException;

    /**
     * Cancella una lingua gestita dall'azienda. Lancia un errore se cerco di cancellare la lingua
     * aziendale.
     *
     * @param lingua
     *            lingua da cancellare
     * @throws LinguaAziendaleDeleteException
     *             sollevata se si sta tentando di cancellare la lingua aziendale
     */
    void cancellaLingua(Lingua lingua) throws LinguaAziendaleDeleteException;

    /**
     * cancella {@link Mansione}.
     *
     * @param mansione
     *            mansione da cancellare
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    void cancellaMansione(Mansione mansione) throws AnagraficaServiceException;

    /**
     * Cancella una {@link NotaAnagrafica}.
     *
     * @param notaAnagrafica
     *            nota da cancellare
     */
    void cancellaNotaAnagrafica(NotaAnagrafica notaAnagrafica);

    /**
     * cancella un {@link TipoDeposito}.
     *
     * @param tipoDeposito
     *            tipoDeposito da cancellare
     */
    void cancellaTipoDeposito(TipoDeposito tipoDeposito);

    /**
     * Cancella {@link TipoSedeEntita}.
     *
     * @param tipoSedeEntita
     *            tipoSedeEntita
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    void cancellaTipoSedeEntita(TipoSedeEntita tipoSedeEntita) throws AnagraficaServiceException;

    /**
     * Cancella una unità di musura.
     *
     * @param unitaMisura
     *            <code>UnitaMisura</code> da cancellare
     */
    void cancellaUnitaMisura(UnitaMisura unitaMisura);

    /**
     * Cancella la zona geografica.
     *
     * @param zonaGeografica
     *            zonaGeografica
     */
    void cancellaZonaGeografica(ZonaGeografica zonaGeografica);

    /**
     * Recupera la lista completa di Cariche.
     *
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return Lista di <code>CaricaVO</code>
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<Carica> caricaCariche(String fieldSearch, String valueSearch)
            throws AnagraficaServiceException;

    /**
     * Carica il codice iva con l'id scelto.
     *
     * @param id
     *            l'id del codice iva da caricare
     * @return CodiceIva
     */
    CodiceIva caricaCodiceIva(Integer id);

    /**
     * Carica il codice iva partendo dal codice identificativo del software Europa.
     *
     * @param codiceEuropa
     *            il codice identificativo del software Europa
     * @return CodiceIva
     * @throws ObjectNotFoundException
     *             oggetto non trovato
     */
    CodiceIva caricaCodiceIva(String codiceEuropa) throws ObjectNotFoundException;

    /**
     * carica una lista di codiciIva.
     *
     * @param codice
     *            codice o parte del codice da ricercare
     * @return List<CodiceIva>
     * @throws ContabilitaException
     */
    List<CodiceIva> caricaCodiciIva(String codice);

    /**
     * Carica tutti i contratti o i contratti dell'entità.
     *
     * @param entita
     *            l'entità di cui caricare i contratti o null
     * @return List<ContrattoSpesometro>
     */
    List<ContrattoSpesometro> caricaContratti(EntitaLite entita);

    /**
     * Carica il contratto identificato dall'id passato.
     *
     * @param idContratto
     *            l'id del contratto da caricare
     * @return ContrattoSpesometro
     */
    ContrattoSpesometro caricaContratto(Integer idContratto);

    /**
     * Carica il contratto identificato dall'id passato.
     *
     * @param idContratto
     *            l'id del contratto da caricare
     * @param loadCollection
     *            indica se caricare le collection lazy per il contratto
     * @return ContrattoSpesometro
     */
    ContrattoSpesometro caricaContratto(Integer idContratto, boolean loadCollection);

    /**
     * Carica, se esiste, la conversione unità di misura tra quella di origine e destinazione.
     *
     * @param unitaMisuraOrigine
     *            unita misura origine
     * @param unitaMisuraDestinazione
     *            unita misura destinazione
     * @return la conversioneUnitaMisura o null
     */
    ConversioneUnitaMisura caricaConversioneUnitaMisura(String unitaMisuraOrigine,
            String unitaMisuraDestinazione);

    /**
     * Carica tutte le conversioni unità di misura.
     *
     * @return list di {@link ConversioneUnitaMisura} caricate
     */
    List<ConversioneUnitaMisura> caricaConversioniUnitaMisura();

    /**
     * Carica i documenti collegati al contratto scelto.
     *
     * @param idContratto
     *            l'id del contratto di cui caricare i documenti
     * @return la lista di documenti per il contratto scelto
     */
    List<Documento> caricaDocumentiContratto(Integer idContratto);

    /**
     * Carica una forma giuridica.
     *
     * @param idFormaGiuridica
     *            Codice della forma giuridica da caricare
     * @return Value Object della forma giuridica caricata
     * @throws AnagraficaServiceException
     *             eccezione generica
     * @throws ObjectNotFoundException
     *             oggetto non trovato
     *
     */
    FormaGiuridica caricaFormaGiuridica(Integer idFormaGiuridica)
            throws AnagraficaServiceException, ObjectNotFoundException;

    /**
     * Recupera la {@link List} di {@link FormaGiuridica}.
     *
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return List<FormaGiuridica>
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<FormaGiuridica> caricaFormeGiuridiche(String fieldSearch, String valueSearch)
            throws AnagraficaServiceException;

    /**
     * Carica la lingua scelta.
     *
     * @param lingua
     *            la lingua da caricare
     * @return Lingua
     */
    Lingua caricaLingua(Lingua lingua);

    /**
     * Carica le lingue per l'azienda corrente.
     *
     * @return List<Lingua>
     */
    List<Lingua> caricaLingue();

    /**
     * Carica la {@link List} di {@link Mansione}.
     *
     * @param descrizione
     *            .
     * @return List<Mansione>
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<Mansione> caricaMansioni(String descrizione) throws AnagraficaServiceException;

    /**
     * Carica tutte le note anagrafica.
     *
     * @return note caricate
     */
    List<NotaAnagrafica> caricaNoteAnagrafica();

    /**
     * Carica i {@link TipoDeposito}.
     *
     * @return List<TipoDeposito>
     */
    List<TipoDeposito> caricaTipiDepositi();

    /**
     * Carica {@link List} di {@link TipoSedeEntita}.
     *
     * @param codice
     *            codice da filtrare
     *
     *
     * @return List<TipoSedeEntita>
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<TipoSedeEntita> caricaTipiSede(String codice) throws AnagraficaServiceException;

    /**
     * Carica la {@link List} di {@link TipoSedeEntita} con attributo sedePrincipale = false.
     *
     * @return List<TipoSedeEntita>
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    List<TipoSedeEntita> caricaTipiSedeSecondari() throws AnagraficaServiceException;

    /**
     * Carica tutte le unità di misura.
     *
     * @return <code>List</code> di <code>UnitaMisura</code> caricate
     */
    List<UnitaMisura> caricaUnitaMisura();

    /**
     * Carica tutte le unità di misura filtrare per descrizione.
     *
     * @param codice
     *            parte del codice um
     * @return <code>List</code> di <code>UnitaMisura</code> caricate
     */
    List<UnitaMisura> caricaUnitaMisura(String codice);

    /**
     * Carica una unità di misura.
     *
     * @param unitaMisura
     *            <code>UnitaMisura</code> da caricare
     * @return <code>UnitaMisura</code> caricata
     */
    UnitaMisura caricaUnitaMisura(UnitaMisura unitaMisura);

    /**
     * Carica la lista di {@link ZonaGeografica}.
     *
     * @param valueSearch
     *            .
     * @param fieldSearch
     *            .
     *
     * @return List<ZonaGeografica>
     */
    List<ZonaGeografica> caricaZoneGeografiche(String fieldSearch, String valueSearch);

    /**
     * Rimuove il contratto dal documento.
     *
     * @param documento
     *            il documento dal quale rimuovere il contratto
     */
    void rimuovContrattoDaDocumento(Documento documento);

    /**
     * Rende persistente l'istanza di <code>CaricaVO</code> passata per parametro.
     *
     * @param carica
     *            la carica da caricare
     * @return carica salvata
     */
    Carica salvaCarica(Carica carica);

    /**
     * Salva il Codice Iva.
     *
     * @param codiceIva
     *            il codice iva da salvare
     * @return il codice iva salvato
     * @throws CodiceIvaRicorsivoException
     *             se il codice iva ha come codice iva collegato se stesso
     */
    CodiceIva salvaCodiceIva(CodiceIva codiceIva) throws CodiceIvaRicorsivoException;

    /**
     * Esegue il salvataggio di {@link ContrattoSpesometro}.
     *
     * @param contratto
     *            contrattoSpesometro da salvare
     * @return contrattoSpesometro salvato
     */
    ContrattoSpesometro salvaContratto(ContrattoSpesometro contratto);

    /**
     * Salva conversioneUnitaMisura.
     *
     * @param conversioneUnitaMisura
     *            la conversioneUnitaMisura da salvare
     * @return conversioneUnitaMisura salvata
     */
    ConversioneUnitaMisura salvaConversioneUnitaMisura(
            ConversioneUnitaMisura conversioneUnitaMisura);

    /**
     * Salva una forma giuridica.
     *
     * @param formaGiuridica
     *            la forma giuidica da salvare
     * @return FormaGiuridica
     */
    FormaGiuridica salvaFormaGiuridica(FormaGiuridica formaGiuridica);

    /**
     * Salva una lingua gestita dall'azienda.
     *
     * @param lingua
     *            lingua da salvare
     * @return Lingua
     */

    Lingua salvaLingua(Lingua lingua);

    /**
     * Salva {@link Mansione}.
     *
     * @param mansione
     *            la mansione da salvare
     * @return Mansione
     */
    Mansione salvaMansione(Mansione mansione);

    /**
     * Salva una {@link NotaAnagrafica}.
     *
     * @param notaAnagrafica
     *            nota da salvare
     * @return nota salvata
     */
    NotaAnagrafica salvaNotaAnagrafica(NotaAnagrafica notaAnagrafica);

    /**
     * Salva il TipoDeposito.
     *
     * @param tipoDeposito
     *            il tipo deposito da salvare
     * @return TipoDeposito
     */
    TipoDeposito salvaTipoDeposito(TipoDeposito tipoDeposito);

    /**
     * Salva {@link TipoSedeEntita}.
     *
     * @param tipoSedeEntita
     *            il tipoSedeEntita da salvare
     * @return TipoSedeEntita
     * @throws AnagraficaServiceException
     *             eccezione generica
     */
    TipoSedeEntita salvaTipoSedeEntita(TipoSedeEntita tipoSedeEntita)
            throws AnagraficaServiceException;

    /**
     * Salva una unità di misura.
     *
     * @param unitaMisura
     *            <code>UnitaMisura</code> da salvare
     * @return <code>UnitaMisura</code> salvata
     */
    UnitaMisura salvaUnitaMisura(UnitaMisura unitaMisura);

    /**
     * Salva la ZonaGeografica.
     *
     * @param zonaGeografica
     *            la zona da salvare
     * @return ZonaGeografica
     */
    ZonaGeografica salvaZonaGeografica(ZonaGeografica zonaGeografica);

}
