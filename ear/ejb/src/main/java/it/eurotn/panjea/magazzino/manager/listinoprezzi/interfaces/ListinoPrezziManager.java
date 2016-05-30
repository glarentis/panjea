package it.eurotn.panjea.magazzino.manager.listinoprezzi.interfaces;

import java.util.List;

import javax.ejb.Local;

import it.eurotn.panjea.magazzino.manager.listinoprezzi.ListinoPrezziDTO;
import it.eurotn.panjea.magazzino.manager.listinoprezzi.ParametriListinoPrezzi;

@Local
public interface ListinoPrezziManager {
    /**
     * @param parametri
     *            parametri per il calcolo prezzo
     * @return lista di prezzi per articolo
     */
    List<ListinoPrezziDTO> caricaListinoPrezzi(ParametriListinoPrezzi parametri);

}
