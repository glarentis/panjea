package it.eurotn.panjea.tesoreria.domain;

import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

/**
 * 
 * Settings tesoreria.
 * 
 * @author giangi
 * @version 1.0, 27/gen/2014
 * 
 */
@Entity
@Audited
@NamedQueries({ @NamedQuery(name = "TesoreriaSettings.caricaAll", query = "from TesoreriaSettings", hints = {
		@QueryHint(name = "org.hibernate.cacheable", value = "true"),
		@QueryHint(name = "org.hibernate.cacheRegion", value = "tesoreriaSettings") }) })
@Table(name = "part_settings")
public class TesoreriaSettings extends EntityBase {
	private static final long serialVersionUID = -3844601307015029172L;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<TipoDocumento> tipiDocumentoFido;

	@Transient
	private boolean letturaAssegni;

	/**
	 * @return Returns the tipiDocumentoFido.
	 */
	public List<TipoDocumento> getTipiDocumentoFido() {
		return tipiDocumentoFido;
	}

	/**
	 * @return Returns the letturaAssegni.
	 */
	public boolean isLetturaAssegni() {
		return letturaAssegni;
	}

	/**
	 * @param letturaAssegni
	 *            The letturaAssegni to set.
	 */
	public void setLetturaAssegni(boolean letturaAssegni) {
		this.letturaAssegni = letturaAssegni;
	}

	/**
	 * @param tipiDocumentoFido
	 *            The tipiDocumentoFido to set.
	 */
	public void setTipiDocumentoFido(List<TipoDocumento> tipiDocumentoFido) {
		this.tipiDocumentoFido = tipiDocumentoFido;
	}
}
