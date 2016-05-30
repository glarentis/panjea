package it.eurotn.panjea.audit.manager;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.jboss.resource.adapter.jdbc.WrappedConnection;
import org.jboss.resource.adapter.jdbc.WrapperDataSource;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.audit.manager.interfaces.AuditManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.util.SqlExecuter;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.AuditManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AuditManager")
public class AuditManagerBean implements AuditManager {

    private static Logger logger = Logger.getLogger(AuditManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext sessionContext;

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void cancellaAuditPrecedente(Date data) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<String> auditTables = getAuditTables();

        SqlExecuter executer = new SqlExecuter();

        // cancello da tutte le tabelle di audit
        for (String tableName : auditTables) {
            StringBuilder sb = new StringBuilder(150);
            sb.append("delete at.* from ");
            sb.append(tableName);
            sb.append(" at inner join revinfo ri on at.REV = ri.id where FROM_UNIXTIME(ri.timestamp/1000) < ");
            sb.append(PanjeaEJBUtil.addQuote(dateFormat.format(data)));
            executer.setSql(sb.toString());
            ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
        }

        // cancello le rev inf
        executer.setSql("delete from revinfo where FROM_UNIXTIME(timestamp/1000) < "
                + PanjeaEJBUtil.addQuote(dateFormat.format(data)));
        ((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);

    }

    @Override
    public Integer caricaNumeroRevInf() {
        logger.debug("--> Enter caricaRevInf");

        Integer result = null;
        Query query = panjeaDAO.prepareQuery("select count(r.id) from RevInf r");

        try {
            @SuppressWarnings("unchecked")
            List<Long> resultQuery = panjeaDAO.getResultList(query);
            result = resultQuery.get(0).intValue();
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento delle revinf", e);
            throw new RuntimeException("Errore durante il caricamento delle revinf", e);
        }
        logger.debug("--> Exit caricaRevInf");
        return result;
    }

    /**
     * Restituisce tutti i nomi delle tabelle di audit.
     *
     * @return nomi tabelle
     */
    @SuppressWarnings("unchecked")
    private List<String> getAuditTables() {
        logger.debug("--> Enter getAuditTables");

        // ricavo il nome del DB per poter cercare le tabelle di audit
        String database = "";
        WrapperDataSource dataSource = (WrapperDataSource) sessionContext.lookup(panjeaDAO.getJndiCurrentDataSource());
        try {
            database = ((WrappedConnection) dataSource.getConnection()).getCatalog();
        } catch (SQLException e) {
            logger.error("--> errore nel trovare il nome del database dal data source", e);
            throw new RuntimeException("errore nel trovare il nome del database dal data source", e);
        }

        List<String> tablesName = new ArrayList<String>();

        Query query = panjeaDAO.getEntityManager().createNativeQuery(
                "SELECT table_name FROM information_schema.tables WHERE table_type = 'base table' AND table_schema='"
                        + database + "' and table_name like '%_aud'");

        try {
            tablesName = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("-->errore durante il caricamento delle tabelle di audit.", e);
            throw new RuntimeException("-->errore durante il caricamento delle tabelle di audit.", e);
        }

        logger.debug("--> Exit getAuditTables");
        return tablesName;
    }

}
