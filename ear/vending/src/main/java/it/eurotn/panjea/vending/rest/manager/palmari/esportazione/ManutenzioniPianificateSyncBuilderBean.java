package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ManutenzioniPianificateSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.ManutenzioniPianificateSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ManutenzioniPianificateSyncBuilder")
public class ManutenzioniPianificateSyncBuilderBean extends SqlGeneratorBean
		implements ManutenzioniPianificateSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE ManutenzioniPianificate(" +
			"Matricola nvarchar(13) NOT NULL," +
			"Manutenzione nvarchar(13) NOT NULL," +
			"Data_scadenza datetime NULL," +
			"Data_prevista datetime NULL," +
			"Scaduto bit NOT NULL," +
			"PRIMARY KEY (Matricola,Manutenzione))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "ManutenzioniPianificate";
	}

}
