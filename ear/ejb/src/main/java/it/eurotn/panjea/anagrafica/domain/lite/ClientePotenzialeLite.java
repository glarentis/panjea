/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain.lite;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Leonardo
 * 
 */
@Entity
@DiscriminatorValue("CP")
public class ClientePotenzialeLite extends ClienteLite {

	public static final String TIPO = "CP";

	private static final long serialVersionUID = -8225776177237710371L;

	@Override
	public String getTipo() {
		return TIPO;
	}

}
