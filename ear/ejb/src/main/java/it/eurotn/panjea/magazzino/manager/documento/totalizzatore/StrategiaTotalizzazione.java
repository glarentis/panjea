package it.eurotn.panjea.magazzino.manager.documento.totalizzatore;

import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.StrategiaTotalizzazioneDocumento;

import javax.ejb.Local;

/**
 * Restituisce il totalizzatore a seconda della strategia scelta.
 * 
 * @author Leonardo
 */
@Local
public interface StrategiaTotalizzazione {

	/**
	 * Restituisce il {@link Totalizzatore} per la strategia scelta.
	 * 
	 * @param strategiaTotalizzazioneDocumento
	 *            la strategia di totalizzazione
	 * @return il {@link Totalizzatore} adeguato
	 */
	Totalizzatore getTotalizzatore(StrategiaTotalizzazioneDocumento strategiaTotalizzazioneDocumento);

}
