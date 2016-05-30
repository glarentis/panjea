package it.eurotn.panjea.preventivi.manager.documento;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

public abstract class SpostaRigheDocumentoManager {
	/**
	 * 
	 * @author mattia
	 * 
	 */
	private class SqlExecuter implements Work {
		private String sql;

		@Override
		public void execute(Connection connection) throws SQLException {
			CallableStatement updateStatements = connection.prepareCall(sql);
			updateStatements.executeUpdate();
			updateStatements.close();
		}

		/**
		 * @param sql
		 *            The sql to set.
		 */
		public void setSql(String sql) {
			this.sql = sql;
		}
	}

	private PanjeaDAO panjeaDAO;

	private static Logger logger = Logger.getLogger(SpostaRighePreventivoManager.class);

	private final String classNameRiga;

	private final String propertyAreaDocumentoCollegata;

	private final String tabellaRighe;

	private final String nomeColonnaFKAreaDocumento;

	/**
	 * @param panjeaDAO
	 *            panjeaDAO
	 * @param classNameRiga
	 *            il nome della classe della riga che si vuole ordinare (ad esempio RigaPreventivo)
	 * @param propertyAreaDocumentoCollegata
	 *            il property nella classe della riga che la collega all'area del documento.
	 * @param tabellaRighe
	 *            La tabella contenente le righe da ordinare
	 * @param nomeColonnaFKAreaDocumento
	 *            nome della colonna contenente la foreign key all'area documento corrispondente.
	 */
	public SpostaRigheDocumentoManager(final PanjeaDAO panjeaDAO, final String classNameRiga,
			final String propertyAreaDocumentoCollegata, final String tabellaRighe,
			final String nomeColonnaFKAreaDocumento) {
		this.panjeaDAO = panjeaDAO;
		this.classNameRiga = classNameRiga;
		this.propertyAreaDocumentoCollegata = propertyAreaDocumentoCollegata;
		this.tabellaRighe = tabellaRighe;
		this.nomeColonnaFKAreaDocumento = nomeColonnaFKAreaDocumento;
	}

	/**
	 * @param idAreaDocumento
	 *            id dell'area documento corrispondente
	 */
	private void ordinaRighe(final int idAreaDocumento) {
		SqlExecuter executer = new SqlExecuter();

		// Le righe note automatiche devono sempre andare in fondo
		// ma non vengono ricreate ogni volta come le righe automatiche
		// quindi le metto all'ultimo posto del documento
		StringBuilder sb = new StringBuilder("update " + tabellaRighe + " set ordinamento=");
		sb.append(Double.MAX_VALUE);
		sb.append(" where TIPO_RIGA='N' and rigaAutomatica=1 and " + nomeColonnaFKAreaDocumento + "=");
		sb.append(idAreaDocumento);
		executer.setSql(sb.toString());
		((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);

		executer.setSql("set @row_number=0");
		((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);

		sb = new StringBuilder("update " + tabellaRighe + " set ordinamento=@row_number:=@row_number+1000 where "
				+ nomeColonnaFKAreaDocumento + "=");
		sb.append(idAreaDocumento);
		sb.append(" order by ordinamento");
		executer.setSql(sb.toString());
		((Session) panjeaDAO.getEntityManager().getDelegate()).doWork(executer);
	}

	/**
	 * @param righeDaSpostare
	 *            gli id delle righe da spostare
	 * 
	 * @param idDest
	 *            idDestinazione
	 */
	public void spostaRighe(final Set<Integer> righeDaSpostare, final Integer idDest) {
		logger.debug("--> Enter spostaRighe");
		// Il metodo deve essere performante. genero update dei soli campi interessati
		try {
			// recupero l'ordinamento base e l'area associata. Se idDest esiste, altrimenti significa che le righe
			// devono essere messe in fondo al doc.
			Double ordinamentoBase = 50000000.0;
			if (idDest != null) {
				Query queryOrdinamentoBase = panjeaDAO.prepareQuery("select ro.ordinamento from " + classNameRiga
						+ " ro where ro.id=:idDest");
				queryOrdinamentoBase.setParameter("idDest", idDest);
				ordinamentoBase = (Double) panjeaDAO.getSingleResult(queryOrdinamentoBase);
			}

			// Riordino gli id in base all'ordinamento originale, carico anche l'id dell' area collegata
			// per avere l'id dell'area da riordinare
			Query queryIdRiordinati = panjeaDAO.prepareQuery("select ro.id,ro." + propertyAreaDocumentoCollegata
					+ ".id from " + classNameRiga + " ro where ro.id in(:ids) order by ro.ordinamento desc");
			queryIdRiordinati.setParameter("ids", righeDaSpostare);
			@SuppressWarnings("unchecked")
			List<Object[]> idRiordinati = panjeaDAO.getResultList(queryIdRiordinati);

			Query queryAggiornamento = panjeaDAO.prepareQuery("update " + classNameRiga
					+ " ro set ro.ordinamento=:ordinamento,ro.version=ro.version+1 where ro.id=:idRiga");
			for (Object[] idRiga : idRiordinati) {
				ordinamentoBase--;
				queryAggiornamento.setParameter("ordinamento", ordinamentoBase);
				queryAggiornamento.setParameter("idRiga", idRiga[0]);
				panjeaDAO.executeQuery(queryAggiornamento);
			}
			int idArea = (Integer) idRiordinati.get(0)[1];
			ordinaRighe(idArea);
		} catch (DAOException e) {
			logger.error("-->errore nell'impostare l'ordinamento delle righe", e);
			throw new RuntimeException(e);
		}
		logger.debug("--> Exit spostaRighe");
	}
}
