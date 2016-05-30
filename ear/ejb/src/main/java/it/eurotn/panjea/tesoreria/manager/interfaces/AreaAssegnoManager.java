package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.tesoreria.domain.AreaAccreditoAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaAssegno;
import it.eurotn.panjea.tesoreria.domain.AreaTesoreria;
import it.eurotn.panjea.tesoreria.domain.Pagamento;
import it.eurotn.panjea.tesoreria.service.exception.EntitaRateNonCoerentiException;
import it.eurotn.panjea.tesoreria.util.AssegnoDTO;
import it.eurotn.panjea.tesoreria.util.ParametriCreazioneAreaChiusure;
import it.eurotn.panjea.tesoreria.util.ParametriRicercaAssegni;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AreaAssegnoManager extends IAreaTesoreriaDAO {

	/**
	 * Assegna il {@link RapportoBancarioAzienda} a tutte le aree assegno.
	 * 
	 * @param rapportoBancarioAzienda
	 *            {@link RapportoBancarioAzienda} da assegnare
	 * @param areeAssegno
	 *            aree assegno
	 */
	void assegnaRapportoBancarioAssegni(RapportoBancarioAzienda rapportoBancarioAzienda, List<AreaAssegno> areeAssegno);

	/**
	 * Cancella l'area assegno se non c' una area accredito assegno collegata.
	 * 
	 * @param areaTesoreria
	 *            l'area tesoreria da cancellare
	 * @param deleteAreeCollegate
	 *            se cancellare anche le aree collegate e il documento o cancellare solo l'areaAssegno
	 */
	void cancellaAreaTesoreria(AreaTesoreria areaTesoreria, boolean deleteAreeCollegate);

	/**
	 * Carica l'areaAssegno con i pagamenti associati.
	 * 
	 * @param idAreaTesoreria
	 *            l'area assegno da caricare
	 * @return areaAssegno
	 * @throws ObjectNotFoundException
	 *             se non esiste l'area con l'id specificato
	 */
	AreaTesoreria caricaAreaTesoreria(Integer idAreaTesoreria) throws ObjectNotFoundException;

	/**
	 * Carica la lista di AreaAssegno collegate all'areaAccreditoAssegno scelta.
	 * 
	 * @param areaAccreditoAssegno
	 *            l'areaAccreditoAssegno di cui caricare la lista di AreaAssegno
	 * @return List<AreaAssegno>
	 */
	List<AreaAssegno> caricaAreeAssegno(AreaAccreditoAssegno areaAccreditoAssegno);

	/**
	 * Carica gli assegni (AreaAssegno, AreaAccreditoAssegno) secondo i ParametriRicercaAssegni passati.
	 * 
	 * @param parametriRicercaAssegni
	 *            i parametri per limitare la ricerca degli assegni
	 * @return List<AssegnoDTO>
	 */
	List<AssegnoDTO> caricaAssegni(ParametriRicercaAssegni parametriRicercaAssegni);

	/**
	 * Carica l'immagine e la associa all'assegno corrente.
	 * 
	 * @param areaAssegno
	 *            l'area assegno a cui legare l'immagine assegno
	 * @return AreaAssegno con ImmagineAssegno legata
	 */
	AreaAssegno caricaImmagineAssegno(AreaAssegno areaAssegno);

	/**
	 * Crea una area assegno a seconda dei parametri passati e associa la lista di pagamenti.
	 * 
	 * @param parametriCreazioneAreaChiusure
	 *            i parametri secondo cui creare l'areaAssegno
	 * @param pagamenti
	 *            i pagamenti da associare al documento
	 * @return AreaAssegno
	 * @throws DocumentoDuplicateException
	 *             DocumentoDuplicateException
	 * @throws EntitaRateNonCoerentiException
	 *             la lista di rate non è della stessa entità
	 */
	AreaAssegno creaAreaAssegno(ParametriCreazioneAreaChiusure parametriCreazioneAreaChiusure, List<Pagamento> pagamenti)
			throws DocumentoDuplicateException, EntitaRateNonCoerentiException;

}
