/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces;

import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.tesoreria.domain.AreaPagamenti;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface RitenutaAccontoTesoreriaManager {

	/**
	 * Assegna la data di scadenza e importo della rata della ritenuta d'acconto in base al pagamento.
	 * 
	 * @param pagamento
	 *            pagamento
	 */
	void aggiornaRataRitenutaAcconto(Pagamento pagamento);

	/**
	 * Cancella se esiste la rata di ritenuta d'acconto associata a ogni pagamento e ricalcola l'importo della rata di
	 * ritenuta senza data scadenza.
	 * 
	 * @param areaPagamenti
	 *            area pagamenti di riferimento
	 */
	void cancellaRataRitenutaAccontoAssociata(AreaPagamenti areaPagamenti);

	/**
	 * Carica la rata delle ritenuta d'acconto che fa riferimento al pagamento indicato.
	 * 
	 * @param pagamentoRiferimento
	 *            pagamento
	 * @return rata caricata o <code>null</code> se non esiste
	 */
	Rata caricaRataRitenutaAcconto(Pagamento pagamentoRiferimento);

	/**
	 * Crea la rata relativa alla ritenuta d'acconto per l'area contabile di riferimento.
	 * 
	 * @param areaContabile
	 *            area contabile
	 * @param areaRate
	 *            area rate
	 * @param numeroRata
	 *            da assegnare alla rata
	 * @return rate create. <code>null</code> se non è stata creata nessuna rata
	 */
	Rata creaRataRitenutaAcconto(AreaContabile areaContabile, AreaRate areaRate, Integer numeroRata);
}
