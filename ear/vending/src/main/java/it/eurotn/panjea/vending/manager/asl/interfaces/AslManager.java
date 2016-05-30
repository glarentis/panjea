package it.eurotn.panjea.vending.manager.asl.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.Asl;

@Local
public interface AslManager extends CrudManager<Asl> {
}