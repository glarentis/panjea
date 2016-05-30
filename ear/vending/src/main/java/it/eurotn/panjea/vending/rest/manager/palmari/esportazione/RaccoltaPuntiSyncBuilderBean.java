package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.RaccoltaPuntiSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.RaccoltaPuntiSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RaccoltaPuntiSyncBuilder")
public class RaccoltaPuntiSyncBuilderBean extends SqlGeneratorBean implements RaccoltaPuntiSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE RaccoltaPunti(" +
			"Raccolta nvarchar(6) NOT NULL," +
			"Descrizione nvarchar(50) NULL," +
			"Data_inizio datetime NULL," +
			"Data_fine datetime NULL," +
			"PRIMARY KEY (Raccolta))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "RaccoltaPunti";
	}

}
