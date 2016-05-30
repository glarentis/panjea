/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain.lite;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author adriano
 * @version 1.0, 05/giu/07
 */
@Entity
@Audited
@DiscriminatorValue("V")
public class VettoreLite extends EntitaLite {

	public static final String TIPO = "V";

	private static final long serialVersionUID = 7616755690832500724L;

	/**
	 * @uml.property name="ordine"
	 */
	private static int ordine = 4;

	/**
	 * @uml.property name="numeroIscrizioneAlbo"
	 */
	@Column(length = 20)
	private String numeroIscrizioneAlbo;

	/**
	 * @return the numeroIscrizioneAlbo
	 * @uml.property name="numeroIscrizioneAlbo"
	 */
	public String getNumeroIscrizioneAlbo() {
		return numeroIscrizioneAlbo;
	}

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

	/**
	 * @param numeroIscrizioneAlbo
	 *            the numeroIscrizioneAlbo to set
	 * @uml.property name="numeroIscrizioneAlbo"
	 */
	public void setNumeroIscrizioneAlbo(String numeroIscrizioneAlbo) {
		this.numeroIscrizioneAlbo = numeroIscrizioneAlbo;
	}
}
