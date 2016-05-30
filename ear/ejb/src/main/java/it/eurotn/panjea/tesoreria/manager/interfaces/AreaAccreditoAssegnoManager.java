/**
 * 
 */
package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;

import java.util.List;

import javax.ejb.Local;

/**
 * @author leonardo
 */
@Local
public interface AreaAccreditoAssegnoManager extends IAreaTesoreriaDAO {

	/**
	 * Cancella l'area accredito assegno e quindi annulla la data pagamento delle aree assegno collegate e il relativo
	 * link tra pagamento dell'area assegno e area accredito assegno.
	 * 
	 * @param areaTesoreria
	 *            l'area tesoreria da cancellare
	 * @param deleteAreeCollegate
	 *            se cancellare anche le aree collegate e il documento o cancellare solo l'areaAccreditoAssegno
	 */
	void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate);

	/**
	 * Carica l'areaAccreditoAssegno con i pagamenti associati.
	 * 
	 * @param idAreaTesoreria
	 *            l'area accredito assegno da caricare
	 * @return areaAccreditoAssegno
	 * @throws ObjectNotFoundException
	 *             se non esiste l'area con l'id specificato
	 */
	AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException;

	/**
	 * Crea un documento di accredito da una lista di assegni scelti associando i pagamenti degli assegni all'accredito.
	 * 
	 * @param parametriCreazioneAreaChiusure
	 *            i parametri da cui recuperare codice,data,rapporto bancario
	 * @param assegni
	 *            gli assegni da accreditare.
	 * @return AreaAccreditoAssegno
	 */
	AreaAccreditoAssegno creaAreaAccreditoAssegno(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure,
			List<AreaAssegno> assegni);

}
