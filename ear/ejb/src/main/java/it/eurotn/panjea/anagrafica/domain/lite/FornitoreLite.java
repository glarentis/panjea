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
@DiscriminatorValue("F")
public class FornitoreLite extends EntitaLite {

	public static final String TIPO = "F";

	private static final long serialVersionUID = 4430375320180210411L;

	private static int ordine = 2;

	/**
	 * @return ordine
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
