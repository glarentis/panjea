package it.eurotn.panjea.auvend.service;

import it.eurotn.panjea.auvend.service.interfaces.AuVendExtDAO;
import it.eurotn.security.JecPrincipal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.Remove;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.resource.connectionmanager.ManagedConnectionPool;

@Stateless(name = "Panjea.AuVendExtDAO")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AuVendExtDAO")
public class AuVendExtDAOBean implements AuVendExtDAO {
	private static final long serialVersionUID = -661046697063547410L;

	private static Logger logger = Logger.getLogger(AuVendExtDAOBean.class);

	private Connection connection;

	@Resource
	private SessionContext context;

	@Override
	public void callProcedure(String procedure, List<Object> parametri) {
		logger.debug("--> Enter callProcedure per la procedure " + procedure);
		initConnection();
		try {
			CallableStatement callable = connection.prepareCall(procedure);
			for (int i = 0; i < parametri.size(); i++) {
				Object parametro = parametri.get(i);
				logger.debug("--> parametro da passare " + parametro);
				if (parametro instanceof Date) {
					logger.debug("--> parametro " + i + " : " + parametro);
					callable.setDate(i + 1, new java.sql.Date(((Date) parametro).getTime()));
				}
				if (parametro instanceof String) {
					logger.debug("--> parametro " + i + " : " + parametro);
					callable.setString(i + 1, (String) parametro);
				}
				if (parametro instanceof Integer) {
					logger.debug("--> parametro " + i + " : " + parametro);
					callable.setInt(i + 1, (Integer) parametro);
				}
			}
			callable.execute();
		} catch (SQLException e) {
			logger.error("--> errore  nel chiamare la procedure " + procedure, e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit callProcedure");
	}

	@Override
	public ResultSet callQuery(String procedure, List<Object> parametri) {
		logger.debug("--> Enter callQuery");
		initConnection();
		try {
			CallableStatement callable = connection.prepareCall(procedure);
			for (int i = 0; i < parametri.size(); i++) {
				Object parametro = parametri.get(i);
				logger.debug("--> parametro da passare " + parametro);
				if (parametro instanceof Date) {
					logger.debug("--> parametro " + i + " : " + parametro);
					callable.setDate(i + 1, new java.sql.Date(((Date) parametro).getTime()));
				}
				if (parametro instanceof String) {
					logger.debug("--> parametro " + i + " : " + parametro);
					callable.setString(i + 1, (String) parametro);
				}
				if (parametro instanceof Integer) {
					logger.debug("--> parametro " + i + " : " + parametro);
					callable.setInt(i + 1, (Integer) parametro);
				}
			}
			logger.debug("--> Exit callQuery");
			return callable.executeQuery();
		} catch (SQLException e) {
			logger.error("--> errore  nel chiamare la procedure " + procedure, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	@Remove
	public void cleanUp() {
		logger.debug("--> Enter remove");
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			logger.error("--> errore SQLException in remove", e);
		}
		connection = null;
		logger.debug("--> Exit remove");
	}

	@Override
	public void closeConnection() {
		logger.debug("--> Enter remove");
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			logger.error("--> errore SQLException in remove", e);
		}
		connection = null;
		logger.debug("--> Exit remove");
	}

	/**
	 * Esegue una comando sql nel database di Auvend
	 *
	 * @param sql
	 *            sql da eseguire
	 * @return true se il comando è andato a buon fine.
	 */
	public boolean execute(String sql) {
		logger.debug("--> Enter execute");
		if (connection == null) {
			initConnection();
			// throw new IllegalStateException("connessione al database nulla");
		}
		try {
			Statement statement = connection.createStatement();
			return statement.execute(sql);
		} catch (SQLException e) {
			logger.error("--> errore nell'eseguire la query " + sql, e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			logger.error("--> errore nell'eseguire la query " + sql, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public ResultSet executeQuery(String sql) {
		logger.debug("--> Enter executeQuery");
		ResultSet resultSet = null;
		initConnection();
		if (connection == null) {
			throw new IllegalStateException("connessione al database nulla");
		}
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
		} catch (SQLException e) {
			logger.error("--> errore nell'eseguire la query " + sql, e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			logger.error("--> errore nell'eseguire la query " + sql, e);
			throw new RuntimeException(e);
		}
		return resultSet;
	}

	@Override
	public void initConnection() {
		logger.debug("--> Enter initConnection");
		connection = null;
		MBeanServer server = MBeanServerLocator.locateJBoss();
		try {
			// HACK. non si capisce perchè rimangono nel pool delle connessioni. Forzo un flash tramite MBean del pool
			JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
			ManagedConnectionPool mbean = (ManagedConnectionPool) MBeanServerInvocationHandler.newProxyInstance(server,
					new ObjectName("jboss.jca:service=ManagedConnectionPool,name=" + jecPrincipal.getCodiceAzienda()
							+ "AuvendDS"), ManagedConnectionPool.class, false);
			logger.debug("--> pooling preFlush");
			mbean.flush();
			logger.debug("--> pooling postFlush");
			DataSource ds = (DataSource) context.lookup("java:/" + jecPrincipal.getCodiceAzienda() + "AuvendDS");
			if (ds == null) {
				throw new RuntimeException("ds java:/" + jecPrincipal.getCodiceAzienda() + "AuvendDS " + " non trovato");
			}
			connection = ds.getConnection();
		} catch (Exception e) {
			logger.error("--> errore SQLException in initConnection", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit initConnection");
	}
}