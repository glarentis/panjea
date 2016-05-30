package it.eurotn.panjea.tesoreria.rich.editors.acconto;

import it.eurotn.panjea.tesoreria.domain.AreaAcconto;

import java.io.Serializable;

/**
 * Classe wrapper di {@link AreaAcconto}.
 * 
 * @author fattazzo
 * 
 */
public class AreaAccontoWrapper implements Serializable {

	private static final long serialVersionUID = 8164198707525604841L;

	private AreaAcconto areaAcconto;

	/**
	 * Costruttore.
	 */
	public AreaAccontoWrapper() {
		super();
		this.areaAcconto = new AreaAcconto();
	}

	/**
	 * Costruttore.
	 * 
	 * @param areaAcconto
	 *            area acconto di riferimento
	 */
	public AreaAccontoWrapper(final AreaAcconto areaAcconto) {
		super();
		this.areaAcconto = areaAcconto;
	}

	/**
	 * @return the areaAcconto
	 */
	public AreaAcconto getAreaAcconto() {
		return areaAcconto;
	}

	/**
	 * @param areaAcconto
	 *            the areaAcconto to set
	 */
	public void setAreaAcconto(AreaAcconto areaAcconto) {
		this.areaAcconto = areaAcconto;
	}
}
