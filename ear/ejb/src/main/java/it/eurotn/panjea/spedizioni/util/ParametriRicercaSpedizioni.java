package it.eurotn.panjea.spedizioni.util;

import it.eurotn.panjea.magazzino.util.ParametriRicercaAreaMagazzino;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "para_ricerca_area_magazzino_spedizioni")
public class ParametriRicercaSpedizioni extends ParametriRicercaAreaMagazzino {

	private static final long serialVersionUID = -3295570956786412615L;

}
