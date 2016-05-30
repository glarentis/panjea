/**
 * 
 */
package it.eurotn.panjea.anagrafica.util;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;

import java.io.Serializable;

/**
 * DTO Object per classe {@link Azienda} e {@link Anagrafica} associata.
 * 
 * @author adriano
 * @version 1.0, 17/dic/07
 */
public class AziendaAnagraficaDTO implements Serializable, IDefProperty {

	private static final long serialVersionUID = 6686398531192254572L;

	/**
	 * @uml.property name="azienda"
	 * @uml.associationEnd
	 */
	private Azienda azienda;

	/**
	 * @uml.property name="sedeAzienda"
	 * @uml.associationEnd
	 */
	private SedeAzienda sedeAzienda;

	/**
	 * Costruttore.
	 * 
	 */
	public AziendaAnagraficaDTO() {
		this.azienda = new Azienda();
		this.sedeAzienda = new SedeAzienda();
	}

	/**
	 * @return Returns the azienda.
	 * @uml.property name="azienda"
	 */
	public Azienda getAzienda() {
		return azienda;
	}

	@Override
	public String getDomainClassName() {
		return azienda.getDomainClassName();
	}

	@Override
	public Integer getId() {
		return azienda.getId();
	}

	/**
	 * @return Returns the sedeAzienda.
	 * @uml.property name="sedeAzienda"
	 */
	public SedeAzienda getSedeAzienda() {
		return sedeAzienda;
	}

	@Override
	public Integer getVersion() {
		return azienda.getVersion();
	}

	@Override
	public boolean isNew() {
		return sedeAzienda.isNew();
	}

	/**
	 * @param azienda
	 *            The azienda to set.
	 * @uml.property name="azienda"
	 */
	public void setAzienda(Azienda azienda) {
		this.azienda = azienda;
	}

	/**
	 * @param sedeAzienda
	 *            The sedeAzienda to set.
	 * @uml.property name="sedeAzienda"
	 */
	public void setSedeAzienda(SedeAzienda sedeAzienda) {
		this.sedeAzienda = sedeAzienda;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("AziendaAnagraficaDTO[");
		buffer.append(" anagrafica = ").append(azienda);
		buffer.append(" azienda = ").append(sedeAzienda);
		buffer.append("]");
		return buffer.toString();
	}
}
