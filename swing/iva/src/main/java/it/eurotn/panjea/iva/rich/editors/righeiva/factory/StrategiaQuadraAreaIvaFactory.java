/**
 * 
 */
package it.eurotn.panjea.iva.rich.editors.righeiva.factory;

import it.eurotn.panjea.contabilita.domain.TipoAreaContabile.GestioneIva;

import org.apache.log4j.Logger;

/**
 * Factory per recuperare il quadratore area iva appropriato per la gestione iva specificata. Nel tipo area contabile va
 * specificato se il documento ha una gestione iva INTRA, ART.17 o NORMALE, a seconda del tipo la quadratura dell'area
 * iva e' diversa. Nel caso di INTRA e ART17 l'area iva e' quadrata se <code>tot.doc.=tot.imponibile</code>; nel caso di
 * gestione iva NORMALE invece e' quadrata se <code>tot.doc.=tot.imponibile+tot.imposta</code> ulteriore caso
 * considerato e' quello in cui l'area iva non e' attiva per il documento considerato.
 * 
 * @author Leonardo
 */
public final class StrategiaQuadraAreaIvaFactory {

	private static Logger logger = Logger.getLogger(StrategiaQuadraAreaIvaFactory.class);

	/**
	 * Metodo statico che recupera il quadratore area iva appropriato per la Gestione Iva scelta.
	 * 
	 * @param gestioneIva
	 *            la gestione iva di cui recuperare il quadratore
	 * @return QuadraAreaIva appropriato alla gestione iva scelta
	 */
	public static StrategiaQuadraAreaIva getQuadratoreAreaIva(GestioneIva gestioneIva) {
		logger.debug("--> Enter getQuadratoreAreaIva per gestione iva " + gestioneIva);

		StrategiaQuadraAreaIva quadraAreaIva = null;

		// se la gestione iva e' null considero l'area iva disattivata
		// (in caso contrario deve essere settata obbligatoriamente una gestione iva per il documento)
		if (gestioneIva == null) {
			quadraAreaIva = new QuadraAreaIvaAssente();
		} else {
			// ho due casi che definiscono come quadrare l'area iva:
			// per gestione iva normale e per i tipi restanti (INTRA,
			// ART.17) che hanno lo stesso comportamento
			if (GestioneIva.NORMALE.compareTo(gestioneIva) == 0) {
				quadraAreaIva = new QuadraAreaIvaNormale();
			} else {
				quadraAreaIva = new QuadraAreaIvaAlternativa();
			}
		}
		logger.debug("--> Exit getQuadratoreAreaIva " + quadraAreaIva.getClass().getName());
		return quadraAreaIva;
	}

	/**
	 * Costruttore privato.
	 */
	private StrategiaQuadraAreaIvaFactory() {
		super();
	}

}
