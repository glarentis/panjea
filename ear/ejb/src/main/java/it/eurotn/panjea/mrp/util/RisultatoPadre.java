package it.eurotn.panjea.mrp.util;

import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.domain.RigheCalcolo;

public class RisultatoPadre {
	private Bom bom;
	private RigheCalcolo[] righeCalcolo;

	/**
	 *
	 * @param bom
	 * @param righeCalcolo
	 */
	public RisultatoPadre(Bom bom, RigheCalcolo[] righeCalcolo) {
		super();
		this.bom = bom;
		this.righeCalcolo = righeCalcolo;
	}

	/**
	 * @return Returns the bom.
	 */
	public Bom getBom() {
		return bom;
	}

	/**
	 * @return Returns the righeCalcolo.
	 */
	public RigheCalcolo[] getRigheCalcolo() {
		return righeCalcolo;
	}

}
