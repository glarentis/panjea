package it.eurotn.panjea.magazzino.service.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

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
import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileDeposito;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.CategoriaSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.domain.CodiceArticoloEntita;
import it.eurotn.panjea.magazzino.domain.Componente;
import it.eurotn.panjea.magazzino.domain.ConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.domain.DepositoMagazzino;
import it.eurotn.panjea.magazzino.domain.FormulaTrasformazione;
import it.eurotn.panjea.magazzino.domain.MagazzinoSettings;
import it.eurotn.panjea.magazzino.domain.MezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.RaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.RigaRaggruppamentoArticoli;
import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione;
import it.eurotn.panjea.magazzino.domain.TemplateSpedizioneMovimenti;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;
import it.eurotn.panjea.magazzino.domain.omaggio.Omaggio;
import it.eurotn.panjea.magazzino.domain.omaggio.TipoOmaggio;
import it.eurotn.panjea.magazzino.domain.rendicontazione.EntitaTipoEsportazione;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;
import it.eurotn.panjea.magazzino.exception.CodiceArticoloEntitaAbitualeEsistenteException;
import it.eurotn.panjea.magazzino.exception.CodiceArticoloEntitaContoTerziEsistenteException;
import it.eurotn.panjea.magazzino.exception.DistintaCircolareException;
import it.eurotn.panjea.magazzino.exception.FormuleLinkateException;
import it.eurotn.panjea.magazzino.exception.GenerazioneCodiceException;
import it.eurotn.panjea.magazzino.exception.GenerazioneDescrizioneException;
import it.eurotn.panjea.magazzino.service.exception.ScontoNotValidException;
import it.eurotn.panjea.magazzino.util.ArticoloConfigurazioneDistinta;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.CategoriaLite;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.riepilogo.util.RiepilogoArticoloDTO;

@Remote
public interface MagazzinoAnagraficaService {

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
     * Aggiunge un articolo ad una configurazione distinta collegata al componentePadre.
     *
     * @param configurazioneDistinta
     *            {@link ConfigurazioneDistinta}
     * @param articoloDaAggiungere
     *            articolo da aggiungere
     * @param componentePadre
     *            componente al quale collegare il nuovo componente
     * @return componente componente aggiunto e collegato al componente padre
     * @throws DistintaCircolareException
     */
    Componente aggiungiComponenteAConfigurazione(ConfigurazioneDistinta configurazioneDistinta,
            Componente componentePadre, Articolo articoloDaAggiungere) throws DistintaCircolareException;

    Componente aggiungiComponenteAConfigurazionePerImportazione(ConfigurazioneDistinta c, Componente componentePadre,
            Articolo articoloComponente);

    /**
     *
     * @param configurazione
     * @param articoloLite
     * @param fasiLavorazioni
     * @return
     */
    Set<FaseLavorazioneArticolo> aggiungiFasiLavorazione(ConfigurazioneDistinta configurazione,
            ArticoloLite articoloLite, Set<FaseLavorazioneArticolo> fasiLavorazioni);

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

    String calcolaEAN();

    void cambiaCategoriaAdArticoli(List<ArticoloRicerca> articoli, Categoria categoriaDestinazione);

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
    boolean cancellaArticoli(List<ArticoloRicerca> articoli);

    void cancellaArticolo(Articolo articolo);

    /**
     * Cancella l'articolo alternativo.
     *
     * @param articoloAlternativo
     *            articoloAlternativo da cancellare
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
     * @param categoriaCommercialeArticolo
     *            categoria da cancellare
     *
     */
    void cancellaCategoriaCommercialeArticolo(CategoriaCommercialeArticolo categoriaCommercialeArticolo);

    void cancellaCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo);

    void cancellaCategoriaContabileDeposito(CategoriaContabileDeposito categoriaContabileDeposito);

    void cancellaCategoriaContabileSedeMagazzino(CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino);

    void cancellaCategorieSediMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino);

    void cancellaCausaleTrasporto(CausaleTrasporto causaleTrasporto);

    void cancellaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita);

    /**
     * Cancella un componente dalla configurazione distinta.
     *
     * @param componenteSelezionato
     *            componente da cancellare
     */
    void cancellaComponentiConfigurazioneDistinta(List<Componente> componenteSelezionato);

    /**
     * Cancella una configurazione distinta e tutti i suoi componenti collegati.
     *
     * @param configurazioneDistinta
     *            configurazione distinta
     */
    void cancellaConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta);

    /**
     * Cancella deposito magazzino.
     *
     * @param id
     *            idDeposito
     */
    void cancellaDepositoMagazzino(Integer id);

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

    void cancellaFasiLavorazioneArticolo(ConfigurazioneDistinta configurazioneDistinta,
            List<FaseLavorazioneArticolo> fasiArticoloDaCancellare);

    void cancellaFormulaTrasformazione(FormulaTrasformazione formulaTrasformazione);

    void cancellaMezzoTrasporto(MezzoTrasporto mezzoTrasporto);

    /**
     *
     *
     * @param raggruppamentoArticoli
     *            da cancellare
     */
    void cancellaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli);

    /**
     *
     * @param rigaRaggruppamentoArticoli
     *            riga da cancellare
     */
    void cancellaRigaRaggruppamentoArticoli(RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli);

    void cancellaSconto(Sconto sconto);

    void cancellaSottoContoContabilizzazione(Integer id);

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

    /**
     * Carica tutti gli <code>ArticoloRicerca</code> per l'azienda loggata.
     *
     * @return <code>List</code> di <code>ArticoloRicerca</code>
     */
    List<ArticoloRicerca> caricaArticoli();

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
    ArticoloConfigurazioneDistinta caricaArticoloConfigurazioneDistinta(ConfigurazioneDistinta configurazioneDistinta)
            throws DistintaCircolareException;

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

    /**
     * @param descrizione
     *            descrizione da cercare, se <code>null</code> non viene considerata
     * @return List di {@link AspettoEsteriore} contenuti nell'anagrafica
     */
    List<AspettoEsteriore> caricaAspettiEsteriori(String descrizione);

    Categoria caricaCategoria(Categoria categoria, boolean initialiazeLazy);

    /**
     *
     * @param categoriaCommercialeArticolo
     *            categoriaCommerciale da caricare. Serve solamente l'id avvalorato
     * @return categoriaCommerciale caricata
     */
    CategoriaCommercialeArticolo caricaCategoriaCommercialeArticolo(
            CategoriaCommercialeArticolo categoriaCommercialeArticolo);

    List<CategoriaLite> caricaCategorie();

    List<Categoria> caricaCategorieCodiceDescrizione(String fieldSearch, String valueSearch);

    /**
     *
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return categorie commerciali per l'articolo
     */
    List<CategoriaCommercialeArticolo> caricaCategorieCommercialeArticolo(String fieldSearch, String valueSearch);

    List<CategoriaContabileArticolo> caricaCategorieContabileArticolo(String fieldSearch, String valueSearch);

    List<CategoriaContabileDeposito> caricaCategorieContabileDeposito(String fieldSearch, String valueSearch);

    List<CategoriaContabileSedeMagazzino> caricaCategorieContabileSedeMagazzino(String fieldSearch, String valueSearch);

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
     * @throws DistintaCircolareException
     *             rilanciata se ho un collegamento circolare.
     */
    Set<Componente> caricaComponenti(ConfigurazioneDistinta configurazioneDistinta) throws DistintaCircolareException;

    /**
     *
     * @param articolo
     *            articolo distinta
     * @return componenti che compongono la distinta.
     * @throws DistintaCircolareException
     */
    Set<Componente> caricaComponenti(int idArticolo) throws DistintaCircolareException;

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

    DepositoMagazzino caricaDepositoMagazzino(Integer id);

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
     * @param senzaCaricatore
     *            true per caricare i mezzi di trasporto senza un caricatore collegato
     * @return lista dei {@link MezzoTrasporto}
     */
    List<MezzoTrasporto> caricaMezziTrasporto(String targa, boolean abilitato, EntitaLite entita,
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
    List<RiepilogoArticoloDTO> caricaRiepilogoArticoli();

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
    List<RigaRaggruppamentoArticoli> caricaRigheRaggruppamentoArticoliByArticolo(int idArticolo);

    List<Sconto> caricaSconti();

    Sconto caricaSconto(Sconto sconto);

    SedeMagazzino caricaSedeMagazzinoBySedeEntita(SedeEntita sedeEntita, boolean ignoraEreditaDatiCommerciali);

    SedeMagazzino caricaSedeMagazzinoPrincipale(Entita entita);

    List<SedeMagazzino> caricaSediMagazzino(Map<String, Object> parametri, boolean textAsLike);

    List<SedeMagazzinoLite> caricaSediMagazzinoByEntita(Entita entita);

    List<SedeMagazzinoLite> caricaSediMagazzinoDiRifatturazione();

    List<SedeMagazzinoLite> caricaSediRifatturazioneAssociate();

    List<SedeMagazzinoLite> caricaSediRifatturazioneNonAssociate(EntitaLite entita);

    List<SottoContoContabilizzazione> caricaSottoContiContabilizzazione(ETipoEconomico tipoEconomico);

    SottoContoContabilizzazione caricaSottoContoContabilizzazione(Integer id);

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

    /**
     *
     * @param articolo
     *            articolo da clonare. Necessario solamente l'id avvalorato.
     * @param nuovoCodice
     *            nuovo codice dell'articolo. Errore se già presente. Se vuoto viene calcolato dalla
     *            maschera.
     * @param nuovaDescrizione
     *            nuova descrizione dell'articolo.
     * @param copyDistinta
     *            true copia la distinta
     * @param copyDistintaConfigurazioni
     *            se true copia tutte le configurazioni dalla distinta origine
     * @param attributi
     *            attributi da copiare
     * @param copyListino
     *            copia le righe di listino
     * @param azzeraPrezziListino
     *            se copia le righe di listino azzera i prezzi.
     * @return articolo clonato con gli attributi e il codice=codice + copia
     */
    Articolo cloneArticolo(int idArticolo, String nuovoCodice, String nuovaDescrizione, boolean copyDistinta,
            boolean copyDistintaConfigurazioni, List<AttributoArticolo> attributi, boolean copyListino,
            boolean azzeraPrezziListino);

    Articolo creaArticolo(Categoria Categoria);

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

    List<ArticoloRicerca> ricercaArticoli(ParametriRicercaArticolo parametriRicercaArticolo);

    /**
     * Ricerca ottimizzata per il caricamento degli articoli. La ricerca restituisce istanze di
     * {@link ArticoloRicerca} avvalorando solo le proprietà id,codice e descrizione.
     *
     * @param parametriRicercaArticolo
     *            parametri di ricerca
     * @return articoli trovati
     */
    List<ArticoloRicerca> ricercaArticoliSearchObject(ParametriRicercaArticolo parametriRicercaArticolo);

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

    /**
     * Rimuove refereze circolare da una distinta
     *
     * @param articolo
     *            articolo distinta con referenze circolari.
     */
    void rimuoviReferenzaCircolare(ArticoloLite articolo);

    void rimuoviSedePerRifatturazione(SedeMagazzinoLite sedeMagazzino);

    Articolo salvaArticolo(Articolo articolo) throws GenerazioneCodiceException, GenerazioneDescrizioneException;

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

    CategoriaContabileArticolo salvaCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo);

    CategoriaContabileDeposito salvaCategoriaContabileDeposito(CategoriaContabileDeposito categoriaContabileDeposito);

    CategoriaContabileSedeMagazzino salvaCategoriaContabileSedeMagazzino(
            CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino);

    CategoriaSedeMagazzino salvaCategoriaSedeMagazzino(CategoriaSedeMagazzino categoriaSedeMagazzino);

    CausaleTrasporto salvaCausaleTraporto(CausaleTrasporto causaleTrasporto);

    CodiceArticoloEntita salvaCodiceArticoloEntita(CodiceArticoloEntita codiceArticoloEntita)
            throws CodiceArticoloEntitaContoTerziEsistenteException, CodiceArticoloEntitaAbitualeEsistenteException;

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
            Set<FaseLavorazioneArticolo> fasiLavorazioni) throws DistintaCircolareException;

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
     *
     * @param raggruppamentoArticoli
     *            raggruppamento da salvare
     * @return raggruppamento salvato
     */
    RaggruppamentoArticoli salvaRaggruppamento(RaggruppamentoArticoli raggruppamentoArticoli);

    /**
     *
     * @param rigaRaggruppamentoArticoli
     *            riga da salvare
     * @return riga salvata.
     */
    RigaRaggruppamentoArticoli salvaRigaRaggruppamentoArticoli(RigaRaggruppamentoArticoli rigaRaggruppamentoArticoli);

    Sconto salvaSconto(Sconto sconto) throws ScontoNotValidException;

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

    SottoContoContabilizzazione salvaSottoContoContabilizzazione(
            SottoContoContabilizzazione sottoContoContabilizzazione);

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

    Componente sostituisciComponenteAConfigurazione(Integer idConfigurazioneDistinta, Integer idComponentePadre,
            Integer idComponenteSelezionato, Integer idArticoloSostitutivo) throws DistintaCircolareException;

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
