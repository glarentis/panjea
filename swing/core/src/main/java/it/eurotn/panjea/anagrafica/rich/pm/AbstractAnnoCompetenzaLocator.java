/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.pm;

/**
 * Classe astratta che recupera l'anno di competenza.
 * 
 * @author Leonardo
 */
public abstract class AbstractAnnoCompetenzaLocator {

	/**
	 * Restituisce l'anno di competenza per la classe specializzata.
	 * 
	 * @return L'integer che definisce l'anno di competenza
	 */
	public abstract Integer getAnnoCompetenza();

}
