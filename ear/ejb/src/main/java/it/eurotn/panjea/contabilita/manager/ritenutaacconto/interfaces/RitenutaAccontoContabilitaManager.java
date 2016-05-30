/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces;

import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface RitenutaAccontoContabilitaManager {

	/**
	 * AGgiorna se necessario l'importo sul quale deve essere calcolata la ritenuta d'acconto.
	 * 
	 * @param idDocumento
	 *            documento di riferimento
	 */
	void aggiornaImponibileDatiRitenutaAcconto(Integer idDocumento);

	/**
	 * Assegna i dati della ritenuta d'acconto all'area contabile.
	 * 
	 * @param areaContabile
	 *            area contabile
	 * @return area contabile aggiornata
	 */
	AreaContabile assegnaDatiRitenutaAccontoAreaContabile(AreaContabile areaContabile);

	/**
	 * Genera le righe contabili per la chiusura del conto erario temporaneo e l'apertura del conto erario da pagare.
	 * 
	 * @param areaContabile
	 *            area contabile di riferimento
	 * @param pagamenti
	 *            pagamenti
	 */
	void creaRigheContabiliPerErarioDaPagare(AreaContabile areaContabile, Set<Pagamento> pagamenti);

	/**
	 * Genera le righe contabili per la chiusura del conto erario da pagare e l'apertura del conto erario pagato.
	 * 
	 * @param areaContabile
	 *            area contabile di riferimento
	 * @param pagamenti
	 *            pagamenti
	 * @param righeContabiliEntita
	 *            righe contabili che si riferiscono alle entità
	 */
	void creaRigheContabiliPerErarioPagato(AreaContabile areaContabile, Set<Pagamento> pagamenti,
			List<RigaContabile> righeContabiliEntita);

	/**
	 * Crea le righe contabili per la gestione della ritenuta d'acconto.
	 * 
	 * @param areaContabile
	 *            area di riferimento
	 * @param ordinamento
	 *            ordinamento di partenza
	 * @return numero di righe create
	 * @throws ContiBaseException
	 *             sollevata se non viene trovato un conto base
	 * @throws ContabilitaException .
	 */
	int creaRigheContabiliRitenutaAcconto(AreaContabile areaContabile, long ordinamento) throws ContabilitaException,
			ContiBaseException;

	/**
	 * Restituisce l'importo della riga contabile che si riferisce alla ritenuta d'acconto.
	 * 
	 * @param documento
	 *            documento
	 * @return importo
	 */
	BigDecimal getImportoRitenutaAcconto(Documento documento);

	/**
	 * Alla mappa di riferimento aggiunge tutte le variabili e importi relativi alla ritenuta acconto se gestita.
	 * 
	 * @param areaContabile
	 *            area contabile di riferimento
	 * @return mappa
	 */
	Map<String, BigDecimal> getMapVariabiliFromRitenutaAcconto(AreaContabile areaContabile);
}
