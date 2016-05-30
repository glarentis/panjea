package it.eurotn.panjea.vending.manager.tipicomunicazione.interfaces;

import javax.ejb.Local;

import it.eurotn.panjea.manager.interfaces.CrudManager;
import it.eurotn.panjea.vending.domain.TipoComunicazione;

@Local
public interface TipiComunicazioneManager extends CrudManager<TipoComunicazione> {

}
