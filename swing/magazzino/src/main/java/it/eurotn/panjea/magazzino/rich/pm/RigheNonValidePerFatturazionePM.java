/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.pm;

import it.eurotn.panjea.magazzino.domain.RigaArticoloLite;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;

import java.io.Serializable;
import java.util.List;

/**
 * Classe che contiene la liste di tutte le righe che non sono risultate valide per la fatturazione differita.
 * 
 * @author fattazzo
 * 
 */
public class RigheNonValidePerFatturazionePM implements Serializable {

	private static final long serialVersionUID = 7565557645559132062L;

	private List<RigaArticoloLite> righeNonValide;

	private ParametriRicercaFatturazione parametriRicercaFatturazione;

	/**
	 * @return the parametriRicercaFatturazione
	 */
	public ParametriRicercaFatturazione getParametriRicercaFatturazione() {
		return parametriRicercaFatturazione;
	}

	/**
	 * @return the righeNonValide
	 */
	public List<RigaArticoloLite> getRigheNonValide() {
		return righeNonValide;
	}

	/**
	 * @param parametriRicercaFatturazione
	 *            the parametriRicercaFatturazione to set
	 */
	public void setParametriRicercaFatturazione(ParametriRicercaFatturazione parametriRicercaFatturazione) {
		this.parametriRicercaFatturazione = parametriRicercaFatturazione;
	}

	/**
	 * @param righeNonValide
	 *            the righeNonValide to set
	 */
	public void setRigheNonValide(List<RigaArticoloLite> righeNonValide) {
		this.righeNonValide = righeNonValide;
	}

}
