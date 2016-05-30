/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili2.manager.interfaces;

import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabile;
import it.eurotn.panjea.beniammortizzabili2.domain.BeneAmmortizzabileLite;
import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;

import java.math.BigDecimal;

import javax.ejb.Local;

/**
 * 
 * Session Stateful incaricato dell'esecuzione dei calcoli del
 * {@link BeneAmmortizzabile} NOTA: necessario eseguire la chiamata al metodo
 * initialize prima di eseguire altri metodi.
 * 
 * @author Leonardo
 * 
 */
@Local
public interface OperazioniBeneAmmortizzabileManager {

	/**
	 * get totale ammortamento fiscale {@link BeneAmmortizzabile}.
	 * 
	 * @param paramIdBeneAmmortizzabile
	 *            id del bene ammortizzabile
	 * @return totle ammortamento fiscale
	 */
	BigDecimal getTotaleAmmortamentoFiscale(Integer paramIdBeneAmmortizzabile);

	/**
	 * Restituisce il totale ammortizzato del bene.
	 * 
	 * @param paramIdBeneAmmortizzabile
	 *            id del bene ammortizzabile
	 * @return totale ammortizzato
	 */
	BigDecimal getTotaleAmmortizzato(Integer paramIdBeneAmmortizzabile);

	/**
	 * get valore bene di {@link BeneAmmortizzabile}.
	 * 
	 * @param paramIdBeneAmmortizzabile
	 *            id del bene ammortizzabile
	 * @return valore del bene
	 */
	BigDecimal getValoreBene(Integer paramIdBeneAmmortizzabile);

	/**
	 * get valore fondo di {@link BeneAmmortizzabile}.
	 * 
	 * @param paramIdBeneAmmortizzabile
	 *            id del bene ammortizzabile
	 * @return valore del fondo
	 */
	BigDecimal getValoreFondo(Integer paramIdBeneAmmortizzabile);

	/**
	 * Inizializzazione dei valori di tutti {@link BeneAmmortizzabile} necessari
	 * al calcolo del loro ammortamento.
	 * 
	 * @param paramSimulazione
	 *            Simulazione
	 */
	void initialize(Simulazione paramSimulazione);

	/**
	 * Inizializzazione dei valori necessari al calcolo di un singolo
	 * {@link BeneAmmortizzabileLite}.
	 * 
	 * @param paramSimulazione
	 *            Simulazione
	 * @param paramIdBeneAmmortizzabile
	 *            id del bene ammortizzabile
	 */
	void initialize(Simulazione paramSimulazione, Integer paramIdBeneAmmortizzabile);

}
