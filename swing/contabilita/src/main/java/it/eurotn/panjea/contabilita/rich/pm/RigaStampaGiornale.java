/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.pm;

import it.eurotn.panjea.contabilita.util.RigaContabileDTO;

/**
 * 
 * 
 * @author fattazzo
 * @version 1.0, 03/ott/07
 * 
 */
public class RigaStampaGiornale {

	private boolean isRigaContabile;
	private boolean isAreaContabile;

	private boolean isFiller;

	private RigaContabileDTO rigaCont;

	private Integer numeroRegistrazione;

	/**
	 * Costruttore.
	 * 
	 * @param isRigaContabile
	 *            isRigaContabile
	 * @param isAreaContabile
	 *            isAreaContabile
	 * @param isFiller
	 *            isFiller
	 * @param rigaContabile
	 *            rigaContabile
	 */
	public RigaStampaGiornale(final boolean isRigaContabile, final boolean isAreaContabile, final boolean isFiller,
			final RigaContabileDTO rigaContabile) {
		super();
		this.isRigaContabile = isRigaContabile;
		this.isAreaContabile = isAreaContabile;
		this.isFiller = isFiller;
		this.rigaCont = rigaContabile;
		this.numeroRegistrazione = new Integer(0);
	}

	/**
	 * Costruttore.
	 * 
	 * @param isRigaContabile
	 *            isRigaContabile
	 * @param isAreaContabile
	 *            isAreaContabile
	 * @param isFiller
	 *            isFiller
	 * @param rigaContabile
	 *            rigaContabile
	 * @param numeroRegistrazione
	 *            numeroRegistrazione
	 */
	public RigaStampaGiornale(final boolean isRigaContabile, final boolean isAreaContabile, final boolean isFiller,
			final RigaContabileDTO rigaContabile, final Integer numeroRegistrazione) {
		this(isRigaContabile, isAreaContabile, isFiller, rigaContabile);
		this.numeroRegistrazione = numeroRegistrazione;
	}

	/**
	 * @return the numeroRegistrazione
	 */
	public Integer getNumeroRegistrazione() {
		return numeroRegistrazione;
	}

	/**
	 * @return the rigaCont
	 */
	public RigaContabileDTO getRigaCont() {
		return rigaCont;
	}

	/**
	 * @return the isAreaContabile
	 */
	public boolean isAreaContabile() {
		return isAreaContabile;
	}

	/**
	 * @return the isFiller
	 */
	public boolean isFiller() {
		return isFiller;
	}

	/**
	 * @return the isRigaContabile
	 */
	public boolean isRigaContabile() {
		return isRigaContabile;
	}

}
