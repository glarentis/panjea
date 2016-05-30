package it.eurotn.panjea.ordini.util;

import it.eurotn.panjea.ordini.domain.RigaNota;
import it.eurotn.panjea.ordini.domain.RigaOrdine;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 */
public class RigaNotaDTO extends RigaOrdineDTO {

	private static final long serialVersionUID = 5645675334748882552L;

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
	public String getNota() {
		return nota;
	}

	@Override
	public RigaOrdine getRigaOrdine() {
		RigaOrdine riga = new RigaNota();
		PanjeaEJBUtil.copyProperties(riga, this);
		return riga;
	}

	/**
	 * @return the rigaAutomatica
	 */
	public boolean isRigaAutomatica() {
		return rigaAutomatica;
	}

	/**
	 * @param nota
	 *            the nota to set
	 * @uml.property name="nota"
	 */
	public void setNota(String nota) {
		this.nota = nota;
	}

	/**
	 * @param rigaAutomatica
	 *            the rigaAutomatica to set
	 */
	public void setRigaAutomatica(boolean rigaAutomatica) {
		this.rigaAutomatica = rigaAutomatica;
	}
}
