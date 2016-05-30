package it.eurotn.panjea.vending.manager.anagraficheasl.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.AnagraficaAsl;

@Local
public interface AnagraficheAslManager extends CrudManager<AnagraficaAsl> {
}