package it.eurotn.panjea.magazzino.service.interfaces;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentiCollegatiPresentiException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.NoteAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.OperazioneAreaContabileNonTrovata;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ProvenienzaPrezzo;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;
import it.eurotn.panjea.magazzino.exception.EsportaDocumentoCassaException;
import it.eurotn.panjea.magazzino.exception.RigaArticoloNonValidaException;
import it.eurotn.panjea.magazzino.exception.SedeNonAppartieneAdEntitaException;
import it.eurotn.panjea.magazzino.exception.TotaleDocumentoNonCoerenteException;
import it.eurotn.panjea.magazzino.importer.util.DocumentoImport;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.AreaMagazzinoManager;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.ListinoPrezziDTO;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.ParametriListinoPrezzi;
import it.eurotn.panjea.magazzino.manager.omaggio.exception.CodiceIvaPerTipoOmaggioAssenteException;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.service.exception.CategoriaContabileAssenteException;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.service.exception.FatturazioneContabilizzazioneException;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTOStampa;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.PreFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRegoleValidazioneRighe;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.RigheMagazzinoDTOResult;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.rate.domain.AreaRate;

@Remote
public interface MagazzinoDocumentoService {

    /**
     * Salva i nuovi prezzi delle righe e riconferma le aree magazzino.
     *
     * @param righe
     *            lista di righeLite con il nuovo prezzo
     */
    void aggiornaPrezzoRighe(List<RigaArticoloLite> righe);

    /**
     * Aggiunge una riga componente ad una rigaDistinta
     *
     * @param idArticolo
     *            idArticolo
     * @param qta
     *            qta per il componente
     * @param rigaDistinta
     *            distinta alla quale aggiungere
     * @return riga aggiunta
     */
    RigaArticoloComponente aggiungiRigaComponente(Integer idArticolo, double qta, RigaArticolo rigaDistinta);

    /**
     * Aggiunge una variazione e modifica la percentuale di variazione a ogni riga articolo
     * dell'area magazzino.
     *
     * @param idAreaMagazzino
     *            area magazzino
     * @param variazione
     *            variazione
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
    void aggiungiVariazione(Integer idAreaMagazzino, BigDecimal variazione, BigDecimal percProvvigione,
            RigaDocumentoVariazioneScontoStrategy variazioneScontoStrategy,
            TipoVariazioneScontoStrategy tipoVariazioneScontoStrategy,
            RigaDocumentoVariazioneProvvigioneStrategy variazioneProvvigioneStrategy,
            TipoVariazioneProvvigioneStrategy tipoVariazioneProvvigioneStrategy);

    /**
     * Applica la lista delle regole di validazione a tutte le righe articolo delle aree magazzino.
     *
     * @param parametriRegoleValidazioneRighe
     *            parametri di validazione
     * @return lista di righe che non sono valide
     */
    List<RigaArticoloLite> applicaRegoleValidazione(ParametriRegoleValidazioneRighe parametriRegoleValidazioneRighe);

    /**
     *
     * @param parametriCalcoloPrezzi
     *            parametri per il calcolo del prezzo
     * @return politica prezzo contenente i dati del prezzo per l'articolo
     */
    PoliticaPrezzo calcolaPrezzoArticolo(ParametriCalcoloPrezzi parametriCalcoloPrezzi);

    /**
     * cambia lo stato de un documeto da confermatoa provisorio.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @return {@link AreaMagazzino}
     */
    AreaMagazzino cambiaStatoDaConfermatoInProvvisorio(AreaMagazzino areaMagazzino);

    /**
     * cambia stato da provisori a confermato.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @return {@link AreaMagazzino}
     */
    AreaMagazzino cambiaStatoDaProvvisorioInConfermato(AreaMagazzino areaMagazzino);

    /**
     *
     * cambia stato di un documento da provisori a forzato.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @return {@link AreaMagazzino}
     */
    AreaMagazzino cambiaStatoDaProvvisorioInForzato(AreaMagazzino areaMagazzino);

    /**
     * Cambia lo stato di spedizione del movimento restituendo quello nuovo.
     *
     * @param areaDocumento
     *            area documento di riferimento
     * @return nuovo stato
     */
    StatoSpedizione cambiaStatoSpedizioneMovimento(IAreaDocumento areaDocumento);

    /**
     * cancelazione di un'area magazzino.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @throws DocumentiCollegatiPresentiException
     *             .
     * @throws TipoDocumentoBaseException
     *             .
     * @throws AreeCollegatePresentiException
     *             .
     */
    void cancellaAreaMagazzino(AreaMagazzino areaMagazzino)
            throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException, AreeCollegatePresentiException;

    /**
     * cancelazione di un'area magazzino.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @param deleteAreeCollegate
     *            se cancellare o meno le aree colegate
     * @param forceDeleteAreeCollegate
     *            se forzar o meno la cancellazione delle aree colegate
     * @throws DocumentiCollegatiPresentiException
     *             .
     * @throws TipoDocumentoBaseException
     *             .
     * @throws AreeCollegatePresentiException
     *             .
     */
    void cancellaAreaMagazzino(AreaMagazzino areaMagazzino, boolean deleteAreeCollegate,
            boolean forceDeleteAreeCollegate) throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException,
                    AreeCollegatePresentiException;

    /**
     * cancellazione di un'area magazzino.
     *
     * @param areeMagazzino
     *            {@link AreaMagazzino}
     * @param deleteAreeCollegate
     *            se cancellare le aree collegate al documento
     * @throws DocumentiCollegatiPresentiException
     *             .
     * @throws TipoDocumentoBaseException
     *             .
     * @throws AreeCollegatePresentiException
     *             .
     */
    void cancellaAreeMagazzino(List<AreaMagazzino> areeMagazzino, boolean deleteAreeCollegate)
            throws DocumentiCollegatiPresentiException, TipoDocumentoBaseException, AreeCollegatePresentiException;

    /**
     * Cancella i movimenti in fatturazione creati da una fatturazione differita temporanea
     * dell'utente specificato.
     *
     * @param utente
     *            utente di riferimento
     */
    void cancellaMovimentiInFatturazione(String utente);

    /**
     * cancella una riga magazzino.
     *
     * @param rigaMagazzino
     *            {@link RigaMagazzino} da cancellare
     * @return {@link AreaMagazzino}
     */
    AreaMagazzino cancellaRigaMagazzino(RigaMagazzino rigaMagazzino);

    /**
     * Cancella la lista di rigaMagazzino ricevuta, ogni riga verrà cancellata in accordo con il
     * tipo;<br>
     * vengono inoltre ordinate le righe in modo da cancellare per ultime le righe testata (in modo
     * da non avere righe collegate che blocchino la cancellazione).
     *
     * @param righeMagazzino
     *            le righe da eliminare
     * @return AreaMagazzino
     */
    AreaMagazzino cancellaRigheMagazzino(List<RigaMagazzino> righeMagazzino);

    /**
     * cancella un tipo area magazzino.
     *
     * @param tipoAreaMagazzino
     *            {@link TipoAreaMagazzino}
     */
    void cancellaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino);

    /**
     * metodo incaricato di eseguire il caricamento di {@link AreaMagazzino}.
     *
     * @param areaMagazzino
     *            da cancellare.
     * @return {@link AreaMagazzino}
     */
    AreaMagazzino caricaAreaMagazzino(AreaMagazzino areaMagazzino);

    /**
     * metodo incaricato di eseguire il caricamento di {@link AreaMagazzinoFullDTO}.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}.
     * @return {@link AreaMagazzinoFullDTO}
     */
    AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTO(AreaMagazzino areaMagazzino);

    /**
     * Ulitilizzata per la stampa del documento.
     *
     * @param parametri
     *            .
     * @return {@link AreaMagazzinoFullDTOStampa}
     */
    AreaMagazzinoFullDTOStampa caricaAreaMagazzinoFullDTO(Map<Object, Object> parametri);

    /**
     * carica l'areamagazzino di un documento.
     *
     * @param documento
     *            {@link Documento}
     * @return {@link AreaMagazzinoFullDTO}
     */
    AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTOByDocumento(Documento documento);

    /**
     *
     * @param parametriRicercaFatturazione
     *            .
     * @return lista {@link AreaMagazzinoLite}
     */
    List<AreaMagazzinoLite> caricaAreeMagazzino(ParametriRicercaFatturazione parametriRicercaFatturazione);

    /**
     * @see AreaMagazzinoManager#caricaAreeMagazzinoCollegate(List)
     * @param areeMagazzino
     *            areeMagazzino
     * @return areeMagazzino collegate
     */
    List<AreaMagazzinoLite> caricaAreeMagazzinoCollegate(List<AreaMagazzino> areeMagazzino);

    /**
     * Carica tutte le aree magazzino che hanno la richiesta di uno o più dati accompagnatori
     * configurati sul tipo area.
     *
     * @param dataEvasione
     *            data di evasione
     * @return aree con richiesta dati accompagnatori
     */
    List<AreaMagazzinoRicerca> caricaAreeMagazzinoConRichiestaDatiAccompagnatori(Date dataEvasione);

    /**
     * Carica tutte le aree magazzino che hanno la richiesta di uno o più dati accompagnatori
     * configurati sul tipo area.
     *
     * @param idAree
     *            id aree
     * @return aree con richiesta dati accompagnatori
     */
    List<AreaMagazzinoRicerca> caricaAreeMagazzinoConRichiestaDatiAccompagnatori(List<Integer> idAree);

    /**
     * Carica tutte le aree magazzino che prevedono un {@link AreaContabile} ma che non è ancora
     * stata creata.
     *
     * @param tipoGenerazione
     *            tipo generazione
     * @param anno
     *            anno di riferimento
     * @return lista di aree trovate
     */
    List<AreaMagazzinoRicerca> caricaAreeMAgazzinoDaContabilizzare(TipoGenerazione tipoGenerazione, int anno);

    /**
     * Carica il codice iva associato al tipo omaggio scelto. Se il tipoOmaggio non deve cambiare il
     * codice iva ritorna il codice dell'articolo.
     *
     *
     * @param rigaArticolo
     *            rigaArticolo di cui caricare il codice iva in relazione all'omaggio. Se
     *            TipoOmaggio è null viene impostato su TipoOmaggio.NESSUNO
     * @return CodiceIva associato al tipo omaggio
     * @throws CodiceIvaPerTipoOmaggioAssenteException
     *             il codice iva per il tipo omaggio non è definito
     */
    CodiceIva caricaCodiceIvaPerSostituzione(IRigaArticoloDocumento rigaArticolo)
            throws CodiceIvaPerTipoOmaggioAssenteException;

    /**
     *
     * @return {@link Date}
     */
    Date caricaDataMovimentiInFatturazione();

    /**
     * Carica i dati generazione della fatturazione temporanea.
     *
     * @return generazione caricati
     */
    List<DatiGenerazione> caricaDatiGenerazioneFatturazioneTemporanea();

    /**
     * Carica tutti i documenti in base all'importer specificato.
     *
     * @param codiceImporter
     *            codice dell'importer importer
     * @param fileImport
     *            file da importare
     * @return documenti caricati
     */
    Collection<DocumentoImport> caricaDocumenti(String codiceImporter, byte[] fileImport);

    /**
     * carica fatturazioni di un'anno.
     *
     * @param annoFatturazione
     *            anno delle fatturazioni.
     * @return lista {@link DatiGenerazione}
     */
    List<DatiGenerazione> caricaFatturazioni(int annoFatturazione);

    /**
     * Restituisce gli id delle aree magazzino ordinati per la stampa dell'evasione.
     *
     * @param aree
     *            aree magazzino
     * @return id ordinati
     */
    List<Integer> caricaIdAreeMagazzinoPerStampaEvasione(List<AreaMagazzinoRicerca> aree);

    /**
     * Carica tutti i codici degli importer presenti.
     *
     * @return importer caricati
     */
    List<String> caricaImporter();

    /**
     * 
     * @param parametriListinoPrezzi
     *            par per il calcolo del prezzo
     * @return lista di prezzi per articolo
     */
    List<ListinoPrezziDTO> caricaListinoPrezzi(ParametriListinoPrezzi parametriListinoPrezzi);

    /**
     * Carica tutti i movimenti generati dalla fatturazione della data di riferimento.
     *
     * @param dataCreazione
     *            data di riferimento
     * @return <code>List</code> di {@link AreaMagazzinoLite} caricate
     */
    List<AreaMagazzinoLite> caricaMovimentiPerFatturazione(Date dataCreazione);

    /**
     * Carica tutti i movimenti per la fatturazione con la data creazione richiesta. Se la data di
     * creazione è nulla verranno caricati tutti i documenti in fatturazione temporanea.
     *
     * @param dataCreazione
     *            data di riferimento
     * @param utente
     *            utente
     * @return movimenti caricati
     */
    List<MovimentoFatturazioneDTO> caricaMovimentPerFatturazione(Date dataCreazione, String utente);

    /**
     * carica le note della sede.
     *
     * @param sedeEntita
     *            {@link SedeEntita}
     * @return {@link NoteAreaMagazzino}
     */
    NoteAreaMagazzino caricaNoteSede(SedeEntita sedeEntita);

    /**
     *
     * @param rigaMagazzino
     *            {@link RigaMagazzino} da caricare.
     * @return {@link RigaMagazzino}
     */
    RigaMagazzino caricaRigaMagazzino(RigaMagazzino rigaMagazzino);

    /**
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @return lista {@link AreaMagazzino}
     */
    List<? extends RigaMagazzino> caricaRigheMagazzino(AreaMagazzino areaMagazzino);

    /**
     * Carica tutte le righe magazzino collegate alle riga passata come parametro.
     *
     * @param rigaMagazzino
     *            riga magazzino di riferimento
     * @return righe magazzino caricate
     */
    List<RigaDestinazione> caricaRigheMagazzinoCollegate(RigaMagazzino rigaMagazzino);

    /**
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @return {@link AreaMagazzinoDTO}
     */
    RigheMagazzinoDTOResult caricaRigheMagazzinoDTO(AreaMagazzino areaMagazzino);

    /**
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @return lista {@link AreaMagazzino}
     */
    List<? extends RigaMagazzino> caricaRigheMagazzinoStampa(AreaMagazzino areaMagazzino);

    /**
     *
     * @param sedeEntita
     *            {@link SedeEntita}
     * @return {@link SedeAreaMagazzinoDTO}
     */
    SedeAreaMagazzinoDTO caricaSedeAreaMagazzinoDTO(SedeEntita sedeEntita);

    /**
     * @param valueSearch
     *            valore da filtrare
     * @param fieldSearch
     *            campo da filtrare
     * @param loadTipiDocumentoDisabilitati
     *            se prendere o meno quelli disabilitati.
     * @return lista {@link TipoAreaMagazzino}.
     */
    List<TipoAreaMagazzino> caricaTipiAreaMagazzino(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentoDisabilitati);

    /**
     *
     * @return lista {@link TipoDocumento}
     */
    List<TipoDocumento> caricaTipiDocumentiMagazzino();

    /**
     *
     * @return {@link TipoDocumento}
     */
    List<TipoDocumento> caricaTipiDocumentoAnagraficaPerFatturazione();

    /**
     *
     * @return {@link TipoDocumento}
     */
    List<TipoDocumento> caricaTipiDocumentoDestinazioneFatturazione();

    /**
     *
     * @param tipoDocumentoDiFatturazione
     *            {@link TipoDocumento}
     * @return lista {@link TipoDocumento}
     */
    List<TipoDocumento> caricaTipiDocumentoPerFatturazione(TipoDocumento tipoDocumentoDiFatturazione);

    /**
     * metodo che restituisce {@link TipoAreaMagazzino} sempre l'oggetto da caricare e non l'id.
     *
     * @param id
     *            id del tipo area da caricare
     * @return tipo area caricata
     */
    TipoAreaMagazzino caricaTipoAreaMagazzino(Integer id);

    /**
     *
     * @param idTipoDocumento
     *            id del tipo documento.
     * @return tipo {@link AreaMagazzino}
     */
    TipoAreaMagazzino caricaTipoAreaMagazzinoPerTipoDocumento(Integer idTipoDocumento);

    /**
     *
     * @param tipoDocumento
     *            {@link TipoDocumento}
     * @return {@link TipoAreaPartita}
     */
    TipoAreaPartita caricaTipoAreaPartitaPerTipoDocumento(TipoDocumento tipoDocumento);

    /**
     * Carica l'ultimo inventario presente per il deposito specificato.
     *
     * @param idDeposito
     *            id del deposito
     * @return ultimo inventario, <code>null</code> se non esiste
     */
    AreaMagazzinoLite caricaUltimoInventario(Integer idDeposito);

    /**
     *
     * @param righeDaCollegare
     *            lista {@link RigaMagazzino}
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @param ordinamento
     *            indica se calcolare l'ordinamento delle righe nell'area
     */
    void collegaRigaMagazzino(List<RigaMagazzino> righeDaCollegare, AreaMagazzino areaMagazzino, boolean ordinamento);

    /**
     *
     * @param righeMagazzinoDaCambiare
     *            righe da collegare all'ultima testata inserita
     */
    void collegaTestata(Set<Integer> righeMagazzinoDaCambiare);

    /**
     * @param utente
     *            utente
     * @return {@link DatiGenerazione}
     * @throws FatturazioneContabilizzazioneException
     *             .
     */
    DatiGenerazione confermaMovimentiInFatturazione(String utente) throws FatturazioneContabilizzazioneException;

    /**
     * Contabilizza tutte le aree magazzino con l'UUID specificato. La creazione dell'area contabile
     * viene gestita dal flag generaAreaContabile presente dul tipo area magazzino del documento di
     * destinazione. Se il parametro forzaGenerazioneAreaContabile viene impostato a
     * <code>true</code> l'area contabile verrà creata sempre.
     *
     * @param idAreeMagazzino
     *            lista di aree magazzino da contabilizzare
     * @param forzaGenerazioneAreaContabile
     *            forza la generazione dell'area contabile
     * @throws ContiBaseException
     *             ContiBaseException
     * @throws CategoriaContabileAssenteException
     *             CategoriaContabileAssenteException
     * @throws ContabilizzazioneException
     *             ContabilizzazioneException
     */
    void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile)
            throws ContiBaseException, CategoriaContabileAssenteException, ContabilizzazioneException;

    /**
     * Contabilizza tutte le aree magazzino con l'UUID specificato. La creazione dell'area contabile
     * viene gestita dal flag generaAreaContabile presente dul tipo area magazzino del documento di
     * destinazione. Se il parametro forzaGenerazioneAreaContabile viene impostato a
     * <code>true</code> l'area contabile verrà creata sempre.
     *
     * @param idAreeMagazzino
     *            lista di aree magazzino da contabilizzare
     * @param forzaGenerazioneAreaContabile
     *            forza la generazione dell'area contabile
     * @param annoContabile
     *            anno contabile
     * @throws ContiBaseException
     *             ContiBaseException
     * @throws CategoriaContabileAssenteException
     *             CategoriaContabileAssenteException
     * @throws ContabilizzazioneException
     *             ContabilizzazioneException
     */
    void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile,
            int annoContabile)
                    throws ContiBaseException, CategoriaContabileAssenteException, ContabilizzazioneException;

    /**
     * Crea una riga articolo.
     *
     * @param parametriCreazioneRigaArticolo
     *            di creazione
     * @return nuova rigaArticolo con i parametri settatti.
     */
    RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametriCreazioneRigaArticolo);

    /**
     * Crea una riga note automatica per l'area magazzino indicata.
     *
     * @param areaMagazzino
     *            area magazzino di riferimento
     * @param note
     *            note
     * @return <code>true</code> se la riga viene generata e salvata correttamente
     */
    boolean creaRigaNoteAutomatica(AreaMagazzino areaMagazzino, String note);

    /**
     * Esporta i dati del documento.
     *
     * @param areaMagazzino
     *            area magazzino di riferimento
     * @throws EsportaDocumentoCassaException
     *             rilanciato se ci sono errori nell'esportazione
     */
    void esportaDocumento(AreaMagazzino areaMagazzino) throws EsportaDocumentoCassaException;

    /**
     *
     * @param areeDaFatturare
     *            Lista di aree da fatturare
     * @param tipoDocumentoDestinazione
     *            tipo di documento da generare
     * @param dataDocumentoDestinazione
     *            data del documento da generare
     * @param noteFatturazione
     *            note da inserire nel documento
     * @param sedePerRifatturazione
     *            sede per la rigatturazione
     * @throws RigaArticoloNonValidaException
     *             rilanciata se ci sono delle riche non valide
     * @throws SedePerRifatturazioneAssenteException
     *             rilanciata se non si ha una sede di rifatturazione sull'entità
     * @throws SedeNonAppartieneAdEntitaException
     *             SedeNonAppartieneAdEntitaException
     */
    void generaFatturazioneDifferitaTemporanea(List<AreaMagazzinoLite> areeDaFatturare,
            TipoDocumento tipoDocumentoDestinazione, Date dataDocumentoDestinazione, String noteFatturazione,
            SedeMagazzinoLite sedePerRifatturazione) throws RigaArticoloNonValidaException,
                    SedePerRifatturazioneAssenteException, SedeNonAppartieneAdEntitaException;

    /**
     *
     * @param areeDaFatturare
     *            areeDaFatturare
     * @param tipoDocumentoDestinazione
     *            tipoDocumentoDestinazione
     * @param dataDocumentoDestinazione
     *            dataDocumentoDestinazione
     * @param noteFatturazione
     *            noteFatturazione
     * @param sedePerRifatturazione
     *            sede per rifatturazione
     * @return movimenti creati
     * @throws RigaArticoloNonValidaException
     *             lanciata quando una riga non è valida
     * @throws SedePerRifatturazioneAssenteException
     *             lanciata quando almeno un'area non ha una sede per rifatturazione
     * @throws SedeNonAppartieneAdEntitaException
     *             SedeNonAppartieneAdEntitaException
     */
    PreFatturazioneDTO generaPreFatturazione(List<AreaMagazzinoLite> areeDaFatturare,
            TipoDocumento tipoDocumentoDestinazione, Date dataDocumentoDestinazione, String noteFatturazione,
            SedeMagazzinoLite sedePerRifatturazione) throws RigaArticoloNonValidaException,
                    SedePerRifatturazioneAssenteException, SedeNonAppartieneAdEntitaException;

    /**
     * Importa i documenti.
     *
     * @param documenti
     *            documenti da importare
     * @param codiceImporter
     *            importer associato
     * @return aree create
     */
    List<AreaMagazzinoRicerca> importaDocumenti(Collection<DocumentoImport> documenti, String codiceImporter);

    /**
     *
     * @param datiGenerazione
     *            {@link DatiGenerazione}
     */
    void impostaComeEsportato(DatiGenerazione datiGenerazione);

    /**
     *
     * Crea e aslva le righe di magazzino per il raggruppamento voluto.
     *
     * @param idAreaMagazzino
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
     * @param tipoMovimento
     *            tipo movimento
     * @param codiceValuta
     *            codice della valuta di riferimento
     * @param codiceLingua
     *            codice della lingua di riferimento
     * @param tipologiaCodiceIvaAlternativo
     *            codive iva alternativo
     * @param percentualeScontoCommerciale
     *            percentuale sconto commerciale
     * @param idAgente
     *            id agente
     * @throws RimanenzaLottiNonValidaException
     *             rilanciata se il lotto non ha più quantità disponibili
     * @throws RigheLottiNonValideException
     *             rilanciata se le righe lotto della riga magazzino non sono valide
     * @throws QtaLottiMaggioreException
     *             rilanciata se la quantità assegnata ai lotti supera la quantità della riga
     *             articolo
     */
    void inserisciRaggruppamentoArticoli(Integer idAreaMagazzino, ProvenienzaPrezzo provenienzaPrezzo,
            Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
            Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
            Integer idZonaGeografica, boolean noteSuDestinazione, TipoMovimento tipoMovimento, String codiceValuta,
            String codiceLingua, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
            BigDecimal percentualeScontoCommerciale)
                    throws RimanenzaLottiNonValidaException, RigheLottiNonValideException, QtaLottiMaggioreException;

    /**
     * Ricalcola i prezzi dell'area ordine e sue righe e la restituisce.
     *
     * @param idAreaMagazzino
     *            id area magazzino
     * @return area magazzino FullDTO con prezzi ricalcolati
     */
    AreaMagazzinoFullDTO ricalcolaPrezziMagazzino(Integer idAreaMagazzino);

    /**
     *
     * @param paramentriRicercaAreaMagazzino
     *            {@link ParametriRicercaAreaMagazzino}
     * @return lista {@link AreaMagazzinoRicerca}
     */
    List<AreaMagazzinoRicerca> ricercaAreeMagazzino(ParametriRicercaAreaMagazzino paramentriRicercaAreaMagazzino);

    /**
     *
     * @param documento
     *            {@link Documento}
     * @param soloAttributiNotNull
     *            .
     * @return lista {@link Documento}
     */
    List<Documento> ricercaDocumentiSenzaAreaMagazzino(Documento documento, boolean soloAttributiNotNull);

    /**
     * metodo incaricato di eseguire il salvataggio di {@link AreaMagazzino} e di
     * {@link AreaPartite} se necessario<br>
     * l'attributo forzaSalvataggio indica di eseguire il salvataggio di {@link AreaMagazzino} se
     * non esiste {@link AreaContabile} <br>
     * per lo stesso {@link Documento} per bypassare l'indicatore
     * {@link OperazioneAreaContabileNonTrovata} AVVISARE.
     *
     *
     * @param areaMagazzino
     *            .
     * @param areaRate
     *            .
     * @param forzaSalvataggio
     *            .
     * @return .
     * @throws DocumentiEsistentiPerAreaMagazzinoException
     *             .
     * @throws DocumentoAssenteBloccaException
     *             .
     * @throws DocumentoAssenteAvvisaException
     *             .
     */
    AreaMagazzinoFullDTO salvaAreaMagazzino(AreaMagazzino areaMagazzino, AreaRate areaRate, boolean forzaSalvataggio)
            throws DocumentoAssenteAvvisaException, DocumentoAssenteBloccaException,
            DocumentiEsistentiPerAreaMagazzinoException;

    /**
     * salva una riga magazzino.
     *
     * @param rigaMagazzino
     *            {@link RigaMagazzino}
     * @return {@link RigaMagazzino}
     * @throws RigheLottiNonValideException
     *             sollevata se le righe lotti non sono valide
     * @throws RimanenzaLottiNonValidaException
     *             rilanciata se la rimanenza dei lotti non risulta valida
     * @throws QtaLottiMaggioreException
     *             rilanciata se la quantità assegnata ai lotti supera la quantità della riga
     *             articolo
     */
    RigaMagazzino salvaRigaMagazzino(RigaMagazzino rigaMagazzino)
            throws RigheLottiNonValideException, RimanenzaLottiNonValidaException, QtaLottiMaggioreException;

    /**
     * salva tipo area magazzino.
     *
     * @param tipoAreaMagazzino
     *            {@link TipoAreaMagazzino} .
     * @return {@link TipoAreaMagazzino}
     */
    TipoAreaMagazzino salvaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino);

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
     * @param strategia
     *            strategia di totalizzazione
     * @param areaMagazzino
     *            areaMagazzino da totalizzare
     * @param areaPartite
     *            area partite
     * @return areaMagazzino totalizzata
     */
    AreaMagazzino totalizzaDocumento(StrategiaTotalizzazioneDocumento strategia, AreaMagazzino areaMagazzino,
            AreaPartite areaPartite);

    /**
     * Valida le righe Iva chiamando il manager areaIvaManager e crea le partite. La chiamata a
     * questo metodo totalizza anche il documento.
     *
     * @param areaIva
     * @return
     */
    AreaMagazzinoFullDTO validaAreaIva(AreaIva areaIva, Integer idAreaMagazzino);

    /**
     * Valida l'area iva partendo da un area di magazzino.
     *
     * @param areaIva
     *            areaIva
     * @param idAreaMagazzino
     *            idAreaMagazzino
     * @param totalizzaDocumento
     *            totalizzaDocumento
     * @return areaMagazzino con areaIva validata
     */
    AreaMagazzinoFullDTO validaAreaIva(AreaIva iva, Integer idAreaMagazzino, boolean totalizzaDocumento);

    /**
     *
     * @param areaMagazzino
     * @param areaRate
     * @param areaContabilePresente
     * @param forzaStato
     * @return {@link AreaMagazzinoFullDTO}
     * @throws TotaleDocumentoNonCoerenteException
     */
    AreaMagazzinoFullDTO validaRigheMagazzino(AreaMagazzino areaMagazzino, AreaRate areaRate,
            Boolean areaContabilePresente, boolean forzaStato) throws TotaleDocumentoNonCoerenteException;
}
