package it.eurotn.panjea.iva.util;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateful(name = "Panjea.ImponibiliIvaQueryExecutor")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ImponibiliIvaQueryExecutor")
public class ImponibiliIvaQueryExecutor implements IImponibiliIvaQueryExecutor {

	private String queryString;

	/**
	 * @uml.property name="areaDocumento"
	 * @uml.associationEnd
	 */
	private IAreaDocumento areaDocumento;

	/**
	 * @uml.property name="panjeaDAO"
	 * @uml.associationEnd
	 */
	@EJB
	private PanjeaDAO panjeaDAO;

	@Override
	public List<Object[]> execute() {
		Query query = panjeaDAO.prepareNamedQuery(queryString);
		query.setParameter("paramArea", areaDocumento);
		@SuppressWarnings("unchecked")
		List<Object[]> aggregatiIva = query.getResultList();

		return aggregatiIva;
	}

	@Override
	public void setAreaDocumento(IAreaDocumento areaDocumento) {
		this.areaDocumento = areaDocumento;
	}

	@Override
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
}
