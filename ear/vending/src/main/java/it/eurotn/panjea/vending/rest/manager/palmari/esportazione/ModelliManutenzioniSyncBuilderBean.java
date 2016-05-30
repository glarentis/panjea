package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ModelliManutenzioniSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.ModelliManutenzioniSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ModelliManutenzioniSyncBuilder")
public class ModelliManutenzioniSyncBuilderBean extends SqlGeneratorBean implements ModelliManutenzioniSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE ModelliManutenzioni(" +
			"Modello nvarchar(20) NOT NULL," +
			"Manutenzione nvarchar(5) NOT NULL," +
			"PRIMARY KEY (Modello,	Manutenzione))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "ModelliManutenzioni";
	}

}
