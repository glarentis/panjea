/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain.lite;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author adriano
 * @version 1.0, 05/giu/07
 */
@Entity
@Audited
@DiscriminatorValue("C")
public class ClienteLite extends EntitaLite {

	public static final String TIPO = "C";

	private static final long serialVersionUID = 1L;

	/**
	 * @uml.property name="ordine"
	 */
	private static int ordine = 1;

	/**
	 * @return ordine
	 * @uml.property name="ordine"
	 */
	@Override
	public int getOrdine() {
		return ordine;
	}

	@Override
	public String getTipo() {
		return TIPO;
	}

}
