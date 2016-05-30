package it.eurotn.panjea.vending.service.interfaces;

import java.util.List;

import javax.ejb.Remote;

import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.manutenzioni.domain.ProdottoCollegato;
import it.eurotn.panjea.vending.domain.AnagraficaAsl;
import it.eurotn.panjea.vending.domain.Cassa;
import it.eurotn.panjea.vending.domain.Distributore;
import it.eurotn.panjea.vending.domain.Gettone;
import it.eurotn.panjea.vending.domain.Modello;
import it.eurotn.panjea.vending.domain.MovimentoCassa;
import it.eurotn.panjea.vending.domain.SistemaElettronico;
import it.eurotn.panjea.vending.domain.TipoComunicazione;
import it.eurotn.panjea.vending.domain.TipoModello;
import it.eurotn.panjea.vending.domain.VendingSettings;
import it.eurotn.panjea.vending.manager.distributore.ParametriRicercaDistributore;
import it.eurotn.panjea.vending.manager.sistemielettronici.ParametriRicercaSistemiElettronici;
import it.eurotn.panjea.vending.domain.Asl;

@Remote
public interface VendingAnagraficaService {

    /**
     * Cancella un {@link AnagraficaAsl}.
     *
     * @param id
     *            id AnagraficaAsl da cancellare
     */
    void cancellaAnagraficaAsl(Integer id);

    /**
     * Cancella un {@link Cassa}.
     *
     * @param id
     *            id Cassa da cancellare
     */
    void cancellaCassa(Integer id);

    /**
     * Cancella un Distributore.
     *
     * @param idDistributore
     *            id Distributore da cancellare
     */
    void cancellaDistributore(Integer idDistributore);

    /**
     * Cancella un {@link Gettone}.
     *
     * @param id
     *            id Gettone da cancellare
     */
    void cancellaGettone(Integer id);

    /**
     * Cancella un {@link Modello}.
     *
     * @param idModello
     *            id modello da cancellare
     */
    void cancellaModello(Integer idModello);

    /**
     * Cancella un {@link MovimentoCassa}.
     *
     * @param id
     *            id MovimentoCassa da cancellare
     */
    void cancellaMovimentoCassa(Integer id);

    /**
     * Cancella un {@link SistemaElettronico}.
     *
     * @param id
     *            id SistemaElettronico da cancellare
     */
    void cancellaSistemaElettronico(Integer id);

    /**
     * Cancella un {@link TipoComunicazione}.
     *
     * @param idTipoComunicazione
     *            id tipo comunicazione da cancellare
     */
    void cancellaTipoComunicazione(Integer idTipoComunicazione);

    /**
     * Cancella un {@link TipoModello}.
     *
     * @param idTipoModello
     *            id tipo modello da cancellare
     */
    void cancellaTipoModello(Integer idTipoModello);

    /**
     * Carica un {@link AnagraficaAsl} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link AnagraficaAsl} caricato
     */
    AnagraficaAsl caricaAnagraficaAslById(Integer id);

    /**
     * Carica tutti i {@link AnagraficaAsl} presenti.
     *
     * @return {@link AnagraficaAsl} caricati
     */
    List<AnagraficaAsl> caricaAnagraficheAsl();

    /**
     * Carica un {@link Cassa} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link Cassa} caricato
     */
    Cassa caricaCassaById(Integer id);

    /**
     * Carica tutti i {@link Cassa} presenti.
     *
     * @return {@link Cassa} caricati
     */
    List<Cassa> caricaCasse();

    /**
     * Carica un distributore.
     *
     * @param idDistributore
     *            id distributore da caricare
     * @return distributore caricato
     */
    Distributore caricaDistributore(Integer idDistributore);

    /**
     * Carica un {@link Gettone} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link Gettone} caricato
     */
    Gettone caricaGettoneById(Integer id);

    /**
     * Carica tutti i {@link Gettone} presenti.
     *
     * @return {@link Gettone} caricati
     */
    List<Gettone> caricaGettoni();

    /**
     * Carica i {@link Modello} presenti.
     *
     * @return modelli caricati
     */
    List<Modello> caricaModelli();

    /**
     * Carica tutti i {@link MovimentoCassa} presenti.
     *
     * @param includiChiusure
     *            include i movimenti di chiusura
     * @return {@link MovimentoCassa} caricati
     */
    List<MovimentoCassa> caricaMovimentiCassa(boolean includiChiusure);

    /**
     * Carica un {@link MovimentoCassa} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link MovimentoCassa} caricato
     */
    MovimentoCassa caricaMovimentoCassaById(Integer id);

    /**
     * Carica tutti i prodotti del distributore.
     *
     * @param idDistributore
     *            id distributore
     * @return prodotti caricati
     */
    List<ProdottoCollegato> caricaProdottiCollegatiByDistributore(Integer idDistributore);

    /**
     * Carica tutti i prodotti dell'installaizone.
     *
     * @param idInstallazione
     *            id dell'installazione
     * @return prodotti collegati
     */
    List<ProdottoCollegato> caricaProdottiCollegatiByInstallazione(Integer idInstallazione);

    /**
     * Carica tutti i prodotti del modello.
     *
     * @param idModello
     *            id modello
     * @return prodotti caricati
     */
    List<ProdottoCollegato> caricaProdottiCollegatiByModello(Integer idModello);

    /**
     * Carica tutti i prodotti del tipo modello.
     *
     * @param idTipoModello
     *            id tipo modello
     * @return prodotti caricati
     */
    List<ProdottoCollegato> caricaProdottiCollegatiByTipoModello(Integer idTipoModello);

    /**
     * Carica un {@link SistemaElettronico} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link SistemaElettronico} caricato
     */
    SistemaElettronico caricaSistemaElettronicoById(Integer id);

    /**
     * Carica tutti i {@link SistemaElettronico} presenti.
     *
     * @return {@link SistemaElettronico} caricati
     */
    List<SistemaElettronico> caricaSistemiElettronici();

    /**
     * Carica i {@link TipoComunicazione} presenti.
     *
     * @return tipi comunicazione caricati
     */
    List<TipoComunicazione> caricaTipiComunicazione();

    /**
     * Carica i {@link TipoModello} presenti.
     *
     * @return tipi modello caricati
     */
    List<TipoModello> caricaTipiModello();

    /**
     * Carica il settings del vending.<br/>
     * Se non esiste ne crea uno, lo salva e lo restituisce.
     *
     * @return <code>VendingSettings</code> caricato
     */
    VendingSettings caricaVendingSettings();

    /**
     * Esegue la chiusura delle casse.
     *
     * @param casseId
     *            id casse da chiudere
     */
    void chiudiCassa(Integer[] casseId);

    /**
     * Esegue la ricerca degli articoli associati all'installazione.
     *
     * @param parametri
     *            parametri di ricerca
     * @return articoli caricati
     */
    List<ArticoloRicerca> ricercaArticoliInstallazione(ParametriRicercaArticolo parametri);

    /**
     * Ricerca le casse presenti.
     *
     * @return casse trovate
     */
    List<Cassa> ricercaCasse();

    /**
     * Carica tutti i distributori in base ai parametri di ricerca
     *
     * @param parametri
     *            parametri di ricerca
     * @return distributori caricati
     */
    List<Distributore> ricercaDistributori(ParametriRicercaDistributore parametri);

    /**
     * Esegue la ricerca dei sistemi elettronici.
     *
     * @param parametri
     *            parametri di ricerca
     * @return risultati
     */
    List<SistemaElettronico> ricercaSistemiElettronici(ParametriRicercaSistemiElettronici parametri);

    /**
     * Salva un {@link AnagraficaAsl}.
     *
     * @param anagraficaAsl
     *            {@link AnagraficaAsl} da salvare
     * @return {@link AnagraficaAsl} salvato
     */
    AnagraficaAsl salvaAnagraficaAsl(AnagraficaAsl anagraficaAsl);

    /**
     * Salva un {@link Cassa}.
     *
     * @param cassa
     *            {@link Cassa} da salvare
     * @return {@link Cassa} salvato
     */
    Cassa salvaCassa(Cassa cassa);

    /**
     * Salva un Distributore.
     *
     * @param distributore
     *            Distributore da salvare
     * @return Distributore salvato
     */
    Distributore salvaDistributore(Distributore distributore);

    /**
     * Salva un {@link Gettone}.
     *
     * @param gettone
     *            {@link Gettone} da salvare
     * @return {@link Gettone} salvato
     */
    Gettone salvaGettone(Gettone gettone);

    /**
     * Salva un {@link Modello}.
     *
     * @param modello
     *            modello da salvare
     * @return modello salvato
     */
    Modello salvaModello(Modello modello);

    /**
     * Salva un {@link MovimentoCassa}.
     *
     * @param movimentoCassa
     *            {@link MovimentoCassa} da salvare
     * @return {@link MovimentoCassa} salvato
     */
    MovimentoCassa salvaMovimentoCassa(MovimentoCassa movimentoCassa);

    /**
     * Salva un {@link SistemaElettronico}.
     *
     * @param sistemaElettronico
     *            {@link SistemaElettronico} da salvare
     * @return {@link SistemaElettronico} salvato
     */
    SistemaElettronico salvaSistemaElettronico(SistemaElettronico sistemaElettronico);

    /**
     * Salva un {@link TipoComunicazione}.
     *
     * @param tipoComunicazione
     *            tipo comunicazione da salvare
     * @return tipo comunicazione salvata
     */
    TipoComunicazione salvaTipoComunicazione(TipoComunicazione tipoComunicazione);

    /**
     * Salva un {@link TipoModello}.
     *
     * @param tipoModello
     *            tipo modello da salvare
     * @return tipo modello salvato
     */
    TipoModello salvaTipoModello(TipoModello tipoModello);

    /**
     * Salva un {@link VendingSettings}.
     *
     * @param vendingSettings
     *            settings da salvare
     * @return <code>VendingSettings</code> salvato
     */
    VendingSettings salvaVendingSettings(VendingSettings vendingSettings);


    /**
     * Cancella un {@link Asl}.
     *
     * @param id
     *            id Asl da cancellare
     */
    void cancellaAsl(Integer id);

    /**
     * Carica tutti i {@link Asl} presenti.
     *
     * @return {@link Asl} caricati
     */
    List<Asl> caricaAsl();

    /**
     * Carica un {@link Asl} in base al suo id.
     *
     * @param id
     *            id
     * @return {@link Asl} caricato
     */
    Asl caricaAslById(Integer id);

    /**
     * Salva un {@link Asl}.
     *
     * @param asl
     *            {@link Asl} da salvare
     * @return {@link Asl} salvato
     */
    Asl salvaAsl(Asl asl);
    
}
