package it.eurotn.panjea.contabilita.rich.bd;

import java.util.List;
import java.util.Map;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.CodiceIvaCorrispettivo;
import it.eurotn.panjea.contabilita.domain.CodiceIvaPrevalente;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.Conto;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.ContoBase;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.Mastro;
import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSottoConti;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

public interface IContabilitaAnagraficaBD {

    /**
     * Cancella un codice iva corrispettivo
     *
     * @param codiceIvaCorrispettivo
     */
    public void cancellaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo);

    /**
     * Cancella un <code>CodiceIvaPrevalente</code>
     *
     * @param codiceIvaPrevalente
     */
    public void cancellaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente);

    /**
     * Esegue la cancellazione dell'argomento <code>Conto</code>
     *
     * @param conto
     */
    public void cancellaConto(Conto conto);

    /**
     * Cancella un <code>ContoBase</code>
     *
     * @param contoBase
     *            <code>ContoBase</code> da cancellare
     */
    public void cancellaContoBase(ContoBase contoBase);

    /**
     * Cancella una <code>ControPartita</code>
     *
     * @param controPartita
     *            <code>ControPartita</code> da cancellare
     */
    public void cancellaControPartita(ControPartita controPartita);

    /**
     * Esegue la cancellazione dell'argomento <code>Mastro</code>
     *
     * @param mastro
     */
    public void cancellaMastro(Mastro mastro);

    /**
     * Metodo che cancella il {@link RegistroIva}
     *
     * @param registroIva
     *            registro da eliminare
     */
    public void cancellaRegistroIva(RegistroIva registroIva);

    /**
     * Esegue la cancellazione dell'argomento <code>SottoConto</code>
     *
     * @param sottoConto
     */
    public void cancellaSottoConto(SottoConto sottoConto);

    /**
     * Cancella una struttura contabile. Se ci sono delle contro partite legate alla struttura contabile da cancellare
     * vengono eliminate
     *
     * @param strutturaContabile
     *            <code>StrutturaContabile</code> da cancellare
     */
    public void cancellaStrutturaContabile(StrutturaContabile strutturaContabile);

    /**
     * metodo che cancella {@link TipoAreaContabile}
     *
     * @param tipoAreaContabile
     */
    public void cancellaTipoAreaContabile(TipoAreaContabile tipoAreaContabile);

    /**
     * cancella {@link TipoDocumentoBase}
     *
     * @param tipoDocumentoBase
     */
    public void cancellaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase);

    /**
     * Carica un <code>CodiceIvaPrevalente</code>. Se l'entità è nulla viene caricato il codice iva predefinito per il
     * tipo area contabile altrimenti viene caricato quello specifico per l'entità. Se non esiste viene retituito un
     * valore a <code>null</code>.
     *
     * @param tipoAreaContabile
     * @param entita
     * @return
     */
    public CodiceIvaPrevalente caricaCodiceIvaPrevalente(TipoAreaContabile tipoAreaContabile, EntitaLite entita);

    /**
     * Carica tutti i codici iva corrispettivo per il tipo documento selezionato
     *
     * @param tipoDocumento
     * @return
     */
    public List<CodiceIvaCorrispettivo> caricaCodiciIvaCorrispettivo(TipoDocumento tipoDocumento);

    /**
     * Carica il settings della contabilit�. Se non esiste ne crea uno, lo salva e lo restituisce
     *
     * @return <code>ContabilitaSettings</code> caricato
     */
    public ContabilitaSettings caricaContabilitaSettings();

    /**
     * Carica tutti i conti base
     *
     * @return <code>List</code> di <code>ContoBase</code> caricati
     */
    @AsyncMethodInvocation
    public List<ContoBase> caricaContiBase();

    /**
     * Restituisce l'istanza di <code>Conto</code> identificata dell'argomento id
     *
     * @param idConto
     * @return
     */
    public Conto caricaConto(Integer idConto);

    /**
     * Carica una contropartita
     *
     * @param controPartita
     *            <code>ControPartita</code> da caricare
     * @return <code>ControPartita</code> caricata
     */
    public ControPartita caricaControPartita(ControPartita controPartita);

    /**
     * Carica tutte le contro partite in base all'area contabile
     *
     * @param areaContabile
     * @return
     * @throws ContabilitaException
     */
    public List<ControPartita> caricaControPartite(AreaContabile areaContabile);

    /**
     * Carica tutte le contro partite della struttura contabile
     *
     * @param strutturaContabile
     * @return <code>List</code> di <code>ControPartita</code> caricate
     */
    public List<ControPartita> caricaControPartite(TipoDocumento tipoDocumento, EntitaLite entita);

    /**
     * Carica tutte le contro partite in base all'area contabile colcolandone l'importo se � stata specificata la
     * formula
     *
     * @param areaContabile
     * @return
     * @throws ContabilitaException
     */
    public List<ControPartita> caricaControPartiteConImporto(AreaContabile areaContabile);

    /**
     * Carica tutte le entità che hanno una struttura contabile personalizzata per il tipo documento richiesto.
     *
     * @param tipoDocumento
     *            tipo documento
     *
     * @return entita caricate
     */
    List<EntitaLite> caricaEntitaConStrutturaContabile(TipoDocumento tipoDocumento);

    /**
     * Restituisce una collezione di tutte la classi di tipo <code>Mastro</code>
     *
     * @return
     */
    public List<Mastro> caricaMastri();

    /**
     * Restituisce l'istanza di <code>Mastro</code> identificata dall'argomento id
     *
     * @param mastro
     * @return
     */
    public Mastro caricaMastro(Integer idMastro);

    /**
     * Metodo che restituisce i {@link RegistroIva} per l'azienda loggata
     *
     * @param valueSearch
     * @param fieldSearch
     *
     * @return List<RegistroIva>
     * @throws ContabilitaException
     */
    @AsyncMethodInvocation
    public List<RegistroIva> caricaRegistriIva(String fieldSearch, String valueSearch);

    /**
     * Metodo che restituisce il {@link RegistroIva} per l'id passato per parametro
     *
     * @param id
     *            identificativo per caricare il {@link RegistroIva}
     * @return RegistroIva
     * @throws ContabilitaException
     */
    public RegistroIva caricaRegistroIva(Integer id);

    /**
     * Restituisce l'istanza di <code>SottoConto</code> identificata dell'argomento id
     *
     * @param idSottoConto
     * @return
     */
    public SottoConto caricaSottoConto(Integer idSottoConto);

    /**
     * Restituisce il {@link SottoConto} identificato
     *
     * @param sottotipoConto
     * @param codiceEntita
     * @return
     */
    public SottoConto caricaSottoContoPerEntita(SottotipoConto sottotipoConto, Integer codiceEntita);

    /**
     * Carica la struttura contabile relativa al tipo documento e all'entit�. Se l'entit� � <code>null</code> viene
     * caricata la struttura contabile base del tipo documento.
     *
     * @param tipoDocumento
     * @param entita
     * @return
     */
    public List<StrutturaContabile> caricaStrutturaContabile(TipoDocumento tipoDocumento, EntitaLite entita);

    /**
     * Mmetodo che restituisce i {@link TipoAreaContabile} per l'azienda loggata
     *
     * @param valueSearch
     *            valore da filtrare
     * @param fieldSearch
     *            campo da filtrare
     *
     * @return
     */
    public List<TipoAreaContabile> caricaTipiAreaContabile(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentiDisabilitati);

    /**
     * carica la lista di {@link TipoDocumentoBase} per azienda
     *
     * @return
     *
     */
    public List<TipoDocumentoBase> caricaTipiDocumentoBase();

    /**
     * carica la lista di {@link TipoDocumentoBase} per azienda e restituisce una {@link Map} con key
     * {@link TipoOperazione} e value {@link TipoAreaContabile} associato
     *
     * @return
     *
     */
    public Map<TipoOperazioneTipoDocumento, TipoAreaContabile> caricaTipiOperazione();

    /**
     * metodo che restituisce {@link TipoAreaContabile}
     *
     * @param id
     * @return
     *
     */
    public TipoAreaContabile caricaTipoAreaContabile(Integer id);

    /**
     * Metodo che restituisce il {@link TipoAreaContabile} per l'id di {@link TipoDocumento} passato per parametro
     *
     * @param tipoDocumento
     * @return
     */
    public TipoAreaContabile caricaTipoAreaContabilePerTipoDocumento(Integer idTipoDocumento);

    /**
     * carica {@link TipoDocumentoBase}
     *
     * @param idTipoDocumentoBase
     * @return
     * @throws ContabilitaException
     */
    public TipoDocumentoBase caricaTipoDocumentoBase(Integer idTipoDocumentoBase) throws ContabilitaException;

    /**
     * Esegue la creazione del {@link SottoConto} per {@link Entita} identificata da codiceEntita
     *
     * @param codiceEntita
     * @param sottoConto
     * @return
     */
    public SottoConto creaSottoContoPerEntita(Entita entita);

    /**
     * Esegue la ricerca di tutti i {@link Conto}
     *
     * @return List<Conto>
     */
    @AsyncMethodInvocation
    public List<Conto> ricercaConti();

    /**
     * Esegue la ricerca dei {@link Conto} per {@link SottotipoConto}
     *
     * @param codiceConto
     * @param sottoTipoConto
     *
     * @return
     */
    @AsyncMethodInvocation
    public List<Conto> ricercaConti(String codiceConto, SottotipoConto sottoTipoConto);

    /**
     * restituisce una collezione di {@link SottoConto}
     *
     * @return
     */
    @AsyncMethodInvocation
    public List<SottoConto> ricercaSottoConti();

    @AsyncMethodInvocation
    public List<SottoConto> ricercaSottoConti(ParametriRicercaSottoConti parametriRicercaSottoConti);

    /**
     * Restituisce una collezione di {@link SottoConto} filtrata per l'argomento <code>codice</code>
     *
     * @param codiceSottoConto
     *            definito secondo la codifica MM.CC.SSSSSS, viene perci� ripartito e l'effettiva ricerca avviene sul
     *            codice del mastro, codice del conto ed infine sottoconto
     * @return
     */
    @AsyncMethodInvocation
    public List<SottoConto> ricercaSottoConti(String codiceSottoConto);

    /**
     * restituisce una collezione di {@link SottoConto} ordinati per mastro, conto e sottoconto
     *
     * @return
     */
    @AsyncMethodInvocation
    public List<SottoConto> ricercaSottoContiOrdinati();

    /**
     * Restituisce una collezione di {@link SottoConto} filtrata per codice/descrizione, per {@link SottotipoConto}
     * ottimizzata.
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
    public CodiceIvaCorrispettivo salvaCodiceIvaCorrispettivo(CodiceIvaCorrispettivo codiceIvaCorrispettivo);

    /**
     * Salva un <code>CodiceIvaPrevalente</code>
     *
     * @param codiceIvaPrevalente
     * @return
     */
    public CodiceIvaPrevalente salvaCodiceIvaPrevalente(CodiceIvaPrevalente codiceIvaPrevalente);

    /**
     * Salva un <code>ContabilitaSettings</code>
     *
     * @return <code>ContabilitaSettings</code> salvato
     */
    public ContabilitaSettings salvaContabilitaSettings(ContabilitaSettings contabilitaSettings);

    /**
     * Esegue il salvataggio dell'argomento <code>Conto</code> e lo restituisce aggiornato
     *
     * @param conto
     * @return
     */
    public Conto salvaConto(Conto conto);

    /**
     * Salva un <code>ContoBase</code>
     *
     * @param contoBase
     *            <code>ContoBase</code> da salvare
     * @return <code>ContoBase</code> salvato
     */
    public ContoBase salvaContoBase(ContoBase contoBase);

    /**
     * Salva una <code>ControPartita</code>
     *
     * @param controPartita
     *            <code>ControPartita</code> da salvare
     * @return <code>ControPartita</code> salvata
     */
    public ControPartita salvaControPartita(ControPartita controPartita);

    /**
     * Esegue il salvataggio dell'argomento <code>Mastro</code> e lo restituisce aggiornato
     *
     * @param mastro
     * @return
     */
    public Mastro salvaMastro(Mastro mastro);

    /**
     * Esegue il salvataggio dell'argomento <code>RegistroIva</code> e lo restituisce aggiornato
     *
     * @param registroIva
     *            il registroIva da salvare
     * @return RegistroIva aggiornato
     */
    public RegistroIva salvaRegistroIva(RegistroIva registroIva);

    /**
     * Esegue il salvataggio dell'argomento <code>SottoConto</code> e lo restituisce aggiornato
     *
     * @param sottoConto
     * @return
     */
    public SottoConto salvaSottoConto(SottoConto sottoConto);

    /**
     * Salva una <code>StrutturaContabile</code>
     *
     * @param strutturaContabile
     *            <code>StrutturaContabile</code> da salvare
     * @return <code>StrutturaContabile</code> salvata
     */
    public StrutturaContabile salvaStrutturaContabile(StrutturaContabile strutturaContabile);

    /**
     * metodo che esegue il salvataggio di {@link TipoAreaContabile}
     *
     * @param tipoAreaContabile
     * @return
     */
    public TipoAreaContabile salvaTipoAreaContabile(TipoAreaContabile tipoAreaContabile);

    /**
     * salva {@link TipoDocumentoBase}
     *
     * @param tipoDocumentoBase
     * @return
     */
    public TipoDocumentoBase salvaTipoDocumentoBase(TipoDocumentoBase tipoDocumentoBase);
}