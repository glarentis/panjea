package it.eurotn.panjea.ordini.service.interfaces;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.lotti.exception.EvasioneLottiException;
import it.eurotn.panjea.lotti.exception.LottiException;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.RigaArticoloDistinta;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.ordini.domain.OrdineImportato;
import it.eurotn.panjea.ordini.domain.RigaArticolo;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.panjea.ordini.domain.RigaOrdineImportata;
import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.ordini.domain.documento.evasione.DistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCarico;
import it.eurotn.panjea.ordini.domain.documento.evasione.RigaDistintaCaricoProduzione;
import it.eurotn.panjea.ordini.exception.CodicePagamentoAssenteException;
import it.eurotn.panjea.ordini.exception.CodicePagamentoEvasioneAssenteException;
import it.eurotn.panjea.ordini.exception.EntitaSenzaTipoDocumentoEvasioneException;
import it.eurotn.panjea.ordini.exception.TipoAreaPartitaDestinazioneRichiestaException;
import it.eurotn.panjea.ordini.manager.documento.evasione.DatiDistintaCaricoVerifica;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTOStampa;
import it.eurotn.panjea.ordini.util.AreaOrdineRicerca;
import it.eurotn.panjea.ordini.util.AreaProduzioneFullDTOStampa;
import it.eurotn.panjea.ordini.util.ParametriRicercaProduzione;
import it.eurotn.panjea.ordini.util.RigaDistintaCaricoStampa;
import it.eurotn.panjea.ordini.util.RigaMovimentazione;
import it.eurotn.panjea.ordini.util.RigaOrdineDTO;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaAreaOrdine;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaEvasione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaMovimentazione;
import it.eurotn.panjea.ordini.util.parametriricerca.ParametriRicercaOrdiniImportati;
import it.eurotn.panjea.ordini.util.righeinserimento.ParametriRigheOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigaOrdineInserimento;
import it.eurotn.panjea.ordini.util.righeinserimento.RigheOrdineInserimento;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.rate.domain.AreaRate;

@Remote
public interface OrdiniDocumentoService {

    /**
     * Aggiorna la data di consegna in accordo con la data di consegna dell'area ordine.<br>
     * Vengono modificate le righe con data di consegna uguale alla data di modifica o tutte le
     * righe se la data di riferimento non è impostata.
     *
     * @param areaOrdine
     *            area di riferimento
     * @param dataRiferimento
     *            se impostata la utilizzo come riferimento per filtrare le righe da modificare
     */
    void aggiornaDataConsegna(AreaOrdine areaOrdine, Date dataRiferimento);

    /**
     *
     * @param righeDistintaDaAggiornare
     *            righe della distinta di carico da aggiornare
     * @return righe della distinta con i dati di evasione instanziati
     */
    List<RigaDistintaCarico> aggiornaRigheCaricoConDatiEvasione(List<RigaDistintaCarico> righeDistintaDaAggiornare);

    /**
     * Aggiunge una variazione ad ogni riga del documento.
     *
     * @param idAreaOrdine
     *            area ordine da variare
     * @param variazione
     *            variazione da aggiungere
     * @param percProvvigione
     *            percentuale di provvigione
     * @param variazioneScontoStrategy
     *            strategia di variazione dello sconto
     * @param tipoVariazioneScontoStrategy
     *            tipo di variazione dello sconto
     * @param variazioneProvvigioneStrategy
     *            strategia di variazione della provvigione
     * @param tipoVariazioneProvvigioneStrategy
     *            tipo di variazione della provvigione
     */
    void aggiungiVariazione(Integer idAreaOrdine, BigDecimal variazione, BigDecimal percProvvigione,
            RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
            TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
            RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
            TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy);

    /**
     * Associa una configurazioneDistinta ad una riga ordine. Se la riga aveva una configurazione
     * personalizzata questa verrà cancellata.
     *
     * @param rigaArticolo
     *            riga
     * @param configurazioneDistintaDaAssociare
     *            conf
     * @return RigaArticolo
     */
    RigaArticolo associaConfigurazioneDistintaARigaOrdine(RigaArticolo rigaArticolo,
            ConfigurazioneDistinta configurazioneDistintaDaAssociare);

    /**
     * Blocca o sblocca l'area ordine.
     *
     * @param idAreaOrdine
     *            area Ordine da bloccare/sbloccare
     * @param blocca
     *            true per bloccare l'area ordine, false per sbloccarla
     * @return area ordine con il nuovo stato
     */
    AreaOrdine bloccaOrdine(int idAreaOrdine, boolean blocca);

    /**
     *
     * @param idOrdini
     *            ordini da bloccare
     * @param blocca
     *            true per bloccare gli ordini false per sbloccarli
     * @return lista di aree ordine
     */
    List<AreaOrdineRicerca> bloccaOrdini(Collection<Integer> idOrdini, boolean blocca);

    /**
     * Calcola la giacenza ad una determinata data per tutti gli articoli di un deposito.<br/>
     * La giacenza ritornata comprende la giacenza di magazzino meno le righe che sono sulle
     * distinte di carico.
     *
     * @param data
     *            data per la giacenza
     * @param depositoLite
     *            deposito interessato alla giacenza
     * @return mappa contenente l'articolo come chiave e la sua giacenza come valore
     */
    Map<ArticoloLite, Double> calcolaGiacenze(DepositoLite depositoLite, Date data);

    /**
     * Cancella un'area ordine, il documento e l'eventuale area rate collegata.
     *
     * @param areaOrdine
     *            areaOrdine da cancellare
     */
    void cancellaAreaOrdine(AreaOrdine areaOrdine);

    /**
     * Cancella una lista di aree ordine.
     *
     * @param areeOrdine
     *            aree da cancellare
     */
    void cancellaAreeOrdine(List<AreaOrdine> areeOrdine);

    /**
     *
     * @param numeroOrdini
     *            ordini da cancellare
     */
    void cancellaOrdiniImportati(Collection<String> numeroOrdini);

    /**
     * Cancella tutte le righe degli ordini importati per un agente.
     *
     * @param codiceAgente
     *            codice dell'agente.
     */
    void cancellaOrdiniImportatiPerAgente(String codiceAgente);

    /**
     * Cancella una {@link RigaOrdine} e restituisce l'area ordine a cui era legata.
     *
     * @param rigaOrdine
     *            riga da cancellare
     * @return area ordine legata alla riga cancellata
     */
    AreaOrdine cancellaRigaOrdine(RigaOrdine rigaOrdine);

    /**
     * Rimuove il collegamento fra due righe ordine
     *
     * @param rigaOrdineOrigine
     *            id riga origine
     * @param rigaOrdineDestinazione
     *            id riga destinazione
     */
    void cancellaRigheCollegate(int rigaOrdineOrigine, int rigaOrdineDestinazione);

    /**
     * Rimuove una lista di righedistinteCarico.
     *
     * @param righeDistintaCarico
     *            righe da rimuovere
     */
    void cancellaRigheDistintaCarico(Set<RigaDistintaCarico> righeDistintaCarico);

    /**
     * Cancella tutte le righe distinte carico lotti legate alla rida distinta.
     *
     * @param rigaDistintaCarico
     *            riga distinta carico
     */
    void cancellaRigheDistintaCaricoLotti(RigaDistintaCarico rigaDistintaCarico);

    /**
     * cancella un {@link TipoAreaOrdine}.
     *
     * @param tipoAreaOrdine
     *            {@link TipoAreaOrdine} da cancellare
     */
    void cancellaTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine);

    /**
     * Carica una {@link AreaOrdine}.
     *
     * @param areaOrdine
     *            area da caricare
     * @return area caricata
     */
    AreaOrdine caricaAreaOrdine(AreaOrdine areaOrdine);

    /**
     * metodo incaricato di eseguire il caricamento di {@link AreaOrdineFullDTO} e controllo.
     *
     * @param paramenters
     *            parametri
     * @return area ordine full DTO caricata
     */
    AreaOrdineFullDTO caricaAreaOrdineControlloFullDTO(Map<Object, Object> paramenters);

    /**
     * metodo incaricato di eseguire il caricamento di {@link AreaOrdineFullDTO} .
     *
     * @param areaOrdine
     *            area ordine da caricare
     * @return area ordine full DTO caricata
     */
    AreaOrdineFullDTO caricaAreaOrdineFullDTO(AreaOrdine areaOrdine);

    /**
     * metodo incaricato di eseguire il caricamento di {@link AreaOrdineFullDTOStampa} .
     *
     * @param paramenters
     *            parametri
     * @return area ordine full DTO caricata
     */
    AreaOrdineFullDTOStampa caricaAreaOrdineFullDTOStampa(Map<Object, Object> paramenters);

    /**
     * metodo incaricato di eseguire il caricamento di {@link AreaProduzioneFullDTOStampa} .
     *
     * @param paramenters
     *            parametri
     * @return area ordine produzione full DTO caricata
     */
    AreaProduzioneFullDTOStampa caricaAreaOrdineProduzioneDTOStampa(Map<Object, Object> paramenters);

    /**
     *
     * @param dataInizioTrasporto
     *            data inizio trasp.
     * @return dati per la verifica degli ordini in produzione
     */
    DatiDistintaCaricoVerifica caricaDatiVerifica(Date dataInizioTrasporto);

    /**
     * Carica la movimentazione degli ordini.
     *
     * @param parametriRicercaMovimentazione
     *            parametri di ricerca
     * @param page
     *            pagina
     * @param sizeOfPage
     *            record per pagina
     * @return righe di movimentazione caricate
     */
    List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
            int page, int sizeOfPage);

    /**
     * Carica una riga ordine.
     *
     * @param rigaOrdine
     *            Riga ordine da caricare
     * @return Riga ordine caricata
     */
    RigaOrdine caricaRigaOrdine(RigaOrdine rigaOrdine);

    /**
     * Carica tutte le righe collegate alle riga ordine passata come parametro.
     *
     * @param rigaOrdine
     *            riga ordine di riferimento
     * @return righe collegate caricate
     */
    List<RigaDestinazione> caricaRigheCollegate(RigaOrdine rigaOrdine);

    /**
     * Carica tutte le {@link RigaDistintaCarico} delle distinte .
     *
     * @param distinte
     *            distinte di riferimento
     * @return righe caricate
     */
    List<RigaDistintaCarico> caricaRigheDistintaCarico(List<DistintaCarico> distinte);

    /**
     * Carica tutte le righe distinta carico con i parametri specificati.
     *
     * @param parametri
     *            parametri di ricerca
     * @return righe caricate
     */
    List<RigaDistintaCaricoStampa> caricaRigheDistintaCarico(Map<Object, Object> parametri);

    /**
     * Carica tutte le righe magazzino in base ai parametri di ricerca.
     *
     * @param parametri
     *            parametri di ricerca
     * @return righe caricate
     */
    List<RigaArticoloDistinta> caricaRigheDistintaMagazzino(ParametriRicercaAreaMagazzino parametri);

    /**
     * Carica tutte le righe ordine che possono essere evase in base ai parametri di ricerca.
     *
     * @param parametriRicercaEvasione
     *            parametri di ricerca
     * @return {@link List} di {@link RigaDistintaCarico}
     */
    List<RigaDistintaCarico> caricaRigheEvasione(ParametriRicercaEvasione parametriRicercaEvasione);

    /**
     * @param parametriRicercaProduzione
     *            parametriRicercaProduzione
     * @return lista di righe ordine produzione
     */
    List<RigaDistintaCaricoProduzione> caricaRigheEvasioneProduzione(
            ParametriRicercaProduzione parametriRicercaProduzione);

    /**
     * Carica tutte le righe evasione che sono state salvate.<br/>
     * Le righe salvate appartengono ad una distinta di carico, quindi sono state "spedite" in
     * magazzino
     *
     * @return righe attaulamente in magazzino per essere evase
     */
    List<RigaDistintaCarico> caricaRigheInMagazzino();

    /**
     * Carica le righe di una area ordine scelta.
     *
     * @param areaOrdine
     *            l'area ordine di cui caricare le righe
     * @return List<RigaoOrdine>
     */
    List<RigaOrdine> caricaRigheOrdine(AreaOrdine areaOrdine);

    /**
     * Carica le righe DTO di un'areaOrdine scelta.
     *
     * @param areaOrdine
     *            l'area ordine di cui caricare le righe
     * @return List<RigaOrdineDTO>
     */
    List<RigaOrdineDTO> caricaRigheOrdineDTO(AreaOrdine areaOrdine);

    /**
     * @param parametri
     *            parametri di ricerca
     * @return lista delle righe ordine importate
     */
    List<RigaOrdineImportata> caricaRigheOrdineImportate(ParametriRicercaOrdiniImportati parametri);

    /**
     * Carica le righe ordine inserimento in base ai parametri specificati.
     *
     * @param parametri
     *            parametri di ricerca
     * @return righe caricate
     */
    RigheOrdineInserimento caricaRigheOrdineInserimento(ParametriRigheOrdineInserimento parametri);

    /**
     * Carica i tipi area ordine.
     *
     * @param valueSearch
     *            valore da filtrare
     * @param fieldSearch
     *            field da filtrare
     *
     * @param loadTipiDocumentoDisabilitati
     *            true se voglio caricare anche i tipi ordine disabilitati
     * @return lista dei tipiAreaOrdine
     */
    List<TipoAreaOrdine> caricaTipiAreaOrdine(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentoDisabilitati);

    /**
     *
     * @return lista dei tipiDocumento legati ai TipiAreaOrdine
     */
    List<TipoDocumento> caricaTipiDocumentiOrdine();

    /**
     *
     * @param id
     *            id del tipiAreaOrdine da caricare
     * @return TipiAreaOrdine caricata
     */
    TipoAreaOrdine caricaTipoAreaOrdine(Integer id);

    /**
     *
     * @param idTipoDocumento
     *            id Tipo Documento riferito al tipoAreaOrdine
     * @return tipoAreaOrdine legata al tipoDocumento
     */
    TipoAreaOrdine caricaTipoAreaOrdinePerTipoDocumento(Integer idTipoDocumento);

    /**
     *
     * @param righeOrdineDaCambiare
     *            righe da collegare all'ultima testata inserita
     */
    void collegaTestata(Set<Integer> righeOrdineDaCambiare);

    /**
     * Conferma gli ordini importati.<br/>
     * La conferma crea la testata, carica i dati commerciali dei clienti<br/>
     * inserisce le righe e ne calcola il prezzo in base ai dati commerciali. Infine totalizza
     * l'ordine.
     *
     * @param ordiniDaConfermare
     *            ordini da confermare.
     * @return dataCreazioneTimestamp degli ordini creati
     */
    Long confermaOrdiniImportati(Collection<OrdineImportato> ordiniDaConfermare);

    /**
     * Esegue una copia dell'ordine di riferimento (area ordine, documento, area rate e righe ordine
     * ) e restituisce l' {@link AreaOrdineFullDTO} generata.
     *
     * @param idAreaOrdine
     *            id dell'ordine da copiare
     * @return copia effettuata
     */
    AreaOrdineFullDTO copiaOrdine(Integer idAreaOrdine);

    /**
     * Crea le distinte di carico con le righe evasione passate come parametro. <br/>
     * Viene creata una distinta di carico per ogni deposito.
     *
     * @param righeEvasione
     *            righe evasione con le quali creare la distinta di carico
     * @return distinte di carico create (una per deposito).
     */
    List<DistintaCarico> creaDistintadiCarico(List<RigaDistintaCarico> righeEvasione);

    /**
     * Crea una riga articolo.
     *
     * @param parametriCreazioneRigaArticolo
     *            di creazione
     * @return nuova rigaArticolo con i parametri settatti.
     */
    RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo);

    /**
     * Crea una riga note automatica per l'area ordine indicata.
     *
     * @param areaOrdine
     *            area ordine di riferimento
     * @param note
     *            note
     * @return <code>true</code> se la riga viene generata e salvata correttamente
     */
    boolean creaRigaNoteAutomatica(AreaOrdine areaOrdine, String note);

    /**
     * Divide una riga in più righe con qta e data modificata
     *
     * @param rigaOriginale
     *            id Riga da dividere
     * @param righeDivise
     *            righe con qta e dataConsegna da creare
     */
    void dividiRiga(Integer rigaOriginale, List<RigaArticolo> righeDivise);

    /**
     * Evade una lista di righe.
     *
     * @param righeEvasione
     *            righe da evadere
     * @param documentoEvasione
     *            documento di destinazione
     * @throws EvasioneLottiException
     *             rlanciata se ho lotti obbligatori e non li ho nelle righeEvasione.
     */
    void evadiOrdini(List<RigaDistintaCarico> righeEvasione, AreaMagazzino documentoEvasione)
            throws EvasioneLottiException;

    /**
     * Effettua l'evasione della lista di righe passate come parametro.
     *
     * @param righeEvasione
     *            righe da evadere
     * @param dataEvasione
     *            data di evasione
     * @return documenti evasi
     * @throws EntitaSenzaTipoDocumentoEvasioneException
     *             eccezione sollevata nel caso in cui esistano entità senza tipo documento di
     *             evasione
     * @throws ContabilizzazioneException
     *             sollevata in caso di errori durante la contabilizzazione
     * @throws ContiBaseException
     *             sollevata se manca il conto base SpeseIncasso
     * @throws TipoAreaPartitaDestinazioneRichiestaException
     *             sollevata se l'ordine che si sta evadendo non prevede un tipo area partita e il
     *             documento di destinazione si
     * @throws CodicePagamentoEvasioneAssenteException
     *             sollevata se non è possibile recuperare il codice pagamento dalla sede
     * @throws CodicePagamentoAssenteException
     *             sollevata se l'ordine con tipo area partite non ha un codice di pagamento
     * @throws LottiException
     *             errore sui lotti
     */
    List<AreaMagazzino> evadiOrdini(List<RigaDistintaCarico> righeEvasione, Date dataEvasione)
            throws EntitaSenzaTipoDocumentoEvasioneException, ContabilizzazioneException, ContiBaseException,
            TipoAreaPartitaDestinazioneRichiestaException, CodicePagamentoEvasioneAssenteException,
            CodicePagamentoAssenteException, LottiException;

    /**
     * Forza tutte le righe ordine.
     *
     * @param righe
     *            righe da forzare
     */
    void forzaRigheOrdine(List<Integer> righe);

    /**
     * Genera tutte righe ordine in base alle righe inserimento.
     *
     * @param righeInserimento
     *            righe inserimento
     * @param areaOrdine
     *            area ordine su cui verranno create le righe
     */
    void generaRigheOrdine(List<RigaOrdineInserimento> righeInserimento, AreaOrdine areaOrdine);

    /**
     *
     * Crea e aslva le righe di magazzino per il raggruppamento voluto.
     *
     * @param idAreaOrdine
     *            area magazzino alla quale associare le nuove righe
     * @param provenienzaPrezzo
     *            provenienza del prezzo. Da listino (comprende anche i contratti) o da costoUltimo.
     * @param idRaggruppamentoArticoli
     *            id del raggruppamento da inserire
     * @param data
     *            data del documento
     * @param idSedeEntita
     *            sede magazzino documento
     * @param idListinoAlternativo
     *            listino alternativo documento
     * @param idListino
     *            listino documento
     * @param importo
     *            importo con parametri di default per la valuta settati
     * @param codiceIvaAlternativo
     *            coedice iva da usare sulla riga articolo, se null viene usato il codice iva
     *            dell'articolo
     * @param idTipoMezzo
     *            = id del tipo mezzo
     * @param idZonaGeografica
     *            id della zona geografica
     * @param noteSuDestinazione
     *            imposta se stampare le note riga sul documento di destinazione
     * @param codiceValuta
     *            codice della valuta di riferimento
     * @param codiceLingua
     *            codice della lingua di riferimento
     * @param dataConsegna
     *            data di consegna
     * @param idAgente
     *            id agente
     * @param tipologiaCodiceIvaAlternativo
     *            tipologia co. iva sost.
     * @param percentualeScontoCommerciale
     *            percentuale sconto commerciale
     * @throws RimanenzaLottiNonValidaException
     *             rilanciata se il lotto non ha più quantità disponibili
     */
    void inserisciRaggruppamentoArticoli(Integer idAreaOrdine, ProvenienzaPrezzo provenienzaPrezzo,
            Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
            Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
            Integer idZonaGeografica, boolean noteSuDestinazione, String codiceValuta, String codiceLingua,
            Date dataConsegna, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
            BigDecimal percentualeScontoCommerciale) throws RimanenzaLottiNonValidaException;

    /**
     * Ricalcola i prezzi dell'area ordine e sue righe e la restituisce.
     *
     * @param idAreaOrdine
     *            id area ordine
     * @return area ordine ricalcolata
     */
    AreaOrdineFullDTO ricalcolaPrezziOrdine(Integer idAreaOrdine);

    /**
     * metodo di ricerca per {@link AreaOrdine}.
     *
     * @param parametriRicercaAreaOrdine
     *            parametri per la ricerca
     *
     * @return Collection di {@link AreaOrdineRicerca} che soddisfano i criteri di ricerca
     */
    List<AreaOrdineRicerca> ricercaAreeOrdine(ParametriRicercaAreaOrdine parametriRicercaAreaOrdine);

    /**
     * Salva una {@link AreaOrdine}.
     *
     * @param areaOrdine
     *            area da salvare
     * @param areaRate
     *            area rate associata all'area ordine da salvare
     * @return area salvata
     */
    AreaOrdineFullDTO salvaAreaOrdine(AreaOrdine areaOrdine, AreaRate areaRate);

    /**
     * Salva la riga ordine senza effettuare nessun controllo.
     *
     * @param rigaOrdine
     *            Riga ordine da salvare
     * @param checkRiga
     *            indica se eseguire i vari controlli e salvare i dati collegati che cambiamo(come
     *            lo stato dell'area oridne)
     * @return Riga ordine salvata
     */
    RigaOrdine salvaRigaOrdine(RigaOrdine rigaOrdine, boolean checkRiga);

    /**
     * Salva una riga ordine importata. Restituisce una lista di righe perchè nel caso di una
     * modifica di una proprietà dell'ordine importato ( es. codice di pagamento ) verranno
     * modificate tutte le righe dell'ordine.
     *
     * @param rigaOrdine
     *            riga da salvare
     * @return riga/righe salvata/e
     */
    List<RigaOrdineImportata> salvaRigaOrdineImportata(RigaOrdineImportata rigaOrdine);

    /**
     * Salva le righe magazzino senza eseguire i controlli.
     *
     * @param righePerArea
     *            righe articolo
     */
    void salvaRigheMagazzinoNoCheck(
            Map<AreaMagazzino, List<it.eurotn.panjea.magazzino.domain.RigaArticolo>> righePerArea);

    /**
     *
     * @param tipoAreaOrdine
     *            tipoAreaOrdine da salvare.
     * @return tipoAreaOrdine salvata
     */
    TipoAreaOrdine salvaTipoAreaOrdine(TipoAreaOrdine tipoAreaOrdine);

    /**
     *
     * @param righeDaSpostare
     *            id delle righe da spostare all'interno del documento
     * @param idDest
     *            id della riga di riferimento per lo spostamento. Le righe verranno spostate sopra
     *            questa
     */
    void spostaRighe(Set<Integer> righeDaSpostare, Integer idDest);

    /**
     * Totalizza il documento<br/>
     * . Calcola il totale del documento e i vari totalizzatori (spese, spese varie etc...).<br/>
     * Non salva il documento lo totalizza solamente
     *
     * @param areaOrdine
     *            areaOrdine da totalizzare
     * @param areaPartite
     *            area partite
     * @return areaOrdine totalizzata
     */
    AreaOrdine totalizzaDocumento(AreaOrdine areaOrdine, AreaPartite areaPartite);

    /**
     * Valida le righe ordine.
     *
     * @param areaOrdine
     *            area ordine
     * @param areaRate
     *            area rate
     * @param cambioStato
     *            indica se al termine della verifica si dovrà cambiare lo stato dell'area
     * @return area ordine
     * @throws TotaleDocumentoNonCoerenteException
     */
    AreaOrdineFullDTO validaRigheOrdine(AreaOrdine areaOrdine, AreaRate areaRate, boolean cambioStato);

    /**
     * @return numero di ordini pronti per l'evasione (ordini presenti nell {@link DistintaCarico}
     */
    int verificaNumeroOrdiniDaEvadere();
}
