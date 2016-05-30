/**
 *
 */
package it.eurotn.panjea.contabilita.rich.bd;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.TipoDocumentoNonConformeException;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.rich.bd.IAreaDocumentiBD;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.EstrattoConto;
import it.eurotn.panjea.contabilita.domain.Giornale;
import it.eurotn.panjea.contabilita.domain.GiornaleIva;
import it.eurotn.panjea.contabilita.domain.NoteAreaContabile;
import it.eurotn.panjea.contabilita.domain.RegistroIva.TipoRegistro;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabileEstrattoConto;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;
import it.eurotn.panjea.contabilita.util.AreaContabileDTO;
import it.eurotn.panjea.contabilita.util.AreaContabileFullDTO;
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
import it.eurotn.panjea.contabilita.util.SaldoConto;
import it.eurotn.panjea.contabilita.util.SaldoContoConfronto;
import it.eurotn.panjea.contabilita.util.SituazioneEpDTO;
import it.eurotn.panjea.contabilita.util.TotaliCodiceIvaDTO;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.iva.util.RigaIvaRicercaDTO;
import it.eurotn.panjea.iva.util.parametriricerca.ParametriRicercaRigheIva;
import it.eurotn.panjea.pagamenti.service.exception.CodicePagamentoNonTrovatoException;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.partite.rich.bd.IPartiteBD;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

/**
 * Interfaccia per il service lato business Contabilita'.
 *
 * @author adriano
 * @version 1.0, 16/mag/07
 *
 */
public interface IContabilitaBD extends IAreaDocumentiBD {

    /**
     * Salva e valida il registro iva (GiornaleIva) e le aree contabili collegate.
     *
     * @param giornaleIva
     * @param mapAreeContabili
     * @param mapRigheIva
     */
    @AsyncMethodInvocation
    public void aggiornaStampaRegistroIva(GiornaleIva giornaleIva, Map<AreaContabileDTO, Integer> mapAreeContabili,
            Map<TotaliCodiceIvaDTO, Integer> mapRigheIva);

    /**
     * Aggiorna tutti i valori provenienti dalla stampa giornale ( giornali, areecontabili, righe contabili ).
     *
     * @param giornale
     *            <code>Giornale</code> da aggiornare
     * @param mapAreeContabili
     * @param mapRigheContabili
     */
    @AsyncMethodInvocation
    public void asyncaggiornaStampaGiornale(Giornale giornale, Map<AreaContabileDTO, Integer> mapAreeContabili,
            Map<RigaContabileDTO, List<Integer>> mapRigheContabili);

    /**
     * Calcola il saldo del conto dalla data specificata.
     *
     * @param conto
     *            il conto di cui calcolare il saldo
     * @param data
     *            la data di fine
     * @return Il saldo del conto dalla data
     */
    @AsyncMethodInvocation
    public BigDecimal calcoloSaldo(SottoConto conto, Date data, Integer annoEsercizio);

    /**
     *
     * @param areaContabile
     * @return
     */
    public AreaContabile cambiaStatoAreaContabileInConfermato(AreaContabile areaContabile);

    /**
     *
     * @param areaContabile
     * @return
     */
    public AreaContabile cambiaStatoAreaContabileInProvvisorio(AreaContabile areaContabile);

    /**
     *
     * @param areaContabile
     * @return
     */
    public AreaContabile cambiaStatoAreaContabileInSimulato(AreaContabile areaContabile);

    /**
     *
     * @param areaContabile
     * @return
     */
    public AreaContabile cambiaStatoAreaContabileInVerificato(AreaContabile areaContabile);

    /**
     * metodo che cancella {@link AreaContabile}.
     *
     * @param areaContabile
     * @throws AreeCollegatePresentiException
     */
    public void cancellaAreaContabile(AreaContabile areaContabile) throws AreeCollegatePresentiException;

    /**
     *
     * @param areaContabile
     * @param deleteAreeCollegate
     * @param forceDeleteAreeCollegate
     * @throws AreeCollegatePresentiException
     */
    public void cancellaAreaContabile(AreaContabile areaContabile, boolean deleteAreeCollegate,
            boolean forceDeleteAreeCollegate) throws AreeCollegatePresentiException;

    @AsyncMethodInvocation
    public void cancellaAreeContabili(List<Integer> idAreeContabili, boolean deleteAreeCollegate);

    /**
     * metodo che cancella {@link RigaContabile}.
     *
     * @param rigaContabile
     * @return area contabile
     */
    AreaContabile cancellaRigaContabile(RigaContabile rigaContabile);

    /**
     * metodo che cancella {@link RigaIva}.
     *
     * @param rigaIva
     */
    public void cancellaRigaIva(RigaIva rigaIva);

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
    @AsyncMethodInvocation
    public AreaContabile caricaAreaContabile(Integer id);

    @AsyncMethodInvocation
    public AreaContabile caricaAreaContabileByDocumento(Integer idDocumento);

    /**
     * metodo che carica un'area contabile full DTO che contiene un'area contabile, righe contabili e area iva.
     *
     * @param id
     *            l'id dell'area contabile
     * @return
     */
    @AsyncMethodInvocation
    public AreaContabileFullDTO caricaAreaContabileFullDTO(Integer id);

    @AsyncMethodInvocation
    public AreaContabileFullDTO caricaAreaContabileFullDTOByDocumento(Integer idDocumento);

    public AreaIva caricaAreaIva(AreaIva areaIva);

    /**
     * Carica il bilancio di verifica rispetto ai parametriRicercaBilancio definiti.
     *
     * @param parametriRicercaBilancio
     *            definizione del filtro da adottare per il caricamento del bilancio
     * @return List<SottoContoDTO>
     */
    @AsyncMethodInvocation
    public List<SaldoConto> caricaBilancio(ParametriRicercaBilancio parametriRicercaBilancio);

    /**
     * Carica il bilancio a confronto tra due periodi temporali definiti.
     *
     * @param parametriRicercaBilancioConfronto
     *            definizione dei filtri per il confronto tra due bilanci in periodi temporali distinti
     * @return List<ConfrontoSottocontiDTO>
     */
    @AsyncMethodInvocation
    public List<SaldoContoConfronto> caricaBilancioConfronto(
            ParametriRicercaBilancioConfronto parametriRicercaBilancioConfronto);

    /**
     * Carica l'estratto conto che contiene la lista di {@link RigaContabileEstrattoConto} per
     * {@link ParametriRicercaEstrattoConto} e il saldo da data registrazione.
     *
     * @param parametriRicercaEstrattoConto
     *            parametri settati per filtrare le righe contabili estratto conto
     * @return EstrattoConto
     */
    @AsyncMethodInvocation
    public EstrattoConto caricaEstrattoConto(ParametriRicercaEstrattoConto parametriRicercaEstrattoConto);

    /**
     * Carica il registro iva precedente a quello passato.
     *
     * @param giornaleIva
     *            il giornale iva del quale trovare il precedente
     * @return il giornale iva precedente a quello scelto o null se non presente
     */
    public GiornaleIva caricaGiornaleIvaPrecedente(GiornaleIva giornaleIva);

    /**
     * Carica il giornale precedente a quello attuale.
     *
     * @return <code>Giornale</code> precedente se esiste, <code>null</code> altrimenti
     */
    public Giornale caricaGiornalePrecedente(Giornale giornaleAttuale);

    /**
     * Carica tutti i giornali dell'azienda per l'anno selezionato.
     *
     * @param anno
     * @return
     */
    @AsyncMethodInvocation
    public List<Giornale> caricaGiornali(int annoCompetenza);

    /**
     * Carica i giornali per l'anno e mese scelti, basandosi sui registri iva presenti nell'anagrafica contabilit�; ogni
     * registro iva ha in quel mese associato un solo giornale. Se non esiste ancora ne creo uno nuovo.
     *
     * @param anno
     *            l'anno in cui cercare il giornale
     * @param mese
     *            il mese in cui cercare il giornale
     * @return List<GiornaleIva>
     */
    @AsyncMethodInvocation
    public List<GiornaleIva> caricaGiornaliIva(Integer anno, Integer mese);

    /**
     * Carica la liquidazione iva.
     *
     * @param dataInizioPeriodo
     * @param dataFinePeriodo
     * @return
     */
    @AsyncMethodInvocation
    public LiquidazioneIvaDTO caricaLiquidazioneIva(Date dataInizioPeriodo, Date dataFinePeriodo);

    /**
     * Carica le note sede e le note entita contabilità.
     *
     * @param sedeEntita
     *            la sede entita di cui caricare le note
     * @return note caricate
     */
    NoteAreaContabile caricaNoteSede(SedeEntita sedeEntita);

    public List<RegistroLiquidazioneDTO> caricaRegistriLiquidazione(Integer anno);

    /**
     * metodo che carica {@link RigaContabile}.
     *
     * @param id
     * @return
     * @throws ContabilitaException
     */
    public RigaContabile caricaRigaContabile(Integer id);

    /**
     *
     * @param id
     *            id riga da caricare
     * @return rigaContabile con le variabile lazy non inizializzate
     */
    public RigaContabile caricaRigaContabileLazy(Integer id);

    /**
     * metodo che carica {@link RigaIva}.
     *
     * @param id
     * @return
     * @throws ContabilitaException
     */
    public RigaIva caricaRigaIva(Integer id);

    @AsyncMethodInvocation
    public List<RigaContabile> caricaRigheContabili(Integer idAreaContabile);

    /**
     * Restiruisce la situazuione economica aziendale divisa in tre liste Economica Patrimoniale Ordine.
     */
    @AsyncMethodInvocation
    public SituazioneEpDTO caricaSituazioneEconomicaPatrimoniale(
            ParametriRicercaSituazioneEP parametriRicercaSituazioneEP);

    /**
     * Carica tutti i tipo documento in base al tipo registro.
     *
     * @param tipoRegistro
     * @return
     */
    public List<TipoDocumento> caricaTipiDocumentoByTipoRegistro(TipoRegistro tipoRegistro);

    /**
     * Carica il {@link TipoAreaPartita} associato al tipo documento <br>
     * . Metodo duplicato su {@link IPartiteBD}
     *
     * @param tipoDocumento
     *            il tipo documento di cui caricare il tipo area partite
     * @return il tipoAreaPartita collegato o una nuova istanza
     */
    public TipoAreaPartita caricaTipoAreaPartitaPerTipoDocumento(TipoDocumento tipoDocumento);

    /**
     * Restituisce l'elenco delle variabili che si possono usare nella contro partita.
     *
     * @return
     */
    public List<String> caricaVariabiliFormulaControPartite();

    /**
     * Restituisce l'elenco delle variabili che si possono usare nella struttura contabile.
     *
     * @return
     */
    public List<String> caricaVariabiliFormulaStrutturaContabile();

    /**
     * Crea le righe contabili per l'area contabile basandosi sulla struttura contabile dell'area
     *
     * @param areaContabile
     * @param list
     * @return List<RigaContabile>
     */
    @AsyncMethodInvocation
    public List<RigaContabile> creaRigheContabili(AreaContabile areaContabile, List<ControPartita> list);

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
     */
    @AsyncMethodInvocation
    public void eseguiAperturaContabile(ParametriAperturaContabile parametriAperturaContabile);

    /**
     * metodo incaricato dell'esecuzione delle operazioni di chiusura conti.
     *
     * @param parametriChiusuraContabile
     */
    @AsyncMethodInvocation
    public void eseguiChiusuraContabile(ParametriChiusuraContabile parametriChiusuraContabile);

    /**
     * Verifica se esistono righe contabili per l'area.
     *
     * @param areaContabile
     * @return <code>true</code> se esistono righe contabili, <code>false</code> altrimenti
     */
    public boolean isRigheContabiliPresenti(AreaContabile areaContabile);

    /**
     * Metodo per la ricerca delle {@link RigaContabile} per il modulo di ricerca e controllo di {@link AreaContabile}
     * restituisce una {@link Collection} di {@link RigaContabileDTO}.
     *
     * @return
     */
    @AsyncMethodInvocation
    public List<RigaContabileDTO> ricercaControlloAreeContabili(ParametriRicercaMovimentiContabili parametriRicerca);

    /**
     * Carica le righe iva per i parametri ricerca settati.
     *
     * @param parametriRicerca
     *            filtro per la ricerca di righe iva
     * @return List<TotaliCodiceIvaDTO>
     */
    @AsyncMethodInvocation
    public List<TotaliCodiceIvaDTO> ricercaRigheIva(ParametriRicercaMovimentiContabili parametriRicerca);

    /**
     * Esegue la ricerca delle righe iva in base ai parametri.
     *
     * @param parametriRicercaRigheIva
     *            parametri ri ricerca
     * @return righe iva caricate
     */
    List<RigaIvaRicercaDTO> ricercaRigheIva(ParametriRicercaRigheIva parametriRicercaRigheIva);

    /**
     * Carica una lista che presenta il riepilogo delle righe iva raggruppate per codice del codice iva, dove per il
     * campo imposta e imponibile viene fatta la somma di tutte le righe trovate.
     *
     * @param parametriRicercaMovimentiContabili
     *            i parametri per cui fare la ricerca righe
     * @return List<TotaliCodiceIvaDTO>
     */
    public List<TotaliCodiceIvaDTO> ricercaRigheRiepilogoCodiciIva(
            ParametriRicercaMovimentiContabili parametriRicercaMovimentiContabili);

    /**
     * metodo che esegue il salvataggio di {@link AreaContabile}.
     *
     * @param areaContabile
     * @return
     * @throws AreaContabileDuplicateException
     * @throws DocumentoDuplicateException
     */
    public AreaContabile salvaAreaContabile(AreaContabile areaContabile)
            throws AreaContabileDuplicateException, DocumentoDuplicateException;

    public AreaContabileFullDTO salvaAreaContabile(AreaContabile areaContabile, AreaRate areaRate)
            throws AreaContabileDuplicateException, DocumentoDuplicateException;

    /**
     * Salva il documento liquidazione.
     *
     * @param areaContabile
     * @return
     * @throws AreaContabileDuplicateException
     * @throws DocumentoDuplicateException
     * @throws FormulaException
     * @throws CodicePagamentoNonTrovatoException
     * @throws ContoRapportoBancarioAssenteException
     * @throws TipoDocumentoNonConformeException
     */
    public AreaContabileFullDTO salvaDocumentoLiquidazione(AreaContabile areaContabile);

    @AsyncMethodInvocation
    public GiornaleIva salvaGiornaleIva(GiornaleIva giornaleIva);

    /**
     * metodo che salva {@link RigaContabile}.
     *
     * @param rigaContabile
     * @return
     */
    public RigaContabile salvaRigaContabile(RigaContabile rigaContabile);

    public RigaIva salvaRigaIva(RigaIva rigaIva, TipoAreaContabile tipoAreaContabile)
            throws CodiceIvaCollegatoAssenteException;

    /**
     * Conferma un documento contabile senza rigenerare o modificare le varie aree
     *
     * @param idAreaContabile
     *            id dell'area contabile
     */
    void validaAreaContabile(int idAreaContabile);

    AreaContabileFullDTO validaAreaIva(AreaIva areaIva, Integer idAreaContabile);

    /**
     * esegue la validazione delle righe contabili verificandone la quadratura e variando lo stato dell'area contabile
     * del documento in CONFERMATO.
     *
     * @param areaContabile
     * @return
     */
    public AreaContabileFullDTO validaRigheContabili(AreaContabile areaContabile);

    /**
     *
     * @return lista di righe contabili che dovrebbero avere un centro ma non ne è assegnato nemmeno uno.
     */
    List<AreaContabileDTO> verificaRigheSenzaCentriDiCosto();

}
