/**
 * 
 */
package it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

import java.util.Date;

import javax.ejb.Local;

/**
 * @author fattazzo
 * 
 */
@Local
public interface RataRitenutaAccontoCalculator {

	/**
	 * La data scadenza viene calcolata al giorno 16 del mese successivo della data del pagamento.
	 * 
	 * @param dataPagamento
	 *            data pagamento
	 * @return data scadenza
	 */
	Date calcolaDataScadenza(Date dataPagamento);

	/**
	 * In base al pagamento restituisce l'importo da utilizzare per la creazione della rata della ritenuta d'acconto.
	 * 
	 * @param pagamento
	 *            pagamento
	 * @return importo
	 */
	Importo calcolaImportoRata(Pagamento pagamento);
}
