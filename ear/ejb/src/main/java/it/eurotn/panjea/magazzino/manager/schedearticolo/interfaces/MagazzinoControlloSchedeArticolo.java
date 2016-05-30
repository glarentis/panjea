/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces;

import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface MagazzinoControlloSchedeArticolo {

	/**
	 * Verifica se devono essere invalidate le schede articolo degli articolo dell'area magazzino.
	 * 
	 * @param areaMagazzino
	 *            area magazzino
	 */
	void checkInvalidaSchedeArticolo(AreaMagazzino areaMagazzino);

	/**
	 * Verifica se devono essere invalidate le schede degli articoli dell'area magazzino.
	 * 
	 * @param areaMagazzino
	 *            area magazzino presente
	 * @param controllaConAreaPresente
	 *            controlla se invalidare le schede articolo in base all'area magazzino presente
	 */
	void checkInvalidaSchedeArticolo(AreaMagazzino areaMagazzino, boolean controllaConAreaPresente);

	/**
	 * Verifica se devono essere invalidate le schede dell'articolo della riga articolo.
	 * 
	 * @param rigaArticolo
	 *            riga articolo
	 */
	void checkInvalidaSchedeArticolo(RigaArticolo rigaArticolo);

	/**
	 * Verifica se devono essere invalidate le chede dell'articolo della riga articolo.
	 * 
	 * @param rigaArticoloPrecedente
	 *            riga articolo presente
	 * @param rigaArticoloSalvata
	 *            nuova riga articolo
	 */
	void checkInvalidaSchedeArticolo(RigaArticolo rigaArticoloPrecedente, RigaArticolo rigaArticoloSalvata);

}
