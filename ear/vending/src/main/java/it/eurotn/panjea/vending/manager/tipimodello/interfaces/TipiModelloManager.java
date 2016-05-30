package it.eurotn.panjea.vending.manager.tipimodello.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.TipoModello;

@Local
public interface TipiModelloManager extends CrudManager<TipoModello> {

}
