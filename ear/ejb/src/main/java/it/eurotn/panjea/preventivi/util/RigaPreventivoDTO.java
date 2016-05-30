package it.eurotn.panjea.preventivi.util;

import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.util.interfaces.IRigaDTO;

public abstract class RigaPreventivoDTO implements IRigaDTO {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String descrizione;

	private Integer livello;

	/**
	 * Default constructor.
	 */
	public RigaPreventivoDTO() {

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof RigaPreventivoDTO)) {
			return false;
		}

		RigaPreventivoDTO other = (RigaPreventivoDTO) obj;
		if (id == null) {
			return other.id == null;
		}

		return id.equals(other.id);
	}

	/**
	 * @return the descrizione
	 */
	@Override
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @return the id
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * @return the livello
	 */
	@Override
	public Integer getLivello() {
		return livello;
	}

	/**
	 * 
	 * @return istanza di riga ordine
	 */
	public abstract RigaPreventivo getRigaPreventivo();

	@Override
	public int hashCode() {
		return (id == null) ? 0 : id.intValue();
	}

	/**
	 * @param descrizione
	 *            the descrizione to set
	 */
	@Override
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param livello
	 *            the livello to set
	 */
	@Override
	public void setLivello(Integer livello) {
		this.livello = livello;
	}
}
