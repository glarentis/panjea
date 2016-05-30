package it.eurotn.panjea.magazzino.rich.bd;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.crypto.Data;

import org.apache.poi.ss.formula.ptg.AreaI;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.AreeCollegatePresentiException;
import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.contabilita.domain.AreaContabileLite;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.service.exception.CodiceIvaCollegatoAssenteException;
import it.eurotn.panjea.documenti.domain.StatoSpedizione;
import it.eurotn.panjea.documenti.util.MovimentoSpedizioneDTO;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.domain.RigaIva;
import it.eurotn.panjea.lotti.exception.RimanenzaLottiNonValidaException;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione;
import it.eurotn.panjea.magazzino.domain.IRigaArticoloDocumento;
import it.eurotn.panjea.magazzino.domain.InventarioArticolo;
import it.eurotn.panjea.magazzino.domain.NoteAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.RigaArticoloComponente;
import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.domain.RigaMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino.StatoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
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
import it.eurotn.panjea.magazzino.manager.listinoprezzi.ListinoPrezziDTO;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.ParametriListinoPrezzi;
import it.eurotn.panjea.magazzino.manager.omaggio.exception.CodiceIvaPerTipoOmaggioAssenteException;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.RigaDocumentoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.provvigioni.interfaces.TipoVariazioneProvvigioneStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.RigaDocumentoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.manager.rigadocumento.variazioni.sconti.interfaces.TipoVariazioneScontoStrategy;
import it.eurotn.panjea.magazzino.service.exception.DocumentiEsistentiPerAreaMagazzinoException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteAvvisaException;
import it.eurotn.panjea.magazzino.service.exception.DocumentoAssenteBloccaException;
import it.eurotn.panjea.magazzino.service.exception.QtaLottiMaggioreException;
import it.eurotn.panjea.magazzino.service.exception.RigheLottiNonValideException;
import it.eurotn.panjea.magazzino.service.exception.SedePerRifatturazioneAssenteException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.magazzino.util.IndiceGiacenzaArticolo;
import it.eurotn.panjea.magazzino.util.InventarioArticoloDTO;
import it.eurotn.panjea.magazzino.util.MovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.MovimentoFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.ParametriCreazioneRigaArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.PreFatturazioneDTO;
import it.eurotn.panjea.magazzino.util.RigaDestinazione;
import it.eurotn.panjea.magazzino.util.RigaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.RigaMovimentazione;
import it.eurotn.panjea.magazzino.util.SedeAreaMagazzinoDTO;
import it.eurotn.panjea.magazzino.util.StatisticheArticolo;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCalcoloIndiciRotazioneGiacenza;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRegoleValidazioneRighe;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriValorizzazioneDistinte;
import it.eurotn.panjea.magazzino.util.rigamagazzino.builder.RigheMagazzinoDTOResult;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;
import it.eurotn.panjea.partite.domain.AreaPartite;
import it.eurotn.panjea.partite.domain.TipoAreaPartita;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;

public interface IMagazzinoDocumentoBD {

    /**
     * Aggiunge una riga componente ad una rigaDistinta.
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
     * Applica le regole di validazione.
     *
     * @param parametriRegoleValidazioneRighe
     *            {@link ParametriRegoleValidazioneRighe}.
     * @return list {@link ArticoloLite}
     */
    @AsyncMethodInvocation
    List<RigaArticoloLite> applicaRegoleValidazione(ParametriRegoleValidazioneRighe parametriRegoleValidazioneRighe);

    /**
     * Setta il valore della giacenza reale con quello della giacenza calcolata per l'inventario
     * articolo del deposito e data richiesti. Se esiste già della giacenza reale viene aggiunto il
     * valore per la differenza.
     *
     * @param data
     *            data
     * @param deposito
     *            deposito
     */
    void avvaloraGiacenzaRealeInventarioArticolo(Date data, DepositoLite deposito);

    /**
     * Calcola la giacenza ad una determinata data per un articolo.<br/>
     * Viene calcolata la giacenza al primo inventario utile (la somma delle qta per articolo nel
     * movimento di inventario) e aggiunta la qta movimentata dalla data dell'inventario fino alla
     * data richiesta.<br/>
     * La giacenza viene calcolata dalle query sql presenti nel file
     * <code>it.eurotn.panjea.magazzino.manager.sqlbuilder.Giacenza.sql</code><br>
     * Considera solamente i movimenti in stato {@link StatoAreaMagazzino#CONFERMATO} e
     * {@link StatoAreaMagazzino#VERIFICATO}
     *
     * @param articolo
     *            articolo interessato al calcolo
     * @param data
     *            data per la giacenza
     * @param depositoLite
     *            deposito interessato alla giacenza
     * @return giacenza dell'articolo e del deposito
     */
    @AsyncMethodInvocation
    Double calcolaDisponibilita(ArticoloLite articolo, Date data, DepositoLite depositoLite);

    /**
     *
     * @param parametri
     *            parametriCalcolo
     * @return indici di rotazione articoli
     */
    List<IndiceGiacenzaArticolo> calcolaIndiciRotazione(ParametriCalcoloIndiciRotazioneGiacenza parametri);

    /**
     * calcola il prezzo di un'articolo.
     *
     * @param parametriCalcoloPrezzi
     *            {@link ParametriCalcoloPrezzi}
     * @return {@link PoliticaPrezzo}
     */
    @AsyncMethodInvocation
    PoliticaPrezzo calcolaPrezzoArticolo(ParametriCalcoloPrezzi parametriCalcoloPrezzi);

    /**
     * Cambia lo stato di spedizione del movimento restituendo quello nuovo.
     *
     * @param areaDocumento
     *            area documento di riferimento
     * @return nuovo stato
     */
    StatoSpedizione cambiaStatoSpedizioneMovimento(IAreaDocumento areaDocumento);

    /**
     * cancella un'area magazzino.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @throws AreeCollegatePresentiException
     *             .
     */
    @AsyncMethodInvocation
    void cancellaAreaMagazzino(AreaMagazzino areaMagazzino) throws AreeCollegatePresentiException;

    /**
     * cancellazione de un'area magazzino.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @param deleteAreeCollegate
     *            boolean per cancellare o meno le aree collegate.
     * @param forceDeleteAreeCollegate
     *            boolean per forzar la cancellazione delle aree colegate.
     * @throws AreeCollegatePresentiException
     *             .
     */
    @AsyncMethodInvocation
    void cancellaAreaMagazzino(AreaMagazzino areaMagazzino, boolean deleteAreeCollegate,
            boolean forceDeleteAreeCollegate) throws AreeCollegatePresentiException;

    /**
     * cancella area magazino.
     *
     * @param areeMagazzino
     *            {@link AreaMagazzino}
     * @param deleteAreeCollegate
     *            definisce se cancellare tutte le aree collegate
     */
    @AsyncMethodInvocation
    void cancellaAreeMagazzino(List<AreaMagazzino> areeMagazzino, boolean deleteAreeCollegate);

    /**
     * Cancella un inventario in preparazione.
     *
     * @param data
     *            data
     * @param deposito
     *            deposito
     */
    @AsyncMethodInvocation
    void cancellaInventarioArticolo(Date data, DepositoLite deposito);

    /**
     * Cancella i movimenti in fatturazione creati da una fatturazione differita temporanea
     * dell'utente specificato.
     *
     * @param utente
     *            utente di riferimento
     */
    @AsyncMethodInvocation
    void cancellaMovimentiInFatturazione(String utente);

    /**
     * cancella una riga iva.
     *
     * @param rigaIva
     *            riga iva a cancellare.
     */
    void cancellaRigaIva(RigaIva rigaIva);

    /**
     * cancella una riga magazzino.
     *
     * @param rigaMagazzino
     *            riga magazzino a cancellare.
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
     *            tipo area amagazzino a cancellare.
     */
    void cancellaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino);

    /**
     * cancella l'area cantabile di un documento.
     *
     * @param idDocumento
     *            documento al quale cancellare l'area contabile
     * @return {@link AreaContabileLite}
     */
    it.eurotn.panjea.contabilita.domain.AreaContabileLite caricaAreaContabileLiteByDocumento(Integer idDocumento);

    /**
     * carica un'area iva.
     *
     * @param areaIva
     *            area a caricare.
     * @return {@link AreaI}
     */
    AreaIva caricaAreaIva(AreaIva areaIva);

    /**
     * carica {@link AreaMagazzino}.
     *
     * @param areaMagazzino
     *            area magazzino a caricare.
     * @return {@link AreaMagazzino}
     */
    AreaMagazzino caricaAreaMagazzino(AreaMagazzino areaMagazzino);

    /**
     * carica area magazinofullDTO.
     *
     * @param areaMagazzino
     *            area magazzino a cui carica l'area magazzino ful DTO
     * @return {@link AreaMagazzinoFullDTO}
     */
    @AsyncMethodInvocation
    AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTO(AreaMagazzino areaMagazzino);

    /**
     * carica area magazinofullDTO.
     *
     * @param documento
     *            documento per qui caricare il aea magazzino fullDTO
     * @return {@link AreaMagazzinoFullDTO}
     */
    @AsyncMethodInvocation
    AreaMagazzinoFullDTO caricaAreaMagazzinoFullDTOByDocumento(Documento documento);

    /**
     * carica area rate.
     *
     * @param documento
     *            documento per caricare l'area rate.
     * @return {@link AreaRate}
     */
    AreaRate caricaAreaRateByDocumento(Documento documento);

    /**
     * carica auan lista di aree magazzino.
     *
     * @param parametriRicercaFatturazione
     *            parametri per la ricerca.
     * @return list {@link AreaMagazzinoLite}
     */
    @AsyncMethodInvocation
    List<AreaMagazzinoLite> caricaAreeMagazzino(ParametriRicercaFatturazione parametriRicercaFatturazione);

    /**
     * carica le aree magazzino collegate.
     *
     * @param areeMagazzino
     *            per cui caricare le areee colegate.
     * @return list {@link AreaMagazzino}
     */
    @AsyncMethodInvocation
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
     * carica data movimenti in fatturazione.
     *
     * @return {@link Data}.
     */
    Date caricaDataMovimentiInFatturazione();

    /**
     * Carica i dati generazione della fatturazione temporanea.
     *
     * @return generazione caricati
     */
    @AsyncMethodInvocation
    List<DatiGenerazione> caricaDatiGenerazioneFatturazioneTemporanea();

    /**
     * Carica tutti i depositi che posso essere utilizzati per la preparazione de un inventario.
     * Vengono esclusi tutti quelli che hanno già un inventario in preparazione.
     *
     * @return depositi
     */
    List<Deposito> caricaDepositiPerInventari();

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
     * carica fatturazioni.
     *
     * @param annoFatturazione
     *            anno di cui caricare le fatturazioni
     * @return list {@link Data}
     */
    @AsyncMethodInvocation
    List<DatiGenerazione> caricaFatturazioni(int annoFatturazione);

    /**
     * Restituisce gli id delle aree magazzino ordinati per la stampa dell'evasione.
     *
     * @param aree
     *            aree magazzino
     * @return id ordinati
     */
    @AsyncMethodInvocation
    List<Integer> caricaIdAreeMagazzinoPerStampaEvasione(List<AreaMagazzinoRicerca> aree);

    /**
     * Carica tutti i codici degli importer presenti.
     *
     * @return importer caricati
     */
    List<String> caricaImporter();

    /**
     * Carica tutti gli inventari articoli presenti raggruppati per data e deposito.
     *
     * @return inventari caricati
     */
    @AsyncMethodInvocation
    List<InventarioArticoloDTO> caricaInventariiArticoli();

    /**
     * Carica l'inventario articolo per data e deposito specificati.
     *
     * @param date
     *            data
     * @param depositoLite
     *            deposito
     * @param caricaGiacenzeAZero
     *            se <code>true</code> carica anche gli articoli con giacenza a 0
     * @return inventari
     */
    @AsyncMethodInvocation
    List<InventarioArticolo> caricaInventarioArticolo(Date date, DepositoLite depositoLite,
            boolean caricaGiacenzeAZero);

    /**
     * Carica l'inventario articolo.
     *
     * @param inventarioArticolo
     *            inventario da caricare
     * @return inventario caricato
     */
    @AsyncMethodInvocation
    InventarioArticolo caricaInventarioArticolo(InventarioArticolo inventarioArticolo);

    /**
     *
     * @param parametriListinoPrezzi
     *            par per il calcolo del prezzo
     * @return lista di prezzi per articolo
     */
    List<ListinoPrezziDTO> caricaListinoPrezzi(ParametriListinoPrezzi parametriListinoPrezzi);

    /**
     * carica movimentazine a pagine.
     *
     * @param parametriRicercaMovimentazione
     *            parmetri di reerca movimentazione.
     * @param page
     *            pagina.
     * @param sizeOfPage
     *            numero di pagine da caricare.
     * @return list {@link RigaMovimentazione}
     */
    @AsyncMethodInvocation
    List<RigaMovimentazione> caricaMovimentazione(ParametriRicercaMovimentazione parametriRicercaMovimentazione,
            int page, int sizeOfPage);

    /**
     * carica movimentazione articolo.
     *
     * @param parametriRicercaMovimentazioneArticolo
     *            parametri per la ricerca della movimentazione articolo.
     * @return {@link MovimentazioneArticolo}
     */
    @AsyncMethodInvocation
    MovimentazioneArticolo caricaMovimentazioneArticolo(
            ParametriRicercaMovimentazioneArticolo parametriRicercaMovimentazioneArticolo);

    /**
     * Carica tutti i movimenti per la fatturazione con la data creazione richiesta. Se la data di
     * creazione è nulla verranno caricati tutti i documenti in fatturazione temporanea.
     *
     * @param dataCreazione
     *            data di riferimento
     * @return list {@link AreaMagazzinoLite}
     */
    @AsyncMethodInvocation
    List<AreaMagazzinoLite> caricaMovimentiPerFatturazione(Date dataCreazione);

    /**
     * Carica tutti i movimenti per la spedizione.
     *
     * @param areaDocumentoClass
     *            classe dei documenti da spedire
     * @param idDocumenti
     *            id documenti
     * @return movimenti caricati
     */
    List<MovimentoSpedizioneDTO> caricaMovimentiPerSpedizione(Class<? extends IAreaDocumento> areaDocumentoClass,
            List<Integer> idDocumenti);

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
    @AsyncMethodInvocation
    List<MovimentoFatturazioneDTO> caricaMovimentPerFatturazione(Date dataCreazione, String utente);

    /**
     * carica le note della seda.
     *
     * @param sedeEntita
     *            sede per cui caricare le note.
     * @return {@link NoteAreaMagazzino}
     */
    NoteAreaMagazzino caricaNoteSede(SedeEntita sedeEntita);

    /**
     * carica una riga iva.
     *
     * @param id
     *            della riga iva cui caricare.
     * @return {@link RigaIva}
     */
    RigaIva caricaRigaIva(Integer id);

    /**
     * carica uan riga magazzino.
     *
     * @param rigaMagazzino
     *            riga magazzino cui caricare.
     * @return {@link RigaMagazzino}
     */
    RigaMagazzino caricaRigaMagazzino(RigaMagazzino rigaMagazzino);

    /**
     * carica le righe magazzino per una area magazzino.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @return list {@link RigaMagazzino}
     */
    @AsyncMethodInvocation
    List<? extends RigaMagazzino> caricaRigheMagazzinobyAreaMagazzino(AreaMagazzino areaMagazzino);

    /**
     * Carica tutte le righe magazzino collegate alle riga passata come parametro.
     *
     * @param rigaMagazzino
     *            riga magazzino di riferimento
     * @return righe magazzino caricate
     */
    List<RigaDestinazione> caricaRigheMagazzinoCollegate(RigaMagazzino rigaMagazzino);

    /**
     * carica una lista de righe magazzino per un'area magazzino.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @return list {@link RigaMagazzinoDTO}
     */
    @AsyncMethodInvocation
    RigheMagazzinoDTOResult caricaRigheMagazzinoDTObyAreaMagazzino(AreaMagazzino areaMagazzino);

    /**
     * carica la sede del {@link AreaMagazzinoFullDTO}.
     *
     * @param sedeEntita
     *            {@link SedeEntita}
     * @return {@link SedeAreaMagazzinoDTO}
     */
    SedeAreaMagazzinoDTO caricaSedeAreaMagazzinoDTO(SedeEntita sedeEntita);

    /**
     * carica le statistiche per un articolo.
     *
     * @param articolo
     *            {@link Articolo}
     * @return {@link StatisticheArticolo}
     */
    @AsyncMethodInvocation
    StatisticheArticolo caricaStatisticheArticolo(Articolo articolo);

    /**
     * carica le statistiche de un'articolo per un'anno.
     *
     * @param articolo
     *            anno da cui caricare le righe.
     * @param anno
     *            anno.
     * @return {@link StatisticheArticolo}
     */
    @AsyncMethodInvocation
    StatisticheArticolo caricaStatisticheArticolo(Articolo articolo, Integer anno);

    /**
     * carica i tipi di areamagazzino.
     *
     * @param valueSearch
     *            valore da filtrare
     * @param fieldSearch
     *            campo da filtrare
     *
     * @param loadTipiDocumentoDisabilitati
     *            se incluedere i documenti disbilitati nella ricerca.
     * @return list {@link TipoAreaMagazzino}
     */
    List<TipoAreaMagazzino> caricaTipiAreaMagazzino(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentoDisabilitati);

    /**
     * carica tipi documeti per la fatturazione.
     *
     * @return list {@link TipoDocumento}
     */
    List<TipoDocumento> caricaTipiDocumentoAnagraficaPerFatturazione();

    /**
     * carica i tipi documeti di destinazione per la fatturazione.
     *
     * @return list {@link TipoDocumento}
     */
    List<TipoDocumento> caricaTipiDocumentoDestinazioneFatturazione();

    /**
     * carica i tipi documenti per la fatturazione.
     *
     * @param tipoDocumentoDiFatturazione
     *            {@link TipoDocumento}
     * @return list {@link TipoDocumento}
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
     * carica tipoareamagazino per un tipo documento.
     *
     * @param id
     *            del tipo documento a cui cercare l'areamagazzino.
     * @return {@link TipoAreaMagazzino}
     */
    TipoAreaMagazzino caricaTipoAreaMagazzinoPerTipoDocumento(Integer id);

    /**
     * carica tipo area partita di un tipo documento.
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
     * carica una valorizzazione.
     *
     * @param parametriRicercaValorizzazione
     *            parametri per la ricerca.
     * @return lista {@link ValorizzazioneArticolo}
     */
    @AsyncMethodInvocation
    List<ValorizzazioneArticolo> caricaValorizzazione(ParametriRicercaValorizzazione parametriRicercaValorizzazione);

    /**
     *
     * @param righeMagazzinoDaCambiare
     *            righe da collegare all'ultima testata inserita
     */
    void collegaTestata(Set<Integer> righeMagazzinoDaCambiare);

    /**
     * conferma i movimenti in fatturazione.
     *
     * @param utente
     *            utente
     * @return {@link DatiGenerazione}
     * @throws Exception
     *             .
     */
    @AsyncMethodInvocation
    DatiGenerazione confermaMovimentiInFatturazione(String utente) throws Exception;

    /**
     * Crea gli inventari articolo.
     *
     * @param data
     *            data di riferimento
     * @param depositi
     *            depositi per i quali creare gli inventari articolo
     */
    @AsyncMethodInvocation
    void creaInventariArticolo(java.util.Date data, List<DepositoLite> depositi);

    /**
     * crea una riga articolo.
     *
     * @param parametri
     *            {@link ParametriCreazioneRigaArticolo}
     * @return {@link RigaArticolo}
     */
    RigaArticolo creaRigaArticolo(ParametriCreazioneRigaArticolo parametri);

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
     * esporto un documento.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @throws EsportaDocumentoCassaException
     *             .
     */
    @AsyncMethodInvocation
    void esportaDocumento(AreaMagazzino areaMagazzino) throws EsportaDocumentoCassaException;

    /**
     * genera una fatturazione differita temporanea.
     *
     * @param areeDaFatturare
     *            l'area su cui fatturare
     * @param tipoDocumentoDestinazione
     *            {@link TipoDocumento} destinazione
     * @param dataDocumentoDestinazione
     *            data documeto destinazione
     * @param noteFatturazione
     *            note per la fatturazione
     * @param sedePerRifatturazione
     *            sede su cui rifatturare
     * @throws RigaArticoloNonValidaException
     *             .
     * @throws SedePerRifatturazioneAssenteException
     *             .
     * @throws SedeNonAppartieneAdEntitaException
     *             .
     */
    @AsyncMethodInvocation
    void generaFatturazioneDifferitaTemporanea(List<AreaMagazzinoLite> areeDaFatturare,
            TipoDocumento tipoDocumentoDestinazione, Date dataDocumentoDestinazione, String noteFatturazione,
            SedeMagazzinoLite sedePerRifatturazione) throws RigaArticoloNonValidaException,
                    SedePerRifatturazioneAssenteException, SedeNonAppartieneAdEntitaException;

    /**
     * Genera l'inventario e gli eventuali movimenti di rettifica.
     *
     * @param dataInventario
     *            data di creazione dell'inventario
     * @param dataInventarioArticolo
     *            data inventario articolo
     * @param deposito
     *            deposito di riferimento
     * @return lista di aree magazzino create ( inventario + eventuale rettifica positiva e/o
     *         negativa )
     */
    @AsyncMethodInvocation
    List<AreaMagazzinoRicerca> generaInventario(Date dataInventario, Date dataInventarioArticolo,
            DepositoLite deposito);

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
     * Importa le giacenze degli articoli contenuti nel file per il deposito specificato.
     *
     * @param fileImportData
     *            file
     * @param idDeposito
     *            deposito di riferimento
     * @return lista di codici articoli presenti nel file di importazione ma non in panjea
     */
    @AsyncMethodInvocation
    List<String> importaArticoliInventario(byte[] fileImportData, Integer idDeposito);

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
     * @param idAgente
     *            id agente
     * @param tipologiaCodiceIvaAlternativo
     *            tipologia iva su area magazzino.
     * @param percentualeScontoCommerciale
     *            percentuale sconto commerciale
     */
    void inserisciRaggruppamentoArticoli(Integer idAreaMagazzino, ProvenienzaPrezzo provenienzaPrezzo,
            Integer idRaggruppamentoArticoli, Date data, Integer idSedeEntita, Integer idListinoAlternativo,
            Integer idListino, Importo importo, CodiceIva codiceIvaAlternativo, Integer idTipoMezzo,
            Integer idZonaGeografica, boolean noteSuDestinazione, TipoMovimento tipoMovimento, String codiceValuta,
            String codiceLingua, Integer idAgente, ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo,
            BigDecimal percentualeScontoCommerciale);

    /**
     *
     * @param righeNonValide
     *            lista {@link RigaArticoloLite}
     */
    @AsyncMethodInvocation
    void modificaPrezziRigheNonValidePerFatturazione(List<RigaArticoloLite> righeNonValide);

    /**
     * Ricalcola i prezzi dell'area ordine e sue righe e la restituisce.
     *
     * @param idAreaMagazzino
     *            id area magazzino
     * @return area magazzino FullDTO con prezzi ricalcolati
     */
    AreaMagazzinoFullDTO ricalcolaPrezziMagazzino(Integer idAreaMagazzino);

    /**
     * ricerca una area magazzino.
     *
     * @param parametriRicercaAreaMagazzino
     *            parametri per la ricerca del area magazzino {@link ParametriRicercaAreaMagazzino}
     * @return lista {@link AreaMagazzinoRicerca}
     */
    @AsyncMethodInvocation
    List<AreaMagazzinoRicerca> ricercaAreeMagazzino(ParametriRicercaAreaMagazzino parametriRicercaAreaMagazzino);

    /**
     * cerca i documeti senza una area magazzino.
     *
     * @param documento
     *            ?.
     * @param soloAttributiNotNull
     *            ?.
     * @return lista {@link Documento}
     */
    @AsyncMethodInvocation
    List<Documento> ricercaDocumentiSenzaAreaMagazzino(Documento documento, boolean soloAttributiNotNull);

    /**
     * salva area magazzino.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @param areaRate
     *            {@link AreaRate}
     * @param forzaSalvataggio
     *            forzar o meno il salvataggio
     * @return {@link AreaMagazzinoFullDTO}
     * @throws DocumentoAssenteBloccaException
     *             .
     * @throws DocumentoAssenteAvvisaException
     *             .
     * @throws DocumentiEsistentiPerAreaMagazzinoException
     *             .
     */
    AreaMagazzinoFullDTO salvaAreaMagazzino(AreaMagazzino areaMagazzino, AreaRate areaRate, boolean forzaSalvataggio)
            throws DocumentoAssenteBloccaException, DocumentoAssenteAvvisaException,
            DocumentiEsistentiPerAreaMagazzinoException;

    /**
     * Salva un inventario articolo.
     *
     * @param inventarioArticolo
     *            inventario da salvare
     * @return inventario salvato
     */
    InventarioArticolo salvaInventarioArticolo(InventarioArticolo inventarioArticolo);

    /**
     * salva riga iva.
     *
     * @param rigaIva
     *            riga iva a salvare
     * @param tipoAreaContabile
     *            {@link TipoAreaContabile}
     * @return {@link RigaIva}
     * @throws CodiceIvaCollegatoAssenteException
     *             .
     */
    RigaIva salvaRigaIva(RigaIva rigaIva, TipoAreaContabile tipoAreaContabile)
            throws CodiceIvaCollegatoAssenteException;

    /**
     * salva riga magazzino.
     *
     * @param rigaMagazzino
     *            {@link RigaMagazzino} a salvare.
     * @return {@link RigaMagazzino}
     * @throws RimanenzaLottiNonValidaException
     *             rilanciata se la rimanenza dei lotti non risulta valida
     * @throws RigheLottiNonValideException
     *             rilanciata se le righe lotto non sono valide
     * @throws QtaLottiMaggioreException
     *             rilanciata se la quantità assegnata ai lotti supera la quantità della riga
     *             articolo
     */
    RigaMagazzino salvaRigaMagazzino(RigaMagazzino rigaMagazzino)
            throws RimanenzaLottiNonValidaException, RigheLottiNonValideException, QtaLottiMaggioreException;

    /**
     * salva tipo area magazzino.
     *
     * @param tipoAreaMagazzino
     *            {@link TipoAreaMagazzino}
     * @return {@link TipoAreaMagazzino}
     */
    TipoAreaMagazzino salvaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino);

    /**
     *
     * @param righeDaSpostare
     *            id delle righe da spostare all'interno del documento
     * @param id
     *            id della riga di riferimento per lo spostamento. Le righe verranno spostate sopra
     *            questa
     */
    void spostaRighe(Set<Integer> righeDaSpostare, Integer id);

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
    @AsyncMethodInvocation
    AreaMagazzino totalizzaDocumento(StrategiaTotalizzazioneDocumento strategia, AreaMagazzino areaMagazzino,
            AreaPartite areaPartite);

    /**
     * valida rigeh iva.
     *
     * @param areaIva
     *            {@link AreaIva}
     * @param idAreaMagazzino
     *            id {@link AreaMagazzino}
     * @return {@link AreaMagazzinoFullDTO}
     */
    AreaMagazzinoFullDTO validaRigheIva(AreaIva areaIva, Integer idAreaMagazzino);

    /**
     * valida righe magazzino.
     *
     * @param areaMagazzino
     *            {@link AreaMagazzino}
     * @param areaRate
     *            {@link AreaRate}
     * @param areaContabileLite
     *            {@link AreaContabileLite}
     * @param forzaStato
     *            forzar o meno lo stato
     * @return {@link AreaMagazzinoFullDTO}
     * @throws TotaleDocumentoNonCoerenteException
     *             .
     */
    AreaMagazzinoFullDTO validaRigheMagazzino(AreaMagazzino areaMagazzino, AreaRate areaRate,
            AreaContabileLite areaContabileLite, boolean forzaStato) throws TotaleDocumentoNonCoerenteException;

    /**
     *
     * @param parametriValorizzazioneDistinte
     *            parametri per la valorizzazione
     * @return mappa con articolo e bom della distinta
     */
    Map<ArticoloConfigurazioneKey, Bom> valorizzaDistinte(
            ParametriValorizzazioneDistinte parametriValorizzazioneDistinte);
}
