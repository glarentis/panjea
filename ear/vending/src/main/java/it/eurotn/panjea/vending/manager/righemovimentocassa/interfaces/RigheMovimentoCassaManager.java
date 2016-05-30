package it.eurotn.panjea.vending.manager.righemovimentocassa.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.RigaMovimentoCassa;

@Local
public interface RigheMovimentoCassaManager extends CrudManager<RigaMovimentoCassa> {
}