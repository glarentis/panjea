package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.DistributoriInConsegnaSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.DistributoriInConsegnaSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DistributoriInConsegnaSyncBuilder")
public class DistributoriInConsegnaSyncBuilderBean extends SqlGeneratorBean
		implements DistributoriInConsegnaSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE DistributoriInConsegna(" +
			"Progressivo int NOT NULL," +
			"Matricola nvarchar(13) NOT NULL,"  +
			"Modello nvarchar(10) NOT NULL," +
			"Tipo_gestione int NOT NULL," +
			"Ultima_lettura int NOT NULL," +
			"Disponibile bit NOT NULL," +
			"PRIMARY KEY (Progressivo))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "DistributoriInConsegna";
	}

}
