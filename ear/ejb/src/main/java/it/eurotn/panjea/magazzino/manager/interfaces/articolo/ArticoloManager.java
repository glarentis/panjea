package it.eurotn.panjea.magazzino.manager.interfaces.articolo;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import it.eurotn.panjea.anagrafica.domain.CodiceIva;
import it.eurotn.panjea.conai.domain.ConaiComponente;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloAlternativo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.Categoria;
import it.eurotn.panjea.magazzino.domain.CategoriaCommercialeArticolo;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.ParametriRicercaArticolo;
import it.eurotn.panjea.riepilogo.util.RiepilogoArticoloDTO;

/**
 *
 * Interfaccia Manager di Articolo.
 *
 * @author adriano
 * @version 1.0, 21/mag/08
 *
 */
@Local
public interface ArticoloManager {

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
     *
     * @return un codice ean calcolato in base al codice GIS1 e protocollo in anagrafica settings
     */
    String calcolaEAN();

    /**
     * metodo incaricato di variare la Categoria di appartenenza agli articoli passati come
     * argomento.<br>
     * attualmente il metodo non gestisce il controllo di dviersità di attributi tra le due
     * categorie, <br>
     * pertanto se la categoria sorgente o di destinazione hanno degli attributi il cambio non viene
     * effettuato
     *
     * @param articoli
     *            lista di articoli da modificare
     * @param categoriaDestinazione
     *            nuova categoria da assegnare agli articoli
     */
    void cambiaCategoriaAdArticoli(List<ArticoloRicerca> articoli, Categoria categoriaDestinazione);

    /**
     * metodo incaricato di variare la categoria commerciale degli articoli ricevuti.
     *
     * @param articoli
     *            gli articoli di cui modificare la categoria commerciale
     * @param categoriaCommercialeArticolo
     *            la categoria di destinazione da associare agli articoli
     * @param categoriaCommercialeArticolo2
     *            la categoria 2 di destinazione da associare agli articoli
     */
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
     *
     * @param articolo
     *            articolo da cancellare
     */
    void cancellaArticolo(Articolo articolo);

    /**
     * Cancella l'articolo alternativo.
     *
     * @param articoloAlternativo
     *            articoloAlternativo da cancellare
     */
    void cancellaArticoloAlternativo(ArticoloAlternativo articoloAlternativo);

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
     * Carica tutti gli <code>Articolo</code> per l'azienda loggata.
     *
     * @return <code>List</code> di <code>Articolo</code>
     */
    List<Articolo> caricaArticoliFull();

    /**
     * Carica un articolo.
     *
     * @param articolo
     *            articolo da caricare
     * @param initializeLazy
     *            indica se devono essere inizializzate tutte le collezioni dell'articolo.
     * @return articolo caricato
     */
    Articolo caricaArticolo(Articolo articolo, boolean initializeLazy);

    /**
     * @param idArticolo
     *            id articolo
     * @return articolo caricato
     *
     */
    ArticoloLite caricaArticoloLite(int idArticolo);

    /**
     * Carica tutti gli attributi dell'articolo specificato.
     *
     * @param articolo
     *            articolo
     * @return attributi caricati
     */
    List<AttributoArticolo> caricaAttributiArticolo(Articolo articolo);

    /**
     * Restituisce tutti gli attributi dell'articolo necessari per la riga del documento.
     *
     * @param idArticolo
     *            id articolo
     * @return attributi
     */
    List<AttributoArticolo> caricaAttributiDaInserireInRiga(Integer idArticolo);

    /**
     * Carica i componenti conai che compongono l'articolo scelto.
     *
     * @param idArticolo
     *            l'id dell'articolo di cui caricare i componenti conai
     * @return Set<ConaiComponente>
     */
    Set<ConaiComponente> caricaComponentiConai(Integer idArticolo);

    /**
     * Carica il riepilogo degli articoli dell'azienda loggata.
     *
     * @return riepilogo caricato
     */
    List<RiepilogoArticoloDTO> caricaRiepilogoArticoli();

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
    Articolo cloneArticolo(Articolo articolo, String nuovoCodice, String nuovaDescrizione, boolean copyDistinta,
            boolean copyDistintaConfigurazioni, List<AttributoArticolo> attributi, boolean copyListino,
            boolean azzeraPrezziListino);

    /**
     * Crea un articolo con gli attributi creati tramite i TipiAttributi sulla categoria.
     *
     * @param categoria
     *            categoria dove creare l'articolo
     * @return articolo con i valori inizializzati
     */
    Articolo creaArticolo(Categoria categoria);

    /**
     * Effettua la ricerca su tutti gli articoli in base ai dati contenuti nell'oggetto
     * <code>ParametriRicercaArticolo</code>. <br>
     * <b>NB</b>:La ricerca descrizione viene effettuata nella descrizione della lingua utente
     *
     * @param parametriRicercaArticolo
     *            parametri di ricerca
     * @return articoli trovati
     */
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

    /**
     * Esegue la ricerca dell'articolo per il codice in modo esatto .
     *
     * @param codiceArticolo
     *            codice da ricercare
     * @return articloloLite con il codice ricercato
     */
    ArticoloLite ricercaArticoloByCodice(String codiceArticolo);

    /**
     * Salva un articolo con gli attributi e la descrizione delle lingue.
     *
     * @param articolo
     *            da salvare
     * @return articolo salvato
     */
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
     * Salva un attributo articolo.
     *
     * @param attributoArticolo
     *            attributo da salvare
     * @return attributo salvato
     */
    AttributoArticolo salvaAttributoArticolo(AttributoArticolo attributoArticolo);

}
