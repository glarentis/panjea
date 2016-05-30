package it.eurotn.panjea.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.jdbc.Work;

public class SqlExecuter implements Work {
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
	 * @uml.property name="sql"
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}
}