/**
 *
 */
package it.eurotn.panjea.tesoreria.domain;

import it.eurotn.panjea.partite.domain.AreaPartite;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;

/**
 * Distingue tutte le classi inerenti la Tesoreria.
 *
 * @author vittorio
 * @version 1.0, 26/nov/2009
 *
 */

@Entity
@Audited
@DiscriminatorValue("TE")
@NamedQueries({
	@NamedQuery(name = "AreaTesoreria.ricercaByDocumento", query = "select a from AreaTesoreria a inner join a.documento d where d.id = :paramIdDocumento"),
	@NamedQuery(name = "AreaTesoreria.checkByDocumento", query = "select a from AreaTesoreria a where a.documento.id  = :paramIdDocumento"),
	@NamedQuery(name = "AreaTesoreria.caricaByDocumento", query = "select a from AreaTesoreria a where a.documento.id  = :paramIdDocumento") })
public class AreaTesoreria extends AreaPartite {
	private static final long serialVersionUID = -8652559365475865732L;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (isNew()) {
			return false;
		}
		if (!(obj instanceof AreaTesoreria)) {
			return false;
		} else {
			return getId().equals(((AreaTesoreria) obj).getId());
		}
	}

	@Override
	public Map<String, Object> fillVariables() {
		return new HashMap<String, Object>();
	}
}
