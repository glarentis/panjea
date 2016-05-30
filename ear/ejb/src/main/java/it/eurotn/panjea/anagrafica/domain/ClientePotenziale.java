/**
 * 
 */
package it.eurotn.panjea.anagrafica.domain;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author Leonardo
 * 
 */
@Entity
@Audited
@DiscriminatorValue("CP")
public class ClientePotenziale extends Cliente {

	private static final long serialVersionUID = 7309134453229902356L;

	@Override
	public TipoEntita getTipo() {
		return null;
	}

}
