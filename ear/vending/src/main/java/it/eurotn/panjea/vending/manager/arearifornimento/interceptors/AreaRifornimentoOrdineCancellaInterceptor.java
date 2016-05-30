package it.eurotn.panjea.vending.manager.arearifornimento.interceptors;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

import it.eurotn.panjea.ordini.domain.documento.AreaOrdine;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

public class AreaRifornimentoOrdineCancellaInterceptor {

    @EJB
    private PanjeaDAO panjeaDAO;

    /**
     * PointCut.
     *
     * @param ctx
     *            contesto della chiamata
     * @return return per il metodo
     * @throws Exception
     *             eventuale exception
     */
    @AroundInvoke
    public Object cancellaAreaRifornimento(InvocationContext ctx) throws Exception {

        AreaOrdine areaOrdine = (AreaOrdine) ctx.getParameters()[0];

        Query query = panjeaDAO.prepareQuery("delete AreaRifornimento ar where ar.areaOrdine.id = :paramAO");
        query.setParameter("paramAO", areaOrdine.getId());
        panjeaDAO.executeQuery(query);

        return ctx.proceed();
    }
}
