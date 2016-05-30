package it.eurotn.panjea.magazzino.interceptor;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.ClientePotenziale;
import it.eurotn.panjea.anagrafica.domain.Entita;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.util.EntitaDocumento;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoEntita;
import it.eurotn.panjea.dms.manager.allegati.attributi.AttributoAllegatoPanjea;
import it.eurotn.panjea.dms.manager.interfaces.DMSAttributiManager;
import it.eurotn.panjea.dms.mdb.AttributoAggiornamentoDTO.TipoAttributo;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseSediEntitaSqlBuilder;
import it.eurotn.panjea.magazzino.interceptor.sqlbuilder.DataWarehouseSediEntitaSqlBuilder.TipoFiltro;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
public class DataWarehouseSediEntitaEntitaAggiornaInterceptor extends DataWarehouseSediEntitaAggiornaInterceptor {

    private static final Logger LOGGER = Logger.getLogger(DataWarehouseSediEntitaEntitaAggiornaInterceptor.class);

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private DMSAttributiManager dmsAttributiManager;

    @Override
    @AroundInvoke
    public Object aggiornaSedeAnagrafica(InvocationContext ctx) throws Exception {
        LOGGER.debug("--> Enter aggiornaSedeAnagrafica");
        Entita entita = (Entita) ctx.proceed();
        SedeAnagrafica sedeAnagrafica = entita.getAnagrafica().getSedeAnagrafica();

        if (entita instanceof ClientePotenziale) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("--> non aggiorno la sede del cliente potenziale nel DW dw_sedientita");
            }
            return entita;
        }

        DataWarehouseSediEntitaSqlBuilder sqlBuilder = new DataWarehouseSediEntitaSqlBuilder(
                TipoFiltro.SEDE_ANAGRAFICA_ID);

        javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sqlBuilder.getSqlDelete());
        query.setParameter(DataWarehouseSediEntitaSqlBuilder.PARAM_FILTRO_SEDE_ANAGRAFICA_ID, sedeAnagrafica.getId());
        panjeaDAO.executeQuery(query);

        query = panjeaDAO.getEntityManager().createNativeQuery(sqlBuilder.getSql());
        query.setParameter(DataWarehouseSediEntitaSqlBuilder.PARAM_FILTRO_SEDE_ANAGRAFICA_ID, sedeAnagrafica.getId());
        panjeaDAO.executeQuery(query);

        EntitaDocumento entitaDocumento = new EntitaDocumento();
        entitaDocumento.setTipoEntita(entita.getTipo());
        entitaDocumento.setId(entita.getId());
        entitaDocumento.setCodice(entita.getCodice());
        entitaDocumento.setDescrizione(entita.getAnagrafica().getDenominazione());

        AttributoAllegatoPanjea att = new AttributoAllegatoEntita(entitaDocumento, getCodiceAzienda());
        /// Aggiorno i dati nel dms
        dmsAttributiManager.aggiornaAttributo(TipoAttributo.ENTITA, att);
        LOGGER.debug("--> Exit aggiornaSedeAnagrafica");
        return entita;
    }

    /**
     *
     * @return codiceAzienda
     */
    private String getCodiceAzienda() {
        return ((JecPrincipal) context.getCallerPrincipal()).getCodiceAzienda();
    }
}