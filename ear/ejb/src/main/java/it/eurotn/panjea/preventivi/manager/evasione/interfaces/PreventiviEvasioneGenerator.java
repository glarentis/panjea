/**
 * 
 */
package it.eurotn.panjea.preventivi.manager.evasione.interfaces;

import it.eurotn.panjea.agenti.domain.lite.AgenteLite;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.ordini.domain.documento.TipoAreaOrdine;
import it.eurotn.panjea.preventivi.util.RigaEvasione;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface PreventiviEvasioneGenerator {

	/**
	 * Effettua l'evasione delle righe evasione del preventivo.
	 * 
	 * @param rigaEvasione
	 *            righe da evadere
	 * @param tipoAreaOrdine
	 *            tipo area ordine di destinazione
	 * @param deposito
	 *            deposito di destinazione
	 * @param dataOrdine
	 *            data da assegnare all'ordine
	 * @param agente
	 *            agente che verrà assegnato all'ordine
	 * @param dataConsegnaOrdine
	 *            data di consegna dell'ordine
	 */
	void evadiPreventivi(List<RigaEvasione> rigaEvasione, TipoAreaOrdine tipoAreaOrdine, DepositoLite deposito,
			Date dataOrdine, AgenteLite agente, Date dataConsegnaOrdine);
}
