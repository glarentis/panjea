package it.eurotn.panjea.magazzino.interceptor;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoArticolo;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoPanjea;
import it.eurotn.panjea.dms.manager.interfaces.DMSAttributiManager;
import it.eurotn.panjea.dms.mdb.AttributoAggiornamentoDTO.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.exception.DataWarehouseException;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseArticoliSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseArticoliSqlBuilder.TipoFiltro;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class DataWarehouseArticoloAggiornaInterceptor {

    private static final Logger LOGGER = Logger.getLogger(DataWarehouseArticoloAggiornaInterceptor.class);

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private DMSAttributiManager dmsAttributiManager;

    /**
     *
     * @param ctx
     *            context della chiamata
     * @return risultato del metodo intecettato
     * @throws DataWarehouseException
     *             exception generica datawarehouse
     * @throws Exception
     *             eccezione generica
     */
    @AroundInvoke
    public Object aggiornaArticolo(InvocationContext ctx) throws Exception { // NOSONAR
        LOGGER.debug("--> Enter aggiornaArticolo");
        // Recuper il codice salvato sul database per sapere se è cambiato ed in caso
        // cambiarlo nel dms
        Query queryOldValue = panjeaDAO.prepareNamedQuery("Articolo.caricaCodiceDescrizioneById");
        queryOldValue.setParameter("idArticolo", ((Articolo) ctx.getParameters()[0]).getId());
        Object[] oldData;
        try {
            oldData = (Object[]) panjeaDAO.getSingleResult(queryOldValue);
        } catch (ObjectNotFoundException ex1) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> Articolo non trovato durante l'aggiornamento dms. Non dovrebbe succedere", ex1);
            }
            // nuovo articolo.
            oldData = new Object[] { "", "" };
        } catch (Exception e) {
            LOGGER.error("-->errore nel sincronizzare l'articolo", e);
            throw new DataWarehouseException("-->errore nel sincronizzare l'articolo", e);
        }

        @SuppressWarnings("checkstyle:variabledeclarationusagedistance") // utilizzato dopo.
        String oldCodice = (String) oldData[0];
        @SuppressWarnings("checkstyle:variabledeclarationusagedistance") // utilizzato dopo.
        String oldDescrizione = (String) oldData[1];
        Articolo result;
        try {
            result = (Articolo) ctx.proceed();
        } catch (Exception e) {
            LOGGER.error("-->errore nel proceed dell'interceptor ", e);
            throw new DataWarehouseException("-->errore nel proceed dell'interceptor ", e);
        }

        // Sincronizzo le proprietà del dms legate all'articolo
        if (!result.getCodice().equals(oldCodice) || !result.getDescrizione().equals(oldDescrizione)) {
            AttributoAllegatoPanjea att = new AttributoAllegatoArticolo(result.getCodice(), result.getDescrizione(),
                    result.getId(), getCodiceAzienda());
            dmsAttributiManager.aggiornaAttributo(TipoAttributo.ARTICOLO, att);
        }

        DataWarehouseArticoliSqlBuilder sqlBuilder = new DataWarehouseArticoliSqlBuilder(TipoFiltro.ARTICOLO_ID);
        javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sqlBuilder.getSqlDelete());
        query.setParameter(DataWarehouseArticoliSqlBuilder.PARAM_FILTRO_ARTICOLO_ID, result.getId());

        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new DataWarehouseException(e);
        }
        query = panjeaDAO.getEntityManager().createNativeQuery(sqlBuilder.getSqlInsert());
        query.setParameter(DataWarehouseArticoliSqlBuilder.PARAM_FILTRO_ARTICOLO_ID, result.getId());

        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new DataWarehouseException(e);
        }
        LOGGER.debug("--> Exit aggiornaArticolo");
        return result;
    }

    /**
     *
     * @return codiceAzienda
     */
    private String getCodiceAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }

}
