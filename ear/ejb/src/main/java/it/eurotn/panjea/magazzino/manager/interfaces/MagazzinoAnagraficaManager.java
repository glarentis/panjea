package it.eurotn.panjea.magazzino.manager.interfaces;

import java.util.List;

import javax.ejb.Local;

import org.hibernate.Filter;

import it.eurotn.panjea.anagrafica.domain.FaseLavorazione;
import it.eurotn.panjea.anagrafica.domain.FaseLavorazioneArticolo;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.domain.CausaleTrasporto;
import it.eurotn.panjea.magazzino.domain.TemplateSpedizioneMovimenti;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoPorto;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.magazzino.domain.rendicontazione.EntitaTipoEsportazione;
import it.eurotn.panjea.magazzino.domain.rendicontazione.TipoEsportazione;

@Local
public interface MagazzinoAnagraficaManager {

    /**
     * Associa una categoria merceologica ad una categoria commerciale.
     *
     * @param categoriaMerceologica
     *            categoriaMerceologica da associare
     * @param categoriaCommercialeArticolo
     *            categoria commerciale da associare
     */
    void aggiungiCategoriaMerceologicaACategoriaCommerciale(Categoria categoriaMerceologica,
            CategoriaCommercialeArticolo categoriaCommercialeArticolo);

    /**
     * Cancellazione di un record dall'anagrafica aspetto esteriore.
     *
     * @param aspettoEsteriore
     *            {@link AspettoEsteriore} da cancellare
     */
    void cancellaAspettoEsteriore(AspettoEsteriore aspettoEsteriore);

    /**
     *
     * @param categoriaCommercialeArticolo
     *            categoria da cancellare
     *
     */
    void cancellaCategoriaCommercialeArticolo(CategoriaCommercialeArticolo categoriaCommercialeArticolo);

    /**
     * Cancellazione di un record dall'anagrafica causale trasporto.
     *
     * @param causaleTrasporto
     *            {@link CausaleTrasporto} da cancellare
     */
    void cancellaCausaleTrasporto(CausaleTrasporto causaleTrasporto);

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
     * Cancella un tipo attributo dall'anagrafica.
     *
     * @param tipoAttributo
     *            {@link TipoAttributo} da cancellare
     */
    void cancellaTipoAttributo(TipoAttributo tipoAttributo);

    /**
     * Cancella un tipo esportazione.
     *
     * @param tipoEsportazione
     *            tipo esportazione da cancellare
     */
    void cancellaTipoEsportazione(TipoEsportazione tipoEsportazione);

    /**
     * Cancellazione di un record dall'anagrafica {@link TipoPorto}.
     *
     * @param tipoPorto
     *            {@link TipoPorto} da cancellare
     */
    void cancellaTipoPorto(TipoPorto tipoPorto);

    /**
     * Cancellazione di un record dall'anagrafica {@link TrasportoCura}.
     *
     * @param trasportoCura
     *            {@link TrasportoCura} da cancellare
     */
    void cancellaTrasportoCura(TrasportoCura trasportoCura);

    /**
     * @param descrizione
     *            descrizione da cercare, se <code>null</code> non viene considerata
     * @return List di {@link AspettoEsteriore} contenuti nell'anagrafica
     */
    List<AspettoEsteriore> caricaAspettiEsteriori(String descrizione);

    /**
     *
     * @param categoriaCommercialeArticolo
     *            categoriaCommerciale da caricare. Serve solamente l'id avvalorato
     * @return categoriaCommerciale caricata
     */
    CategoriaCommercialeArticolo caricaCategoriaCommercialeArticolo(
            CategoriaCommercialeArticolo categoriaCommercialeArticolo);

    /**
     *
     * @param fieldSearch
     *            .
     * @param valueSearch
     *            .
     * @return categorie commerciali per l'articolo
     */
    List<CategoriaCommercialeArticolo> caricaCategorieCommercialeArticolo(String fieldSearch, String valueSearch);

    /**
     * @param descrizione
     *            descrizione
     * @return List di {@link CausaleTrasporto} contenuti nell'anagrafica
     */
    List<CausaleTrasporto> caricaCausaliTraporto(String descrizione);

    /**
     * Carica tutte le {@link EntitaTipoEsportazione} presenti.
     *
     * @return {@link EntitaTipoEsportazione} caricate
     */
    List<EntitaTipoEsportazione> caricaEntitaTipoEsportazione();

    /**
     * Carica tutte le {@link EntitaLite} per il {@link TipoEsportazione} richiesto.
     *
     * @param tipoEsportazione
     *            {@link TipoEsportazione} di riferimento
     * @return {@link EntitaLite} caricate
     */
    List<EntitaLite> caricaEntitaTipoEsportazione(TipoEsportazione tipoEsportazione);

    /**
     *
     * @param fase
     *            fase da caricare (obbligatorio solo id della fase)
     * @return fase con gli articoli e configurazioni collegati inizializzati
     */
    FaseLavorazione caricaFaseLavorazione(FaseLavorazione fase);

    /**
     * Carica l'anagrafica delle fasi di lavorazione.<br>
     *
     * @param codice
     *            codice
     * @return List<FaseLavorazione>
     */
    List<FaseLavorazione> caricaFasiLavorazione(String codice);

    /**
     * @return carica il template per la spedizione dei movimenti
     */
    TemplateSpedizioneMovimenti caricaTemplateSpedizioneMovimenti();

    /**
     * Carica i {@link TipoAttributo} presenti nell'anagrafica.<br/>
     *
     * @return lista dei {@link TipoAttributo} che possono essere anche di tipo {@link TipoVariante}
     */
    List<TipoAttributo> caricaTipiAttributo();

    /**
     * Carica i tipi esportazioni configurati per l'azienda loggata.
     *
     * @param nome
     *            nome da filtrare
     * @return tipi esportazioni caricati
     */
    List<TipoEsportazione> caricaTipiEsportazione(String nome);

    /**
     *
     * @param descrizione
     *            descrizione da filtrare
     * @return List di {@link TipoPorto} contenuti nell'anagrafica
     */
    List<TipoPorto> caricaTipiPorto(String descrizione);

    /**
     * Carica il tipoAttributo con le descrizioni in lingua.<br/>
     * Abilitando o meno {@link Filter} di Hibernate posso recuperare solamente la descrizione in lingua/e interessate.
     *
     * @param tipoAttributo
     *            Obbligatorio solamente il campo id.
     * @return {@link TipoAttributo} caricato con i nomi in lingua. Se abilito il filtro di hibernate posso caricare
     *         solamente la lingua desiderata.
     */
    TipoAttributo caricaTipoAttributo(TipoAttributo tipoAttributo);

    /**
     *
     * @param codice
     *            codice del {@link TipoAttributo}
     * @return {@link TipoAttributo} contenente solamente il codice in lingua aziendale. (i campi lazy non vengono
     *         caricati).
     */
    TipoAttributo caricaTipoAttributoByCodice(String codice);

    /**
     * Carica un {@link TipoEsportazione}.
     *
     * @param idTipoEsportazione
     *            id del tipo esportaizone da caricare
     * @param loadLazy
     *            <code>true</code> per caricare il tipo esportazione senza le collection inizializzate
     * @return {@link TipoEsportazione} caricato
     */
    TipoEsportazione caricaTipoEsportazione(Integer idTipoEsportazione, boolean loadLazy);

    /**
     * Carica un tipo variante con gli attributi non inizializzati.
     *
     * @param tipoVariante
     *            Obbligatorio solamente il campo id
     * @return Tipo variante con gli attributi lazy non inizializzati
     */
    TipoVariante caricaTipoVariante(TipoVariante tipoVariante);

    /**
     *
     * @param descrizione
     *            descrizione da filtrare
     * @return List di {@link TrasportoCura} contenuti nell'anagrafica
     */
    List<TrasportoCura> caricaTrasportiCura(String descrizione);

    /**
     * Carica un {@link TrasportoCura} in base alla descrizione.
     *
     * @param descrizione
     *            descrizione
     * @return {@link TrasportoCura} caricato, <code>null</code> se non esiste
     */
    TrasportoCura caricaTrasportoCuraByDescrizione(String descrizione);

    /**
     * Salva un record {@link AspettoEsteriore}.
     *
     * @param aspettoEsteriore
     *            oggetto con i nuovi dati
     * @return {@link AspettoEsteriore} con i dati aggiornati.
     */
    AspettoEsteriore salvaAspettoEsteriore(AspettoEsteriore aspettoEsteriore);

    /**
     *
     * @param categoriaCommercialeArticolo
     *            categoria da salvare
     * @return categoria salvata
     */
    CategoriaCommercialeArticolo salvaCategoriaCommercialeArticolo(
            CategoriaCommercialeArticolo categoriaCommercialeArticolo);

    /**
     * Salva un record {@link CausaleTrasporto}.
     *
     * @param causaleTrasporto
     *            oggetto con i nuovi dati
     * @return {@link CausaleTrasporto} con i dati aggiornati
     */
    CausaleTrasporto salvaCausaleTraporto(CausaleTrasporto causaleTrasporto);

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

    /**
     * Salva il template per la spedizione dei movimenti.
     *
     * @param templateSpedizioneMovimenti
     *            template da salvare
     * @return template salvato
     */
    TemplateSpedizioneMovimenti salvaTemplateSpedizioneMovimenti(
            TemplateSpedizioneMovimenti templateSpedizioneMovimenti);

    /**
     * Salva il tipo attributo e le descrizioni in lingue <br>
     * . <b>NB:</b>attenzione a non aver caricato il tipoAttributo con il filtro lingua abilitato altrimenti verrebbero
     * cancellate le altre lingue
     *
     * @param tipoAttributo
     *            tipoAttributo con le lingue inizializzate
     * @return {@link TipoAttributo} con i dati aggiornati
     */
    TipoAttributo salvaTipoAttributo(TipoAttributo tipoAttributo);

    /**
     * Salva un {@link TipoEsportazione}.
     *
     * @param tipoEsportazione
     *            {@link TipoEsportazione} da salvare
     * @return {@link TipoEsportazione} salvata
     */
    TipoEsportazione salvaTipoEsportazione(TipoEsportazione tipoEsportazione);

    /**
     * Salva un record {@link TipoPorto}.
     *
     * @param tipoPorto
     *            oggetto con i nuovi dati
     * @return tipoPorto salvato
     */
    TipoPorto salvaTipoPorto(TipoPorto tipoPorto);

    /**
     * Salva un <code>TrasportoCura</code>.
     *
     * @param trasportoCura
     *            <code>TrasportoCura</code> da salvare
     * @return <code>TrasportoCura</code> salvato
     */
    TrasportoCura salvaTrasportoCura(TrasportoCura trasportoCura);
}
