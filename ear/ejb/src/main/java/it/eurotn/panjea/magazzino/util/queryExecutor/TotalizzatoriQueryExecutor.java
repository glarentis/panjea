package it.eurotn.panjea.magazzino.util.queryExecutor;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateful(name = "Panjea.TotalizzatoriQueryExecutor")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TotalizzatoriQueryExecutor")
public class TotalizzatoriQueryExecutor implements ITotalizzatoriQueryExecutor {

    @EJB
    protected PanjeaDAO panjeaDAO;

    private String queryString;

    private IAreaDocumento areaDocumento;

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> execute() {
        Query query = panjeaDAO.prepareNamedQueryWithoutFlush(queryString);
        query.setParameter("paramArea", areaDocumento);
        return query.getResultList();
    }

    /**
     * @return the areaDocumento
     */
    @Override
    public IAreaDocumento getAreaDocumento() {
        return areaDocumento;
    }

    /**
     * @param areaDocumento
     *            the areaDocumento to set
     */
    @Override
    public void setAreaDocumento(IAreaDocumento areaDocumento) {
        this.areaDocumento = areaDocumento;
    }

    /**
     * @param queryString
     *            the queryString to set
     */
    @Override
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

}
