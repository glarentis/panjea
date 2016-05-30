package it.eurotn.panjea.vending.manager.arearifornimento.interceptors;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoFullDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.vending.domain.documento.AreaRifornimento;

public class AreaRifornimentoMagazzinoCaricaInterceptor {

    private static final Logger LOGGER = Logger.getLogger(AreaRifornimentoMagazzinoCaricaInterceptor.class);

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
    public Object caricaAreaRifornimentoMagazzino(InvocationContext ctx) throws Exception {
        AreaMagazzinoFullDTO areaMagazzinoFullDTO = (AreaMagazzinoFullDTO) ctx.proceed();
        areaMagazzinoFullDTO
                .setAreaRifornimento(caricaByAreaMagazzino(areaMagazzinoFullDTO.getAreaMagazzino().getId()));

        return areaMagazzinoFullDTO;
    }

    /**
     * Carica l'area rifornimento in base all'area Magazzino.
     *
     * @param idAreaMagazzino
     *            id area Magazzino
     * @return area rifornimento
     */
    public AreaRifornimento caricaByAreaMagazzino(Integer idAreaMagazzino) {
        LOGGER.debug("--> Enter caricaByAreaMagazzino");
        AreaRifornimento areaRifornimento = null;

        Integer idAreaRifornimento = null;

        Query query = panjeaDAO
                .prepareQuery("select ar.id from AreaRifornimento ar where ar.areaMagazzino.id = :paramAM");
        query.setParameter("paramAM", idAreaMagazzino);

        try {
            idAreaRifornimento = (Integer) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            // non è un errore il fatto che non esista una area rifornimento per un'area Magazzino
            idAreaRifornimento = null;
        } catch (Exception e) {
            LOGGER.error(
                    "--> errore durante il caricamento dell'area rifornimento per l'area Magazzino " + idAreaMagazzino,
                    e);
            throw new GenericException(
                    "errore durante il caricamento dell'area rifornimento per l'area Magazzino " + idAreaMagazzino, e);
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
                Hibernate.initialize(areaRifornimento.getAreaMagazzino().getDepositoOrigine());
            } catch (Exception e) {
                LOGGER.error("--> errore durante il caricamento dell'area rifornimento " + idAreaRifornimento, e);
                throw new GenericException("errore durante il caricamento dell'area rifornimento " + idAreaRifornimento,
                        e);
            }
        }
        return areaRifornimento;
    }
}
