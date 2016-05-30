package it.eurotn.panjea.offerte.manager.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.service.exception.DocumentoDuplicateException;
import it.eurotn.panjea.offerte.domain.AreaOfferta;

import javax.ejb.Local;

/**
 * 
 * @author Leonardo
 */
@Local
public interface AreaOffertaManager {

	/**
	 * Carica l'area offerta.
	 * 
	 * @param idAreaOfferta
	 *            id dell'area da caricare
	 * @return area caricata
	 */
	AreaOfferta caricaAreaOfferta(Integer idAreaOfferta);

	/**
	 * Carica l'area offerta dal documento.
	 * 
	 * @param documento
	 *            documento di riferimento
	 * @return area caricata
	 */
	AreaOfferta caricaAreaOffertaByDocumento(Documento documento);

	/**
	 * Salva l'area offerta.
	 * 
	 * @param areaOfferta
	 *            area da salvare
	 * @return area offerta salva
	 * @throws DocumentoDuplicateException
	 *             rilanciata se esiste già il documento
	 */
	AreaOfferta salvaAreaOfferta(AreaOfferta areaOfferta) throws DocumentoDuplicateException;

	/**
	 * Esegue la totalizzazione dell'area offerta.
	 * 
	 * @param areaOfferta
	 *            area da totalizzare
	 * @return area totalizzata
	 */
	AreaOfferta totalizzaAreaOfferta(AreaOfferta areaOfferta);

}
