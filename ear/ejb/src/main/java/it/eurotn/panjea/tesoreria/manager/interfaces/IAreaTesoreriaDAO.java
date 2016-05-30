package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.tesoreria.domain.AreaEffetti;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;

import java.util.List;

public interface IAreaTesoreriaDAO {

	/**
	 * Cancella una {@link AreaTesoreria}.
	 * 
	 * @param areaTesoreria
	 *            area da cancellare
	 * @param deleteAreeCollegate
	 *            <code>true</code> per la cancellazione delle aree collegate
	 */
	void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate);

	/**
	 * Carica una {@link AreaTesoreria}.
	 * 
	 * @param idAreaTesoreria
	 *            id dell'area da caricare
	 * @return area caricata
	 * @throws ObjectNotFoundException
	 *             rilaciata se l'area da caricare non esiste
	 */
	AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException;

	/**
	 * Restituisce la lista delle aree emissione effetti relative all' {@link AreaTesoreria} di riferimento.
	 * 
	 * @param areaTesoreria
	 *            area tesoreria
	 * @return lista di aree emissione effetti
	 */
	List<AreaEffetti> getAreeEmissioneEffetti(AreaTesoreria areaTesoreria);
}
