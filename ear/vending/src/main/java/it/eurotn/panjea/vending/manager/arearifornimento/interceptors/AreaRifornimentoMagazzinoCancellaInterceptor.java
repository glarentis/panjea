package it.eurotn.panjea.vending.manager.arearifornimento.interceptors;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

import it.eurotn.panjea.magazzino.domain.documento.AreaMagazzino;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

public class AreaRifornimentoMagazzinoCancellaInterceptor {

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

        AreaMagazzino areaMagazzino = (AreaMagazzino) ctx.getParameters()[0];

        Query query = panjeaDAO.prepareQuery("delete AreaRifornimento ar where ar.areaMagazzino.id = :paramAM");
        query.setParameter("paramAM", areaMagazzino.getId());
        panjeaDAO.executeQuery(query);

        return ctx.proceed();
    }
}
