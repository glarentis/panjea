package it.eurotn.panjea.ordini.util;

import it.eurotn.panjea.ordini.domain.RigaOrdine;

import java.io.Serializable;

/**
 * @author fattazzo
 */
public abstract class RigaOrdineDTO implements Serializable {

	private static final long serialVersionUID = 4247025688726131690L;

	private Integer id;
	private String descrizione;

	private boolean rigaCollegata;

	private Integer livello;

	private boolean evasioneForzata;

	/**
	 * Default constructor.
	 */
	public RigaOrdineDTO() {

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
		RigaOrdineDTO other = (RigaOrdineDTO) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the livello
	 */
	public Integer getLivello() {
		return livello;
	}

	/**
	 * 
	 * @return istanza di riga ordine
	 */
	public abstract RigaOrdine getRigaOrdine();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * @return the evasioneForzata
	 */
	public boolean isEvasioneForzata() {
		return evasioneForzata;
	}

	/**
	 * @return the rigaCollegata
	 */
	public boolean isRigaCollegata() {
		return rigaCollegata;
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param evasioneForzata
	 *            the evasioneForzata to set
	 */
	public void setEvasioneForzata(boolean evasioneForzata) {
		this.evasioneForzata = evasioneForzata;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param livello
	 *            the livello to set
	 */
	public void setLivello(Integer livello) {
		this.livello = livello;
	}

	/**
	 * @param rigaCollegata
	 *            the rigaCollegata to set
	 */
	public void setRigaCollegata(boolean rigaCollegata) {
		this.rigaCollegata = rigaCollegata;
	}
}
