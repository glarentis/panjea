/**
 * 
 */
package it.eurotn.panjea.offerte.manager.interfaces;

import it.eurotn.panjea.offerte.domain.RigaOfferta;

import java.util.List;

import javax.ejb.Local;

/**
 * @author Leonardo
 */
@Local
public interface RigheOffertaManager {

	/**
	 * Carica le righe dell'area offerta.
	 * 
	 * @param idAreaOfferta
	 *            area di riferimento
	 * @return righe caricate
	 */
	List<RigaOfferta> caricaRigheOfferta(Integer idAreaOfferta);

	/**
	 * Crea una riga offerta.
	 * 
	 * @param idArticolo
	 *            id articolo
	 * @return riga creata
	 */
	RigaOfferta creaRigaOfferta(Integer idArticolo);

	/**
	 * Salva una riga offerta.
	 * 
	 * @param rigaOfferta
	 *            riga da salvare
	 * @return riga salvata
	 */
	RigaOfferta salvaRigaOfferta(RigaOfferta rigaOfferta);

}
