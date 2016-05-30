/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface MagazzinoStatoSchedeArticolo {

	/**
	 * Invalida tutte le schede degli articoli dell'area magazzino indicata.
	 * 
	 * @param areaMagazzino
	 *            area magazzino
	 */
	void invalidaSchedeArticolo(AreaMagazzino areaMagazzino);

	/**
	 * Invalida le schede dell'articolo indicato a partire dal mese e anno specificato.
	 * 
	 * @param anno
	 *            anno
	 * @param mese
	 *            mese
	 * @param idArticolo
	 *            id articolo
	 */
	void invalidaSchedeArticolo(Integer anno, Integer mese, Integer idArticolo);

}
