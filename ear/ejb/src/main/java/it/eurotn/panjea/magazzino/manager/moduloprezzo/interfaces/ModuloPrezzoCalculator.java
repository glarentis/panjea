package it.eurotn.panjea.magazzino.manager.moduloprezzo.interfaces;

import java.util.Set;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.moduloprezzo.ParametriCalcoloPrezzi;

/**
 * Interfaccia implementata dai Bean che devono calcolare il prezzo di un articolo.
 *
 * @author giangi
 *
 */
@Local
public interface ModuloPrezzoCalculator {

    /**
     * 
     * @param parametriCalcoloPrezzi
     *            parametri per calcolare i prezzi e con i valori di eventuali altri moduli prezzo settati.
     * @return parametri del calcolo dei prezzi aggiornati
     */
    ParametriCalcoloPrezzi calcola(ParametriCalcoloPrezzi parametriCalcoloPrezzi);

    /**
     * 
     * @param parametriCalcoloPrezzi
     *            parametri per il calcolo del prezzo
     * @return lista di articoli interessati a questo modulo (ad es. per il listino tutti gli articoli in listino)
     */
    Set<ArticoloLite> caricaArticoli(ParametriCalcoloPrezzi parametriCalcoloPrezzi);
}
