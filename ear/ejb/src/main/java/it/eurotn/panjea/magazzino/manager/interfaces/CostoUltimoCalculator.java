package it.eurotn.panjea.magazzino.manager.interfaces;

import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;

import java.util.List;

import javax.ejb.Local;

@Local
public interface CostoUltimoCalculator {

	/**
	 * Valorizza con il valore del costo ultimo tutte le righe di {@link ValorizzazioneArticolo}.
	 * 
	 * @param listValorizzazioni
	 *            righe di valorizzazione
	 * @return righe di valorizzazione aggiornate con il costo ultimo
	 */
	List<ValorizzazioneArticolo> valorizzaCostoUltimo(List<ValorizzazioneArticolo> listValorizzazioni);
}
