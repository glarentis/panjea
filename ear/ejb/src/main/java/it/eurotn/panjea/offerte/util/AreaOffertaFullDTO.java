/**
 *
 */
package it.eurotn.panjea.offerte.util;

import it.eurotn.locking.IDefProperty;
import it.eurotn.panjea.offerte.domain.AreaOfferta;
import it.eurotn.panjea.offerte.domain.RigaOfferta;

import java.io.Serializable;
import java.util.List;

/**
 * @author Leonardo
 */
public class AreaOffertaFullDTO implements Serializable, IDefProperty {

	private static final long serialVersionUID = -8200090507570103516L;

	private AreaOfferta areaOfferta = null;

	private List<RigaOfferta> righeOfferta = null;

	{
		areaOfferta = new AreaOfferta();
	}

	/**
	 * Costruttore.
	 */
	public AreaOffertaFullDTO() {
		super();
	}

	@Override
	public boolean equals(Object obj) {
		if (areaOfferta != null && obj instanceof AreaOffertaFullDTO) {
			return areaOfferta.equals(((AreaOffertaFullDTO) obj).getAreaOfferta());
		}
		return false;
	}

	/**
	 * @return the areaOfferta
	 */
	public AreaOfferta getAreaOfferta() {
		return areaOfferta;
	}

	@Override
	public String getDomainClassName() {
		return areaOfferta.getDomainClassName();
	}

	@Override
	public Integer getId() {
		return areaOfferta.getId();
	}

	/**
	 * @return the righeOfferta
	 */
	public List<RigaOfferta> getRigheOfferta() {
		return righeOfferta;
	}

	@Override
	public Integer getVersion() {
		return areaOfferta.getVersion();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((areaOfferta == null) ? 0 : areaOfferta.hashCode());
		return result;
	}

	@Override
	public boolean isNew() {
		return areaOfferta.isNew();
	}

	/**
	 * @param areaOfferta
	 *            the areaOfferta to set
	 */
	public void setAreaOfferta(AreaOfferta areaOfferta) {
		this.areaOfferta = areaOfferta;
	}

	/**
	 * @param righeOfferta
	 *            the righeOfferta to set
	 */
	public void setRigheOfferta(List<RigaOfferta> righeOfferta) {
		this.righeOfferta = righeOfferta;
	}

}
