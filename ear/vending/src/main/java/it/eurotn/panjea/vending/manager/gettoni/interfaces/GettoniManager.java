package it.eurotn.panjea.vending.manager.gettoni.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.Gettone;

@Local
public interface GettoniManager extends CrudManager<Gettone> {
}