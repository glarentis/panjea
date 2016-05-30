/**
 * 
 */
package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface GestioneInventarioArticoloDocumentoManager {

	/**
	 * Cancella un inventario in preparazione.
	 * 
	 * @param data
	 *            data
	 * @param deposito
	 *            deposito
	 */
	void cancellaInventarioArticolo(Date data, DepositoLite deposito);

	/**
	 * Crea e salva l'area magazzino per il tipo area magazzino richiesto.
	 * 
	 * @param tipoAreaMagazzino
	 *            tipo area magazzino
	 * @param dataMovimento
	 *            data movimento
	 * @param deposito
	 *            deposito
	 * @return {@link AreaMagazzino}
	 */
	AreaMagazzino creaAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino, Date dataMovimento, DepositoLite deposito);

	/**
	 * Crea la riga articolo per l'area magazzino, l'articolo e la quantità richiesti.
	 * 
	 * @param areaMagazzino
	 *            area magazzino
	 * @param articolo
	 *            articolo
	 * @param quantita
	 *            quantità
	 * @param linguaAzienda
	 *            lingua aziendale
	 */
	void creaRigaArticolo(AreaMagazzino areaMagazzino, Articolo articolo, double quantita, String linguaAzienda);

	/**
	 * Totalizza le aree magazzino passate come parametro.
	 * 
	 * @param areeMagazzino
	 *            aree
	 * @return aree magazzino totalizzate
	 */
	List<AreaMagazzinoRicerca> totalizzaAreeMagazzino(List<AreaMagazzino> areeMagazzino);
}
