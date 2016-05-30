package it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces;

import java.util.Set;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.PoliticaPrezzo;

/**
 * Interfaccia di ModuloPrezzoManager: il manager espone i metodi di caricamento e modifica dei moduli prezzo
 * disponibili e di calcolo del prezzo dell'articolo.
 *
 * @author fattazzo
 *
 */
@Local
public interface PrezzoArticoloCalculator {

    /**
     * Il metodo carica secondo l'ordinamento selezionato tutti i moduli prezzo presenti e dati i parametri calcola per
     * ogni modulo il prezzo e lo sconto dell'articolo.
     * 
     * @param parametriCalcoloPrezzi
     *            parametri per il calcolo
     * @return politica prezzo caricata
     */
    PoliticaPrezzo calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi);

    /**
     * 
     * @param parametriCalcoloPrezzi
     *            parametri per il calcolo del prezzo
     * @return lista di articoli interessati al calcolo.
     */
    Set<ArticoloLite> caricaArticoli(ParametriCalcoloPrezzi parametriCalcoloPrezzi);

    /**
     * Inserisce i valori a 0 per tutti i risultati della politica prezzo vuoti..
     * 
     * @param politicaPrezzo
     *            politica prezzo
     * @param idArticolo
     *            id articolo di riferimento
     * @return politica prezzo modificata
     */
    PoliticaPrezzo fillOnEmpty(PoliticaPrezzo politicaPrezzo, Integer idArticolo);
}
