package it.eurotn.panjea.vending.manager.arearifornimento.interceptors;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.ordini.util.AreaOrdineFullDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;

public class AreaRifornimentoOrdineCaricaInterceptor {

    private static final Logger LOGGER = Logger.getLogger(AreaRifornimentoOrdineCaricaInterceptor.class);

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
    public Object caricaAreaRifornimentoOrdine(InvocationContext ctx) throws Exception {
        AreaOrdineFullDTO areaOrdineFullDTO = (AreaOrdineFullDTO) ctx.proceed();
        areaOrdineFullDTO.setAreaRifornimento(caricaByAreaOrdine(areaOrdineFullDTO.getAreaOrdine().getId()));

        return areaOrdineFullDTO;
    }

    /**
     * Carica l'area rifornimento in base all'area Ordine.
     *
     * @param idAreaOrdine
     *            id area Ordine
     * @return area rifornimento
     */
    public AreaRifornimento caricaByAreaOrdine(Integer idAreaOrdine) {
        LOGGER.debug("--> Enter caricaByAreaOrdine");
        AreaRifornimento areaRifornimento = null;

        Integer idAreaRifornimento = null;

        Query query = panjeaDAO.prepareQuery("select ar.id from AreaRifornimento ar where ar.areaOrdine.id = :paramAM");
        query.setParameter("paramAM", idAreaOrdine);

        try {
            idAreaRifornimento = (Integer) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            // non è un errore il fatto che non esista una area rifornimento per un'area Ordine
            idAreaRifornimento = null;
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento dell'area rifornimento per l'area Ordine " + idAreaOrdine,
                    e);
            throw new GenericException(
                    "errore durante il caricamento dell'area rifornimento per l'area Ordine " + idAreaOrdine, e);
        }

        if (idAreaRifornimento != null) {
            try {
                // Non riesco a iniettare il manager dell'area rifornimento nell'interceptor ( forse
                // perchè estende il
                // crud manager e l'interceptor non è un ejb ) quindi faccio una load e inizializzo
                // tutte le proprietà
                // lazy

                areaRifornimento = panjeaDAO.load(AreaRifornimento.class, idAreaRifornimento);
                Hibernate.initialize(areaRifornimento.getInstallazione());
                Hibernate.initialize(areaRifornimento.getInstallazione().getDeposito());
                Hibernate.initialize(
                        areaRifornimento.getInstallazione().getDeposito().getSedeEntita().getEntita().getAnagrafica());
                Hibernate.initialize(areaRifornimento.getInstallazione().getArticolo());
                Hibernate.initialize(areaRifornimento.getOperatore());
                Hibernate.initialize(areaRifornimento.getAreaOrdine().getDepositoOrigine());
            } catch (Exception e) {
                LOGGER.error("--> errore durante il caricamento dell'area rifornimento " + idAreaRifornimento, e);
                throw new GenericException("errore durante il caricamento dell'area rifornimento " + idAreaRifornimento,
                        e);
            }
        }
        return areaRifornimento;
    }
}
