package it.eurotn.panjea.vending.rest.manager.palmari.esportazione;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.interfaces.ModelliSyncBuilder;
import it.eurotn.panjea.vending.rest.manager.palmari.esportazione.sqlgenerator.SqlGeneratorBean;

@Stateless(name = "Panjea.ModelliSyncBuilder")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ModelliSyncBuilder")
public class ModelliSyncBuilderBean extends SqlGeneratorBean implements ModelliSyncBuilder {

	@Override
	protected Map<String, String> getSelectInsertFields() {
		Map<String, String> fields = new TreeMap<String, String>();
        fields.put("v.codice", "Modello");
        fields.put("v.descrizione", "Descrizione");
        fields.put("v.ritiroCassetta", "ObbligaRitiroCassetta");
        return fields;
	}

	@Override
	protected String getSQLCreateTable(String codiceOperatore) {
		return
			"CREATE TABLE Modelli(" +
			"Modello nvarchar(10) NOT NULL," +
			"Descrizione nvarchar(255) NULL," +
			"ObbligaRitiroCassetta bit NOT NULL," +
			"PRIMARY KEY (Modello))";
	}

	@Override
	protected String getSQLSelectRows(String codiceOperatore) {
		return "from vend_modelli v";
	}

	@Override
	protected String getTableName() {
		return "Modelli";
	}

}
