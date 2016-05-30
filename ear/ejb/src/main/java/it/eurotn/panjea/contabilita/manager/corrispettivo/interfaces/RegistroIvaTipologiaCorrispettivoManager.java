/**
 *
 */
package it.eurotn.panjea.contabilita.manager.corrispettivo.interfaces;

import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.contabilita.util.LiquidazioneIvaDTO;

import java.util.Date;

import javax.ejb.Local;

/**
 * Interfaccia manager per caricare i totali del registro iva corrispettivo considerando i diversi tipo corrispettivo.
 * 
 * @author Leonardo
 */
@Local
public interface RegistroIvaTipologiaCorrispettivoManager {

	/**
	 * Carica le righe del registro iva corrispettivo considerando le diverse tipologie corrispettivo preparando così il
	 * riepilogo dei codici iva raggruppati per codice e il riepilogo di righe ventilazione per la relativa tabella.
	 * 
	 * @param registroIva
	 *            il registro iva che deve essere di tipo corrispettivo, pena il lancio di una exception
	 * @param dataInizioPeriodo
	 *            inizio periodo in cui eseguire la ricerca
	 * @param dataFinePeriodo
	 *            fine periodo in cui eseguire la ricerca
	 * @return LiquidazioneIvaDTO
	 */
	LiquidazioneIvaDTO caricaTotali(RegistroIva registroIva, Date dataInizioPeriodo, Date dataFinePeriodo);
}
