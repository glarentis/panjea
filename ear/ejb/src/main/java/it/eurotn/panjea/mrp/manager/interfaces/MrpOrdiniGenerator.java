package it.eurotn.panjea.mrp.manager.interfaces;

import javax.ejb.Local;

@Local
public interface MrpOrdiniGenerator {
	/**
	 * genera gli ordini in base alla tabella del risultato mrp.
	 *
	 * @param idRisultatiMrp array contenente gli id dei risultati di cui generare gli ordini
	 * @return timeStampCreazione
	 */
	Long generaOrdini(Integer[] idRisultatiMrp);
}
