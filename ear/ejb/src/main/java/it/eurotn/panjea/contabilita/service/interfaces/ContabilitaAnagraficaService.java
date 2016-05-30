package it.eurotn.panjea.contabilita.service.interfaces;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.ModificaTipoAreaConDocumentoException;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.CodiceIvaCorrispettivo;
import it.eurotn.panjea.contabilita.domain.CodiceIvaPrevalente;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ContiBase;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;
import it.eurotn.panjea.contabilita.service.exception.TipoContoBaseEsistenteException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti;

@Remote
public interface ContabilitaAnagraficaService {

    /**
     * Cancella un codice iva corrispettivo.
     *
     * @param codiceIvaCorrispettivo
     *            {@link CodiceIvaCorrispettivo}
     *
     */
    void cancellaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo);

    /**
     * Cancella un {@link CodiceIvaPrevalente}.
     *
     * @param codiceIvaPrevalente
     *            {@link CodiceIvaPrevalente}
     */
    void cancellaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente);

    /**
     * Esegue la cancellazione dell'argomento {@link Conto}.
     *
     * @param conto
     *            {@link Conto}
     */
    void cancellaConto(Conto conto);

    /**
     * Cancella un {@link ContoBase}.
     *
     * @param contoBase
     *            {@link ContoBase} da cancellare
     */
    void cancellaContoBase(ContoBase contoBase);

    /**
     * Cancella una {@link ControPartita}.
     *
     * @param controPartita
     *            {@link ControPartita} da cancellare
     */
    void cancellaControPartita(ControPartita controPartita);

    /**
     * Esegue la cancellazione dell'argomento {@link Mastro}.
     *
     * @param mastro
     *            {@link Mastro}
     */
    void cancellaMastro(Mastro mastro);

    /**
     * Metodo che cancella il {@link RegistroIva}.
     *
     * @param registroIva
     *            registro da eliminare
     */
    void cancellaRegistroIva(RegistroIva registroIva);

    /**
     * Esegue la cancellazione dell'argomento {@link SottoConto}.
     *
     * @param sottoConto
     *            {@link SottoConto}
     */
    void cancellaSottoConto(SottoConto sottoConto);

    /**
     * Cancella una struttura contabile. Se ci sono delle contro partite legate alla struttura
     * contabile da cancellare vengono eliminate.
     *
     * @param strutturaContabile
     *            {@link StrutturaContabile} da cancellare
     * @throws ContabilitaException
     *             standardexception
     */
    void cancellaStrutturaContabile(StrutturaContabile strutturaContabile) throws ContabilitaException;

    /**
     * metodo che cancella {@link TipoAreaContabile}.
     *
     * @param tipoAreaContabile
     *            {@link TipoAreaContabile}
     */
    void cancellaTipoAreaContabile(TipoAreaContabile tipoAreaContabile);

    /**
     * cancella {@link TipoDocumentoBase}.
     *
     * @param tipoDocumentoBase
     *            {@link TipoDocumentoBase}
     */
    void cancellaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase);

    /**
     * Carica un {@link CodiceIvaPrevalente}. Se l'entità è nulla viene caricato il codice iva
     * predefinito per il tipo area contabile altrimenti viene caricato quello specifico per
     * l'entità. Se non esiste viene retituito un valore a null.
     *
     * @param tipoAreaContabile
     *            {@link TipoAreaContabile}
     * @param entita
     *            {@link Entita}
     * @return {@link CodiceIvaPrevalente}
     */
    CodiceIvaPrevalente caricaCodiceIvaPrevalente(TipoAreaContabile tipoAreaContabile, EntitaLite entita);

    /**
     * Carica tutti i codici iva corrispettivo per il tipo documento selezionato.
     *
     * @param tipoDocumento
     *            {@link TipoDocumento}
     * @return lista {@link CodiceIvaCorrispettivo}
     */
    List<CodiceIvaCorrispettivo> caricaCodiciIvaCorrispettivo(TipoDocumento tipoDocumento);

    /**
     * Carica il settings della contabilità. Se non esiste ne crea uno, lo salva e lo restituisce.
     *
     * @return <code>ContabilitaSettings</code> caricato
     */
    ContabilitaSettings caricaContabilitaSettings();

    /**
     * Carica tutti i conti base.
     *
     * @return {@link List} di {@link ContoBase} caricati
     * @throws ContabilitaException
     *             standard exception
     */
    List<ContoBase> caricaContiBase() throws ContabilitaException;

    /**
     * Restituisce l'istanza di {@link Conto} identificata dell'argomento id.
     *
     * @param idConto
     *            a caricare.
     * @return {@link Conto}
     * @throws ContabilitaException
     *             standard exeption
     */
    Conto caricaConto(Integer idConto) throws ContabilitaException;

    /**
     * Carica una contropartita.
     *
     * @param controPartita
     *            <code>ControPartita</code> da caricare
     * @return <code>ControPartita</code> caricata
     * @throws ContabilitaException
     *             standard exception
     */
    ControPartita caricaControPartita(ControPartita controPartita) throws ContabilitaException;

    /**
     * Carica tutte le contro partite in base all'area contabile.
     *
     * @param areaContabile
     *            {@link AreaContabile}
     * @return lista {@link ControPartita}
     * @throws ContabilitaException
     *             estandard exception
     */
    List<ControPartita> caricaControPartite(AreaContabile areaContabile) throws ContabilitaException;

    /**
     * Carica tutte le contro partite.
     *
     * @param tipoDocumento
     *            {@link TipoDocumento}
     * @param entita
     *            {@link EntitaLite}
     *
     * @return {@link List} di {@link ControPartita} caricate
     * @throws ContabilitaException
     *             standard exception
     */
    List<ControPartita> caricaControPartite(TipoDocumento tipoDocumento, EntitaLite entita) throws ContabilitaException;

    /**
     * Carica tutte le contro partite in base all'area contabile colcolandone l'importo se è stata
     * specificata la formula.
     *
     * @param areaContabile
     *            {@link AreaContabile}
     * @return lista {@link ControPartita}
     * @throws ContabilitaException
     *             standard exception
     * @throws FormulaException
     */
    List<ControPartita> caricaControPartiteConImporto(AreaContabile areaContabile)
            throws ContabilitaException, FormulaException;

    /**
     * Carica tutte le entità che hanno una struttura contabile personalizzata per il tipo documento
     * richiesto.
     *
     * @param tipoDocumento
     *            tipo documento
     *
     * @return entita caricate
     */
    List<EntitaLite> caricaEntitaConStrutturaContabile(TipoDocumento tipoDocumento);

    /**
     * Restituisce una collezione di tutte la classi di tipo <code>Mastro</code> .
     *
     * @return {@link List} {@link Mastro}
     * @throws ContabilitaException
     *             standard exception
     */
    List<Mastro> caricaMastri() throws ContabilitaException;

    /**
     * Restituisce l'istanza di <code>Mastro</code> identificata dall'argomento id.
     *
     * @param idMastro
     *            {@link Mastro}
     * @return {@link Mastro}
     * @throws ContabilitaException
     *             standard exception
     */
    Mastro caricaMastro(Integer idMastro) throws ContabilitaException;

    /**
     * Metodo che restituisce i {@link RegistroIva} per l'azienda loggata.
     *
     * @param valueSearch
     *            .
     * @param fieldSearch
     *            .
     *
     * @return List<RegistroIva>
     * @throws ContabilitaException
     *             standard exception
     *
     */
    List<RegistroIva> caricaRegistriIva(String fieldSearch, String valueSearch) throws ContabilitaException;

    /**
     * Metodo che restituisce il {@link RegistroIva} per l'id passato per parametro.
     *
     * @param id
     *            identificativo per caricare il {@link RegistroIva}
     * @return RegistroIva
     * @throws ContabilitaException
     *             standard exception
     */
    RegistroIva caricaRegistroIva(Integer id) throws ContabilitaException;

    /**
     * Restituisce l'istanza di <code>SottoConto</code> identificata dell'argomento id.
     *
     * @param idSottoConto
     *            id del sottoconto a caricare
     * @return {@link SottoConto}
     * @throws ContabilitaException
     *             standard exception
     */
    SottoConto caricaSottoConto(Integer idSottoConto) throws ContabilitaException;

    /**
     * Restituisce il {@link SottoConto} identificato.
     *
     * @param codiceEntita
     *            codice della entita a caricare
     * @return {@link SottoConto}
     * @throws ContabilitaException
     *             standard exception
     */
    SottoConto caricaSottoContoPerEntita(SottotipoConto sottotipoConto, Integer codiceEntita)
            throws ContabilitaException;

    /**
     * Carica la struttura contabile relativa al tipo documento e all'entità. Se l'entità è
     * <code>null</code> viene caricata la struttura contabile base del tipo documento.
     *
     * @param tipoDocumento
     *            {@link TipoDocumento}
     * @param entita
     *            {@link EntitaLite}
     * @return lista {@link StrutturaContabile}
     * @throws ContabilitaException
     *             standard exception
     */
    List<StrutturaContabile> caricaStrutturaContabile(TipoDocumento tipoDocumento, EntitaLite entita)
            throws ContabilitaException;

    /**
     * Metodo che restituisce i {@link TipoAreaContabile} per l'azienda loggata.
     *
     * @param valueSearch
     *            valore da filtrare
     * @param fieldSearch
     *            campo da filtrare
     *
     * @param loadTipiDocumentiDisabilitati
     *            nel caso si vuola caricare anche i documenti disabilitati
     * @return lista {@link TipoAreaContabile}
     * @throws ContabilitaException
     *             standard exception
     */
    List<TipoAreaContabile> caricaTipiAreaContabile(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentiDisabilitati) throws ContabilitaException;

    /**
     * carica la lista di {@link ContoBase} per azienda e restituisce una {@link Map} con key
     * {@link ETipoContoBase} e value {@link SottoConto}.
     *
     * @return
     * @throws ContabilitaException
     */
    ContiBase caricaTipiContoBase() throws ContabilitaException;

    /**
     * Metodo che restituisce l'elenco di {@link TipoDocumento} istanziati da
     * {@link TipoAreaContabile}.
     *
     * @return
     * @throws ContabilitaException
     */
    List<TipoDocumento> caricaTipiDocumentiContabili() throws ContabilitaException;

    /**
     * carica la lista di {@link TipoDocumentoBase} per azienda.
     *
     * @return
     * @throws ContabilitaException
     */
    List<TipoDocumentoBase> caricaTipiDocumentoBase() throws ContabilitaException;

    /**
     * carica la lista di {@link TipoDocumentoBase} per azienda e restituisce una {@link Map} con
     * key {@link TipoOperazioneTipoDocumento} e value {@link TipoAreaContabile} associato.
     *
     * @return
     * @throws ContabilitaException
     */
    Map<TipoOperazioneTipoDocumento, TipoAreaContabile> caricaTipiOperazione() throws ContabilitaException;

    /**
     * metodo che restituisce {@link TipoAreaContabile}.
     *
     * @param id
     * @return
     * @throws ContabilitaException
     */
    TipoAreaContabile caricaTipoAreaContabile(Integer id) throws ContabilitaException;

    /**
     * Metodo che restituisce il {@link TipoAreaContabile} per l'id di {@link TipoDocumento} passato
     * per parametro.
     *
     * @param tipoDocumento
     * @return
     * @throws ContabilitaException
     */
    TipoAreaContabile caricaTipoAreaContabilePerTipoDocumento(Integer idTipoDocumento) throws ContabilitaException;

    /**
     * carica {@link TipoAreaContabile} corrispondente a {@link TipoOperazione} richiesto, se non
     * esiste viene rilanciata l'eccezione {@link TipiDocumentoBaseException}.
     *
     * @return
     * @throws ContabilitaException
     * @throws TipoDocumentoBaseException
     *             TipoDocumentoBaseException
     */
    TipoAreaContabile caricaTipoAreaContabilePerTipoOperazione(TipoOperazioneTipoDocumento tipoOperazione)
            throws TipoDocumentoBaseException, ContabilitaException;

    /**
     * carica {@link TipoDocumentoBase}.
     *
     * @param idTipoDocumentoBase
     * @return
     * @throws ContabilitaException
     */
    TipoDocumentoBase caricaTipoDocumentoBase(Integer idTipoDocumentoBase) throws ContabilitaException;

    /**
     * Esegue la creazione del {@link SottoConto} per {@link Entita} identificata da codiceEntita.
     *
     * @param Entita
     *            entita per la quale creare il conto
     * @return sottoconto creato
     * @throws ContabilitaException
     *             eccezione generica
     */
    SottoConto creaSottoContoPerEntita(Entita entita) throws ContabilitaException;

    /**
     * Esegue la ricerca di tutti i {@link Conto}.
     *
     * @return List<Conto>
     */
    List<Conto> ricercaConti();

    /**
     * Esegue la ricerca dei {@link Conto} per {@link SottotipoConto} e per codiceConto.
     *
     * @param codiceConto
     *            definito secondo questa codifica MMM.CCC
     * @param sottoTipoConto
     * @return
     * @throws ContabilitaException
     */
    List<Conto> ricercaConti(String codiceConto, SottotipoConto sottoTipoConto) throws ContabilitaException;

    /**
     * Restituisce una collezione di {@link SottoConto}.
     *
     * @return
     * @throws ContabilitaException
     */
    List<SottoConto> ricercaSottoConti() throws ContabilitaException;

    /**
     * Restituisce una collezione di {@link SottoConto} filtrata per codice/descrizione, per
     * {@link SottotipoConto}.
     *
     * @param parametriRicercaSottoConti
     * @return
     * @throws ContabilitaException
     */
    List<SottoConto> ricercaSottoConti(ParametriRicercaSottoConti parametriRicercaSottoConti)
            throws ContabilitaException;

    /**
     * Restituisce una collezione di {@link SottoConto} filtrata per l'argomento <code>codice</code>
     * .
     *
     * @param codiceSottoConto
     *            definito secondo questa codifica MMM.CCC.SSSSSS, viene perci� ripartito e
     *            l'effettiva ricerca avviene sul codice del mastro, codice del conto ed infine
     *            sottoconto
     * @return
     * @throws ContabilitaException
     */
    List<SottoConto> ricercaSottoConti(String codiceSottoConto) throws ContabilitaException;

    /**
     * Restituisce una collezione di {@link SottoConto} ordinati per mastro conto e sottoconto.
     *
     * @return
     * @throws ContabilitaException
     */
    List<SottoConto> ricercaSottoContiOrdinati() throws ContabilitaException;

    /**
     * Restituisce una collezione di {@link SottoConto} filtrata per codice/descrizione, per
     * {@link SottotipoConto} ottimizzata.
     *
     * @param parametriRicercaSottoConti
     *            parametri di ricerca
     * @return lista di sottoconti
     */
    List<SottoConto> ricercaSottoContiSearchObject(ParametriRicercaSottoConti parametriRicercaSottoConti);

    /**
     * Salva un codice iva corrispettivo.
     *
     * @param codiceIvaCorrispettivo
     * @return
     */
    CodiceIvaCorrispettivo salvaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo);

    /**
     * Salva un <code>CodiceIvaPrevalente</code>.
     *
     * @param codiceIvaPrevalente
     * @return
     */
    CodiceIvaPrevalente salvaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente);

    /**
     * Salva un <code>ContabilitaSettings</code>.
     *
     * @return <code>ContabilitaSettings</code> salvato
     */
    ContabilitaSettings salvaContabilitaSettings(ContabilitaSettings contabilitaSettings);

    /**
     * Esegue il salvataggio dell'argomento <code>Conto</code> e lo restituisce aggiornato.
     *
     * @param conto
     * @return
     */
    Conto salvaConto(Conto conto);

    /**
     * Salva un <code>ContoBase</code>.
     *
     * @param contoBase
     *            <code>ContoBase</code> da salvare
     * @return <code>ContoBase</code> salvato
     */
    ContoBase salvaContoBase(ContoBase contoBase) throws TipoContoBaseEsistenteException, ContabilitaException;

    /**
     * Salva una <code>ControPartita</code>..
     *
     * @param controPartita
     *            <code>ControPartita</code> da salvare
     * @return <code>ControPartita</code> salvata
     */
    ControPartita salvaControPartita(ControPartita controPartita) throws FormulaException;

    /**
     * Esegue il salvataggio dell'argomento <code>Mastro</code> e lo restituisce aggiornato.
     *
     * @param mastro
     * @return
     */
    Mastro salvaMastro(Mastro mastro);

    /**
     * Esegue il salvataggio dell'argomento <code>RegistroIva</code> e lo restituisce aggiornato.
     *
     * @param registroIva
     *            il registroIva da salvare
     * @return RegistroIva aggiornato
     */
    RegistroIva salvaRegistroIva(RegistroIva registroIva);

    /**
     * Esegue il salvataggio dell'argomento <code>SottoConto</code> e lo restituisce aggiornato.
     *
     * @param sottoConto
     * @return
     */
    SottoConto salvaSottoConto(SottoConto sottoConto);

    /**
     * Salva una <code>StrutturaContabile</code>.
     *
     * @param strutturaContabile
     *            <code>StrutturaContabile</code> da salvare
     * @return <code>StrutturaContabile</code> salvata
     */
    StrutturaContabile salvaStrutturaContabile(StrutturaContabile strutturaContabile) throws FormulaException;

    /**
     * metodo che esegue il salvataggio di {@link TipoAreaContabile}.
     *
     * @param tipoAreaContabile
     * @return
     * @throws ModificaTipoAreaConDocumentoException
     */
    TipoAreaContabile salvaTipoAreaContabile(TipoAreaContabile tipoAreaContabile)
            throws ModificaTipoAreaConDocumentoException;

    /**
     * salva {@link TipoDocumentoBase}.
     *
     * @param tipoDocumentoBase
     * @return
     */
    TipoDocumentoBase salvaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase);
}
