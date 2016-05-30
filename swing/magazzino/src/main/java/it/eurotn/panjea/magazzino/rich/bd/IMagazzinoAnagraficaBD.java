package it.eurotn.panjea.magazzino.rich.bd;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazione;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloAlternativo;
import it.eurotn.panjea.magazzino.domain.ArticoloDeposito;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.DepositoMagazzino;
import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.Listino;
import it.eurotn.panjea.magazzino.domain.Listino.ETipoListino;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaListino;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.ScaglioneListino;
import it.eurotn.panjea.magazzino.domain.ScaglioneListinoStorico;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.TemplateSpedizioneMovimenti;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.VersioneListino;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ListinoTipoMezzoZonaGeografica;
import it.eurotn.panjea.magazzino.domain.omaggio.Omaggio;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.domain.rendicontazione.EntitaTipoEsportazione;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.exception.ArticoliDuplicatiManutenzioneListinoException;
import it.eurotn.panjea.magazzino.exception.DistintaCircolareException;
import it.eurotn.panjea.magazzino.exception.FormuleLinkateException;
import it.eurotn.panjea.magazzino.exception.ListinoManutenzioneNonValidoException;
import it.eurotn.panjea.magazzino.service.exception.RigaListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.service.exception.RigheListinoListiniCollegatiException;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.ConfrontoListinoDTO;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.magazzino.util.RigaListinoDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriAggiornaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaConfrontoListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaManutenzioneListino;
import it.eurotn.panjea.magazzino.util.parametriricerca.RigaManutenzioneListino;
import it.eurotn.panjea.rich.bd.AsyncMethodInvocation;
import it.eurotn.panjea.riepilogo.util.RiepilogoArticoloDTO;
import it.eurotn.panjea.riepilogo.util.RiepilogoSedeEntitaDTO;

public interface IMagazzinoAnagraficaBD extends IRicercaArticoloBD {

    /**
     * Aggiorna il campo formula senza aumentare la versione di articolo.
     *
     * @param idArticolo
     *            idArticolo
     * @param formula
     *            formula da aggiornare
     */
    void aggiornaArticoliAlternativiFiltro(int idArticolo, String formula);

    /**
     * Dati {@link ParametriAggiornaManutenzioneListino} inserisce aggiorna la versione listino
     * scelta con le sue righe. <br/>
     * Nota che il valore viene arrotondato al numero decimali scelto a questa fase del processo,
     * cioe' quando viene aggiornata/inserita la rigaListino.
     *
     * @param parametriAggiornaManutenzioneListino
     *            i parametri per aggiornare/creare la versione listino
     * @throws ArticoliDuplicatiManutenzioneListinoException
     *             sollevata se esistono articoli duplicati
     */
    void aggiornaListinoDaManutenzione(ParametriAggiornaManutenzioneListino parametriAggiornaManutenzioneListino)
            throws ArticoliDuplicatiManutenzioneListinoException;

    /**
     * Associa una categoria merceologica ad una categoria commerciale.
     *
     * @param categoriaMerceologica
     *            categoriaMerceologica da associare
     * @param categoriaCommercialeArticolo
     *            categoria commerciale da associare
     */
    void aggiungiCategoriaMerceologicaACategoriaCommerciale(int idCategoriaMerceologica,
            int idCategoriaCommercialeArticolo);

    /**
     * Aggiunge un articolo ad una configurazione distinta collegata al componentePade
     *
     * @param configurazioneDistinta
     *            {@link ConfigurazioneDistinta}
     * @param articoloDaAggiungere
     *            articolo da aggiungere
     * @param componentePadre
     *            componente al quale collegare il nuovo componente
     * @return componente componente aggiunto e collegato al componente padre
     */
    Componente aggiungiComponenteAConfigurazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componentePadre, Articolo articoloDaAggiungere);

    /**
     *
     * @param configurazione
     * @param componente
     * @param fasiLavorazione
     * @return
     */
    Set<FaseLavorazioneArticolo> aggiungiFasiLavorazione(ConfigurazioneDistinta configurazioneDistinta,
            ArticoloLite articolo, Set<FaseLavorazioneArticolo> fasiLavorazione);

    /**
     *
     * @param configurazione
     * @param componente
     * @param fasiLavorazioni
     * @return
     */
    Set<FaseLavorazioneArticolo> aggiungiFasiLavorazione(ConfigurazioneDistinta configurazione, Componente componente,
            Set<FaseLavorazioneArticolo> fasiLavorazioni);

    void associaSediASedePerRifatturazione(List<SedeEntita> sediEntita, SedeMagazzinoLite sedePerRifatturazione);

    void associaSediMagazzinoPerRifatturazione(SedeMagazzinoLite sedeDiRifatturazione,
            List<SedeMagazzinoLite> sediDaAssociare);

    @AsyncMethodInvocation
    void asyncAggiornaMovimenti(java.util.Date dataIniziale);

    String calcolaEAN();

    @AsyncMethodInvocation
    void cambiaCategoriaAdArticoli(List<ArticoloRicerca> articoli, Categoria categoriaDestinazione);

    @AsyncMethodInvocation
    void cambiaCategoriaCommercialeAdArticoli(List<ArticoloRicerca> articoli,
            CategoriaCommercialeArticolo categoriaCommercialeArticolo,
            CategoriaCommercialeArticolo categoriaCommercialeArticolo2);

    /**
     * Sostituisce il codice iva degli articoli.
     *
     * @param codiceIvaDaSostituire
     *            codice da sostituire
     * @param nuovoCodiceIva
     *            codice da applicare
     */
    void cambiaCodiceIVA(CodiceIva codiceIvaDaSostituire, CodiceIva nuovoCodiceIva);

    /**
     * Cancella la lista di articoli specificata.
     *
     * @param articoli
     *            articoli da cancellare
     * @return <code>true</code> se tutti gli articoli sono stati cancellati
     */
    @AsyncMethodInvocation
    boolean cancellaArticoli(List<ArticoloRicerca> articoli);

    void cancellaArticolo(Articolo articolo);

    /**
     * Cancella l' {@link ArticoloAlternativo} scelto.
     *
     * @param articoloAlternativo
     *            il {@link ArticoloAlternativo} da cancellare
     */
    void cancellaArticoloAlternativo(ArticoloAlternativo articoloAlternativo);

    /**
     * Cancella l' {@link ArticoloDeposito} scelto.
     *
     * @param articoloDeposito
     *            il {@link ArticoloDeposito} da cancellare
     */
    void cancellaArticoloDeposito(ArticoloDeposito articoloDeposito);

    void cancellaAspettoEsteriore(AspettoEsteriore aspettoEsteriore);

    void cancellaCategoria(Integer idCategoria);

    /**
     *
     * @param categoriaCommercialeArticolo
     *            categoria da cancellare
     *
     */
    void cancellaCategoriaCommercialeArticolo(CategoriaCommercialeArticolo categoriaCommercialeArticolo);

    void cancellaCategorieSediMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino);

    void cancellaCausaleTrasporto(CausaleTrasporto causaleTrasporto);

    void cancellaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita);

    /**
     * Cancella un componente dalla configurazione distinta.
     *
     * @param componenti
     *            componenti da cancellare
     */
    void cancellaComponentiConfigurazioneDistinta(List<Componente> componenti);

    /**
     * Cancella una configurazione distinta e tutti i suoi componenti collegati.
     *
     * @param configurazioneDistinta
     *            configurazione distinta
     */
    void cancellaConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta);

    /**
     * Cancella un {@link EntitaTipoEsportazione}.
     *
     * @param entitaTipoEsportazione
     *            {@link EntitaTipoEsportazione} da cancellare
     */
    void cancellaEntitaTipoEsportazione(EntitaTipoEsportazione entitaTipoEsportazione);

    /**
     * Cancella una fase lavorazione.
     *
     * @param faseLavorazione
     *            {@link FaseLavorazione} da cancellare
     */
    void cancellaFaseLavorazione(FaseLavorazione faseLavorazione);

    /**
     * Cancella le fasi lavorazioni articolo della configurazione.
     *
     * @param configurazioneDistinta
     *            configurazioneDistinta
     * @param fasiArticoloDaCancellare
     *            fasiArticoloDaCancellare
     */
    void cancellaFasiLavorazioneArticolo(ConfigurazioneDistinta configurazioneDistinta,
            List<FaseLavorazioneArticolo> fasiArticoloDaCancellare);

    void cancellaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione);

    @AsyncMethodInvocation
    void cancellaListino(Listino listino);

    void cancellaListinoTipoMezzoZonaGeografica(ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica);

    void cancellaMezzoTrasporto(MezzoTrasporto mezzoTrasporto);

    /**
     *
     *
     * @param raggruppamentoArticoli
     *            da cancellare
     */
    void cancellaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli);

    void cancellaRigaListino(RigaListino rigaListino);

    /**
     *
     * @param rigaRaggruppamentoArticoli
     *            riga da cancellare
     */
    void cancellaRigaRaggruppamentoArticoli(RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli);

    @AsyncMethodInvocation
    void cancellaRigheListino(List<RigaListino> righeListino);

    @AsyncMethodInvocation
    void cancellaRigheManutenzioneListino(List<RigaManutenzioneListino> righeManutenzioneListino);

    void cancellaSconto(Sconto sconto);

    void cancellaTipoAttributo(TipoAttributo tipoAttributo);

    /**
     * Cancella il {@link TipoDocumentoBaseMagazzino} senza controlli.
     *
     * @param tipoDocumentoBase
     *            tipo documento base da cancellare
     */
    void cancellaTipoDocumentoBase(TipoDocumentoBaseMagazzino tipoDocumentoBase);

    /**
     * Cancella un tipo esportazione.
     *
     * @param tipoEsportazione
     *            tipo esportazione da cancellare
     */
    void cancellaTipoEsportazione(TipoEsportazione tipoEsportazione);

    void cancellaTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto);

    void cancellaTipoPorto(TipoPorto tipoPorto);

    void cancellaTrasportoCura(TrasportoCura trasportoCura);

    @AsyncMethodInvocation
    void cancellaVersioneListino(VersioneListino versioneListino);

    /**
     *
     * @param articolo
     *            articolo interessato.
     * @return articoli alternativi di articolo
     */
    Set<ArticoloAlternativo> caricaArticoliAlternativi(Articolo articolo);

    /**
     * Carica la lista di {@link ArticoloDeposito} del deposito scelto.
     *
     * @param deposito
     *            deposito di cui caricare gli {@link ArticoloDeposito}
     * @return List<ArticoloDeposito>
     */
    List<ArticoloDeposito> caricaArticoliDeposito(Deposito deposito);

    /**
     * Carica la lista di {@link ArticoloDeposito} dell'articolo scelto.
     *
     * @param idArticolo
     *            l'id dell'articolo di cui caricare gli {@link ArticoloDeposito}
     * @return List<ArticoloDeposito>
     */
    List<ArticoloDeposito> caricaArticoliDeposito(Integer idArticolo);

    Articolo caricaArticolo(Articolo articolo, boolean initializeLazy);

    /**
     * Carica un articoloConfigurazione con tutti i dati per la distinta caricati (fasi componenti
     * caricati in deep e distinte padre).
     *
     * @param configurazioneDistinta
     *            conf. per la quale caricare i dati.
     * @return articolo con tutti i dati per la distinta inizializzati.
     * @throws DistintaCircolareException
     *             rilanciata se ho un riferimento circolare nella disinta
     */
    ArticoloConfigurazioneDistinta caricaArticoloConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta);

    /**
     * Carica il {@link ArticoloDeposito} dell'articolo e deposito scelti.
     *
     * @param idArticolo
     *            l'id dell'articolo di cui caricare il {@link ArticoloDeposito}
     * @param idDeposito
     *            l'id del deposito di cui caricare l' {@link ArticoloDeposito}
     * @return {@link ArticoloDeposito} caricato, <code>null</code> se non esiste
     */
    ArticoloDeposito caricaArticoloDeposito(Integer idArticolo, Integer idDeposito);

    /**
     * @param idArticolo
     *            id articolo
     * @return articolo caricato
     *
     */
    ArticoloLite caricaArticoloLite(int idArticolo);

    List<AspettoEsteriore> caricaAspettiEsteriori(String descrizione);

    Categoria caricaCategoria(Categoria categoria, boolean initialiazeLazy);

    /**
     *
     * @param categoriaCommercialeArticolo
     *            categoriaCommerciale da caricare. Serve solamente l'id avvalorato
     * @return categoriaCommerciale caricata
     */
    CategoriaCommercialeArticolo caricaCategoriaCommercialeArticolo(int idCategoriaCommercialeArticolo);

    List<CategoriaLite> caricaCategorie();

    List<Categoria> caricaCategorieCodiceDescrizione(String fieldSearch, String valueSearch);

    /**
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return categorie commerciali per l'articolo
     */
    List<CategoriaCommercialeArticolo> caricaCategorieCommercialeArticolo(String fieldSearch, String valueSearch);

    List<CategoriaSedeMagazzino> caricaCategorieSediMagazzino(String fieldSearch, String valueSearch);

    List<CausaleTrasporto> caricaCausaliTraporto(String descrizione);

    /**
     * Carica la lista di {@link CodiceArticoloEntita} dell'entità scelta.
     *
     * @param entita
     *            entità di cui caricare i {@link CodiceArticoloEntita}
     * @return List<CodiceArticoloEntita>
     */
    List<CodiceArticoloEntita> caricaCodiciArticoloEntita(Entita entita);

    List<CodiceArticoloEntita> caricaCodiciArticoloEntita(Integer idArticolo);

    /**
     *
     * @param configurazioneDistinta
     *            configurazione delal disinta da caricare
     *
     * @return componenti che compongono la configurazione della distinta.
     */
    Set<Componente> caricaComponenti(ConfigurazioneDistinta configurazioneDistinta);

    /**
     *
     * @param articolo
     *            articolo distinta
     * @return componenti che compongono la distinta.
     */
    Set<Componente> caricaComponenti(int idArticolo);

    /**
     *
     * @param idConfigurazione
     *            conf da caricare
     * @return configurazioneDistinta
     */
    ConfigurazioneDistinta caricaConfigurazioneDistinta(int idConfigurazione);

    /**
     *
     * @param distinta
     *            articolo distinta.
     * @return lista delle configurazioni per la distinta. La configurazione base viene messa
     *         all'inizio.
     */
    List<ConfigurazioneDistinta> caricaConfigurazioniDistinta(ArticoloLite distinta);

    /**
     * Carica il confronto in base ai parametri specificati.
     *
     * @param parametri
     *            parametri di ricerca
     * @return confronto
     */
    ConfrontoListinoDTO caricaConfrontoListino(ParametriRicercaConfrontoListino parametri);

    DepositoMagazzino caricaDepositoMagazzinoByDeposito(Deposito deposito);

    /**
     * Carica la lista di {@link Componente} che rappresenta le distinte che hanno quell'articolo
     * componente.
     *
     * @param idArticolo
     *            l'id articolo del componente di cui recuperare le distinte
     * @return List<Componente> che rappresenta le distinte che hanno l'articolo componente come
     *         componenti
     */
    @AsyncMethodInvocation
    Set<Componente> caricaDistinteComponente(Integer idArticolo);

    /**
     * Carica tutte le {@link EntitaTipoEsportazione} presenti.
     *
     * @return {@link EntitaTipoEsportazione} caricate
     */
    List<EntitaTipoEsportazione> caricaEntitaTipoEsportazione();

    /**
     *
     * @param idFase
     *            id fase da caricare
     * @return fase con gli articoli e configurazioni collegati inizializzati
     */
    FaseLavorazione caricaFaseLavorazione(int idFase);

    /**
     * Carica l'anagrafica delle fasi di lavorazione.<br>
     *
     * @param codice
     *            codice
     *
     * @return List<FaseLavorazione>
     */
    List<FaseLavorazione> caricaFasiLavorazione(String codice);

    Set<FaseLavorazioneArticolo> caricaFasiLavorazioneArticolo(ConfigurazioneDistinta configurazioneDistinta);

    FormulaTrasformazione caricaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione);

    List<FormulaTrasformazione> caricaFormuleTrasformazione(String codice);

    /**
     * Carica l'importo della riga listino in base all'articolo e listino. La versione listino
     * considerata è quella in vigore alla data attuale. L'importo ha la scale con il numero di
     * decimali preso dalla riga listino.
     *
     * @param idListino
     *            id listino
     * @param idArticolo
     *            id articolo
     * @return importo della riga, <code>null</code> se non esiste
     */
    BigDecimal caricaImportoListino(Integer idListino, Integer idArticolo);

    /**
     * Carica tutti i listini relativi all'azienda. Le righe listino non vengono inizializzate.
     *
     * @return listini azienda.i
     */
    @AsyncMethodInvocation
    List<Listino> caricaListini();

    /**
     * Carica tutti i listini relativi all'azienda. Le righe listino non vengono inizializzate.
     *
     * @param tipoListino
     *            tipo listino da caricare. Se <code>null</code> vengono caricati tutti i listini
     * @param searchField
     *            il campo su cui eseguire la ricerca ( codice o descrizione )
     * @param searchValue
     *            valore per la ricerca ( codice o descrizione )
     * @return listini azienda.i
     */
    @AsyncMethodInvocation
    List<Listino> caricaListini(ETipoListino tipoListino, String searchField, String searchValue);

    List<ListinoTipoMezzoZonaGeografica> caricaListiniTipoMezzoZonaGeografica();

    @AsyncMethodInvocation
    Listino caricaListino(Listino listino, boolean initializeLazy);

    MagazzinoSettings caricaMagazzinoSettings();

    /**
     * Carica i {@link MezzoTrasporto} presenti nell'anagrafica.<br/>
     *
     * @param targa
     *            targa da filtrare
     * @param abilitato
     *            true se voglio caricare solamente quelli abilitati
     * @param entita
     *            null se non si vuole filtrare l'entità
     * @return lista dei {@link MezzoTrasporto}
     */
    List<MezzoTrasporto> caricaMezziTrasporto(String targa, boolean abilitato, EntitaLite entita);

    /**
     *
     * @param valueSearch
     * @param b
     * @param entitaLite
     * @param senzaCaricatore
     * @return
     */
    List<MezzoTrasporto> caricaMezziTrasporto(String valueSearch, boolean abilitati, EntitaLite entitaLite,
            boolean senzaCaricatore);

    /**
     * Carica la lista di omaggi disponibili.
     *
     * @return List<Omaggio>
     */
    List<Omaggio> caricaOmaggi();

    /**
     * Carica l'omaggio associato al tipo omaggio specificato.
     *
     * @param tipoOmaggio
     *            il tipo omaggio di cui caricare l'omaggio
     * @return Omaggio
     */
    Omaggio caricaOmaggioByTipo(TipoOmaggio tipoOmaggio);

    /**
     *
     * @return lista di tutti i raggruppamenti presenti. Le righe non vengono caricate
     */
    List<RaggruppamentoArticoli> caricaRaggruppamenti();

    /**
     * Carica un {@link RaggruppamentoArticoli}.
     *
     * @param raggruppamentoArticoli
     *            raggruppamento da salvare
     * @return raggruppamento salvato
     */
    RaggruppamentoArticoli caricaRaggruppamentoArticoli(RaggruppamentoArticoli raggruppamentoArticoli);

    /**
     * Carica il riepilogo degli articoli dell'azienda loggata.
     *
     * @return riepilogo caricato
     */
    @AsyncMethodInvocation
    List<RiepilogoArticoloDTO> caricaRiepilogoArticoli();

    /**
     * Carica la riga listino
     *
     * @param idRiga
     *            id della riga da caricare
     * @return riga caricata
     */
    RigaListino caricaRigaListino(Integer idRiga);

    @AsyncMethodInvocation
    List<RigaListinoDTO> caricaRigheListinoByArticolo(Integer idArticolo);

    @AsyncMethodInvocation
    List<RigaListinoDTO> caricaRigheListinoByVersione(Integer idVersioneListino);

    /**
     *
     * @param data
     *            data per recuperare la versione interessata
     * @param articoli
     *            articoli interessati
     * @return lista di tutte le righe per gli articoli interessati appartenenti a tutti i listini
     *         (con la versione valida nella data richiesta) che posso essere aggiornate all'ultimo
     *         costo
     */
    @AsyncMethodInvocation
    List<RigaListino> caricaRigheListinoDaAggiornare(Date data, List<ArticoloLite> articoli);

    @AsyncMethodInvocation
    List<RigaManutenzioneListino> caricaRigheManutenzioneListino();

    /**
     *
     * @param raggruppamentoArticoli
     *            raggruppamento per il quale caricare le righe
     * @return righe del raggruppamento
     */
    Set<RigaRaggruppamentoArticoli> caricaRigheRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli);

    /**
     *
     * @param idArticolo
     *            id dell'articolo da caricare
     * @return righeRaggruppamento legato all'articolo richiesto.
     */
    @AsyncMethodInvocation
    List<RigaRaggruppamentoArticoli> caricaRigheRaggruppamentoArticoliByArticolo(int idArticolo);

    List<Sconto> caricaSconti();

    Sconto caricaSconto(Sconto sconto);

    SedeMagazzino caricaSedeMagazzinoBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali);

    SedeMagazzino caricaSedeMagazzinoPrincipale(Entita entita);

    @AsyncMethodInvocation
    List<SedeMagazzino> caricaSediMagazzino(Map<String, Object> parametri, boolean textAsLike);

    @AsyncMethodInvocation
    List<SedeMagazzinoLite> caricaSediMagazzinoByEntita(Entita entita);

    /**
     *
     * @param listino
     *            listino o lisino alternativo da caricare
     * @return sediMagazzino legate al listino.
     */
    @AsyncMethodInvocation
    List<RiepilogoSedeEntitaDTO> caricaSediMagazzinoByListino(Listino listino);

    @AsyncMethodInvocation
    List<SedeMagazzinoLite> caricaSediMagazzinoDiRifatturazione();

    @AsyncMethodInvocation
    List<SedeMagazzinoLite> caricaSediRifatturazioneAssociate();

    @AsyncMethodInvocation
    List<SedeMagazzinoLite> caricaSediRifatturazioneNonAssociate(EntitaLite entita);

    /**
     * Carica lo storico dello scaglione di riferimento.
     *
     * @param scaglioneListino
     *            scaglione
     * @param numeroVersione
     *            se presente verranno filtrati gli storici della versione
     * @return storico caricato
     */
    List<ScaglioneListinoStorico> caricaStoricoScaglione(ScaglioneListino scaglioneListino, Integer numeroVersione);

    /**
     * @return carica il template per la spedizione dei movimenti
     */
    TemplateSpedizioneMovimenti caricaTemplateSpedizioneMovimenti();

    List<TipoAttributo> caricaTipiAttributo();

    /**
     * Carica tutti i {@link TipoDocumentoBaseMagazzino} per azienda.
     *
     * @return list di {@link TipoDocumentoBaseMagazzino} caricati
     */
    List<TipoDocumentoBaseMagazzino> caricaTipiDocumentoBase();

    /**
     * Carica i tipi esportazioni configurati per l'azienda loggata.
     *
     * @param nome
     *            nome da filtrare
     *
     * @return tipi esportazioni caricati
     */
    List<TipoEsportazione> caricaTipiEsportazione(String nome);

    List<TipoMezzoTrasporto> caricaTipiMezzoTrasporto();

    List<TipoPorto> caricaTipiPorto(String descrizione);

    TipoAttributo caricaTipoAttributo(TipoAttributo tipoAttributo);

    /**
     * Carica un {@link TipoEsportazione}.
     *
     * @param idTipoEsportazione
     *            id del tipo esportaizone da caricare
     * @param loadLazy
     *            <code>true</code> per caricare il tipo esportazione senza le collection
     *            inizializzate
     * @return {@link TipoEsportazione} caricato
     */
    TipoEsportazione caricaTipoEsportazione(Integer idTipoEsportazione, boolean loadLazy);

    TipoVariante caricaTipoVariante(TipoVariante tipoVariante);

    /**
     *
     * @param descrizione
     *            desc. da filtrare
     * @return trasporti cura filtrati.
     */
    List<TrasportoCura> caricaTrasportiCura(String descrizione);

    @AsyncMethodInvocation
    VersioneListino caricaVersioneListino(VersioneListino versioneListino, boolean initializeLazy);

    @AsyncMethodInvocation
    VersioneListino caricaVersioneListinoByData(Listino listino, Date data);

    /**
     * Carica tutte le versioni listino presenti per l'azienda corrente.
     *
     * @param valueSearch
     *            .
     * @param fieldSearch
     *            .
     *
     * @param tipoListino
     *            tipo di listino da caricare
     * @return List<VersioneListino>
     */
    @AsyncMethodInvocation
    List<VersioneListino> caricaVersioniListino(String fieldSearch, String valueSearch, ETipoListino tipoListino);

    /**
     *
     * @param idArticolo
     *            articolo da clonare. Necessario solamente l'id avvalorato.
     * @param nuovoCodice
     *            nuovo codice dell'articolo. Errore se già presente. Se vuoto viene calcolato dalla
     *            maschera.
     * @param nuovaDescrizione
     *            nuova descrizione dell'articolo.
     * @param copyDistinta
     *            true copia la distinta
     * @param attributi
     *            attributi per il nuovo articolo
     * @param copyListino
     *            copia le righe di listino
     * @param azzeraPrezziListino
     *            se copia le righe di listino azzera i prezzi.
     * @return articolo clonato con gli attributi e il codice=codice + copia
     */
    Articolo cloneArticolo(int idArticolo, String nuovoCodice, String nuovaDescrizione, boolean copyDistinta,
            List<AttributoArticolo> attributi, boolean copyListino, boolean azzeraPrezziListino);

    @AsyncMethodInvocation
    VersioneListino copiaVersioneListino(VersioneListino versioneListino, Date dataNuovaVersioneListino);

    Articolo creaArticolo(Categoria categoria);

    Categoria creaCategoria(Integer idCategoriaPadre);

    /**
     * Crea un nuovo deposito e lo associa al mezzo di trasporto.
     *
     * @param mezzoTrasporto
     *            mezzo di trasporto
     * @param codiceDeposito
     *            codice da assegnare al deposito
     * @param descrizioneDeposito
     *            descrizione da assegnare al deposito
     * @return mezzo di trasporto con il deposito associato
     */
    MezzoTrasporto creaNuovoDepositoMezzoDiTrasporto(MezzoTrasporto mezzoTrasporto, String codiceDeposito,
            String descrizioneDeposito);

    /**
     * Dati i {@link ParametriRicercaManutenzioneListino} ricerca e inserisce le
     * {@link RigaManutenzioneListino} associate ai parametri.
     *
     * @param parametriRicercaManutenzioneListino
     *            ParametriRicercaManutenzioneListino da cui trovare ed inserire le righe
     * @throws ListinoManutenzioneNonValidoException
     *             sollevata nel caso in cui il listino dei parametri manutenzione non sia valido
     *             con le eventuali righe manutenzione già presenti
     */
    @AsyncMethodInvocation
    void inserisciRigheRicercaManutenzioneListino(
            ParametriRicercaManutenzioneListino parametriRicercaManutenzioneListino)
                    throws ListinoManutenzioneNonValidoException;

    @AsyncMethodInvocation
    List<ArticoloRicerca> ricercaArticoli(ParametriRicercaArticolo parametriRicercaArticolo);

    List<Sconto> ricercaSconti(String codiceSconto);

    /**
     * Rimuove il deposito del mezzo di trasporto e lo cancella. Se la cancellazione non dovesse
     * andare a buon fine perchè il deposito è legato a documenti questo viene disabilitato.
     *
     * @param mezzoTrasporto
     *            mezzo di trasporto
     * @return mezzo di trasporto senza il deposito
     */
    MezzoTrasporto rimuoviDepositoDaMezzoDiTrasporto(MezzoTrasporto mezzoTrasporto);

    void rimuoviReferenzaCircolare(ArticoloLite articolo);

    void rimuoviSedePerRifatturazione(SedeMagazzinoLite sedeMagazzino);

    Articolo salvaArticolo(Articolo articolo);

    /**
     * Salva l'articolo alternativo.
     *
     * @param articoloAlternativo
     *            articolo alternativo da salvare.
     * @return ArticoloAlternativo
     */
    ArticoloAlternativo salvaArticoloAlternativo(ArticoloAlternativo articoloAlternativo);

    /**
     * Salva l' {@link ArticoloDeposito} creato.
     *
     * @param articoloDeposito
     *            l' {@link ArticoloDeposito} da salvare
     * @return {@link ArticoloDeposito} salvato
     */
    ArticoloDeposito salvaArticoloDeposito(ArticoloDeposito articoloDeposito);

    AspettoEsteriore salvaAspettoEsteriore(AspettoEsteriore aspettoEsteriore);

    /**
     * Salva un attributo articolo.
     *
     * @param attributoArticolo
     *            attributo da salvare
     * @return attributo salvato
     */
    AttributoArticolo salvaAttributoArticolo(AttributoArticolo attributoArticolo);

    Categoria salvaCategoria(Categoria categoria);

    /**
     *
     * @param categoriaCommercialeArticolo
     *            categoria da salvare
     * @return categoria salvata
     */
    CategoriaCommercialeArticolo salvaCategoriaCommercialeArticolo(
            CategoriaCommercialeArticolo categoriaCommercialeArticolo);

    CategoriaSedeMagazzino salvaCategoriaSedeMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino);

    CausaleTrasporto salvaCausaleTraporto(CausaleTrasporto causaleTrasporto);

    CodiceArticoloEntita salvaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita);

    /**
     * Salva il componente.
     *
     * @param componente
     *            componente da salvare
     * @return componente salvato
     */
    Componente salvaComponente(Componente componente);

    /**
     * Salva una configurazione di una distinta. Se nuova copia dall configurazione base tutti i
     * componenti
     *
     * @param configurazioneDistinta
     *            conf da salvare.
     * @return configurazioneDistintaSalvata
     */
    ConfigurazioneDistinta salvaConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta);

    DepositoMagazzino salvaDepositoMagazzino(DepositoMagazzino depositoMagazzino);

    void salvaDistintaArticolo(ArticoloLite articoloLite, Set<Componente> distinte, Set<Componente> componenti,
            Set<FaseLavorazioneArticolo> fasiLavorazioni);

    /**
     * Salva un {@link EntitaTipoEsportazione}.
     *
     * @param entitaTipoEsportazione
     *            {@link EntitaTipoEsportazione} da salvare
     * @return {@link EntitaTipoEsportazione} salvata
     */
    EntitaTipoEsportazione salvaEntitaTipoEsportazione(EntitaTipoEsportazione entitaTipoEsportazione);

    /**
     * Salva la fase di lavorazione.
     *
     * @param faseLavorazione
     *            la descrizione della fase di lavorazione da salvare
     * @return FaseLavorazione
     */
    FaseLavorazione salvaFaseLavorazione(FaseLavorazione faseLavorazione);

    /**
     * Salva la fase di lavorazione articolo.
     *
     * @param faseLavorazioneArticolo
     *            la descrizione della fase di lavorazione articolo da salvare
     * @return FaseLavorazioneArticolo
     */
    FaseLavorazioneArticolo salvaFaseLavorazioneArticolo(FaseLavorazioneArticolo faseLavorazioneArticolo);

    FormulaTrasformazione salvaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione);

    Listino salvaListino(Listino listino);

    ListinoTipoMezzoZonaGeografica salvaListinoTipoMezzoZonaGeografica(
            ListinoTipoMezzoZonaGeografica listinoTipoMezzoZonaGeografica);

    MagazzinoSettings salvaMagazzinoSettings(MagazzinoSettings magazzinoSettings);

    MezzoTrasporto salvaMezzoTrasporto(MezzoTrasporto mezzoTrasporto);

    /**
     * Salva l'omaggio.
     *
     * @param omaggio
     *            omaggio da salvare
     * @return l'maggio salvato
     */
    Omaggio salvaOmaggio(Omaggio omaggio);

    /**
     * Salva il prezzo di una lista di righe listino eseguendo sempre il controllo sulla quantità
     * della riga sulla base della tipologia del listino.
     *
     * @param listRigheListino
     *            righe da salvare
     * @return righe salvate
     * @throws RigheListinoListiniCollegatiException
     *             sollevata se ci sono righe listino da salvare che fanno parte di listini base
     */
    @AsyncMethodInvocation
    List<RigaListino> salvaPrezzoRigheListino(List<RigaListino> listRigheListino)
            throws RigheListinoListiniCollegatiException;

    /**
     * Salva il prezzo di una lista di righe listino eseguendo sempre il controllo sulla quantità
     * della riga sulla base della tipologia del listino.
     *
     * @param listRigheListino
     *            righe da salvare
     * @param aggiornaListiniCollegati
     *            aggiorna il prezzo sui listini collegati
     * @return righe salvate
     */
    List<RigaListino> salvaPrezzoRigheListino(List<RigaListino> listRigheListino, boolean aggiornaListiniCollegati);

    /**
     *
     * @param raggruppamentoArticoli
     *            raggruppamento da salvare
     * @return raggruppamento salvato
     */
    RaggruppamentoArticoli salvaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli);

    /**
     * Salva una riga listino. Se il listino della riga è di tipo normale la riga non potrà essere
     * salvata se la quantità non è uguale a 0 e viene rilanciata una eccezione.
     *
     * @param rigaListino
     *            riga da salvare
     * @return rigaListino salvata
     * @throws RigaListinoListiniCollegatiException
     *             sollevata se alla riga listino sono configurati dei listini collegati
     */
    RigaListino salvaRigaListino(RigaListino rigaListino) throws RigaListinoListiniCollegatiException;

    /**
     * Salva una riga listino. Se il listino della riga è di tipo normale la riga non potrà essere
     * salvata se la quantità non è uguale a 0 e viene rilanciata una eccezione.
     *
     * @param rigaListino
     *            riga da salvare
     * @param aggiornaListiniCollegati
     *            aggiorna il prezzo sui listini collegati
     * @return rigaListino salvata
     */
    RigaListino salvaRigaListino(RigaListino rigaListino, boolean aggiornaListiniCollegati);

    List<RigaManutenzioneListino> salvaRigaManutenzioneListino(RigaManutenzioneListino rigaManutenzioneListino);

    /**
     *
     * @param rigaRaggruppamentoArticoli
     *            riga da salvare
     * @return riga salvata.
     */
    RigaRaggruppamentoArticoli salvaRigaRaggruppamentoArticoli(RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli);

    Sconto salvaSconto(Sconto sconto);

    /**
     * Salva una {@link SedeMagazzino}.
     *
     * @param sedeMagazzino
     *            sede da salvare
     * @return sede magazzino salvata
     */
    SedeMagazzino salvaSedeMagazzino(SedeMagazzino sedeMagazzino);

    /**
     * Salva una lista di {@link SedeMagazzino}.
     *
     * @param sedi
     *            sedi da salvare
     * @return sedi magazzino salvate
     */
    List<SedeMagazzino> salvaSediMagazzino(List<SedeMagazzino> sedi);

    /**
     * Salva il template per la spedizione dei movimenti.
     *
     * @param templateSpedizioneMovimenti
     *            template da salvare
     * @return template salvato
     */
    TemplateSpedizioneMovimenti salvaTemplateSpedizioneMovimenti(
            TemplateSpedizioneMovimenti templateSpedizioneMovimenti);

    TipoAttributo salvaTipoAttributo(TipoAttributo tipoAttributo);

    /**
     * Esegue il salvataggio di {@link TipoDocumentoBaseMagazzino} e lo restituisce salvato.
     *
     * @param tipoDocumentoBase
     *            tipo documento base da salvare
     * @return {@link TipoDocumentoBaseMagazzino} salvato
     */
    TipoDocumentoBaseMagazzino salvaTipoDocumentoBase(TipoDocumentoBaseMagazzino tipoDocumentoBase);

    /**
     * Salva un {@link TipoEsportazione}.
     *
     * @param tipoEsportazione
     *            {@link TipoEsportazione} da salvare
     * @return {@link TipoEsportazione} salvata
     */
    TipoEsportazione salvaTipoEsportazione(TipoEsportazione tipoEsportazione);

    TipoMezzoTrasporto salvaTipoMezzoTrasporto(TipoMezzoTrasporto tipoMezzoTrasporto);

    TipoPorto salvaTipoPorto(TipoPorto tipoPorto);

    TrasportoCura salvaTrasportoCura(TrasportoCura trasportoCura);

    VersioneListino salvaVersioneListino(VersioneListino versioneListino);

    @AsyncMethodInvocation
    void sincronizzaAnagrafiche();

    @AsyncMethodInvocation
    void sincronizzaDimensionedata();

    /**
     * Sincronizza gli attributi del DMS
     */
    @AsyncMethodInvocation
    void sincronizzaDMS();

    Componente sostituisciComponenteAConfigurazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componentePadre, Componente componenteSelezionato, Articolo articoloSostitutivo);

    /**
     * Verifica che le fomule non abbiano riferimenti circolari in base al tipo attributo.
     *
     * @param tipoAttributo
     *            tipo attributo di riferimento
     * @param formulaTrasformazione
     *            formula del tipo attributo di riferimento
     * @param formuleTipiAttributo
     *            mappa contenente i tipi attributo e formula associata
     * @throws FormuleLinkateException
     *             FormuleLinkateException
     */
    void verificaFormuleLinkate(TipoAttributo tipoAttributo, FormulaTrasformazione formulaTrasformazione,
            Map<TipoAttributo, FormulaTrasformazione> formuleTipiAttributo) throws FormuleLinkateException;
}