package it.eurotn.panjea.preventivi.domain.interfaces;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.preventivi.util.interfaces.IRigaDTO;

public interface IRigaDocumento extends IDefProperty, Cloneable {
	/**
	 * 
	 * @return crea nuova riga dto a partire dall'istanza corrente
	 */
	IRigaDTO creaRigaDTO();

	/**
	 * 
	 * @return livello riga
	 */
	int getLivello();

	/**
	 * 
	 * @return numero riga
	 */
	int getNumeroRiga();

	/**
	 * 
	 * @return ordinamento
	 */
	double getOrdinamento();
}
