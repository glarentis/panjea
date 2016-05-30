/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.fatturazione;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzinoLite;

/**
 * Classe di presentazione per evitare la selezione delle righe gia' scelte e per modificare il renderer per renderle
 * disattive.
 * 
 * @author fattazzo
 * 
 */
public class AreaMagazzinoLitePM {

	/**
	 * Enum per definire lo stato della riga.
	 * 
	 * @author fattazzo
	 */
	public enum StatoRigaAreaMagazzinoLitePM {
		SELEZIONABILE, AGGIUNTO_CARRELLO, NON_SELEZIONABILE
	}

	public static final String SELECTED_PROPERTY = "selected";

	private boolean selected = false;

	private AreaMagazzinoLite areaMagazzinoLite = null;

	private StatoRigaAreaMagazzinoLitePM statoRigaAreaMagazzinoLitePM = null;

	/**
	 * Costruttore.
	 */
	public AreaMagazzinoLitePM() {
		super();
	}

	/**
	 * Costruttore.
	 * 
	 * @param areaMagazzinoLite
	 *            area magazzino di riferimento
	 */
	public AreaMagazzinoLitePM(final AreaMagazzinoLite areaMagazzinoLite) {
		super();
		this.areaMagazzinoLite = areaMagazzinoLite;

		if (!this.areaMagazzinoLite.getRigheNonValide().isEmpty()) {
			this.statoRigaAreaMagazzinoLitePM = StatoRigaAreaMagazzinoLitePM.NON_SELEZIONABILE;
		} else {
			this.statoRigaAreaMagazzinoLitePM = StatoRigaAreaMagazzinoLitePM.SELEZIONABILE;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AreaMagazzinoLitePM other = (AreaMagazzinoLitePM) obj;
		if (areaMagazzinoLite == null) {
			if (other.areaMagazzinoLite != null) {
				return false;
			}
		} else if (!areaMagazzinoLite.getId().equals(other.areaMagazzinoLite.getId())) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return areaMagazzinoLite
	 */
	public AreaMagazzinoLite getAreaMagazzinoLite() {
		return areaMagazzinoLite;
	}

	/**
	 * @return statoRigaAreaMagazzinoLitePM
	 */
	public StatoRigaAreaMagazzinoLitePM getStatoRigaAreaMagazzinoLitePM() {
		return statoRigaAreaMagazzinoLitePM;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((areaMagazzinoLite == null) ? 0 : areaMagazzinoLite.getId().hashCode());
		return result;
	}

	/**
	 * @return selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * 
	 * @param areaMagazzinoLite
	 *            the areaMagazzinoLite to set
	 */
	public void setAreaMagazzinoLite(AreaMagazzinoLite areaMagazzinoLite) {
		this.areaMagazzinoLite = areaMagazzinoLite;
	}

	/**
	 * @param selected
	 *            the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @param statoRigaAreaMagazzinoLitePM
	 *            the statoRigaAreaMagazzinoLitePM to set
	 */
	public void setStatoRigaAreaMagazzinoLitePM(StatoRigaAreaMagazzinoLitePM statoRigaAreaMagazzinoLitePM) {
		this.statoRigaAreaMagazzinoLitePM = statoRigaAreaMagazzinoLitePM;
	}
}
