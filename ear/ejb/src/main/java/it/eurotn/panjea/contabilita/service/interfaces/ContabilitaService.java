package it.eurotn.panjea.contabilita.service.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.interfaces.IAreaDocumentiService;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.TipoDocumentoNonConformeException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.centricosto.domain.CentroCosto;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.Conto.SottotipoConto;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.EstrattoConto;
import it.eurotn.panjea.contabilita.domain.Giornale;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.NoteAreaContabile;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.AperturaEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ChiusuraEsistenteException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.ContiEntitaAssentiException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.DocumentiNonStampatiGiornaleException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;
import it.eurotn.panjea.contabilita.service.exception.GiornaliNonValidiException;
import it.eurotn.panjea.contabilita.service.exception.RigheContabiliNonValidiException;
import it.eurotn.panjea.contabilita.service.exception.StatoAreaContabileNonValidoException;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
import it.eurotn.panjea.contabilita.util.DocumentoEntitaDTO;
import it.eurotn.panjea.contabilita.util.FatturatoDTO;
import it.eurotn.panjea.contabilita.util.LiquidazioneIvaDTO;
import it.eurotn.panjea.contabilita.util.ParametriAperturaContabile;
import it.eurotn.panjea.contabilita.util.ParametriChiusuraContabile;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancio;
import it.eurotn.panjea.contabilita.util.ParametriRicercaBilancioConfronto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaEstrattoConto;
import it.eurotn.panjea.contabilita.util.ParametriRicercaMovimentiContabili;
import it.eurotn.panjea.contabilita.util.ParametriRicercaSituazioneEP;
import it.eurotn.panjea.contabilita.util.RegistroLiquidazioneDTO;
import it.eurotn.panjea.contabilita.util.RigaContabileDTO;
import it.eurotn.panjea.contabilita.util.RisultatoControlloProtocolli;
import it.eurotn.panjea.contabilita.util.RisultatoControlloRateSaldoContabile;
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.contabilita.util.SaldoContoConfronto;
import it.eurotn.panjea.contabilita.util.SituazioneEpDTO;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoNonTrovatoException;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.service.interfaces.PartiteService;
import it.eurotn.panjea.rate.domain.AreaRate;

@Remote
public interface ContabilitaService extends IAreaDocumentiService {

    /**
     * Aggiorna tutti i valori provenienti dalla stampa giornale ( giornali, areecontabili, righe contabili ).
     *
     * @param giornale
     *            <code>Giornale</code> da aggiornare
     * @param mapAreeContabili
     * @param mapRigheContabili
     * @throws DocumentoDuplicateException
     * @throws AreaContabileDuplicateException
     */
    void aggiornaStampaGiornale(Giornale giornale, Map<AreaContabileDTO, Integer> mapAreeContabili,
            Map<RigaContabileDTO, List<Integer>> mapRigheContabili)
                    throws ContabilitaException, AreaContabileDuplicateException, DocumentoDuplicateException;

    /**
     * Salva e valida il registro iva (GiornaleIva) e le aree contabili collegate.
     *
     * @param giornaleIva
     * @param mapAreeContabili
     * @param mapRigheIva
     */
    void aggiornaStampaRegistroIva(GiornaleIva giornaleIva, Map<AreaContabileDTO, Integer> mapAreeContabili,
            Map<TotaliCodiceIvaDTO, Integer> mapRigheIva);

    /**
     * Calcola il saldo del conto specificato (M.C.S.) in una determinata data.
     *
     * @param conto
     * @param data
     * @param centroCosto
     * @return il saldo del conto dalla alla specificata
     */
    BigDecimal calcoloSaldo(SottoConto conto, CentroCosto centroCosto, Date data, Integer annoEsercizio);

    /**
     * Calcola il saldo del conto specificato (M.C.S.) in una determinata data.
     *
     * @param conto
     * @param data
     * @return il saldo del conto dalla alla specificata
     */
    BigDecimal calcoloSaldo(SottoConto conto, Date data, Integer annoEsercizio);

    /**
     *
     * @param areaContabile
     * @return
     * @throws StatoAreaContabileNonValidoException
     */
    AreaContabile cambiaStatoAreaContabileInConfermato(AreaContabile areaContabile)
            throws StatoAreaContabileNonValidoException, ContabilitaException;

    /**
     *
     * @param areaContabile
     * @return
     * @throws StatoAreaContabileNonValidoException
     */
    AreaContabile cambiaStatoAreaContabileInProvvisorio(AreaContabile areaContabile)
            throws StatoAreaContabileNonValidoException, ContabilitaException;

    /**
     *
     * @param areaContabile
     * @return
     * @throws StatoAreaContabileNonValidoException
     */
    AreaContabile cambiaStatoAreaContabileInSimulato(AreaContabile areaContabile)
            throws StatoAreaContabileNonValidoException, ContabilitaException;

    /**
     *
     * @param areaContabile
     * @return
     * @throws StatoAreaContabileNonValidoException
     */
    AreaContabile cambiaStatoAreaContabileInVerificato(AreaContabile areaContabile)
            throws StatoAreaContabileNonValidoException, ContabilitaException;

    /**
     * metodo che cancella {@link AreaContabile}.
     *
     * @param areaContabile
     * @throws DocumentiCollegatiPresentiException
     * @throws AreeCollegatePresentiException
     */
    void cancellaAreaContabile(AreaContabile areaContabile)
            throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException;

    /**
     * Cancella l'area contabile. Se il documento non ha altre aree collegate che non dipendono dall'area contabile (es
     * area iva) cancella il documento.
     *
     * @param areaContabile
     * @param deleteAreeCollegate
     *            indica se cancellare o meno le aree collegate e quidi l'intero documento
     * @param forceDeleteAreeCollegate
     *            utilizzato solamente se deleteAreeCollegate=true. <br>
     *            se il parametro è false viene generata l'eccezione {@link AreeCollegatePresentiException}<br>
     *            se true vengono cancellate le aree collegate al documento e l'intero documento
     * @throws DocumentiCollegatiPresentiException
     * @throws AreeCollegatePresentiException
     */
    void cancellaAreaContabile(AreaContabile areaContabile, boolean deleteAreeCollegate,
            boolean forceDeleteAreeCollegate)
                    throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException;

    /**
     *
     * @param areeContabili
     * @throws DocumentiCollegatiPresentiException
     * @throws AreeCollegatePresentiException
     */
    void cancellaAreeContabili(List<Integer> IdAreeContabili, boolean deleteAreeCollegate)
            throws DocumentiCollegatiPresentiException, AreeCollegatePresentiException;

    /**
     * metodo che cancella {@link RigaContabile}.
     *
     * @param rigaContabile
     * @return area contabile
     */
    AreaContabile cancellaRigaContabile(RigaContabile rigaContabile);

    /**
     * Cancella le righe contabili.
     *
     * @param areaContabile
     *            area di riferimento
     * @return area contabile
     */
    AreaContabile cancellaRigheContabili(AreaContabile areaContabile);

    /**
     * Cancella le righe contabili.
     *
     * @param righeContabili
     *            righe da cancellare
     * @return area contabile
     */
    AreaContabile cancellaRigheContabili(List<RigaContabile> righeContabili);

    /**
     * metodo che restituisce {@link AreaContabile}.
     *
     * @param id
     * @return
     * @throws ContabilitaException
     */
    AreaContabile caricaAreaContabile(Integer id) throws ContabilitaException;

    AreaContabile caricaAreaContabileByDocumento(Integer idDocumento);

    /**
     * metodo che carica un'area contabile full DTO che contiene un'area contabile, righe contabili e area iva.
     *
     * @param id
     *            l'id dell'area contabile
     * @return
     * @throws ContabilitaException
     */
    AreaContabileFullDTO caricaAreaContabileFullDTO(Integer id);

    /**
     * Carica un'areaContabileFullDTO dall' id del documento, se l'areacontabilo non � presente viene ritornato null.
     *
     * @param idDocumento
     * @return
     */
    AreaContabileFullDTO caricaAreaContabileFullDTOByDocumento(Integer idDocumento);

    /**
     * @see AreaContabileManager#caricaAreaContabileLiteByDocumento(it.eurotn.panjea.anagrafica.documenti.domain.Documento)
     * @param idDocumento
     * @return
     */
    it.eurotn.panjea.contabilita.domain.AreaContabileLite caricaAreaContabileLiteByDocumento(Integer idDocumento);

    /**
     *
     * @param parametriRicercaBilancio
     * @return
     * @throws ContabilitaException
     */
    List<SaldoConto> caricaBilancio(ParametriRicercaBilancio parametriRicercaBilancio)
            throws TipoDocumentoBaseException, ContabilitaException;

    /**
     * Metodo per caricare il bilancio a confronto, vengono caricati i bilanci di due periodi temporali e viene generata
     * la riga di confronto tra i due che riporta saldo iniziale, saldo finale differenza saldi e percentuale differenza
     * saldi.
     *
     * @param parametriRicercaBilancioConfronto
     *            i parametri contenenti le date su cui caricare i bilanci
     * @return List<ConfrontoSottocontiDTO> righeBilancioConfronto
     * @throws ContabilitaException
     */
    List<SaldoContoConfronto> caricaBilancioConfronto(
            ParametriRicercaBilancioConfronto parametriRicercaBilancioConfronto)
                    throws TipoDocumentoBaseException, ContabilitaException;

    /**
     * metodo di ricerca per stampa/visualizzazione estratto conto.
     *
     * @param params
     *            parametri di ricerca
     * @return estratto conto
     */
    EstrattoConto caricaEstrattoConto(Map<Object, Object> params);

    /**
     * metodo di ricerca per stampa/visualizzazione estratto conto.
     *
     * @param parametriRicercaEstrattoConto
     *            parametri di ricerca
     * @return estratto conto
     * @throws ContabilitaException
     * @throws TipoDocumentoBaseException
     *             TipoDocumentoBaseException
     */
    EstrattoConto caricaEstrattoConto(ParametriRicercaEstrattoConto parametriRicercaEstrattoConto)
            throws ContabilitaException, TipoDocumentoBaseException;

    /**
     * Carica il fatturato in base ai parametri specificati.
     *
     * @param parameters
     *            parametri
     * @return fatturato
     */
    List<FatturatoDTO> caricaFatturato(Map<Object, Object> parameters);

    /**
     * Carica il registro iva precedente a quello passato.
     *
     * @param giornaleIva
     *            il giornale iva del quale trovare il precedente
     * @return il giornale iva precedente a quello scelto o null se non presente
     */
    GiornaleIva caricaGiornaleIvaPrecedente(GiornaleIva giornaleIva);

    /**
     * Carica il giornale precedente a quello attuale.
     *
     * @return <code>Giornale</code> precedente se esiste, <code>null</code> altrimenti
     */
    Giornale caricaGiornalePrecedente(Giornale giornaleAttuale);

    /**
     * Carica tutti i giornali dell'azienda per l'anno selezionato.
     *
     * @param anno
     * @return
     */
    List<Giornale> caricaGiornali(int annoCompetenza) throws ContabilitaException;

    /**
     * Carica i giornali per l'anno e mese scelti, basandosi sui registri iva presenti nell'anagrafica contabilità. Ogni
     * registro iva ha in quel mese associato un solo giornale. Se non esiste ancora ne creo uno nuovo.
     *
     * @param anno
     *            l'anno in cui cercare il giornale
     * @param mese
     *            il mese in cui cercare il giornale
     * @return List<GiornaleIva>
     */
    List<GiornaleIva> caricaGiornaliIva(Integer anno, Integer mese);

    /**
     * Carica la liquidazione iva.
     *
     * @param dataInizioPeriodo
     * @param dataFinePeriodo
     * @return LiquidazioneIvaDTO
     */
    LiquidazioneIvaDTO caricaLiquidazioneIva(Date dataInizioPeriodo, Date dataFinePeriodo) throws ContabilitaException;

    /**
     * Carica le note sede e le note entita contabilità.
     *
     * @param sedeEntita
     *            la sede entita di cui caricare le note
     * @return note caricate
     */
    NoteAreaContabile caricaNoteSede(SedeEntita sedeEntita);

    /**
     * Carica il DTO della liquidazione contenente le eventuali areeContabili e GiornaliIva (di tipo Riepilogativo) Se
     * l'iva e' mensile avra' una lista di 12 elementi, se trimestrale di 4.
     *
     * @param anno
     * @return
     * @throws ContabilitaException
     * @throws TipoDocumentoBaseException
     *             TipoDocumentoBaseException
     */
    List<RegistroLiquidazioneDTO> caricaRegistriLiquidazione(Integer anno)
            throws ContabilitaException, TipoDocumentoBaseException;

    /**
     * Carica tutti i documenti delle entità che fanno parte della blacklist per il periodo indicato.
     *
     * @param params
     *            parametri
     * @return documenti caricati
     */
    List<DocumentoEntitaDTO> caricaRiepilogoDocumentiBlacklist(Map<Object, Object> params);

    /**
     * metodo che carica {@link RigaContabile}.
     *
     * @param id
     * @return
     * @throws ContabilitaException
     */
    RigaContabile caricaRigaContabile(Integer id) throws ContabilitaException;

    /**
     *
     * @param id
     *            id riga da caricare
     * @return rigaContabile con le variabile lazy non inizializzate
     */
    RigaContabile caricaRigaContabileLazy(Integer id);

    /**
     * Metodo che esegue il caricamento delle righe contabili dato l'identificativo dell' {@link AreaContabile}.
     *
     * @param idAreaContabile
     * @return
     */
    List<RigaContabile> caricaRigheContabili(Integer idAreaContabile);

    /**
     * Carica la situazione economica patrimoniale aziendale.
     *
     * @param parametriRicercaSituazioneEP
     * @return
     */
    SituazioneEpDTO caricaSituazioneEconomicoPatrimoniale(ParametriRicercaSituazioneEP parametriRicercaSituazioneEP);

    /**
     * Carica tutti i tipo documento in base al tipo registro.
     *
     * @param tipoRegistro
     * @return
     */
    List<TipoDocumento> caricaTipiDocumentoByTipoRegistro(TipoRegistro tipoRegistro);

    /**
     * Carica il {@link TipoAreaPartita} associato al tipo documento. <br>
     * Metodo duplicato su {@link PartiteService}
     *
     * @param tipoDocumento
     *            il tipo documento di cui caricare il tipo area partite
     * @return il tipoAreaPartita collegato o una nuova istanza
     */
    TipoAreaPartita caricaTipoAreaPartitaPerTipoDocumento(TipoDocumento tipoDocumento);

    /**
     * Restituisce l'elenco delle variabili che si possono usare nella contro partite.
     *
     * @return
     */
    List<String> caricaVariabiliFormulaControPartite();

    /**
     * Restituisce l'elenco delle variabili che si possono usare nella struttura contabile.
     *
     * @return
     */
    List<String> caricaVariabiliFormulaStrutturaContabile();

    /**
     * Crea le righe contabili per l'area contabile basandosi sulla struttura contabile dell'area.
     *
     * @param areaContabile
     * @param list
     * @return List<RigaContabile>
     * @throws ContoRapportoBancarioAssenteException
     * @throws ContoEntitaAssenteException
     */
    List<RigaContabile> creaRigheContabili(AreaContabile areaContabile, List<ControPartita> list)
            throws ContabilitaException, FormulaException, ContoRapportoBancarioAssenteException,
            ContiEntitaAssentiException;

    /**
     * Crea tutte le righe contabili automatiche.
     *
     * @param areaContabile
     *            area di riferimento
     */
    void creaRigheContabiliAutomatiche(AreaContabile areaContabile);

    /**
     * metodo incaricato dell'esecuzione delle operazioni di apertura conti.
     *
     * @param parametriAperturaContabile
     * @throws AperturaEsistenteException
     * @throws ChiusuraAssenteException
     * @throws TipoDocumentoBaseException
     *             TipoDocumentoBaseException
     * @throws ContiBaseException
     */
    void eseguiAperturaContabile(ParametriAperturaContabile parametriAperturaContabile) throws ContabilitaException,
            AperturaEsistenteException, ChiusuraAssenteException, ContiBaseException, TipoDocumentoBaseException;

    /**
     * metodo incaricato dell'esecuzione delle operazioni di chiusura conti.
     *
     * @param parametriChiusuraContabile
     * @throws ChiusuraEsistenteException
     * @throws ContiBaseException
     * @throws DocumentiNonStampatiGiornaleException
     * @throws GiornaliNonValidiException
     */
    void eseguiChiusuraContabile(ParametriChiusuraContabile parametriChiusuraContabile)
            throws TipoDocumentoBaseException, ContabilitaException, ChiusuraEsistenteException,
            DocumentiNonStampatiGiornaleException, ContiBaseException, GiornaliNonValidiException;

    /**
     * Verifica se esistono righe contabili per l'area.
     *
     * @param areaContabile
     * @return <code>true</code> se esistono righe contabili, <code>false</code> altrimenti
     */
    boolean isRigheContabiliPresenti(AreaContabile areaContabile) throws ContabilitaException;

    /**
     * metodo di ricerca per il controllo delle aree contabili.
     *
     * @param parametriRicerca
     * @return
     */
    List<RigaContabileDTO> ricercaControlloAreeContabili(ParametriRicercaMovimentiContabili parametriRicerca);

    /**
     * Carica le righe iva per i parametri ricerca settati.
     *
     * @param parametriRicerca
     *            filtro per la ricerca di righe iva
     * @return List<TotaliCodiceIvaDTO>
     */
    List<TotaliCodiceIvaDTO> ricercaRigheIva(ParametriRicercaMovimentiContabili parametriRicerca);

    /**
     * Carica una lista che presenta il riepilogo delle righe iva raggruppate per codice del codice iva, dove per il
     * campo imposta e imponibile viene fatta la somma di tutte le righe trovate.
     *
     * @param parametriRicercaMovimentiContabili
     *            parametri su cui impostare la ricerca
     * @return List<TotaliCodiceIvaDTO>
     */
    List<TotaliCodiceIvaDTO> ricercaRigheRiepilogoCodiciIva(
            ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili);

    /**
     * metodo che esegue il salvataggio di {@link AreaContabile}.
     *
     * @param areaContabile
     * @return
     * @throws AreaContabileDuplicateException
     * @throws DocumentoDuplicateException
     */
    AreaContabile salvaAreaContabile(AreaContabile areaContabile)
            throws ContabilitaException, AreaContabileDuplicateException, DocumentoDuplicateException;

    /**
     * metodo che esegue il salvataggio di {@link AreaContabile} e {@link AreaPartite} il metodo ritorna
     * {@link AreaContabileFullDTO} con tutti i dati del documento contabile.
     *
     * @param areaContabile
     * @param areaRate
     * @return {@link AreaContabileFullDTO} con {@link AreaContabile} e {@link AreaPartite} salvate
     * @throws DocumentoDuplicateException
     * @throws AreaContabileDuplicateException
     * @throws ContabilitaException
     */
    AreaContabileFullDTO salvaAreaContabile(AreaContabile areaContabile, AreaRate areaRate)
            throws ContabilitaException, AreaContabileDuplicateException, DocumentoDuplicateException;

    /**
     * Salva il documento di liquidazione compreso di righe contabili generate da una struttura contabile.
     *
     * @param areaContabile
     * @return
     * @throws FormulaException
     * @throws CodicePagamentoNonTrovatoException
     * @throws ContoRapportoBancarioAssenteException
     * @throws TipoDocumentoNonConformeException
     */
    AreaContabileFullDTO salvaDocumentoLiquidazione(AreaContabile areaContabile) throws AreaContabileDuplicateException,
            DocumentoDuplicateException, FormulaException, CodicePagamentoNonTrovatoException,
            ContoRapportoBancarioAssenteException, TipoDocumentoNonConformeException;

    /**
     * Salva un giornale iva riepilogativo. Solamente il tipo riepilogativo può essere salvato direttamente perchè non
     * deve aggiornare le righe contabili. Per salvare le modifiche agli altri giornaliIva vedi @see
     * aggiornaStampaRegistroIva.
     *
     * @param giornaleIva
     * @return
     */
    GiornaleIva salvaGiornaleIvaRiepilogativo(GiornaleIva giornaleIva);

    /**
     * metodo che salva {@link RigaContabile}.
     *
     * @param rigaContabile
     * @return
     */
    RigaContabile salvaRigaContabile(RigaContabile rigaContabile);

    /**
     * Conferma un documento contabile senza rigenerare o modificare le varie aree
     *
     * @param idAreaContabile
     *            id dell'area contabile
     * @throws ContabilitaException
     *             ex generica
     * @throws RigheContabiliNonValidiException
     *             righe contabili non valide.
     */
    void validaAreaContabile(int idAreaContabile) throws ContabilitaException, RigheContabiliNonValidiException;

    AreaContabileFullDTO validaAreaIva(AreaIva areaIva, Integer idAreaContabile);

    /**
     * esegue la validazione delle righe contabili verificandone la quadratura e variando lo stato dell'area contabile
     * del documento in CONFERMATO.
     *
     * @param areaContabile
     * @return
     * @throws RigheContabiliNonValidiException
     */
    AreaContabileFullDTO validaRigheContabili(AreaContabile areaContabile)
            throws ContabilitaException, RigheContabiliNonValidiException;

    /**
     *
     * @param params
     *            parametri
     * @return lista con i codici dei protocolli che hanno data inferiore al protocollo che lo "segue"
     */
    List<RisultatoControlloProtocolli> verificaDataProtocolli(Map<Object, Object> params);

    /**
     * Verifica se nei documenti contabili ci sono dei "buchi" di numerazione.
     *
     * @param params
     *            parametri
     * @return lista con i codici mancanti. Lista vuota se non ci sono codici
     */
    List<RisultatoControlloProtocolli> verificaProtocolliMancanti(Map<Object, Object> params);

    /**
     *
     * @return lista di righe contabili che dovrebbero avere un centro ma non ne è assegnato nemmeno uno.
     */
    List<AreaContabileDTO> verificaRigheSenzaCentriDiCosto();

    /**
     * Verifica eventuali discrepanze trai saldi contabili e il residuo rate di clienti e fornitori.
     *
     * @param parametri
     *            parametri passati come mappa per l'esecuzione diretta dal report.
     * @return lista di risultati con le differenze diverse da zero.
     */

    List<RisultatoControlloRateSaldoContabile> verificaSaldi(Map<Object, Object> parametri);

    /**
     * Verifica eventuali discrepanze trai saldi contabili e il residuo rate di clienti e fornitori.
     *
     * @param sottotipoConto
     *            SottotipoConto del conto (cliente o fornitore) per il quale verificare i saldi.
     * @return lista di risultati con le differenze diverse da zero.
     */
    List<RisultatoControlloRateSaldoContabile> verificaSaldi(SottotipoConto sottotipoConto);

    /**
     * @param params
     *            parametri
     * @return array contenenti delle liste con i risultati dei vari controlli. index 0: protocolli mancanti, index[1]
     *         protocolli con data errata
     */
    List<?>[] verificheContabili(Map<Object, Object> params);
}
