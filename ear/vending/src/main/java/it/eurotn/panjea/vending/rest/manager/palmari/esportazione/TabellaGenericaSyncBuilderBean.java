package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.TabellaGenericaSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.TabellaGenericaSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TabellaGenericaSyncBuilder")
public class TabellaGenericaSyncBuilderBean extends SqlGeneratorBean implements TabellaGenericaSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		return new TreeMap<String, String>();
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return 
			"CREATE TABLE TabellaGenerica(" +
			"Chiave nvarchar(20) NOT NULL," +
			"Progressivo int NOT NULL," +
			"Data_modifica datetime NOT NULL," +
			"ValoreIntero int NULL," +
			"ValoreReale real NULL," +
			"ValoreBooleano bit NULL," +
			"ValoreStringa nvarchar(255) NULL," +
			"ValoreData datetime NULL," +
			"PRIMARY KEY (Chiave,Progressivo))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return null;
	}

	@Override
	protected String getTableName() {
		return "TabellaGenerica";
	}

}
