package it.eurotn.panjea.contabilita.util;

import it.eurotn.panjea.contabilita.domain.GiornaleIva;

import java.io.Serializable;

/**
 * Contiene i dati relativi ad una liquidazione: Registro riepilogativo se è stampata a giornale AreacontabileDTO se ha
 * un documento collegato.
 * 
 * @author giangi
 */
public class RegistroLiquidazioneDTO implements Serializable {
	private static final long serialVersionUID = -533980195632022535L;

	/**
	 * @uml.property name="giornaleIva"
	 * @uml.associationEnd
	 */
	private GiornaleIva giornaleIva;
	/**
	 * @uml.property name="areaContabileDTO"
	 * @uml.associationEnd
	 */
	private AreaContabileDTO areaContabileDTO;

	/**
	 * Costruttore.
	 * 
	 */
	public RegistroLiquidazioneDTO() {
		super();
		giornaleIva = new GiornaleIva();
		areaContabileDTO = new AreaContabileDTO();
	}

	/**
	 * @return areaContabileDTO
	 * @uml.property name="areaContabileDTO"
	 */
	public AreaContabileDTO getAreaContabileDTO() {
		return areaContabileDTO;
	}

	/**
	 * @return giornaleIva
	 * @uml.property name="giornaleIva"
	 */
	public GiornaleIva getGiornaleIva() {
		return giornaleIva;
	}

	/**
	 * @param areaContabileDTO
	 *            the areaContabileDTO to set
	 * @uml.property name="areaContabileDTO"
	 */
	public void setAreaContabileDTO(AreaContabileDTO areaContabileDTO) {
		this.areaContabileDTO = areaContabileDTO;
	}

	/**
	 * @param giornaleIva
	 *            the giornaleIva to set
	 * @uml.property name="giornaleIva"
	 */
	public void setGiornaleIva(GiornaleIva giornaleIva) {
		this.giornaleIva = giornaleIva;
	}

}
