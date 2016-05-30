/**
 * 
 */
package it.eurotn.panjea.tesoreria.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * Classe astratta capostipite per le operazioni di chiusura delle rate.
 * 
 * @author vittorio
 * @version 1.0, 26/nov/2009
 * 
 */
@Entity
@Audited
@DiscriminatorValue("CH")
public class AreaChiusure extends AreaTesoreria {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7918851923260495738L;

}
