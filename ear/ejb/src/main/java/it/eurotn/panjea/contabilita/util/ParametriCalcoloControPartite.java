/**
 *
 */
package it.eurotn.panjea.contabilita.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Parametri per la creazione delle righe contabili in base alle contro partite.
 * 
 * @author Fattazzo
 */
public class ParametriCalcoloControPartite extends HashMap<String, Map<String, BigDecimal>> {

	private static final long serialVersionUID = -6184351284868453133L;

	public static final String SENZA_CODICE_IVA = "SENZAIVA";

}
