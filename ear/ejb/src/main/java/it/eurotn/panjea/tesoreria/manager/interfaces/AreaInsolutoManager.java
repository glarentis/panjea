/**
 * 
 */
package it.eurotn.panjea.tesoreria.manager.interfaces;

import it.eurotn.panjea.tesoreria.domain.AreaInsoluti;
import it.eurotn.panjea.tesoreria.domain.Effetto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.ejb.Local;

/**
 * Gestore Area Insoluti.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 * 
 */
@Local
public interface AreaInsolutoManager extends IAreaTesoreriaDAO {
	/**
	 * Annulla insoluto relativo all'effetto.
	 * 
	 * @param effetto
	 *            di cui si annulla linsoluto
	 */
	void annullaInsoluto(Effetto effetto);

	/**
	 * Creazione dell'area insoluti.
	 * 
	 * @param dataDocumento
	 *            relativa al doc da generare
	 * @param nDocumento
	 *            relativo al doc da generare
	 * @param speseInsoluto
	 *            spese insoluto
	 * @param effetti
	 *            da cui prendere il riferimento dell'area effetti
	 * @return l'area creata
	 */
	AreaInsoluti creaAreaInsoluti(Date dataDocumento, String nDocumento, BigDecimal speseInsoluto, Set<Effetto> effetti);

}
