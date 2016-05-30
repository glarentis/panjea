package it.eurotn.panjea.preventivi.util;

import it.eurotn.panjea.preventivi.domain.RigaNota;
import it.eurotn.panjea.preventivi.domain.RigaPreventivo;
import it.eurotn.panjea.preventivi.util.interfaces.IRigaNotaDTO;
import it.eurotn.util.PanjeaEJBUtil;

public class RigaNotaDTO extends RigaPreventivoDTO implements IRigaNotaDTO {

	private static final long serialVersionUID = 1L;

	/**
	 * @uml.property name="nota"
	 */
	private String nota;

	private boolean rigaAutomatica;

	/**
	 * Csotruttore.
	 */
	public RigaNotaDTO() {
		super();
	}

	/**
	 * @return the nota
	 * @uml.property name="nota"
	 */
	@Override
	public String getNota() {
		return nota;
	}

	@Override
	public RigaPreventivo getRigaPreventivo() {
		RigaPreventivo riga = new RigaNota();
		PanjeaEJBUtil.copyProperties(riga, this);
		return riga;
	}

	/**
	 * @return the rigaAutomatica
	 */
	@Override
	public boolean isRigaAutomatica() {
		return rigaAutomatica;
	}

	/**
	 * @param nota
	 *            the nota to set
	 * @uml.property name="nota"
	 */
	@Override
	public void setNota(String nota) {
		this.nota = nota;
	}

	/**
	 * @param rigaAutomatica
	 *            the rigaAutomatica to set
	 */
	@Override
	public void setRigaAutomatica(boolean rigaAutomatica) {
		this.rigaAutomatica = rigaAutomatica;
	}

}
