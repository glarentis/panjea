package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ChiaviDepositoSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.ChiaviDepositoSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ChiaviDepositoSyncBuilder")
public class ChiaviDepositoSyncBuilderBean extends SqlGeneratorBean implements ChiaviDepositoSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE ChiaviDeposito(" +
			"Cliente nvarchar(6) NOT NULL," +
			"Chiave int NOT NULL," +
			"Tipo nvarchar(5) NOT NULL," +
			"Quantita real NULL," + 
			"Sospeso decimal(7, 2) NULL," +
			"PRIMARY KEY (Cliente,Chiave,Tipo))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "ChiaviDeposito";
	}

}
